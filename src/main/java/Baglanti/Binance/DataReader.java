package Baglanti.Binance;

import Grafik.Mum;
import Grafik.MumList;
import org.json.JSONArray;

public class DataReader {

    private BinanceHesap binanceHesap;

    public DataReader(BinanceHesap binanceHesap) {
        this.binanceHesap = binanceHesap;
    }

    public void updateMumListForSpot(String symbol, String interval, long lastTimestamp) throws Exception {
        String jsonData = binanceHesap.fetchSpotData(symbol, interval);
        readData(jsonData, binanceHesap.getCurrentMumList(), lastTimestamp);
    }
    // UST TIME FRAMEYI SONRADAN EKLEDIM
    public void ustTimeFrameListGuncelleSpot ( String symbol , String interval, long lastTimestamp) throws Exception {
        String jsonData = binanceHesap.fetchSpotData(symbol,interval);
        readData( jsonData , binanceHesap.getUstTimeFrameList(),lastTimestamp);
    }
    public void updateMumListForFutures(String symbol, String interval, long lastTimestamp) throws Exception {
        String jsonData = binanceHesap.fetchFuturesData(symbol, interval);
        readData(jsonData, binanceHesap.getCurrentMumList(), lastTimestamp);
    }
    public void ustTimeFrameListGuncelleFutures ( String symbol , String interval, long lastTimestamp) throws Exception {
        String jsonData = binanceHesap.fetchFuturesData(symbol,interval);
        readData( jsonData , binanceHesap.getUstTimeFrameList(),lastTimestamp);
    }
    private void readData(String jsonData, MumList list, long lastTimestamp) {
        JSONArray jsonArray = new JSONArray(jsonData);


        for (int i = 0; i < jsonArray.length(); i++) {
            JSONArray data = jsonArray.getJSONArray(i);

            long timestamp = data.getLong(0);
            if (timestamp > lastTimestamp) {
                double open = data.getDouble(1);
                double low = data.getDouble(2);
                double high = data.getDouble(3);
                double close = data.getDouble(4);
                double volume = data.getDouble(5);

                list.sonaEkle(new Mum(open, close, high, low, timestamp,volume));
                //System.out.println("Mum Eklendi kapanıs : " + list.getSon().getKapanisFiyati());
            } else if (timestamp == lastTimestamp) {
                list.sondanSil();
                double open = data.getDouble(1);
                double low = data.getDouble(2);
                double high = data.getDouble(3);
                double close = data.getDouble(4);
                double volume = data.getDouble(5);
                list.sonaEkle(new Mum(open, close, high, low, timestamp,volume));
                //System.out.println("Son Mum Güncellendi : " + list.getSon().getKapanisFiyati());


            }
        }
    }
}
