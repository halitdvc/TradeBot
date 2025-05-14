package Strateji;

import Grafik.Mum;
import Indikator.EmaIndikator;
import Indikator.Indikator;

public class EmaStrategy implements IStrateji {

    private Indikator ema20;
    private Indikator ema50;

    long timestamp = 0 ;   // en son islem yapilan mumum damgasi
    long guncelTimestamp = 1 ;  // surekli guncellenen mum damgasi
    public EmaStrategy() {
        ema20 = new EmaIndikator(20);
        ema50 = new EmaIndikator(50);
    }

    @Override
    public boolean buy(Mum mum) {

        guncelTimestamp = mum.getZamanDamgasi();  // tek mumda hedef gelirse ayni mumda bir daha alim yapmamali

        double ema20Value = ema20.hesapla(mum);
        double ema50Value = ema50.hesapla(mum);
        Mum oncekiMum = mum.getOnceki();

        // Önceki mumun EMA 20'nin üstünde olup olmadığını kontrol eder
        boolean oncekiMumEma20Ustunde = oncekiMum.getEnDusuk() > ema20Value;
        if(ema20Value > ema50Value && oncekiMumEma20Ustunde && mum.getEnDusuk() <= ema20Value && mum.getEnYuksek() >= ema20Value && guncelTimestamp > timestamp) {
            timestamp=mum.getZamanDamgasi();
            return true;
        }
        return false;
    }

    @Override
    public boolean sell(Mum mum) {

        guncelTimestamp = mum.getZamanDamgasi();
        double ema20Value = ema20.hesapla(mum);
        double ema50Value = ema50.hesapla(mum);
        Mum oncekiMum = mum.getOnceki();

        // Önceki mumun EMA 20'nin altında olup olmadığını kontrol eder
        boolean oncekiMumEma20Altinda = oncekiMum.getEnYuksek() < ema20Value;

        if ( ema20Value < ema50Value && oncekiMumEma20Altinda && mum.getEnYuksek() >= ema20Value && mum.getEnDusuk() <= ema20Value && guncelTimestamp > timestamp){
            timestamp=mum.getZamanDamgasi();
            return true;
        }
        return false;
    }
}
