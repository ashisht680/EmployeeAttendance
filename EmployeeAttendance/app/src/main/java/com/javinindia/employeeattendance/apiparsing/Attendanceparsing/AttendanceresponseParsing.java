package com.javinindia.employeeattendance.apiparsing.Attendanceparsing;

import android.util.Log;

import com.javinindia.employeeattendance.apiparsing.base.ApiBaseData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ashish on 20-02-2017.
 */

public class AttendanceresponseParsing extends ApiBaseData {

    ArrayList<AttandanceDetails> attandanceDetailsArrayList;

    public ArrayList<AttandanceDetails> getAttandanceDetailsArrayList() {
        return attandanceDetailsArrayList;
    }

    public void setAttandanceDetailsArrayList(ArrayList<AttandanceDetails> attandanceDetailsArrayList) {
        this.attandanceDetailsArrayList = attandanceDetailsArrayList;
    }

    public void responseParseMethod(Object response) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            setMsg(jsonObject.optString("msg"));
            setStatus(jsonObject.optInt("status"));
            if (jsonObject.optInt("status")==1 && jsonObject.has("details"))
                setAttandanceDetailsArrayList(getAttDetailInMethod(jsonObject.optJSONArray("details")));

            Log.d("Response", this.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<AttandanceDetails> getAttDetailInMethod(JSONArray details) {
        ArrayList<AttandanceDetails> wishDetailsArrayList = new ArrayList<>();
        for (int i = 0; i < details.length(); i++) {
            AttandanceDetails wishDetails = new AttandanceDetails();
            JSONObject jsonObject = details.optJSONObject(i);

            if (jsonObject.has("adetails"))
                wishDetails.setAdetails(getAdetailsMethod(jsonObject.optJSONObject("adetails")));
            if (jsonObject.has("attend_details_images"))
                wishDetails.setDetails_imagesArrayList(getPostImageMethod(jsonObject.optJSONArray("attend_details_images")));


            wishDetailsArrayList.add(wishDetails);
            Log.d("Response 2", wishDetailsArrayList.toString());
        }
        return wishDetailsArrayList;
    }

    private adetails getAdetailsMethod(JSONObject jsonObject) {
        adetails notificationDetails = new adetails();
        if (jsonObject.has("attend_id"))
            notificationDetails.setAttend_id(jsonObject.optString("attend_id"));
        if (jsonObject.has("trav_id"))
            notificationDetails.setTrav_id(jsonObject.optString("trav_id"));
        if (jsonObject.has("location"))
            notificationDetails.setLocation(jsonObject.optString("location"));
        if (jsonObject.has("lat"))
            notificationDetails.setLat(jsonObject.optString("lat"));
        if (jsonObject.has("lng"))
            notificationDetails.setLng(jsonObject.optString("lng"));
        if (jsonObject.has("insertDate"))
            notificationDetails.setInsertDate(jsonObject.optString("insertDate"));
        if (jsonObject.has("description"))
            notificationDetails.setDescription(jsonObject.optString("description"));

        return notificationDetails;
    }

    private ArrayList<attend_details_images> getPostImageMethod(JSONArray jsonArray) {
        ArrayList<attend_details_images> postImageArrayList = new ArrayList<>();
        attend_details_images postImage;
        for (int i = 0; i < jsonArray.length(); i++) {
            postImage = new attend_details_images();
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject.has("id"))
                postImage.setId(jsonObject.optString("id"));
            if (jsonObject.has("attend_id"))
                postImage.setAttend_id(jsonObject.optString("attend_id"));
            if (jsonObject.has("image_name"))
                postImage.setImage_name(jsonObject.optString("image_name"));
            postImageArrayList.add(postImage);
        }
        return postImageArrayList;
    }
}
