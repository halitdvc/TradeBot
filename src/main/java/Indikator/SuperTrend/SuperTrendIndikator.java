package Indikator.SuperTrend;

import Grafik.Mum;
import Indikator.AtrIndikator;

public class SuperTrendIndikator {

    private int periyot;
    private double faktor;
    private AtrIndikator atrIndikator;

    public SuperTrendIndikator(int periyot, double faktor) {
        this.periyot = periyot;
        this.faktor = faktor;
        this.atrIndikator = new AtrIndikator(periyot);
    }

    public SuperTrendValue hesapla(Mum mum) {
        if (mum == null) {
            throw new IllegalArgumentException("Mum null olamaz.");
        }

        double atr = atrIndikator.hesapla(mum);
        double superTrend = 0.0;
        double direction = 1.0;

        double upperBand = 0.0;
        double lowerBand = 0.0;
        double prevUpperBand = 0.0;
        double prevLowerBand = 0.0;

        Mum currentMum = mum;
        for (int i = 0; i < periyot; i++) {
            if (currentMum == null) {
                throw new IllegalArgumentException("Yeterli Mum verisi yok.");
            }

            double averagePrice = (currentMum.getEnYuksek() + currentMum.getEnDusuk()) / 2;
            upperBand = averagePrice + faktor * atr;
            lowerBand = averagePrice - faktor * atr;

            if (i > 0) {
                prevUpperBand = upperBand;
                prevLowerBand = lowerBand;

                if (currentMum.getKapanisFiyati() > prevUpperBand) {
                    direction = 1.0;
                    superTrend = lowerBand;
                } else if (currentMum.getKapanisFiyati() < prevLowerBand) {
                    direction = -1.0;
                    superTrend = upperBand;
                }
            }
            currentMum = currentMum.getOnceki();
        }

        return new SuperTrendValue(superTrend, direction);
    }
}
