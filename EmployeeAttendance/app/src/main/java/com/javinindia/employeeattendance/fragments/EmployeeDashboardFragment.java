package com.javinindia.employeeattendance.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.javinindia.employeeattendance.R;
import com.javinindia.employeeattendance.activity.LoginActivity;
import com.javinindia.employeeattendance.apiparsing.loginsignupparsing.EmployeeLoginSignupResponse;
import com.javinindia.employeeattendance.constant.Constants;
import com.javinindia.employeeattendance.font.FontAsapBoldSingleTonClass;
import com.javinindia.employeeattendance.font.FontAsapRegularSingleTonClass;
import com.javinindia.employeeattendance.location.GPSTracker;
import com.javinindia.employeeattendance.picasso.CircleTransform;
import com.javinindia.employeeattendance.preference.SharedPreferencesManager;
import com.javinindia.employeeattendance.utility.CheckConnection;
import com.javinindia.employeeattendance.utility.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ashish on 16-02-2017.
 */

public class EmployeeDashboardFragment extends BaseFragment implements View.OnClickListener ,EditProfileFragment.OnCallBackEditProfileListener ,CheckConnectionFragment.OnCallBackInternetListener{
    private RequestQueue requestQueue;
    ImageView imgEmpProfile;
    AppCompatTextView txtEmpName, txtEmpId, txtEmpMailid;
    GPSTracker gps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        }else {
            methodLocation();
            // getLocationMethod();
        }
    }

    private void methodLocation() {
        if (!hasGPSDevice(activity)) {
            Toast.makeText(activity, "Gps not Supported", Toast.LENGTH_SHORT).show();
        }
        final LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(activity)) {
            Toast.makeText(activity, "Gps not enabled", Toast.LENGTH_SHORT).show();
            //enableLoc();
        } else {
            getLocationMethod();
        }
    }

    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    private void getLocationMethod() {
        gps = new GPSTracker(activity);
        if(gps.canGetLocation()){
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            if (latitude==0.0 && longitude==0.0){

            }else {
               // Toast.makeText(activity,latitude + "" + longitude + "",Toast.LENGTH_LONG).show();
            }


        }else{
            gps.showSettingsAlert();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initToolbar(view);
        initialization(view);
        setViewApi();
        return view;
    }

    private void setViewApi() {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.TRACKER_EMPLOYEE_PROFILE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int status;
                        String msg = null, id, name, email, mobile, password, emp_id, profile_pic, device_token, TYPE;
                        loading.dismiss();
                        EmployeeLoginSignupResponse loginSignupResponseParsing = new EmployeeLoginSignupResponse();
                        loginSignupResponseParsing.responseParseMethod(response);

                        status = loginSignupResponseParsing.getStatus();
                        msg = loginSignupResponseParsing.getMsg();
                        profile_pic = loginSignupResponseParsing.getPic().trim();
                        TYPE = loginSignupResponseParsing.getType().trim();

                        if (status == 1) {
                            id = loginSignupResponseParsing.getDetailsArrayList().get(0).getId().trim();
                            name = loginSignupResponseParsing.getDetailsArrayList().get(0).getName().trim();
                            email = loginSignupResponseParsing.getDetailsArrayList().get(0).getEmail().trim();
                            mobile = loginSignupResponseParsing.getDetailsArrayList().get(0).getMobile().trim();
                            password = loginSignupResponseParsing.getDetailsArrayList().get(0).getPassword().trim();
                            emp_id = loginSignupResponseParsing.getDetailsArrayList().get(0).getEmp_id().trim();
                            device_token = loginSignupResponseParsing.getDetailsArrayList().get(0).getDevice_token().trim();

                            saveDataOnPreference(TYPE, id, name, email, password, emp_id, profile_pic, device_token);
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
                // http://hnwkart.com/attendance/viewProfile.php?userid=12&type=EMPLOYEE
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

    private void saveDataOnPreference(String Type, String id, String name, String email, String password, String emp_id, String profile_pic, String device_token) {
        SharedPreferencesManager.setType(activity, Type);
        SharedPreferencesManager.setUserID(activity, id);
        SharedPreferencesManager.setEmail(activity, email);
        SharedPreferencesManager.setUsername(activity, name);
        SharedPreferencesManager.setDeviceToken(activity, device_token);
        SharedPreferencesManager.setEmpId(activity, emp_id);
        SharedPreferencesManager.setProfileImage(activity, profile_pic);
        SharedPreferencesManager.setPassword(activity, password);

        if (!TextUtils.isEmpty(SharedPreferencesManager.getEmail(activity))) {
            txtEmpMailid.setText(SharedPreferencesManager.getEmail(activity));
        }
        if (!TextUtils.isEmpty(SharedPreferencesManager.getUsername(activity))) {
            txtEmpName.setText(SharedPreferencesManager.getUsername(activity));
        }
        if (!TextUtils.isEmpty(SharedPreferencesManager.getEmpId(activity))) {
            txtEmpId.setText("ID:" + SharedPreferencesManager.getEmpId(activity));
        }

        if (!TextUtils.isEmpty(SharedPreferencesManager.getProfileImage(activity))) {
            Picasso.with(activity).load(SharedPreferencesManager.getProfileImage(activity)).transform(new CircleTransform()).into(imgEmpProfile);
        } else {
            Picasso.with(activity).load(R.drawable.default_avatar).transform(new CircleTransform()).into(imgEmpProfile);
        }
    }

    private void initialization(View view) {
        imgEmpProfile = (ImageView) view.findViewById(R.id.imgEmpProfile);
        txtEmpName = (AppCompatTextView) view.findViewById(R.id.txtEmpName);
        txtEmpName.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtEmpId = (AppCompatTextView) view.findViewById(R.id.txtEmpId);
        txtEmpId.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtEmpMailid = (AppCompatTextView) view.findViewById(R.id.txtEmpMailid);
        txtEmpMailid.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        AppCompatTextView txtEmpDashboard = (AppCompatTextView) view.findViewById(R.id.txtEmpDashboard);
        txtEmpDashboard.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        AppCompatTextView txtEmpEdit = (AppCompatTextView) view.findViewById(R.id.txtEmpEdit);
        txtEmpEdit.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        AppCompatTextView txtAttendance = (AppCompatTextView) view.findViewById(R.id.txtAttendance);
        txtAttendance.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        AppCompatTextView txtVisitCustomer = (AppCompatTextView) view.findViewById(R.id.txtVisitCustomer);
        txtVisitCustomer.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        AppCompatTextView txtMyAttendance = (AppCompatTextView) view.findViewById(R.id.txtMyAttendance);
        txtMyAttendance.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        AppCompatTextView txtMyCustomers = (AppCompatTextView) view.findViewById(R.id.txtMyCustomers);
        txtMyCustomers.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtAttendance.setOnClickListener(this);
        txtVisitCustomer.setOnClickListener(this);
        txtMyAttendance.setOnClickListener(this);
        txtMyCustomers.setOnClickListener(this);
        txtEmpEdit.setOnClickListener(this);

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.employee_dashboard_layout;
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
       /* toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onBackPressed();
            }
        });*/
        final ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle(null);
        AppCompatTextView textView = (AppCompatTextView) view.findViewById(R.id.tittle);
        textView.setText("EMPLOYEE");
        textView.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtAttendance:
                if (CheckConnection.haveNetworkConnection(activity)) {
                    BaseFragment baseFragment = new EmployeeAttendanceFragment();
                    callFragmentMethod(baseFragment, this.getClass().getSimpleName(), R.id.container);
                } else {
                    CheckConnectionFragment fragment = new CheckConnectionFragment();
                    fragment.setMyCallBackInternetListener(this);
                    callFragmentMethod(fragment, this.getClass().getSimpleName(), R.id.container);
                }
                break;
            case R.id.txtVisitCustomer:
                if (CheckConnection.haveNetworkConnection(activity)) {
                    BaseFragment baseFragment1 = new VisitCustomerFragment();
                    callFragmentMethod(baseFragment1, this.getClass().getSimpleName(), R.id.container);
                } else {
                    CheckConnectionFragment fragment = new CheckConnectionFragment();
                    fragment.setMyCallBackInternetListener(this);
                    callFragmentMethod(fragment, this.getClass().getSimpleName(), R.id.container);
                }
                break;
            case R.id.txtMyAttendance:
                if (CheckConnection.haveNetworkConnection(activity)) {
                    BaseFragment myAttendanceFragment = new MyAttendanceFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("id", SharedPreferencesManager.getUserID(activity));
                    myAttendanceFragment.setArguments(bundle);
                    callFragmentMethod(myAttendanceFragment, this.getClass().getSimpleName(), R.id.container);
                } else {
                    CheckConnectionFragment fragment = new CheckConnectionFragment();
                    fragment.setMyCallBackInternetListener(this);
                    callFragmentMethod(fragment, this.getClass().getSimpleName(), R.id.container);
                }
                break;
            case R.id.txtMyCustomers:
                if (CheckConnection.haveNetworkConnection(activity)) {
                    BaseFragment customersListFragment = new CustomersListFragment();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("id", SharedPreferencesManager.getUserID(activity));
                    customersListFragment.setArguments(bundle1);
                    callFragmentMethod(customersListFragment, this.getClass().getSimpleName(), R.id.container);
                } else {
                    CheckConnectionFragment fragment = new CheckConnectionFragment();
                    fragment.setMyCallBackInternetListener(this);
                    callFragmentMethod(fragment, this.getClass().getSimpleName(), R.id.container);
                }
                break;
            case R.id.txtEmpEdit:
                if (CheckConnection.haveNetworkConnection(activity)) {
                    EditProfileFragment baseFragment2 = new EditProfileFragment();
                    baseFragment2.setMyCallBackOfferListener(this);
                    callFragmentMethod(baseFragment2, this.getClass().getSimpleName(), R.id.container);
                } else {
                    CheckConnectionFragment fragment = new CheckConnectionFragment();
                    fragment.setMyCallBackInternetListener(this);
                    callFragmentMethod(fragment, this.getClass().getSimpleName(), R.id.container);
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_changePass:
                ChangePasswordFragment fragment = new ChangePasswordFragment();
                callFragmentMethod(fragment, this.getClass().getSimpleName(), R.id.container);
                return true;
            case R.id.action_feedback:
                FeedbackFragment fragment1 = new FeedbackFragment();
                callFragmentMethod(fragment1, this.getClass().getSimpleName(), R.id.container);
                return true;
            case R.id.action_logout:
                dialogBox();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        activity.getMenuInflater().inflate(R.menu.navigation_menu, menu);
    }

    public void dialogBox() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle("Logout");
        alertDialogBuilder.setMessage("Thanks for visiting ! Be back soon!");
        alertDialogBuilder.setPositiveButton("Ok!",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        sendDataOnLogOutApi();
                    }
                });

        alertDialogBuilder.setNegativeButton("Take me back",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void sendDataOnLogOutApi() {
        final ProgressDialog loading = ProgressDialog.show(activity, "Logging out...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.TRACKER_EMPLOYEE_LOGOUT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        responseImplement(response);
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
                //http://hnwkart.com/attendance/logout.php?uid=1&type=TRACKER
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", SharedPreferencesManager.getUserID(activity));
                params.put("type", SharedPreferencesManager.getType(activity));
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private void responseImplement(String response) {
        JSONObject jsonObject = null;
        String msg = null;
        int status = 0;
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
            SharedPreferencesManager.setType(activity, null);
            SharedPreferencesManager.setUserID(activity, null);
            SharedPreferencesManager.setEmail(activity, null);
            SharedPreferencesManager.setUsername(activity, null);
            SharedPreferencesManager.setDeviceToken(activity, null);
            SharedPreferencesManager.setEmpId(activity, null);
            SharedPreferencesManager.setProfileImage(activity, null);
            SharedPreferencesManager.setPassword(activity, null);
            Intent refresh = new Intent(activity, LoginActivity.class);
            startActivity(refresh);//Start the same Activity
            activity.finish();
        } else {
            if (!TextUtils.isEmpty(msg)) {
                showDialogMethod("Try again");
            }
        }
    }

    @Override
    public void OnCallBackEditProfile() {
        setViewApi();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 123: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted.
                    getLocationMethod();
                } else {
                    // User refused to grant permission. You can add AlertDialog here
                    Toast.makeText(activity, "You didn't give permission to access device location", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void OnCallBackInternet() {
        setViewApi();
    }
}
