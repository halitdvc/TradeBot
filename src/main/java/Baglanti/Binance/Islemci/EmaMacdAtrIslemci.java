package Baglanti.Binance.Islemci;

import Baglanti.Binance.AlimSatimIslemleri;
import Baglanti.Binance.BinanceHesap;
import Indikator.AtrIndikator;
import Indikator.Indikator;
import Strateji.IStrateji;

public class EmaMacdAtrIslemci extends Islemci{


    private BinanceHesap hesap ;
    private String coin ;

    private Indikator atr;
    private IStrateji strateji ;
    private AlimSatimIslemleri alimSatimIslemleri;

    long lastTimestamp = 0;

    private String timeFrame = "15m";

    private double kacR = 2 ;
    private double riskOrani; // yuzde hesaptan geliyor
    private double islemAraligi ;
    private double birR ;
    private double hesapBakiyesi ;
    private double alimMiktari; // dolar
    private double riskeAtilacakToplamTutar;

    private int kaldirac = 20;

    private double basariliIslem = 0;
    private double basarisizIslem = 0;

    private boolean shortIslemdemiyim = false ;
    private boolean longIslemdemiyim = false ;

    private double girisNoktasi ;
    private double risk ;
    private double tp = 0;
    private double sl = 0;

    public EmaMacdAtrIslemci(BinanceHesap hesap , String coin , double riskOrani){
        this.hesap=hesap;
        this.coin=coin;

        atr = new AtrIndikator();
        strateji = new Strateji.EmaMacdAtr();
        alimSatimIslemleri= new AlimSatimIslemleri(hesap);
        this.riskOrani = riskOrani;
    }


    @Override
    public void calistir() throws Exception {
        hesap.getDataReader().updateMumListForFutures(coin,timeFrame,lastTimestamp);

        if (hesap.getCurrentMumList().getSon() != null) {
            lastTimestamp = hesap.getCurrentMumList().getSon().getZamanDamgasi();
        }

        if (strateji.buy(hesap.getCurrentMumList().getSon()) && !longIslemdemiyim){

            longIslemdemiyim =true;
            girisNoktasi = hesap.getCurrentMumList().getSon().getKapanisFiyati();
            // risk = girisNoktasi-hesap.getCurrentMumList().getSon().getOnceki().getEnDusuk();
            risk = Math.abs(atr.hesapla( hesap.getCurrentMumList().getSon()));

            tp = risk*kacR + girisNoktasi;
            sl = girisNoktasi - risk ;

            islemAraligi =  Math.abs(tp - sl);
            birR = islemAraligi /(1+kacR);
            hesapBakiyesi = hesap.getFuturesBalance("USDT");
            riskeAtilacakToplamTutar = hesapBakiyesi*riskOrani;
            alimMiktari = (riskeAtilacakToplamTutar/birR) * girisNoktasi;
            // Long Islemleri

            hesap.kaldıracAyarlama(coin, kaldirac);
            System.out.println("---------------------------------------------------------");

            System.out.println("Giris Noktasi: " + girisNoktasi);
            System.out.println("Risk: " + risk);
            System.out.println("TP: " + tp);
            System.out.println("SL: " + sl);
            System.out.println("1R Degeri: " + birR);
            System.out.println("Hesap Bakiyesi: " + hesapBakiyesi);
            System.out.println("Riske Atilacak Toplam Tutar: " + riskeAtilacakToplamTutar);
            System.out.println("Alim Miktari: " + alimMiktari);
            System.out.println("---------------------------------------------------------");
            alimSatimIslemleri.futuresLongDolarBazli(coin,alimMiktari,sl,tp);

        }
        else if ( strateji.sell(hesap.getCurrentMumList().getSon()) && !shortIslemdemiyim){

            shortIslemdemiyim =true;
            girisNoktasi = hesap.getCurrentMumList().getSon().getKapanisFiyati();
            // risk = Math.abs(girisNoktasi-hesap.getCurrentMumList().getSon().getOnceki().getEnYuksek());
            risk = Math.abs(atr.hesapla( hesap.getCurrentMumList().getSon()));

            tp = girisNoktasi -risk*kacR;
            sl = girisNoktasi + risk ;
            // Short Islemleri

            islemAraligi =  Math.abs(tp - sl);
            birR = islemAraligi /(1+kacR);
            hesapBakiyesi = hesap.getFuturesBalance("USDT");
            riskeAtilacakToplamTutar = hesapBakiyesi*riskOrani;
            alimMiktari = (riskeAtilacakToplamTutar/birR) * girisNoktasi;
            // Short Islemleri

            // futures islem kodlari

            /////////

            hesap.kaldıracAyarlama(coin, kaldirac);
            System.out.println("---------------------------------------------------------");

            System.out.println("Short Giris Noktasi: " + girisNoktasi);
            System.out.println("Risk: " + risk);
            System.out.println("TP: " + tp);
            System.out.println("SL: " + sl);
            System.out.println("1R Degeri: " + birR);
            System.out.println("Hesap Bakiyesi: " + hesapBakiyesi);
            System.out.println("Riske Atilacak Toplam Tutar: " + riskeAtilacakToplamTutar);
            System.out.println("Alim Miktari: " + alimMiktari);
            System.out.println("---------------------------------------------------------");

            alimSatimIslemleri.futuresShortDolarBazli(coin,alimMiktari,sl,tp);


        }
        if (longIslemdemiyim) {
            if (hesap.getCurrentMumList().getSon().getKapanisFiyati() >= tp )   {
                basariliIslem++;
                longIslemdemiyim= false;
                hesap.cancelAllOpenFuturesOrders(coin);
                System.out.println(" Long Islem tp ");
                if ( basariliIslem != 0 && basarisizIslem != 0)
                    System.out.println(basariliIslem/(basariliIslem+basarisizIslem));
            }
            if (hesap.getCurrentMumList().getSon().getKapanisFiyati() <= sl  ) {
                basarisizIslem++;
                longIslemdemiyim= false;
                hesap.cancelAllOpenFuturesOrders(coin);
                System.out.println(" Long Islem sl ");
                if ( basariliIslem != 0 && basarisizIslem != 0)
                    System.out.println(basariliIslem/(basariliIslem+basarisizIslem));
            }
        }
        else if (shortIslemdemiyim) {
            if (hesap.getCurrentMumList().getSon().getKapanisFiyati() <= tp )   {
                basariliIslem++;
                shortIslemdemiyim= false;
                hesap.cancelAllOpenFuturesOrders(coin);
                System.out.println(" Short Islem tp ");
                if ( basariliIslem != 0 && basarisizIslem != 0)
                    System.out.println(basariliIslem/(basariliIslem+basarisizIslem));
            }
            if (hesap.getCurrentMumList().getSon().getKapanisFiyati() >= sl  ) {
                basarisizIslem++;
                shortIslemdemiyim= false;
                hesap.cancelAllOpenFuturesOrders(coin);
                System.out.println(" Short Islem sl ");
                if ( basariliIslem != 0 && basarisizIslem != 0)
                    System.out.println(basariliIslem/(basariliIslem+basarisizIslem));
            }
        }

        Thread.sleep(5000); // 0.5 saniye beklet

    }
}
