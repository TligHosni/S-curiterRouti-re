package master.pro.houssine.pfe.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import master.pro.houssine.pfe.Adapter.MessageAdapter;
import master.pro.houssine.pfe.Model.SharedPrefManger;
import master.pro.houssine.pfe.Model.User;
import master.pro.houssine.pfe.R;
import master.pro.houssine.pfe.Response.ResponseAddMessage;
import master.pro.houssine.pfe.Response.ResponseAfficheMessage;
import master.pro.houssine.pfe.Response.ResponseMessage;
import master.pro.houssine.pfe.Retrofit.InterfaceAPI;
import master.pro.houssine.pfe.Retrofit.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiscussionFragment extends Fragment {

    EditText message_txt;
    FloatingActionButton send_message,delete_msg;
    Button  back;
    LinearLayout addmessage;
    ListView recyclerView;
    LinearLayoutManager layoutManager;
    MessageAdapter adapter;
    private final static int INTERVAL = 1000 * 20; //20 second
    Handler mHandler = new Handler();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_discussion, container, false);

        message_txt = view.findViewById(R.id.message);
        send_message = view.findViewById(R.id.send_message);
        delete_msg = view.findViewById(R.id.delete_message);
        recyclerView = view.findViewById(R.id.recycler_view);
        addmessage = view.findViewById(R.id.layout_add_message);
        back = view.findViewById(R.id.back);
        layoutManager = new LinearLayoutManager(getActivity());
        User user = SharedPrefManger.getInstance(getActivity()).getUser();
        affiche();


        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int user_id = user.getId();
                String message = message_txt.getText().toString();
                send(message);
                affiche();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactFragment fragment = new ContactFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, fragment);
                fragmentTransaction.commit();
            }
        });

//        List<ResponseAfficheMessage> list = new ArrayList<>();
//        recyclerView.setLongClickable(true);
//        MessageAdapter adapter = new MessageAdapter(getActivity(), list);
//        recyclerView.setAdapter(adapter);

        recyclerView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {

                delete_msg.setVisibility(View.VISIBLE);
                addmessage.setVisibility(View.INVISIBLE);

             //   list.remove(arg1);
                adapter.notifyDataSetChanged();

                return true;
            }
        });

        delete_msg.setVisibility(View.INVISIBLE);
        addmessage.setVisibility(View.VISIBLE);

        return view;
    }

    Runnable mHandlerTask = new Runnable()
    {
        @Override
        public void run() {
            affiche();
            mHandler.postDelayed(mHandlerTask, INTERVAL);
        }
    };

    void startRepeatingTask()
    {
        mHandlerTask.run();
    }

    void stopRepeatingTask()
    {
        mHandler.removeCallbacks(mHandlerTask);
    }


    private void send(String message) {

        Bundle bundle = this.getArguments();
        int discussion_id = bundle.getInt("discussion_id");
        int user_id = bundle.getInt("user_id");

        InterfaceAPI api = RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class);

        Call<ResponseAddMessage> call = api.addMessage(discussion_id, message, user_id);
        call.enqueue(new Callback<ResponseAddMessage>() {
            @Override
            public void onResponse(Call<ResponseAddMessage> call, Response<ResponseAddMessage> response) {
                if (response.isSuccessful()) {

                    Toast.makeText(getActivity(), "Your message is send", Toast.LENGTH_LONG).show();
                    message_txt.setText("");
                } else {
                    Toast.makeText(getActivity(), "Error! Please tryy again.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseAddMessage> call, Throwable t) {
                Toast.makeText(getContext(), "Failure add message ", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void affiche() {
        Bundle bundle = this.getArguments();
        int discussion_id = bundle.getInt("discussion_id");

            InterfaceAPI api = RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class);

            Call<ResponseMessage> call = api.listMessages(discussion_id);
            call.enqueue(new Callback<ResponseMessage>() {

                @Override
                public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {

                    if (response.isSuccessful()) {


                        List<ResponseAfficheMessage> messageList = response.body().getResult();
                        MessageAdapter messageAdapter = new MessageAdapter(getActivity(), R.layout.right_item_message);
                        for (ResponseAfficheMessage message : messageList) {
                            messageAdapter.add(message);
                        }
                        recyclerView.setAdapter(messageAdapter);

                    } else {
                        Toast.makeText(getContext(), "Error! Please try again.", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseMessage> call, Throwable t) {
                    Toast.makeText(getContext(), "nooooo!!!", Toast.LENGTH_SHORT).show();
                }
            });

    }




}