package com.javinindia.employeeattendance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.javinindia.employeeattendance.R;
import com.javinindia.employeeattendance.fragments.BaseFragment;
import com.javinindia.employeeattendance.fragments.CheckConnectionFragment;
import com.javinindia.employeeattendance.fragments.EmployeeDashboardFragment;
import com.javinindia.employeeattendance.fragments.LoginFragment;
import com.javinindia.employeeattendance.fragments.TrackerHomeFragment;
import com.javinindia.employeeattendance.preference.SharedPreferencesManager;
import com.javinindia.employeeattendance.utility.CheckConnection;


/**
 * Created by Ashish on 26-09-2016.
 */
public class LoginActivity extends BaseActivity implements CheckConnectionFragment.OnCallBackInternetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
       // new MyAndroidFirebaseInstanceIdService();
        if (CheckConnection.haveNetworkConnection(this)) {
            String username = SharedPreferencesManager.getUsername(getApplicationContext());
            if (TextUtils.isEmpty(username)) {
                BaseFragment baseFragment = new LoginFragment();
                FragmentManager fm = this.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.setCustomAnimations(0, 0, 0, 0);
                fragmentTransaction.add(R.id.container, baseFragment);
                fragmentTransaction.commit();
            } else {
                String TYPE = SharedPreferencesManager.getType(getApplicationContext());
                if (TYPE.equals("TRACKER")){
                    BaseFragment baseFragment = new TrackerHomeFragment();
                    FragmentManager fm = this.getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.setCustomAnimations(0, 0, 0, 0);
                    fragmentTransaction.add(R.id.container, baseFragment);
                    fragmentTransaction.commit();
                }else {
                    BaseFragment baseFragment = new EmployeeDashboardFragment();
                    FragmentManager fm = this.getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.setCustomAnimations(0, 0, 0, 0);
                    fragmentTransaction.add(R.id.container, baseFragment);
                    fragmentTransaction.commit();
                }
            }
        } else {
            CheckConnectionFragment baseFragment = new CheckConnectionFragment();
            baseFragment.setMyCallBackInternetListener(this);
            FragmentManager fm = this.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.setCustomAnimations(0, 0, 0, 0);
            fragmentTransaction.add(R.id.container, baseFragment);
            fragmentTransaction.commit();
        }


    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_login;
    }

    @Override
    public void OnCallBackInternet() {
        Intent refresh = new Intent(this, LoginActivity.class);
        startActivity(refresh);//Start the same Activity
        finish();
    }

/*    public class MyAndroidFirebaseInstanceIdService extends FirebaseInstanceIdService {

        private static final String TAG = "MyAndroidFCMIIDService";
        @Override
        public void onTokenRefresh() {
            //Get hold of the registration token
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            if (!TextUtils.isEmpty(refreshedToken)) {
                SharedPreferencesManager.setDeviceToken(getApplicationContext(), refreshedToken);
            } else {

            }

        }

    }*/
}
