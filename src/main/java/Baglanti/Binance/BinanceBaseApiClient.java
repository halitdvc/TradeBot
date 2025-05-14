package Baglanti.Binance;

import org.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public abstract class BinanceBaseApiClient {

    protected final String API_KEY;
    protected final String SECRET_KEY;
    protected abstract String getBaseUrl();
    public BinanceBaseApiClient(String API_KEY, String SECRET_KEY) {
        this.API_KEY = API_KEY;
        this.SECRET_KEY = SECRET_KEY;
    }

    protected JSONObject makeRequest(String endpoint, String method, Map<String, Object> params) throws Exception {
        String queryString = buildQueryString(params);

        // HMAC imzasını sadece parametre varsa ekleyelim:
        if(!params.isEmpty()) {
            String signature = hmacSha256(queryString, SECRET_KEY);
            queryString += "&signature=" + signature;
        }

        URL url = new URL(getBaseUrl() + endpoint + "?" + queryString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("X-MBX-APIKEY", API_KEY);


        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            String errorLine;
            StringBuilder errorMessage = new StringBuilder();
            while ((errorLine = errorReader.readLine()) != null) {
                errorMessage.append(errorLine);
            }
            errorReader.close();
            throw new RuntimeException("Failed to make request. HTTP Code: " + responseCode + ". Message: " + errorMessage.toString());
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();


        return new JSONObject(response.toString());
    }
    protected String buildQueryString(Map<String, Object> params) {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (result.length() > 0) {
                result.append("&");
            }
            result.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return result.toString();
    }

    protected String hmacSha256(String data, String secret) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] bytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }


}
