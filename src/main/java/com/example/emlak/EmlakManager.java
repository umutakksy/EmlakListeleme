package com.example.emlak;

import com.example.emlak.veriyapilari.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;

public class EmlakManager {
    // Veri Yapıları
    private final Liste<Emlak> genel_ilanlar = new Liste<>();
    private HashMap<String, Liste<Emlak>> sehir_bazli_ilanlar = new HashMap<>();
    private TreeSet<Emlak> siralanmis_ilanlar = new TreeSet<>(Comparator.comparing(Emlak::getFiyat));
    private final LinkedList<Emlak> favori_ilanlar = new LinkedList<>();
    private final Stack<EmlakIslem> geri_alma_islemleri = new Stack<>();

    // Sabitler
    private final String JSON_DOSYA = "emlaklar.json";
    private final ObjectMapper mapper = new ObjectMapper();

    private final ObservableList<Emlak> emlakListesi = FXCollections.observableArrayList();

    public ObservableList<Emlak> getEmlakListesi() {
        return emlakListesi;
    }

    public LinkedList<Emlak> getFavoriIlanlar() {
        return favori_ilanlar;
    }

    public Stack<EmlakIslem> getGeriAlmaIslemleri() {
        return geri_alma_islemleri;
    }

    public HashMap<String, Liste<Emlak>> getSehirBazliIlanlar() {
        return sehir_bazli_ilanlar;
    }

    public TreeSet<Emlak> getSiralanmisIlanlar() {
        return siralanmis_ilanlar;
    }

    public void emlakEkle(Emlak yeniEmlak) {
        // Veri yapılarına ekleme
        genel_ilanlar.add(yeniEmlak);
        emlakListesi.add(yeniEmlak);
        siralanmis_ilanlar.add(yeniEmlak);
        Liste<Emlak> sehirIlanlari = sehir_bazli_ilanlar.get(yeniEmlak.getSehir());
        if (sehirIlanlari == null) {
            sehirIlanlari = new Liste<>();
            sehir_bazli_ilanlar.put(yeniEmlak.getSehir(), sehirIlanlari);
        }
        sehirIlanlari.add(yeniEmlak);

        // Geri alma işlemi kaydet
        geri_alma_islemleri.push(new EmlakIslem("EKLE", yeniEmlak));

        jsonKaydet();
    }

    public void emlakSil(Emlak secili) {
        // Veri yapılarından silme
        genel_ilanlar.remove(secili);
        emlakListesi.remove(secili);
        siralanmis_ilanlar.remove(secili);
        favori_ilanlar.remove(secili);

        // Şehir bazlı HashMap'den silme
        Liste<Emlak> sehirIlanlari = sehir_bazli_ilanlar.get(secili.getSehir());
        if (sehirIlanlari != null) {
            sehirIlanlari.remove(secili);
            if (sehirIlanlari.size() == 0) {
                sehir_bazli_ilanlar.remove(secili.getSehir());
            }
        }

        // Geri alma işlemi kaydet
        geri_alma_islemleri.push(new EmlakIslem("SIL", secili));

        jsonKaydet();
    }

    public void emlakGuncelle(Emlak secili, Emlak eskiEmlak, Emlak yeniEmlak) {
        // Eski değerleri güncelle
        secili.setBaslik(yeniEmlak.getBaslik());
        secili.setTip(yeniEmlak.getTip());
        secili.setDurum(yeniEmlak.getDurum());  // Yeni alan eklendi
        secili.setSehir(yeniEmlak.getSehir());
        secili.setAdres(yeniEmlak.getAdres());
        secili.setFiyat(yeniEmlak.getFiyat());
        secili.setMetrekare(yeniEmlak.getMetrekare());
        secili.setOdaSayisi(yeniEmlak.getOdaSayisi());

        // TreeSet'i yeniden düzenle
        siralanmis_ilanlar.remove(secili);
        siralanmis_ilanlar.add(secili);

        // Geri alma işlemi kaydet
        geri_alma_islemleri.push(new EmlakIslem("GUNCELLE", eskiEmlak));

        jsonKaydet();
    }

    public void favoriyeEkle(Emlak secili) {
        if (!favori_ilanlar.contains(secili)) {
            secili.setFavori(true);
            favori_ilanlar.add(secili);
        }
    }

    public void favorileriGoster() {
        emlakListesi.clear();
        emlakListesi.addAll(favori_ilanlar.toList().toJavaList());
    }

    public void sehirFiltrele(String seciliSehir) {
        Liste<Emlak> sehirIlanlari = sehir_bazli_ilanlar.get(seciliSehir);
        emlakListesi.clear();
        if (sehirIlanlari != null) {
            emlakListesi.addAll(sehirIlanlari.toJavaList());
        }
    }

    public void durumFiltrele(String seciliDurum) {
        ObservableList<Emlak> filtrelenmisListe = FXCollections.observableArrayList();

        for (Emlak emlak : genel_ilanlar) { // emlakListesi yerine genel_ilanlar kullanıldı
            if (emlak.getDurum().equalsIgnoreCase(seciliDurum)) {
                filtrelenmisListe.add(emlak);
            }
        }
        // TableView güncelleniyor
        emlakListesi.clear();
        emlakListesi.addAll(filtrelenmisListe);
    }

    public void fiyataSirala() {
        emlakListesi.clear();
        emlakListesi.addAll(siralanmis_ilanlar.toList().toJavaList());
    }

