package HalitDeveci;

import Baglanti.Binance.BinanceHesap;
import Grafik.GrafikAnaliz;
import Grafik.Mum;
import Indikator.ZigZag;
import Grafik.PiyasaTrendi;
import Indikator.Indikator;
import Utils.EnvReader;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        EnvReader env= new EnvReader();
        final String API_KEY = env.get("API_KEY");
        final String API_SECRET = env.get("SECRET_KEY");

            long lastTimestamp = 0;

            Indikator zigzag = new ZigZag(12,5,2);

        try {
            BinanceHesap binanceHesap = new BinanceHesap(API_KEY, API_SECRET);

            while (true) { // Sürekli bir döngü
                // Spot verisi güncelleme
                konsolTemizle();
                System.out.println("Spot verisi güncelleniyor...");
                binanceHesap.getDataReader().updateMumListForSpot("BTCUSDT", "15m", lastTimestamp);

                // Mevcut mum listesini al ve lastTimestamp değerini güncelle
                if (binanceHesap.getCurrentMumList().getSon() != null) {
                    lastTimestamp = binanceHesap.getCurrentMumList().getSon().getZamanDamgasi();
                }

                Mum sonMum = binanceHesap.getCurrentMumList().getSon();
               // zigzag.hesapla(sonMum);


                // Piyasa trend analizi
                if (sonMum != null && sonMum.getOnceki() != null) {
                    PiyasaTrendi trend = GrafikAnaliz.piyasaAnalizi(sonMum.getOnceki());
                    trendMesajiYazdir(trend,sonMum.getKapanisFiyati());
                } else {
                    System.out.println("Gerekli mum verisi eksik.");
                }

                System.out.println(" Mum Sayisi " + binanceHesap.getCurrentMumList().getMumSayisi());

                try {

                    Thread.sleep(5000); // 5 saniye (dakikada bir) beklet
                } catch (InterruptedException e) {
                    e.printStackTrace();
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


    public static void trendMesajiYazdir(PiyasaTrendi trend, double kapanisFiyati) {
        switch (trend) {
            case YUKSELEN_TEPE:
                System.out.println("Yükselen tepe bulundu! Kapanış fiyatı: " + kapanisFiyati);
                break;
            case YUKSELEN_DIP:
                System.out.println("Yükselen dip bulundu! Kapanış fiyatı: " + kapanisFiyati);
                break;
            case ALCALAN_TEPE:
                System.out.println("Alçalan tepe bulundu! Kapanış fiyatı: " + kapanisFiyati);
                break;
            case ALCALAN_DIP:
                System.out.println("Alçalan dip bulundu! Kapanış fiyatı: " + kapanisFiyati);
                break;
            // Diğer trendler için de benzer şekilde case ekleyebilirsiniz.
            default:
                System.out.println("Belirgin bir trend yok. Kapanış fiyatı: " + kapanisFiyati);
                break;
        }
    }
    public static void konsolTemizle () {
        for (int i  = 0  ;i < 10 ;i++) System.out.println();
    }

}
