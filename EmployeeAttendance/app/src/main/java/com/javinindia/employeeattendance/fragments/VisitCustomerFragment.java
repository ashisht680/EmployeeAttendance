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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.CAMERA;

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
    private static final int CROP_FROM_CAMERA = 2;
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
                Log.e("aadhar",jsonObj.toString());
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
                    try {
                        img();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
                        //{"status":1,"msg":"success"}
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

    private void img() throws IOException {

        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                cameraIntent();
            } else {
                requestPermission();
            }
        } else {
            cameraIntent();
        }

    }


    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(activity, CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(activity, new String[]{CAMERA}, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        img();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(activity, "You Denied for camera permission so you cant't update image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void OnCallBackInternet() {

    }


    public class CropOptionAdapter extends ArrayAdapter<CropOption> {
        private ArrayList<CropOption> mOptions;
        private LayoutInflater mInflater;

        public CropOptionAdapter(Context context, ArrayList<CropOption> options) {
            super(context, R.layout.crop_selector, options);

            mOptions = options;

            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup group) {
            if (convertView == null)
                convertView = mInflater.inflate(R.layout.crop_selector, null);

            CropOption item = mOptions.get(position);

            if (item != null) {
                ((ImageView) convertView.findViewById(R.id.iv_icon))
                        .setImageDrawable(item.icon);
                ((TextView) convertView.findViewById(R.id.tv_name))
                        .setText(item.title);

                return convertView;
            }

            return null;
        }
    }

    public class CropOption {
        public CharSequence title;
        public Drawable icon;
        public Intent appIntent;
    }

    public Bitmap decodeFile(String filePath) {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);
        final int REQUIRED_SIZE = 1024;
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, o2);
        return bitmap;
    }

    private void cameraIntent() throws IOException {


        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion > Build.VERSION_CODES.M) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
            mImageCaptureUri = FileProvider.getUriForFile(activity,
                    BuildConfig.APPLICATION_ID + ".provider",
                    f);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            startActivityForResult(intent, PICK_FROM_CAMERA);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            mImageCaptureUri = Uri.fromFile(getOutputMediaFile());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            startActivityForResult(intent, PICK_FROM_CAMERA);
        }

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
        /*if (requestCode == PICK_FROM_FILE && resultCode == Activity.RESULT_OK && null != data) {
            mImageCaptureUri = data.getData();
            doCrop();
        } else*/
        if (requestCode == PICK_FROM_CAMERA && resultCode == Activity.RESULT_OK) {
            doCrop();
        } else if (requestCode == CROP_FROM_CAMERA) {
            try {
                if (outPutFile.exists()) {
                    photo = decodeFile(outPutFile.getAbsolutePath());
                    imgProfilePic.setImageBitmap(photo);
                    // profilePicUrlSetMethod();
                } else {
                    Toast.makeText(activity, "Error while save image", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void doCrop() {
        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        List<ResolveInfo> list = activity.getPackageManager().queryIntentActivities(
                intent, 0);

        int size = list.size();
        if (size == 0) {
            Toast.makeText(activity, "Can not find image crop app",
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            intent.setData(mImageCaptureUri);
            intent.putExtra("outputX", 512);
            intent.putExtra("outputY", 512);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outPutFile.getAbsoluteFile()));

            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);
                i.setComponent(new ComponentName(res.activityInfo.packageName,
                        res.activityInfo.name));
                startActivityForResult(i, CROP_FROM_CAMERA);
            } else {
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();
                    co.title = activity.getPackageManager().getApplicationLabel(
                            res.activityInfo.applicationInfo);
                    co.icon = activity.getPackageManager().getApplicationIcon(
                            res.activityInfo.applicationInfo);
                    co.appIntent = new Intent(intent);
                    co.appIntent
                            .setComponent(new ComponentName(
                                    res.activityInfo.packageName,
                                    res.activityInfo.name));
                    cropOptions.add(co);
                }

                CropOptionAdapter adapter = new CropOptionAdapter(
                        activity.getApplicationContext(), cropOptions);
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Choose Crop App");
                builder.setCancelable(false);
                builder.setAdapter(adapter,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                startActivityForResult(
                                        cropOptions.get(item).appIntent,
                                        CROP_FROM_CAMERA);
                            }
                        });

                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                        if (mImageCaptureUri != null) {
                            activity.getContentResolver().delete(mImageCaptureUri, null,
                                    null);
                            mImageCaptureUri = null;
                        }
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }
}
