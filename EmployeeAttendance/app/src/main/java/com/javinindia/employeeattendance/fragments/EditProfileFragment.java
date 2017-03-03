package com.javinindia.employeeattendance.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.javinindia.employeeattendance.R;
import com.javinindia.employeeattendance.apiparsing.loginsignupparsing.EmployeeLoginSignupResponse;
import com.javinindia.employeeattendance.apiparsing.stateparsing.CityMasterParsing;
import com.javinindia.employeeattendance.apiparsing.stateparsing.CountryMasterApiParsing;
import com.javinindia.employeeattendance.constant.Constants;
import com.javinindia.employeeattendance.font.FontAsapRegularSingleTonClass;
import com.javinindia.employeeattendance.picasso.CircleTransform;
import com.javinindia.employeeattendance.preference.SharedPreferencesManager;
import com.javinindia.employeeattendance.utility.Utility;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ashish on 12-10-2016.
 */
public class EditProfileFragment extends BaseFragment implements View.OnClickListener {
    private RequestQueue requestQueue;

    ImageView imgProfilePic, imgProfilePicNotFound;
    AppCompatEditText etStoreName, etOwner, etEmailAddress, etMobile, etLandLine, etState, etCity, etPin, etAddress;
    RelativeLayout rlUpadteImg;
    AppCompatTextView txtUpdate, txtOwnerHd, txtEmailHd, txtMobileHd, txtLandLineHd, txtStateHd, txtCityHd, txtPinHd, txtAddressHd;
    // AppCompatTextView txtAddNewCategory;

    Calendar calendar;
    TimePickerDialog timepickerdialog;
    private int CalendarHour, CalendarMinute;
    String format, mallId;

    public ArrayList<String> stateList = new ArrayList<>();
    String stateArray[] = null;
    public ArrayList<String> cityList = new ArrayList<>();
    String cityArray[] = null;
    public ArrayList<String> mallList = new ArrayList<>();
    String mallArray[] = null;

    private Uri mImageCaptureUri;
    private ImageView mImageView;
    private android.app.AlertDialog dialog;

    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;
    Bitmap photo = null;
    String sPic;
    int size = 0;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;

    private File outPutFile = null;

    //  public RecyclerView gridTags;
    // private ShopCategoryAdaptar adaptar;

    private OnCallBackEditProfileListener onCallBackEditProfile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public interface OnCallBackEditProfileListener {
        void OnCallBackEditProfile();
    }

    public void setMyCallBackOfferListener(OnCallBackEditProfileListener callback) {
        this.onCallBackEditProfile = callback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        initToolbar(view);
        initialize(view);

        return view;
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
        textView.setText("Edit Profile");
        textView.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        sendDataOnRegistrationApi();
        captureImageInitialization();
        outPutFile = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (menu != null) {
            menu.findItem(R.id.action_changePass).setVisible(false);
            menu.findItem(R.id.action_feedback).setVisible(false);
        }
    }

    private void initialize(View view) {

        imgProfilePicNotFound = (ImageView) view.findViewById(R.id.imgProfilePicNotFound);
        imgProfilePic = (ImageView) view.findViewById(R.id.imgProfilePic);
        rlUpadteImg = (RelativeLayout) view.findViewById(R.id.rlUpadteImg);
        etStoreName = (AppCompatEditText) view.findViewById(R.id.etStoreName);
        etStoreName.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtEmailHd = (AppCompatTextView) view.findViewById(R.id.txtEmailHd);
        txtEmailHd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etEmailAddress = (AppCompatEditText) view.findViewById(R.id.etEmailAddress);
        etEmailAddress.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtMobileHd = (AppCompatTextView) view.findViewById(R.id.txtMobileHd);
        txtMobileHd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etMobile = (AppCompatEditText) view.findViewById(R.id.etMobile);
        etMobile.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtOwnerHd = (AppCompatTextView) view.findViewById(R.id.txtOwnerHd);
        txtOwnerHd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etOwner = (AppCompatEditText) view.findViewById(R.id.etOwner);
        etOwner.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtLandLineHd = (AppCompatTextView) view.findViewById(R.id.txtLandLineHd);
        txtLandLineHd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etLandLine = (AppCompatEditText) view.findViewById(R.id.etLandLine);
        etLandLine.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtStateHd = (AppCompatTextView) view.findViewById(R.id.txtStateHd);
        txtStateHd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etState = (AppCompatEditText) view.findViewById(R.id.etState);
        etState.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtCityHd = (AppCompatTextView) view.findViewById(R.id.txtCityHd);
        txtCityHd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etCity = (AppCompatEditText) view.findViewById(R.id.etCity);
        etCity.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtPinHd = (AppCompatTextView) view.findViewById(R.id.txtPinHd);
        txtPinHd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etPin = (AppCompatEditText) view.findViewById(R.id.etPin);
        etPin.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtAddressHd = (AppCompatTextView) view.findViewById(R.id.txtAddressHd);
        txtAddressHd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etAddress = (AppCompatEditText) view.findViewById(R.id.etAddress);
        etAddress.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());

