package Indikator.MacdIndikator;

public class MacdSinyalEleman {
    private double deger;
    private long timeStamp ;

    private MacdSinyalEleman onceki;
    private MacdSinyalEleman sonraki;

    public MacdSinyalEleman(double deger ,long timeStamp) {
        this.deger = deger;
        this.timeStamp=timeStamp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public double getDeger() {
        return deger;
    }

    public MacdSinyalEleman getOnceki() {
        return onceki;
    }

    public void setOnceki(MacdSinyalEleman onceki) {
        this.onceki = onceki;
    }

    public MacdSinyalEleman getSonraki() {
        return sonraki;
    }

    public void setSonraki(MacdSinyalEleman sonraki) {
        this.sonraki = sonraki;
    }
}
