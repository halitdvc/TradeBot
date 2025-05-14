package Strateji;

import Grafik.Mum;
import Indikator.EmaIndikator;

import static Grafik.MumAnaliz.*;

public class DipPipFarkliTarz implements IStrateji{

    private EmaIndikator kisaEma;
    private EmaIndikator uzunEma;

    boolean geriCekilme = false ;
    boolean islemdemiyim = false ;

    boolean tepeYapildimi = false ;

    double girisNoktasi ;
    double risk ;
    double hedef ;



    public DipPipFarkliTarz ( ) {
        kisaEma = new EmaIndikator(6) ;
        uzunEma = new EmaIndikator(18) ;
    }


    @Override
    public boolean buy(Mum mum) {
        // Alim Kosullari
        mum=mum.getOnceki();

        boolean isTrendBuy = kisaEma.hesapla(mum.getSonraki()) > uzunEma.hesapla(mum.getSonraki()) ;


        if (tepeMi(mum.getOnceki())){
            tepeYapildimi = true ; // gericekilme baslamis

        }
        if ( tepeYapildimi) System.out.println("geri cekilme bekleniyor");


        if (!isTrendBuy ) tepeYapildimi =false;// 15 dk lik trend bozulmadan saatlik trend bozulursa burasi degismiyor eksik !!

        if  (  isTrendBuy && !islemdemiyim && tepeYapildimi && yukselenMumMu(mum.getSonraki())) { // trend sinyali geldi
            girisNoktasi = mum.getSonraki().getKapanisFiyati();
            System.out.println("Giris noktasi : " + girisNoktasi);
            risk = girisNoktasi - mum.getEnDusuk();
            System.out.println("SL : " + mum.getEnDusuk());
            hedef = risk*1.5 + girisNoktasi;
            System.out.println("TP : " + hedef );
            islemdemiyim = true;
            return true;
        }
        else return false;


    }

    @Override
    public boolean sell(Mum mum) {
        if ( mum.getKapanisFiyati() >= hedef && islemdemiyim) { // hedef gelirse
            tepeYapildimi = false ;
            islemdemiyim = false ;
            return true;
        }
        if ( mum.getEnDusuk()< girisNoktasi-risk&& islemdemiyim){  // stop olursak
            tepeYapildimi = false ;
            islemdemiyim = false ;
            return true;
        }
        else return false;
    }
}
