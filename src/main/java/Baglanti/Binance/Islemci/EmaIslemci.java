    package Baglanti.Binance.Islemci;

    import Baglanti.Binance.AlimSatimIslemleri;
    import Baglanti.Binance.BinanceHesap;
    import Grafik.Mum;
    import Indikator.EmaIndikator;
    import Indikator.Indikator;
    import Strateji.EmaStrategy;
    import Strateji.IStrateji;

    public class EmaIslemci extends Islemci {

        private BinanceHesap hesap;
        private String coin;
        private Indikator ema20;
        private Indikator ema50;
        private IStrateji strateji;
        private AlimSatimIslemleri alimSatimIslemleri;

        long lastTimestamp = 0;
        long lastTimestampUstTime = 0;
        private String timeFrame = "15m";
        private String ustTimeFrame = "1h";
        private double kacR = 2;
        private double riskOrani; // yuzde hesaptan geliyor
        private double islemAraligi;
        private double birR;
        private double hesapBakiyesi;
        private double alimMiktari; // dolar
        private double riskeAtilacakToplamTutar;

        private int kaldirac = 50;

        private double basariliIslem = 0;
        private double basarisizIslem = 0;


        private boolean shortIslemdemiyim = false;
        private boolean longIslemdemiyim = false;

        private double girisNoktasi;
        private double risk;
        private double tp = 0;
        private double sl = 0;

        public EmaIslemci(BinanceHesap hesap, String coin, double riskOrani) throws Exception {
            this.hesap = hesap;
            this.coin = coin;
            ema20 = new EmaIndikator(20);
            ema50 = new EmaIndikator(50);
            strateji = new EmaStrategy();
            alimSatimIslemleri = new AlimSatimIslemleri(hesap);
            this.riskOrani = riskOrani;
        }

        public void calistir() throws Exception {

            hesap.getDataReader().updateMumListForFutures(coin,timeFrame,lastTimestamp);

            hesap.getDataReader().ustTimeFrameListGuncelleFutures(coin,ustTimeFrame,lastTimestampUstTime);

            if (hesap.getCurrentMumList().getSon() != null) {
                lastTimestamp = hesap.getCurrentMumList().getSon().getZamanDamgasi();
            }
            if (hesap.getUstTimeFrameList().getSon() != null) {
                lastTimestampUstTime = hesap.getUstTimeFrameList().getSon().getZamanDamgasi();
            }


            Mum sonMum = hesap.getCurrentMumList().getSon();
            if (ema20.hesapla(sonMum)>ema50.hesapla(sonMum)){ // ust trend buy ise


            if (strateji.buy(sonMum) && !longIslemdemiyim && !shortIslemdemiyim) {
                longIslemdemiyim = true;
                girisNoktasi = ema20.hesapla(sonMum);
                risk = girisNoktasi - ema50.hesapla(sonMum);
                tp = girisNoktasi + (risk * kacR);
                sl = ema50.hesapla(sonMum);

                islemAraligi = Math.abs(tp - sl);
                birR = islemAraligi / (1 + kacR);
                hesapBakiyesi = hesap.getFuturesBalance("USDT");
                riskeAtilacakToplamTutar = hesapBakiyesi * riskOrani;
                alimMiktari = (riskeAtilacakToplamTutar / birR) * girisNoktasi;

                hesap.kaldıracAyarlama(coin, kaldirac);

                System.out.println("Long Giris Noktasi (" + coin + "): " + girisNoktasi);
                System.out.println("Risk: " + risk);
                System.out.println("TP: " + tp);
                System.out.println("SL: " + sl);
                System.out.println("ema20 : "+ema20.hesapla(sonMum));
                System.out.println("ema50 : "+ema50.hesapla(sonMum));
                System.out.println("high " + sonMum.getEnYuksek());
                System.out.println("low " + sonMum.getEnDusuk());
                System.out.println("open " + sonMum.getAcilisFiyati());
                System.out.println("close " + sonMum.getKapanisFiyati());
                System.out.println("1R Degeri: " + birR);
                System.out.println("Hesap Bakiyesi: " + hesapBakiyesi);
                System.out.println("Riske Atilacak Toplam Tutar: " + riskeAtilacakToplamTutar);
                System.out.println("Alim Miktari: " + alimMiktari);

                alimSatimIslemleri.futuresLongDolarBazli(coin, alimMiktari, sl, tp);
            }}

            else{ // ust trend sell ise
            if (strateji.sell(sonMum) && !shortIslemdemiyim && !longIslemdemiyim) {
                shortIslemdemiyim = true;
                girisNoktasi = ema20.hesapla(sonMum);
                risk = ema50.hesapla(sonMum) - girisNoktasi;
                tp = girisNoktasi - (risk * kacR);
                sl = ema50.hesapla(sonMum);

                islemAraligi = Math.abs(tp - sl);
                birR = islemAraligi / (1 + kacR);
                hesapBakiyesi = hesap.getFuturesBalance("USDT");
                riskeAtilacakToplamTutar = hesapBakiyesi * riskOrani;
                alimMiktari = (riskeAtilacakToplamTutar / birR) * girisNoktasi;

                hesap.kaldıracAyarlama(coin, kaldirac);

                System.out.println("Short Giris Noktasi (" + coin + "): " + girisNoktasi);
                System.out.println("Risk: " + risk);
                System.out.println("TP: " + tp);
                System.out.println("SL: " + sl);
                System.out.println("ema20 : "+ema20.hesapla(sonMum));
                System.out.println("ema50 : "+ema50.hesapla(sonMum));
                System.out.println("high " + sonMum.getEnYuksek());
                System.out.println("low " + sonMum.getEnDusuk());
                System.out.println("open " + sonMum.getAcilisFiyati());
                System.out.println("close " + sonMum.getKapanisFiyati());
                System.out.println("1R Degeri: " + birR);
                System.out.println("Hesap Bakiyesi: " + hesapBakiyesi);
                System.out.println("Riske Atilacak Toplam Tutar: " + riskeAtilacakToplamTutar);
                System.out.println("Alim Miktari: " + alimMiktari);

                alimSatimIslemleri.futuresShortDolarBazli(coin, alimMiktari, sl, tp);
            }}

            // İşlemleri izleme ve güncelleme
            if (longIslemdemiyim) {
                if (sonMum.getKapanisFiyati() > tp) {
                    basariliIslem++;
                    longIslemdemiyim= false;
                    hesap.cancelAllOpenFuturesOrders(coin);
                    System.out.println(coin+" Long Islem tp ");
                    System.out.println("---------------------------------------------------------");
                    if ( basariliIslem != 0 && basarisizIslem != 0)
                        System.out.println(coin+" "+(basariliIslem/(basariliIslem+basarisizIslem)));

                } else if (sonMum.getKapanisFiyati() < sl) {
                    basarisizIslem++;
                    longIslemdemiyim= false;
                    hesap.cancelAllOpenFuturesOrders(coin);
                    System.out.println(coin+" Long Islem sl ");
                    System.out.println("---------------------------------------------------------");
                    if ( basariliIslem != 0 && basarisizIslem != 0)
                        System.out.println(coin+" "+(basariliIslem/(basariliIslem+basarisizIslem)));

                }
            } else if (shortIslemdemiyim) {
                if (sonMum.getKapanisFiyati() < tp) {
                    basariliIslem++;
                    shortIslemdemiyim= false;
                    hesap.cancelAllOpenFuturesOrders(coin);
                    System.out.println(coin+" Short Islem tp ");
                    System.out.println("---------------------------------------------------------");
                    if ( basariliIslem != 0 && basarisizIslem != 0)
                        System.out.println(coin+" "+(basariliIslem/(basariliIslem+basarisizIslem)));

                } else if (sonMum.getKapanisFiyati() > sl) {
                    basarisizIslem++;
                    shortIslemdemiyim= false;
                    hesap.cancelAllOpenFuturesOrders(coin);
                    System.out.println(coin+" Short Islem sl ");
                    System.out.println("---------------------------------------------------------");
                    if ( basariliIslem != 0 && basarisizIslem != 0)
                        System.out.println(coin+" "+(basariliIslem/(basariliIslem+basarisizIslem)));

                }
            }

            Thread.sleep(10); // 0.01 saniye beklet
        }

        public void konsolKaydir() {
            for (int i = 0; i < 10; i++)
                System.out.println();
        }
    }
