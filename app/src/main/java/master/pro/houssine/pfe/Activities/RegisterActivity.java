package master.pro.houssine.pfe.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import master.pro.houssine.pfe.R;
import master.pro.houssine.pfe.Response.ResponseRegister;
import master.pro.houssine.pfe.Retrofit.InterfaceAPI;
import master.pro.houssine.pfe.Retrofit.RetrofitClientInstance;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText name_txt;
    EditText phone_txt;
    EditText password_txt;
    EditText sexe_txt;
    Button register;
    private Object Menu;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name_txt = findViewById(R.id.name);
        phone_txt = findViewById(R.id.phone);
        password_txt = findViewById(R.id.password);
        sexe_txt = findViewById(R.id.sexe);
        register = findViewById(R.id.register);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference().child("Users").child("Name");

//        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//        View popupView = inflater.inflate(R.layout.popup, null);
//
//         //create the popup window
//        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
//        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
//        boolean focusable = true; // lets taps outside the popup also dismiss it
//        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable); register.setOnClickListener(new  View.OnClickListener() {

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = name_txt.getText().toString();
                String phone = phone_txt.getText().toString();
                String password = password_txt.getText().toString();
                String sexe = sexe_txt.getText().toString();

                myRef.setValue(name);
                myRef.setValue(phone);
                myRef.setValue(sexe);

                register(name, phone, password, sexe);
                saveData();
            }

        });
    }

    private void saveData() {
        String name = name_txt.getText().toString().trim();
        String phone = phone_txt.getText().toString().trim();
        String password = password_txt.getText().toString().trim();
        String sexe = sexe_txt.getText().toString().trim();

//        User model = new User();
//        model.setName(name);
//        model.setPassword(password);
//        model.setPhone(phone);
//        model.setSexe(sexe);
//        DatabaseClass.getDatabase(getApplicationContext()).getDao().insertAllData(model);

        name_txt.setText("");
        password_txt.setText("");
        phone_txt.setText("");
        sexe_txt.setText("");
        Toast.makeText(this, "Data Successfully Saved", Toast.LENGTH_SHORT).show();

    }

    private void register(String name, String phone, String password, String sexe) {
        InterfaceAPI api = RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class);
        Call<ResponseRegister> call = api.add(name, phone, password, sexe);
        call.enqueue(new Callback<ResponseRegister>() {

            @Override
            public void onResponse(Call<ResponseRegister> call, Response<ResponseRegister> response) {

                if (response.isSuccessful()) {
//                    if (response.body().getSucces()) {

                        mAuth.createUserWithEmailAndPassword(phone,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(getApplicationContext(), "Error!!" +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


//                    } else {
//                        Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                    }

                } else {
                    Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseRegister> call, Throwable t) {
            }

            });
        }
    }