package com.example.emlak;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class EmlakUI implements Initializable {

    // FXML Bileşenleri
    @FXML private TableView<Emlak> tabloGoruntuleme;
    @FXML private TableColumn<Emlak, String> colBaslik;
    @FXML private TableColumn<Emlak, String> colTip;
    @FXML private TableColumn<Emlak, String> colDurum;  // Yeni kolon
    @FXML private TableColumn<Emlak, String> colSehir;
    @FXML private TableColumn<Emlak, String> colAdres;
    @FXML private TableColumn<Emlak, Double> colFiyat;
    @FXML private TableColumn<Emlak, Integer> colMetrekare;
    @FXML private TableColumn<Emlak, String> colOdaSayisi;
    @FXML private TableColumn<Emlak, Boolean> colFavori;

    @FXML private TextField txtBaslik;
    @FXML private TextField txtAdres;
    @FXML private TextField txtFiyat;
    @FXML private TextField txtMetrekare;
    @FXML private TextField txtAra;
    @FXML private TextField txtSehir;

    @FXML private ComboBox<String> cmbTip;
    @FXML private ComboBox<String> cmbDurum;  // Yeni ComboBox
    @FXML private ComboBox<String> cmbOdaSayisi;

    @FXML private Button btnEkle;
    @FXML private Button btnGuncelle;
    @FXML private Button btnSil;
    @FXML private Button btnTemizle;
    @FXML private Button btnFavoriyeEkle;
    @FXML private Button btnFavorileriGoster;
    @FXML private Button btnSehirFiltrele;
    @FXML private Button btnDurumFiltrele;  // Yeni buton
    @FXML private Button btnFiyataSirala;
    @FXML private Button btnGeriAl;
    @FXML private Button btnTumunuGoster;

    // Veri Modeli
    private EmlakManager emlakManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        emlakManager = new EmlakManager();
        setupUI();
        setupTable();

        // JSON'dan veri yükleme ve örnek veri ekleme
        emlakManager.jsondanYukle();
        if (emlakManager.getEmlakListesi().isEmpty()) {
            emlakManager.ornekVerilerEkle();
        }
    }

    private void setupUI() {
        // ComboBox'ları doldur
        cmbTip.setItems(FXCollections.observableArrayList("Daire", "Villa", "İşyeri"));
        cmbTip.setValue("Daire");

        cmbDurum.setItems(FXCollections.observableArrayList("Satılık", "Kiralık"));  // Yeni ComboBox
        cmbDurum.setValue("Satılık");

        cmbOdaSayisi.setItems(FXCollections.observableArrayList("1", "2", "3", "4", "5+"));
        cmbOdaSayisi.setValue("2");
    }

    private void setupTable() {
        // Tablo verilerini bağla
        tabloGoruntuleme.setItems(emlakManager.getEmlakListesi());

        // Kolon değerlerini ayarla
        colBaslik.setCellValueFactory(new PropertyValueFactory<>("baslik"));
        colTip.setCellValueFactory(new PropertyValueFactory<>("tip"));
        colDurum.setCellValueFactory(new PropertyValueFactory<>("durum"));  // Yeni kolon
        colSehir.setCellValueFactory(new PropertyValueFactory<>("sehir"));
        colAdres.setCellValueFactory(new PropertyValueFactory<>("adres"));
        colFiyat.setCellValueFactory(new PropertyValueFactory<>("fiyat"));
        colMetrekare.setCellValueFactory(new PropertyValueFactory<>("metrekare"));
        colOdaSayisi.setCellValueFactory(new PropertyValueFactory<>("odaSayisi"));
        colFavori.setCellValueFactory(new PropertyValueFactory<>("favori"));

        // Tablo seçim olayı
        tabloGoruntuleme.getSelectionModel().selectedItemProperty().addListener(
                (obs, eski, yeni) -> secilenEmlakGoster(yeni)
        );
    }

    @FXML
    private void emlakEkle() {
        try {
            if (!validateInput()) return;

            String baslik = txtBaslik.getText().trim();
            String tip = cmbTip.getValue();
            String durum = cmbDurum.getValue();  // Yeni alan
            String sehir = txtSehir.getText().trim();
            String adres = txtAdres.getText().trim();
            double fiyat = Double.parseDouble(txtFiyat.getText());
            int metrekare = Integer.parseInt(txtMetrekare.getText());
            String odaSayisi = cmbOdaSayisi.getValue();

            Emlak yeniEmlak = new Emlak(baslik, tip, durum, sehir, adres, fiyat, metrekare, odaSayisi);  // Güncellendi

            emlakManager.emlakEkle(yeniEmlak);
            formuTemizle();
            alertGoster("Bilgi", "İlan başarıyla eklendi!");

        } catch (NumberFormatException e) {
            alertGoster("Hata", "Fiyat ve Metrekare sayısal olmalıdır.");
        }
    }

    @FXML
    private void emlakSil() {
        Emlak secili = tabloGoruntuleme.getSelectionModel().getSelectedItem();
        if (secili == null) {
            alertGoster("Uyarı", "Lütfen silmek için bir emlak seçin.");
            return;
        }

        emlakManager.emlakSil(secili);
        formuTemizle();
        alertGoster("Bilgi", "İlan silindi!");
    }

    @FXML
    private void emlakGuncelle() {
        Emlak secili = tabloGoruntuleme.getSelectionModel().getSelectedItem();
        if (secili == null) {
            alertGoster("Uyarı", "Lütfen güncellemek için bir emlak seçin.");
            return;
        }

        try {
            if (!validateInput()) return;

            // Eski değerleri kaydet
            Emlak eskiEmlak = new Emlak(secili.getBaslik(), secili.getTip(), secili.getDurum(), secili.getSehir(),
                    secili.getAdres(), secili.getFiyat(), secili.getMetrekare(), secili.getOdaSayisi());

            Emlak yeniEmlak = new Emlak(txtBaslik.getText().trim(), cmbTip.getValue(), cmbDurum.getValue(),
                    txtSehir.getText().trim(), txtAdres.getText().trim(), Double.parseDouble(txtFiyat.getText()),
                    Integer.parseInt(txtMetrekare.getText()), cmbOdaSayisi.getValue());

            emlakManager.emlakGuncelle(secili, eskiEmlak, yeniEmlak);
            tabloGoruntuleme.refresh();
            formuTemizle();
            alertGoster("Bilgi", "İlan güncellendi!");

        } catch (NumberFormatException e) {
            alertGoster("Hata", "Lütfen sayısal değerleri doğru girin.");
        }
    }

    @FXML
    private void favoriyeEkle() {
        Emlak secili = tabloGoruntuleme.getSelectionModel().getSelectedItem();
        if (secili == null) {
            alertGoster("Uyarı", "Lütfen favoriye eklemek için bir emlak seçin.");
            return;
        }

        if (!emlakManager.getFavoriIlanlar().contains(secili)) {
            emlakManager.favoriyeEkle(secili);
            tabloGoruntuleme.refresh();
            alertGoster("Bilgi", "İlan favorilere eklendi!");
        } else {
            alertGoster("Uyarı", "Bu ilan zaten favorilerde!");
        }
    }

    @FXML
    private void favorileriGoster() {
        emlakManager.favorileriGoster();
        alertGoster("Bilgi", "Favori ilanlar gösteriliyor. (" + emlakManager.getFavoriIlanlar().size() + " adet)");
    }

    @FXML
    private void sehirFiltrele() {
        String seciliSehir = txtSehir.getText().trim();
        if (seciliSehir.isEmpty()) {
            alertGoster("Uyarı", "Lütfen şehir giriniz.");
            return;
        }

        emlakManager.sehirFiltrele(seciliSehir);
        alertGoster("Bilgi", seciliSehir + " şehrindeki ilanlar gösteriliyor. (" +
                (emlakManager.getSehirBazliIlanlar().get(seciliSehir) != null ?
                        emlakManager.getSehirBazliIlanlar().get(seciliSehir).size() : 0) + " adet)");
    }

    @FXML
    private void durumFiltrele() {  // Yeni metod
        String seciliDurum = cmbDurum.getValue();
        if (seciliDurum == null || seciliDurum.isEmpty()) {
            alertGoster("Uyarı", "Lütfen durum seçiniz.");
            return;
        }

        emlakManager.durumFiltrele(seciliDurum);
        alertGoster("Bilgi", seciliDurum + " durumundaki ilanlar gösteriliyor.");
    }

    @FXML
    private void fiyataSirala() {
        emlakManager.fiyataSirala();
        alertGoster("Bilgi", "İlanlar fiyata göre sıralandı!");
    }

    @FXML
    private void geriAl() {
        if (emlakManager.getGeriAlmaIslemleri().isEmpty()) {
            alertGoster("Uyarı", "Geri alınacak işlem yok!");
            return;
        }

        emlakManager.geriAl();
        tabloGoruntuleme.refresh();
        alertGoster("Bilgi", "Son işlem geri alındı!");
    }

    @FXML
    private void tumunuGoster() {
        emlakManager.tumunuGoster();
        formuTemizle();
    }

    @FXML
    private void formuTemizle() {
        txtBaslik.clear();
        cmbTip.setValue("Daire");
        cmbDurum.setValue("Satılık");  // Yeni alan
        txtSehir.clear();
        txtAdres.clear();
        txtFiyat.clear();
        txtMetrekare.clear();
        cmbOdaSayisi.setValue("2");
        tabloGoruntuleme.getSelectionModel().clearSelection();
    }

    @FXML
    private void aramaYap(KeyEvent event) {
        String aramaMetni = txtAra.getText().toLowerCase();
        emlakManager.emlakAra(aramaMetni);
    }

    private void secilenEmlakGoster(Emlak e) {
        if (e != null) {
            txtBaslik.setText(e.getBaslik());
            cmbTip.setValue(e.getTip());
            cmbDurum.setValue(e.getDurum());  // Yeni alan
            txtSehir.setText(e.getSehir());
            txtAdres.setText(e.getAdres());
            txtFiyat.setText(String.valueOf(e.getFiyat()));
            txtMetrekare.setText(String.valueOf(e.getMetrekare()));
            cmbOdaSayisi.setValue(e.getOdaSayisi());
        }
    }

    private boolean validateInput() {
        if (txtBaslik.getText().trim().isEmpty()) {
            alertGoster("Uyarı", "Başlık boş olamaz.");
            return false;
        }
        if (txtSehir.getText().trim().isEmpty()) {
            alertGoster("Uyarı", "Şehir girilmelidir.");
            return false;
        }
        if (txtAdres.getText().trim().isEmpty()) {
            alertGoster("Uyarı", "Adres boş olamaz.");
            return false;
        }
        if (txtFiyat.getText().trim().isEmpty()) {
            alertGoster("Uyarı", "Fiyat boş olamaz.");
            return false;
        }
        if (txtMetrekare.getText().trim().isEmpty()) {
            alertGoster("Uyarı", "Metrekare boş olamaz.");
            return false;
        }
        return true;
    }

    private void alertGoster(String baslik, String mesaj) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(baslik);
        alert.setHeaderText(null);
        alert.setContentText(mesaj);
        alert.showAndWait();
    }
}