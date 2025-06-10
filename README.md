# Emlak Listeleme ve Yönetim Uygulaması

Bu proje, Java ve JavaFX kullanılarak geliştirilmiş bir masaüstü emlak listeleme ve yönetim uygulamasıdır. Kullanıcıların yeni emlak ilanları eklemesine, mevcut ilanları görüntülemesine, detaylarını güncellemesine, silmesine, filtrelemesine ve çeşitli kriterlere göre sıralamasına olanak tanır.

## Özellikler

* **Kullanıcı Dostu Arayüz**: JavaFX ile geliştirilmiş modern ve etkileşimli grafiksel kullanıcı arayüzü.
* **İlan Yönetimi**:
    * Yeni emlak ilanı ekleme.
    * Mevcut emlak ilanlarını güncelleme.
    * Emlak ilanlarını silme.
* **Veri Yönetimi**:
    * Emlak ilanlarını başlık, adres, şehir, tip veya duruma göre arama.
    * Şehire göre filtreleme.
    * Duruma (Satılık/Kiralık) göre filtreleme.
    * Fiyata göre sıralama.
    * Favori ilanları belirleme ve görüntüleme.
    * Son yapılan ekleme, silme veya güncelleme işlemlerini geri alma özelliği.
* **Veri Kalıcılığı**: Emlak ilanları JSON formatında `emlaklar.json` dosyasına kaydedilir ve uygulaması başlatıldığında bu dosyadan yüklenir.
* **Özel Veri Yapıları**: Projede performans ve öğrenme amacıyla kendi implementasyonlarımız olan özel veri yapıları kullanılmıştır:
    * **Liste**: Genel ilan listesi için kullanılır.
    * **HashMap**: Şehir bazlı ilanları gruplandırmak için kullanılır.
    * **TreeSet**: Emlakları fiyata göre sıralı tutmak için kullanılır.
    * **LinkedList**: Favori ilanları yönetmek için kullanılır.
    * **Stack**: Geri alma (undo) işlemlerini yönetmek için kullanılır.


## Gereksinimler

* Java Development Kit (JDK) 17 veya üzeri
* Java FX
* Maven

## Kurulum ve Çalıştırma

1.  Projeyi klonlayın:
    ```bash
    git clone [https://github.com/umutakksy/emlaklisteleme.git](https://github.com/umutakksy/emlaklisteleme.git)
    cd emlaklisteleme
    ```
2.  Maven bağımlılıklarını yükleyin:
    ```bash
    mvn clean install
    ```
3.  Uygulamayı çalıştırın:
    ```bash
    mvn javafx:run
    ```

## Kullanım

Uygulama açıldığında bir emlak listeleme arayüzü göreceksiniz.

* **İlan Ekle**: Sol taraftaki form alanlarını doldurarak ve "Ekle" butonuna tıklayarak yeni ilan ekleyebilirsiniz.
* **İlan Güncelle**: Tablodan bir ilan seçin, sağdaki form alanlarını güncelleyin ve "Güncelle" butonuna tıklayın.
* **İlan Sil**: Tablodan bir ilan seçin ve "Sil" butonuna tıklayın.
* **Arama**: "Ara" metin kutusuna anahtar kelimeler girerek ilanları filtreleyin.
* **Filtreleme ve Sıralama**: Şehir, durum veya fiyata göre ilanları filtrelemek/sıralamak için ilgili butonları kullanın.
* **Favoriler**: Bir ilanı seçip "Favoriye Ekle" butonuna tıklayarak favorilerinize ekleyebilirsiniz. "Favorileri Göster" ile sadece favori ilanları listeleyebilirsiniz.
* **Geri Al**: "Geri Al" butonu ile son yapılan işlemi geri alabilirsiniz.
* **Tümünü Göster**: Filtreleri temizleyip tüm ilanları tekrar görmek için "Tümünü Göster" butonuna tıklayın.
