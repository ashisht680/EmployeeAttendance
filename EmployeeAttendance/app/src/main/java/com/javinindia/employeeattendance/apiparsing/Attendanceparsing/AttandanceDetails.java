package com.javinindia.employeeattendance.apiparsing.Attendanceparsing;

import java.util.ArrayList;

/**
 * Created by Ashish on 20-02-2017.
 */

public class AttandanceDetails {
    private adetails adetails;
    private ArrayList<attend_details_images> details_imagesArrayList;

    public com.javinindia.employeeattendance.apiparsing.Attendanceparsing.adetails getAdetails() {
        return adetails;
    }

    public void setAdetails(com.javinindia.employeeattendance.apiparsing.Attendanceparsing.adetails adetails) {
        this.adetails = adetails;
    }

    public ArrayList<attend_details_images> getDetails_imagesArrayList() {
        return details_imagesArrayList;
    }

    public void setDetails_imagesArrayList(ArrayList<attend_details_images> details_imagesArrayList) {
        this.details_imagesArrayList = details_imagesArrayList;
    }
}
