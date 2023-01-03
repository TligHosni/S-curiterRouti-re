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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import master.pro.houssine.pfe.Fragments.DiscussionFragment;
import master.pro.houssine.pfe.R;
import master.pro.houssine.pfe.Response.ResponseAfficheContact;
import master.pro.houssine.pfe.Response.ResponseContact;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    Context context;
    List<ResponseAfficheContact> listContact;
    ClickedItem clickedItem;


    public ContactAdapter(Context context, List<ResponseAfficheContact> listContact) {
        this.context = context;
        this.listContact = listContact;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvc, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.sujet.setText(listContact.get(position).getSujet());
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(listContact.get(position).getCreated_at());
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdfnewformat = new SimpleDateFormat("HH:mm dd/MMM/yyyy  ");
            String finalDateString = sdfnewformat.format(convertedDate);
            holder.created.setText(finalDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                DiscussionFragment fragment = new DiscussionFragment();
                int id1 = listContact.get(position).getId();
                int id2 = listContact.get(position).getUser_id();
                bundle.putInt("discussion_id", id1);
                bundle.putInt("user_id", id2);
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
        public void ClickedContact(ResponseContact responseContact);

    }

    @Override
    public int getItemCount() {
        return listContact.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sujet;
        TextView created;
        ImageView arrow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sujet = itemView.findViewById(R.id.sujett);
            created = itemView.findViewById(R.id.created);


            // update = itemView.findViewById(R.id.updateId);
            //delete = itemView.findViewById(R.id.deleteId);
        }
    }

}
