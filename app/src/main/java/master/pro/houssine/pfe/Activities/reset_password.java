package master.pro.houssine.pfe.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.jetbrains.annotations.Nullable;

import master.pro.houssine.pfe.R;

public class reset_password extends AppCompatActivity {

    EditText new_password_txt;
    Button reset;
    String new_password;

    @Nullable
    protected void onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.reset_password);
        View view = inflater.inflate(R.layout.activity_reset_password, container, false);

        new_password_txt = findViewById(R.id.new_password);
        reset = findViewById(R.id.reset);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new_password = new_password_txt.getText().toString();
                reset(new_password);
            }
        });
    }

    private void reset(String new_password) {


    }

}