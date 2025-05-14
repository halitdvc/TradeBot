package Grafik;

public class GrafikAnaliz {
    private static Mum aktifDip = null;
    private static Mum aktifTepe = null;
    private static PiyasaTrendi oncekiTrend = PiyasaTrendi.BELIRGIN_TREND_YOK;
    private static PiyasaTrendi aktifTrend = PiyasaTrendi.BELIRGIN_TREND_YOK;
    private static boolean gecerliMumVeKomsulari(Mum mum) {
        boolean valid = mum != null && mum.getOnceki() != null &&
                mum.getSonraki() != null && mum.getOnceki().getOnceki() != null &&
                mum.getSonraki().getSonraki() != null;
       // System.out.println("gecerliMumVeKomsulari sonucu: " + valid);
        return valid;
    }

    public static boolean tepeMi(Mum mum) {
        boolean isTepe = gecerliMumVeKomsulari(mum) &&
                mum.getEnYuksek() > mum.getOnceki().getEnYuksek() &&
                mum.getEnYuksek() > mum.getSonraki().getEnYuksek();
        System.out.println("tepeMi sonucu : " + isTepe);
        return isTepe;
    }
    public static boolean dipMi(Mum mum) {
        boolean isDip = gecerliMumVeKomsulari(mum) &&
                mum.getEnDusuk() < mum.getOnceki().getEnDusuk() &&
                mum.getEnDusuk() < mum.getSonraki().getEnDusuk();
        System.out.println("dipMi sonucu : " + isDip);
        return isDip;
    }
    public static boolean yukselenTepeMi(Mum mum) {
        if (aktifTepe == null) {
            return false;
        }

        boolean isYukselenTepe = tepeMi(mum) &&
                mum.getEnYuksek() > aktifTepe.getEnYuksek() ;

        if (isYukselenTepe) {
            aktifTepe = mum;
            System.out.println("Yukselen tepemi " + isYukselenTepe);
            System.out.println(mum.getEnYuksek());
        }

        return isYukselenTepe;
    }

    public static boolean alcAlanTepeMi(Mum mum) {
        if (aktifTepe == null) {
            return false;
        }

        boolean isAlcalanTepe = tepeMi(mum) &&
                mum.getEnYuksek() < aktifTepe.getEnYuksek() ;

        if (isAlcalanTepe) {
            aktifTepe = mum;
            System.out.println("Alcalan tepemi " + isAlcalanTepe);
            System.out.println(mum.getEnYuksek());
        }

        return isAlcalanTepe;
    }

    public static boolean yukselenDipMi ( Mum mum ) {
        if (aktifDip == null) {
            return false;
        }

        boolean isYukselenDip = dipMi(mum) &&
                mum.getEnDusuk() > aktifDip.getEnDusuk() ;

        if (isYukselenDip) {
            aktifDip = mum; // Eğer yükselen bir dip bulunduysa aktif dip bu mum olmalı.
            System.out.println("Yukselen Dip mi " + isYukselenDip);
            System.out.println(mum.getEnDusuk());
        }

        return isYukselenDip;
    }

    public static boolean alcAlanDipMi ( Mum mum ) {
        if (aktifDip == null) {
            return false;
        }

        boolean isAlcalanDip = dipMi( mum ) &&
                mum.getEnDusuk() < aktifDip.getEnDusuk() ;

        if (isAlcalanDip) {
            aktifDip = mum; // Eğer alçalan bir dip bulunduysa aktif dip bu mum olmalı.
            System.out.println("Alcalan Dip mi " + isAlcalanDip);
            System.out.println(mum.getEnDusuk());
        }

        return isAlcalanDip;
    }

    private static final double ZIGZAG_THRESHOLD = 0.05;  // %5 olarak varsayılan değer.

    public static boolean zigzagTepeMi(Mum mum) {
        if (aktifTepe == null) {
            return false;
        }

        double yuzdeDegisim = (mum.getEnYuksek() - aktifTepe.getEnYuksek()) / aktifTepe.getEnYuksek();

        if (yuzdeDegisim > ZIGZAG_THRESHOLD) {
            aktifTepe = mum;
            return true;
        }

        return false;
    }

    public static boolean zigzagDipMi(Mum mum) {
        if (aktifDip == null) {
            return false;
        }

        double yuzdeDegisim = (aktifDip.getEnDusuk() - mum.getEnDusuk()) / aktifDip.getEnDusuk();

        if (yuzdeDegisim > ZIGZAG_THRESHOLD) {
            aktifDip = mum;
            return true;
        }

        return false;
    }

    public static PiyasaTrendi piyasaAnalizi(Mum mum) {
        mum = mum.getOnceki();
        if (aktifDip == null && dipMi(mum)) {
            aktifDip = mum;
        }

        if (aktifTepe == null && tepeMi(mum)) {
            aktifTepe = mum;
        }



        if (aktifDip != null) {
            System.out.println("piyasa analizi : dip arayışı ");
            System.out.println();
            if (yukselenDipMi(mum)) {
                aktifTrend = PiyasaTrendi.YUKSELEN_DIP;
            } else if (alcAlanDipMi(mum)) {
                aktifTrend = PiyasaTrendi.ALCALAN_DIP;
            }
        }

        if (aktifTepe != null) {

            if (yukselenTepeMi(mum)) {
                aktifTrend = PiyasaTrendi.YUKSELEN_TEPE;
            } else if (alcAlanTepeMi(mum)) {
                aktifTrend = PiyasaTrendi.ALCALAN_TEPE;
            }
        }

        if (aktifTrend != PiyasaTrendi.BELIRGIN_TREND_YOK) {
            System.out.println("Aktif Trend: " + aktifTrend.name() + " devam ediyor!");
            oncekiTrend = aktifTrend;
        }
    return aktifTrend;
    }



        public static PiyasaTrendi piyasaAnaliziZigzag(Mum mum) {
            mum = mum.getOnceki();

            if (aktifDip == null || zigzagDipMi(mum)) {
                aktifDip = mum;
            }

            if (aktifTepe == null || zigzagTepeMi(mum)) {
                aktifTepe = mum;
            }

            if (aktifDip != null) {
                if (yukselenDipMi(mum)) {
                    aktifTrend = PiyasaTrendi.YUKSELEN_DIP;
                } else if (alcAlanDipMi(mum)) {
                    aktifTrend = PiyasaTrendi.ALCALAN_DIP;
                }
            }

            if (aktifTepe != null) {
                if (yukselenTepeMi(mum)) {
                    aktifTrend = PiyasaTrendi.YUKSELEN_TEPE;
                } else if (alcAlanTepeMi(mum)) {
                    aktifTrend = PiyasaTrendi.ALCALAN_TEPE;
                }
            }

            if (aktifTrend != PiyasaTrendi.BELIRGIN_TREND_YOK) {
                System.out.println("Aktif Trend (Zigzag): " + aktifTrend.name() + " devam ediyor!");
                oncekiTrend = aktifTrend;
            }

            return aktifTrend;
        }



}

