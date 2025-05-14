package Strateji;

import Grafik.Mum;
import Indikator.EmaIndikator;


public class EmaTrendTakip implements IStrateji {
    EmaIndikator ema ;


    public EmaTrendTakip(int periyot) {
        ema = new EmaIndikator(periyot);

    }




    @Override
    public boolean buy(Mum mum) {

        return mum.getOnceki().getKapanisFiyati()>ema.hesapla(mum.getOnceki());
    }

    @Override
    public boolean sell(Mum mum) {
        return mum.getOnceki().getKapanisFiyati()<ema.hesapla(mum.getOnceki());
    }
}
