package Baglanti.Binance;

import org.json.JSONArray;
import org.json.JSONObject;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class BinanceSpotApiClient extends BinanceBaseApiClient {


    private static final String BASE_URL = "https://api.binance.com";

    @Override
    protected String getBaseUrl() {
        return BASE_URL;
    }

    public BinanceSpotApiClient(String API_KEY, String SECRET_KEY) {
        super(API_KEY, SECRET_KEY);
    }

    public double getBalance(String asset) throws Exception {
        long timestamp = System.currentTimeMillis();
        Map<String, Object> params = new HashMap<>();
        params.put("timestamp", String.valueOf(timestamp));

        JSONObject jsonResponse = makeRequest("/api/v3/account", "GET", params);
        JSONArray balances = jsonResponse.getJSONArray("balances");
        for (int i = 0; i < balances.length(); i++) {
            JSONObject balanceObject = balances.getJSONObject(i);
            if (asset.equals(balanceObject.getString("asset"))) {
                return balanceObject.getDouble("free");
            }
        }
        throw new RuntimeException("Asset not found: " + asset);
    }

/*    public void piyasaEmriVer(String symbol, String side, double quantity) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("side", side);
        params.put("type", "MARKET");
        params.put("quantity", Double.toString(quantity));

       // params.put("quantity", String.valueOf(quantity));
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));

        makeRequest("/api/v3/order", "POST", params);
    }*/

    public void piyasaEmriVer(String symbol, String side, double quantity) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("side", side);
        params.put("type", "MARKET");
       // params.put("quantity", String.format("%.4f", quantity)); // 0.0009 gibi bir değer kullanmak için
        params.put("quantity", String.valueOf(quantity));
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));

        makeRequest("/api/v3/order", "POST", params);
    }


    public void limitEmriVer(String symbol, String side, double quantity, double price) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("side", side);
        params.put("type", "LIMIT");
        params.put("timeInForce", "GTC");
        params.put("price", String.valueOf(price));
        params.put("quantity", String.valueOf(quantity));
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));

        makeRequest("/api/v3/order", "POST", params);
    }

    public JSONObject getSpotTradingPairInfo(String symbol) throws Exception {
        JSONObject response = makeRequest("/api/v3/exchangeInfo", "GET", new HashMap<>());

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

    public JSONObject getSpotExchangeInfo() throws Exception {
        return makeRequest("/api/v3/exchangeInfo", "GET", new HashMap<>());
    }

    public double getSpotStepSize(String symbol) throws Exception {
        JSONObject exchangeInfo = getSpotExchangeInfo();
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




}
