# TradeBot

**TradeBot**, Java ile geliştirilmiş bir kripto para alım-satım botudur. Binance Futures API'si ile entegre çalışarak, kullanıcı tanımlı stratejilere göre otomatik işlemler gerçekleştirir. Kaldıraç, pozisyon büyüklüğü, TP/SL gibi işlemler dinamik olarak hesaplanır.

## 🚀 Özellikler

- ✅ Binance Futures API entegrasyonu
- 📊 Gerçek zamanlı veri akışı ile analiz
- 🧠 Otomatik pozisyon açma ve kapama
- ⚙️ Dinamik kaldıraç ve pozisyon büyüklüğü hesaplaması
- 🛡️ TP (take profit) / SL (stop loss) desteği
- 📁 Basit yapı – strateji geliştirmeye açık

## 🔧 Gereksinimler

- Java 11+
- Maven
- Binance API Key ve Secret

## ⚙️ Kurulum

### 1. Depoyu Klonla

```bash
git clone https://github.com/halitdvc/TradeBot.git
cd TradeBot
```

### 2. Ortam Degiskenlerini ayarla

```bash
BINANCE_API_KEY=your_api_key
BINANCE_SECRET_KEY=your_secret_key
```

### 3. Uygulamayi BotMainKaldiracli dan calistir


Birden Fazla hesap ve coin yonetimi icin thread kullanir.

## ⚙️ Kurulum

### 1. Tablo olustur

```bash
-- Table: public.anahesaplar

-- DROP TABLE IF EXISTS public.anahesaplar;

CREATE TABLE IF NOT EXISTS public.anahesaplar
(
    id integer NOT NULL DEFAULT nextval('anahesaplar_id_seq'::regclass),
    "apiKey" character varying COLLATE pg_catalog."default",
    "secretKey" character varying COLLATE pg_catalog."default",
    hangicoin character varying COLLATE pg_catalog."default" DEFAULT 'ETHUSDC'::character varying,
    isactive boolean DEFAULT false,
    "kullaniciAdi" character varying COLLATE pg_catalog."default" DEFAULT 'halit'::character varying,
    "kullaniciSoyadi" character varying COLLATE pg_catalog."default" DEFAULT 'deveci'::character varying,
    CONSTRAINT anahesaplar_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.anahesaplar
    OWNER to postgres;
```

### 2. Ortam Degiskenlerini ayarla

```bash
DB_URL=""
DB_USER= ""
DB_PASSWORD = ""
```
### 3. Uygulamayi CokluHesapYonetimi den calistir


MIT License © halitdvc
