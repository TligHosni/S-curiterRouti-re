package master.pro.houssine.pfe.Activities;

import static java.lang.Integer.parseInt;
import static master.pro.houssine.pfe.databinding.ActivityMainBinding.inflate;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import master.pro.houssine.pfe.Firebase.FirebaseDataReceiver;
import master.pro.houssine.pfe.Fragments.ContactFragment;
import master.pro.houssine.pfe.Fragments.DiscussionFragment;
import master.pro.houssine.pfe.Fragments.HomeFragment;
import master.pro.houssine.pfe.Fragments.NotificationsFragment;
import master.pro.houssine.pfe.Fragments.ProfilFragment;
import master.pro.houssine.pfe.Fragments.RapportListeFragment;
import master.pro.houssine.pfe.Model.SharedPrefManger;
import master.pro.houssine.pfe.Model.User;
import master.pro.houssine.pfe.R;
import master.pro.houssine.pfe.Response.ResponseAfficheLatLongAcc;
import master.pro.houssine.pfe.Response.ResponseDeleteToken;
import master.pro.houssine.pfe.Response.ResponseNbrNotif;
import master.pro.houssine.pfe.Response.ResponseRendezVous;
import master.pro.houssine.pfe.Response.ResponseUpdateAccident;
import master.pro.houssine.pfe.Retrofit.InterfaceAPI;
import master.pro.houssine.pfe.Retrofit.RetrofitClientInstance;
import master.pro.houssine.pfe.Services.LocationService;
import master.pro.houssine.pfe.databinding.ActivityMainBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity<mMessageReceiver> extends AppCompatActivity /*implements NavigationView.OnNavigationItemSelectedListener*/ {

    ActivityMainBinding binding;
    //    DrawerLayout drawerLayout;
//    ActionBarDrawerToggle actionBarDrawerToggle;
//    BottomNavigationView navigationView;
//    BottomAppBar bottomAppBar;
//    Toolbar toolbar;
//    View name;
//    SharedPrefManger sharedPrefManger;
//    TextView txtName;
//    View notificationIndicator;
//    LinearLayout linProfile, linHome, linlogout, linalarm, linnotif;
    private LocListener locationListener;
    CircleImageView image_user;
    BottomNavigationView bottomNavigationView;
    public static FirebaseDataReceiver firebaseDataReceiver;
    static int nb_dsc;
    String rendez_vous;




    public static void redirectActivity(Activity activity, Class aclasse) {
        Intent intent = new Intent(activity, aclasse);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(MainActivity.this, AcceuilActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        SharedPrefManger sharedPrefManger = new SharedPrefManger(MainActivity.this);
        int user_id = sharedPrefManger.getInstance(MainActivity.this).getUser().getId();

        InterfaceAPI api = RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class);
        Call<ResponseRendezVous> call = api.getRendezVous(user_id);
        call.enqueue(new Callback<ResponseRendezVous>() {

            @Override
            public void onResponse(Call<ResponseRendezVous> call, Response<ResponseRendezVous> response) {
//                    Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                rendez_vous = response.body().getRendezvous();
                try {
                    Date date_r = sdf.parse(rendez_vous);

                    Calendar rendezVousTime = Calendar.getInstance();
                    rendezVousTime.setTime(date_r);

//                    Toast.makeText(MainActivity.this, "new date  "+ rendezVousTime.getTime(), Toast.LENGTH_SHORT).show();
                    rendezVousTime.add(Calendar.MINUTE,-10);

//                    Toast.makeText(MainActivity.this, "new date  "+ rendezVousTime.getTime(), Toast.LENGTH_SHORT).show();

                    SimpleDateFormat sdfnewformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    String finalDateString = sdfnewformat.format(rendezVousTime.getTime());

                    Date mStartTime = sdfnewformat.parse(finalDateString);


                    Thread t = new Thread() {
                        @Override
                        public void run() {
                            try {
                                while (!isInterrupted()) {
                                    Thread.sleep(1000);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            String finalDateString_currentTime = sdfnewformat.format(Calendar.getInstance().getTime());
//                                            Toast.makeText(MainActivity.this, "new date  "+ finalDateString, Toast.LENGTH_SHORT).show();
//                                            Toast.makeText(MainActivity.this, "current date  "+ finalDateString_currentTime, Toast.LENGTH_SHORT).show();

                                            if(finalDateString.equals(finalDateString_currentTime)){
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                                                    NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT);
                                                    NotificationManager manager = MainActivity.this.getSystemService(NotificationManager.class);
                                                    manager.createNotificationChannel(channel);
                                                }
                                                String message = "Vous avez un rendez_vous après 10 minutes";
                                                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "My Notification");
                                                builder.setContentTitle("Rendez_vous");
                                                builder.setContentText(message);
                                                builder.setSmallIcon(R.drawable.logo6);
                                                builder.setAutoCancel(true);
                                                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
                                                managerCompat.notify(1, builder.build());
                                            }
                                        }
                                    });
                                }
                            } catch (InterruptedException e) {
                            }
                        }
                    };
                    t.start();



