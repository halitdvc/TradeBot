package Baglanti.Binance.Test;

import Baglanti.Binance.AlimSatimIslemleri;
import Baglanti.Binance.BinanceBaseApiClient;
import Baglanti.Binance.BinanceHesap;
import Baglanti.Binance.BinanceList.BinanceHesapEleman;
import Baglanti.Binance.BinanceSpotApiClient;
import Grafik.Mum;
import Grafik.MumList;

public class BinanceBaglantiTest {
    public static void main(String[] args) throws Exception {
        final String API_KEY = "lwnX78JXlCVCNlF0cjkImhi9hUPpOr3yXn6lo8kcKq9V7AjiBNhoNhJSfXssM6oy";
        final String API_SECRET = "2wWhGSafw6DWQJ1HUSuYlHjJH3YuhiAGz5y1sArHzR4kcesnx08Oif1fxmrsDyPl";

        BinanceHesapEleman busra = new BinanceHesapEleman(999,"SlGk6riSBi7En4VCK1TODupc0gKpoEmnm43VQHZMJFDOMax1ZhMzBllFcJx5hiQv","yg93CYRQWuJ1gbI9PcenioPr5Gik73gHJHZcGFpQTlXCNwKlEPPS5m3d2JkeDgYu","BTCUSDT");
        String api = "SlGk6riSBi7En4VCK1TODupc0gKpoEmnm43VQHZMJFDOMax1ZhMzBllFcJx5hiQv";
        String secret = "yg93CYRQWuJ1gbI9PcenioPr5Gik73gHJHZcGFpQTlXCNwKlEPPS5m3d2JkeDgYu";
        long lastTimestamp = 0;

        String Coin = "BTCUSDT";
        String time = "15m";
        int kaldirac = 50;

       // double tp = 43000.1232141254321;
        //double sl = 45000.35432654;

        double miktar =150 ;
       
            try {

               // BinanceHesap binanceHesap = new BinanceHesap(API_KEY, API_SECRET);
              //  BinanceHesap binanceHesap = new BinanceHesap(api,secret);
                //binanceHesap.kaldıracAyarlama(Coin,kaldirac);

              //  binanceHesap.getDataReader().updateMumListForSpot(Coin,time,lastTimestamp);


              //  AlimSatimIslemleri alimSatimIslemleri = new AlimSatimIslemleri( binanceHesap );

              ///  alimSatimIslemleri.spotAlimDolarBazli("ETHUSDT",400);

                //busraya 400 dolarlik eth aldin 0.006 eth



              // alimSatimIslemleri.futuresLongDolarBazli(Coin , miktar );
                //alimSatimIslemleri.cancelShortFuturesPosition(Coin,miktar);
                //alimSatimIslemleri.cancelLongFuturesPosition(Coin,miktar);
                //alimSatimIslemleri.futuresShortDolarBazli(Coin,miktar);
               // System.out.println(binanceHesap.getFuturesBalance("USDT"));

                //alimSatimIslemleri.spotAlimDolarBazli(Coin,40);

                //alimSatimIslemleri.spotSatimDolarBazli(Coin,40);

//                for(;;){
//                    binanceHesap.getDataReader().updateMumListForFutures(Coin,time,lastTimestamp);
//                    if (binanceHesap.getCurrentMumList().getSon() != null) {
//                        lastTimestamp = binanceHesap.getCurrentMumList().getSon().getZamanDamgasi();
//                    }
//                    if ( binanceHesap.getCurrentMumList().getSon().getKapanisFiyati() < tp){
//                        binanceHesap.cancelAllOpenFuturesOrders(Coin);
//                    }
//                    if ( binanceHesap.getCurrentMumList().getSon().getKapanisFiyati() > sl){
//                        binanceHesap.cancelAllOpenFuturesOrders(Coin);
//                    }
//
//                }



            } catch (Exception e) {
                System.out.println("Bir hata meydana geldi: " + e.getMessage());
                e.printStackTrace();
            }

        }


}

