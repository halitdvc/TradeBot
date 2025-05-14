package Baglanti.Binance.Islemci;

import Baglanti.Binance.AlimSatimIslemleri;
import Baglanti.Binance.BinanceHesap;
import Indikator.Indikator;
import Strateji.DipPipFutures;
import Strateji.EmaYutan;
import Strateji.IStrateji;

public class EmaYutanIslemci extends Islemci {


    private BinanceHesap hesap ;
    private String coin ;

    private IStrateji strateji ;
    private AlimSatimIslemleri alimSatimIslemleri;

    long lastTimestamp = 0;

    private String timeFrame = "1m";

    private double kacR = 1.5 ;
    private double riskOrani; // yuzde hesaptan geliyor
    private double islemAraligi ;
    private double birR ;
    private double hesapBakiyesi ;
    private double alimMiktari; // dolar
    private double riskeAtilacakToplamTutar;

    private int kaldirac = 50;

    private double basariliIslem = 0;
    private double basarisizIslem = 0;

    private boolean shortIslemdemiyim = false ;
    private boolean longIslemdemiyim = false ;

    private double girisNoktasi ;
    private double risk ;
    private double tp = 0;
    private double sl = 0;

    public EmaYutanIslemci(BinanceHesap hesap , String coin , double riskOrani) throws Exception {
        this.hesap=hesap;
        this.coin=coin;
        strateji = new EmaYutan();
        alimSatimIslemleri= new AlimSatimIslemleri(hesap);
        this.riskOrani = riskOrani;

    }

