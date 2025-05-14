package Strateji;

import Grafik.Mum;
import Indikator.EmaIndikator;
import Indikator.Indikator;

public class EmaCrossStrateji implements IStrateji {

    private EmaIndikator kisaEmaBig;
    private EmaIndikator uzunEmaBig;

    private EmaIndikator kisaEmaSmall;
    private EmaIndikator uzunEmaSmall;

    private Mum ustTimeFrameMum ;



    public EmaCrossStrateji( int kisaPeriod, int uzunPeriod , Mum ustTimeFrameMum ) {

        this.kisaEmaBig = new EmaIndikator( kisaPeriod ) ;
        this.uzunEmaBig = new EmaIndikator( uzunPeriod ) ;

        this.kisaEmaSmall = new EmaIndikator( kisaPeriod ) ;
        this.uzunEmaSmall = new EmaIndikator( uzunPeriod ) ;
        this.ustTimeFrameMum = ustTimeFrameMum ;
    }
    @Override
    public boolean buy(Mum mum) {

        return kisaEmaBig.hesapla(ustTimeFrameMum) > uzunEmaBig.hesapla(ustTimeFrameMum)
                && kisaEmaSmall.hesapla(mum) > uzunEmaSmall.hesapla(mum);
    }

    @Override
    public boolean sell(Mum mum) {
        return kisaEmaBig.hesapla(ustTimeFrameMum) < uzunEmaBig.hesapla(ustTimeFrameMum)
                && kisaEmaSmall.hesapla(mum) > uzunEmaSmall.hesapla(mum);
    }

}
