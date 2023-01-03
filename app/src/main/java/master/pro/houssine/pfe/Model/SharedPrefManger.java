package master.pro.houssine.pfe.Model;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManger {

    private static final String SHARED_PREF_NAME = "my_shared_preff";

    private static SharedPrefManger mInstance;
    private Context mCtx;

    public SharedPrefManger(Context mCtx) {
        this.mCtx = mCtx;
    }

    public void save(Context context, String key, String value) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, String.valueOf(value));
        editor.apply();
    }

    public void saveString(Context context, String key, double value) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, String.valueOf(value));
        editor.apply();
    }

    public String getStringSaved(Context context, String key) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public static synchronized SharedPrefManger getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new SharedPrefManger(mCtx);
        }
        return mInstance;
    }

    public void saveUser(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("id", user.getId());
        editor.putString("name", user.getName());
        editor.putString("email", user.getEmail());
        editor.putString("password",user.getPassword());
        editor.putString("role",user.getRole());
        editor.putString("sexe",user.getSexe());
        editor.putString("phone", user.getPhone());
        editor.putString("image", String.valueOf(user.getImage()));
        editor.putString("latitude", user.getLatitude());
        editor.putString("longitude", user.getLongitude());
        editor.putString("naissance", user.getNaissance());
        editor.putString("lastlogout", user.getLastlogout());
        editor.putString("lastlogin", user.getLastlogin());
        editor.putString("deviceid", user.getDeviceid());
        editor.putString("created", user.getCreated());
        editor.putString("update", user.getUpdated());
        editor.putString("etat", user.getEtat());
        editor.putString("rendez_vous", user.getRendez_vous());

        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("id", -1) != -1;
    }

    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        User user = new User(
                sharedPreferences.getInt("id", -1),
                sharedPreferences.getString("name", null),
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("password", null),
                sharedPreferences.getString("role", null),
                sharedPreferences.getString("sexe", null),
                sharedPreferences.getString("phone", null),
                sharedPreferences.getString("image", null),
                sharedPreferences.getString("latitude", null),
                sharedPreferences.getString("longitude", null),
                sharedPreferences.getString("naissance", null),
                sharedPreferences.getString("lastlogout", null),
                sharedPreferences.getString("lastlogint", null),
                sharedPreferences.getString("deviceid", null),
                sharedPreferences.getString("created", null),
                sharedPreferences.getString("updated", null),
                sharedPreferences.getString("etat", null),
                sharedPreferences.getString("rendez_vous", null)
        );

        return user;
    }

    public void clear() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}