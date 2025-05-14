package Baglanti.Binance.Islemci;

import Baglanti.Binance.AlimSatimIslemleri;
import Baglanti.Binance.BinanceHesap;
import Indikator.EmaIndikator;
import Strateji.EmaSmaCross;
import Strateji.EmaTrendTakip;
import Strateji.IStrateji;

public class EmaSmaCrossIslemci extends Islemci{

    private BinanceHesap hesap ;
    private String coin ;
    private IStrateji strateji ;
    private AlimSatimIslemleri alimSatimIslemleri;
    long lastTimestamp = 0;
    String timeFrame = "1m";
    boolean longIslemdemiyim = false;
    boolean shortIslemdemiyim = false;
    int kaldirac = 30 ;
    double hesapYuzdesi = 0.04 ;
    double hesapBakiyesi ;
    double longIslemBoyutu ;
    double shortIslemBoyutu ;
    double girisNoktasi ;
    double cikisNoktasi ;
    static int hesapSayisi = 0 ;

    //private EmaIndikator anaTrendEma;

    public EmaSmaCrossIslemci(BinanceHesap hesap , String coin){
        this.hesap = hesap ;
        this.coin = coin ;
        strateji = new EmaSmaCross();
        alimSatimIslemleri = new AlimSatimIslemleri(hesap);
        //anaTrendEma = new EmaIndikator(200);

    }

    @Override
    public void calistir() throws Exception {



        hesap.kaldıracAyarlama(coin, kaldirac);

        hesap.getDataReader().updateMumListForFutures(coin,timeFrame,lastTimestamp);
        if (hesap.getCurrentMumList().getSon() != null) {
            lastTimestamp = hesap.getCurrentMumList().getSon().getZamanDamgasi();
        }



        if(strateji.buy(hesap.getCurrentMumList().getSon())&& !longIslemdemiyim){

            if (shortIslemdemiyim ) {  // short islemde isem islemi kapatiyorum
                //alimSatimIslemleri.cancelShortFuturesPosition(coin, shortIslemBoyutu);
                cikisNoktasi = hesap.getCurrentMumList().getSon().getKapanisFiyati();
                System.out.println("Short Cikis " + cikisNoktasi);
                shortIslemdemiyim= false;
            }


            hesapBakiyesi = hesap.getFuturesBalance("USDT");
            longIslemBoyutu = hesapBakiyesi*hesapYuzdesi*kaldirac;
            //alimSatimIslemleri.futuresLongDolarBazli(coin,longIslemBoyutu);



            longIslemdemiyim = true;
            girisNoktasi = hesap.getCurrentMumList().getSon().getKapanisFiyati();

            System.out.println("Long Giris " + girisNoktasi);

        }



        else if (strateji.sell(hesap.getCurrentMumList().getSon() ) && !shortIslemdemiyim) {

            if (longIslemdemiyim ) {  // Long islemde isem islemi kapatiyorum
                //alimSatimIslemleri.cancelLongFuturesPosition(coin,longIslemBoyutu);
                cikisNoktasi = hesap.getCurrentMumList().getSon().getKapanisFiyati();
                System.out.println("Long Cikis " + cikisNoktasi);
                longIslemdemiyim = false;
            }

            hesapBakiyesi = hesap.getFuturesBalance("USDT");
            shortIslemBoyutu = hesapBakiyesi*hesapYuzdesi*kaldirac;
            //alimSatimIslemleri.futuresShortDolarBazli(coin,shortIslemBoyutu);

            shortIslemdemiyim= true;
            girisNoktasi = hesap.getCurrentMumList().getSon().getKapanisFiyati();

            System.out.println("Short Giris " + girisNoktasi);
        }

        Thread.sleep(500); // 0.5 saniye beklet

    }

}
