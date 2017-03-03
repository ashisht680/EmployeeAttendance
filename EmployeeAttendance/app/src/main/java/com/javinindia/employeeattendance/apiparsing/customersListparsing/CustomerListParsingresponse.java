package com.javinindia.employeeattendance.apiparsing.customersListparsing;

import android.util.Log;

import com.javinindia.employeeattendance.apiparsing.base.ApiBaseData;
import com.javinindia.employeeattendance.apiparsing.loginsignupparsing.Details;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ashish on 20-02-2017.
 */

public class CustomerListParsingresponse extends ApiBaseData {
    private ArrayList<CustomerDetails> customerDetailsArrayList;

    public ArrayList<CustomerDetails> getCustomerDetailsArrayList() {
        return customerDetailsArrayList;
    }

    public void setCustomerDetailsArrayList(ArrayList<CustomerDetails> customerDetailsArrayList) {
        this.customerDetailsArrayList = customerDetailsArrayList;
    }

    public void responseParseMethod(Object response) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            setMsg(jsonObject.optString("msg"));
            setStatus(jsonObject.optInt("status"));
            if (jsonObject.optInt("status") == 1 && jsonObject.has("details") && jsonObject.optJSONArray("details") != null)
                setCustomerDetailsArrayList(getDetailMethod(jsonObject.optJSONArray("details")));

            Log.d("Response", this.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<CustomerDetails> getDetailMethod(JSONArray details) {
        ArrayList<CustomerDetails> empDeatil = new ArrayList<>();
        for (int i = 0; i < details.length(); i++) {
            CustomerDetails info = new CustomerDetails();
            JSONObject jsonObject = details.optJSONObject(i);
            if (jsonObject.has("id"))
                info.setId(jsonObject.optString("id"));
            if (jsonObject.has("trav_id"))
                info.setTrav_id(jsonObject.optString("trav_id"));
            if (jsonObject.has("adhar_number"))
                info.setAdhar_number(jsonObject.optString("adhar_number"));
            if (jsonObject.has("name"))
                info.setName(jsonObject.optString("name"));
            if (jsonObject.has("gender"))
                info.setGender(jsonObject.optString("gender"));
            if (jsonObject.has("yob"))
                info.setYob(jsonObject.optString("yob"));
            if (jsonObject.has("adhar_pic"))
                info.setAdhar_pic(jsonObject.optString("adhar_pic"));
            if (jsonObject.has("description"))
                info.setDescription(jsonObject.optString("description"));
            if (jsonObject.has("care_of"))
                info.setCare_of(jsonObject.optString("care_of"));
            if (jsonObject.has("landmark"))
                info.setLandmark(jsonObject.optString("landmark"));
            if (jsonObject.has("village_town_city"))
                info.setVillage_town_city(jsonObject.optString("village_town_city"));
            if (jsonObject.has("post_office"))
                info.setPost_office(jsonObject.optString("post_office"));
            if (jsonObject.has("district"))
                info.setDistrict(jsonObject.optString("district"));
            if (jsonObject.has("state"))
                info.setState(jsonObject.optString("state"));
            if (jsonObject.has("pincode"))
                info.setPincode(jsonObject.optString("pincode"));
            if (jsonObject.has("insertDate"))
                info.setInsertDate(jsonObject.optString("insertDate"));

            empDeatil.add(info);
            Log.d("Response", empDeatil.toString());
        }
        return empDeatil;
    }
}
