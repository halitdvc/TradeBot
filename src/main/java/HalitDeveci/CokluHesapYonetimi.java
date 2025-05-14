package HalitDeveci;

import Baglanti.Binance.BinanceList.BinanceHesapEleman;
import Baglanti.Binance.BinanceList.BinanceHesapList;
import VeriTabani.DatabaseManager;
import VeriTabani.VeriGuncellemeServisi;
import org.postgresql.util.PSQLException;

public class CokluHesapYonetimi {
    public static void main(String[] args) throws Exception {



        VeriGuncellemeServisi veriGuncellemeServisi = new VeriGuncellemeServisi();
        veriGuncellemeServisi.baslat();



        int tryCount = 0;
        int maxDeneme = 100;



        while (true) {

            synchronized ( veriGuncellemeServisi) {
                BinanceHesapEleman isaretci = veriGuncellemeServisi.getHesapList().getSon();
                while (isaretci != null) {


                    try {
                        isaretci.getIslemci().calistir();
                        //System.out.println(veriGuncellemeServisi.getHesapList().getHesapSayisi());   // hesap sayisi

                    } catch (Exception E) {
                        System.out.println("Hata Olustu, tekrar deniyor... " + tryCount + "/" + maxDeneme);
                        System.out.println(E.getLocalizedMessage());
                    }


                    tryCount++;
                    isaretci = isaretci.getOnceki();


                    Thread.sleep(100);

                }

            }

            Thread.sleep(100);

        }

    }
}
