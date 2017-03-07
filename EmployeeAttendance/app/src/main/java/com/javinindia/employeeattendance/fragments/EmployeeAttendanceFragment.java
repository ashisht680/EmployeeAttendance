package com.javinindia.employeeattendance.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.test.mock.MockPackageManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.javinindia.employeeattendance.BuildConfig;
import com.javinindia.employeeattendance.R;
import com.javinindia.employeeattendance.constant.Constants;
import com.javinindia.employeeattendance.font.FontAsapRegularSingleTonClass;
import com.javinindia.employeeattendance.location.GPSTracker;
import com.javinindia.employeeattendance.location.NewLoc;
import com.javinindia.employeeattendance.preference.SharedPreferencesManager;
import com.javinindia.employeeattendance.utility.CheckConnection;
import com.javinindia.employeeattendance.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.CAMERA;

/**
 * Created by Ashish on 16-02-2017.
 */

public class EmployeeAttendanceFragment extends BaseFragment implements View.OnClickListener, CheckConnectionFragment.OnCallBackInternetListener {
    private AppCompatButton btnCurrentLocation;
    private LinearLayout lnrImages;
    private ImageView imageView;
    public ArrayList<String> map = new ArrayList<String>();
    public ArrayList<Bitmap> mapbit = new ArrayList<Bitmap>();
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    HorizontalScrollView horizontalScrollView;
    GPSTracker gps;
    double latitude = 0.0;
    double longitude = 0.0;
    AppCompatEditText et_AttendanceDesc;
    private RequestQueue requestQueue;
    private int count = 0;
    private int ash = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        if (map.size() == 0) {
            horizontalScrollView.setVisibility(View.GONE);
        } else {
            horizontalScrollView.setVisibility(View.VISIBLE);
        }
        return view;
    }

    private void initialization(View view) {
        et_AttendanceDesc = (AppCompatEditText) view.findViewById(R.id.et_AttendanceDesc);
        horizontalScrollView = (HorizontalScrollView) view.findViewById(R.id.scroll1);
        btnCurrentLocation = (AppCompatButton) view.findViewById(R.id.btnCurrentLocation);
        btnCurrentLocation.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        AppCompatButton buttonAddImage = (AppCompatButton) view.findViewById(R.id.btnAddImages);
        buttonAddImage.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        AppCompatButton btnSubmit = (AppCompatButton) view.findViewById(R.id.btnSubmit);
        btnSubmit.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        lnrImages = (LinearLayout) view.findViewById(R.id.lnrImages);
        buttonAddImage.setOnClickListener(this);
        btnCurrentLocation.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        Log.e("resume", "res");
        super.onResume();
        if (map.size() == 0) {
            horizontalScrollView.setVisibility(View.GONE);
        } else {
            horizontalScrollView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.employee_attendance_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

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
        textView.setText("ATTENDANCE");
        textView.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCurrentLocation:
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
                } else {
                    methodLocation();
                }
                break;
            case R.id.btnAddImages:
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA);
                } else {
                    methodAddImages();
                }
                break;
            case R.id.btnSubmit:
                Utility.hideKeyboard(activity);
                if (CheckConnection.haveNetworkConnection(activity)) {
                    methodSubmitComment();
                } else {
                    CheckConnectionFragment fragment = new CheckConnectionFragment();
                    fragment.setMyCallBackInternetListener(this);
                    callFragmentMethod(fragment, this.getClass().getSimpleName(), R.id.container);
                }
                break;
        }
    }

    private void methodSubmitComment() {
        if (map.size() > 5) {
            Toast.makeText(activity, "You can capture upto 5 images only. Please de-select some.", Toast.LENGTH_SHORT).show();
        } else {
            if (map.size() > 0) {
                if (btnCurrentLocation.getText().toString().equalsIgnoreCase("DONE")) {
                    String des = et_AttendanceDesc.getText().toString().trim();
                    if (TextUtils.isEmpty(des)) {
                        Toast.makeText(activity, "You have not entered description", Toast.LENGTH_SHORT).show();
                    } else {
                        insertAttendance();
                    }
                } else {
                    Toast.makeText(activity, "Please press the location button", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(activity, "Please capture some images", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void insertAttendance() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.INSERT_ATTENDANCE_EMPLOYEE_URL,
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
                //http://hnwkart.com/attendance/employee_attendance.php?userid=45&lat=12.45&long=45.33&description=fdfdfdf
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", SharedPreferencesManager.getUserID(activity));
                params.put("lat", Double.toString(latitude));
                params.put("long", Double.toString(longitude));
                params.put("description", et_AttendanceDesc.getText().toString());
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private void responseImplement(String response) {
        JSONObject jsonObject = null;
        String msg = null, atId = null;
        int status = 0;
        try {
            //{"status":1,"msg":"success","id":"6"}
            jsonObject = new JSONObject(response);
            if (jsonObject.has("status"))
                status = jsonObject.optInt("status");
            if (jsonObject.has("msg"))
                msg = jsonObject.optString("msg");
            if (jsonObject.has("id")) {
                atId = jsonObject.optString("id");

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (status == 1) {
            uploadImage(atId, map.get(count));
        } else {
            if (!TextUtils.isEmpty(msg)) {
                showDialogMethod(msg);
            }
        }
    }

    private void uploadImage(final String atId, final String xyz) {
        final ProgressDialog loading = ProgressDialog.show(activity, "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.INSERT_ATTENDANCE_IMAGES_EMPLOYEE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String sResponse) {
                        loading.dismiss();
                        if (sResponse != null) {
                            Log.e("resimageashish", count + " " + sResponse);
                            count++;
                            if (count < map.size()) {
                                Log.e("imaggasdasvdsvh", count + "" + map.get(count) + "");
                                uploadImage(atId, map.get(count));

                                ash(map.get(count));
                            }
                            if (count == map.size()) {
                                Toast.makeText(activity, " Photo uploaded successfully", Toast.LENGTH_SHORT).show();
                                activity.onBackPressed();
                            }
                        } else {

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loading.dismiss();
                        volleyErrorHandle(volleyError);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //http://hnwkart.com/attendance/insert_employee_attendance_images.php?attend_id=4&pic=p.jpg
                Bitmap bitmap;
                bitmap = decodeFile(xyz);
                Log.e("imgBit", map.get(count));
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                mapbit.get(count).compress(Bitmap.CompressFormat.JPEG, 100, bos);
                byte[] data = bos.toByteArray();
                String encodedImage = Base64.encodeToString(data, Base64.DEFAULT);

                Map<String, String> params = new Hashtable<String, String>();
                params.put("attend_id", atId);
                params.put("pic", encodedImage + "image/jpeg");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue.add(stringRequest);
    }

    private void ash(String s) {
    }


    private void methodLocation() {
        if (!hasGPSDevice(activity)) {
            Toast.makeText(activity, "Gps not Supported", Toast.LENGTH_SHORT).show();
        }
        final LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(activity)) {
            Toast.makeText(activity, "Gps not enabled", Toast.LENGTH_SHORT).show();
            //enableLoc();
        } else {
            getLocationMethod();
        }
    }

    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    private void getLocationMethod() {
        gps = new GPSTracker(activity);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            if (latitude == 0.0 && longitude == 0.0) {
                btnCurrentLocation.setBackgroundColor(Utility.getColor(activity, R.color.toolbar_color));
                btnCurrentLocation.setText("TRY AGAIN");
            } else {
                btnCurrentLocation.setBackgroundColor(Utility.getColor(activity, R.color.offer_green_color));
                btnCurrentLocation.setText("DONE");
                Toast.makeText(activity, latitude + "," + longitude + "", Toast.LENGTH_LONG).show();
            }

        } else {
            gps.showSettingsAlert();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }

    private void methodAddImages() {

        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                Uri photoURI = FileProvider.getUriForFile(activity,
                        BuildConfig.APPLICATION_ID + ".provider",
                        f);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, 1);
            } else {
                requestPermission();
            }
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            startActivityForResult(intent, 1);
        }


    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(activity, CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(activity, new String[]{CAMERA}, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    methodAddImages();
                } else {
                    Toast.makeText(activity, "You Denied for camera permission so you cant't update image", Toast.LENGTH_SHORT).show();
                }
            }
            case 123: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted.
                    getLocationMethod();
                } else {
                    // User refused to grant permission. You can add AlertDialog here
                    Toast.makeText(activity, "You didn't give permission to access device location", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (map.size() == 0) {
            horizontalScrollView.setVisibility(View.GONE);
        } else {
            horizontalScrollView.setVisibility(View.VISIBLE);
        }
        if (resultCode == activity.RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    //BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = decodeFile(f.getAbsolutePath());
                    mapbit.add(bitmap);
                    Log.e("map data", map + "");
                    try {
                        //lnrImages.removeAllViews();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    imageView = new ImageView(activity);
                    imageView.setImageBitmap(bitmap);
                    imageView.setPadding(8, 8, 8, 8);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setAdjustViewBounds(true);
                    imageView.setId(ash);
                    lnrImages.addView(imageView);
                    final File finalF = f;
                    map.add(ash, finalF.getAbsolutePath());
                    ash++;
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            methodToShowDialogForDelete(v);
                            map.remove(finalF.getAbsolutePath());
                            int position = 0;
                            position = v.getId();
                            mapbit.remove(position);
                            /*if (v.getTag() instanceof Integer) {
                                position = (Integer) v.getTag();

                            }*/

                        }
                    });
                    String path = android.os.Environment.getExternalStorageDirectory() + File.separator + "Phoenix" + File.separator + "default";
                    //f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    String capture = file.getAbsolutePath();
                    //  map.add(capture);
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Bitmap decodeFile(String filePath) {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);
        final int REQUIRED_SIZE = 1024;
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, o2);
        return bitmap;
    }

    private void methodToShowDialogForDelete(final View v) {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                activity);
        alertDialogBuilder.setTitle("Delete Image");
        alertDialogBuilder.setMessage("Do you really want to delete this image");
        alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                lnrImages.removeView(v);
                if (map.size() == 0) {
                    horizontalScrollView.setVisibility(View.GONE);
                }
            }
        });
        alertDialogBuilder
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    @Override
    public void OnCallBackInternet() {

    }
}
