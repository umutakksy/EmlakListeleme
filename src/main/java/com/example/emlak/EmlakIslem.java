package com.example.emlak;

public class EmlakIslem {
    private String islemTipi;
    private Emlak emlak;

    public EmlakIslem(String islemTipi, Emlak emlak) {
        this.islemTipi = islemTipi;
        this.emlak = emlak;
    }

    public String getIslemTipi() { return islemTipi; }
    public Emlak getEmlak() { return emlak; }
}