        txtUpdate = (AppCompatTextView) view.findViewById(R.id.txtUpdate);
        txtUpdate.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());

        etState.setOnClickListener(this);
        etCity.setOnClickListener(this);

        txtUpdate.setOnClickListener(this);
        rlUpadteImg.setOnClickListener(this);

    }

    private void sendDataOnRegistrationApi() {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.TRACKER_EMPLOYEE_PROFILE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int status;
                        String msg = null, id, name, email, mobile, password, emp_id, profile_pic, device_token, TYPE, altNum, state, city, address, pincode;
                        loading.dismiss();
                        EmployeeLoginSignupResponse loginSignupResponseParsing = new EmployeeLoginSignupResponse();
                        loginSignupResponseParsing.responseParseMethod(response);

                        status = loginSignupResponseParsing.getStatus();
                        msg = loginSignupResponseParsing.getMsg();
                        sPic = loginSignupResponseParsing.getPic().trim();
                        TYPE = loginSignupResponseParsing.getType().trim();

                        if (status == 1) {
                            id = loginSignupResponseParsing.getDetailsArrayList().get(0).getId().trim();
                            name = loginSignupResponseParsing.getDetailsArrayList().get(0).getName().trim();
                            email = loginSignupResponseParsing.getDetailsArrayList().get(0).getEmail().trim();
                            mobile = loginSignupResponseParsing.getDetailsArrayList().get(0).getMobile().trim();
                            password = loginSignupResponseParsing.getDetailsArrayList().get(0).getPassword().trim();
                            emp_id = loginSignupResponseParsing.getDetailsArrayList().get(0).getEmp_id().trim();
                            device_token = loginSignupResponseParsing.getDetailsArrayList().get(0).getDevice_token().trim();
                            altNum = loginSignupResponseParsing.getDetailsArrayList().get(0).getAlt_mobile().trim();
                            state = loginSignupResponseParsing.getDetailsArrayList().get(0).getState().trim();
                            city = loginSignupResponseParsing.getDetailsArrayList().get(0).getCity().trim();
                            address = loginSignupResponseParsing.getDetailsArrayList().get(0).getAddress().trim();
                            pincode = loginSignupResponseParsing.getDetailsArrayList().get(0).getPincode().trim();

                            if (!TextUtils.isEmpty(name)) {
                                etStoreName.setText(Utility.fromHtml(name));
                            }
                            if (!TextUtils.isEmpty(emp_id)) {
                                etOwner.setText(Utility.fromHtml(emp_id));
                            }
                            if (!TextUtils.isEmpty(email)) {
                                etEmailAddress.setText(Utility.fromHtml(email));
                            }
                            if (!TextUtils.isEmpty(mobile)) {
                                etMobile.setText(Utility.fromHtml(mobile));
                            }
                            if (!TextUtils.isEmpty(altNum)) {
                                etLandLine.setText(Utility.fromHtml(altNum));
                            }
                            if (!TextUtils.isEmpty(state)) {
                                etState.setText(Utility.fromHtml(state));
                            }
                            if (!TextUtils.isEmpty(city)) {
                                etCity.setText(Utility.fromHtml(city));
                            }

                            if (!TextUtils.isEmpty(address)) {
                                etAddress.setText(Utility.fromHtml(address));
                            }
                            if (!TextUtils.isEmpty(pincode)) {
                                etPin.setText(Utility.fromHtml(pincode));
                            }

                            if (!TextUtils.isEmpty(sPic))
                                Utility.imageLoadGlideLibraryPic(activity, imgProfilePicNotFound, imgProfilePic, sPic);

                            saveDataOnPreference(TYPE, id, name, email, password, emp_id, sPic, device_token);
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
                params.put("type", SharedPreferencesManager.getType(activity));
                params.put("userid", SharedPreferencesManager.getUserID(activity));
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.edit_profile_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }

    @Override
    public void onClick(View v) {
        int i;
        switch (v.getId()) {

            case R.id.txtUpdate:
                methodUpdateView();
                break;
            case R.id.rlUpadteImg:
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA);
                } else {
                    dialog.show();
                }
                break;
            case R.id.etState:
                methodState();
                break;
            case R.id.etCity:
                if (!TextUtils.isEmpty(etState.getText())) {
                    methodCity();
                } else {
                    Toast.makeText(activity, "Select State first", Toast.LENGTH_LONG).show();
                }
                break;

        }
    }

    private void methodUpdateView() {
        String store = etStoreName.getText().toString().trim();
        String owner = etOwner.getText().toString().trim();
        String email = etEmailAddress.getText().toString().trim();
        String mobile = etMobile.getText().toString().trim();
        String landLine = etLandLine.getText().toString().trim();
        if (TextUtils.isEmpty(store)) {
            Toast.makeText(activity, "Please write  name", Toast.LENGTH_LONG).show();
        } else if (SharedPreferencesManager.getType(activity).equals("EMPLOYEE") && TextUtils.isEmpty(owner)) {
            Toast.makeText(activity, "Please write Emp ID", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(activity, "Please write email", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(activity, "Please write Mobile number", Toast.LENGTH_LONG).show();
        } else {
            methodSubbmit(store, owner, email, mobile, landLine);
        }
    }

    private void methodSubbmit(final String store, final String owner, final String email, final String mobile, final String landLine) {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.UPDATE_TRACKER_EMPLOYEE__PROFILE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("edit", response);
                        int status;
                        String msg = null, id, name, email, mobile, password, emp_id, profile_pic, device_token, TYPE;
                        loading.dismiss();
                        EmployeeLoginSignupResponse loginSignupResponseParsing = new EmployeeLoginSignupResponse();
                        loginSignupResponseParsing.responseParseMethod(response);

                        status = loginSignupResponseParsing.getStatus();
                        msg = loginSignupResponseParsing.getMsg();
                        sPic = loginSignupResponseParsing.getPic().trim();
                        TYPE = loginSignupResponseParsing.getType().trim();

                        if (status == 1) {
                            id = loginSignupResponseParsing.getDetailsArrayList().get(0).getId().trim();
                            name = loginSignupResponseParsing.getDetailsArrayList().get(0).getName().trim();
                            email = loginSignupResponseParsing.getDetailsArrayList().get(0).getEmail().trim();
                            mobile = loginSignupResponseParsing.getDetailsArrayList().get(0).getMobile().trim();
                            password = loginSignupResponseParsing.getDetailsArrayList().get(0).getPassword().trim();
                            emp_id = loginSignupResponseParsing.getDetailsArrayList().get(0).getEmp_id().trim();
                            device_token = loginSignupResponseParsing.getDetailsArrayList().get(0).getDevice_token().trim();

                            saveDataOnPreference(TYPE, id, name, email, password, emp_id, sPic, device_token);
                            onCallBackEditProfile.OnCallBackEditProfile();
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
                params.put("name", store);
                params.put("emp_id", owner);
                params.put("mobile", mobile);
                params.put("amobile", etLandLine.getText().toString());
                params.put("email", email);
                params.put("state", etState.getText().toString());
                params.put("city", etCity.getText().toString());
                params.put("type", SharedPreferencesManager.getType(activity));
                params.put("address", etAddress.getText().toString());
                params.put("pincode", etPin.getText().toString());
                if (photo != null) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    byte[] data = bos.toByteArray();
                    String encodedImage = Base64.encodeToString(data, Base64.DEFAULT);
                    params.put("pic", encodedImage + "image/jpeg");
                    params.put("action", "new");
                } else {
                    params.put("pic", sPic);
                    params.put("action", "old");
                }
                if (!TextUtils.isEmpty(SharedPreferencesManager.getDeviceToken(activity))) {
                    params.put("device_token", SharedPreferencesManager.getDeviceToken(activity));
                } else {
                    params.put("device_token", "deviceToken");
                }
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private void saveDataOnPreference(String Type, String id, String name, String email, String password, String emp_id, String profile_pic, String device_token) {
        SharedPreferencesManager.setType(activity, Type);
        SharedPreferencesManager.setUserID(activity, id);
        SharedPreferencesManager.setEmail(activity, email);
        SharedPreferencesManager.setUsername(activity, name);
        SharedPreferencesManager.setDeviceToken(activity, device_token);
        SharedPreferencesManager.setEmpId(activity, emp_id);
        SharedPreferencesManager.setProfileImage(activity, profile_pic);
        SharedPreferencesManager.setPassword(activity, password);

    }


    public String encodeToBase64(Bitmap image) {
        Bitmap bitmap = image;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] b = byteArrayOutputStream.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }


    private void methodState() {
        stateList.removeAll(stateList);
        stateArray = null;
        sendRequestOnState();
    }

    private void sendRequestOnState() {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.STATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        CountryMasterApiParsing countryMasterApiParsing = new CountryMasterApiParsing();
                        countryMasterApiParsing.responseParseMethod(response);
                        if (countryMasterApiParsing.getCountryDetails().getStateDetailsArrayList().size() > 0) {
                            for (int i = 0; i < countryMasterApiParsing.getCountryDetails().getStateDetailsArrayList().size(); i++) {
                                stateList.add(countryMasterApiParsing.getCountryDetails().getStateDetailsArrayList().get(i).getState());
                            }
                            if (stateList.size() > 0) {
                                stateArray = new String[stateList.size()];
                                stateList.toArray(stateArray);

                                if (stateList != null && stateList.size() > 0) {
                                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
                                    builder.setTitle("Select State");
                                    builder.setNegativeButton(android.R.string.cancel, null);
                                    builder.setItems(stateArray, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int item) {
                                            etState.setText(stateArray[item]);
                                            etCity.setText("");
                                            //etMall.setText("");
                                        }
                                    });
                                    builder.create();
                                    builder.show();
                                }
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
                params.put("country", "india");
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private void methodCity() {
        cityList.removeAll(cityList);
        cityArray = null;

        sendRequestOnCity();
    }

    private void sendRequestOnCity() {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.STATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        CityMasterParsing cityMasterParsing = new CityMasterParsing();
                        cityMasterParsing.responseParseMethod(response);
                        for (int i = 0; i < cityMasterParsing.getCountryDetails().getCityDetails().size(); i++) {
                            cityList.add(cityMasterParsing.getCountryDetails().getCityDetails().get(i).getCity());
                        }
                        if (cityList.size() > 0) {
                            cityArray = new String[cityList.size()];
                            cityList.toArray(cityArray);

                            if (cityList != null && cityList.size() > 0) {
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
                                builder.setTitle("Select City");
                                builder.setNegativeButton(android.R.string.cancel, null);
                                builder.setItems(cityArray, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int item) {
                                        etCity.setText(cityArray[item]);
                                        //etMall.setText("");
                                    }
                                });
                                builder.create();
                                builder.show();
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
                params.put("country", "india");
                params.put("state", etState.getText().toString());
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }


    private void captureImageInitialization() {
        final String[] items = new String[]{"Take from camera",
                "Select from gallery"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                android.R.layout.select_dialog_item, items);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);

        builder.setTitle("Select Image");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) { // pick from
                // camera
                if (item == 0) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp1.jpg");
                    mImageCaptureUri = Uri.fromFile(f);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                    startActivityForResult(intent, PICK_FROM_CAMERA);

                } else {
                    // pick from file
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, PICK_FROM_FILE);
                }
            }
        });

        dialog = builder.create();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != activity.RESULT_OK)
            return;

        switch (requestCode) {
            case PICK_FROM_CAMERA:

                doCrop();

                break;

            case PICK_FROM_FILE:

                // After selecting image from files, save the selected path
                mImageCaptureUri = data.getData();
                doCrop();
                break;

            case CROP_FROM_CAMERA:
                try {
                    if (outPutFile.exists()) {
                        photo = decodeFile(outPutFile.getAbsolutePath());
                        outPutFile.getPath();

                        imgProfilePicNotFound.setImageBitmap(photo);
                        imgProfilePic.setImageBitmap(photo);
                    } else {
                        Toast.makeText(activity, "Error while save image", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
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
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outPutFile));

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
                        activity, cropOptions);
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity);
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

                android.support.v7.app.AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    dialog.show();
                    //return;
                } else {
                    Toast.makeText(activity, "You Denied for camera permission so you cant't update image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}