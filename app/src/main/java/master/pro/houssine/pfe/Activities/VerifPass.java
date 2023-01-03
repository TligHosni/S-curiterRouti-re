package master.pro.houssine.pfe.Activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.Nullable;

import master.pro.houssine.pfe.Fragments.ContactAdmin;
import master.pro.houssine.pfe.Model.SharedPrefManger;
import master.pro.houssine.pfe.Model.User;
import master.pro.houssine.pfe.R;
import master.pro.houssine.pfe.Response.ResponseContact;
import master.pro.houssine.pfe.Response.ResponseVerifyPhone;
import master.pro.houssine.pfe.Retrofit.InterfaceAPI;
import master.pro.houssine.pfe.Retrofit.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifPass extends DialogFragment {
    LoginActivity loginActivity;
    private EditText phone;
    private Button verify;
    SharedPrefManger sharedPrefManger;

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_reset_password, container, false);

        phone = view.findViewById(R.id.phone_verif);
        verify = view.findViewById(R.id.verify);

        sharedPrefManger = new SharedPrefManger(getActivity());


        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone_ch = phone.getText().toString().trim();

                if (phone_ch.isEmpty()) {
                     Toast.makeText(getActivity(),"Phone vide",Toast.LENGTH_SHORT).show();
                } else {
                    verifyPhone(phone_ch);
                    User user = SharedPrefManger.getInstance(getActivity()).getUser();
                    int id = user.getId();
                    ContactAdmin changePassDialog = new ContactAdmin();
                    changePassDialog.show(getChildFragmentManager(), "");
                }
            }
        });
        return view;
    }

    public void verifyPhone(String phone){

        InterfaceAPI api = RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class);
        Call<ResponseVerifyPhone> call = api.verifyPhone(phone);

        call.enqueue(new Callback<ResponseVerifyPhone>() {

            @Override
            public void onResponse(Call<ResponseVerifyPhone> call, Response<ResponseVerifyPhone> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSucces()) {

                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();

                        sharedPrefManger.saveUser(response.body().getUser());
                    } else {
                        Toast.makeText(getContext(), "Phone not exist", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseVerifyPhone> call, Throwable t) {
            }
        });


    }
//    public void showAlert(int id){
//        final Dialog dialog = new Dialog(getActivity());
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.fragment_contact_u);
//
//
//        EditText id_txt = (EditText) dialog.findViewById(R.id.id);
//        EditText name_txt = (EditText) dialog.findViewById(R.id.name_u);
//        EditText phone_txt = (EditText) dialog.findViewById(R.id.phone_u);
//        EditText subject_txt = (EditText) dialog.findViewById(R.id.subject);
//        EditText message_txt = (EditText) dialog.findViewById(R.id.message);
//        Button send = (Button) dialog.findViewById(R.id.send);
//
//
//        send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int id = Integer.parseInt(id_txt.getText().toString());
//                String name = name_txt.getText().toString();
//                String phone = phone_txt.getText().toString();
//                String subject = subject_txt.getText().toString();
//                String message = message_txt.getText().toString();
//
////                if (new_password.equals(confirm_new_password)) {
//                    send(id, name,phone,subject,message);
//                id_txt.setText("");
//                message_txt.setText("");
//                phone_txt.setText("");
//                name_txt.setText("");
//                subject_txt.setText("");
////                } else {
////                    Toast.makeText(getActivity(), "Error typing, please reset  ", Toast.LENGTH_SHORT).show();
////                    SharedPrefManger.getInstance(getActivity()).clear();
////                }
//                dialog.dismiss();
//            }
//        });
//
//        dialog.show();
//    }

//    private void reset(String new_password, int id) {
//        InterfaceAPI api = RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class);
//        Call<ResponseResetPassword> call = api.updatePassword(new_password, id);
//
//        call.enqueue(new Callback<ResponseResetPassword>() {
//
//            @Override
//            public void onResponse(Call<ResponseResetPassword> call, Response<ResponseResetPassword> response) {
//                if (response.isSuccessful()) {
//                    if (response.body().getSucces()) {
//
//                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
//                        SharedPrefManger.getInstance(getActivity()).clear();
//                        Intent intent = new Intent(getActivity(), LoginActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
//
//                    } else {
//                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(getActivity(), "Invalid", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseResetPassword> call, Throwable t) {
//            }
//        });
//
//    }

    private void send(int id, String user_name, String user_phone, String subject, String message) {
        InterfaceAPI api = RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class);
        Call<ResponseContact> call = api.contactAdmin(id, user_name, user_phone, subject, message);
        call.enqueue(new Callback<ResponseContact>() {

            @Override
            public void onResponse(Call<ResponseContact> call, Response<ResponseContact> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Your request is send", Toast.LENGTH_LONG).show();

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