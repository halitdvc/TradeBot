package Strateji;

import Grafik.Mum;
import Indikator.EmaIndikator;
import Indikator.SMAIndikator;

public class EmaSmaCross implements IStrateji {

    private EmaIndikator sma;
    private EmaIndikator ema;

    private int PERIYOT = 20;
    private int ANATRENDPERIYOT = 200;

    private EmaIndikator anaTrendEma;

    private boolean longIslemdemiyim ; // Başlangıçta işlem yok
    private boolean shortIslemdemiyim ;

    public EmaSmaCross() {
        this.sma = new EmaIndikator(PERIYOT);
        this.ema = new EmaIndikator(PERIYOT);
        this.anaTrendEma = new EmaIndikator(ANATRENDPERIYOT);
        this.longIslemdemiyim = false;
        this.shortIslemdemiyim = false;
    }

    @Override
    public boolean buy(Mum mum) {
        if (!longIslemdemiyim && !shortIslemdemiyim) { // İslemsizsem
            System.out.printf(" Buy --> ema > sma : %b \n",ema.hesapla(mum.getOnceki()) > sma.hesapla(mum.getOnceki().getOnceki().getOnceki().getOnceki().getOnceki().getOnceki()));
            System.out.printf(" Buy --> close > ema200 : %b \n",mum.getKapanisFiyati() > anaTrendEma.hesapla(mum));
            System.out.printf(" Buy --> \n",ema.hesapla(mum.getOnceki()) > sma.hesapla(mum.getOnceki().getOnceki().getOnceki().getOnceki().getOnceki().getOnceki()) && (mum.getKapanisFiyati() > anaTrendEma.hesapla(mum)));
            System.out.printf(" Buy --> ema %f \n",ema.hesapla(mum.getOnceki()));
            System.out.printf(" Buy --> sma %f \n",sma.hesapla(mum.getOnceki().getOnceki().getOnceki().getOnceki().getOnceki().getOnceki()));

            if (ema.hesapla(mum.getOnceki()) > sma.hesapla(mum.getOnceki().getOnceki().getOnceki().getOnceki().getOnceki().getOnceki()) && mum.getKapanisFiyati() > anaTrendEma.hesapla(mum)) {
                longIslemdemiyim = true; // İşlem açıldı
                return true;
            }
        } else if ( shortIslemdemiyim) {
            if (ema.hesapla(mum.getOnceki()) < sma.hesapla(mum.getOnceki().getOnceki().getOnceki().getOnceki().getOnceki().getOnceki())) {
                shortIslemdemiyim = false;
                return true;
            }
        } return false;
    }

    @Override
    public boolean sell(Mum mum) {
        System.out.printf(" Sell --> ema < sma : %b \n",ema.hesapla(mum.getOnceki()) < sma.hesapla(mum.getOnceki().getOnceki().getOnceki().getOnceki().getOnceki().getOnceki()));
        System.out.printf(" Sell --> close < ema200 : %b \n",mum.getKapanisFiyati() < anaTrendEma.hesapla(mum));
        System.out.printf(" Buy --> \n",ema.hesapla(mum.getOnceki()) < sma.hesapla(mum.getOnceki().getOnceki().getOnceki().getOnceki().getOnceki().getOnceki()) && mum.getKapanisFiyati() < anaTrendEma.hesapla(mum));
        System.out.printf(" Sell --> ema %f \n",ema.hesapla(mum.getOnceki()));
        System.out.printf(" Sell --> sma %f \n",sma.hesapla(mum.getOnceki().getOnceki().getOnceki().getOnceki().getOnceki().getOnceki()));
        if (!longIslemdemiyim && !shortIslemdemiyim) { // İslemsizsem
            if (ema.hesapla(mum.getOnceki()) < sma.hesapla(mum.getOnceki().getOnceki().getOnceki().getOnceki().getOnceki().getOnceki()) && mum.getKapanisFiyati() < anaTrendEma.hesapla(mum)) {
                shortIslemdemiyim = true; // İşlem açıldı
                return true;
            }
        } else if ( longIslemdemiyim) {
            if (ema.hesapla(mum.getOnceki()) > sma.hesapla(mum.getOnceki().getOnceki().getOnceki().getOnceki().getOnceki().getOnceki())) {
                longIslemdemiyim = false;
                return true;
            }
        } return false;
    }
}
