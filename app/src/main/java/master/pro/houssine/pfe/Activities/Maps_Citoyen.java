package master.pro.houssine.pfe.Activities;

import static java.lang.Double.parseDouble;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import master.pro.houssine.pfe.R;
import master.pro.houssine.pfe.Response.ResponseAddAccident;
import master.pro.houssine.pfe.Response.ResponseAfficheAccident;
import master.pro.houssine.pfe.Retrofit.InterfaceAPI;
import master.pro.houssine.pfe.Retrofit.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Maps_Citoyen extends AppCompatActivity  {


    private static final String CHANNEL_ID = "simplified coding";
    private static final String CHANNEL_NAME = "Simplified coding";
    private static final String CHANNEL_DESC = "simplified coding Notifications";

    private static int MY_FINE_LOCATION_REQUEST = 99;
    private static int MY_BACKGROUND_LOCATION_REQUEST = 100;
    private List<Polyline> polylines = null;
    protected LatLng start = null;

    //    private final int REQUEST_LOCATION = 1003;
    private static final int REQUEST_LOCATION = 0;
    boolean active = false;
    double longitude;
    double latitude;
    String serialID;
    private GoogleMap googleMap = null;
    String date = "";
    DatePickerDialog.OnDateSetListener date_serach;
    final Calendar myCalendar = Calendar.getInstance();
    boolean isbuttonClicked = false;
    private static int PLACE_PICKER_REQUEST = 1;
    LocationManager locationManager;
    Location location;
    private FusedLocationProviderClient locationProviderClient;
    Context context;
    ArrayList<ResponseAfficheAccident> branshes = new ArrayList<>();
    private MainActivity.LocListener locationListener;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference ref = db.getReference().child("Citoyen");
    Marker markerCurrent, marker, marker_accident;
    String degrees[] = {"Normale","Dangereux"};
    static String degre;
    int nbBlessures = 0;
    int nbMorts = 0;
    static String comm;

    MapView mMapView;

    FloatingActionButton historique;

    Button picker;

    FloatingActionButton ajoutAccident;

    @SuppressLint("VisibleForTests")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_citoyen);
        MapsInitializer.initialize(getApplicationContext());

        getBranshes();
        getHistorique_location(date);

        mMapView = findViewById(R.id.mapp);
        historique = findViewById(R.id.historique);
        picker = findViewById(R.id.picker);
        ajoutAccident = findViewById(R.id.ajoutAccident);

        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();




        locationProviderClient = new FusedLocationProviderClient(this);
        picker.setVisibility(View.INVISIBLE);


        final int year = myCalendar.get(Calendar.YEAR);
        final int month = myCalendar.get(Calendar.MONTH);
        final int day = myCalendar.get(Calendar.DAY_OF_MONTH);

        ajoutAccident.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {

                if (!isbuttonClicked) {

                    picker.setVisibility(View.VISIBLE);
                    ajoutAccident.setImageResource(R.drawable.check);
                    isbuttonClicked = true;
                    marker = null;

                } else {
                    isbuttonClicked = false;
                    picker.setVisibility(View.INVISIBLE);
                    ajoutAccident.setImageResource(R.drawable.car_collision);


                    if (googleMap != null) {
                        double myLat = googleMap.getCameraPosition().target.latitude;
                        double myLng = googleMap.getCameraPosition().target.longitude;
                        LatLng markerPoint = new LatLng(myLat, myLng);
                        marker_accident = googleMap.addMarker(new MarkerOptions().position(markerPoint)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.accidentlocation)));
                        serialID = getIMEIDeviceId(Maps_Citoyen.this);

                        final Dialog dialog = new Dialog(Maps_Citoyen.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.popup_bottom);

                        TextView text_blessure = dialog.findViewById(R.id.text_nbblessure);
                        TextView text_mort = dialog.findViewById(R.id.text_morts);
                        EditText comm_txt = (EditText) dialog.findViewById(R.id.commentaire);
                        Spinner spinner = (Spinner) dialog.findViewById(R.id.spinnerD);
                        ImageView plus_b = dialog.findViewById(R.id.addBlessure);
                        Button moin_b = dialog.findViewById(R.id.moinsBlessure);
                        ImageView plus_m = dialog.findViewById(R.id.addMorts);
                        Button moin_m = dialog.findViewById(R.id.moinsMorts);


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Maps_Citoyen.this, android.R.layout.simple_spinner_item, degrees);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                               degre = degrees[i].toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                        plus_b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                nbBlessures++;
                                text_blessure.setText("" + nbBlessures);
                            }
                        });

                        moin_b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                nbBlessures--;
                                text_blessure.setText("" + nbBlessures);
                            }
                        });

                        plus_m.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                nbMorts++;
                                text_mort.setText("" + nbMorts);
                            }
                        });

                        moin_m.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                nbMorts--;
                                text_mort.setText("" + nbMorts);
                            }
                        });

                        dialog.show();
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        dialog.getWindow().setGravity(Gravity.BOTTOM);

                        LinearLayout ajouter = dialog.findViewById(R.id.addacc);
                        LinearLayout annuler = dialog.findViewById(R.id.annul);

                        ajouter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                comm = comm_txt.getText().toString();
                                addAccident(serialID, String.valueOf(myLat), String.valueOf(myLng), comm, degre, nbBlessures, nbMorts);
                                dialog.dismiss();
                            }
                        });

                        annuler.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                marker_accident.remove();
                            }
                        });

                    }

                }
            }
        });


        historique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Maps_Citoyen.this, date_serach, year, month, day);
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
    }



    //***** Numero de series de téléphone *****

    @SuppressLint("HardwareIds")
    public String getIMEIDeviceId(Context context) {

        String deviceId;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            deviceId = Settings.Secure.getString(Maps_Citoyen.this.getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) Maps_Citoyen.this.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (context.checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
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
                deviceId = Settings.Secure.getString(Maps_Citoyen.this.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }
        Log.d("deviceId", deviceId);
        return deviceId;
    }

    // ***** Web service d'ajouter un accident *****

    public void addAccident(String serialID, String latitude, String longitude, String comm, String degre, int nbBlessures, int nbMorts) {

        InterfaceAPI api = RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class);
        Call<ResponseAddAccident> call = api.addAccident(serialID, latitude, longitude, comm, degre, nbBlessures, nbMorts);

        call.enqueue(new Callback<ResponseAddAccident>() {

            @Override
            public void onResponse(Call<ResponseAddAccident> call, Response<ResponseAddAccident> response) {
                Toast.makeText(Maps_Citoyen.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseAddAccident> call, Throwable t) {

                AlertDialog alertDialog = new AlertDialog.Builder(Maps_Citoyen.this)

                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Erreur")
                        .setMessage("Désolé vous etes déja bloqué depuis l'adminstrateur")
                        .show();

                Toast.makeText(Maps_Citoyen.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    // ***** Web service d'affichage des accident *****

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

    // ***** Web service d'affichage des accident précédentes *****

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
                Toast.makeText(Maps_Citoyen.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Bitmap resizeMapIcons(String iconName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", Objects.requireNonNull(context).getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    //**** Afficher les accident sur map *****

    public void showMarkers() {

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                if (ContextCompat.checkSelfPermission(Objects.requireNonNull(Maps_Citoyen.this), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED
                        && ContextCompat.checkSelfPermission(Maps_Citoyen.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_DENIED) {

                    googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(Maps_Citoyen.this, R.raw.map_style));
                    googleMap.getUiSettings().setCompassEnabled(true);
                    googleMap.setMyLocationEnabled(true);
                    googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                        @Override
                        public void onMyLocationChange(Location location) {


                            location = location;
                            LatLng ltlng = new LatLng(location.getLatitude(), location.getLongitude());
                            if(active == false) {
                                for (int i = 0; branshes.size() > i; i++) {


                                    //************************** Afficher markers  ****************************//
                                    final ResponseAfficheAccident mbranshe = branshes.get(i);

                                    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                    if (ActivityCompat.checkSelfPermission(Maps_Citoyen.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Maps_Citoyen.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                        // TODO: Consider calling
                                        //    ActivityCompat#requestPermissions
                                        // here to request the missing permissions, and then overriding
                                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                        //                                          int[] grantResults)
                                        // to handle the case where the user grants the permission. See the documentation
                                        // for ActivityCompat#requestPermissions for more details.
                                        return;
                                    }
                                    location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                    longitude = location.getLongitude();
                                    latitude = location.getLatitude();

                                    Location loc2 = new Location("");
                                    loc2.setLatitude(parseDouble(mbranshe.getLatitude()));
                                    loc2.setLongitude(parseDouble(mbranshe.getLongitude()));

                                    float distanceInMeters = location.distanceTo(loc2);
                                    String distance = String.valueOf(distanceInMeters);

//                        Toast.makeText(Maps_Citoyen.this, distance, Toast.LENGTH_SHORT).show();
                                    if (distanceInMeters < 10000) {

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                                            channel.setDescription(CHANNEL_DESC);
                                            NotificationManager manager = Maps_Citoyen.this.getSystemService(NotificationManager.class);
                                            manager.createNotificationChannel(channel);
                                        }
                                        displayNotification(Maps_Citoyen.this);

                                        final Dialog dialog = new Dialog(Maps_Citoyen.this);
                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        dialog.setCancelable(true);
                                        dialog.setContentView(R.layout.alarm_alert);

                                        Vibrator vibrator = (Vibrator) Maps_Citoyen.this.getSystemService(Context.VIBRATOR_SERVICE);
                                        long[] pattern = {2000, 1000};
                                        vibrator.vibrate(pattern, 0);

                                        MediaPlayer mp = MediaPlayer.create(Maps_Citoyen.this, R.raw.alarm2);
                                        mp.start();

                                        Button fermer = (Button) dialog.findViewById(R.id.cancel);
                                        fermer.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialog.dismiss();
                                                mp.stop();
                                                vibrator.cancel();
                                            }
                                        });
                                        dialog.show();
                                    }
                                    active = true;
                                }
                            }
                            //CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(ltlng, 16f);
                            // mMap.animateCamera(cameraUpdate);
                        }
                    });


                } else {

                    ActivityCompat.requestPermissions(Maps_Citoyen.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

                }


                if (!(branshes.isEmpty())) {
                    for (int i = 0; branshes.size() > i; i++) {


                        //************************** Afficher markers  ****************************//
                        final ResponseAfficheAccident mbranshe = branshes.get(i);

                        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        longitude = location.getLongitude();
                        latitude = location.getLatitude();

                        Location loc2 = new Location("");
                        loc2.setLatitude(parseDouble(mbranshe.getLatitude()));
                        loc2.setLongitude(parseDouble(mbranshe.getLongitude()));

                        float distanceInMeters = location.distanceTo(loc2);
                        String distance = String.valueOf(distanceInMeters);

//                        Toast.makeText(Maps_Citoyen.this, distance, Toast.LENGTH_SHORT).show();
                        if (distanceInMeters < 10000) {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                                channel.setDescription(CHANNEL_DESC);
                                NotificationManager manager = Maps_Citoyen.this.getSystemService(NotificationManager.class);
                                manager.createNotificationChannel(channel);
                            }
                            displayNotification(Maps_Citoyen.this);

                            final Dialog dialog = new Dialog(Maps_Citoyen.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(true);
                            dialog.setContentView(R.layout.alarm_alert);

                            Vibrator vibrator = (Vibrator) Maps_Citoyen.this.getSystemService(Context.VIBRATOR_SERVICE);
                            long[] pattern = {2000, 1000};
                            vibrator.vibrate(pattern, 0);

                            MediaPlayer mp = MediaPlayer.create(Maps_Citoyen.this, R.raw.alarm2);
                            mp.start();

                            Button fermer = (Button) dialog.findViewById(R.id.cancel);
                            fermer.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                    mp.stop();
                                    vibrator.cancel();
                                }
                            });
                            dialog.show();
                        }

                        if (!(mbranshe.equals(""))) {
                            LatLng latLng = new LatLng(parseDouble(mbranshe.getLatitude()), parseDouble(mbranshe.getLongitude()));
                            Geocoder geocoder;
                            List<Address> addresses = new ArrayList<>();
                            geocoder = new Geocoder(Maps_Citoyen.this, Locale.getDefault());
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
//                            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(2).build();
//                            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 7);
//                            //mMap.moveCamera(update);
                            // googleMap.getUiSettings().setCompassEnabled(true);
                        }

                    }
                }

            }
        });
    }


    public static void displayNotification(Activity activity) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(activity, CHANNEL_ID)
                        .setSmallIcon(R.drawable.accidentlocation)
                        .setContentTitle("Attention !!!")
                        .setContentText("Il y a un accident après 10 kilomètres !!")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent intent = new Intent(activity, Maps_Citoyen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);

        NotificationManagerCompat mNotificationMgr = NotificationManagerCompat.from(activity);
        mNotificationMgr.notify(1, mBuilder.build());

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


    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                StringBuilder stringBuilder = new StringBuilder();
                String lat = String.valueOf(place.getLatLng().latitude);
                String lon = String.valueOf(place.getLatLng().longitude);

                serialID = getIMEIDeviceId(Maps_Citoyen.this);
                //addAccident(serialID, lat, lon);

                stringBuilder.append("LATITUDE:");
                stringBuilder.append(lat);
                stringBuilder.append("LONGITUDE:");
                stringBuilder.append(lon);
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(Maps_Citoyen.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Maps_Citoyen.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 99);
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 99);

            }

            return false;

        } else {
            return true;
        }

    }


}