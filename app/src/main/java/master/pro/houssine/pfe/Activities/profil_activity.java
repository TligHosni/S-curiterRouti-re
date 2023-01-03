package master.pro.houssine.pfe.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import master.pro.houssine.pfe.Fragments.HomeFragment;
import master.pro.houssine.pfe.Fragments.ProfilFragment;
import master.pro.houssine.pfe.Model.SharedPrefManger;
import master.pro.houssine.pfe.Model.User;
import master.pro.houssine.pfe.R;
import master.pro.houssine.pfe.Retrofit.InterfaceAPI;
import master.pro.houssine.pfe.Retrofit.ResponseUpdateImage;
import master.pro.houssine.pfe.Retrofit.RetrofitClientInstance;
import master.pro.houssine.pfe.Utils.FileUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class profil_activity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static final int REQUEST_LOCATION = 1;

    LocationManager locationManager;
    String latitude,longitude;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference myRef = db.getReference().child("Users");

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int IMAGE_RESULT = 200;
    Button logout;
    Button edit;
    TextView name, phone, sexe, naissance, email;
    DrawerLayout drawerLayout;
    Button current_location;
    ImageView image_user;
    private Uri mImageUri;

    boolean isbuttonClicked = false;

    SharedPrefManger sharedPrefManger;
    static int id;

    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_PHONE = "phone1";
    private static final String KEY_PASSWORD = "password1";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);


        logout = findViewById(R.id.logout_profil);
        edit = findViewById(R.id.edit);
        image_user = findViewById(R.id.image);
        phone = findViewById(R.id.phone);
        name = findViewById(R.id.name);
        sexe = findViewById(R.id.sexe);
        email = findViewById(R.id.email);
        naissance = findViewById(R.id.naissance);
        drawerLayout = findViewById(R.id.drawer_layout);
        current_location = findViewById(R.id.position);

        User user = SharedPrefManger.getInstance(this).getUser();
        id = user.getId();

        phone.setText(user.getPhone());
        name.setText(user.getName());
        sexe.setText(user.getSexe());
        email.setText(user.getEmail());
        naissance.setText(user.getNaissance());

        name.setEnabled(false);
        name.setTextColor(Color.BLACK);
        sexe.setEnabled(false);
        sexe.setTextColor(Color.BLACK);
        email.setEnabled(false);
        email.setTextColor(Color.BLACK);
        naissance.setEnabled(false);
        naissance.setTextColor(Color.BLACK);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isbuttonClicked) {
                    name.setEnabled(true);
                    name.requestFocus();
                    name.setTextColor(Color.BLACK);

                    sexe.setEnabled(true);
                    sexe.requestFocus();
                    sexe.setTextColor(Color.BLACK);

                    email.setEnabled(true);
                    email.requestFocus();
                    email.setTextColor(Color.BLACK);

                    naissance.setEnabled(true);
                    naissance.requestFocus();
                    naissance.setTextColor(Color.BLACK);

                    isbuttonClicked = true;

                    edit.setBackgroundResource(R.drawable.check);

                } else {
                    Toast.makeText(profil_activity.this, "submit data !", Toast.LENGTH_SHORT).show();
                    isbuttonClicked = false;
                    image_user.setEnabled(false);
                    name.setEnabled(false);
                    sexe.setEnabled(false);
                    email.setEnabled(false);
                    naissance.setEnabled(false);

                    String new_name = name.getText().toString();
                    String new_email = email.getText().toString();

//                    updateProfile(new_name, new_email, user.getId());


                    edit.setBackgroundResource(R.drawable.editing);

                }

            }
        });

        image_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                HomeFragment fragment = new HomeFragment();
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
            }
        });


        current_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();

                } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    getLocation();
                }

            }
        });

    }

    private void openFileChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(image_user);

            updateImage(mImageUri, id);
        }
    }

    private void updateImage(Uri mImageUri, int id) {

        MultipartBody.Part image = null;
//        if (!old_image.equals(SharedPrefManager.getInstance(getActivity()).getUser().getImage())) {
        if (!image_user.equals("")) {

            File file = FileUtils.getFile(getApplicationContext(),mImageUri);
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            image = MultipartBody.Part.createFormData("image", file.getName(), reqFile);

        }
        InterfaceAPI api = RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class);
        Call<ResponseUpdateImage> call = api.updateImage(image, id);
        call.enqueue(new Callback<ResponseUpdateImage>() {

            @Override
            public void onResponse(Call<ResponseUpdateImage> call, Response<ResponseUpdateImage> response) {

                if (response.isSuccessful()) {

                    Toast.makeText(profil_activity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

//                                 Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
//                                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                 startActivity(intent);

                } else {

                    Toast.makeText(profil_activity.this, "Error", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseUpdateImage> call, Throwable t) {

            }
        });

    }

//    private void updateProfile(String name, String email, int id) {
//
//        InterfaceAPI api = RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class);
//        Call<ResponseUpdateProfile> call = api.updateProfile(name, email, id);
//        call.enqueue(new Callback<ResponseUpdateProfile>() {
//
//            @Override
//            public void onResponse(Call<ResponseUpdateProfile> call, Response<ResponseUpdateProfile> response) {
//
//                if (response.isSuccessful()) {
//
//                    Toast.makeText(profil_activity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
//
////                                 Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
////                                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
////                                 startActivity(intent);
//
//                } else {
//
//                    Toast.makeText(profil_activity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseUpdateProfile> call, Throwable t) {
//
//            }
//
//
//        });
//    }


//    public void ClickMenu(View view) {
//        //Open drawer
//
//        MainActivity.openDrawer(drawerLayout);
//    }
//
//    public void ClickLogo(View view) {
//
//        MainActivity.closeDrawer(drawerLayout);
//    }

    public void ClickProfile(View view) {
        Bundle bundle = new Bundle();
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        ProfilFragment fragment = new ProfilFragment();
        fragment.setArguments(bundle);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
    }

    public void ClickHome(View view) {
        Bundle bundle = new Bundle();
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(bundle);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
    }




    // code current location


    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(profil_activity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (profil_activity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(profil_activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location != null) {
                double lat = location.getLatitude();
                double longi = location.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);

               // TextView.setText("Your current location is"+ "\n" + "Lattitude = " + latitude + "\n" + "Longitude = " + longitude);

                HashMap<String,String> userMap = new HashMap<>();

                // userMap.put("name", user.getName() );
                userMap.put("latitude", latitude );
                userMap.put("longitude", longitude );

                myRef.setValue(userMap);

            } else  if (location1 != null) {
                double lat = location1.getLatitude();
                double longi = location1.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);

                //  textView.setText("Your current location is"+ "\n" + "Lattitude = " + lattitude + "\n" + "Longitude = " + longitude);

                HashMap<String,String> userMap = new HashMap<>();

                //   userMap.put("name", user.getName() );
                userMap.put("latitude", latitude );
                userMap.put("longgitude", longitude );

                myRef.setValue(userMap);


            } else  if (location2 != null) {
                double lat = location2.getLatitude();
                double longi = location2.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);

                //  textView.setText("Your current location is"+ "\n" + "Lattitude = " + lattitude + "\n" + "Longitude = " + longitude);

                HashMap<String,String> userMap = new HashMap<>();

                //  userMap.put("name", user.getName() );
                userMap.put("latitude", latitude );
                userMap.put("longitude", longitude );

                myRef.setValue(userMap);

            }else{

                Toast.makeText(this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();

            }
        }
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
//    @Override
//    protected void onStart() {
//
//        super.onStart();
//        if (sharedPrefManger.isLoggedIn()) {
//            Intent intent = new Intent(profil.this, MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//        }
//
//    }
}