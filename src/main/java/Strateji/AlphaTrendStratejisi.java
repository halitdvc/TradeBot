package Strateji;

import Grafik.Mum;
import Indikator.AtrIndikator;
import Indikator.RsiIndikator;

import java.util.ArrayList;
public class AlphaTrendStratejisi implements IStrateji {

    private double coeff;
    private AtrIndikator atrIndikator;
    private RsiIndikator rsiIndikator;
    private double alphaTrend;
    ArrayList<Double> alphaTrendValues = new ArrayList<Double>();

    long guncelTimestamp = 1; // Sürekli güncellenen mum damgasi
    long timestamp = 0; // En son hesaplama yapılan mumun damgasi

    public AlphaTrendStratejisi( ) {
        coeff = 2;
        atrIndikator = new AtrIndikator(25);
        rsiIndikator = new RsiIndikator(25);
    }

    private void hesaplaAlphaTrend(Mum mum) {
        guncelTimestamp = mum.getZamanDamgasi();  // surekli guncellenen mum damgasi
        if(guncelTimestamp > timestamp) {
            double upT = hesaplaUpT(mum);
            double downT = hesaplaDownT(mum);
            double oncekiAlphaTrend = alphaTrendValues.isEmpty() ? 0.0 : alphaTrendValues.get(alphaTrendValues.size() - 1);
            double rsiHesapla= rsiIndikator.hesapla(mum);

            if (rsiHesapla > 50) {
                if(upT < oncekiAlphaTrend) {
                    alphaTrend = oncekiAlphaTrend;
                } else {
                    alphaTrend = upT;
                }
            } else {
                if(downT>oncekiAlphaTrend) {
                    alphaTrend = oncekiAlphaTrend;
                } else {
                    alphaTrend = downT;
                }
            }
            System.out.println("alphaTrend: " + alphaTrend);
            alphaTrendValues.add(alphaTrend);

            // saving only 1000 most recent trend values
            if (alphaTrendValues.size() > 1000) {
                alphaTrendValues.remove(0);
            }
            timestamp = guncelTimestamp; // en son hesaplama yapılan mumun damgasi
        }
    }

    private boolean crossOver() {
        int currentIndex = alphaTrendValues.size() - 1;
        return currentIndex >= 2 && alphaTrendValues.get(currentIndex) > alphaTrendValues.get(currentIndex - 2);
    }

    private boolean crossUnder() {
        int currentIndex = alphaTrendValues.size() - 1;
        return currentIndex >= 2 && alphaTrendValues.get(currentIndex) < alphaTrendValues.get(currentIndex - 2);
    }

    @Override
    public boolean buy(Mum mum) {
        hesaplaAlphaTrend(mum);
        return crossOver();
    }

    @Override
    public boolean sell(Mum mum) {
        hesaplaAlphaTrend(mum);
        return crossUnder();
    }

    private double hesaplaUpT(Mum mum) {
        return mum.getEnDusuk() - atrIndikator.hesapla(mum) * coeff;
    }

    private double hesaplaDownT(Mum mum) {
        return mum.getEnYuksek() + atrIndikator.hesapla(mum) * coeff;
    }
}
