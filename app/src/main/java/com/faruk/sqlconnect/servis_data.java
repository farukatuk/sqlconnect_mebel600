package com.faruk.sqlconnect;

public class servis_data {
    private String sKayNo;
    private String sMakNo;
    private String sMarka;
    private String sModel;
    private String sSeriNo;
    private String sUnvan;
    private String sDurum;
    private String sGelTarih;
    private String sGelSaat;
    private String sTeknisyen;
    private String sSikayet;
    private String sYetkili;
    private String sAlan;

    public servis_data(String sKayNo, String sMakNo, String sMarka, String sModel, String sSeriNo,
                       String sUnvan, String sDurum, String sGelTarih, String sGelSaat, String sTeknisyen, String sSikayet, String sYetkili, String sAlan) {
        this.sKayNo = sKayNo;
        this.sMakNo = sMakNo;
        this.sMarka = sMarka;
        this.sModel = sModel;
        this.sSeriNo = sSeriNo;
        this.sUnvan = sUnvan;
        this.sDurum = sDurum;
        this.sGelTarih = sGelTarih;
        this.sGelSaat = sGelSaat;
        this.sTeknisyen = sTeknisyen;
        this.sSikayet = sSikayet;
        this.sYetkili = sYetkili;
        this.sAlan = sAlan;
    }

    public String getsKayNo() {
        return sKayNo;
    }

    public void setsKayNo(String sKayNo) {
        this.sKayNo = sKayNo;
    }

    public String getsMakNo() {
        return sMakNo;
    }

    public void setsMakNo(String sMakNo) {
        this.sMakNo = sMakNo;
    }

    public String getsMarka() {
        return sMarka;
    }

    public void setsMarka(String sMarka) {
        this.sMarka = sMarka;
    }

    public String getsModel() {
        return sModel;
    }

    public void setsModel(String sModel) {
        this.sModel = sModel;
    }

    public String getsSeriNo() {
        return sSeriNo;
    }

    public void setsSeriNo(String sSeriNo) {
        this.sSeriNo = sSeriNo;
    }

    public String getsUnvan() {
        return sUnvan;
    }

    public void setsUnvan(String sUnvan) {
        this.sUnvan = sUnvan;
    }

    public String getsDurum() {
        return sDurum;
    }

    public void setsDurum(String sDurum) {
        this.sDurum = sDurum;
    }

    public String getsGelTarih() {
        return sGelTarih;
    }

    public void setsGelTarih(String sGelTarih) {
        this.sGelTarih = sGelTarih;
    }

    public String getsGelSaat() {
        return sGelSaat;
    }

    public void setsGelSaat(String sGelSaat) {
        this.sGelSaat = sGelSaat;
    }

    public String getsTeknisyen() {
        return sTeknisyen;
    }

    public void setsTeknisyen(String sTeknisyen) {
        this.sTeknisyen = sTeknisyen;
    }

    public String getsSikayet() {
        return sSikayet;
    }

    public void setsSikayet(String sSikayet) {
        this.sSikayet = sSikayet;
    }


    public String getsYetkili() {
        return sYetkili;
    }

    public void setsYetkili(String sSikayet) {
        this.sYetkili = sYetkili;
    }

    public String getsAlan() {
        return sAlan;
    }

    public void setsAlan(String sSikayet) {
        this.sAlan = sAlan;
    }

}
