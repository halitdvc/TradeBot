package HalitDeveci;

import Baglanti.Binance.AlimSatimIslemleri;
import Baglanti.Binance.BinanceHesap;
import Indikator.Indikator;
import Indikator.EmaIndikator;
import Indikator.AtrIndikator;
import Strateji.DipPipFutures;
import Strateji.IStrateji;
import Utils.EnvReader;

import javax.crypto.SecretKey;
import java.io.IOException;

public class BotMainKaldiracli {
    public static void main(String[] args) throws IOException {
        EnvReader env= new EnvReader();
        final String API_KEY = env.get("API_KEY");
        final String API_SECRET = env.get("SECRET_KEY");

        System.out.println(API_KEY +"    "+ API_SECRET);
        long lastTimestamp = 0;
        long lastTimestampUstTime = 0;

        Indikator kucukEma = new EmaIndikator(6);
        Indikator buyukEma = new EmaIndikator(18);
        Indikator atr = new AtrIndikator();

        IStrateji strateji = new DipPipFutures();

        String sembol = "ADAUSDT";
        String timeFrame = "15m";
        String ustTimeFrame = "1h";

        double kacR = 1.5 ;
        double riskOrani = 0.02 ; // yuzde 2
        double islemAraligi ;
        double birR ;
        double hesapBakiyesi ;
        double alimMiktari; // dolar
        double riskeAtilacakToplamTutar;

        int kaldirac = 50;




        double basariliIslem = 0;
        double basarisizIslem = 0;

        boolean shortIslemdemiyim = false ;
        boolean longIslemdemiyim = false ;


        double girisNoktasi ;
        double risk ;
        double tp = 0;
        double sl = 0;


        try {
            BinanceHesap binanceHesap = new BinanceHesap(API_KEY,API_SECRET);

            AlimSatimIslemleri alimSatimIslemleri = new AlimSatimIslemleri(binanceHesap) ;

            while ( true ) {
                konsolKaydir();

                System.out.println(" Spot Verisi Guncelleniyor ");

                System.out.println(" Mum sayisi : " + binanceHesap.getUstTimeFrameList().getMumSayisi());

                //binanceHesap.getDataReader().updateMumListForSpot(sembol,timeFrame, lastTimestamp);
                binanceHesap.getDataReader().updateMumListForFutures(sembol,timeFrame,lastTimestamp);

                //binanceHesap.getDataReader().ustTimeFrameListGuncelleSpot(sembol,ustTimeFrame,lastTimestampUstTime);
                binanceHesap.getDataReader().ustTimeFrameListGuncelleFutures(sembol,ustTimeFrame,lastTimestampUstTime);
                if (binanceHesap.getCurrentMumList().getSon() != null) {
                    lastTimestamp = binanceHesap.getCurrentMumList().getSon().getZamanDamgasi();
                }
                if (binanceHesap.getUstTimeFrameList().getSon() != null) {
                    lastTimestampUstTime = binanceHesap.getUstTimeFrameList().getSon().getZamanDamgasi();
                }
                if (kucukEma.hesapla(binanceHesap.getUstTimeFrameList().getSon()) >buyukEma.hesapla(binanceHesap.getUstTimeFrameList().getSon()))
                System.out.println("TREND BUY");
                else System.out.println("TREND SELL");

                System.out.println(" ATR : " + atr.hesapla(binanceHesap.getCurrentMumList().getSon()));

                if (kucukEma.hesapla(binanceHesap.getUstTimeFrameList().getSon())>buyukEma.hesapla(binanceHesap.getUstTimeFrameList().getSon())){
                    // Ust Trend Buy

                    if (strateji.buy(binanceHesap.getCurrentMumList().getSon()) && !longIslemdemiyim){

                        longIslemdemiyim =true;
                        girisNoktasi = binanceHesap.getCurrentMumList().getSon().getKapanisFiyati();
                        risk = girisNoktasi-binanceHesap.getCurrentMumList().getSon().getOnceki().getEnDusuk();

                        tp = risk*kacR + girisNoktasi;
                        sl = girisNoktasi - risk ;

                        islemAraligi =  Math.abs(tp - sl);
                        birR = islemAraligi /(1+kacR);
                        hesapBakiyesi = binanceHesap.getFuturesBalance("USDT");
                        riskeAtilacakToplamTutar = hesapBakiyesi*riskOrani;
                        alimMiktari = riskeAtilacakToplamTutar/birR;


                        // Long Islemleri

                        binanceHesap.kaldıracAyarlama(sembol, kaldirac);

                        System.out.println("Giris " + girisNoktasi);
                        System.out.println( " tp : " + tp);
                        System.out.println( " sl : " + sl);
                        System.out.println("Miktar :"+alimMiktari);
                        alimSatimIslemleri.futuresLongDolarBazli(sembol,alimMiktari,sl,tp);
                        // futures islem kodlari

                        /////////




                    }


                }
                else {
                    // Ust Trend Sell
                    if ( strateji.sell(binanceHesap.getCurrentMumList().getSon()) && !shortIslemdemiyim){

                        shortIslemdemiyim =true;
                        girisNoktasi = binanceHesap.getCurrentMumList().getSon().getKapanisFiyati();
                        risk = Math.abs(girisNoktasi-binanceHesap.getCurrentMumList().getSon().getOnceki().getEnYuksek());

                        tp = girisNoktasi -risk*kacR;
                        sl = girisNoktasi + risk ;
                        // Short Islemleri

                        islemAraligi =  Math.abs(tp - sl);
                        birR = islemAraligi /(1+kacR);
                        hesapBakiyesi = binanceHesap.getFuturesBalance("USDT");
                        riskeAtilacakToplamTutar = hesapBakiyesi*riskOrani;
                        alimMiktari = riskeAtilacakToplamTutar/birR;
                        // futures islem kodlari

                        /////////

                        binanceHesap.kaldıracAyarlama(sembol, kaldirac);
                        System.out.println("Giris " + girisNoktasi);
                        System.out.println( " tp : " + tp);
                        System.out.println( " sl : " + sl);
                        System.out.println("Miktar :"+alimMiktari);
                        alimSatimIslemleri.futuresShortDolarBazli(sembol,alimMiktari,sl,tp);


                    }
                }

                if (longIslemdemiyim) {
                 if (binanceHesap.getCurrentMumList().getSon().getKapanisFiyati() >= tp )   {
                     basariliIslem++;
                     longIslemdemiyim= false;
                     binanceHesap.cancelAllOpenFuturesOrders(sembol);
                     System.out.println(" Long Islem tp ");
                 }
                 if (binanceHesap.getCurrentMumList().getSon().getKapanisFiyati() <= sl  ) {
                     basarisizIslem++;
                     longIslemdemiyim= false;
                     binanceHesap.cancelAllOpenFuturesOrders(sembol);
                     System.out.println(" Long Islem sl ");
                 }
                }
                else if (shortIslemdemiyim) {
                    if (binanceHesap.getCurrentMumList().getSon().getKapanisFiyati() <= tp )   {
                        basariliIslem++;
                        shortIslemdemiyim= false;
                        binanceHesap.cancelAllOpenFuturesOrders(sembol);
                        System.out.println(" Short Islem tp ");
                    }
                    if (binanceHesap.getCurrentMumList().getSon().getKapanisFiyati() >= sl  ) {
                        basarisizIslem++;
                        shortIslemdemiyim= false;
                        binanceHesap.cancelAllOpenFuturesOrders(sembol);
                        System.out.println(" Short Islem sl ");
                    }
                }
                if ( basariliIslem != 0 && basarisizIslem != 0)
                    System.out.println(basariliIslem/(basariliIslem+basarisizIslem));




                Thread.sleep(1000); // 0.5 saniye beklet



            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static void konsolKaydir () {
        for (int i  = 0  ;i < 10 ;i++) System.out.println();
    }
}


