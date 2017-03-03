package com.javinindia.employeeattendance.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.javinindia.employeeattendance.R;
import com.javinindia.employeeattendance.font.FontAsapBoldSingleTonClass;
import com.javinindia.employeeattendance.font.FontAsapRegularSingleTonClass;
import com.javinindia.employeeattendance.utility.CheckConnection;

/**
 * Created by Ashish on 16-12-2016.
 */

public class CheckConnectionFragment extends BaseFragment {
    ProgressBar progressBar;

    private OnCallBackInternetListener backInternetListener;


    public interface OnCallBackInternetListener {
        void OnCallBackInternet();
    }

    public void setMyCallBackInternetListener(OnCallBackInternetListener callback) {
        this.backInternetListener = callback;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        progressBar = (ProgressBar)view.findViewById(R.id.progress);
        AppCompatTextView txtAppName, txtNoInt, txtGotIt;
        txtAppName = (AppCompatTextView) view.findViewById(R.id.txtAppName);
        txtAppName.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtNoInt = (AppCompatTextView) view.findViewById(R.id.txtNoInt);
        txtNoInt.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtGotIt = (AppCompatTextView) view.findViewById(R.id.txtGotIt);
        txtGotIt.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtGotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckConnection.haveNetworkConnection(activity)) {
                    backInternetListener.OnCallBackInternet();
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                    alertDialogBuilder.setTitle("No Internet Connection");
                    alertDialogBuilder.setMessage("You are offline please check your internet connection");
                    alertDialogBuilder.setPositiveButton("got it!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }

            }
        });
        return view;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.internet_check_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }
}
