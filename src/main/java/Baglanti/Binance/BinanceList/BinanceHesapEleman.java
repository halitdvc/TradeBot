package Baglanti.Binance.BinanceList;

import Baglanti.Binance.BinanceHesap;
import Baglanti.Binance.Islemci.*;
import Strateji.EmaTrendTakip;

public class BinanceHesapEleman {


    private int id;
    private BinanceHesap binanceHesap ;



    private Islemci islemci;

    private String coin;
    private double riskOrani = 0.03;

    private BinanceHesapEleman sonraki ;
    private BinanceHesapEleman onceki ;

    public BinanceHesapEleman ( int id ,String apiKey , String secretKey,String coin ) throws Exception {
        this.id = id;
        binanceHesap= new BinanceHesap(apiKey,secretKey);

       // if ( coin.equals("BTCUSDC"))
           // islemci = new EmaTrendTakipIslemci(binanceHesap,coin);
       // else
         //islemci = new AlphaTrendIslemci(binanceHesap,coin);

        if (islemci == null )
        islemci = new DipPipIslemci(binanceHesap,coin,riskOrani);
    }
    public BinanceHesapEleman ( int id, String apiKey , String secretKey,String coin , double riskOrani) throws Exception {
        this.id = id;
        binanceHesap= new BinanceHesap(apiKey,secretKey);
      //  islemci = new DipPipIslemci(binanceHesap,coin,riskOrani);
        if (islemci == null )
        islemci = new EmaYutanIslemci(binanceHesap,coin,riskOrani);
    }



    public BinanceHesap getBinanceHesap() {
        return binanceHesap;
    }

    public BinanceHesapEleman getSonraki() {
        return sonraki;
    }

    public void setSonraki(BinanceHesapEleman sonraki) {
        this.sonraki = sonraki;
    }

    public BinanceHesapEleman getOnceki() {
        return onceki;
    }

    public void setOnceki(BinanceHesapEleman onceki) {
        this.onceki = onceki;
    }

    public Islemci getIslemci() {
        return islemci;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
