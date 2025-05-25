package com.example.emlak;

import java.util.Objects;

public class Emlak {
    private String baslik;
    private String tip;
    private String sehir;
    private String adres;
    private double fiyat;
    private int metrekare;
    private String odaSayisi;
    private boolean favori = false;

    public Emlak() { }

    public Emlak(String baslik, String tip, String sehir, String adres, double fiyat, int metrekare, String odaSayisi) {
        this.baslik = baslik;
        this.tip = tip;
        this.sehir = sehir;
        this.adres = adres;
        this.fiyat = fiyat;
        this.metrekare = metrekare;
        this.odaSayisi = odaSayisi;
    }

    // Getter ve Setter metodları
    public String getBaslik() { return baslik; }
    public void setBaslik(String baslik) { this.baslik = baslik; }
    public String getTip() { return tip; }
    public void setTip(String tip) { this.tip = tip; }
    public String getSehir() { return sehir; }
    public void setSehir(String sehir) { this.sehir = sehir; }
    public String getAdres() { return adres; }
    public void setAdres(String adres) { this.adres = adres; }
    public double getFiyat() { return fiyat; }
    public void setFiyat(double fiyat) { this.fiyat = fiyat; }
    public int getMetrekare() { return metrekare; }
    public void setMetrekare(int metrekare) { this.metrekare = metrekare; }
    public String getOdaSayisi() { return odaSayisi; }
    public void setOdaSayisi(String odaSayisi) { this.odaSayisi = odaSayisi; }
    public boolean isFavori() { return favori; }
    public void setFavori(boolean favori) { this.favori = favori; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Emlak emlak = (Emlak) obj;
        return Objects.equals(baslik, emlak.baslik) &&
                Objects.equals(sehir, emlak.sehir) &&
                Objects.equals(adres, emlak.adres);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baslik, sehir, adres);
    }
}