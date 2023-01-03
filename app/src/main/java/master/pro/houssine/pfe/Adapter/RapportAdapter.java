package master.pro.houssine.pfe.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import master.pro.houssine.pfe.Fragments.RapportFragment;
import master.pro.houssine.pfe.R;
import master.pro.houssine.pfe.Response.ResponseAfficheRapport;
import master.pro.houssine.pfe.Response.ResponseRapports;
import master.pro.houssine.pfe.Retrofit.RetrofitClientInstance;

public class RapportAdapter extends RecyclerView.Adapter<RapportAdapter.ViewHolder> {

    Context context;
    List<ResponseAfficheRapport> listRapport;
    RapportAdapter.ClickedItem clickedItem;

    public RapportAdapter(Context context, List<ResponseAfficheRapport> listRapport) {
        this.context = context;
        this.listRapport = listRapport;
    }

    @NonNull
    @Override
    public RapportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvr, parent, false);
        return new RapportAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RapportAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        //Set Image
        String imageurl = listRapport.get(position).getImage();
        String url = RetrofitClientInstance.API_BASE_URL_IMAGE + imageurl;
        Picasso.get().load(url).error(R.mipmap.ic_launcher).into(holder.imageRapp);
        //Set date

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(listRapport.get(position).getDate_heure());
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdfnewformat = new SimpleDateFormat("dd/MMM/yyyy  HH:mm");
            String finalDateString = sdfnewformat.format(convertedDate);
            holder.created.setText(finalDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.typeUsager.setText(listRapport.get(position).getTypeUsager());
        holder.vehicule.setText(listRapport.get(position).getVehicule());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                RapportFragment fragment = new RapportFragment();
                int id = listRapport.get(position).getId();
                int user_id = listRapport.get(position).getUser_id();
                String date = listRapport.get(position).getDate_heure();
                String latitude = listRapport.get(position).getLatitude();
                String longitude = listRapport.get(position).getLongitude();
                String typeUsager = listRapport.get(position).getTypeUsager();
                int ageUsager = (listRapport.get(position).getAgeUsager());
                String sexeUsager = listRapport.get(position).getSexeUsager();
                String detailRoute = listRapport.get(position).getDetailRoute();
                String detailEnv = listRapport.get(position).getDetailEnv();
                String descriptionAccident = listRapport.get(position).getDescriptionAccident();
                String vehicule = listRapport.get(position).getVehicule();
                String vitesse = listRapport.get(position).getVitesse();
                String alcole = listRapport.get(position).getAlcole();
                String image = listRapport.get(position).getImage();


                bundle.putInt("id", id);
                bundle.putInt("user_id", user_id);
                bundle.putInt("ageUsager", ageUsager);
                bundle.putString("latitude", latitude);
                bundle.putString("longitude", longitude);
                bundle.putString("date", date);
                bundle.putString("typeUsager", typeUsager);
                bundle.putString("sexeUsager", sexeUsager);
                bundle.putString("detailRoute", detailRoute);
                bundle.putString("detailEnv", detailEnv);
                bundle.putString("descriptionAccident", descriptionAccident);
                bundle.putString("vehicule", vehicule);
                bundle.putString("vitesse", vitesse);
                bundle.putString("alcole", alcole);
                bundle.putString("image", image);


                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
            }
        });


    }

//    public void setData(List<ResponseContact> responseContacts) {
//        this.listContact = listContact;
//        notifyDataSetChanged();
//    }

    public interface ClickedItem {
        public void ClickedContact(ResponseRapports responseRapports);

    }

    @Override
    public int getItemCount() {
        return listRapport.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageRapp;
        TextView created;
        TextView typeUsager;
        TextView vehicule;
        ImageView arrow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageRapp = itemView.findViewById(R.id.imageRapp);
            created = itemView.findViewById(R.id.created);
            typeUsager = itemView.findViewById(R.id.typeUsager);
            vehicule = itemView.findViewById(R.id.vehicule);

        }
    }
}
