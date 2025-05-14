package Grafik;

public class MumAnaliz {
    public static double gercekMenzil(Mum mum) {
        if (mum.getOnceki() == null) {
            return mum.getEnYuksek() - mum.getEnDusuk();
        }
        return Math.max(Math.max(mum.getEnYuksek() - mum.getEnDusuk(), Math.abs(mum.getEnYuksek() - mum.getOnceki().getKapanisFiyati())), Math.abs(mum.getEnDusuk() - mum.getOnceki().getKapanisFiyati()));
    }

    public static MumRengi mumRengiHesapla ( Mum mum ) {
        if(mum.getKapanisFiyati() > mum.getAcilisFiyati() )
            return MumRengi.YESIL;
        else if ( mum.getKapanisFiyati() < mum.getAcilisFiyati() )
            return MumRengi.KIRMIZI;
        else
            return MumRengi.GRİ;
    }
    private static double mumGovdesiHesapla(Mum mum) {
        return Math.abs(mum.getKapanisFiyati() - mum.getAcilisFiyati());
    }
    private static double ustFitilHesapla ( Mum mum) {
        if (mumRengiHesapla(mum) == MumRengi.KIRMIZI) return mum.getEnYuksek()-mum.getAcilisFiyati();
        else return mum.getEnYuksek()-mum.getKapanisFiyati();
    }
    private static double altFitilHesapla ( Mum mum) {
        if (mumRengiHesapla(mum) == MumRengi.KIRMIZI) return mum.getKapanisFiyati()-mum.getEnDusuk();
        else return mum.getAcilisFiyati()-mum.getEnDusuk();
    }
    private static double mumBoyutu(Mum mum) {
        return Math.abs(mum.getAcilisFiyati()-mum.getKapanisFiyati());
    }

    public static boolean isAyiPinBar ( Mum mum ) {
        return mumBoyutu(mum) * ( 2. / 3.) < ustFitilHesapla(mum);
    }
    public static boolean isBogaPinBar ( Mum mum ) {
        return mumBoyutu(mum) * ( 2. / 3.) < altFitilHesapla(mum);
    }

    public static boolean ayiYutan ( Mum mum ) {
        return yutanMumMu(mum) && mumRengiHesapla(mum) == MumRengi.KIRMIZI;
    }
    public static boolean bogaYutan ( Mum mum ) {
        return yutanMumMu(mum) && mumRengiHesapla(mum) == MumRengi.YESIL;
    }

    private static boolean yutanMumMu(Mum mum) {
        if (mum.getOnceki() == null) {
            return false; // Önceki mum yoksa yutan mum değil.
        }
        MumRengi mevcutMumRengi = mumRengiHesapla(mum);
        MumRengi oncekiMumRengi = mumRengiHesapla(mum.getOnceki());

        if (mevcutMumRengi != oncekiMumRengi &&
               mumGovdesiHesapla(mum) > mumGovdesiHesapla(mum.getOnceki())) {
            return true;
        }

        return false;
    }

    public static boolean yukselenMumMu ( Mum mum ) {
        if (mum.getOnceki() == null) {
            return false;
        }
        return mum.getEnYuksek() > mum.getOnceki().getEnYuksek();
    }
    public static boolean alcalanMumMu ( Mum mum ) {
        if (mum.getOnceki() == null) {
            return false;
        }
        return mum.getEnDusuk() < mum.getOnceki().getEnDusuk();
    }
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
        return isTepe;
    }
    public static boolean dipMi(Mum mum) {
        boolean isDip = gecerliMumVeKomsulari(mum) &&
                mum.getEnDusuk() < mum.getOnceki().getEnDusuk() &&
                mum.getEnDusuk() < mum.getSonraki().getEnDusuk();
        return isDip;
    }
}
