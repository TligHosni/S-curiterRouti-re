package master.pro.houssine.pfe.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import master.pro.houssine.pfe.Model.SharedPrefManger;
import master.pro.houssine.pfe.Model.User;
import master.pro.houssine.pfe.R;
import master.pro.houssine.pfe.Response.ResponseAddRapport;
import master.pro.houssine.pfe.Retrofit.InterfaceAPI;
import master.pro.houssine.pfe.Retrofit.RetrofitClientInstance;
import master.pro.houssine.pfe.Utils.AppUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddRapportFragment extends Fragment  {

    private static final int PICK_IMAGE_REQUEST = 1;
    EditText  ageU_txt, vehicule_txt, detailR_txt, detailE_txt, description_txt,nbr_mort_txt;
    MapView map;
    ImageView image;
    Button add, cancel;
    private Spinner spinner,spinnerT;
    static String vitesse,typeU;
    String sexe_txt;
    String alcole_txt;
    private RadioGroup radioGroup_sexe,radioGroup_alcole;
    private RadioButton radioButton1,radioButton2,radioButton3,radioButton4;
    MarkerOptions markerOptions;
    private GoogleMap googleMap;
    private int id;
    private Context context;
    private static Uri mImageUri;
    String[] courses = {"1", "2", "3", "4", "5", "6"};
    String[] types = {"Jeune", "vieux", "vieillard", "mineur"};

    static String sexeUsager = null;
    static String alcole = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_rapport, container, false);

        ageU_txt = view.findViewById(R.id.ageU);
        vehicule_txt = view.findViewById(R.id.vehicule);
        spinner = view.findViewById(R.id.spinner);
        spinnerT = view.findViewById(R.id.spinnertype);
        detailR_txt = view.findViewById(R.id.detailR);
        detailE_txt = view.findViewById(R.id.detailE);
        description_txt = view.findViewById(R.id.description);
        nbr_mort_txt = view.findViewById(R.id.nbr_mort);
        map = view.findViewById(R.id.map);
        map.onCreate(savedInstanceState);
        map.onResume();
        image = view.findViewById(R.id.image);
        add = view.findViewById(R.id.addR);
        cancel = view.findViewById(R.id.cancel);

        radioGroup_sexe = (RadioGroup) view.findViewById(R.id.radiogroup_sexe);
        radioGroup_alcole = (RadioGroup) view.findViewById(R.id.radiogroup_alcole);

        radioButton1  = (RadioButton) view.findViewById(R.id.femme);
        radioButton2  = (RadioButton) view.findViewById(R.id.homme);
        radioButton1.setChecked(true);

        radioButton3 = (RadioButton) view.findViewById(R.id.pos);
        radioButton4= (RadioButton) view.findViewById(R.id.neg);
        radioButton3.setChecked(true);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, courses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                vitesse = courses[i].toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, types);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerT.setAdapter(adapter1);
        spinnerT.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                typeU = types[i].toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Bundle bundle = this.getArguments();
        Double latitude = bundle.getDouble("latitude");
        Double longitude = bundle.getDouble("longitude");

        Geocoder geocoder;
        final List<Address>[] addresses = new List[]{new ArrayList<>()};
        geocoder = new Geocoder(getContext(), Locale.getDefault());

        Objects.requireNonNull(map).getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                // Add a marker in Sydney and move the camera
                LatLng latLng = new LatLng(latitude, longitude);
                googleMap.addMarker(new MarkerOptions().position(latLng)
                        // below line is use to add custom marker on our map.
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.accidentlocation)));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style));
                googleMap.getUiSettings().setCompassEnabled(true);

                try {
                    addresses[0] = geocoder.getFromLocation(latitude, longitude, 1);
                } catch (
                        IOException e) {
                    e.printStackTrace();
                }
                String address = addresses[0].get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses[0].get(0).getLocality();
                String state = addresses[0].get(0).getAdminArea();
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
                        CircleImageView branch_img = v.findViewById(R.id.imageRapp);
                        branshe_adress.setText(state);

                        branshe_city.setText(address);
                        return v;
                    }
                });

                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                googleMap.moveCamera(update);
                googleMap.getUiSettings().setCompassEnabled(true);

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = SharedPrefManger.getInstance(getActivity()).getUser();
                int ageUsager = Integer.parseInt(ageU_txt.getText().toString());
                int nbr_mort = Integer.parseInt(nbr_mort_txt.getText().toString());

                // get selected radio button from radioGroup
               int selectedID = radioGroup_sexe.getCheckedRadioButtonId();
                // find the radiobutton by returned id

                if (selectedID == R.id.homme) {
                    sexeUsager = "Homme";
                } else {
                    if (selectedID == R.id.femme) {
                        sexeUsager = "Femme";
                    }
                }

                // get selected radio button from radioGroup
                int selectedId = radioGroup_alcole.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                radioButton2 = (RadioButton) view.findViewById(selectedId);

                if (selectedId == R.id.pos) {
                    alcole = "Positive";
                } else {
                    if (selectedId == R.id.neg) {
                        alcole = "Negative";
                    }
                }
                String vehicule = vehicule_txt.getText().toString();
                String detailR = detailR_txt.getText().toString();
                String detailE = detailE_txt.getText().toString();
                String description = description_txt.getText().toString();

                add(typeU, sexeUsager, ageUsager, vehicule, alcole, vitesse, detailR, detailE, description, nbr_mort);
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        return view;
    }

    //Add Rapport


    //Add Images
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

            Picasso.get().load(mImageUri).into(image);

        }
    }

    private void add(String typeUsager, String sexeUsager, int ageUsager, String vehicule, String alcole, String vitesse, String detailR, String detailE, String description, int nbr_mort) {


        Bundle bundle = this.getArguments();
        String latitude = String.valueOf(bundle.getDouble("latitude"));
        String longitude = String.valueOf(bundle.getDouble("longitude"));
        int accident_id = bundle.getInt("accident_id");

        MultipartBody.Part image = null;

        File file = AppUtils.getFile(getContext(), Uri.parse(String.valueOf(mImageUri)));
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        image = MultipartBody.Part.createFormData("image", file.getName(), reqFile);

        User user = SharedPrefManger.getInstance(getActivity()).getUser();
        int user_id = user.getId();

        InterfaceAPI api = RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class);
        Call<ResponseAddRapport> call = api.addRapport(latitude, longitude, typeUsager, ageUsager, sexeUsager,detailE, detailR,description, vehicule, vitesse, alcole,   user_id, image, accident_id, nbr_mort);
        call.enqueue(new Callback<ResponseAddRapport>() {

            @Override
            public void onResponse(Call<ResponseAddRapport> call, Response<ResponseAddRapport> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    Toast.makeText(getActivity(), "ouiii", Toast.LENGTH_LONG).show();
                    RapportListeFragment fragment = new RapportListeFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, fragment);
                    fragmentTransaction.commit();
                } else {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseAddRapport> call, Throwable t) {

            }
        });
    }


}
