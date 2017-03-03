package com.javinindia.employeeattendance.recyclerview;

import android.content.Context;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


import com.javinindia.employeeattendance.R;
import com.javinindia.employeeattendance.apiparsing.customersListparsing.CustomerDetails;
import com.javinindia.employeeattendance.font.FontAsapBoldSingleTonClass;
import com.javinindia.employeeattendance.font.FontAsapRegularSingleTonClass;
import com.javinindia.employeeattendance.picasso.CircleTransform;
import com.javinindia.employeeattendance.preference.SharedPreferencesManager;
import com.javinindia.employeeattendance.utility.Utility;
import com.javinindia.employeeattendance.volleycustomrequest.VolleySingleTon;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by Ashish on 18-11-2016.
 */
public class AllCustomersAdapter extends RecyclerView.Adapter<AllCustomersAdapter.ViewHolder> {
    List<Object> list;
    Context context;
    MyClickListener myClickListener;

    public AllCustomersAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Object> list) {
        this.list = list;
    }

    public List<Object> getData() {
        return list;
    }

    @Override
    public AllCustomersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_customer_item_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AllCustomersAdapter.ViewHolder viewHolder, final int position) {
        final VolleySingleTon volleySingleTon = VolleySingleTon.getInstance(context);
        final CustomerDetails requestDetail = (CustomerDetails) list.get(position);

        if (!TextUtils.isEmpty(requestDetail.getName().trim())) {
            String Name = requestDetail.getName().trim();
            viewHolder.txtName.setText(Utility.fromHtml(Name));
        }else {
            viewHolder.txtName.setText(Utility.fromHtml("Name not found"));
        }
        if (!TextUtils.isEmpty(requestDetail.getInsertDate().trim())) {
            String date = requestDetail.getInsertDate().trim();
            // Convert input string into a date
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date1 = null;
            try {
                date1 = inputFormat.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // Format date into output format
            DateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss");
            String outputString = outputFormat.format(date1);
            viewHolder.txtTime.setText(outputString);
        }else {
            viewHolder.txtTime.setText(Utility.fromHtml("Timing not found"));
        }

        if (!TextUtils.isEmpty(requestDetail.getAdhar_pic().trim())) {
            String pic = requestDetail.getAdhar_pic().trim();
            Picasso.with(context).load(pic).transform(new CircleTransform()).into(viewHolder.imgProfile);
        } else {
            Picasso.with(context).load(R.drawable.default_avatar).transform(new CircleTransform()).into(viewHolder.imgProfile);
        }

        viewHolder.rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myClickListener.onViewClick(position,requestDetail);
            }
        });

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView txtName, txtTime;
        public ImageView imgProfile;
        public ProgressBar progressBar;
        public RelativeLayout rlMain;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            rlMain = (RelativeLayout)itemLayoutView.findViewById(R.id.rlMain);
            progressBar = (ProgressBar) itemLayoutView.findViewById(R.id.progress);
            txtName = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtName);
            txtName.setTypeface(FontAsapBoldSingleTonClass.getInstance(context).getTypeFace());
            txtTime = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtTime);
            txtTime.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());
            imgProfile = (ImageView) itemLayoutView.findViewById(R.id.imgProfile);
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public interface MyClickListener {
        void onViewClick(int position, CustomerDetails detailsList);
    }

    public void setMyClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public void deleteItem(int index) {
        list.remove(index);
        notifyItemRemoved(index);
    }
}