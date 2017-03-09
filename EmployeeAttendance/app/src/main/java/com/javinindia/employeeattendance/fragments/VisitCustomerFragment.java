package com.javinindia.employeeattendance.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.javinindia.employeeattendance.BuildConfig;
import com.javinindia.employeeattendance.R;
import com.javinindia.employeeattendance.activity.ZingScannerActivity;
import com.javinindia.employeeattendance.apiparsing.AadharSacnParsingresponse.AadharResponse;
import com.javinindia.employeeattendance.constant.Constants;
import com.javinindia.employeeattendance.font.FontAsapRegularSingleTonClass;
import com.javinindia.employeeattendance.preference.SharedPreferencesManager;
import com.javinindia.employeeattendance.utility.CheckConnection;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ashish on 16-02-2017.
 */

public class VisitCustomerFragment extends BaseFragment implements View.OnClickListener,CheckConnectionFragment.OnCallBackInternetListener {
    private AppCompatTextView txtID, txtName, txtGender, txtYOB, txtCO, txtLM, txtVTC, txtPO, txtDist, txtState, txtPC;
    String result;
    LinearLayout llAadharDetail;
    ImageView imgProfilePic;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    private Uri mImageCaptureUri = null;

    private static final int PICK_FROM_CAMERA = 1;
    Bitmap photo = null;
    private File outPutFile = null;
    AppCompatEditText etDescription;
    private RequestQueue requestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        SharedPreferencesManager.setAadhar(activity, null);
        outPutFile = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (menu != null){
            menu.findItem(R.id.action_changePass).setVisible(false);
            menu.findItem(R.id.action_feedback).setVisible(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SharedPreferencesManager.getAadhar(activity) != null && !TextUtils.isEmpty(SharedPreferencesManager.getAadhar(activity))) {
            JSONObject jsonObj = null;
            result = SharedPreferencesManager.getAadhar(activity);
            try {
                jsonObj = XML.toJSONObject(result);
                if (jsonObj.length() > 0) {
                    llAadharDetail.setVisibility(View.VISIBLE);
                    AadharResponse aadharResponse = new AadharResponse();
                    aadharResponse.responseParseMethod(jsonObj);
                    if (aadharResponse.getUidAddhar() != 0) {
                        txtID.setText(aadharResponse.getUidAddhar() + "");
                    } else {
                        txtID.setText("");
                    }
                    if (!TextUtils.isEmpty(aadharResponse.getName())) {
                        txtName.setText(aadharResponse.getName());
                    } else {
                        txtName.setText("");
                    }

                    if (!TextUtils.isEmpty(aadharResponse.getGender())) {
                        txtGender.setText(aadharResponse.getGender());
                    } else {
                        txtGender.setText("");
                    }

                    if (aadharResponse.getYob() != 0) {
                        txtYOB.setText(aadharResponse.getYob() + "");
                    } else {
                        txtYOB.setText("");
                    }

                    if (!TextUtils.isEmpty(aadharResponse.getCo())) {
                        txtCO.setText(aadharResponse.getCo());
                    } else {
                        txtCO.setText("");
                    }

                    if (!TextUtils.isEmpty(aadharResponse.getLm())) {
                        txtLM.setText(aadharResponse.getLm());
                    } else {
                        txtLM.setText("");
                    }

                    if (!TextUtils.isEmpty(aadharResponse.getVtc())) {
                        txtVTC.setText(aadharResponse.getVtc());
                    } else {
                        txtVTC.setText("");
                    }

                    if (!TextUtils.isEmpty(aadharResponse.getPo())) {
                        txtPO.setText(aadharResponse.getPo());
                    } else {
                        txtPO.setText("");
                    }

                    if (!TextUtils.isEmpty(aadharResponse.getDist())) {
                        txtDist.setText(aadharResponse.getDist());
                    } else {
                        txtDist.setText("");
                    }

                    if (!TextUtils.isEmpty(aadharResponse.getState())) {
                        txtState.setText(aadharResponse.getState());
                    } else {
                        txtState.setText("");
                    }

                    if (aadharResponse.getPc() != 0) {
                        txtPC.setText(aadharResponse.getPc() + "");
                    } else {
                        txtPC.setText("");
                    }
                } else {
                    llAadharDetail.setVisibility(View.GONE);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initToolbar(view);
        initialization(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        outPutFile = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
    }

    private void initialization(View view) {
        etDescription  =(AppCompatEditText)view.findViewById(R.id.etDescription);
        imgProfilePic = (ImageView) view.findViewById(R.id.imgProfilePic);
        llAadharDetail = (LinearLayout) view.findViewById(R.id.llAadharDetail);
        txtID = (AppCompatTextView) view.findViewById(R.id.txtID);
        txtName = (AppCompatTextView) view.findViewById(R.id.txtName);
        txtGender = (AppCompatTextView) view.findViewById(R.id.txtGender);
        txtYOB = (AppCompatTextView) view.findViewById(R.id.txtYOB);
        txtCO = (AppCompatTextView) view.findViewById(R.id.txtCO);
        txtLM = (AppCompatTextView) view.findViewById(R.id.txtLM);
        txtVTC = (AppCompatTextView) view.findViewById(R.id.txtVTC);
        txtPO = (AppCompatTextView) view.findViewById(R.id.txtPO);
        txtDist = (AppCompatTextView) view.findViewById(R.id.txtDist);
        txtState = (AppCompatTextView) view.findViewById(R.id.txtState);
        txtPC = (AppCompatTextView) view.findViewById(R.id.txtPC);
        AppCompatButton btnSacnAddhar = (AppCompatButton) view.findViewById(R.id.btnSacnAddhar);
        AppCompatButton btnSubmitVisit = (AppCompatButton)view.findViewById(R.id.btnSubmitVisit);
        btnSubmitVisit.setOnClickListener(this);
        btnSacnAddhar.setOnClickListener(this);
        imgProfilePic.setOnClickListener(this);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.visit_customer_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }

    private void initToolbar(View view) {
        final Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onBackPressed();
            }
        });
        final ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle(null);
        AppCompatTextView textView = (AppCompatTextView) view.findViewById(R.id.tittle);
        textView.setText("VISIT CUSTOMER");
        textView.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSacnAddhar:
                txtID.setText("");
                txtName.setText("");
                txtGender.setText("");
                txtYOB.setText("");
                txtCO.setText("");
                txtLM.setText("");
                txtVTC.setText("");
                txtPO.setText("");
                txtDist.setText("");
                txtState.setText("");
                txtPC.setText("");
                SharedPreferencesManager.setAadhar(activity, null);
                Intent splashIntent = new Intent(activity, ZingScannerActivity.class);
                startActivity(splashIntent);
                break;
            case R.id.imgProfilePic:
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA);
                } else {
                    capture();
                }
                break;
            case R.id.btnSubmitVisit:
                if (CheckConnection.haveNetworkConnection(activity)) {
                    methodUpdateView();
                } else {
                    CheckConnectionFragment fragment = new CheckConnectionFragment();
                    fragment.setMyCallBackInternetListener(this);
                    callFragmentMethod(fragment, this.getClass().getSimpleName(), R.id.container);
                }
                break;
        }
    }

    public void capture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mImageCaptureUri = FileProvider.getUriForFile(activity,
                    "com.javinindia.employeeattendance.provider",
                    getOutputMediaFile());


        } else {
            mImageCaptureUri = Uri.fromFile(getOutputMediaFile());
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    private void methodUpdateView() {
        String aID = txtID.getText().toString().trim();
        String Disc = etDescription.getText().toString().trim();
        if (TextUtils.isEmpty(aID)) {
            Toast.makeText(activity, "Please scan your aadhar card", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(Disc)) {
            Toast.makeText(activity, "Please write description", Toast.LENGTH_LONG).show();
        }   else {
            methodSubbmit(aID,Disc);
        }
    }

    private void methodSubbmit(final String aID, final String disc) {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.INSERT_CUSTOMER_VISIT_EMPLOYEE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int status=0;
                        String msg = null;
                        loading.dismiss();

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            if (jsonObject.has("status"))
                                status = jsonObject.optInt("status");
                            if (jsonObject.has("msg"))
                                msg = jsonObject.optString("msg");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (status == 1) {
                            Toast.makeText(activity, "Your visit successfully submitted", Toast.LENGTH_LONG).show();
                            activity.onBackPressed();
                        } else {
                            if (!TextUtils.isEmpty(msg)) {
                                showDialogMethod(msg);
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        volleyErrorHandle(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", SharedPreferencesManager.getUserID(activity));
                params.put("adhar_number", aID);
                params.put("name", txtName.getText().toString().trim());
                params.put("gender", txtGender.getText().toString().trim());
                params.put("yob", txtYOB.getText().toString().trim());
                params.put("care_of", txtCO.getText().toString().trim());
                params.put("landmark", txtLM.getText().toString().trim());
                params.put("village_town_city", txtVTC.getText().toString().trim());
                params.put("post_office", txtPO.getText().toString().trim());
                params.put("district",txtDist.getText().toString().trim());
                params.put("state",txtState.getText().toString().trim());
                params.put("pincode",txtPO.getText().toString().trim());
                params.put("desp",disc);
                if (photo != null) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    byte[] data = bos.toByteArray();
                    String encodedImage = Base64.encodeToString(data, Base64.DEFAULT);
                    params.put("pic", encodedImage + "image/jpeg");
                } else {
                    params.put("pic", "");
                }

                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    @Override
    public void OnCallBackInternet() {

    }


    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FROM_CAMERA && resultCode == Activity.RESULT_OK) {
            if (outPutFile.exists()) {
                try {

                    InputStream imageStream = activity.getContentResolver().openInputStream(mImageCaptureUri);
                    photo = BitmapFactory.decodeStream(imageStream);
                    photo = getResizedBitmap(photo, 700);
                    imgProfilePic.setImageBitmap(photo);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(activity, "Error while save image", Toast.LENGTH_SHORT).show();
            }
        } else {

        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    capture();
                } else {
                    Toast.makeText(activity, "You Denied for camera permission so you cant't update image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
