package master.pro.houssine.pfe.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import master.pro.houssine.pfe.Fragments.DiscussionFragment;
import master.pro.houssine.pfe.Fragments.HomeFragment;
import master.pro.houssine.pfe.R;
import master.pro.houssine.pfe.Response.ResponseAfficheNotification;
import master.pro.houssine.pfe.Response.ResponseContact;

public class NotificationAdapter  extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{
    Context context;
    List<ResponseAfficheNotification> listNotif;
    ContactAdapter.ClickedItem clickedItem;


    public NotificationAdapter(Context context, List<ResponseAfficheNotification> listNotif) {
        this.context = context;
        this.listNotif = listNotif;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvn, parent, false);
        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.title.setText(listNotif.get(position).getTitle_notification());
        holder.body.setText(listNotif.get(position).getContent_notification());

        if(listNotif.get(position).getValue() == 0) holder.ll_notif.setBackgroundResource(R.drawable.edit_round);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(listNotif.get(position).getCreated());
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
                if(listNotif.get(position).getTitle_notification().equals("Nouveau Message"))
                {
                    Bundle bundle = new Bundle();
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    DiscussionFragment fragment = new DiscussionFragment();
                    int id1 = listNotif.get(position).getElement();
                    int id2 = listNotif.get(position).getUser_id();
                    bundle.putInt("discussion_id", id1);
                    bundle.putInt("user_id", id2);
                    fragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
                }else
                {
                    Bundle bundle = new Bundle();
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    HomeFragment fragment = new HomeFragment();
                    fragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
                }

            }
        });


    }

    public interface ClickedItem {
        public void ClickedContact(ResponseContact responseContact);

    }

    @Override
    public int getItemCount() {
        return listNotif.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView body;
        TextView created;
        ImageView arrow;
        LinearLayout ll_notif;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            body = itemView.findViewById(R.id.body);
            created = itemView.findViewById(R.id.created);
            ll_notif = itemView.findViewById(R.id.ll_notif);
        }
    }
}
