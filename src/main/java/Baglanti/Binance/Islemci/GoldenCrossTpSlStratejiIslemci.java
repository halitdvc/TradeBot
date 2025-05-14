
package Baglanti.Binance.Islemci;

import Baglanti.Binance.AlimSatimIslemleri;
import Baglanti.Binance.BinanceHesap;
import Indikator.Indikator;
import Indikator.AtrIndikator;

import Strateji.GoldenCrossTpSlStrateji;
import Strateji.IStrateji;

public class GoldenCrossTpSlStratejiIslemci extends Islemci {

    private BinanceHesap hesap ;
    private String coin ;

    private Indikator atr;
    private IStrateji strateji ;
    private AlimSatimIslemleri alimSatimIslemleri;

    long lastTimestamp = 0;

    private String timeFrame = "5m";

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

    public GoldenCrossTpSlStratejiIslemci(BinanceHesap hesap , String coin , double riskOrani) throws Exception {
        this.hesap=hesap;
        this.coin=coin;

        atr = new AtrIndikator();
        strateji = new GoldenCrossTpSlStrateji();
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
            alimMiktari = riskeAtilacakToplamTutar/birR;
            // Long Islemleri

            hesap.kaldıracAyarlama(coin, kaldirac);

            System.out.println("Long Giris " + girisNoktasi);
            System.out.println( " tp : " + tp);
            System.out.println( " sl : " + sl);
            System.out.println("Miktar :"+alimMiktari);
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
            alimMiktari = riskeAtilacakToplamTutar/birR; ;
            // Short Islemleri

            // futures islem kodlari

            /////////

            hesap.kaldıracAyarlama(coin, kaldirac);
            System.out.println("Short Giris " + girisNoktasi);
            System.out.println( " tp : " + tp);
            System.out.println( " sl : " + sl);
            System.out.println("Miktar :"+alimMiktari);
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

        Thread.sleep(500); // 0.5 saniye beklet

    }

    public void konsolKaydir () {
        for (int i  = 0  ;i < 10 ;i++) System.out.println();
    }

}
