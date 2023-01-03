package master.pro.houssine.pfe.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import master.pro.houssine.pfe.Adapter.NotificationAdapter;
import master.pro.houssine.pfe.Adapter.RapportAdapter;
import master.pro.houssine.pfe.Model.SharedPrefManger;
import master.pro.houssine.pfe.Model.User;
import master.pro.houssine.pfe.R;
import master.pro.houssine.pfe.Response.ResponseAfficheRapport;
import master.pro.houssine.pfe.Response.ResponseRapports;
import master.pro.houssine.pfe.Retrofit.InterfaceAPI;
import master.pro.houssine.pfe.Retrofit.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RapportListeFragment extends Fragment {

    public static List<ResponseRapports> list;
    CircleImageView imageRapp;
    TextView created;
    int user_id;
    RecyclerView recyclerView;
    NotificationAdapter notificationAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_liste_rapport, container, false);

        imageRapp = view.findViewById(R.id.imageRapp);
        created = view.findViewById(R.id.created);
        recyclerView = view.findViewById(R.id.recycleview);


//        addRapport.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AddRapportFragment fragment = new AddRapportFragment();
//                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.frame_layout, fragment);
//                fragmentTransaction.commit();
//            }
//        });


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        afficheRapport();

        return view;
    }

    private void afficheRapport() {

        User user = SharedPrefManger.getInstance(getActivity()).getUser();

        int user_id = user.getId();

        InterfaceAPI api = RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class);
        Call<ResponseRapports> call = api.listRapports(user_id);

        call.enqueue(new Callback<ResponseRapports>() {
            @Override
            public void onResponse(Call<ResponseRapports> call, Response<ResponseRapports> response) {
                List<ResponseAfficheRapport> list = response.body().getRapports();
                RapportAdapter rapportAdapter = new RapportAdapter(getActivity(), list);
                recyclerView.setAdapter(rapportAdapter);
            }

            @Override
            public void onFailure(Call<ResponseRapports> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



}