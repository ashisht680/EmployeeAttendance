package com.javinindia.employeeattendance.apiparsing.employeeParsingResponse;

import android.util.Log;

import com.javinindia.employeeattendance.apiparsing.base.ApiBaseData;
import com.javinindia.employeeattendance.apiparsing.loginsignupparsing.Details;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ashish on 21-02-2017.
 */

public class EmployeeResponse extends ApiBaseData {
    private ArrayList<Details> detailsArrayList;

    public ArrayList<Details> getDetailsArrayList() {
        return detailsArrayList;
    }

    public void setDetailsArrayList(ArrayList<Details> detailsArrayList) {
        this.detailsArrayList = detailsArrayList;
    }

    public void responseParseMethod(Object response) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            setMsg(jsonObject.optString("msg"));
            setStatus(jsonObject.optInt("status"));
            if (jsonObject.optInt("status") == 1 && jsonObject.has("list") && jsonObject.optJSONArray("list") != null)
                setDetailsArrayList(getDetailMethod(jsonObject.optJSONArray("list")));

            Log.d("Response", this.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Details> getDetailMethod(JSONArray details) {
        ArrayList<Details> empDeatil = new ArrayList<>();
        for (int i = 0; i < details.length(); i++) {
            Details info = new Details();
            JSONObject jsonObject = details.optJSONObject(i);
            if (jsonObject.has("emp_id"))
                info.setEmp_id(jsonObject.optString("emp_id"));
            if (jsonObject.has("id"))
                info.setId(jsonObject.optString("id"));
            if (jsonObject.has("name"))
                info.setName(jsonObject.optString("name"));
            if (jsonObject.has("email"))
                info.setEmail(jsonObject.optString("email"));
            if (jsonObject.has("mobile"))
                info.setMobile(jsonObject.optString("mobile"));
            if (jsonObject.has("alt_mobile"))
                info.setAlt_mobile(jsonObject.optString("alt_mobile"));
            if (jsonObject.has("password"))
                info.setPassword(jsonObject.optString("password"));
            if (jsonObject.has("state"))
                info.setState(jsonObject.optString("state"));
            if (jsonObject.has("city"))
                info.setCity(jsonObject.optString("city"));
            if (jsonObject.has("profile_pic"))
                info.setProfile_pic(jsonObject.optString("profile_pic"));
            if (jsonObject.has("address"))
                info.setAddress(jsonObject.optString("address"));
            if (jsonObject.has("insertDate"))
                info.setInsertDate(jsonObject.optString("insertDate"));
            if (jsonObject.has("updateDate"))
                info.setUpdateDate(jsonObject.optString("updateDate"));
            if (jsonObject.has("status"))
                info.setStatus(jsonObject.optString("status"));
            if (jsonObject.has("pincode"))
                info.setPincode(jsonObject.optString("pincode"));
            if (jsonObject.has("device_token"))
                info.setDevice_token(jsonObject.optString("device_token"));

            empDeatil.add(info);
            Log.d("Response", empDeatil.toString());
        }
        return empDeatil;
    }
}
