package com.javinindia.employeeattendance.apiparsing.Attendanceparsing;

/**
 * Created by Ashish on 20-02-2017.
 */

public class adetails {

    private String attend_id;
    private String trav_id;
    private String location;
    private String lat;
    private String lng;
    private String insertDate;
    private String description;

    public String getAttend_id() {
        return attend_id;
    }

    public void setAttend_id(String attend_id) {
        this.attend_id = attend_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTrav_id() {
        return trav_id;
    }

    public void setTrav_id(String trav_id) {
        this.trav_id = trav_id;
    }
}
