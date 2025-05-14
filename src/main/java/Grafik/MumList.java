package Grafik;

public class MumList {

    private Mum son ;

    private int mumSayisi ;

    public MumList ( ) {
        son = null ;
        mumSayisi = 0 ;
    }

       public void sonaEkle ( Mum mum ) {
          if ( son  == null ) son = mum ;
          else {
              mum.setOnceki(son);
              son.setSonraki(mum);
              son = mum ;

          }
          mumSayisi++;
          }


       /*   public void sonaEkle(Mum mum) {
              if (son == null) {
                  son = mum; // İlk mum olduğunda direkt ekleme
              } else {
                  // Heikin Ashi hesaplamaları
                  double kapanis = (mum.getAcilisFiyati() + mum.getKapanisFiyati() + mum.getEnYuksek() + mum.getEnDusuk()) / 4;
                  double acilis = (son.getAcilisFiyati() + son.getKapanisFiyati()) / 2;
                  double enYuksek = mum.getEnYuksek();
                  double enDusuk = mum.getEnDusuk();

                  Mum heikinAshiMum = new Mum(acilis, kapanis, enDusuk, enYuksek, mum.getZamanDamgasi(),mum.getHacim());

                  // Heikin Ashi mumunu bağlama
                  heikinAshiMum.setOnceki(son);
                  son.setSonraki(heikinAshiMum);
                  son = heikinAshiMum;
              }
              mumSayisi++;
          }*/


    public void sondanSil ( ) {
        son = son.getOnceki();
        son.setSonraki(null);
        mumSayisi--;

    }
    public Mum getSon() {
        return son;
    }

    public void setSon(Mum son) {
        this.son = son;
    }

    public int getMumSayisi() {
        return mumSayisi;
    }

    public void setMumSayisi(int mumSayisi) {
        this.mumSayisi = mumSayisi;
    }
}
