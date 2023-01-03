package master.pro.houssine.pfe.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import master.pro.houssine.pfe.R;

public class a_propos extends AppCompatActivity {
    FloatingActionButton acceuil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apropos);
        acceuil = findViewById(R.id.acceuil);
        acceuil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(a_propos.this, AcceuilActivity.class);
                startActivity(intent);
            }
        });
    }
}