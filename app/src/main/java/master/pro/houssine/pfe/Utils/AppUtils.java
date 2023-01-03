package master.pro.houssine.pfe.Utils;


import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import master.pro.houssine.pfe.Activities.AppFragment;
import master.pro.houssine.pfe.Model.SharedPrefManger;
import master.pro.houssine.pfe.R;

public class AppUtils {

    public static final String[] MONTHS = {"جانفي", "فيفري", "مارس", "أفريل", "ماي", "جوان", "جويلية", "أوت", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر"};
    public static final String[] DAYS = {"الأحد", "الاثنين", "الثلاثاء", "الإربعاء", "الخميس", "الجمعة", "السبت"};
    final static String DATE_FORMAT = "yyyy/MM/dd";
    Context context;
    // Variable of puch notification
    public static final String CHANNEL_ID = "simplified coding";
    public static final String CHANNEL_NAME = "Simplified coding";
    public static final String CHANNEL_DESC = "simplified coding Notifications";

    public static boolean isDigitalOnly(CharSequence original) {
        if (original != null) {

            return TextUtils.isDigitsOnly(original);
        }
        return false;
    }

    public static String replaceArabicNumbers(CharSequence original) {
        if (original != null) {
            return original.toString().replaceAll("٠", "0")
                    .replaceAll("١", "1")
                    .replaceAll("٢", "2")
                    .replaceAll("٣", "3")
                    .replaceAll("٤", "4")
                    .replaceAll("٥", "5")
                    .replaceAll("٦", "6")
                    .replaceAll("٧", "7")
                    .replaceAll("٨", "8")
                    .replaceAll("٩", "9");
        }

        return null;
    }

    public static String getMonth(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        @SuppressLint("SimpleDateFormat") String monthName = replaceArabicNumbers(new SimpleDateFormat("dd MMMM yyyy", new Locale("ar")).format(cal.getTime()));
        return monthName;
    }

    public static String getMonthName(int nb) {
        return MONTHS[nb];
    }

    public static boolean isAfterToday(int year, int month, int day) {
        Calendar today = Calendar.getInstance();
        Calendar myDate = Calendar.getInstance();

        myDate.set(year, month, day);

        if (myDate.before(today)) {
            return false;
        }
        return true;
    }

    public static String getDayName(String date) {

        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        df.setLenient(false);
        Date mdate = null;
        try {
            mdate = df.parse(date);
        } catch (ParseException e) {
            return "";
        }
        Calendar c = Calendar.getInstance();
        c.setTime(mdate);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        return DAYS[dayOfWeek - 1];

    }

    public static long getDateInMilliSeconds(String givenDateString, String format) {
        String DATE_TIME_FORMAT = format;
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.US);
        long timeInMilliseconds = 1;
        try {
            Date mDate = sdf.parse(givenDateString);
            timeInMilliseconds = mDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeInMilliseconds;
    }

    public static boolean isDateValid(String date) {
        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }

    }

    public static boolean isAfterNow(String date) {
        boolean isvalid = false;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyyTHH:mm:00");
        try {
            Date currentDateandTime = sdf.parse(sdf.format(new Date()));
            Date selectedDateandTime = sdf.parse(sdf.format(date));

            if (selectedDateandTime != null) {
                isvalid = selectedDateandTime.after(currentDateandTime);
            }

        } catch (ParseException e) {
            e.printStackTrace();
            isvalid = false;
        }


        return isvalid;


    }

    public static String getToken(Context context) {
//       String token = MessagingService.onNewToken();
//        return token;
        final String[] token = {""};
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        SharedPrefManger sharedPrefManger = new SharedPrefManger(context);
                        String token = sharedPrefManger.getStringSaved(context, "token");
                        if (token.equals("")) {
                            sharedPrefManger.save(context, "token", task.getResult());
                        }
                        Log.d("Token : ", task.getResult());
                        // Get new FCM registration token
                        token = task.getResult();

                    }
                });
        return  token[0];
    }

    public static boolean isLocationEnabled(Activity activity) {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static File getFile(Context context, Uri uri) {
        if (uri != null) {
            String path = getPath(context, uri);
            if (path != null && isLocal(path)) {
                return new File(path);
            }
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // LocalStorageProvider

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
    public static boolean isLocal(String url) {
        if (url != null && !url.startsWith("http://") && !url.startsWith("https://")) {
            return true;
        }
        return false;
    }
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     * @author paulburke
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     * @author paulburke
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                    DatabaseUtils.dumpCursor(cursor);

                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }



    // puch notification code.
    public static void displayNotification(Activity activity) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(activity, CHANNEL_ID)
                        .setSmallIcon(R.drawable.t)
                        .setContentTitle("Message")
                        .setContentText("You have new message")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

//        AppCompatActivity appCompatActivity = (AppCompatActivity) activity ;
//        ContactFragment fragment = new ContactFragment();
//        appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
//

        Intent intent = new Intent(activity, AppFragment.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);

        NotificationManagerCompat mNotificationMgr = NotificationManagerCompat.from(activity);
        mNotificationMgr.notify(1, mBuilder.build());
    }
}
