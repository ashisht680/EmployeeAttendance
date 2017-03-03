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
import com.javinindia.employeeattendance.apiparsing.Attendanceparsing.AttandanceDetails;
import com.javinindia.employeeattendance.apiparsing.loginsignupparsing.Details;
import com.javinindia.employeeattendance.font.FontAsapBoldSingleTonClass;
import com.javinindia.employeeattendance.font.FontAsapRegularSingleTonClass;
import com.javinindia.employeeattendance.picasso.CircleTransform;
import com.javinindia.employeeattendance.utility.Utility;
import com.javinindia.employeeattendance.volleycustomrequest.VolleySingleTon;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Ashish on 21-02-2017.
 */

public class AllEmployeeAdapter extends RecyclerView.Adapter<AllEmployeeAdapter.ViewHolder> {
    List<Object> list;
    Context context;
    MyClickListener myClickListener;

    public AllEmployeeAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Object> list) {
        this.list = list;
    }

    public List<Object> getData() {
        return list;
    }

    @Override
    public AllEmployeeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attendance_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AllEmployeeAdapter.ViewHolder viewHolder, final int position) {
        final VolleySingleTon volleySingleTon = VolleySingleTon.getInstance(context);
        final Details requestDetail = (Details) list.get(position);

        if (!TextUtils.isEmpty(requestDetail.getName().trim())) {
            String name = requestDetail.getName().trim();
            viewHolder.txtName.setText(Utility.fromHtml(name));
        }else {
            viewHolder.txtName.setText(Utility.fromHtml("Name not found"));
        }
        if (!TextUtils.isEmpty(requestDetail.getEmail().trim())) {
            String date = requestDetail.getEmail().trim();
            viewHolder.txtTime.setText(date);
        }
        else {
            viewHolder.txtTime.setText(Utility.fromHtml("Timing not found"));
        }
        if (!TextUtils.isEmpty(requestDetail.getEmp_id().trim())) {
            String eId = requestDetail.getEmp_id().trim();
            viewHolder.txtDescription.setText("ID: "+eId);
        }


        if (!TextUtils.isEmpty(requestDetail.getProfile_pic().trim())) {
            String pic = requestDetail.getProfile_pic().trim();
            Picasso.with(context).load(pic).transform(new CircleTransform()).into(viewHolder.imgProfile);
        } else {
            Picasso.with(context).load(R.drawable.default_avatar).transform(new CircleTransform()).into(viewHolder.imgProfile);
        }


        viewHolder.rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myClickListener.onViewClick(position, requestDetail);
            }
        });

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView txtName, txtTime, txtDescription;
        public ImageView imgProfile;
        public ProgressBar progressBar;
        public RelativeLayout rlMain;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            rlMain = (RelativeLayout) itemLayoutView.findViewById(R.id.rlMain);
            progressBar = (ProgressBar) itemLayoutView.findViewById(R.id.progress);
            txtName = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtName);
            txtName.setTypeface(FontAsapBoldSingleTonClass.getInstance(context).getTypeFace());
            txtTime = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtTime);
            txtTime.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());
            txtDescription = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtDescription);
            txtDescription.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());
            imgProfile = (ImageView) itemLayoutView.findViewById(R.id.imgProfile);
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public interface MyClickListener {
        void onViewClick(int position, Details detailsList);
    }

    public void setMyClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public void deleteItem(int index) {
        list.remove(index);
        notifyItemRemoved(index);
    }
}
