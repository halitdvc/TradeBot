package Indikator;

import Grafik.Mum;

import static Grafik.MumAnaliz.gercekMenzil;

public class AtrIndikator extends Indikator{

    private int periyot = 14 ;

    public AtrIndikator () {
    }

    public AtrIndikator ( int periyot ) {
        this.periyot = periyot ;
    }

    @Override
    public double hesapla(Mum mum) {
        if (mum == null) {
            throw new IllegalArgumentException("Mum null olamaz.");
        }

        double toplamGercekMenzil = 0.0;
        int sayac = 0;

        Mum geciciMum = mum;

        // Periyot için yeterli veri olup olmadığını kontrol et
        for (int i = 0; i < periyot; i++) {
            if (geciciMum == null) {
                throw new IllegalArgumentException("Yeterli Mum verisi yok.");
            }
            geciciMum = geciciMum.getOnceki();
        }

        while (mum != null && sayac < periyot) {
            toplamGercekMenzil += gercekMenzil( mum);
            mum = mum.getOnceki();
            sayac++;
        }

        return toplamGercekMenzil / sayac;
    }
}
