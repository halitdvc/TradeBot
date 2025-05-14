package Strateji;

import Grafik.Mum;
import Grafik.MumRengi;
import Indikator.EmaIndikator;

import static Grafik.MumAnaliz.*;

public class EmaYutan implements IStrateji{

    private EmaIndikator emaKisa;
    private EmaIndikator emaUzun;
    private EmaIndikator emaTrend;

    long timestamp = 0 ;   // en son islem yapilan mumum damgasi
    long guncelTimestamp = 1 ;  // surekli guncellenen mum damgasi

    public EmaYutan (){
        emaKisa = new EmaIndikator(20);
        emaUzun = new EmaIndikator(50);
        emaTrend = new EmaIndikator(200);
    }



    @Override
    public boolean buy(Mum mum) {

        mum=mum.getOnceki();  // kapanmis muma gecis yapildi

        guncelTimestamp = mum.getSonraki().getZamanDamgasi();  // tek mumda hedef gelirse ayni mumda bir daha alim yapmamali

        boolean isTrendBuy = emaKisa.hesapla(mum) > emaUzun.hesapla(mum) && emaUzun.hesapla(mum)>emaTrend.hesapla(mum);  // kapanmis muma gore trend hesaplandi.


        if  ( isTrendBuy && bogaYutan(mum) && (formasyonYukselen1(mum,emaKisa) || formasyonYukselen1(mum,emaUzun))
                && guncelTimestamp > timestamp) {

            timestamp=mum.getSonraki().getZamanDamgasi();
            System.out.println("---------------------------------------------------------");



            return true;
        }
        else return false;
    }

    @Override
    public boolean sell(Mum mum) {
        mum=mum.getOnceki();  // kapanmis muma gecis yapildi

        guncelTimestamp = mum.getSonraki().getZamanDamgasi();  // tek mumda hedef gelirse ayni mumda bir daha alim yapmamali

        boolean isTrendSell = emaKisa.hesapla(mum) < emaUzun.hesapla(mum)&& emaUzun.hesapla(mum)<emaTrend.hesapla(mum);  // kapanmis muma gore trend hesaplandi.


        if  ( isTrendSell && ayiYutan(mum) && (formasyonDusen1(mum,emaKisa) || formasyonDusen2(mum,emaUzun))
                && guncelTimestamp > timestamp) {

            timestamp=mum.getSonraki().getZamanDamgasi();
            System.out.println("---------------------------------------------------------");



            return true;
        }
        else return false;
    }

    public boolean formasyonYukselen1 (Mum mum ,EmaIndikator ema){
        return      mum.getEnDusuk()<ema.hesapla(mum) &&  mum.getKapanisFiyati()>ema.hesapla(mum) && (mumRengiHesapla(mum.getOnceki().getOnceki())== MumRengi.KIRMIZI  || mumRengiHesapla(mum.getOnceki().getOnceki().getOnceki())== MumRengi.KIRMIZI);
    }
    public boolean formasyonYukselen2 (Mum mum ,EmaIndikator ema){
        return      mum.getOnceki().getEnDusuk()<ema.hesapla(mum) &&  mum.getKapanisFiyati()>ema.hesapla(mum) && (mumRengiHesapla(mum.getOnceki().getOnceki())== MumRengi.KIRMIZI  || mumRengiHesapla(mum.getOnceki().getOnceki().getOnceki())== MumRengi.KIRMIZI);
    }

    public boolean formasyonDusen1 (Mum mum ,EmaIndikator ema){
        return      mum.getEnYuksek()>ema.hesapla(mum) &&  mum.getKapanisFiyati()<ema.hesapla(mum) && (mumRengiHesapla(mum.getOnceki().getOnceki())== MumRengi.YESIL  || mumRengiHesapla(mum.getOnceki().getOnceki().getOnceki())== MumRengi.YESIL);
    }
    public boolean formasyonDusen2 (Mum mum ,EmaIndikator ema){
        return      mum.getOnceki().getEnYuksek()>ema.hesapla(mum) &&  mum.getKapanisFiyati()<ema.hesapla(mum) && (mumRengiHesapla(mum.getOnceki().getOnceki())== MumRengi.YESIL  || mumRengiHesapla(mum.getOnceki().getOnceki().getOnceki())== MumRengi.YESIL);
    }
}