//                        Toast.makeText(MainActivity.this, "days" + elapsedDays + " heure" + elapsedHours + " minute" + elapsedMinutes, Toast.LENGTH_SHORT).show();

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseRendezVous> call, Throwable t) {
                Toast.makeText(MainActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
    }

    @Override
    protected void onPause() {
        super.onResume();
        onResume();
    }

    @Override
        protected void onDestroy() {
        super.onResume();
        onResume();
    }



    @SuppressLint({"NonConstantResourceId", "RestrictedApi", "WrongViewCast", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        User user = SharedPrefManger.getInstance(this).getUser();

        bottomNavigationView = findViewById(R.id.buttonPanel);
        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.rapport_fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                replaceFragement(new RapportListeFragment());
            }
        });

//****Crashlytics********
//        Button crashButton = new Button(this);
//        crashButton.setText("Test Crash");
//        crashButton.setOnClickListener(new View.OnClickListener(){
//            public void onClick (View view){
//                throw new RuntimeException("Test Crash"); // Force a crash
//            }
//        });
//        addContentView(crashButton, new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT));

//



        if (getIntent().getExtras() != null) {
            Intent intent = getIntent();
            String type = intent.getStringExtra("type");
            String element = intent.getStringExtra("element");
            String from = intent.getStringExtra("from");

            int d_id = Integer.parseInt(element);


            if (!type.equals("message")) {
                replaceFragement(new HomeFragment());
                if (from.equals("dataReceiver")) {
                    updateAccident(element);
                    trajet(d_id);
                }
            } else {
                if (from.equals("dataReceiver")) {
                    AppCompatActivity activity = MainActivity.this;
                    Bundle bundle = new Bundle();
                    bundle.putInt("discussion_id", d_id);
                    DiscussionFragment fragment = new DiscussionFragment();
                    fragment.setArguments(bundle);
                    fragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
                    replaceFragement(new ContactFragment());
                }
            }
        }


        final Handler handler = new Handler();
        Runnable refresh = new Runnable() {
            @Override
            public void run() {
                // data request
                handler.postDelayed(this, 20);
            }
        };
        handler.postDelayed(refresh, 20);

        nbrNotif();

        if (user != null) {

        }

        Intent servIntent = new Intent(getApplicationContext(), LocationService.class);
        startService(servIntent);
