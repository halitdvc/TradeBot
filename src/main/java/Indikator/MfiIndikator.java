package Indikator;

import Grafik.Mum;

public class MfiIndikator extends Indikator {

    private int periyot = 14;

    public MfiIndikator() {

    }

    public MfiIndikator(int periyot) {
        this.periyot = periyot;
    }

    @Override
    public double hesapla(Mum mum) {
        double pozitifAkis = 0;
        double negatifAkis = 0;

        Mum geciciMum = mum;
        for (int i = 0; i < periyot; i++) {
            if (geciciMum == null || geciciMum.getOnceki() == null) break;

            double tipikFiyat = (geciciMum.getEnYuksek() + geciciMum.getEnDusuk() + geciciMum.getKapanisFiyati()) / 3;
            double oncekiTipikFiyat = (geciciMum.getOnceki().getEnYuksek() + geciciMum.getOnceki().getEnDusuk() + geciciMum.getOnceki().getKapanisFiyati()) / 3;

            double akisHacmi = tipikFiyat * geciciMum.getHacim();

            if (tipikFiyat > oncekiTipikFiyat) {
                pozitifAkis += akisHacmi;
            } else if (tipikFiyat < oncekiTipikFiyat) {
                negatifAkis += akisHacmi;
            }

            geciciMum = geciciMum.getOnceki();
        }

        if (negatifAkis == 0) {
            return 100; // Eğer negatif akış yoksa, MFI 100 olarak kabul edilir.
        }

        double mfi = 100 - (100 / (1 + (pozitifAkis / negatifAkis)));
        return mfi;
    }
}
