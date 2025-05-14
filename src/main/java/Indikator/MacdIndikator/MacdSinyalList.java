package Indikator.MacdIndikator;

public class MacdSinyalList {

    private MacdSinyalEleman son ;

    private int elemanSayisi ;

    public MacdSinyalList ( ) {
        son = null ;
        elemanSayisi = 0 ;
    }

    public void sonaEkle ( MacdSinyalEleman eleman ) {

        if ( son  == null ) son = eleman ;
        else {
            System.out.println(elemanYenimi(eleman));
            System.out.println(elemanSayisi);
                if(elemanYenimi(eleman)){
                    eleman.setOnceki(son);
                    son.setSonraki(eleman);
                    son = eleman ;
                    elemanSayisi++;

                }
                else if (son.getOnceki()!=null && elemanVarmi(eleman)){
                    System.out.println("elemani guncelle");
                    sondanSil();
                    sonaEkle(eleman);


                }
        }
    }
    public void sondanSil ( ) {
        son = son.getOnceki();
        son.setSonraki(null);
        elemanSayisi--;

    }
    public boolean elemanVarmi(MacdSinyalEleman eleman){
        return son.getTimeStamp() == eleman.getTimeStamp();
    }

    public boolean elemanYenimi (MacdSinyalEleman eleman){
        return son.getTimeStamp() < eleman.getTimeStamp();
    }

    public MacdSinyalEleman getSon() {
        return son;
    }

    public void setSon(MacdSinyalEleman son) {
        this.son = son;
    }

    public int getElemanSayisi() {
        return elemanSayisi;
    }

    public void setElemanSayisi(int elemanSayisi) {
        this.elemanSayisi = elemanSayisi;
    }
}
