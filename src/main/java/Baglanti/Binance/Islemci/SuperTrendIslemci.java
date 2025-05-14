package Baglanti.Binance.Islemci;

import Baglanti.Binance.AlimSatimIslemleri;
import Baglanti.Binance.BinanceHesap;
import Grafik.Mum;
import Indikator.SuperTrend.SuperTrendIndikator;
import Strateji.IStrateji;
import Strateji.SuperTrendStrateji;

public class SuperTrendIslemci extends Islemci {
    private BinanceHesap hesap;
    private String coin;
    private IStrateji strateji;
    private AlimSatimIslemleri alimSatimIslemleri;
    private long lastTimestamp = 0;
    private String timeFrame = "15m";
    private boolean longIslemdemiyim = false;
    private boolean shortIslemdemiyim = false;
    private int kaldirac = 30;
    private double hesapYuzdesi = 0.04;
    private double hesapBakiyesi;
    private double longIslemBoyutu;
    private double shortIslemBoyutu;
    private double girisNoktasi;
    private double cikisNoktasi;

    public SuperTrendIslemci(BinanceHesap hesap, String coin) {
        this.hesap = hesap;
        this.coin = coin;
        this.strateji = new SuperTrendStrateji(10, 3); // SuperTrend indikatör periyot ve faktör
        this.alimSatimIslemleri = new AlimSatimIslemleri(hesap);
    }

    public void calistir() throws Exception {

        hesap.kaldıracAyarlama(coin, kaldirac);
        hesap.getDataReader().updateMumListForFutures(coin, timeFrame, lastTimestamp);
        if (hesap.getCurrentMumList().getSon() != null) {
            lastTimestamp = hesap.getCurrentMumList().getSon().getZamanDamgasi();
        }

        Mum sonMum = hesap.getCurrentMumList().getSon();

        if (strateji.buy(sonMum) && !longIslemdemiyim) {

            if (shortIslemdemiyim) { // short işlemdeysem işlemi kapatıyorum
                alimSatimIslemleri.cancelShortFuturesPosition(coin, shortIslemBoyutu);
                cikisNoktasi = sonMum.getKapanisFiyati();
                System.out.println("Short Cikis " + cikisNoktasi);
                shortIslemdemiyim = false;
            }

            hesapBakiyesi = hesap.getFuturesBalance("USDT");
            longIslemBoyutu = hesapBakiyesi * hesapYuzdesi * kaldirac;
           // alimSatimIslemleri.futuresLongDolarBazli(coin, longIslemBoyutu);

            longIslemdemiyim = true;
            girisNoktasi = sonMum.getKapanisFiyati();

            System.out.println("Long Giris " + girisNoktasi);

        } else if (strateji.sell(sonMum) && !shortIslemdemiyim) {

            if (longIslemdemiyim) { // Long işlemdeysem işlemi kapatıyorum
                alimSatimIslemleri.cancelLongFuturesPosition(coin, longIslemBoyutu);
                cikisNoktasi = sonMum.getKapanisFiyati();
                System.out.println("Long Cikis " + cikisNoktasi);
                longIslemdemiyim = false;
            }

            hesapBakiyesi = hesap.getFuturesBalance("USDT");
            shortIslemBoyutu = hesapBakiyesi * hesapYuzdesi * kaldirac;
           // alimSatimIslemleri.futuresShortDolarBazli(coin, shortIslemBoyutu);

            shortIslemdemiyim = true;
            girisNoktasi = sonMum.getKapanisFiyati();

            System.out.println("Short Giris " + girisNoktasi);
        }

        Thread.sleep(500); // 0.5 saniye beklet
    }
}

