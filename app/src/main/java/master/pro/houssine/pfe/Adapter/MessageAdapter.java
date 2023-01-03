package master.pro.houssine.pfe.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import master.pro.houssine.pfe.Model.SharedPrefManger;
import master.pro.houssine.pfe.R;
import master.pro.houssine.pfe.Response.ResponseAfficheMessage;


public class MessageAdapter extends ArrayAdapter<ResponseAfficheMessage> {

    Context context;
    List<ResponseAfficheMessage> list = new ArrayList<ResponseAfficheMessage>();

    ViewGroup parent;

    TextView chatText, datetxt, timetxt, datecnv;
    String time;

    public MessageAdapter(Context context, int textViewResourceId) {
        super(context.getApplicationContext(), textViewResourceId);
        this.context = context;

    }
//    public MessageAdapter(Context context, List<ResponseAfficheMessage> list) {
//        this.context = context;
//        this.list = list;
//    }


    @Override
    public void add(ResponseAfficheMessage object) {
        super.add(object);
        list.add(object);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ResponseAfficheMessage chatMessageObj = list.get(position);
        View row = convertView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        String id = String.valueOf(SharedPrefManger.getInstance(context).getUser().getId());
        if (id.equals(String.valueOf(chatMessageObj.getUser_id()))) {
            row = inflater.inflate(R.layout.right_item_message, parent, false);
            chatText = row.findViewById(R.id.msgr);
//            datetxt = row.findViewById(R.id.item_date);
            timetxt = row.findViewById(R.id.item_time);
            row.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        } else {
            row = inflater.inflate(R.layout.left_item_message, parent, false);
            chatText = row.findViewById(R.id.msgr);
//            datetxt = row.findViewById(R.id.item_date);
            timetxt = row.findViewById(R.id.item_time);
            row.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//            datetxt.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        }

        chatText.setText(chatMessageObj.getMessage());

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(chatMessageObj.getCreated_at());
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdfnewformat = new SimpleDateFormat("dd/MMM/yyyy  HH:mm");
            String finalDateString = sdfnewformat.format(convertedDate);
            timetxt.setText(finalDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return row;
    }


    public int getCount() {
        return this.list.size();
    }


    public ResponseAfficheMessage getItem(int index) {
        return this.list.get(index);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView sujet;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sujet = itemView.findViewById(R.id.sujet);



        }

    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }


}
