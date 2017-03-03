package com.javinindia.employeeattendance.apiparsing.AadharSacnParsingresponse;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ashish on 17-02-2017.
 */

public class AadharResponse {

    private int uid;
    private long uidAddhar;
    private String name;
    private String gender;
    private int yob;
    private String co;
    private String lm;
    private String loc;
    private String vtc;
    private String po;
    private String dist;
    private String state;
    private int pc;

    public long getUidAddhar() {
        return uidAddhar;
    }

    public void setUidAddhar(long uidAddhar) {
        this.uidAddhar = uidAddhar;
    }

    public String getCo() {
        return co;
    }

    public void setCo(String co) {
        this.co = co;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLm() {
        return lm;
    }

    public void setLm(String lm) {
        this.lm = lm;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPc() {
        return pc;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }

    public String getPo() {
        return po;
    }

    public void setPo(String po) {
        this.po = po;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getVtc() {
        return vtc;
    }

    public void setVtc(String vtc) {
        this.vtc = vtc;
    }

    public int getYob() {
        return yob;
    }

    public void setYob(int yob) {
        this.yob = yob;
    }

    public void responseParseMethod(Object response) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            if (jsonObject.has("PrintLetterBarcodeData") && jsonObject.optJSONObject("PrintLetterBarcodeData") != null) {

                if (jsonObject.optJSONObject("PrintLetterBarcodeData").has("name"))
                    setName(jsonObject.optJSONObject("PrintLetterBarcodeData").getString("name"));
                if (jsonObject.optJSONObject("PrintLetterBarcodeData").has("gender"))
                    setGender(jsonObject.optJSONObject("PrintLetterBarcodeData").getString("gender"));
                if (jsonObject.optJSONObject("PrintLetterBarcodeData").has("yob"))
                    setYob(jsonObject.optJSONObject("PrintLetterBarcodeData").getInt("yob"));
                if (jsonObject.optJSONObject("PrintLetterBarcodeData").has("co"))
                    setCo(jsonObject.optJSONObject("PrintLetterBarcodeData").getString("co"));
                if (jsonObject.optJSONObject("PrintLetterBarcodeData").has("lm"))
                    setLm(jsonObject.optJSONObject("PrintLetterBarcodeData").getString("lm"));
                if (jsonObject.optJSONObject("PrintLetterBarcodeData").has("vtc"))
                    setVtc(jsonObject.optJSONObject("PrintLetterBarcodeData").getString("vtc"));
                if (jsonObject.optJSONObject("PrintLetterBarcodeData").has("po"))
                    setPo(jsonObject.optJSONObject("PrintLetterBarcodeData").getString("po"));
                if (jsonObject.optJSONObject("PrintLetterBarcodeData").has("dist"))
                    setDist(jsonObject.optJSONObject("PrintLetterBarcodeData").getString("dist"));
                if (jsonObject.optJSONObject("PrintLetterBarcodeData").has("state"))
                    setState(jsonObject.optJSONObject("PrintLetterBarcodeData").getString("state"));
                if (jsonObject.optJSONObject("PrintLetterBarcodeData").has("pc"))
                    setPc(jsonObject.optJSONObject("PrintLetterBarcodeData").getInt("pc"));
                if (jsonObject.optJSONObject("PrintLetterBarcodeData").has("uid")){
                    setUidAddhar(jsonObject.optJSONObject("PrintLetterBarcodeData").getLong("uid"));
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
