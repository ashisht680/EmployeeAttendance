package com.javinindia.employeeattendance.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.javinindia.employeeattendance.R;
import com.javinindia.employeeattendance.font.FontAsapBoldSingleTonClass;
import com.javinindia.employeeattendance.font.FontAsapRegularSingleTonClass;
import com.javinindia.employeeattendance.picasso.CircleTransform;
import com.javinindia.employeeattendance.preference.SharedPreferencesManager;
import com.squareup.picasso.Picasso;

/**
 * Created by Ashish on 21-02-2017.
 */

public class EmployeeViewFragment extends BaseFragment implements View.OnClickListener {
    private AppCompatTextView txtEID, txtHdEID, txtName, txtHdName, txtEmail, txtHdEmail, txtMobile, txtHdMobile, txtAltMobile, txtHdAltMobile, txtAddress, txtHdAddress, txtCity, txtHdCity, txtState, txtHdState, txtPincode, txtHdPincode;
    public ImageView imgProfile;
    public ProgressBar progressBar;
    AppCompatTextView btnAttendance, btnCustomer;
    String id;
    String e_id;
    String email;
    String name;
    String mobile;
    String aMobile;
    String profile_pic;
    String address;
    String state, city;
    String pincode;
    int position;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        id = getArguments().getString("id");
        e_id = getArguments().getString("e_id");
        email = getArguments().getString("email");
        name = getArguments().getString("name");
        mobile = getArguments().getString("mobile");
        aMobile = getArguments().getString("aMobile");
        profile_pic = getArguments().getString("profile_pic");
        address = getArguments().getString("address");
        state = getArguments().getString("state");
        city = getArguments().getString("city");
        pincode = getArguments().getString("pincode");
        position = getArguments().getInt("position");

        getArguments().remove("id");
        getArguments().remove("e_id");
        getArguments().remove("email");
        getArguments().remove("name");
        getArguments().remove("mobile");
        getArguments().remove("aMobile");
        getArguments().remove("profile_pic");
        getArguments().remove("address");
        getArguments().remove("state");
        getArguments().remove("pincode");
        getArguments().remove("position");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (menu != null) {
            menu.findItem(R.id.action_changePass).setVisible(false);
            menu.findItem(R.id.action_feedback).setVisible(false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initToolbar(view);
        initialization(view);
        setDateOnView();
        return view;
    }

    private void setDateOnView() {
        if (!TextUtils.isEmpty(e_id)) {
            txtEID.setText(e_id);
        } else {
            txtEID.setText("");
        }
        if (!TextUtils.isEmpty(name)) {
            txtName.setText(name);
        } else {
            txtName.setText("");
        }

        if (!TextUtils.isEmpty(email)) {
            txtEmail.setText(email);
        } else {
            txtEmail.setText("");
        }

        if (!TextUtils.isEmpty(mobile)) {
            txtMobile.setText(mobile);
        } else {
            txtMobile.setText("");
        }

        if (!TextUtils.isEmpty(aMobile)) {
            txtAltMobile.setText(aMobile);
        } else {
            txtAltMobile.setText("");
        }

        if (!TextUtils.isEmpty(address)) {
            txtAddress.setText(address);
        } else {
            txtAddress.setText("");
        }

        if (!TextUtils.isEmpty(city)) {
            txtCity.setText(city);
        } else {
            txtCity.setText("");
        }

        if (!TextUtils.isEmpty(state)) {
            txtState.setText(state);
        } else {
            txtState.setText("");
        }


        if (!TextUtils.isEmpty(pincode)) {
            txtPincode.setText(pincode);
        } else {
            txtPincode.setText("");
        }


        if (!TextUtils.isEmpty(profile_pic)) {
            Picasso.with(activity).load(profile_pic).transform(new CircleTransform()).into(imgProfile);
        } else {
            Picasso.with(activity).load(R.drawable.default_avatar).transform(new CircleTransform()).into(imgProfile);
        }
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
        textView.setText(name);
        textView.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
    }

    private void initialization(View view) {
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        imgProfile = (ImageView) view.findViewById(R.id.imgProfile);
        txtEID = (AppCompatTextView) view.findViewById(R.id.txtEID);
        txtEID.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtHdEID = (AppCompatTextView) view.findViewById(R.id.txtHdEID);
        txtHdEID.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtName = (AppCompatTextView) view.findViewById(R.id.txtName);
        txtName.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtHdName = (AppCompatTextView) view.findViewById(R.id.txtHdName);
        txtHdName.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtEmail = (AppCompatTextView) view.findViewById(R.id.txtEmail);
        txtEmail.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtHdEmail = (AppCompatTextView) view.findViewById(R.id.txtHdEmail);
        txtHdEmail.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtMobile = (AppCompatTextView) view.findViewById(R.id.txtMobile);
        txtMobile.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtHdMobile = (AppCompatTextView) view.findViewById(R.id.txtHdMobile);
        txtHdMobile.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtAltMobile = (AppCompatTextView) view.findViewById(R.id.txtAltMobile);
        txtAltMobile.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtHdAltMobile = (AppCompatTextView) view.findViewById(R.id.txtHdAltMobile);
        txtHdAltMobile.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtAddress = (AppCompatTextView) view.findViewById(R.id.txtAddress);
        txtAddress.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtHdAddress = (AppCompatTextView) view.findViewById(R.id.txtHdAddress);
        txtHdAddress.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtCity = (AppCompatTextView) view.findViewById(R.id.txtCity);
        txtCity.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtHdCity = (AppCompatTextView) view.findViewById(R.id.txtHdCity);
        txtHdCity.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtState = (AppCompatTextView) view.findViewById(R.id.txtState);
        txtState.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtHdState = (AppCompatTextView) view.findViewById(R.id.txtHdState);
        txtHdState.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtPincode = (AppCompatTextView) view.findViewById(R.id.txtPincode);
        txtPincode.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtHdPincode = (AppCompatTextView) view.findViewById(R.id.txtHdPincode);
        txtHdPincode.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());

        btnAttendance = (AppCompatTextView) view.findViewById(R.id.btnAttendance);
        btnAttendance.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        btnCustomer = (AppCompatTextView) view.findViewById(R.id.btnCustomer);
        btnCustomer.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        btnAttendance.setOnClickListener(this);
        btnCustomer.setOnClickListener(this);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.employee_view_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAttendance:
                BaseFragment myAttendanceFragment = new MyAttendanceFragment();
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                myAttendanceFragment.setArguments(bundle);
                callFragmentMethod(myAttendanceFragment, this.getClass().getSimpleName(), R.id.container);
                break;
            case R.id.btnCustomer:
                BaseFragment customersListFragment = new CustomersListFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString("id", id);
                customersListFragment.setArguments(bundle1);
                callFragmentMethod(customersListFragment, this.getClass().getSimpleName(), R.id.container);
                break;
        }
    }
}
