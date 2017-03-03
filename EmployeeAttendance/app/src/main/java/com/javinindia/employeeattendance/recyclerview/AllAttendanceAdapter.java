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
 * Created by Ashish on 20-02-2017.
 */

public class AllAttendanceAdapter extends RecyclerView.Adapter<AllAttendanceAdapter.ViewHolder> {
    List<Object> list;
    Context context;
    MyClickListener myClickListener;

    public AllAttendanceAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Object> list) {
        this.list = list;
    }

    public List<Object> getData() {
        return list;
    }

    @Override
    public AllAttendanceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attendance_item_layout, parent, false);
         ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AllAttendanceAdapter.ViewHolder viewHolder, final int position) {
        final VolleySingleTon volleySingleTon = VolleySingleTon.getInstance(context);
        final AttandanceDetails requestDetail = (AttandanceDetails) list.get(position);

        if (!TextUtils.isEmpty(requestDetail.getAdetails().getLocation().trim())) {
            String location = requestDetail.getAdetails().getLocation().trim();
            viewHolder.txtName.setText(Utility.fromHtml(location));
        }else {
            viewHolder.txtName.setText(Utility.fromHtml("Location not found"));
        }
        if (!TextUtils.isEmpty(requestDetail.getAdetails().getInsertDate().trim())) {
            String date = requestDetail.getAdetails().getInsertDate().trim();
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

        if (!TextUtils.isEmpty(requestDetail.getAdetails().getDescription().trim())){
            String des  =requestDetail.getAdetails().getDescription().trim();
            viewHolder.txtDescription.setText(des);
        }else {
            viewHolder.txtDescription.setText(Utility.fromHtml("Description not found"));
        }

        if (requestDetail.getDetails_imagesArrayList().size()>0){
            if (!TextUtils.isEmpty(requestDetail.getDetails_imagesArrayList().get(0).getImage_name().trim())) {
                String pic = requestDetail.getDetails_imagesArrayList().get(0).getImage_name().trim();
                Picasso.with(context).load(pic).transform(new CircleTransform()).into(viewHolder.imgProfile);
            } else {
                Picasso.with(context).load(R.drawable.default_avatar).transform(new CircleTransform()).into(viewHolder.imgProfile);
            }
        }else {
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

        public AppCompatTextView txtName, txtTime,txtDescription;
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
            txtDescription = (AppCompatTextView)itemLayoutView.findViewById(R.id.txtDescription);
            txtDescription.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());
            imgProfile = (ImageView) itemLayoutView.findViewById(R.id.imgProfile);
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public interface MyClickListener {
        void onViewClick(int position, AttandanceDetails detailsList);
    }

    public void setMyClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public void deleteItem(int index) {
        list.remove(index);
        notifyItemRemoved(index);
    }
}
