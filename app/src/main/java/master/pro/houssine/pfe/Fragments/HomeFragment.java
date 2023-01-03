package master.pro.houssine.pfe.Fragments;

import static java.lang.Double.parseDouble;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.annotations.NotNull;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import master.pro.houssine.pfe.Model.SharedPrefManger;
import master.pro.houssine.pfe.R;
import master.pro.houssine.pfe.Response.ResponseAfficheAccident;
import master.pro.houssine.pfe.Response.ResponseOccurAccident;
import master.pro.houssine.pfe.Response.ResponseUpdateProfile;
import master.pro.houssine.pfe.Retrofit.InterfaceAPI;
import master.pro.houssine.pfe.Retrofit.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    //  private final int REQUEST_LOCATION = 1003;
    private static final int REQUEST_LOCATION = 0;
    MapView mMapView;
    String latitudee;
    String longitudee;
    String serialID;
    private GoogleMap googleMap;
    FloatingActionButton currentLoc, addAccident, zoomon, zoomout;
    LocationManager locationManager;
    private FusedLocationProviderClient locationProviderClient;
    ConstraintLayout addrendez_vous;
    DatePickerDialog.OnDateSetListener date_serach;
    final Calendar myCalendar = Calendar.getInstance();
    final int year = myCalendar.get(Calendar.YEAR);
    final int month = myCalendar.get(Calendar.MONTH);
    final int day = myCalendar.get(Calendar.DAY_OF_MONTH);
    int myday, myMonth, myYear, myHour, myMinute, hour, minute;
    private static final String GOOGLEMAP_COMPASS = "GoogleMapCompass";                   // [4]
    private static final String GOOGLEMAP_TOOLBAR = "GoogleMapToolbar";                   // [3]
    private static final String GOOGLEMAP_ZOOMIN_BUTTON = "GoogleMapZoomInButton";        // [2]child[0]
    private static final String GOOGLEMAP_ZOOMOUT_BUTTON = "GoogleMapZoomOutButton";      // [2]child[1]
    private static final String GOOGLEMAP_MYLOCATION_BUTTON = "GoogleMapMyLocationButton";// [0]
    private static final String SHARED_PREF_NAME = "my_shared_preff";

    ArrayList<ResponseAfficheAccident> branshes = new ArrayList<>();
    private DrawerLayout drawerLayout;

    public void LocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        final Handler handler = new Handler();
        Runnable refresh = new Runnable() {
            @Override
            public void run() {
                // data request
                handler.postDelayed(this, 200);
            }
        };
        handler.postDelayed(refresh, 500);

        getBranshes();

        mMapView = view.findViewById(R.id.mapp);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        zoomon = view.findViewById(R.id.zoomon);
        zoomout = view.findViewById(R.id.zoomout);
        addrendez_vous = view.findViewById(R.id.addrendez_vous);

        addrendez_vous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.popup_rendez_vous);
                dialog.setCancelable(true);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                dialog.show();

                SharedPrefManger sharedPrefManger = new SharedPrefManger(getContext());
                String rendez_vous = sharedPrefManger.getInstance(getContext()).getUser().getRendez_vous();
                Toast.makeText(getActivity(), "rendez_vous: "+rendez_vous, Toast.LENGTH_SHORT).show();



                EditText date_day = dialog.findViewById(R.id.date_day);
                EditText time1 = dialog.findViewById(R.id.time1);
                EditText time2 = dialog.findViewById(R.id.time2);
                FloatingActionButton ok = dialog.findViewById(R.id.ok);
                ImageView ic_close = dialog.findViewById(R.id.ic_close);

                ic_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                date_day.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), date_serach, year, month, day);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int day) {
                                    myYear = year;
                                    myday = day;
                                    myMonth = month;
                                    Calendar c = Calendar.getInstance();
                                    hour = c.get(Calendar.HOUR);
                                    minute = c.get(Calendar.MINUTE);
                                    date_day.setText(year + "-" + (month+1) + "-" + day);

                                }
                            });
                        }
                        datePickerDialog.show();
                    }
                });

                time1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), R.style.MyAlertDialogStyle, new TimePickerDialog.OnTimeSetListener() {
                            @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
                            @Override
                            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                                time1.setText(selectedHour + ":" + selectedMinute + ":00");
                            }
                        }, hour, minute, false);
                        timePickerDialog.show();
                    }
                });

                time2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), R.style.MyAlertDialogStyle, new TimePickerDialog.OnTimeSetListener() {
                            @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
                            @Override
                            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                                time2.setText(selectedHour + ":" + selectedMinute + ":00");
                            }
                        }, hour, minute, false);
                        timePickerDialog.show();
                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//
                        try {
                            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

                            String starttime = date_day.getText().toString() +"T"+ time1.getText().toString();
                            String endtime = date_day.getText().toString()  +"T"+ time2.getText().toString();

                            Date mStartTime = mSimpleDateFormat.parse(date_day.getText().toString() +"T"+ time1.getText().toString());
                            Date mEndTime = mSimpleDateFormat.parse(date_day.getText().toString()  +"T"+ time2.getText().toString());

                            Intent intent = new Intent(Intent.ACTION_EDIT);
                            intent.setType("vnd.android.cursor.item/event");
                            intent.putExtra("beginTime", mStartTime.getTime());
                            intent.putExtra("time", true);
                            intent.putExtra("rule", "FREQ=YEARLY");
                            intent.putExtra("endTime", mEndTime.getTime());
                            intent.putExtra("title", "Rendez_Vous");
                            startActivity(intent);

                            SimpleDateFormat formatdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String finalDateString = formatdate.format(mStartTime);

                            SharedPrefManger sharedPrefManger = new SharedPrefManger(getContext());
                            int user_id = sharedPrefManger.getInstance(getContext()).getUser().getId();
                            update_rendez_vous(finalDateString,user_id);

                            SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("rendez_vous", finalDateString);
                            editor.apply();

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });


        locationProviderClient = new FusedLocationProviderClient(getContext());

        zoomon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleMap.animateCamera(CameraUpdateFactory.zoomIn());
