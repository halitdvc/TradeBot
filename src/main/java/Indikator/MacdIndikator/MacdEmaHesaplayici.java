package Indikator.MacdIndikator;

public class MacdEmaHesaplayici {


        private int periyot;

        public MacdEmaHesaplayici(int periyot) {
            this.periyot = periyot;
        }


        public double hesapla(MacdSinyalEleman eleman) {
            if (eleman == null) {
                return 0.0;
            }

            double carpici = 2.0 / (periyot + 1);
            double ema = smaHesapla(eleman, periyot); // İlk değeri SMA olarak belirle

            for (int i = 1; i < periyot; i++) {
                eleman = eleman.getOnceki();
                if (eleman == null) {
                    break;
                }
                ema = (eleman.getDeger() - ema) * carpici + ema;
            }

            return ema;
        }

        private double smaHesapla(MacdSinyalEleman eleman, int periyot) {
            double toplam = 0.0;
            int count = 0;

            while (eleman != null && count < periyot) {
                toplam += eleman.getDeger();
                eleman = eleman.getOnceki();
                count++;
            }

            return toplam / count;
        }

    }


