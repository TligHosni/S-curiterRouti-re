package master.pro.houssine.pfe.Services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;

import master.pro.houssine.pfe.Model.SharedPrefManger;

public class LocationService extends Service {
    public static final String BROADCAST_ACTION = "HelloWorld";
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    public static AllowAllHostnameVerifier locationArrayList;
    public LocationManager locationManager;
    public LocationManager locationManager1;
    public MyLocationListener listener;
    public Location previousBestLocation = null;
    GeoFire geoFire;
    Location oldLocation = null;
    Boolean returned = false;
    SharedPrefManger sharedPrefManger;
    Intent intent;
    int counter = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
        sharedPrefManger = new SharedPrefManger(getApplicationContext());
        //startForeground(1,intent);

    }

    @Override
    public void onStart(Intent intent, int startId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user_locations");
        geoFire = new GeoFire(ref);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager1 = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 32000, 0, (LocationListener) listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 32000, 0, listener);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    public boolean isUsable(Location location) {
        return location != null && location.hasAccuracy() && location.getAccuracy() < 800 && location.getSpeed() > 0;
    }

    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }

        return provider1.equals(provider2);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        Log.v("STOP_SERVICE", "DONE");
        if (listener != null) {
            locationManager.removeUpdates(listener);
        }


    }


    public class MyLocationListener implements LocationListener {
        float distanceInMeters = 10;

        public void onLocationChanged(final Location loc) {

            sharedPrefManger.save(getApplicationContext(), "latitude", loc.getLatitude() + "");
            sharedPrefManger.save(getApplicationContext(), "longitude", loc.getLongitude() + "");

            Log.d("geolocation!", loc.getLatitude() + "" + loc.getLongitude());

            if (isBetterLocation(loc, previousBestLocation)) {
//                Toast.makeText(getApplicationContext(), "geolocation! : " + loc.getLatitude() + " || " + loc.getLongitude(), Toast.LENGTH_SHORT).show();
                // if (SharedPrefManager.getInstance(getApplicationContext()).getUser().getRole().equals("server") || SharedPrefManager.getInstance(getApplicationContext()).getUser().getRole().equals("serveur")) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user_locations");
                GeoFire geoFire = new GeoFire(ref);
                geoFire.setLocation(String.valueOf(sharedPrefManger.getUser().getId()),
                        new GeoLocation(loc.getLatitude(), loc.getLongitude()), new GeoFire.CompletionListener() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void onComplete(String key, DatabaseError error) {
                                if (error != null) {
                                    Log.d("geolocation!", loc.getLatitude() + "" + loc.getLongitude());
                                } else {
                                    Log.d("location sended successfully!", "success");
                                }

                            }
                        });

                loc.getLatitude();
                loc.getLongitude();
                intent.putExtra("Latitude", loc.getLatitude());
                intent.putExtra("Longitude", loc.getLongitude());
                intent.putExtra("Provider", loc.getProvider());
                sendBroadcast(intent);
                //  }
                //  locatnewww(loc);


            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {


        }

        public void onProviderDisabled(String provider) {
            Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent1);
        }


        public void onProviderEnabled(String provider) {
            //Toast.makeText( getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }
    }


}