//                SharedPrefManger sharedPrefManger = new SharedPrefManger(getContext());
//                SharedPrefManger.getInstance(getActivity()).clear();
//                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        zoomout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });

        return view;
    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
//        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        // vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        // vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    private void update_rendez_vous(String date, int user_id) {
        InterfaceAPI api = RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class);
        Call<ResponseUpdateProfile> call = api.Rendez_vous(date, user_id);
        call.enqueue(new Callback<ResponseUpdateProfile>() {

            @Override
            public void onResponse(Call<ResponseUpdateProfile> call, Response<ResponseUpdateProfile> response) {

                SharedPrefManger sharedPrefManger = new SharedPrefManger(getContext());
                sharedPrefManger.saveUser(response.body().getUser());
                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResponseUpdateProfile> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    @SuppressLint("HardwareIds")
    public static String getIMEIDeviceId(Context context) {

        String deviceId;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return "";
                }
            }
            assert mTelephony != null;
            if (mTelephony.getDeviceId() != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    deviceId = mTelephony.getImei();
                } else {
                    deviceId = mTelephony.getDeviceId();
                }
            } else {
                deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }
        Log.d("deviceId", deviceId);
        return deviceId;
    }

    public void getBranshes() {
        branshes = new ArrayList<>();
        InterfaceAPI api = RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class);
        Call<ResponseAfficheAccident> call = api.listAccident();
        call.enqueue(new Callback<ResponseAfficheAccident>() {
            @Override
            public void onResponse(@NotNull Call<ResponseAfficheAccident> call, @NotNull Response<ResponseAfficheAccident> response) {
                if (response.isSuccessful()) {

                    branshes = (ArrayList<ResponseAfficheAccident>) response.body().getResult();
                    final Handler handler = new Handler();
                    Runnable refresh = new Runnable() {
                        @Override
                        public void run() {
                            // data request
                            handler.postDelayed(this, 200);
                        }
                    };
                    handler.postDelayed(refresh, 500);
                    showMarkers();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseAfficheAccident> call, @NotNull Throwable t) {
            }
        });
    }

    public Bitmap resizeMapIcons(String iconName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", requireContext().getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    public void showMarkers() {
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED
                        && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_DENIED) {
                    googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style));
                    googleMap.getUiSettings().setCompassEnabled(true);
                    googleMap.setMyLocationEnabled(true);

                } else {

                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

                }

                for (int i = 0; i < branshes.size(); i++) {

                    ResponseAfficheAccident mbranshe = branshes.get(i);

                    googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                        @Override
                        public void onMapLongClick(@NonNull LatLng latLng) {
                            LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            Double longitude = location.getLongitude();
                            Double latitude = location.getLatitude();

                            DisplayTruck(latitude, longitude, Double.parseDouble(mbranshe.getLatitude()), Double.parseDouble(mbranshe.getLongitude()));

                        }
                    });
                    //  ************************** add multiple markers ****************************//
                    if (!(mbranshe.equals(""))) {
                        LatLng latLng = new LatLng(parseDouble(mbranshe.getLatitude()), parseDouble(mbranshe.getLongitude()));
                        Geocoder geocoder;
                        List<Address> addresses = new ArrayList<>();
                        geocoder = new Geocoder(getContext(), Locale.getDefault());
                        try {
                            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//                                if (addresses.size() > 0) {
                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();


                        Marker marker = googleMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .anchor(0.5f, 0.5f)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.accidentlocation)));
                        // }
                        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                            @Override
                            public View getInfoWindow(Marker marker) {

                                return null;
                            }

                            @Override
                            public View getInfoContents(Marker marker) {
                                View v = getLayoutInflater().inflate(R.layout.marker_info, null);
                                TextView branshe_adress = v.findViewById(R.id.adress);
                                TextView branshe_date = v.findViewById(R.id.date);
                                TextView branshe_city = v.findViewById(R.id.city);


                                branshe_adress.setText(state);

                                @SuppressLint("SimpleDateFormat")
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                Date convertedDate = new Date();
                                try {
                                    convertedDate = dateFormat.parse(mbranshe.getCreated());
                                    @SuppressLint("SimpleDateFormat")
                                    SimpleDateFormat sdfnewformat = new SimpleDateFormat("HH:mm \n dd/MMM/yyyy  ");
                                    String finalDateString = sdfnewformat.format(convertedDate);
                                    branshe_date.setText(finalDateString);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                branshe_city.setText(address);
                                return v;
                            }
                        });

                        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {
                                Bundle bundle = new Bundle();
                                int accident_id = mbranshe.getId();
                                InterfaceAPI api = RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class);
                                Call<ResponseOccurAccident> call = api.NbrOccurAccident(accident_id);

                                call.enqueue(new Callback<ResponseOccurAccident>() {

                                    @Override
                                    public void onResponse(Call<ResponseOccurAccident> call, Response<ResponseOccurAccident> response) {
                                        if (response.isSuccessful()) {
                                            if (response.body().getResult() == 1) {

                                                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom));

                                                builder.setMessage("Rapport déja ajouter");
                                                builder.setCancelable(true);
//                                                builder.setNegativeButton("OUI", new DialogInterface.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                                                    }
//                                                });
                                                AlertDialog alertDialog = builder.create();
                                                alertDialog.show();
                                            } else {
                                                AppCompatActivity activity = (AppCompatActivity) getContext();
                                                AddRapportFragment fragment = new AddRapportFragment();

                                                Double latitude = marker.getPosition().latitude;
                                                Double longitude = marker.getPosition().longitude;
                                                bundle.putDouble("latitude", latitude);
                                                bundle.putDouble("longitude", longitude);
                                                bundle.putInt("accident_id", accident_id);
                                                fragment.setArguments(bundle);
                                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();

                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseOccurAccident> call, Throwable t) {
                                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });


                            }
                        });
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(2).build();
                        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 7);
                        mMap.moveCamera(update);
                        googleMap.getUiSettings().setCompassEnabled(true);
                    }

                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED
                            && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_DENIED) {

                        googleMap.setMyLocationEnabled(true);

                    }
                }

                return;
            }
        }
    }

    @Override
    public void onStart() {
        mMapView.onStart();
        super.onStart();
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        mMapView.onLowMemory();
        super.onLowMemory();
    }

    public void DisplayTruck(Double sourceLatitude, Double sourceLongitude, Double destinationLatitude, Double destinationLongitude) {
        try {


            String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f,%f(%s)&daddr=%f,%f (%s)", sourceLatitude, sourceLongitude, "Home Sweet Home", destinationLatitude, destinationLongitude, "Where the party is at");
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Uri uri = Uri.parse("http://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

}
