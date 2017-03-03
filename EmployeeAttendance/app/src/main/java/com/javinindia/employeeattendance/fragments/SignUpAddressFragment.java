package com.javinindia.employeeattendance.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.javinindia.employeeattendance.apiparsing.stateparsing.CityMasterParsing;
import com.javinindia.employeeattendance.apiparsing.stateparsing.CountryMasterApiParsing;
import com.javinindia.employeeattendance.constant.Constants;
import com.javinindia.employeeattendance.font.FontAsapRegularSingleTonClass;
import com.javinindia.employeeattendance.preference.SharedPreferencesManager;
import com.javinindia.employeeattendance.utility.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ashish on 08-09-2016.
 */
public class SignUpAddressFragment extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private AppCompatEditText et_State, et_City, et_PinCode;
    private AppCompatEditText et_EmpId, et_name, et_email, et_MobileNum, et_Landline, et_password, et_ConfirmPassword, et_Address;
    CheckBox radioButton;
    TextView txtTermCondition;
    private RequestQueue requestQueue;
    private String type;
    public ArrayList<String> stateList = new ArrayList<>();
    String stateArray[] = null;
    public ArrayList<String> cityList = new ArrayList<>();
    String cityArray[] = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString("type");
        getArguments().remove("type");
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(getFragmentLayout(), container, false);

        initialize(view);

        return view;
    }

    private void initialize(View view) {
        ImageView imgBack = (ImageView) view.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(this);

        AppCompatButton btn_regester = (AppCompatButton) view.findViewById(R.id.btn_regester);
        btn_regester.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        btn_regester.setText("REGISTER AS " + type);

        et_EmpId = (AppCompatEditText) view.findViewById(R.id.et_EmpId);
        et_EmpId.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        if (type.equals("TRACKER")) {
            et_EmpId.setHint("ID(optional)");
        } else {
            et_EmpId.setHint("ID");
        }
        et_name = (AppCompatEditText) view.findViewById(R.id.et_name);
        et_name.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        et_email = (AppCompatEditText) view.findViewById(R.id.et_email);
        et_email.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        et_MobileNum = (AppCompatEditText) view.findViewById(R.id.et_MobileNum);
        et_MobileNum.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        et_Landline = (AppCompatEditText) view.findViewById(R.id.et_Landline);
        et_Landline.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());

        et_State = (AppCompatEditText) view.findViewById(R.id.et_State);
        et_State.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        et_City = (AppCompatEditText) view.findViewById(R.id.et_City);
        et_City.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        et_Address = (AppCompatEditText) view.findViewById(R.id.et_Address);
        et_Address.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        et_PinCode = (AppCompatEditText) view.findViewById(R.id.et_PinCode);
        et_PinCode.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());

        et_password = (AppCompatEditText) view.findViewById(R.id.et_password);
        et_password.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        et_ConfirmPassword = (AppCompatEditText) view.findViewById(R.id.et_ConfirmPassword);
        et_ConfirmPassword.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());

        radioButton = (CheckBox) view.findViewById(R.id.radioButton);
        txtTermCondition = (TextView) view.findViewById(R.id.txtTermCondition);
        txtTermCondition.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtTermCondition.setText(Utility.fromHtml("<font color=#ffffff>" + "I accept the" + "</font>" + "\t" + "<font color=#0d7bbf>" + "terms and conditions." + "</font>"));

        AppCompatCheckBox cbShowPwd = (AppCompatCheckBox) view.findViewById(R.id.cbShowPwd);
        cbShowPwd.setOnCheckedChangeListener(this);
        AppCompatCheckBox cbShowConPwd = (AppCompatCheckBox) view.findViewById(R.id.cbShowConPwd);
        cbShowConPwd.setOnCheckedChangeListener(this);

        txtTermCondition.setOnClickListener(this);
        et_State.setOnClickListener(this);
        et_City.setOnClickListener(this);
        radioButton.setOnClickListener(this);
        btn_regester.setOnClickListener(this);

    }


    @Override
    protected int getFragmentLayout() {
        return R.layout.sign_up_address_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_regester:
                Utility.hideKeyboard(activity);
                registrationMethod();
                break;
            case R.id.imgBack:
                activity.onBackPressed();
                break;
            case R.id.txtTermCondition:
                BaseFragment termFragment = new TermAndConditionFragment();
                callFragmentMethod(termFragment, this.getClass().getSimpleName(), R.id.container);
                break;
            case R.id.et_State:
                methodState();
                break;
            case R.id.et_City:
                if (!TextUtils.isEmpty(et_State.getText())) {
                    methodCity();
                } else {
                    Toast.makeText(activity, "Select State first", Toast.LENGTH_LONG).show();
                }
                break;
        }
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
                        //  progressDialog.dismiss();
                        Log.e("MasterTags", response);
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
                                //  builder.setTitle("Select City");
                                builder.setNegativeButton(android.R.string.cancel, null);
                                builder.setItems(cityArray, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int item) {
                                        // Do something with the selection
                                        et_City.setText(cityArray[item]);
                                        // et_Mall.setText("");
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
                params.put("state", et_State.getText().toString());
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
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
                        Log.e("MasterTags", response);
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
                                    //  builder.setTitle("Select State");
                                    builder.setNegativeButton(android.R.string.cancel, null);
                                    builder.setItems(stateArray, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int item) {
                                            // Do something with the selection
                                            et_State.setText(stateArray[item]);
                                            et_City.setText("");
                                            //et_Mall.setText("");
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

    private void registrationMethod() {
        String idEmp = et_EmpId.getText().toString().trim();
        String name = et_name.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String mobileNum = et_MobileNum.getText().toString().trim();
        String landline = et_Landline.getText().toString().trim();
        String state = et_State.getText().toString().trim();
        String city = et_City.getText().toString().trim();
        String address = et_Address.getText().toString().trim();
        String pinCode = et_PinCode.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String confirmPassword = et_ConfirmPassword.getText().toString().trim();

        if (registerValidation(idEmp, name, email, mobileNum, landline, state, city, address, pinCode, password, confirmPassword)) {
            hitSignupApi(type, idEmp, name, email, mobileNum, landline, state, city, address, pinCode, password, confirmPassword);
        }

    }

    private void hitSignupApi(final String type, final String idEmp, final String name, final String email, final String mobileNum, final String landline, final String state, final String city, final String address, final String pinCode, final String password, String confirmPassword) {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.TRACKER_EMPLOYEE_SIGNUP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int status;
                        String msg = null, id, name, email, mobile, password, emp_id, profile_pic, device_token,TYPE;
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

                            saveDataOnPreference( TYPE,id, name, email, password, emp_id, profile_pic, device_token);
                            Intent refresh = new Intent(activity, LoginActivity.class);
                            startActivity(refresh);//Start the same Activity
                            activity.finish();
                        } else {
                            if (!TextUtils.isEmpty(msg)) {
                                showDialogMethod("Sorry, this email account already exists. Please enter a different email id.");
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
                params.put("type", type);
                params.put("emp_id", idEmp);
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("mobile", mobileNum);
                params.put("amobile", landline);
                params.put("state", state);
                params.put("city", city);
                params.put("address", address);
                params.put("pincode", pinCode);
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

    private void saveDataOnPreference(String Type,String id, String name, String email, String password, String emp_id, String profile_pic, String device_token) {
        SharedPreferencesManager.setType(activity,Type);
        SharedPreferencesManager.setUserID(activity, id);
        SharedPreferencesManager.setEmail(activity, email);
        SharedPreferencesManager.setUsername(activity, name);
        SharedPreferencesManager.setDeviceToken(activity, device_token);
        SharedPreferencesManager.setEmpId(activity, emp_id);
        SharedPreferencesManager.setProfileImage(activity, profile_pic);
        SharedPreferencesManager.setPassword(activity,password);
    }

    private boolean registerValidation(String idEmp, String name, String email, String mobileNum, String landline, String state, String city, String address, String pinCode, String password, String confirmPassword) {
        if (type.equals("EMPLOYEE") && TextUtils.isEmpty(idEmp)) {
            et_EmpId.setError("You have not entered any employee ID.");
            et_EmpId.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(name)) {
            et_name.setError("You have not entered name");
            et_name.requestFocus();
            return false;
        } else if (!Utility.isEmailValid(email)) {
            et_email.setError("Email id entered is invalid");
            et_email.requestFocus();
            return false;
        } else if (mobileNum.length() != 10) {
            et_MobileNum.setError("Mobile number entered is invalid");
            et_MobileNum.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(state)) {
            Toast.makeText(activity, "You have not entered state", Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(city)) {
            Toast.makeText(activity, "You have not entered city", Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(address)) {
            et_Address.setError("You have not entered Address");
            et_Address.requestFocus();
            return false;
        } else if (pinCode.length() != 6) {
            et_PinCode.setError("pincode entered is invalid");
            et_PinCode.requestFocus();
            return false;
        } else if (password.length() < 6) {
            et_password.setError("Password should be more than 6 characters");
            et_password.requestFocus();
            return false;
        } else if (!confirmPassword.equals(password)) {
            et_ConfirmPassword.setError("Password does not match");
            et_ConfirmPassword.requestFocus();
            return false;
        } else if (!radioButton.isChecked()) {
            Toast.makeText(activity, "You have not accepted the terms and conditions.", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(this.getClass().getSimpleName());
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.cbShowPwd:
                if (!b) {
                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    compoundButton.setButtonDrawable(R.drawable.ic_visibility_white);
                } else {
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    compoundButton.setButtonDrawable(R.drawable.ic_visibility_off_white);
                }
                break;
            case R.id.cbShowConPwd:
                if (!b) {
                    et_ConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    compoundButton.setButtonDrawable(R.drawable.ic_visibility_white);
                } else {
                    et_ConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    compoundButton.setButtonDrawable(R.drawable.ic_visibility_off_white);
                }
                break;
        }
    }
}