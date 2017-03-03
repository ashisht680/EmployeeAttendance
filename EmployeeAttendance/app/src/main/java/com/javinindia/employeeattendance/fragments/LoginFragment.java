package com.javinindia.employeeattendance.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import com.javinindia.employeeattendance.constant.Constants;
import com.javinindia.employeeattendance.font.FontAsapRegularSingleTonClass;
import com.javinindia.employeeattendance.preference.SharedPreferencesManager;
import com.javinindia.employeeattendance.utility.Utility;

import java.util.HashMap;
import java.util.Map;


public class LoginFragment extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private RequestQueue requestQueue;
    private EditText etUsername;
    private EditText etPassword;
    AppCompatButton btnSignInAs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        initialize(view);
        return view;
    }


    private void initialize(View view) {
        AppCompatButton buttonLogin = (AppCompatButton) view.findViewById(R.id.btn_login);
        buttonLogin.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        btnSignInAs = (AppCompatButton) view.findViewById(R.id.btnSignInAs);
        btnSignInAs.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        TextView txtForgotPass = (TextView) view.findViewById(R.id.forgot_password);
        txtForgotPass.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etUsername = (EditText) view.findViewById(R.id.et_username);
        etUsername.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etPassword = (EditText) view.findViewById(R.id.et_password);
        etPassword.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        TextView txtRegistration = (TextView) view.findViewById(R.id.txtRegistration);
        txtRegistration.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        buttonLogin.setOnClickListener(this);
        btnSignInAs.setOnClickListener(this);
        txtForgotPass.setOnClickListener(this);
        txtRegistration.setOnClickListener(this);
        AppCompatCheckBox cbShowPwd = (AppCompatCheckBox) view.findViewById(R.id.cbShowPwd);
        cbShowPwd.setOnCheckedChangeListener(this);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.login_layout;
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
        BaseFragment baseFragment;
        switch (v.getId()) {
            case R.id.btn_login:
                Utility.hideKeyboard(activity);
                if (btnSignInAs.getText().toString().equals("SIGN IN AS")){
                    Toast.makeText(activity,"Please select sign in type",Toast.LENGTH_LONG).show();
                }else {
                    loginMethod(btnSignInAs.getText().toString());
                }
                break;
            case R.id.forgot_password:
                baseFragment = new ForgotPasswordFragment();
                callFragmentMethod(baseFragment, this.getClass().getSimpleName(), R.id.container);
                break;
            case R.id.txtRegistration:
                SignInAsMethod("register");
                break;
            case R.id.btnSignInAs:
                SignInAsMethod("signin");
                break;
        }
    }

    private void SignInAsMethod(String signin) {
        final String percentArray[] = {"TRACKER", "EMPLOYEE"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
        if (signin.equals("signin")) {
            builder.setTitle("SIGN IN AS");
            builder.setNegativeButton(android.R.string.cancel, null);
            builder.setItems(percentArray, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    btnSignInAs.setText(percentArray[item]);
                    dialog.dismiss();
                }
            });
        } else {
            builder.setTitle("REGISTER AS");
            builder.setNegativeButton(android.R.string.cancel, null);
            builder.setItems(percentArray, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    btnSignInAs.setText(percentArray[item]);
                    if (percentArray[item].equals("TRACKER")) {
                        BaseFragment baseFragment = new SignUpAddressFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("type", "TRACKER");
                        baseFragment.setArguments(bundle);
                        callFragmentMethod(baseFragment, this.getClass().getSimpleName(), R.id.container);
                    } else {
                        BaseFragment baseFragment = new SignUpAddressFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("type", "EMPLOYEE");
                        baseFragment.setArguments(bundle);
                        callFragmentMethod(baseFragment, this.getClass().getSimpleName(), R.id.container);
                    }
                    dialog.dismiss();
                }
            });
        }


        builder.create();
        builder.show();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void loginMethod(String signType) {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (validation(username, password)) {
            sendDataOnLoginApi(username, password ,signType);
        }

    }

    private void sendDataOnLoginApi(final String username, final String password , final String signType) {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.TRACKER_EMPLOYEE_LOGIN_URL,
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
                                showDialogMethod("Sorry, Invalid email/password");
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
                params.put("type", signType);
                params.put("email", username);
                params.put("password", password);
                if (!TextUtils.isEmpty(SharedPreferencesManager.getDeviceToken(activity))) {
                    params.put("deviceToken", SharedPreferencesManager.getDeviceToken(activity));
                } else {
                    params.put("deviceToken", "deviceToken");
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

    private boolean validation(String username, String password) {
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Mobile/Email field is empty.");
            etUsername.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(password)) {
            etPassword.setError("Please enter Password.");
            etPassword.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (!b) {
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            compoundButton.setButtonDrawable(R.drawable.ic_visibility_white);
        } else {
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            compoundButton.setButtonDrawable(R.drawable.ic_visibility_off_white);
        }
    }
}