//        Toast.makeText(MainActivity.this, "Location service activated ... ", Toast.LENGTH_SHORT).show();


        locationListener = new LocListener();


        replaceFragement(new HomeFragment());
        binding.buttonPanel.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case R.id.home:
                    replaceFragement(new HomeFragment());
                    break;


                case R.id.placeholder:
                    replaceFragement(new RapportListeFragment());
                    break;

                case R.id.Profile:
                    replaceFragement(new ProfilFragment());
                    break;

                case R.id.Notification:
                    replaceFragement(new NotificationsFragment());
                    break;

                case R.id.contact:
                    replaceFragement(new ContactFragment());
                    break;
            }
            return true;
        });


        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                HomeFragment fragment = new HomeFragment();
                FragmentManager fragmentManager = Objects.requireNonNull(MainActivity.this).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, fragment);
                fragmentTransaction.commit();
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);

        // The callback can be enabled or disabled here or in handleOnBackPressed()

    }

    private void deleteToken() {
        User user = SharedPrefManger.getInstance(MainActivity.this).getUser();
        int user_id = user.getId();
        SharedPrefManger sharedPrefManger = new SharedPrefManger(MainActivity.this);
        String token = sharedPrefManger.getStringSaved(getApplicationContext(), "token");
        InterfaceAPI api = RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class);

        Call<ResponseDeleteToken> call = api.deleteToken(token, user_id);
        call.enqueue(new Callback<ResponseDeleteToken>() {

            @Override
            public void onResponse(Call<ResponseDeleteToken> call, Response<ResponseDeleteToken> response) {
                Toast.makeText(MainActivity.this, "Déconnexion ...", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<ResponseDeleteToken> call, Throwable t) {

            }
        });
    }

    public void badgeVisible(int v) {
        new Handler(this.getMainLooper()).postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                BadgeDrawable badgeDrawable = binding.buttonPanel.getOrCreateBadge(R.id.Notification);
                badgeDrawable.setVisible(true);
                badgeDrawable.setNumber(v);
                badgeDrawable.setVerticalOffset(dpToPx(MainActivity.this, 3));
                badgeDrawable.setBadgeTextColor(getResources().getColor(R.color.white));
                badgeDrawable.setBackgroundColor(getColor(R.color.red));
            }
        }, 1000);
    }

    public static int dpToPx(Context context, int dp) {
        Resources resources = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics()));
    }

    private void nbrNotif() {

        User user = SharedPrefManger.getInstance(MainActivity.this).getUser();
        int user_id = user.getId();

        InterfaceAPI api = RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class);

        Call<ResponseNbrNotif> call = api.NbrNotif(user_id);
        call.enqueue(new Callback<ResponseNbrNotif>() {

            @Override
            public void onResponse(Call<ResponseNbrNotif> call, Response<ResponseNbrNotif> response) {
                if (response.isSuccessful()) {
                    if (response.body().getNotif() > 0) {
                        nb_dsc = response.body().getNotif();
                        badgeVisible(nb_dsc);

                    }

                } else {
                    Toast.makeText(MainActivity.this, "Error! Please try again.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseNbrNotif> call, Throwable t) {
            }
        });

    }

    private void replaceFragement(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

    }

//
//    public void ClickMenu(View view) {
//        //Open drawer
//        openDrawer(drawerLayout);
//    }
//
//    public void ClickLogo(View view) {
//        closeDrawer(drawerLayout);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        closeDrawer(drawerLayout);
//    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 99);
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 99);

            }

            return false;

        } else {
            return true;
        }

    }

    public static class LocListener implements android.location.LocationListener {
        Context context;

        @Override
        public void onLocationChanged(Location location) {
            float distanceInMeters = 1;
            if (location != null) {

                Toast.makeText(context, "location Main : " + location.getLatitude() + " // " + location.getLongitude(), Toast.LENGTH_SHORT).show();

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user_locations");
                GeoFire geoFire = new GeoFire(ref);
                geoFire.setLocation(String.valueOf(SharedPrefManger.getInstance(context).getUser().getId()),
                        new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void onComplete(String key, DatabaseError error) {
                                if (error != null) {
                                    Log.d("geolocation!", location.getLatitude() + "" + location.getLongitude());
                                } else {
                                    Log.d("location sended successfully!", "success");
                                }

                            }
                        });
            }


        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }

    private void updateAccident(String intentelement) {

        InterfaceAPI api = RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class);

        Call<ResponseUpdateAccident> call = api.updateConfirmeAccident(parseInt(intentelement));
        call.enqueue(new Callback<ResponseUpdateAccident>() {
            @Override
            public void onResponse(Call<ResponseUpdateAccident> call, Response<ResponseUpdateAccident> response) {
                Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseUpdateAccident> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Nooo", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void trajet(int intentelement) {

        InterfaceAPI api = RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class);

        Call<ResponseAfficheLatLongAcc> call = api.ShowAccident(intentelement);
        call.enqueue(new Callback<ResponseAfficheLatLongAcc>() {
            @Override
            public void onResponse(Call<ResponseAfficheLatLongAcc> call, Response<ResponseAfficheLatLongAcc> response) {
                if (response.isSuccessful()) {

                    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

                    DisplayTruck(latitude, longitude, Double.parseDouble(response.body().getLat()), Double.parseDouble(response.body().getLon()));

                } else {
                    Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseAfficheLatLongAcc> call, Throwable t) {
                Toast.makeText(MainActivity.this, "noooooo....rrrrr", Toast.LENGTH_SHORT).show();
            }
        });

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






