package master.pro.houssine.pfe.Fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static java.lang.Double.parseDouble;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.annotations.NotNull;

import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import master.pro.houssine.pfe.Activities.LoginActivity;
import master.pro.houssine.pfe.R;
import master.pro.houssine.pfe.Response.ResponseAddAccident;
import master.pro.houssine.pfe.Response.ResponseAfficheAccident;
import master.pro.houssine.pfe.Retrofit.InterfaceAPI;
import master.pro.houssine.pfe.Retrofit.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {

//    private static String latitudee;
//    private static String longitudee;
//    private String serialID;
//
//   static List<ResponseAfficheAccident> lists = new ArrayList<>();
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Initialize view
//        View view = inflater.inflate(R.layout.fragment_map, container, false);
//
//
//        //Initialize map fragment
//        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
//
//        // Async map
//        Objects.requireNonNull(supportMapFragment).getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(@NonNull GoogleMap googleMap) {
//                //When map is loaded
//                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                    @Override
//                    public void onMapClick(@NonNull LatLng latLng) {
//                        // When click on map
//                        //Initialize marker options
//                        MarkerOptions markerOptions = new MarkerOptions();
//
//                        //set position of marker
//                        markerOptions.position(latLng);
//
//                        //set title of marker
//                        markerOptions.title(latLng.latitude + " : " + latLng.longitude);
//                        latitudee = String.valueOf(latLng.latitude);
//                        longitudee = String.valueOf(latLng.longitude);
//                        serialID = getIMEIDeviceId(getContext());
//                        addAccident(latitudee, longitudee, serialID);
//
//                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.carcollision));
//
//                        listeAccident(googleMap);
//
//                        //Animating to zom the marker
//                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
//
//                        googleMap.addMarker(markerOptions);
//                        listeAccident(googleMap);
//
//                    }
//
//                });
//
//
//            }
//        });
//        return view;
//    }
//
//    public static String getIMEIDeviceId(Context context) {
//
//        String deviceId;
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
//        } else {
//            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                    return "";
//                }
//            }
//            assert mTelephony != null;
//            if (mTelephony.getDeviceId() != null) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    deviceId = mTelephony.getImei();
//                } else {
//                    deviceId = mTelephony.getDeviceId();
//                }
//            } else {
//                deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
//            }
//        }
//        Log.d("deviceId", deviceId);
//        return deviceId;
//    }
//
//    public void addAccident(String latitudee, String longitudee, String serialID) {
//
//        InterfaceAPI api = RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class);
//        Call<ResponseAddAccident> call = api.addAccident(latitudee, longitudee, serialID);
//
//        call.enqueue(new Callback<ResponseAddAccident>() {
//
//            @Override
//            public void onResponse(Call<ResponseAddAccident> call, Response<ResponseAddAccident> response) {
//                Toast.makeText(getActivity(), "Accident added", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Call<ResponseAddAccident> call, Throwable t) {
//                Toast.makeText(getActivity(), "Accident Nottt", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }
//
//    public void listeAccident(GoogleMap googleMap) {
//        lists = new ArrayList<>();
//        InterfaceAPI api = RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class);
//        Call<ResponseAfficheAccident> call = api.listAccident();
//
//        call.enqueue(new Callback<ResponseAfficheAccident>() {
//            @Override
//            public void onResponse(Call<ResponseAfficheAccident> call, Response<ResponseAfficheAccident> response) {
//                if (response.isSuccessful()) {
//                        for (int i = 0; i < response.body().getResult().size(); i++) {
//                            lists = Collections.singletonList(response.body().getResult().get(i));
//                        }
//                        showMarkers(lists,googleMap);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseAfficheAccident> call, Throwable t) {
//                Toast.makeText(getActivity(), "Erreur", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }
//
//    public Bitmap resizeMapIcons(String iconName, int width, int height) {
//        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", Objects.requireNonNull(getContext()).getPackageName()));
//        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
//        return resizedBitmap;
//    }
//
//    public void showMarkers(List<ResponseAfficheAccident> list, GoogleMap googleMap) {
////        for (int i = 0; i < list.size(); i++) {
////
////            MarkerOptions markerOptions = new MarkerOptions();
////
////            LatLng latLng = new LatLng(parseDouble(list.get(i).getLatitude()), parseDouble(list.get(i).getLongitude()));
////
////            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.carcollision));
////
////            googleMap.addMarker(markerOptions.title(list.get(i).getLatitude()+":"+list.get(i).getLongitude()));
////
////           // LatLng latLng = new LatLng(Double.parseDouble(list.get(0).getLatitude()), Double.parseDouble(list.get(0).getLongitude()));
////
////            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(12).build();
////            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
////
////
////        }
//        for (int i = 0; i < list.size(); i++) {
//
//            final list mbranshe = lists.get(i);
//
//            LatLng latLng = new LatLng(Double.parseDouble(mbranshe.), Double.parseDouble(mbranshe.getLongitude()));
//            googleMap.addMarker(new MarkerOptions()
//                    .position(latLng)
//                    .anchor(0.5f, 0.5f)
//                    .title(mbranshe.getName())
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_icon_map_pointer)));
//
//
//
//        }

    private static final String GOOGLEMAP_COMPASS = "GoogleMapCompass";                   // [4]
    private static final String GOOGLEMAP_TOOLBAR = "GoogleMapToolbar";                   // [3]
    private static final String GOOGLEMAP_ZOOMIN_BUTTON = "GoogleMapZoomInButton";        // [2]child[0]
    private static final String GOOGLEMAP_ZOOMOUT_BUTTON = "GoogleMapZoomOutButton";      // [2]child[1]
    private static final String GOOGLEMAP_MYLOCATION_BUTTON = "GoogleMapMyLocationButton";// [0]

    private final int REQUEST_LOCATION = 1003;
    MapView mMapView;
    String latitudee;
    String longitudee;
    String serialID;
    private GoogleMap googleMap;
    Button addPicker;
    Button addRapport;
    Button historique;
    String date = "";
    DatePickerDialog.OnDateSetListener date_serach;
    final Calendar myCalendar = Calendar.getInstance();
    Button picker;
    TextView placeName;
    boolean isbuttonClicked = false;
    private static int PLACE_PICKER_REQUEST = 1;

    ArrayList<ResponseAfficheAccident> branshes = new ArrayList<>();
    private BreakIterator txtLatitude;
    private BreakIterator txtLongitude;

    public void LocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        getHistorique_location(date);

        mMapView = view.findViewById(R.id.mapp);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        Objects.requireNonNull(mMapView).getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                //When map is loaded
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {
                        // When click on map
                        //Initialize marker options
                        MarkerOptions markerOptions = new MarkerOptions();

                        //set position of marker
                        markerOptions.position(latLng);

                        //set title of marker
                        markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                        latitudee = String.valueOf(latLng.latitude);
                        longitudee = String.valueOf(latLng.longitude);
                        serialID = getIMEIDeviceId(getContext());
                        addAccident(serialID, latitudee, longitudee);

                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.accidentlocation));

                        //Animating to zom the marker
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));

                        googleMap.addMarker(markerOptions);
                    }
                });
            }
        });

        addRapport = view.findViewById(R.id.addRapport);
        picker = view.findViewById(R.id.picker);
        historique = view.findViewById(R.id.historique);
        picker.setVisibility(View.INVISIBLE);

        final int year = myCalendar.get(Calendar.YEAR);
        final int month = myCalendar.get(Calendar.MONTH);
        final int day = myCalendar.get(Calendar.DAY_OF_MONTH);


        historique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date_serach, year, month, day);
                datePickerDialog.show();
            }
        });

        date_serach = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = year + "-" + month + "-" + day;
                getHistorique_location(date);
            }
        };


        addPicker = view.findViewById(R.id.addPicker);
        addPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isbuttonClicked) {
                    picker.setVisibility(View.VISIBLE);
//                    placeName.setVisibility(View.VISIBLE);
                    picker.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                            try {
                                startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                            } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    isbuttonClicked = true;

                    addPicker.setBackgroundResource(R.drawable.check_box);
                } else {
                    addPicker.setBackgroundResource(R.drawable.addrapport);
                    picker.setVisibility(View.INVISIBLE);
//                    placeName.setVisibility(View.INVISIBLE);

                    isbuttonClicked = false;
                }
            }

        });

        addRapport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getActivity());
                String lat = String.valueOf(place.getLatLng().latitude);
                String lon = String.valueOf(place.getLatLng().longitude);
                String placeName = String.valueOf(place.getName());
                txtLatitude.setText("Latitude : " + lat);
                txtLongitude.setText("Longitude : " + lon);

                MarkerOptions markerOptions = new MarkerOptions();

                //set title of marker
                markerOptions.title(lat + " : " + lon);
                latitudee = lat;
                longitudee = lon;
                serialID = getIMEIDeviceId(getContext());
                addAccident(latitudee, longitudee, serialID);

                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.accidentlocation));

                //Animating to zom the marker
                //  googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                googleMap.addMarker(markerOptions);
                Toast.makeText(getActivity(), placeName, Toast.LENGTH_SHORT).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);

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

    public void addAccident(String serialID, String latitude, String longitude) {

        InterfaceAPI api = RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class);
        Call<ResponseAddAccident> call = api.addAccident(serialID, latitude, longitude,"null","null",0,0);

        call.enqueue(new Callback<ResponseAddAccident>() {

            @Override
            public void onResponse(Call<ResponseAddAccident> call, Response<ResponseAddAccident> response) {
                Toast.makeText(getActivity(), "Accident added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseAddAccident> call, Throwable t) {
                Toast.makeText(getActivity(), "Accident Nottt", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void showAlert(String serialID, String latitude, String longitude) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.confirme_accident);

        TextView confirm = (TextView) dialog.findViewById(R.id.confirm);
        TextView annuler = (TextView) dialog.findViewById(R.id.annuler);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAccident(serialID, latitude, longitude);
                dialog.dismiss();
            }
        });

        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void getBranshes() {

        branshes = new ArrayList<>();
        InterfaceAPI api = RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class);
        Call<ResponseAfficheAccident> call = api.listAccident();
        call.enqueue(new Callback<ResponseAfficheAccident>() {
            @Override
            public void onResponse(@NotNull Call<ResponseAfficheAccident> call, @NotNull Response<ResponseAfficheAccident> response) {
                if (response.isSuccessful()) {

                    branshes = (ArrayList<ResponseAfficheAccident>) Objects.requireNonNull(response.body()).getResult();
                    showMarkers();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseAfficheAccident> call, @NotNull Throwable t) {
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateLabel() {
        String myFormat = "Y-m-d";
        SimpleDateFormat dateFormat = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        }
        date = (Objects.requireNonNull(dateFormat).format(myCalendar.getTime()));
        getHistorique_location(date);
    }

    public void getHistorique_location(String date) {

        branshes = new ArrayList<>();
        InterfaceAPI api = RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class);
        Call<ResponseAfficheAccident> call = api.listAccidents(date);
        call.enqueue(new Callback<ResponseAfficheAccident>() {
            @Override
            public void onResponse(@NotNull Call<ResponseAfficheAccident> call, @NotNull Response<ResponseAfficheAccident> response) {
                if (response.isSuccessful()) {

                    branshes = (ArrayList<ResponseAfficheAccident>) Objects.requireNonNull(response.body()).getResult();
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

                if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED
                        && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_DENIED) {

                    googleMap.setMyLocationEnabled(true);
                    googleMap.getUiSettings().setZoomControlsEnabled(true);
                    googleMap.getUiSettings().setCompassEnabled(true);

                } else {

                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

                }

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int locationPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
                    if (locationPermission != PackageManager.PERMISSION_GRANTED) {
                        setConrolsPositions();
                    } else {
                        setConrolsPositions();
                    }
                } else {
                    setConrolsPositions();
                }
                Geocoder geocoder;
                List<Address> addresses = null;
                geocoder = new Geocoder(getContext(), Locale.getDefault());

                if (!(branshes.isEmpty())) {
                    for (int i = 0; i < branshes.size(); i++) {

                        final ResponseAfficheAccident mbranshe = branshes.get(i);

                        //************************** add multiple markers ****************************//
//
//                        try {
//                            addresses = geocoder.getFromLocation(parseDouble(mbranshe.getLatitude()), parseDouble(mbranshe.getLongitude()), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        String address = addresses.get(0).getSubAdminArea(); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                        String city = addresses.get(0).getLocality();
//                        String state = addresses.get(0).getAdminArea();
//                        String country = addresses.get(0).getCountryName();
//                        String postalCode = addresses.get(0).getPostalCode();
//                        String knownName = addresses.get(0).getFeatureName();

                        if (!(mbranshe.equals(" "))) {
                            LatLng latLng = new LatLng(parseDouble(mbranshe.getLatitude()), parseDouble(mbranshe.getLongitude()));
                            Marker marker = googleMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .anchor(0.5f, 0.5f)
                                    .title("Adresse:")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.accidentlocation)));
//                        marker.showInfoWindow();


                            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(2).build();
                            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 7);
                            mMap.moveCamera(update);
                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            googleMap.getUiSettings().setZoomControlsEnabled(true);
                            googleMap.getUiSettings().setIndoorLevelPickerEnabled(true);
                            googleMap.getUiSettings().setCompassEnabled(true);
                            moveZoomControls(mMapView, -1, -1, 0, 0, false, true);
                        }
//                        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//                            @Override
//                            public View getInfoWindow(Marker marker) {
//
//                                return null;
//                            }
//
//                            @Override
//                            public View getInfoContents(Marker marker) {
//                                View v = getLayoutInflater().inflate(R.layout.marker_info, null);
//                                TextView branshe_adress = v.findViewById(R.id.adress);
//                                TextView branshe_date = v.findViewById(R.id.date);
//                                TextView branshe_city = v.findViewById(R.id.city);
//
//                                branshe_adress.setText(state);
//
//                                @SuppressLint("SimpleDateFormat")
//                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//                                Date convertedDate = new Date();
//                                try {
//                                    convertedDate = dateFormat.parse(mbranshe.getCreated());
//                                    @SuppressLint("SimpleDateFormat")
//                                    SimpleDateFormat sdfnewformat = new SimpleDateFormat("HH:mm \n dd/MMM/yyyy  ");
//                                    String finalDateString = sdfnewformat.format(convertedDate);
//                                    branshe_date.setText(finalDateString);
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//
//                                branshe_city.setText(address);
//                                return v;
//                            }
//                        });
//
//
//                        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//                            @Override
//                            public void onInfoWindowClick(Marker marker) {
//
//                                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//
//                                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1001);
//                                } else {
//
//                                    Intent intent = new Intent(Intent.ACTION_CALL);
//
////                                intent.setData(Uri.parse("tel:" + HomeFragment.phone));
//                                    intent.setData(Uri.parse("Date:" + mbranshe.getCreated()));
//                                    startActivity(intent);
//                                }
//                            }
//                        });


                    }
                }
            }
        });
    }

    private void moveView(View view, int left, int top, int right, int bottom,
                          boolean horizontal, boolean vertical) {
        try {
            assert view != null;

            // replace existing layout params
            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);

            if (left >= 0) {
                rlp.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
                rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            }

            if (top >= 0) {
                rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            }

            if (right >= 0) {
                rlp.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
                rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            }

            if (bottom >= 0) {
                rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            }

            if (horizontal) {
                rlp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            }

            if (vertical) {
                rlp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            }

            rlp.setMargins(10, 10, right, bottom);

            view.setLayoutParams(rlp);
        } catch (Exception ex) {
            Log.e(TAG, "moveView() - failed: " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }

    private void moveZoomControls(View mapView, int left, int top, int right, int bottom,
                                  boolean horizontal, boolean vertical) {

        assert mapView != null;

        View zoomIn = mapView.findViewWithTag(GOOGLEMAP_ZOOMIN_BUTTON);

        // we need the parent view of the zoomin/zoomout buttons - it didn't have a tag
        // so we must get the parent reference of one of the zoom buttons
        View zoomInOut = (View) zoomIn.getParent();

        if (zoomInOut != null) {
            moveView(zoomInOut, left, top, right = 40, bottom = 10, horizontal, vertical);
        }
    }

    void setConrolsPositions() {
        try {
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
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);

            final ViewGroup parent = (ViewGroup) mMapView.findViewWithTag("GoogleMapMyLocationButton").getParent();
            parent.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        Resources r = getResources();
                        //convert our dp margin into pixels
                        int marginPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, r.getDisplayMetrics());
                        // Get the map compass view

                        View mapLocation = mMapView.findViewWithTag("GoogleMapMyLocationButton");

                        // create layoutParams, giving it our wanted width and height(important, by default the width is "match parent")
                        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(mapLocation.getHeight(),
                                mapLocation.getHeight());
                        // position on top right
                        rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
                        rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
                        //give compass margin
                        rlp.setMargins(marginPixels, marginPixels, marginPixels, marginPixels);
                        mapLocation.setLayoutParams(rlp);


                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED
                            && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_DENIED) {
                        setConrolsPositions();
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

}