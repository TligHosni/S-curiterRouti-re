package master.pro.houssine.pfe.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import master.pro.houssine.pfe.Adapter.ContactAdapter;
import master.pro.houssine.pfe.Model.SharedPrefManger;
import master.pro.houssine.pfe.Model.User;
import master.pro.houssine.pfe.R;
import master.pro.houssine.pfe.Response.ResponseAfficheContact;
import master.pro.houssine.pfe.Response.ResponseContact;
import master.pro.houssine.pfe.Retrofit.InterfaceAPI;
import master.pro.houssine.pfe.Retrofit.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#} factory method to
 * create an instance of this fragment.
 */
public class ContactFragment extends Fragment {


    public static List<ResponseContact> list;
    EditText sujet_txt;
    EditText created_txt;
    Button addcontact;
    int user_id;
    RecyclerView recyclerView;
    ContactAdapter contactAdapter;
    // private static List<Contact> list = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.contact_fragment, container, false);

        sujet_txt = view.findViewById(R.id.sujet);
        created_txt = view.findViewById(R.id.created);
        recyclerView = view.findViewById(R.id.recycleview);
        addcontact = view.findViewById(R.id.addcontact);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL));

        affiche();

        addcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactU changePassDialog = new contactU();
                changePassDialog.show(getChildFragmentManager(), "");
            }
        });

        return view;
    }

    private void affiche() {
        User user = SharedPrefManger.getInstance(getActivity()).getUser();

        int user_id = user.getId();

        InterfaceAPI api = RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class);
        Call<ResponseContact> call = api.listDiscussions(user_id);

        call.enqueue(new Callback<ResponseContact>() {

            @Override
            public void onResponse(Call<ResponseContact> call, Response<ResponseContact> response) {
                if (response.isSuccessful()) {

                    List<ResponseAfficheContact> list = response.body().getContacts();
                    ContactAdapter contactAdapter = new ContactAdapter(getActivity(), list);
                    recyclerView.setAdapter(contactAdapter);
                }
            }

            @Override
            public void onFailure(Call<ResponseContact> call, Throwable t) {

            }

        });
    }



}
