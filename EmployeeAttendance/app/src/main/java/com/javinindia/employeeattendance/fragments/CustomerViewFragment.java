package com.javinindia.employeeattendance.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.javinindia.employeeattendance.R;
import com.javinindia.employeeattendance.font.FontAsapBoldSingleTonClass;
import com.javinindia.employeeattendance.font.FontAsapRegularSingleTonClass;
import com.javinindia.employeeattendance.picasso.CircleTransform;
import com.squareup.picasso.Picasso;

/**
 * Created by Ashish on 20-02-2017.
 */

public class CustomerViewFragment extends BaseFragment {
    private AppCompatTextView txtID, txtName, txtGender, txtYOB, txtCO, txtLM, txtVTC, txtPO, txtDist, txtState, txtPC, txtDescription
            ,txtHdID,txtHdName,txtHdGender,txtHdYob,txtHdCO,txtHdLM,txtHdVTC,txtHdPO,txtHdDist,txtHdState,txtHdPC,txtHdDescription;
    public ImageView imgProfile;
    public ProgressBar progressBar;

    String id;
    String trav_id;
    String adhar_number;
    String name;
    String gender;
    String yob;
    String adhar_pic;
    String description;
    String care_of;
    String landmark;
    String village_town_city;
    String post_office;
    String district;
    String state;
    String pincode;
    String insertDate;
    int position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        id = getArguments().getString("id");
        trav_id = getArguments().getString("trav_id");
        adhar_number = getArguments().getString("adhar_number");
        name = getArguments().getString("name");
        gender = getArguments().getString("gender");
        yob = getArguments().getString("yob");
        adhar_pic = getArguments().getString("adhar_pic");
        description = getArguments().getString("description");
        care_of = getArguments().getString("care_of");
        landmark = getArguments().getString("landmark");
        village_town_city = getArguments().getString("village_town_city");
        post_office = getArguments().getString("post_office");
        district = getArguments().getString("district");
        state = getArguments().getString("state");
        pincode = getArguments().getString("pincode");
        insertDate = getArguments().getString("insertDate");
        position = getArguments().getInt("position");

        getArguments().remove("id");
        getArguments().remove("trav_id");
        getArguments().remove("adhar_number");
        getArguments().remove("name");
        getArguments().remove("gender");
        getArguments().remove("yob");
        getArguments().remove("adhar_pic");
        getArguments().remove("description");
        getArguments().remove("care_of");
        getArguments().remove("landmark");
        getArguments().remove("village_town_city");
        getArguments().remove("post_office");
        getArguments().remove("district");
        getArguments().remove("state");
        getArguments().remove("pincode");
        getArguments().remove("insertDate");
        getArguments().remove("position");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (menu != null){
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

        if (!TextUtils.isEmpty(adhar_number)) {
            txtID.setText(adhar_number);
        } else {
            txtID.setText("");
        }
        if (!TextUtils.isEmpty(name)) {
            txtName.setText(name);
        } else {
            txtName.setText("");
        }

        if (!TextUtils.isEmpty(gender)) {
            txtGender.setText(gender);
        } else {
            txtGender.setText("");
        }

        if (!TextUtils.isEmpty(yob)) {
            txtYOB.setText(yob);
        } else {
            txtYOB.setText("");
        }

        if (!TextUtils.isEmpty(care_of)) {
            txtCO.setText(care_of);
        } else {
            txtCO.setText("");
        }

        if (!TextUtils.isEmpty(landmark)) {
            txtLM.setText(landmark);
        } else {
            txtLM.setText("");
        }

        if (!TextUtils.isEmpty(village_town_city)) {
            txtVTC.setText(village_town_city);
        } else {
            txtVTC.setText("");
        }

        if (!TextUtils.isEmpty(post_office)) {
            txtPO.setText(post_office);
        } else {
            txtPO.setText("");
        }

        if (!TextUtils.isEmpty(district)) {
            txtDist.setText(district);
        } else {
            txtDist.setText("");
        }

        if (!TextUtils.isEmpty(state)) {
            txtState.setText(state);
        } else {
            txtState.setText("");
        }

        if (!TextUtils.isEmpty(pincode)) {
            txtPC.setText(pincode);
        } else {
            txtPC.setText("");
        }

        if (!TextUtils.isEmpty(description)) {
            txtDescription.setText(description);
        } else {
            txtDescription.setText("");
        }

        if (!TextUtils.isEmpty(adhar_pic)) {
            Picasso.with(activity).load(adhar_pic).transform(new CircleTransform()).into(imgProfile);
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
        txtID = (AppCompatTextView) view.findViewById(R.id.txtID);
        txtID.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtName = (AppCompatTextView) view.findViewById(R.id.txtName);
        txtName.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtGender = (AppCompatTextView) view.findViewById(R.id.txtGender);
        txtGender.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtYOB = (AppCompatTextView) view.findViewById(R.id.txtYOB);
        txtYOB.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtCO = (AppCompatTextView) view.findViewById(R.id.txtCO);
        txtCO.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtLM = (AppCompatTextView) view.findViewById(R.id.txtLM);
        txtLM.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtVTC = (AppCompatTextView) view.findViewById(R.id.txtVTC);
        txtVTC.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtPO = (AppCompatTextView) view.findViewById(R.id.txtPO);
        txtPO.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtDist = (AppCompatTextView) view.findViewById(R.id.txtDist);
        txtDist.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtState = (AppCompatTextView) view.findViewById(R.id.txtState);
        txtState.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtPC = (AppCompatTextView) view.findViewById(R.id.txtPC);
        txtPC.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtDescription = (AppCompatTextView) view.findViewById(R.id.txtDescription);
        txtDescription.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());

        txtHdID = (AppCompatTextView) view.findViewById(R.id.txtHdID);
        txtHdID.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtHdName = (AppCompatTextView) view.findViewById(R.id.txtHdName);
        txtHdName.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtHdGender = (AppCompatTextView) view.findViewById(R.id.txtHdGender);
        txtHdGender.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtHdYob = (AppCompatTextView) view.findViewById(R.id.txtHdYob);
        txtHdYob.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtHdCO = (AppCompatTextView) view.findViewById(R.id.txtHdCO);
        txtHdCO.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtHdLM = (AppCompatTextView) view.findViewById(R.id.txtHdLM);
        txtHdLM.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtHdVTC = (AppCompatTextView) view.findViewById(R.id.txtHdVTC);
        txtHdVTC.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtHdPO = (AppCompatTextView) view.findViewById(R.id.txtHdPO);
        txtHdPO.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtHdDist = (AppCompatTextView) view.findViewById(R.id.txtHdDist);
        txtHdDist.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtHdState = (AppCompatTextView) view.findViewById(R.id.txtHdState);
        txtHdState.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtHdPC = (AppCompatTextView) view.findViewById(R.id.txtHdPC);
        txtHdPC.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtHdDescription = (AppCompatTextView) view.findViewById(R.id.txtHdDescription);
        txtHdDescription.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.customer_view_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }
}
