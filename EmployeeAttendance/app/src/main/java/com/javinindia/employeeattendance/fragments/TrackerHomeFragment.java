package com.javinindia.employeeattendance.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

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
import com.javinindia.employeeattendance.picasso.CircleTransform;
import com.javinindia.employeeattendance.preference.SharedPreferencesManager;
import com.javinindia.employeeattendance.utility.CheckConnection;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ashish on 14-02-2017.
 */

public class TrackerHomeFragment extends BaseFragment implements View.OnClickListener, EditProfileFragment.OnCallBackEditProfileListener, CheckConnectionFragment.OnCallBackInternetListener {
    private RequestQueue requestQueue;
    ImageView imgTrackerProfile;
    AppCompatTextView txtTrackerName, txtTrackerMailid, txtTrackerEdit;
    AppCompatTextView txtTotalEmp, txtTotalCustomer, txtTrackerDashboard;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
            txtTrackerMailid.setText(SharedPreferencesManager.getEmail(activity));
        }
        if (!TextUtils.isEmpty(SharedPreferencesManager.getUsername(activity))) {
            txtTrackerName.setText(SharedPreferencesManager.getUsername(activity));
        }

        if (!TextUtils.isEmpty(SharedPreferencesManager.getProfileImage(activity))) {
            Picasso.with(activity).load(SharedPreferencesManager.getProfileImage(activity)).transform(new CircleTransform()).into(imgTrackerProfile);
        } else {
            Picasso.with(activity).load(R.mipmap.ic_launcher).transform(new CircleTransform()).into(imgTrackerProfile);
        }
    }

    private void initialization(View view) {
        txtTrackerDashboard = (AppCompatTextView) view.findViewById(R.id.txtTrackerDashboard);
        txtTrackerDashboard.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtTotalEmp = (AppCompatTextView) view.findViewById(R.id.txtTotalEmp);
        txtTotalEmp.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtTotalCustomer = (AppCompatTextView) view.findViewById(R.id.txtTotalCustomer);
        txtTotalCustomer.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        imgTrackerProfile = (ImageView) view.findViewById(R.id.imgTrackerProfile);
        txtTrackerName = (AppCompatTextView) view.findViewById(R.id.txtTrackerName);
        txtTrackerName.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtTrackerMailid = (AppCompatTextView) view.findViewById(R.id.txtTrackerMailid);
        txtTrackerMailid.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtTrackerEdit = (AppCompatTextView) view.findViewById(R.id.txtTrackerEdit);
        txtTrackerEdit.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtTrackerEdit.setOnClickListener(this);
        txtTotalEmp.setOnClickListener(this);
        txtTotalCustomer.setOnClickListener(this);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.tracker_dashboard_layout;
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
        textView.setText("TRACKER");
        textView.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtTrackerEdit:
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
            case R.id.txtTotalEmp:
                if (CheckConnection.haveNetworkConnection(activity)) {
                    BaseFragment employeeListFragment = new EmployeeListFragment();
                    callFragmentMethod(employeeListFragment, this.getClass().getSimpleName(), R.id.container);
                } else {
                    CheckConnectionFragment fragment = new CheckConnectionFragment();
                    fragment.setMyCallBackInternetListener(this);
                    callFragmentMethod(fragment, this.getClass().getSimpleName(), R.id.container);
                }
                break;
            case R.id.txtTotalCustomer:
                if (CheckConnection.haveNetworkConnection(activity)) {
                    BaseFragment customersListFragment = new CustomersListFragment();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("id", "all");
                    customersListFragment.setArguments(bundle1);
                    callFragmentMethod(customersListFragment, this.getClass().getSimpleName(), R.id.container);
                } else {
                    CheckConnectionFragment fragment = new CheckConnectionFragment();
                    fragment.setMyCallBackInternetListener(this);
                    callFragmentMethod(fragment, this.getClass().getSimpleName(), R.id.container);
                }
                break;
        }
    }

    @Override
    public void OnCallBackEditProfile() {
        setViewApi();
    }

    @Override
    public void OnCallBackInternet() {
        setViewApi();
    }
}
