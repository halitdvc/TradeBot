package Strateji;

import Grafik.Mum;
import Indikator.EmaIndikator;
import Indikator.Indikator;




public class DipPipFutures implements IStrateji{

    private Indikator kisaEma;
    private Indikator uzunEma;


    long timestamp = 0 ;   // en son islem yapilan mumum damgasi
    long guncelTimestamp = 1 ;  // surekli guncellenen mum damgasi

    public DipPipFutures () {

        kisaEma = new EmaIndikator(6) ;
        uzunEma = new EmaIndikator(18) ;
    }
    @Override
    public boolean buy(Mum mum) {

        mum = mum.getOnceki();

        mum=mum.getOnceki();

        guncelTimestamp = mum.getSonraki().getZamanDamgasi();  // tek mumda hedef gelirse ayni mumda bir daha alim yapmamali !

        boolean isTrendBuy = kisaEma.hesapla(mum.getSonraki()) > uzunEma.hesapla(mum.getSonraki());






        if  ( isTrendBuy && (mum.getSonraki().getKapanisFiyati()>kisaEma.hesapla(mum.getSonraki())) && mum.getSonraki().getEnYuksek() > mum.getEnYuksek()                                                                  // high > high[1]
                                    && mum.getOnceki().getEnYuksek()>mum.getEnYuksek()                                                                                                                                      // high[2] > high[1]
                                    && mum.getOnceki().getOnceki().getEnYuksek()>mum.getOnceki().getEnYuksek()                                                                                                              // high[3] > high[2]
                                    //&& mum.getOnceki().getOnceki().getOnceki().getEnYuksek() > mum.getOnceki().getOnceki().getEnYuksek()                                                                                    // high[4] > high[3]
                                    && mum.getSonraki().getEnDusuk() > mum.getEnDusuk()                                                                                                                                     // low[1] < low
                                    && guncelTimestamp > timestamp) {

            timestamp=mum.getSonraki().getZamanDamgasi();
            System.out.println("---------------------------------------------------------");

            System.out.println("Ema 6 :  "+kisaEma.hesapla(mum.getSonraki()));
            System.out.println("Ema 18:  "+uzunEma.hesapla(mum.getSonraki()));

            return true;
        }
        else return false;

    }
    @Override
    public boolean sell(Mum mum) {
        mum=mum.getOnceki();
        mum=mum.getOnceki();

        guncelTimestamp = mum.getSonraki().getZamanDamgasi();

        boolean isTrendSell = kisaEma.hesapla(mum.getSonraki()) < uzunEma.hesapla(mum.getSonraki());




        if  ( isTrendSell && (mum.getSonraki().getKapanisFiyati()<kisaEma.hesapla(mum.getSonraki()))  && mum.getSonraki().getEnDusuk() < mum.getEnDusuk()                                               // low < low[1]
                && mum.getOnceki().getEnDusuk()<mum.getEnDusuk()                                                                                                                                        // low[2] < low[1]
                && mum.getOnceki().getOnceki().getEnDusuk()<mum.getOnceki().getEnDusuk()                                                                                                                // low[3] < low[2]
                //&& mum.getOnceki().getOnceki().getOnceki().getEnDusuk() < mum.getOnceki().getOnceki().getEnDusuk()                                                                                      // low[4] < low[3]
                && mum.getSonraki().getEnYuksek() < mum.getEnYuksek()                                                                                                                                   // high[1] > high
                && guncelTimestamp > timestamp) {

            timestamp=mum.getSonraki().getZamanDamgasi();
            System.out.println("---------------------------------------------------------");

            System.out.println("Ema 6 :  "+kisaEma.hesapla(mum.getSonraki()));
            System.out.println("Ema 18:  "+uzunEma.hesapla(mum.getSonraki()));

            return true;
        }
        else  return false;
    }
}