    public void calistir () throws Exception {

        hesap.getDataReader().updateMumListForFutures(coin,timeFrame,lastTimestamp);


        if (hesap.getCurrentMumList().getSon() != null) {
            lastTimestamp = hesap.getCurrentMumList().getSon().getZamanDamgasi();
        }
            if (strateji.buy(hesap.getCurrentMumList().getSon()) && !longIslemdemiyim){

                longIslemdemiyim =true;
                girisNoktasi = hesap.getCurrentMumList().getSon().getKapanisFiyati();

                risk = girisNoktasi-hesap.getCurrentMumList().getSon().getOnceki().getOnceki().getEnDusuk();


                tp = risk*kacR + girisNoktasi;
                sl = girisNoktasi - risk ;




                islemAraligi =  Math.abs(tp - sl);
                birR = islemAraligi /(1+kacR);
                hesapBakiyesi = hesap.getFuturesBalance("USDT");
                riskeAtilacakToplamTutar = hesapBakiyesi*riskOrani;
                alimMiktari = (riskeAtilacakToplamTutar/birR) * girisNoktasi;

                if (alimMiktari > 500) { // dusuk hacimli piyada gereksiz buyuk islem acip komisyona para vermeyi onler
                    alimMiktari = hesapBakiyesi*5;
                    tp = tp + birR;
                }
                // Long Islemleri

                hesap.kaldıracAyarlama(coin, kaldirac);


                System.out.println("Long Giris Noktasi ("+coin+"): " + girisNoktasi);
                System.out.println("Risk: " + risk);
                System.out.println("TP: " + tp);
                System.out.println("SL: " + sl);
                System.out.println("1R Degeri: " + birR);
                System.out.println("Hesap Bakiyesi: " + hesapBakiyesi);
                System.out.println("Riske Atilacak Toplam Tutar: " + riskeAtilacakToplamTutar);
                System.out.println("Alim Miktari: " + alimMiktari);

                double[] tpVeSl = alimSatimIslemleri.futuresLongDolarBazli(coin,alimMiktari,sl,tp);

                /////////

                tp = tpVeSl[0];
                sl = tpVeSl[1];


            }

            else if ( strateji.sell(hesap.getCurrentMumList().getSon()) && !shortIslemdemiyim){

                shortIslemdemiyim =true;
                girisNoktasi = hesap.getCurrentMumList().getSon().getKapanisFiyati();

                risk = Math.abs(girisNoktasi-hesap.getCurrentMumList().getSon().getOnceki().getOnceki().getEnYuksek());



                tp = girisNoktasi -risk*kacR;
                sl = girisNoktasi + risk ;
                // Short Islemleri

                islemAraligi =  Math.abs(tp - sl);
                birR = islemAraligi /(1+kacR);
                hesapBakiyesi = hesap.getFuturesBalance("USDT");
                riskeAtilacakToplamTutar = hesapBakiyesi*riskOrani;
                alimMiktari = (riskeAtilacakToplamTutar/birR) * girisNoktasi;
                // Short Islemleri

                if (alimMiktari > 500) { // dusuk hacimli piyada gereksiz buyuk islem acip komisyona para vermeyi onler
                    alimMiktari = hesapBakiyesi*5;
                    tp = tp + birR;
                }
                /////////

                hesap.kaldıracAyarlama(coin, kaldirac);


                System.out.println("Short Giris Noktasi ("+coin+"):" + girisNoktasi);
                System.out.println("Risk: " + risk);
                System.out.println("TP: " + tp);
                System.out.println("SL: " + sl);
                System.out.println("1R Degeri: " + birR);
                System.out.println("Hesap Bakiyesi: " + hesapBakiyesi);
                System.out.println("Riske Atilacak Toplam Tutar: " + riskeAtilacakToplamTutar);
                System.out.println("Alim Miktari: " + alimMiktari);
                System.out.println("---------------------------------------------------------");

                double[] tpVeSl = alimSatimIslemleri.futuresShortDolarBazli(coin,alimMiktari,sl,tp);
                tp = tpVeSl[0];
                sl = tpVeSl[1];

            }


        if (longIslemdemiyim) {
            if (hesap.getCurrentMumList().getSon().getKapanisFiyati() > tp )   {
                basariliIslem++;
                longIslemdemiyim= false;
                hesap.cancelAllOpenFuturesOrders(coin);
                System.out.println(coin+" Long Islem tp ");
                System.out.println("---------------------------------------------------------");
                if ( basariliIslem != 0 && basarisizIslem != 0)
                    System.out.println(coin+" "+(basariliIslem/(basariliIslem+basarisizIslem)));
            }
            if (hesap.getCurrentMumList().getSon().getKapanisFiyati() < sl  ) {
                basarisizIslem++;
                longIslemdemiyim= false;
                hesap.cancelAllOpenFuturesOrders(coin);
                System.out.println(coin+" Long Islem sl ");
                System.out.println("---------------------------------------------------------");
                if ( basariliIslem != 0 && basarisizIslem != 0)
                    System.out.println(coin+" "+(basariliIslem/(basariliIslem+basarisizIslem)));
            }
        }
        else if (shortIslemdemiyim) {
            if (hesap.getCurrentMumList().getSon().getKapanisFiyati() < tp )   {
                basariliIslem++;
                shortIslemdemiyim= false;
                hesap.cancelAllOpenFuturesOrders(coin);
                System.out.println(coin+" Short Islem tp ");
                System.out.println("---------------------------------------------------------");
                if ( basariliIslem != 0 && basarisizIslem != 0)
                    System.out.println(coin+" "+(basariliIslem/(basariliIslem+basarisizIslem)));
            }
            if (hesap.getCurrentMumList().getSon().getKapanisFiyati() > sl  ) {
                basarisizIslem++;
                shortIslemdemiyim= false;
                hesap.cancelAllOpenFuturesOrders(coin);
                System.out.println(coin+" Short Islem sl ");
                System.out.println("---------------------------------------------------------");
                if ( basariliIslem != 0 && basarisizIslem != 0)
                    System.out.println(coin+" "+(basariliIslem/(basariliIslem+basarisizIslem)));
            }
        }





        Thread.sleep(10); // 0.05 saniye beklet



    }












    public void konsolKaydir () {
        for (int i  = 0  ;i < 10 ;i++) System.out.println();
    }

}
