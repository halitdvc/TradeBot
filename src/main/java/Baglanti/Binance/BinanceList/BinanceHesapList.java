package Baglanti.Binance.BinanceList;

public class BinanceHesapList {

    private BinanceHesapEleman son ;

    private int hesapSayisi ;


    public BinanceHesapList ( ) {

        son = null ;
        hesapSayisi = 0 ;

    }


    public void sonaEkle ( BinanceHesapEleman hesap ) {
        if(!hesapMevcutMu(hesap)){


            if ( son  == null ) son = hesap ; //  liste bossa


            else { // sona ekleme
                hesap.setOnceki(son);
                son.setSonraki(hesap);
                son = hesap ;

            }
                System.out.println("Hesap Eklendi ... id:" + son.getId());
            hesapSayisi++;
        }
    }

    public boolean hesapMevcutMu(BinanceHesapEleman hesap) {
        BinanceHesapEleman isaretci = son;
        while (isaretci != null) {
            if (isaretci.getId() == hesap.getId()) {
                //System.out.println("Hesap Guncellendi ... id:" + son.getId());
                return true;
            }
            isaretci = isaretci.getOnceki();
        }
        return false;
    }
    public boolean hesapMevcutMuById(int id) {
        BinanceHesapEleman isaretci = son;
        while (isaretci != null) {
            if (isaretci.getId() == id) {
                //System.out.println("Hesap Guncellendi ... id:" + son.getId());
                return true;
            }
            isaretci = isaretci.getOnceki();
        }
        return false;
    }

    public BinanceHesapEleman getSon() {
        return son;
    }

    public void setSon(BinanceHesapEleman son) {
        this.son = son;
    }

    public int getHesapSayisi() {
        return hesapSayisi;
    }

    public void setHesapSayisi(int hesapSayisi) {
        this.hesapSayisi = hesapSayisi;
    }
}
