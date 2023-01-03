package master.pro.houssine.pfe.Fragments;

import static android.app.Activity.RESULT_OK;
import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.geofire.LocationCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import master.pro.houssine.pfe.Activities.LoginActivity;
import master.pro.houssine.pfe.Activities.MainActivity.LocListener;
import master.pro.houssine.pfe.Model.SharedPrefManger;
import master.pro.houssine.pfe.Model.User;
import master.pro.houssine.pfe.R;
import master.pro.houssine.pfe.Response.ResponseDeleteToken;
import master.pro.houssine.pfe.Response.ResponseUpdateProfile;
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


public class ProfilFragment extends Fragment {


    private static final int PICK_IMAGE_REQUEST = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    private DatabaseReference refBase = null;
    private DatabaseReference refLocation = null;
    private DatabaseReference refUser = null;


    LocationManager locationManager;
    String latitude, longitude;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference myRef = db.getReference().child("Users");

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("geofire");
    GeoFire geoFire = new GeoFire(ref);

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int IMAGE_RESULT = 200;
    private static final int REQUEST_LOCATION = 1;

    ImageView logout;
    ImageView edit;
    TextView name, phone, sexe, naissance, email, edit_text;
    CircleImageView image_user;
    Button current_location;
    LinearLayout edit_layout;
    Context context;
    boolean isbuttonClicked = false;
    SharedPrefManger sharedPrefManger;
    private Uri mImageUri;

    final Calendar myCalendar = Calendar.getInstance();
    static int id;


    public ProfilFragment() {
        // Required empty public constructor
    }

    public static int getAllPermissionsResult() {
        return ALL_PERMISSIONS_RESULT;
    }

    @SuppressLint({"MissingPermission", "WrongConstant", "MissingInflatedId"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        LocListener locationListener = new LocListener();

        logout = view.findViewById(R.id.logoutCompte);
        edit = view.findViewById(R.id.update_image);
        edit_layout = view.findViewById(R.id.ll3);
        edit_text = view.findViewById(R.id.update_text);
        image_user = view.findViewById(R.id.image_user);
        phone = view.findViewById(R.id.phone);
        name = view.findViewById(R.id.name);
        sexe = view.findViewById(R.id.sexe);
        email = view.findViewById(R.id.email);
        naissance = view.findViewById(R.id.naissance);
        current_location = view.findViewById(R.id.position);


        User user = SharedPrefManger.getInstance(context).getUser();

        id = user.getId();

        phone.setText(user.getPhone());
        name.setText(user.getName());
        sexe.setText(user.getSexe());
        email.setText(user.getEmail());

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(user.getNaissance());
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdfnewformat = new SimpleDateFormat("yyyy-MM-d");
            String finalDateString = sdfnewformat.format(convertedDate);
            naissance.setText(finalDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        name.setEnabled(false);
        name.setTextColor(Color.BLACK);
        phone.setEnabled(false);
        phone.setTextColor(Color.BLACK);
        sexe.setEnabled(false);
        sexe.setTextColor(Color.BLACK);
        email.setEnabled(false);
        email.setTextColor(Color.BLACK);
        naissance.setEnabled(false);
        naissance.setTextColor(Color.BLACK);

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        edit_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isbuttonClicked) {
                    name.setEnabled(true);
                    name.requestFocus();
                    name.setTextColor(Color.BLACK);

                    phone.setEnabled(true);
                    phone.setTextColor(Color.BLACK);

                    email.setEnabled(true);
                    email.setTextColor(Color.BLACK);

                    naissance.setEnabled(true);
                    naissance.setTextColor(Color.BLACK);

                    isbuttonClicked = true;

                    edit.setBackgroundResource(R.drawable.check);
                    edit_text.setText("Confirmer");

                } else {
                    isbuttonClicked = false;
                    image_user.setEnabled(false);
                    phone.setEnabled(false);
                    name.setEnabled(false);
                    sexe.setEnabled(false);
                    email.setEnabled(false);
                    naissance.setEnabled(false);

                    edit.setBackgroundResource(R.drawable.edit);
                    edit_text.setText("Modifier");

                    String new_name = name.getText().toString();
                    String new_phone = phone.getText().toString();
                    String new_email = email.getText().toString();
                    String new_naissance = naissance.getText().toString();

                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    Date convertedDate = new Date();
                    try {
                        convertedDate = dateFormat.parse(naissance.getText().toString());
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat sdfnewformat = new SimpleDateFormat("yyyy-MM-d");
                        String finalDateString = sdfnewformat.format(convertedDate);
                        naissance.setText(finalDateString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    updateProfile(new_name, new_phone, new_email, new_naissance, user.getId());


                }

            }
        });

        String imageurl = SharedPrefManger.getInstance(getActivity()).getUser().getImage();
        String url = RetrofitClientInstance.API_BASE_URL_IMAGE + imageurl;
//        progressBar.setVisibility(View.VISIBLE);
        Glide.with(requireActivity())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(image_user);

//        image_user.setScaleType(ImageView.ScaleType.FIT_XY);

        image_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };

