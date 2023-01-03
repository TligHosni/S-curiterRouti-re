package master.pro.houssine.pfe.Fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import master.pro.houssine.pfe.Adapter.NotificationAdapter;
import master.pro.houssine.pfe.Model.SharedPrefManger;
import master.pro.houssine.pfe.Model.User;
import master.pro.houssine.pfe.R;
import master.pro.houssine.pfe.Response.ResponseAfficheNotification;
import master.pro.houssine.pfe.Response.ResponseNotifications;
import master.pro.houssine.pfe.Response.ResponseSeenNotif;
import master.pro.houssine.pfe.Retrofit.InterfaceAPI;
import master.pro.houssine.pfe.Retrofit.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment {

    public static List<ResponseNotifications> list;
    EditText title;
    EditText body;
    EditText created_txt;
    TextView notification_badge;

    int user_id;
    RecyclerView recyclerView;
    NotificationAdapter notificationAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        title = view.findViewById(R.id.title);
        body = view.findViewById(R.id.body);
        created_txt = view.findViewById(R.id.created);
        recyclerView = view.findViewById(R.id.recycleview);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        afficheNotif();

        return view;
    }

    private void afficheNotif() {

        User user = SharedPrefManger.getInstance(getActivity()).getUser();

        int user_id = user.getId();

        InterfaceAPI api = RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class);
        Call<ResponseNotifications> call = api.listNotifications(user_id);

        call.enqueue(new Callback<ResponseNotifications>() {
            @Override
            public void onResponse(Call<ResponseNotifications> call, Response<ResponseNotifications> response) {

                List<ResponseAfficheNotification> list = response.body().getNotifications();
                NotificationAdapter notificationAdapter = new NotificationAdapter(getActivity(), list);
                recyclerView.setAdapter(notificationAdapter);
                seenNotif(user_id);
            }

            @Override
            public void onFailure(Call<ResponseNotifications> call, Throwable t) {

            }
        });
    }

    private void seenNotif(int user_id) {


        InterfaceAPI api = RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class);
        Call<ResponseSeenNotif> call = api.SeenNotif(user_id);

        call.enqueue(new Callback<ResponseSeenNotif>() {
            @Override
            public void onResponse(Call<ResponseSeenNotif> call, Response<ResponseSeenNotif> response) {
                Toast.makeText(getActivity(), "Seen succes", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseSeenNotif> call, Throwable t) {

            }
        });
    }


}