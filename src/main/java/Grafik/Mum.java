package Grafik;

public class Mum {
    private double acilisFiyati ;
    private double kapanisFiyati ;
    private double enYuksek ;
    private double enDusuk ;
    private long zamanDamgasi ;




    private double hacim ;


    private Mum sonraki ;
    private Mum onceki ;

    public Mum (double acilisFiyati , double kapanisFiyati , double enDusuk , double enYuksek , long zamanDamgasi , double hacim  ) {
        this.acilisFiyati=acilisFiyati;
        this.enDusuk=enDusuk;
        this.enYuksek=enYuksek;
        this.kapanisFiyati=kapanisFiyati;
        this.zamanDamgasi=zamanDamgasi;
        this.hacim=hacim;

    }

    public double getAcilisFiyati() {
        return acilisFiyati;
    }


    public double getKapanisFiyati() {
        return kapanisFiyati;
    }


    public double getEnYuksek() {
        return enYuksek;
    }


    public double getEnDusuk() {
        return enDusuk;
    }


    public long getZamanDamgasi() {
        return zamanDamgasi;
    }


    public Mum getSonraki() {
        return sonraki;
    }

    public void setSonraki(Mum sonraki) {
        this.sonraki = sonraki;
    }

    public Mum getOnceki() {
        return onceki;
    }

    public void setOnceki(Mum onceki) {
        this.onceki = onceki;
    }

    public double getHacim() {
        return hacim;
    }




}
