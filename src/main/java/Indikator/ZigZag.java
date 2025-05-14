package Indikator;

import Grafik.Mum;


public class ZigZag extends Indikator {

    private int depth;
    private double deviation;
    private int backstep;

    public ZigZag(int depth, double deviation, int backstep) {
        this.depth = depth;
        this.deviation = deviation;
        this.backstep = backstep;
    }

    public double hesapla(Mum mum) {
        Mum tepeMum = mum;
        Mum dipMum = mum;

        for (int i = 0; i < depth && mum != null; i++) {
            if (mum.getEnYuksek() > tepeMum.getEnYuksek()) {
                tepeMum = mum;
            }

            if (mum.getEnDusuk() < dipMum.getEnDusuk()) {
                dipMum = mum;
            }

            mum = mum.getOnceki();
        }

        if (tepeMum != null && tepeMum.getKapanisFiyati() - dipMum.getKapanisFiyati() > deviation) {
            System.out.println("Tepe bulundu! Fiyat: " + tepeMum.getEnYuksek() + ", Zaman: " + tepeMum.getZamanDamgasi());
        } if (dipMum != null) {
            System.out.println("Dip bulundu! Fiyat: " + dipMum.getEnDusuk() + ", Zaman: " + dipMum.getZamanDamgasi());
        }

        return 0;
    }
}