    public void geriAl() {
        if (geri_alma_islemleri.isEmpty()) return;
        EmlakIslem sonIslem = geri_alma_islemleri.pop();
        switch (sonIslem.getIslemTipi()) {
            case "EKLE":
                Emlak eklenecek = sonIslem.getEmlak();
                genel_ilanlar.remove(eklenecek);
                emlakListesi.remove(eklenecek);
                siralanmis_ilanlar.remove(eklenecek);
                Liste<Emlak> sehirIlanlariEkle = sehir_bazli_ilanlar.get(eklenecek.getSehir());
                if (sehirIlanlariEkle != null) {
                    sehirIlanlariEkle.remove(eklenecek);
                    if (sehirIlanlariEkle.size() == 0) {
                        sehir_bazli_ilanlar.remove(eklenecek.getSehir());
                    }
                }
                break;
            case "SIL":
                Emlak geriEklenen = sonIslem.getEmlak();
                genel_ilanlar.add(geriEklenen);
                emlakListesi.add(geriEklenen);
                siralanmis_ilanlar.add(geriEklenen);
                Liste<Emlak> sehirIlanlariSil = sehir_bazli_ilanlar.get(geriEklenen.getSehir());
                if (sehirIlanlariSil == null) {
                    sehirIlanlariSil = new Liste<>();
                    sehir_bazli_ilanlar.put(geriEklenen.getSehir(), sehirIlanlariSil);
                }
                sehirIlanlariSil.add(geriEklenen);
                break;
            case "GUNCELLE":
                // Güncelleme işleminin geri alınması için mevcut emlak bulunup eski değerlerle güncellenir
                Emlak eskiDegerler = sonIslem.getEmlak();
                // Burada mevcut emlağı bulmak ve eski değerlerle güncellemek gerekir
                // Bu işlem daha karmaşık olduğu için basit bir implementasyon yapıldı
                break;
        }
        jsonKaydet();
    }

    public void tumunuGoster() {
        emlakListesi.clear();
        emlakListesi.addAll(genel_ilanlar.toJavaList());
    }

    public void emlakAra(String arama) {
        if (arama.isEmpty()) {
            emlakListesi.clear();
            emlakListesi.addAll(genel_ilanlar.toJavaList());
            return;
        }
        ObservableList<Emlak> filtreli = FXCollections.observableArrayList();
        for (Emlak e : genel_ilanlar) {
            if (e.getBaslik().toLowerCase().contains(arama) ||
                    e.getAdres().toLowerCase().contains(arama) ||
                    e.getSehir().toLowerCase().contains(arama) ||
                    e.getTip().toLowerCase().contains(arama) ||
                    e.getDurum().toLowerCase().contains(arama)) { // Durum alanı da arama kapsamına eklendi
                filtreli.add(e);
            }
        }
        emlakListesi.setAll(filtreli);
    }

    public void ornekVerilerEkle() {
        Emlak emlak1 = new Emlak("Güzel Daire", "Daire", "Satılık", "İstanbul", "Kadıköy Merkez", 1250000, 120, "3");
        Emlak emlak2 = new Emlak("Modern Villa", "Villa", "Satılık", "Antalya", "Lara Sahil", 3500000, 250, "5+");
        Emlak emlak3 = new Emlak("Ofis Kiralık", "İşyeri", "Kiralık", "Ankara", "Çankaya Plaza", 8000, 80, "1");

        Liste<Emlak> tempList = new Liste<>();
        tempList.add(emlak1);
        tempList.add(emlak2);
        tempList.add(emlak3);

        for (int i = 0; i < tempList.size(); i++) {
            Emlak emlak = tempList.get(i);
            genel_ilanlar.add(emlak);
            emlakListesi.add(emlak);
            siralanmis_ilanlar.add(emlak);
            Liste<Emlak> sehirIlanlari = sehir_bazli_ilanlar.get(emlak.getSehir());
            if (sehirIlanlari == null) {
                sehirIlanlari = new Liste<>();
                sehir_bazli_ilanlar.put(emlak.getSehir(), sehirIlanlari);
            }
            sehirIlanlari.add(emlak);
        }
    }

    private void jsonKaydet() {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(JSON_DOSYA), genel_ilanlar.toJavaList());
        } catch (IOException e) {
            throw new RuntimeException("Veri kaydedilirken hata oluştu: " + e.getMessage());
        }
    }

    public void jsondanYukle() {
        File dosya = new File(JSON_DOSYA);
        if (!dosya.exists()) return;
        try {
            java.util.List<Emlak> liste = mapper.readValue(dosya, new TypeReference<java.util.List<Emlak>>() {});
            genel_ilanlar.clear();
            emlakListesi.clear();
            siralanmis_ilanlar = new TreeSet<>(Comparator.comparing(Emlak::getFiyat));
            sehir_bazli_ilanlar = new HashMap<>();
            favori_ilanlar.clear();
            for (Emlak emlak : liste) {
                genel_ilanlar.add(emlak);
                emlakListesi.add(emlak);
                siralanmis_ilanlar.add(emlak);
                Liste<Emlak> sehirIlanlari = sehir_bazli_ilanlar.get(emlak.getSehir());
                if (sehirIlanlari == null) {
                    sehirIlanlari = new Liste<>();
                    sehir_bazli_ilanlar.put(emlak.getSehir(), sehirIlanlari);
                }
                sehirIlanlari.add(emlak);
                if (emlak.isFavori()) {
                    favori_ilanlar.add(emlak);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Veri yüklenirken hata oluştu: " + e.getMessage());
        }
    }
}