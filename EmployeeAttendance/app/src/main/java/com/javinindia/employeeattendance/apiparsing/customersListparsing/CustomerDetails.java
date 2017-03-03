package com.javinindia.employeeattendance.apiparsing.customersListparsing;

/**
 * Created by Ashish on 20-02-2017.
 */

public class CustomerDetails {

    private String id;
    private String trav_id;
    private String adhar_number;
    private String name;
    private String gender;
    private String yob;
    private String adhar_pic;
    private String description;
    private String care_of;
    private String landmark;
    private String village_town_city;
    private String post_office;
    private String district;
    private String state;
    private String pincode;
    private String insertDate;

    public String getAdhar_number() {
        return adhar_number;
    }

    public void setAdhar_number(String adhar_number) {
        this.adhar_number = adhar_number;
    }

    public String getAdhar_pic() {
        return adhar_pic;
    }

    public void setAdhar_pic(String adhar_pic) {
        this.adhar_pic = adhar_pic;
    }

    public String getCare_of() {
        return care_of;
    }

    public void setCare_of(String care_of) {
        this.care_of = care_of;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPost_office() {
        return post_office;
    }

    public void setPost_office(String post_office) {
        this.post_office = post_office;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTrav_id() {
        return trav_id;
    }

    public void setTrav_id(String trav_id) {
        this.trav_id = trav_id;
    }

    public String getVillage_town_city() {
        return village_town_city;
    }

    public void setVillage_town_city(String village_town_city) {
        this.village_town_city = village_town_city;
    }

    public String getYob() {
        return yob;
    }

    public void setYob(String yob) {
        this.yob = yob;
    }
}
