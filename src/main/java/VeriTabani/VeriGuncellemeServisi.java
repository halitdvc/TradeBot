package VeriTabani;

import Baglanti.Binance.BinanceList.BinanceHesapList;
import org.postgresql.util.PSQLException;

import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class VeriGuncellemeServisi {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private BinanceHesapList hesapList= new BinanceHesapList();
    private DatabaseManager db = new DatabaseManager();
    public void baslat() {
        final Runnable hesapKontrol = new Runnable() {
            public void run() {


                try {
                    //System.out.println("Kontrol Ediliyor");
                    hesapList = db.getAllActiveAccounts(hesapList);
                    //System.out.println("hesap sayisi : +"+hesapList.getHesapSayisi());

                }catch (PSQLException e ) {
                    System.out.println("Hata Olustu sql...." + e);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }
        };

        // İlk çalıştırmayı başlatma ve sonrasında her 10 saniyede bir tekrarla
        scheduler.scheduleAtFixedRate(hesapKontrol, 0, 10, TimeUnit.SECONDS);
    }

    public void durdur() {
        scheduler.shutdown();
    }




    public BinanceHesapList getHesapList() {
        return hesapList;
    }

    public void setHesapList(BinanceHesapList hesapList) {
        this.hesapList = hesapList;
    }
}
