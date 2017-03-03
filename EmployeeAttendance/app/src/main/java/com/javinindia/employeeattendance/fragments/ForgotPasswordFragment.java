package com.javinindia.employeeattendance.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.javinindia.employeeattendance.constant.Constants;
import com.javinindia.employeeattendance.font.FontAsapRegularSingleTonClass;
import com.javinindia.employeeattendance.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ForgotPasswordFragment extends BaseFragment implements View.OnClickListener {

    private EditText etEmailAddress;
    private RequestQueue requestQueue;
    private BaseFragment fragment;
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }

    private void initialize(View view) {
        ImageView imgBack = (ImageView) view.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(this);
        btnSignInAs = (AppCompatButton) view.findViewById(R.id.btnSignInAs);
        btnSignInAs.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        TextView txtForgot = (TextView) view.findViewById(R.id.txtForgot);
        txtForgot.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        TextView txtForgotdiscription = (TextView) view.findViewById(R.id.txtForgotdiscription);
        txtForgotdiscription.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        AppCompatButton buttonResetPassword = (AppCompatButton) view.findViewById(R.id.btn_reset_password);
        buttonResetPassword.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etEmailAddress = (EditText) view.findViewById(R.id.et_email_address);
        etEmailAddress.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        buttonResetPassword.setOnClickListener(this);
        btnSignInAs.setOnClickListener(this);
    }


    @Override
    protected int getFragmentLayout() {
        return R.layout.forgot_password_layout;
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

                if (btnSignInAs.getText().toString().equals("FORGOT FOR")){
                    Toast.makeText(activity,"Please select forgot type",Toast.LENGTH_LONG).show();
                }else {
                    String email = etEmailAddress.getText().toString().trim();
                    if (TextUtils.isEmpty(email)) {
                        etEmailAddress.setError("Enter detail");
                        etEmailAddress.requestFocus();
                    }else {
                        sendDataOnForgetPasswordApi(email);
                    }
                }

                break;
            case R.id.imgBack:
                activity.onBackPressed();
                break;
            case R.id.btnSignInAs:
                SignInAsMethod();
                break;
        }
    }

    private void SignInAsMethod() {
        final String percentArray[] = {"TRACKER", "EMPLOYEE"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
        builder.setTitle("FORGOT FOR");
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setItems(percentArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                btnSignInAs.setText(percentArray[item]);
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    private void sendDataOnForgetPasswordApi(final String email) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.TRACKER_EMPLOYEE_FORGET_PASSWORD_URL,
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
                //http://hnwkart.com/attendance/forgot.php?user=9599348895&type=TRACKER
                Map<String, String> params = new HashMap<String, String>();
                params.put("user", email);
                params.put("type", btnSignInAs.getText().toString().trim());
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private void responseImplement(String response) {
        JSONObject jsonObject = null;
        String userid = null, msg = null;
        int status = 0;
        try {
            jsonObject = new JSONObject(response);
            if (jsonObject.has("status"))
                status = jsonObject.optInt("status");
            if (jsonObject.has("userid"))
                userid = jsonObject.optString("userid");
            if (jsonObject.has("msg"))
                msg = jsonObject.optString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (status==1) {
            Toast.makeText(activity, "New password has sent to your registered mail/mobile number.", Toast.LENGTH_SHORT).show();
            activity.onBackPressed();
        } else {
            if (!TextUtils.isEmpty(msg)) {
                showDialogMethod(msg);
            }
        }
    }

}
