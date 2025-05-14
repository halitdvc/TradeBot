package Baglanti.Binance;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AlimSatimIslemleri {

    private final BinanceHesap hesap;

    public AlimSatimIslemleri(BinanceHesap hesap) {
        this.hesap = hesap;
    }

    // Spot alım işlemi
    public void spotAlim(String sembol, double miktar) throws Exception {
        hesap.spotPiyasaEmriVer(sembol, "BUY", miktar);
    }

    // Spot satış işlemi
    public void spotSatim(String sembol, double miktar) throws Exception {
        hesap.spotPiyasaEmriVer(sembol, "SELL", miktar);
    }

    // Dolar miktarını kullanarak spot alım işlemi
    public void spotAlimDolarBazli(String sembol, double dolarMiktar) throws Exception {
        double currentPrice = hesap.getCurrentMumList().getSon().getKapanisFiyati();
        double stepSize = hesap.getSpotApiClient().getSpotStepSize(sembol);

        double miktar = dolariKriptoMiktaraCevir(dolarMiktar, currentPrice, stepSize);

        this.spotAlim(sembol, miktar);
    }

    // Dolar miktarını kullanarak spot satış işlemi
    public void spotSatimDolarBazli(String sembol, double dolarMiktar) throws Exception {
        double currentPrice = hesap.getCurrentMumList().getSon().getKapanisFiyati();
        double stepSize = hesap.getSpotApiClient().getSpotStepSize(sembol);

        double miktar = dolariKriptoMiktaraCevir(dolarMiktar, currentPrice, stepSize);

        this.spotSatim(sembol, miktar);
    }

    // Miktarı step size'a uygun olarak yuvarlar
    private double yuvarlaMiktar(double miktar, double stepSize) {
        BigDecimal bd = new BigDecimal(miktar);
        // Step size tam sayı ise (örneğin 1.0), miktarı tam sayıya yuvarla.
        if (stepSize == 1.0) {
            bd = bd.setScale(0, RoundingMode.DOWN);
        } else {
            // Değilse, step size'ın ondalık kısmının hassasiyetine göre yuvarla.
            int scale = Math.max(0, String.valueOf(stepSize).length() - String.valueOf(stepSize).indexOf('.') - 1);
            bd = bd.setScale(scale, RoundingMode.HALF_UP);
        }
        return bd.doubleValue();
    }
    private double yuvarlaFiyat(double fiyat, double tickSize) {
        BigDecimal bd = new BigDecimal(fiyat);
        int scale = Math.max(0, String.valueOf(tickSize).length() - String.valueOf(tickSize).indexOf('.') - 1);
        bd = bd.setScale(scale, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    // Dolar miktarını kripto miktarına çevirir
    private double dolariKriptoMiktaraCevir(double miktar, double currentPrice, double stepSize) {
        System.out.print("Miktar : ");
        System.out.println(yuvarlaMiktar(miktar / currentPrice, stepSize));
        return yuvarlaMiktar(miktar / currentPrice, stepSize);
    }
    // Fiyatı step size'a uygun olarak yuvarlar

    private JSONObject cancelAllFuturesPosition (String sembol,String side,String positionSide,double dolarMiktar) throws Exception {
        double currentPrice = hesap.getCurrentMumList().getSon().getKapanisFiyati();
        double stepSize = hesap.getFuturesApiClient().getFuturesStepSize(sembol);
        double tickSize = hesap.getFuturesApiClient().getFuturesTickSize(sembol);
        System.out.println("Step Size : " + stepSize);

        double miktar = dolariKriptoMiktaraCevir(dolarMiktar, currentPrice, stepSize);

        return hesap.cancelAllFuturesPosition(sembol,side,positionSide,miktar);
    }

    public void cancelLongFuturesPosition ( String sembol ,double Miktar) throws Exception {
        cancelAllFuturesPosition(sembol,"SELL" ,"LONG", Miktar);
    }
    public void cancelShortFuturesPosition ( String sembol ,double Miktar) throws Exception {
        cancelAllFuturesPosition(sembol,"BUY" ,"SHORT", Miktar);
    }


    public double[] futuresLongDolarBazli(String sembol, double dolarMiktar, double stopLossFiyatı, double takeProfitFiyatı) throws Exception {
        double currentPrice = hesap.getCurrentMumList().getSon().getKapanisFiyati();
        double stepSize = hesap.getFuturesApiClient().getFuturesStepSize(sembol);
        double tickSize = hesap.getFuturesApiClient().getFuturesTickSize(sembol);

        System.out.println("Step Size : " + stepSize);

        double miktar = dolariKriptoMiktaraCevir(dolarMiktar, currentPrice, stepSize);
        // hesap.kaldıracAyarlama(sembol, 10);
        //hesap.pozisyonAcFutures(sembol, "BUY", miktar, "LONG",takeProfitFiyatı,stopLossFiyatı);

        double sl = yuvarlaFiyat(stopLossFiyatı,tickSize);
        double tp = yuvarlaFiyat(takeProfitFiyatı,tickSize);
        System.out.println(" SL : " + sl );
        System.out.println(" TP : " + tp );

        hesap.pozisyonAcFutures(sembol, "BUY", miktar, "LONG");
        hesap.futuresSlAyarla(sembol, "SELL", miktar, yuvarlaFiyat(stopLossFiyatı,tickSize), "LONG");
        hesap.futuresTpAyarla(sembol, "SELL", miktar, yuvarlaFiyat(takeProfitFiyatı,tickSize), "LONG");

        double[] tpVeSl = new double[2];
        tpVeSl[0] = tp; // TP değeri
        tpVeSl[1] = sl; // SL değeri

        return tpVeSl;
    }
    public void futuresLongDolarBazli(String sembol, double dolarMiktar) throws Exception {
        double currentPrice = hesap.getCurrentMumList().getSon().getKapanisFiyati();
        double stepSize = hesap.getFuturesApiClient().getFuturesStepSize(sembol);
        double tickSize = hesap.getFuturesApiClient().getFuturesTickSize(sembol);

        System.out.println("Step Size : " + stepSize);

        double miktar = dolariKriptoMiktaraCevir(dolarMiktar, currentPrice, stepSize);
        // hesap.kaldıracAyarlama(sembol, 10);
        //hesap.pozisyonAcFutures(sembol, "BUY", miktar, "LONG",takeProfitFiyatı,stopLossFiyatı);



        hesap.pozisyonAcFutures(sembol, "BUY", miktar, "LONG");

    }

    public double[] futuresShortDolarBazli(String sembol, double dolarMiktar, double stopLossFiyatı, double takeProfitFiyatı) throws Exception {
        double currentPrice = hesap.getCurrentMumList().getSon().getKapanisFiyati();
        double stepSize = hesap.getFuturesApiClient().getFuturesStepSize(sembol);
        double tickSize = hesap.getFuturesApiClient().getFuturesTickSize(sembol);

        System.out.println("Step Size : " + stepSize);

        double miktar = dolariKriptoMiktaraCevir(dolarMiktar, currentPrice, stepSize);
        // hesap.kaldıracAyarlama(sembol, 10);
        //hesap.pozisyonAcFutures(sembol, "SELL", miktar, "SHORT",takeProfitFiyatı,stopLossFiyatı);

        double sl = yuvarlaFiyat(stopLossFiyatı,tickSize);
        double tp = yuvarlaFiyat(takeProfitFiyatı,tickSize);
        System.out.println(" SL : " + sl );
        System.out.println(" TP : " + tp );

        hesap.pozisyonAcFutures(sembol, "SELL", miktar, "SHORT");
        hesap.futuresSlAyarla(sembol, "BUY", miktar,yuvarlaFiyat(stopLossFiyatı,tickSize), "SHORT");
        hesap.futuresTpAyarla(sembol, "BUY", miktar, yuvarlaFiyat(takeProfitFiyatı,tickSize), "SHORT");


        double[] tpVeSl = new double[2];
        tpVeSl[0] = tp; // TP değeri
        tpVeSl[1] = sl; // SL değeri

        return tpVeSl;
    }
    public void futuresShortDolarBazli(String sembol, double dolarMiktar) throws Exception {
        double currentPrice = hesap.getCurrentMumList().getSon().getKapanisFiyati();
        double stepSize = hesap.getFuturesApiClient().getFuturesStepSize(sembol);
        double tickSize = hesap.getFuturesApiClient().getFuturesTickSize(sembol);

        System.out.println("Step Size : " + stepSize);

        double miktar = dolariKriptoMiktaraCevir(dolarMiktar, currentPrice, stepSize);
        // hesap.kaldıracAyarlama(sembol, 10);
        //hesap.pozisyonAcFutures(sembol, "SELL", miktar, "SHORT",takeProfitFiyatı,stopLossFiyatı);



        hesap.pozisyonAcFutures(sembol, "SELL", miktar, "SHORT");

    }


    public JSONObject cancelAllOpenFuturesOrders (String sembol) throws Exception {
        return hesap.cancelAllOpenFuturesOrders(sembol);
    }



    /// daha iyi birsey bul .
    public double hesaplaPozisyonMiktari(double tp, double sl, double hesapBakiyesi, double riskOrani) {
        // Pozisyon türünü TP ve SL'ye göre belirle (SL > TP ise short, TP > SL ise long pozisyon)
        double islemAraligi = Math.abs(tp - sl); // İşlem aralığı her zaman pozitif olmalıdır
        double R = islemAraligi / 3; // 1R risk miktarı
        double riskeAtilacakToplamTutar = hesapBakiyesi * riskOrani; // Hesap bakiyesinin belirli bir yüzdesi
        double pozisyonMiktariR = riskeAtilacakToplamTutar / R; // Ne kadar R risk alınabileceği

        // Mevcut piyasa fiyatını al (bu değer dinamik olarak alınmalıdır)
        double mevcutPiyasaFiyati = 10; // Örnek olarak 10 USD kullanılmıştır
        double pozisyonMiktariKripto = pozisyonMiktariR / mevcutPiyasaFiyati; // Pozisyon büyüklüğü kripto cinsinden

        return pozisyonMiktariKripto;
    }




}
