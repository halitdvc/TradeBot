package Baglanti.Binance.Islemci;

import Baglanti.Binance.AlimSatimIslemleri;
import Baglanti.Binance.BinanceHesap;
import Strateji.AlphaTrendStratejisi;
import Strateji.IStrateji;


public class AlphaTrendIslemci extends Islemci {

    private BinanceHesap hesap ;
    private String coin ;
    private IStrateji strateji ;
    private AlimSatimIslemleri alimSatimIslemleri;
    long lastTimestamp = 0;
    String timeFrame = "1d";
    boolean longIslemdemiyim = false;
    boolean shortIslemdemiyim = false;
    int kaldirac = 125 ;
    double hesapYuzdesi = 0.05 ;
    double hesapBakiyesi ;
    double longIslemBoyutu ;
    double shortIslemBoyutu ;
    double girisNoktasi ;
    double cikisNoktasi ;

    public AlphaTrendIslemci(BinanceHesap hesap , String coin ) {
        this.hesap = hesap ;
        this.coin = coin ;
        strateji = new AlphaTrendStratejisi();
        alimSatimIslemleri = new AlimSatimIslemleri(hesap);
    }

    public void calistir() throws Exception {

        hesap.kaldıracAyarlama(coin, kaldirac);
        
        hesap.getDataReader().updateMumListForFutures(coin, timeFrame, lastTimestamp);
        if (hesap.getCurrentMumList().getSon() != null) {
            lastTimestamp = hesap.getCurrentMumList().getSon().getZamanDamgasi();
        }
        
        // EmaTrendTakipIslemci'deki gibi, 
        // alım/satım şartları AlphaTrendStratejisi tarafından belirlenebilir.

        if(strateji.buy(hesap.getCurrentMumList().getSon()) && !longIslemdemiyim) {
            if (shortIslemdemiyim ) {  // short islemde isem islemi kapatiyorum
                alimSatimIslemleri.cancelShortFuturesPosition(coin, shortIslemBoyutu);
                cikisNoktasi = hesap.getCurrentMumList().getSon().getKapanisFiyati();
                System.out.println("Short Cikis "  + coin + "  " + cikisNoktasi);
                shortIslemdemiyim= false;
            }


            hesapBakiyesi = hesap.getFuturesBalance("USDT");
            longIslemBoyutu = hesapBakiyesi*hesapYuzdesi*kaldirac;
            alimSatimIslemleri.futuresLongDolarBazli(coin,longIslemBoyutu);



            longIslemdemiyim = true;
            girisNoktasi = hesap.getCurrentMumList().getSon().getKapanisFiyati();

            System.out.println("Long Giris " + coin + "  " + girisNoktasi);
        }
        else if (strateji.sell(hesap.getCurrentMumList().getSon() ) && !shortIslemdemiyim) {

            if (longIslemdemiyim ) {  // Long islemde isem islemi kapatiyorum
                alimSatimIslemleri.cancelLongFuturesPosition(coin,longIslemBoyutu);
                cikisNoktasi = hesap.getCurrentMumList().getSon().getKapanisFiyati();
                System.out.println("Long Cikis "  + coin + "  " + cikisNoktasi);
                longIslemdemiyim = false;
            }

            hesapBakiyesi = hesap.getFuturesBalance("USDT");
            shortIslemBoyutu = hesapBakiyesi*hesapYuzdesi*kaldirac;
            alimSatimIslemleri.futuresShortDolarBazli(coin,shortIslemBoyutu);

            shortIslemdemiyim= true;
            girisNoktasi = hesap.getCurrentMumList().getSon().getKapanisFiyati();

            System.out.println("Short Giris " + coin + "  "  + girisNoktasi);
        }
        Thread.sleep(500); // 0.5 saniye beklet
    }
}