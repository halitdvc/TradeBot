package Baglanti.Binance;


import Grafik.MumList;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BinanceHesap {
    private final BinanceFuturesApiClient futuresApiClient;
    private final BinanceSpotApiClient spotApiClient;

    private static final String SPOT_BASE_URL = "https://api.binance.com/api/v3/klines";
    private static final String FUTURES_BASE_URL = "https://fapi.binance.com/fapi/v1/klines";
    private DataReader dataReader = new DataReader(this); // Bu BinanceHesap nesnesini DataReader'a referans olarak geçiriyor.

    private MumList currentMumList = new MumList();

    // SONRADAN EKLEDIM UST TIME FRAMEYI
    private MumList ustTimeFrameList = new MumList();

    public double usdtBalance; // hesaptaki bakiye

    public BinanceHesap(String API_KEY, String SECRET_KEY) throws Exception {
        this.futuresApiClient = new BinanceFuturesApiClient(API_KEY, SECRET_KEY);
        this.spotApiClient = new BinanceSpotApiClient(API_KEY, SECRET_KEY);
        this.usdtBalance = getSpotBalance("USDT");
    }




    private String fetchData(String baseUrl, String symbol, String interval) throws Exception {
        URL url = new URL(baseUrl + "?symbol=" + symbol + "&interval=" + interval);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-MBX-APIKEY", spotApiClient.API_KEY);

        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }

        connection.disconnect();
        return response.toString();
    }

    public double getSpotBalance(String asset) throws Exception {
        return spotApiClient.getBalance(asset);
    }

    public void spotPiyasaEmriVer(String symbol, String side, double quantity) throws Exception {
        spotApiClient.piyasaEmriVer(symbol, side, quantity);
    }

    public void spotLimitEmriVer(String symbol, String side, double quantity, double price) throws Exception {
        spotApiClient.limitEmriVer(symbol, side, quantity, price);
    }

    public double getFuturesBalance(String asset) throws Exception {
        return futuresApiClient.getBalance(asset);
    }

    public void kaldıracAyarlama(String sembol, int kaldırac) throws Exception {
        futuresApiClient.kaldıracAyarlama(sembol, kaldırac);
    }


    public void pozisyonAcFutures (String sembol, String side, double miktar, String aLong,double takeProfitPrice, double stopLossPrice) throws Exception{
        futuresApiClient.openPosition(sembol,side,miktar,aLong,takeProfitPrice,stopLossPrice);
    }

    public void pozisyonAcFutures (String sembol, String side, double miktar, String aLong) throws Exception{
        futuresApiClient.openPosition(sembol,side,miktar,aLong);
    }
    public void futuresSlAyarla (String sembol, String side, double miktar, double stopPrice, String aLong) throws Exception {
        futuresApiClient.setStopLoss(sembol,side,miktar,stopPrice,aLong);
    }
    public void futuresTpAyarla (String sembol, String side, double miktar, double takePrice, String aLong) throws Exception {
        futuresApiClient.setTakeProfit(sembol,side,miktar,takePrice,aLong);
    }


    public JSONObject getSpotTradingPairInfo(String symbol) throws Exception {
        return spotApiClient.getSpotTradingPairInfo(symbol);
    }

    public JSONObject getFuturesTradingPairInfo(String symbol) throws Exception {
        return futuresApiClient.getFuturesTradingPairInfo(symbol);
    }

    public BinanceFuturesApiClient getFuturesApiClient() {
        return futuresApiClient;
    }

    public BinanceSpotApiClient getSpotApiClient() {
        return spotApiClient;
    }

    public String fetchSpotData(String symbol, String interval) throws Exception {
        return fetchData(SPOT_BASE_URL, symbol, interval);
    }

    public String fetchFuturesData(String symbol, String interval) throws Exception {
        return fetchData(FUTURES_BASE_URL, symbol, interval);
    }
    public MumList getCurrentMumList() {
        return currentMumList;
    }
// SONRADAN EKLEDIM UST TIME FRAMEYI
    public MumList getUstTimeFrameList() {
        return ustTimeFrameList;
    }

    public DataReader getDataReader() {
        return dataReader;
    }

    public void setDataReader(DataReader dataReader) {
        this.dataReader = dataReader;
    }



    public JSONObject cancelAllOpenFuturesOrders (String sembol) throws Exception {
        return futuresApiClient.cancelAllOpenFuturesOrders(sembol);
    }
    public JSONObject cancelAllFuturesPosition (String sembol,String side,String positionSide,double miktar) throws Exception {
        return futuresApiClient.closeAllOpenPositions(sembol,side,positionSide,miktar);
    }

}


