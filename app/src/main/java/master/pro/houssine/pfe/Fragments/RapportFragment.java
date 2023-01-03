package master.pro.houssine.pfe.Fragments;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;

import master.pro.houssine.pfe.Model.SharedPrefManger;
import master.pro.houssine.pfe.Model.User;
import master.pro.houssine.pfe.R;
import master.pro.houssine.pfe.Retrofit.RetrofitClientInstance;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RapportFragment} factory method to
 * create an instance of this fragment.
 */
public class RapportFragment extends Fragment {

    TextView date, typeUsager, ageUsager, sexeUsager, detailRoute, detailEnv, vehicule;
    ImageView image;
    MapView map;

    static String date_d;
    static String latitude_d;
    static String longitude_d;
    static String typeUsager_d;
    static int ageUsager_d;
    static String sexeUsager_d;
    static String detailRoute_d;
    static String detailEnv_d;
    static String vehicule_d;
    static String image_d;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rapport, container, false);

        User user = SharedPrefManger.getInstance(getActivity()).getUser();

        date = view.findViewById(R.id.Date);
        typeUsager = view.findViewById(R.id.TypeUsager);
        ageUsager = view.findViewById(R.id.AgeUsager);
        sexeUsager = view.findViewById(R.id.SexeUsager);
        detailRoute = view.findViewById(R.id.DetailRoute);
        detailEnv = view.findViewById(R.id.DetailEnvironment);
        vehicule = view.findViewById(R.id.Vehicule);
        image = view.findViewById(R.id.image);
        map = (MapView) view.findViewById(R.id.map);
        map.onCreate(savedInstanceState);
        map.onResume();


        Bundle bundle = this.getArguments();

        if (bundle != null) {

            date_d = bundle.getString("date");
            latitude_d = bundle.getString("latitude");
            longitude_d = bundle.getString("longitude");
            typeUsager_d = bundle.getString("typeUsager");
            ageUsager_d = bundle.getInt("ageUsager");
            sexeUsager_d = bundle.getString("sexeUsager");
            detailRoute_d = bundle.getString("detailRoute");
            detailEnv_d = bundle.getString("detailEnv");
            vehicule_d = bundle.getString("vehicule");
            image_d = bundle.getString("image");
        }

        date.setText(date_d);
        typeUsager.setText(typeUsager_d);
        ageUsager.setText(Integer.toString(ageUsager_d));
        sexeUsager.setText(sexeUsager_d);
        detailRoute.setText(detailRoute_d);
        detailEnv.setText(detailEnv_d);
        vehicule.setText(vehicule_d);


        String url = RetrofitClientInstance.API_BASE_URL_IMAGE + image_d;
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
                .into(image);


        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        map.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                GoogleMap googleMap = mMap;

                // For dropping a marker at a point on the Map
                LatLng latLng = new LatLng(Double.parseDouble(user.getLatitude()), Double.parseDouble(user.getLongitude()));
                //LatLng sydney = new LatLng(-34, 151);
                googleMap.addMarker(new MarkerOptions().position(latLng).title("Marker Title").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }

        });



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        map.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map.onLowMemory();
    }
}