package Indikator.MacdIndikator;

import Grafik.Mum;
import Indikator.Indikator;
import Indikator.EmaIndikator;

public class MACDIndikator extends Indikator {
    private int kisaPeriyot= 12;
    private int uzunPeriyot = 26;
    private int sinyalPeriyot = 9;

    private EmaIndikator kisaEma;
    private EmaIndikator uzunEma;

    private MacdSinyalList sinyalList;
    private MacdEmaHesaplayici macdEmaHesaplayici;

    public MACDIndikator(int kisaPeriyot, int uzunPeriyot, int sinyalPeriyot) {
        this.kisaPeriyot = kisaPeriyot;
        this.uzunPeriyot = uzunPeriyot;
        this.sinyalPeriyot = sinyalPeriyot;

        kisaEma = new EmaIndikator(kisaPeriyot);
        uzunEma = new EmaIndikator(uzunPeriyot);

        sinyalList= new MacdSinyalList();
        macdEmaHesaplayici = new MacdEmaHesaplayici(sinyalPeriyot);
    }
    public MACDIndikator () {
        kisaEma = new EmaIndikator(kisaPeriyot);
        uzunEma = new EmaIndikator(uzunPeriyot);

        sinyalList= new MacdSinyalList();
        macdEmaHesaplayici = new MacdEmaHesaplayici(sinyalPeriyot);
    }

    @Override
    public double hesapla(Mum mum) {
        if (mum == null) {
            throw new IllegalArgumentException("Mum null olamaz.");
        }
        if ( sinyalList.getSon() == null) DegerleriGuncelle(mum);

        // Kısa ve Uzun EMA hesaplama
        double fastEma =  kisaEma.hesapla(mum);
        double slowEma = uzunEma.hesapla(mum);
        double macd =fastEma- slowEma;



        sinyalList.sonaEkle(new MacdSinyalEleman(macd,mum.getZamanDamgasi()));

        double signal = macdEmaHesaplayici.hesapla(sinyalList.getSon());

        System.out.println( " fastEma : " +fastEma);
        System.out.println( " slowEma : " +slowEma);
        System.out.println( " macd : " + macd);
        System.out.println( " signal : " + signal);
        System.out.println( " histogram : " + (macd-signal));
        System.out.println(mum.getSonraki().getKapanisFiyati());

        return macd - signal;
    }


    void DegerleriGuncelle(Mum mum) {
        Mum isaretci = mum;
        for( int i = 0 ; i<sinyalPeriyot ; i++){
            isaretci =isaretci.getOnceki();
        }
        double macd;
        while ( isaretci.getSonraki() != null){
            double fastEma =  kisaEma.hesapla(isaretci);
            double slowEma = uzunEma.hesapla(isaretci);
            macd =fastEma- slowEma;

            sinyalList.sonaEkle(new MacdSinyalEleman(macd, mum.getZamanDamgasi()));

            isaretci=isaretci.getSonraki();
        }

    }
}

