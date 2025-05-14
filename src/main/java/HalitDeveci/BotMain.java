package HalitDeveci;

import Baglanti.Binance.AlimSatimIslemleri;
import Baglanti.Binance.BinanceHesap;
import Indikator.EmaIndikator;
import Indikator.Indikator;
import Strateji.DipPipFarkliTarz;
import Strateji.IStrateji;
import Utils.EnvReader;

import java.io.IOException;

public class BotMain {
    public static void main(String[] args) throws IOException {
        EnvReader env= new EnvReader();
        final String API_KEY = env.get("API_KEY");
        final String API_SECRET = env.get("SECRET_KEY");

        long lastTimestamp = 0;
        long lastTimestampUstTime = 0;

        Indikator kucukEma = new EmaIndikator(6);
        Indikator buyukEma = new EmaIndikator(18);

        IStrateji strateji = new DipPipFarkliTarz();

        String sembol = "BTCUSDT";
        String timeFrame = "15m";
        String ustTimeFrame = "1h";

        double alimMiktari = 30; // dolar


        double alimFiyati = 0;
        double satimFiyati = 0;

        double basariliIslem = 0;
        double basarisizIslem = 0;




        try {
            BinanceHesap binanceHesap = new BinanceHesap( API_KEY , API_SECRET);

            AlimSatimIslemleri alimSatimIslemleri = new AlimSatimIslemleri(binanceHesap);

            while ( true ) { // surekli guncelleme

                konsolKaydir();

                System.out.println(" Spot Verisi Guncelleniyor ");

                System.out.println(" Mum sayisi : " + binanceHesap.getUstTimeFrameList().getMumSayisi());

                binanceHesap.getDataReader().updateMumListForSpot(sembol,timeFrame, lastTimestamp);

                binanceHesap.getDataReader().ustTimeFrameListGuncelleSpot(sembol,ustTimeFrame,lastTimestampUstTime);

                // Mevcut mum listesini al ve lastTimestamp değerini güncelle
                if (binanceHesap.getCurrentMumList().getSon() != null) {
                    lastTimestamp = binanceHesap.getCurrentMumList().getSon().getZamanDamgasi();
                }
                if (binanceHesap.getUstTimeFrameList().getSon() != null) {
                    lastTimestampUstTime = binanceHesap.getUstTimeFrameList().getSon().getZamanDamgasi();
                }
                System.out.println("kucuk ema : "+kucukEma.hesapla(binanceHesap.getUstTimeFrameList().getSon()));
                System.out.println("buyuk ema : "+buyukEma.hesapla(binanceHesap.getUstTimeFrameList().getSon()));
                //alim satim kontrolu
                if (kucukEma.hesapla(binanceHesap.getUstTimeFrameList().getSon())>buyukEma.hesapla(binanceHesap.getUstTimeFrameList().getSon())){

                    // kucuk ema buyuk emadan buyukse alim yonunde bakilmali
                    if ( strateji.buy(binanceHesap.getCurrentMumList().getSon())){
                        // alim yapiyorum
                        System.out.println("ALDIM");
                        alimSatimIslemleri.spotAlimDolarBazli(sembol,alimMiktari);
                        alimFiyati = binanceHesap.getCurrentMumList().getSon().getKapanisFiyati();
                    }
                    else if ( strateji.sell(binanceHesap.getCurrentMumList().getSon())){
                        // satiyorum
                        System.out.println("SATTIM");
                        alimSatimIslemleri.spotSatimDolarBazli(sembol,alimMiktari);
                        satimFiyati = binanceHesap.getCurrentMumList().getSon().getKapanisFiyati();


                        if (  satimFiyati > alimFiyati){
                            basariliIslem++ ;
                            System.out.println("Islem TP Alim : " + alimFiyati +" Satim : " + satimFiyati);
                        }
                        else {
                            basarisizIslem++;
                            System.out.println("Islem SL Alim : " + alimFiyati +" Satim : " + satimFiyati);

                        }

                    }

                    if ( basariliIslem != 0 && basarisizIslem != 0)
                        System.out.println(basariliIslem/(basariliIslem+basarisizIslem));


                    try {

                        Thread.sleep(2000); // 2 saniye beklet
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }

        } catch (IOException e) {
            System.out.println("Veri alma hatası: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Bir hata meydana geldi: " + e.getMessage());
            e.printStackTrace();
        }



    }


    public static void konsolKaydir () {
        for (int i  = 0  ;i < 10 ;i++) System.out.println();
    }
}