        naissance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogPickerDate(getContext());
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                logout();

            }
        });

        LocationCurrent();


        return view;
    }

    private void showDialogPickerDate(Context context) {

        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.datepicker);
        dialog.setCanceledOnTouchOutside(true);

        DatePicker picker = (DatePicker) dialog.findViewById(R.id.datePicker1);
        Button confirm_date = (Button) dialog.findViewById(R.id.confirm_date);

        confirm_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String new_naissance = picker.getYear() +"-"+ (picker.getMonth() + 1) +"-"+ picker.getDayOfMonth();
                naissance.setText(new_naissance);

                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-d";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        naissance.setText(dateFormat.format(myCalendar.getTime()));
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
        if (!image_user.equals(SharedPrefManger.getInstance(getActivity()).getUser().getImage())) {
            if (!image_user.equals("")) {

                User user = SharedPrefManger.getInstance(context).getUser();

                File file = FileUtils.getFile(getContext(), mImageUri);
                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                image = MultipartBody.Part.createFormData("image", file.getName(), reqFile);

            }
        }

        InterfaceAPI api = RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class);
        Call<ResponseUpdateImage> call = api.updateImage(image, id);
        call.enqueue(new Callback<ResponseUpdateImage>() {

            @Override
            public void onResponse(Call<ResponseUpdateImage> call, Response<ResponseUpdateImage> response) {

                if (response.isSuccessful()) {

                    sharedPrefManger = new SharedPrefManger(getContext());
                    sharedPrefManger.saveUser(Objects.requireNonNull(response).body().getUser());
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUpdateImage> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();


            }
        });
    }

    // ****** Update Profile ******
    private void updateProfile(String name, String phone, String email, String naissance, int id) {

        InterfaceAPI api = RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class);
        Call<ResponseUpdateProfile> call = api.updateProfile(name, phone, email, naissance+" 00:00:00", id);
        call.enqueue(new Callback<ResponseUpdateProfile>() {

            @Override
            public void onResponse(Call<ResponseUpdateProfile> call, Response<ResponseUpdateProfile> response) {

//                if (response.isSuccessful()) {

                //.User user = SharedPrefManger.getInstance(context).getUser();
                sharedPrefManger = new SharedPrefManger(getContext());
                sharedPrefManger.saveUser(response.body().getUser());
                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();

//                                 Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
//                                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                 startActivity(intent);

//                } else {
//
//                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();


//                }
            }

            @Override
            public void onFailure(Call<ResponseUpdateProfile> call, Throwable t) {

            }
        });
    }

    public void logout() {


        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom));

        builder.setMessage("Vous etes sur de quittez ??");
        builder.setCancelable(true);
        builder.setNegativeButton("OUI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteToken();
                SharedPrefManger.getInstance(getActivity()).clear();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // code current location


    private void deleteToken() {
        User user = SharedPrefManger.getInstance(getActivity()).getUser();
        int user_id = user.getId();
        SharedPrefManger sharedPrefManger = new SharedPrefManger(getActivity());
        String token = sharedPrefManger.getStringSaved(getContext(), "token");
        InterfaceAPI api = RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class);

        Call<ResponseDeleteToken> call = api.deleteToken(token, user_id);
        call.enqueue(new Callback<ResponseDeleteToken>() {

            @Override
            public void onResponse(Call<ResponseDeleteToken> call, Response<ResponseDeleteToken> response) {
//                Toast.makeText(getActivity(), "خروج..", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseDeleteToken> call, Throwable t) {
            }
        });
    }

    private void LocationCurrent() {

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                double lat = location.getLatitude();
                double longi = location.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);

                geoFire.setLocation("firebase-hq", new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        if (error != null) {
                            System.err.println("There was an error saving the location to GeoFire: " + error);
                        } else {
                            System.out.println("Location saved on server successfully!");
                        }
                    }
                });

                geoFire.getLocation("firebase-hq", new LocationCallback() {
                    @Override
                    public void onLocationResult(String key, GeoLocation location) {
                        if (location != null) {
                            System.out.println(String.format("The location for key %s is [%f,%f]", key, location.latitude, location.longitude));
                        } else {
                            System.out.println(String.format("There is no location for key %s in GeoFire", key));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.err.println("There was an error getting the GeoFire location: " + databaseError);
                    }
                });


                //Toast.makeText(getActivity(), "Your current location is" + "\n" + "Latitude = " + latitude + "\n" + "Longitude = " + longitude, Toast.LENGTH_SHORT).show();
                //TextView.setText(Integer.parseInt("Your current location is"+ "\n" + "Lattitude = " + latitude + "\n" + "Longitude = " + longitude));

                HashMap<String, String> userMap = new HashMap<>();

                // userMap.put("name", user.getName() );
                userMap.put("latitude", latitude);
                userMap.put("longitude", longitude);

                myRef.setValue(userMap);

            } else if (location1 != null) {
                double lat = location1.getLatitude();
                double longi = location1.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);

                //  textView.setText("Your current location is"+ "\n" + "Lattitude = " + lattitude + "\n" + "Longitude = " + longitude);

                HashMap<String, String> userMap = new HashMap<>();

                //   userMap.put("name", user.getName() );
                userMap.put("latitude", latitude);
                userMap.put("longgitude", longitude);

                myRef.setValue(userMap);


            } else if (location2 != null) {
                double lat = location2.getLatitude();
                double longi = location2.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);

                //  textView.setText("Your current location is"+ "\n" + "Lattitude = " + lattitude + "\n" + "Longitude = " + longitude);

                HashMap<String, String> userMap = new HashMap<>();

                //  userMap.put("name", user.getName() );
                userMap.put("latitude", latitude);
                userMap.put("longitude", longitude);

                myRef.setValue(userMap);

                geoFire.setLocation("firebase-hq", new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        if (error != null) {
                            System.err.println("There was an error saving the location to GeoFire: " + error);
                        } else {
                            System.out.println("Location saved on server successfully!");
                        }
                    }
                });


            } else {

                Toast.makeText(getContext(), "Unble to Trace your location", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void searchNearby(double latitude, double longitude, double radius) {
        this.searchNearby(new GeoLocation(latitude, longitude), radius);
    }

    //***** Geofire ******
    private void searchNearby(GeoLocation geoLocation, double radius) {

        GeoQuery geoQuery = this.geoFire.queryAtLocation(geoLocation, radius);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {

            @SuppressLint("RestrictedApi")
            @Override
            public void onKeyEntered(String key, GeoLocation location) {

                String loc = String.valueOf(location.latitude) + ", " + String.valueOf(location.longitude);
                Log.d(LOG_TAG, "onKeyEntered: " + key + " @ " + loc);

                /* once the key is known, one can lookup the associated record */
                refUser.child(key).addListenerForSingleValueEvent(new ValueEventListener() {

                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d(LOG_TAG, "onDataChange: " + dataSnapshot.toString());
                    }

                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onCancelled(@NonNull DatabaseError firebaseError) {
                        Log.e(LOG_TAG, "onCancelled: " + firebaseError.getMessage());
                    }
                });
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onKeyExited(String key) {
                Log.d(LOG_TAG, "onKeyExited: " + key);
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                Log.d(LOG_TAG, "onKeyMoved: " + key);
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onGeoQueryReady() {
                Log.d(LOG_TAG, "onGeoQueryReady");
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onGeoQueryError(DatabaseError error) {
                Log.e(LOG_TAG, "onGeoQueryError" + error.getMessage());
            }
        });
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
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


}