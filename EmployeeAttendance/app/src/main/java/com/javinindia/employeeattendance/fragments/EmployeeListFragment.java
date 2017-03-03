package com.javinindia.employeeattendance.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.javinindia.employeeattendance.R;
import com.javinindia.employeeattendance.apiparsing.Attendanceparsing.AttendanceresponseParsing;
import com.javinindia.employeeattendance.apiparsing.employeeParsingResponse.EmployeeResponse;
import com.javinindia.employeeattendance.apiparsing.loginsignupparsing.Details;
import com.javinindia.employeeattendance.constant.Constants;
import com.javinindia.employeeattendance.font.FontAsapRegularSingleTonClass;
import com.javinindia.employeeattendance.recyclerview.AllAttendanceAdapter;
import com.javinindia.employeeattendance.recyclerview.AllEmployeeAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ashish on 21-02-2017.
 */

public class EmployeeListFragment extends BaseFragment implements AllEmployeeAdapter.MyClickListener{
    private RecyclerView recyclerview;
    private int startLimit = 0;
    private int countLimit = 10;
    private boolean loading = true;
    private RequestQueue requestQueue;
    private AllEmployeeAdapter adapter;
    ArrayList arrayList;
    AppCompatTextView txtDataNotFound;
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        initializeMethod(view);
        sendRequestOnAllEmployeeFeed(startLimit, countLimit);
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
        AppCompatTextView textView =(AppCompatTextView)view.findViewById(R.id.tittle) ;
        textView.setText("EMPLOYEES");
        textView.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
    }

    private void initializeMethod(View view) {
        progressBar = (ProgressBar)view.findViewById(R.id.progress);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        adapter = new AllEmployeeAdapter(activity);
        LinearLayoutManager layoutMangerDestination
                = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(layoutMangerDestination);
        recyclerview.addOnScrollListener(new replyScrollListener());
        recyclerview.setAdapter(adapter);
        adapter.setMyClickListener(EmployeeListFragment.this);
        txtDataNotFound = (AppCompatTextView) view.findViewById(R.id.txtDataNotFound);
        txtDataNotFound.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());

    }

    private void sendRequestOnAllEmployeeFeed(final int AstartLimit, final int AcountLimit) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.EMPLOYEES_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        EmployeeResponse responseparsing = new EmployeeResponse();
                        responseparsing.responseParseMethod(response);
                        txtDataNotFound.setText("Data not found");
                        int status = responseparsing.getStatus();
                        if (status ==1) {
                            if (responseparsing.getDetailsArrayList().size() > 0) {
                                arrayList = responseparsing.getDetailsArrayList();
                                if (arrayList.size() > 0) {
                                    txtDataNotFound.setVisibility(View.GONE);
                                    if (adapter.getData() != null && adapter.getData().size() > 0) {
                                        adapter.getData().addAll(arrayList);
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        adapter.setData(arrayList);
                                        adapter.notifyDataSetChanged();
                                    }
                                } else {
                                   // txtDataNotFound.setVisibility(View.VISIBLE);
                                }
                            } else {
                                //txtDataNotFound.setVisibility(View.VISIBLE);
                            }
                        } else {
                          //   txtDataNotFound.setVisibility(View.VISIBLE);
                        }
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
                Map<String, String> params = new HashMap<String, String>();
              //  params.put("userid", id);
                params.put("startlimit", String.valueOf(AstartLimit));
                params.put("countlimit", String.valueOf(AcountLimit));
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }
    @Override
    protected int getFragmentLayout() {
        return R.layout.employee_list_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }

    @Override
    public void onViewClick(int position, Details detailsList) {
        String id = detailsList.getId().trim();
        String e_id  = detailsList.getEmp_id().trim();
        String email  = detailsList.getEmail().trim();
        String name = detailsList.getName().trim();
        String mobile = detailsList.getMobile().trim();
        String aMobile = detailsList.getAlt_mobile().trim();
        String profile_pic = detailsList.getProfile_pic().trim();
        String address = detailsList.getAddress().trim();
        String state = detailsList.getState().trim();
        String city  =detailsList.getCity().trim();
        String pincode = detailsList.getPincode().trim();

        EmployeeViewFragment fragment1 = new EmployeeViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("e_id", e_id);
        bundle.putString("email", email);
        bundle.putString("name", name);
        bundle.putString("mobile", mobile);
        bundle.putString("aMobile", aMobile);
        bundle.putString("profile_pic", profile_pic);
        bundle.putString("address", address);
        bundle.putString("state", state);
        bundle.putString("city",city);
        bundle.putString("pincode", pincode);
        bundle.putInt("position", position);

        fragment1.setArguments(bundle);
        callFragmentMethod(fragment1, this.getClass().getSimpleName(), R.id.container);
    }

    public class replyScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            LinearLayoutManager recyclerLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int visibleItemCount = recyclerView.getChildCount();
            int totalItemCount = recyclerLayoutManager.getItemCount();

            int visibleThreshold = ((totalItemCount / 2) < 20) ? totalItemCount / 2 : 20;
            int firstVisibleItem = recyclerLayoutManager.findFirstVisibleItemPosition();

            if (loading) {
                if (totalItemCount > startLimit) {
                    loading = false;
                    startLimit = totalItemCount;
                }
            } else {
                int nonVisibleItemCounts = totalItemCount - visibleItemCount;
                int effectiveVisibleThreshold = firstVisibleItem + visibleThreshold;

                if (nonVisibleItemCounts <= effectiveVisibleThreshold) {
                    startLimit = startLimit + 1;
                    countLimit = 10;

                    sendRequestOnAllEmployeeFeed(startLimit, countLimit);
                    loading = true;
                }
            }
            super.onScrollStateChanged(recyclerView, newState);
        }
    }
}
