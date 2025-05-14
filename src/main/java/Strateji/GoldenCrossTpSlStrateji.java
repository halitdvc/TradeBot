package Strateji;

import Indikator.*;
import Grafik.Mum;

public class GoldenCrossTpSlStrateji implements IStrateji {


        private SMAIndikator kisaSma;
        private SMAIndikator uzunSma;


        public GoldenCrossTpSlStrateji(   ) {
            kisaSma = new SMAIndikator(50);
            uzunSma = new SMAIndikator(200);
        }



        @Override
        public boolean buy(Mum mum) {
            double kisaSmaDegeri = kisaSma.hesapla(mum);
            double uzunSmaDegeri = uzunSma.hesapla(mum);

            return mum.getOnceki().getKapanisFiyati() <= kisaSmaDegeri && mum.getKapanisFiyati() > kisaSmaDegeri && kisaSmaDegeri > uzunSmaDegeri;
        }

        @Override
        public boolean sell(Mum mum) {
            double kisaSmaDegeri = kisaSma.hesapla(mum);
            double uzunSmaDegeri = uzunSma.hesapla(mum);

            return mum.getOnceki().getKapanisFiyati() >= kisaSmaDegeri && mum.getKapanisFiyati() < kisaSmaDegeri && kisaSmaDegeri < uzunSmaDegeri;
        }
}

