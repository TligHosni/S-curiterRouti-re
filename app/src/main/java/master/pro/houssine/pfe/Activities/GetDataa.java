package master.pro.houssine.pfe.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import master.pro.houssine.pfe.Fragments.ContactFragment;
import master.pro.houssine.pfe.R;
import master.pro.houssine.pfe.Response.ResponseContact;

public class GetDataa extends AppCompatActivity {

    RecyclerView recyclerview;

    private List<ResponseContact> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_fragment);
        recyclerview = findViewById(R.id.recycleview);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();

    }

    private void getData() {
        list = new ArrayList<>();
        list = ContactFragment.list;
//        list = DatabaseClass.getDatabase(getApplicationContext()).getDao().getAllData();
//        recyclerview.setAdapter(new ContactAdapter(getApplicationContext(), list));
    }
}