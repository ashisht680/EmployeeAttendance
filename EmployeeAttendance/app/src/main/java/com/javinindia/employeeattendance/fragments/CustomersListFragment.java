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
import com.javinindia.employeeattendance.apiparsing.customersListparsing.CustomerDetails;
import com.javinindia.employeeattendance.apiparsing.customersListparsing.CustomerListParsingresponse;
import com.javinindia.employeeattendance.constant.Constants;
import com.javinindia.employeeattendance.font.FontAsapRegularSingleTonClass;
import com.javinindia.employeeattendance.preference.SharedPreferencesManager;
import com.javinindia.employeeattendance.recyclerview.AllCustomersAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ashish on 20-02-2017.
 */

public class CustomersListFragment extends BaseFragment implements AllCustomersAdapter.MyClickListener {
    private RecyclerView recyclerview;
    private int startLimit = 0;
    private int countLimit = 10;
    private boolean loading = true;
    private RequestQueue requestQueue;
    private AllCustomersAdapter adapter;
    ArrayList arrayList;
    AppCompatTextView txtDataNotFound;
    ProgressBar progressBar;
    String id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        id = getArguments().getString("id");
        getArguments().remove("id");
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
        sendRequestOnAllCustomerFeed(startLimit, countLimit);
        return view;
    }

    private void sendRequestOnAllCustomerFeed(final int AstartLimit, final int AcountLimit) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.CUSTOMER_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        CustomerListParsingresponse responseparsing = new CustomerListParsingresponse();
                        responseparsing.responseParseMethod(response);
                        txtDataNotFound.setText("Data not found");
                        int status = responseparsing.getStatus();
                        if (status ==1) {
                            if (responseparsing.getCustomerDetailsArrayList().size() > 0) {
                                arrayList = responseparsing.getCustomerDetailsArrayList();
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
                                  //  txtDataNotFound.setVisibility(View.VISIBLE);
                                }
                            } else {
                               //  txtDataNotFound.setVisibility(View.VISIBLE);
                            }
                        } else {
                           //  txtDataNotFound.setVisibility(View.VISIBLE);
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
                params.put("userid",id);
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
        textView.setText("CUSTOMERS");
        textView.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
    }

    private void initializeMethod(View view) {
        progressBar = (ProgressBar)view.findViewById(R.id.progress);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        adapter = new AllCustomersAdapter(activity);
        LinearLayoutManager layoutMangerDestination
                = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(layoutMangerDestination);
        recyclerview.addOnScrollListener(new replyScrollListener());
        recyclerview.setAdapter(adapter);
        adapter.setMyClickListener(CustomersListFragment.this);
        txtDataNotFound = (AppCompatTextView) view.findViewById(R.id.txtDataNotFound);
        txtDataNotFound.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }
    @Override
    protected int getFragmentLayout() {
        return R.layout.all_customers_list;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }

    @Override
    public void onViewClick(int position, CustomerDetails detailsList) {
        String id = detailsList.getId().trim();
        String trav_id  = detailsList.getTrav_id().trim();
        String adhar_number = detailsList.getAdhar_number().trim();
        String name = detailsList.getName().trim();
        String gender = detailsList.getGender().trim();
        String yob = detailsList.getYob().trim();
        String adhar_pic = detailsList.getAdhar_pic().trim();
        String description = detailsList.getDescription().trim();
        String care_of = detailsList.getCare_of().trim();
        String landmark = detailsList.getLandmark().trim();
        String village_town_city = detailsList.getVillage_town_city().trim();
        String post_office = detailsList.getPost_office().trim();
        String district = detailsList.getDistrict().trim();
        String state = detailsList.getState().trim();
        String pincode = detailsList.getPincode().trim();
        String insertDate = detailsList.getInsertDate().trim();

        CustomerViewFragment fragment1 = new CustomerViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("trav_id", trav_id);
        bundle.putString("adhar_number", adhar_number);
        bundle.putString("name", name);
        bundle.putString("gender", gender);
        bundle.putString("yob", yob);
        bundle.putString("adhar_pic", adhar_pic);
        bundle.putString("description", description);
        bundle.putString("care_of", care_of);
        bundle.putString("landmark", landmark);
        bundle.putString("village_town_city", village_town_city);
        bundle.putString("post_office", post_office);
        bundle.putString("district", district);
        bundle.putString("state", state);
        bundle.putString("pincode", pincode);
        bundle.putString("insertDate", insertDate);
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

                    sendRequestOnAllCustomerFeed(startLimit, countLimit);
                    loading = true;
                }
            }
            super.onScrollStateChanged(recyclerView, newState);
        }
    }
}
