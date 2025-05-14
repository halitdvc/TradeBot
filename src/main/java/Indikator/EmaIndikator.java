package Indikator;
import Grafik.Mum;

public class EmaIndikator extends Indikator {

    private int periyot;

    public EmaIndikator(int periyot) {
        this.periyot = periyot;
    }

    @Override
    public double hesapla(Mum mum) {
        if (mum == null) {
            return 0.0;
        }

        double carpici = 2.0 / (periyot + 1);
        double ema = smaHesapla(mum, periyot); // İlk değeri SMA olarak belirle

        for (int i = 1; i < periyot; i++) {
            mum = mum.getOnceki();
            if (mum == null) {
                break;
            }
            ema = (mum.getKapanisFiyati() - ema) * carpici + ema;
        }

        return ema;
    }

    public double smaHesapla(Mum mum, int periyot) {
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
