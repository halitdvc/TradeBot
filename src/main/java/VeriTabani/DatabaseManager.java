package VeriTabani;

import java.io.IOException;
import java.sql.*;


import Baglanti.Binance.BinanceList.BinanceHesapEleman;
import Baglanti.Binance.BinanceList.BinanceHesapList;
import Utils.EnvReader;
import org.w3c.dom.ls.LSOutput;


public class DatabaseManager {

    EnvReader env;

    private String url ;
    private String user ;
    private String password ;

    public Connection connect() throws SQLException, IOException {
        env= new EnvReader();
        return DriverManager.getConnection(env.get("DB_URL"), env.get("DB_USER"), env.get("DB_PASSWORD"));
    }


    public void insertData(String name, String email) {
        String sqlInsert = "INSERT INTO TestTable (name, email) VALUES (?, ?);";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.executeUpdate();
            System.out.println("Veri başarıyla eklendi.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public BinanceHesapList getAllActiveAccounts(BinanceHesapList list) throws SQLException {

        String sql = "SELECT * FROM anahesaplar WHERE isactive = TRUE";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String apiKey = rs.getString("apikey");
                String secretKey = rs.getString("secretkey");
                String coin = rs.getString("hangicoin");
                if( !list.hesapMevcutMuById(id))  list.sonaEkle(new BinanceHesapEleman(id,apiKey, secretKey, coin)) ;


            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return list;
    }
}
