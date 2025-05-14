package Baglanti.Binance;

import Utils.EnvReader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BinanceExchangeInfo {
    static EnvReader env;

    static {
        try {
            env = new EnvReader();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String API_KEY = env.get("API_KEY");
    private static final String BASE_URL = "https://api.binance.com";

    public BinanceExchangeInfo() throws IOException {
    }

    public static void main(String[] args) throws Exception {
        getTradingInfo("ADAUSDT");
    }

    private static void getTradingInfo(String symbol) throws Exception {
        String endpoint = BASE_URL + "/api/v3/exchangeInfo";
        HttpURLConnection connection = (HttpURLConnection) new URL(endpoint).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-MBX-APIKEY", API_KEY);

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        JSONObject jsonResponse = new JSONObject(response.toString());
        JSONArray symbolsArray = jsonResponse.getJSONArray("symbols");

        for (int i = 0; i < symbolsArray.length(); i++) {
            JSONObject symbolObject = symbolsArray.getJSONObject(i);
            if (symbol.equals(symbolObject.getString("symbol"))) {
                JSONArray filters = symbolObject.getJSONArray("filters");
                for (int j = 0; j < filters.length(); j++) {
                    JSONObject filterObject = filters.getJSONObject(j);
                    if ("LOT_SIZE".equals(filterObject.getString("filterType"))) {
                        System.out.println("Min Quantity: " + filterObject.getString("minQty"));
                        System.out.println("Max Quantity: " + filterObject.getString("maxQty"));
                        System.out.println("Step Size: " + filterObject.getString("stepSize"));
                        //System.out.println("Tick Size: " + filterObject.getString("tickSize"));
                    }
                }
            }
        }
    }
}
