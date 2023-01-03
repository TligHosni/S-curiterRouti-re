package master.pro.houssine.pfe.Activities;

import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import master.pro.houssine.pfe.R;

public class Code_verif extends AppCompatActivity {

    EditText code_validation;
    private SmsVerifyCatcher smsVerifyCatcher;

//    private static final int REQ_USER_CONSENT = 200;
//    SmsBroadcastReceiver smsBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_verif);

        code_validation = findViewById(R.id.code_validation);

//        startSmsUserConsent();

        //init SmsVerifyCatcher
        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                String code = parseCode(message);//Parse verification code
                code_validation.setText(code);//set code in edit text
                //then you can send verification code to server
            }
        });

        //set phone number filter if needed
//        smsVerifyCatcher.setPhoneNumberFilter("ICQ");
        //smsVerifyCatcher.setFilter("regexp");

//        //button for sending verification code manual
//        btnVerify.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //send verification code to server
//            }
//        });
    }





    /**
     * Parse verification code
     *
     * @param message sms message
     * @return only four numbers from massage string
     */
    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{6}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }

    @Override
    protected void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
    }

    /**
     * need for Android 6 real time permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
//    private void startSmsUserConsent() {
//        SmsRetrieverClient client = SmsRetriever.getClient(this);
//        //We can add sender phone number or leave it blank
//        // I'm adding null here
//        client.startSmsUserConsent(null).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Toast.makeText(getApplicationContext(), "On Success", Toast.LENGTH_LONG).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getApplicationContext(), "On OnFailure", Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQ_USER_CONSENT) {
//            if ((resultCode == RESULT_OK) && (data != null)) {
//                //That gives all message to us.
//                // We need to get the code from inside with regex
//                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
//                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
////                textViewMessage.setText(
////                        String.format("%s - %s", getString(R.string.received_message), message));
//
//                getOtpFromMessage(message);
//            }
//        }
//    }
//
//    private void getOtpFromMessage(String message) {
//        // This will match any 6 digit number in the message
//        Pattern pattern = Pattern.compile("(|^)\\d{6}");
//        Matcher matcher = pattern.matcher(message);
//        if (matcher.find()) {
//            code_validation.setText(matcher.group(0));
//            Toast.makeText(this, "code: "+matcher.group(0).toString(), Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void registerBroadcastReceiver() {
//        smsBroadcastReceiver = new SmsBroadcastReceiver();
//        smsBroadcastReceiver.smsBroadcastReceiverListener =
//                new SmsBroadcastReceiver.SmsBroadcastReceiverListener() {
//                    @Override
//                    public void onSuccess(Intent intent) {
//                        startActivityForResult(intent, REQ_USER_CONSENT);
//                    }
//
//                    @Override
//                    public void onFailure() {
//
//                    }
//                };
//        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
//        registerReceiver(smsBroadcastReceiver, intentFilter);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        registerBroadcastReceiver();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        unregisterReceiver(smsBroadcastReceiver);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        registerBroadcastReceiver();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        unregisterReceiver(smsBroadcastReceiver);
//    }
}