package Strateji;

import Grafik.Mum;
import Indikator.RsiIndikator;
import Indikator.EmaIndikator;
import Indikator.Indikator;
import Indikator.MacdIndikator.MACDIndikator;

public class EmaMacdAtr implements IStrateji{

    private Indikator ema ;
    private Indikator macd ;
    private Indikator rsi;


    private int emaPeriyot = 200 ;
    private int macdKisa = 12 ;
    private int macdUzun = 26 ;
    private int macdSinyal = 15 ;

    long timestamp = 0 ;   // en son islem yapilan mumum damgasi
    long guncelTimestamp = 1 ;  // surekli guncellenen mum damgasi

    public EmaMacdAtr () {
        ema = new EmaIndikator(emaPeriyot);
        rsi = new RsiIndikator();
        macd = new MACDIndikator(macdKisa,macdUzun,macdSinyal);
    }

    @Override
    public boolean buy(Mum mum) {
        mum = mum.getOnceki();  //  kapanmis muma gecildi

        guncelTimestamp = mum.getSonraki().getZamanDamgasi();  // tek mumda hedef gelirse ayni mumda bir daha alim yapmamali

        boolean isTrendBuy = mum.getKapanisFiyati() > ema.hesapla(mum);  // trend yukari yonlumu
        boolean isMacdBuy = macd.hesapla(mum) > 0 && macd.hesapla(mum.getOnceki())<0 ;
        boolean isRsiBuy = rsi.hesapla(mum) > 50 ;

        System.out.println("''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''");

        if ( isMacdBuy && isTrendBuy && isRsiBuy && guncelTimestamp > timestamp){
            timestamp=mum.getSonraki().getZamanDamgasi();
            return true;
            }

        return false;
    }

    @Override
    public boolean sell(Mum mum) {
        mum= mum.getOnceki();

        guncelTimestamp = mum.getSonraki().getZamanDamgasi();  // tek mumda hedef gelirse ayni mumda bir daha alim yapmamali


        boolean isTrendSell = mum.getKapanisFiyati() < ema.hesapla(mum);  // trend asagi yonlumu
        boolean isMacdSell = macd.hesapla(mum) < 0 && macd.hesapla(mum.getOnceki())>0 ;
        boolean isRsiSell = rsi.hesapla(mum) < 50 ;

        if ( isMacdSell &&  isTrendSell && isRsiSell && guncelTimestamp > timestamp) {
            timestamp = mum.getSonraki().getZamanDamgasi();
            return true;
        }
        return false;
    }
}
