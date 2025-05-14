package Strateji;

import Grafik.Mum;
import Indikator.SuperTrend.SuperTrendIndikator;
import Indikator.SuperTrend.SuperTrendValue;

public class SuperTrendStrateji implements IStrateji {
    private SuperTrendIndikator superTrendIndikator;

    public SuperTrendStrateji(int periyot, double faktor) {
        this.superTrendIndikator = new SuperTrendIndikator(periyot, faktor);
    }

    @Override
    public boolean buy(Mum mum) {
        SuperTrendValue superTrendValue = superTrendIndikator.hesapla(mum);
        double superTrend = superTrendValue.getSuperTrend();
        double direction = superTrendValue.getDirection();
        return direction == 1.0 && mum.getOnceki().getKapanisFiyati() > superTrend;
    }

    @Override
    public boolean sell(Mum mum) {
        SuperTrendValue superTrendValue = superTrendIndikator.hesapla(mum);
        double superTrend = superTrendValue.getSuperTrend();
        double direction = superTrendValue.getDirection();
        return direction == -1.0 && mum.getOnceki().getKapanisFiyati() < superTrend;
    }
}
