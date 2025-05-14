package Indikator;

import Grafik.Mum;

public class SMAIndikator extends Indikator {
    private int periyot;

    public SMAIndikator(int periyot) {
        this.periyot = periyot;
    }

    @Override
    public double hesapla(Mum mum) {
        double toplam = 0.0;
        int count = 0;

        while (mum != null && count < periyot) {
            toplam += mum.getKapanisFiyati();
            mum = mum.getOnceki();
            count++;
        }

        return toplam / count;
    }

}
