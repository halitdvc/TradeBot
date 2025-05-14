package Baglanti.Binance;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BinanceFuturesApiClient extends BinanceBaseApiClient {

    private static final String FUTURES_BASE_URL = "https://fapi.binance.com";

    @Override
    protected String getBaseUrl() {
        return FUTURES_BASE_URL;
    }

    public BinanceFuturesApiClient(String API_KEY, String SECRET_KEY) {
        super(API_KEY, SECRET_KEY);
    }

    public void kaldıracAyarlama(String sembol, int kaldırac) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("symbol", sembol);
        params.put("leverage", kaldırac);
        params.put("timestamp", System.currentTimeMillis());

        makeRequest("/fapi/v1/leverage", "POST", params);
    }
    public JSONObject openPosition(String sembol, String side, double miktar, String positionSide ,double takeProfitPrice, double stopLossPrice) throws Exception {
        Map<String, Object> marketParams = new HashMap<>();
        marketParams.put("symbol", sembol);
        marketParams.put("side", side); // BUY veya SELL
        marketParams.put("type", "MARKET");
        marketParams.put("quantity", String.valueOf(miktar));
        marketParams.put("positionSide", positionSide); // "BOTH", "LONG" veya "SHORT" olabilir.
        marketParams.put("timestamp", System.currentTimeMillis());

        // Take Profit ve Stop Loss için ekstra parametreler izole posizyonlarda uygun
        marketParams.put("takeProfit", takeProfitPrice);
        marketParams.put("stopLoss", stopLossPrice);

        return makeRequest("/fapi/v1/order", "POST", marketParams);
    }
    public JSONObject openPosition(String sembol, String side, double miktar, String positionSide ) throws Exception {
        Map<String, Object> marketParams = new HashMap<>();
        marketParams.put("symbol", sembol);
        marketParams.put("side", side); // BUY veya SELL
        marketParams.put("type", "MARKET");
        marketParams.put("quantity", String.valueOf(miktar));
        marketParams.put("positionSide", positionSide); // "BOTH", "LONG" veya "SHORT" olabilir.
        marketParams.put("timestamp", System.currentTimeMillis());

        return makeRequest("/fapi/v1/order", "POST", marketParams);
    }



    public JSONObject setStopLoss(String sembol, String side, double miktar, double stopPrice, String positionSide) throws Exception {
        Map<String, Object> stopLossParams = new HashMap<>();
        stopLossParams.put("symbol", sembol);
        stopLossParams.put("side", side);
        stopLossParams.put("type", "STOP_MARKET");
        stopLossParams.put("quantity", String.valueOf(miktar));
        stopLossParams.put("stopPrice", String.valueOf(stopPrice));
        stopLossParams.put("positionSide", positionSide);
        stopLossParams.put("timestamp", System.currentTimeMillis());

        return makeRequest("/fapi/v1/order", "POST", stopLossParams);
    }

    public JSONObject setTakeProfit(String sembol, String side, double miktar, double takePrice, String positionSide) throws Exception {
        Map<String, Object> takeProfitParams = new HashMap<>();
        takeProfitParams.put("symbol", sembol);
        takeProfitParams.put("side", side);
        takeProfitParams.put("type", "TAKE_PROFIT_MARKET");
        takeProfitParams.put("quantity", String.valueOf(miktar));
        takeProfitParams.put("stopPrice", String.valueOf(takePrice)); // Bu parametreyi ekleyin.
        takeProfitParams.put("positionSide", positionSide);
        takeProfitParams.put("timestamp", System.currentTimeMillis());

        return makeRequest("/fapi/v1/order", "POST", takeProfitParams);
    }


    public JSONObject cancelAllOpenFuturesOrders(String sembol) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("symbol", sembol);
        params.put("timestamp", System.currentTimeMillis());

        return makeRequest("/fapi/v1/allOpenOrders", "DELETE", params);
    }



    public double getBalance(String asset) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("timestamp", System.currentTimeMillis());

        JSONObject jsonResponse = makeRequest("/fapi/v2/account", "GET", params);
        JSONArray balances = jsonResponse.getJSONArray("assets");
        for (int i = 0; i < balances.length(); i++) {
            JSONObject balanceObject = balances.getJSONObject(i);
            if (asset.equals(balanceObject.getString("asset"))) {
                return balanceObject.getDouble("walletBalance");
            }
        }
        throw new RuntimeException("Asset not found: " + asset);
    }
    public JSONObject getFuturesTradingPairInfo(String symbol) throws Exception {
        JSONObject response = makeRequest("/fapi/v1/exchangeInfo", "GET", new HashMap<>());

        JSONArray symbols = response.getJSONArray("symbols");
        for (int i = 0; i < symbols.length(); i++) {
            JSONObject currentSymbol = symbols.getJSONObject(i);
            if (currentSymbol.getString("symbol").equals(symbol)) {
                return currentSymbol;
            }
        }

        throw new RuntimeException("Symbol not found: " + symbol);
    }


    // exchange info

    private JSONObject getFuturesExchangeInfo() throws Exception {
        return makeRequest("/fapi/v1/exchangeInfo", "GET", new HashMap<>());
    }

    public double getFuturesStepSize(String symbol) throws Exception {
        JSONObject exchangeInfo = getFuturesExchangeInfo();
        JSONArray symbols = exchangeInfo.getJSONArray("symbols");
        for (int i = 0; i < symbols.length(); i++) {
            JSONObject currentSymbol = symbols.getJSONObject(i);
            if (currentSymbol.getString("symbol").equals(symbol)) {
                JSONArray filters = currentSymbol.getJSONArray("filters");
                for (int j = 0; j < filters.length(); j++) {
                    JSONObject filterObject = filters.getJSONObject(j);
                    if ("LOT_SIZE".equals(filterObject.getString("filterType"))) {
                        //System.out.println("Min Quantity: " + filterObject.getString("minQty"));
                        //System.out.println("Max Quantity: " + filterObject.getString("maxQty"));
                        //System.out.println("Step Size: " + filterObject.getString("stepSize"));
                        return Double.parseDouble(filterObject.getString("stepSize"));
                    }
                }
            }
        }
        throw new RuntimeException("Step size not found for symbol: " + symbol);
    }

    public double getFuturesTickSize(String symbol) throws Exception {
        JSONObject exchangeInfo = getFuturesExchangeInfo();
        JSONArray symbols = exchangeInfo.getJSONArray("symbols");

        for (int i = 0; i < symbols.length(); i++) {
            JSONObject currentSymbol = symbols.getJSONObject(i);
            if (currentSymbol.getString("symbol").equals(symbol)) {
                JSONArray filters = currentSymbol.getJSONArray("filters");
                for (int j = 0; j < filters.length(); j++) {
                    JSONObject filterObject = filters.getJSONObject(j);
                    if ("PRICE_FILTER".equals(filterObject.getString("filterType"))) {
                        return Double.parseDouble(filterObject.getString("tickSize"));
                    }
                }
            }
        }
        throw new RuntimeException("Tick size not found for symbol: " + symbol);
    }


    public JSONObject closeAllOpenPositions(String symbol, String side,String positionSide, double quantity) throws Exception {
        Map<String, Object> params = new HashMap<>();


        params.put("symbol", symbol);
        params.put("side", side);
        params.put("positionSide", positionSide);
        params.put("type", "MARKET");
        params.put("quantity", quantity);

        params.put("timestamp", System.currentTimeMillis());


        return makeRequest("/fapi/v1/order", "POST", params);
    }



}



