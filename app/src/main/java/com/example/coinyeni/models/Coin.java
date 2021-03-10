package com.example.coinyeni.models;

public class Coin {
    private String ad;
    private String miktar;
    private String fiyat;
    private String tip;
    private String anlikfiyat;

    public Coin(String ad, String miktar, String fiyat, String tip, String anlikfiyat) {
        this.ad = ad;
        this.miktar = miktar;
        this.fiyat = fiyat;
        this.tip = tip;
        this.anlikfiyat = anlikfiyat;
    }

    public String getAd() {
        return ad;
    }

    public String getMiktar() {
        return miktar;
    }

    public String getFiyat() {
        return fiyat;
    }

    public String getTip() {
        return tip;
    }

    public String getAnlikfiyat() {
        return anlikfiyat;
    }

    public String getYuzdeKar() {
        float deger = Float.parseFloat(miktar)*Float.parseFloat(anlikfiyat);
        float verilen = Float.parseFloat(miktar)*Float.parseFloat(fiyat);
        return "% "+String.format("%.2f", (((deger-verilen)*100)/verilen));
    }

    public float getKar() {
        float deger = Float.parseFloat(miktar)*Float.parseFloat(anlikfiyat);
        float verilen = Float.parseFloat(miktar)*Float.parseFloat(fiyat);
        return (deger-verilen);
    }

    public float getDeger() {
        return Float.parseFloat(miktar)*Float.parseFloat(anlikfiyat);
    }
}
