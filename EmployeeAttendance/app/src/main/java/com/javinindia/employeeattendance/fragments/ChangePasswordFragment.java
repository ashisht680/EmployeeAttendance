package com.javinindia.employeeattendance.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.javinindia.employeeattendance.R;
import com.javinindia.employeeattendance.constant.Constants;
import com.javinindia.employeeattendance.font.FontAsapRegularSingleTonClass;
import com.javinindia.employeeattendance.preference.SharedPreferencesManager;
import com.javinindia.employeeattendance.utility.CheckConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ashish on 16-11-2016.
 */
public class ChangePasswordFragment extends BaseFragment implements View.OnClickListener,CheckConnectionFragment.OnCallBackInternetListener, CompoundButton.OnCheckedChangeListener {

    private AppCompatEditText et_old_password, et_new_password;
    private RequestQueue requestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (menu != null) {
            menu.findItem(R.id.action_changePass).setVisible(false);
            menu.findItem(R.id.action_feedback).setVisible(false);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
        textView.setText("Change password");
        textView.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
    }

    private void initialize(View view) {
        TextView txtForgot = (TextView) view.findViewById(R.id.txtForgot);
        txtForgot.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        TextView txtForgotdiscription = (TextView) view.findViewById(R.id.txtForgotdiscription);
        txtForgotdiscription.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        AppCompatButton buttonResetPassword = (AppCompatButton) view.findViewById(R.id.btn_reset_password);
        buttonResetPassword.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        et_old_password = (AppCompatEditText) view.findViewById(R.id.et_old_password);
        et_old_password.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        et_new_password = (AppCompatEditText) view.findViewById(R.id.et_new_password);
        et_new_password.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        buttonResetPassword.setOnClickListener(this);

        AppCompatCheckBox cbOldPwd = (AppCompatCheckBox) view.findViewById(R.id.cbOldPwd);
        cbOldPwd.setOnCheckedChangeListener(this);
        AppCompatCheckBox cbNewPwd = (AppCompatCheckBox) view.findViewById(R.id.cbNewPwd);
        cbNewPwd.setOnCheckedChangeListener(this);
    }


    @Override
    protected int getFragmentLayout() {
        return R.layout.change_password_layout;
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
        switch (v.getId()) {
            case R.id.btn_reset_password:
                String oldPass = et_old_password.getText().toString().trim();
                String newPass = et_new_password.getText().toString().trim();
                if (TextUtils.isEmpty(oldPass)) {
                    et_old_password.setError("Enter your old password");
                    et_old_password.requestFocus();
                } else {
                    if (TextUtils.isEmpty(newPass)) {
                        et_new_password.setError("Enter your new password");
                        et_new_password.requestFocus();
                    } else {
                        if (CheckConnection.haveNetworkConnection(activity)) {
                            sendDataOnForgetPasswordApi(oldPass, newPass);
                        } else {
                            CheckConnectionFragment fragment = new CheckConnectionFragment();
                            fragment.setMyCallBackInternetListener(this);
                            callFragmentMethod(fragment, this.getClass().getSimpleName(), R.id.container);
                        }

                    }
                }

                break;
        }
    }

    private void sendDataOnForgetPasswordApi(final String old, final String newP) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.TRACKER_EMPLOYEE_CHANGE_PASSWORD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        responseImplement(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        volleyErrorHandle(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //http://hnwkart.com/attendance/changepassword.php?userid=1&old_pass=jamesbond&new_pass=123456&type=TRACKER
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", SharedPreferencesManager.getUserID(activity));
                params.put("type", SharedPreferencesManager.getType(activity));
                params.put("old_pass", old);
                params.put("new_pass", newP);
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }


    private void responseImplement(String response) {
        JSONObject jsonObject = null;
        String msg = null,pass=null;
        int status = 0;
        try {
            jsonObject = new JSONObject(response);
            if (jsonObject.has("status"))
                status = jsonObject.optInt("status");
            if (jsonObject.has("msg"))
                msg = jsonObject.optString("msg");
            if (jsonObject.has("npass")){
                pass = jsonObject.optString("npass");
                SharedPreferencesManager.setPassword(activity,pass);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (status == 1) {
            Toast.makeText(activity, "Your password successfully changed", Toast.LENGTH_LONG).show();
            activity.onBackPressed();
        } else {
            if (!TextUtils.isEmpty(msg)) {
                showDialogMethod(msg);
            }
        }
    }


    @Override
    public void OnCallBackInternet() {
        activity.onBackPressed();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.cbOldPwd:
                if (!b) {
                    et_old_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    compoundButton.setButtonDrawable(R.drawable.ic_visibility_white);
                } else {
                    et_old_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    compoundButton.setButtonDrawable(R.drawable.ic_visibility_off_white);
                }
                break;
            case R.id.cbNewPwd:
                if (!b) {
                    et_new_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    compoundButton.setButtonDrawable(R.drawable.ic_visibility_white);
                } else {
                    et_new_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    compoundButton.setButtonDrawable(R.drawable.ic_visibility_off_white);
                }
                break;
        }
    }
}