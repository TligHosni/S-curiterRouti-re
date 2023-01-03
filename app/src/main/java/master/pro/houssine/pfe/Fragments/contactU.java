package master.pro.houssine.pfe.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import master.pro.houssine.pfe.Model.SharedPrefManger;
import master.pro.houssine.pfe.Model.User;
import master.pro.houssine.pfe.R;
import master.pro.houssine.pfe.Response.ResponseContact;
import master.pro.houssine.pfe.Retrofit.InterfaceAPI;
import master.pro.houssine.pfe.Retrofit.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class contactU extends DialogFragment {

    EditText subject_txt,message_txt;
    Button send;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contact_u, container, false);

        subject_txt = view.findViewById(R.id.subject);
        message_txt = view.findViewById(R.id.message);

        send = view.findViewById(R.id.send_contact);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = SharedPrefManger.getInstance(getActivity()).getUser();
                if (subject_txt.getText().length() == 0 || message_txt.getText().length() == 0) {
                    Toast.makeText(getActivity(), "Il faut remplir tous les champs", Toast.LENGTH_SHORT).show();
                } else {
                    int user_id = user.getId();
                    String user_name = user.getName();
                    String user_phone = user.getPhone();
                    String subject = subject_txt.getText().toString();
                    String message = message_txt.getText().toString();
                    send(user_id, user_name, user_phone, subject, message);
                    ContactFragment fragment = new ContactFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, fragment);
                    fragmentTransaction.commit();
                }
            }
        });


        return view;
    }

    private void send(int user_id, String user_name, String user_phone, String subject, String message) {
        InterfaceAPI api = RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class);
        Call<ResponseContact> call = api.contactAdmin(user_id, user_name, user_phone, subject, message);
        call.enqueue(new Callback<ResponseContact>() {

            @Override
            public void onResponse(Call<ResponseContact> call, Response<ResponseContact> response) {
                if (response.isSuccessful()) {
                        Toast.makeText(getActivity(), "Your request is send", Toast.LENGTH_LONG).show();
                        subject_txt.setText("");
                        message_txt.setText("");
                } else {
                    Toast.makeText(getActivity(), "Error! Please try again.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseContact> call, Throwable t) {

            }
        });
    }
}