package Indikator;

import Grafik.Mum;

public class RsiIndikator extends Indikator{

    private int periyot = 14;

    public RsiIndikator (){

    }

    public  RsiIndikator ( int periyot ) {
        this.periyot = periyot ;
    }

    @Override
    public double hesapla(Mum mum) {
        double ortalamaKazanc = 0;
        double ortalamaKayip = 0;

        for(int i = 0; i< periyot;i++){
            if(mum.getOnceki()==null|| mum == null) break;
            double degisim = mum.getKapanisFiyati()-mum.getOnceki().getKapanisFiyati();

            if(degisim > 0)ortalamaKazanc += degisim;
            else ortalamaKayip-=degisim;

            mum=mum.getOnceki();
        }

        ortalamaKazanc /= periyot;
        ortalamaKayip /= periyot;

        if (ortalamaKayip == 0) { //0 a bölme hatası sakat
            return 100;
        }

        double rs = ortalamaKazanc/ortalamaKayip;
        double rsi = 100-(100 / (1+rs));
        return rsi;
    }
}
