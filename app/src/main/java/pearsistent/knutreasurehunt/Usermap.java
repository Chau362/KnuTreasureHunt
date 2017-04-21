package pearsistent.knutreasurehunt;


import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

///Edited by Bogyu 4.16

public class Usermap extends Fragment implements OnMapReadyCallback, LocationListener {
    private MapView mapView;
    private LocationManager locationManager;;

    double latitude;
    double longitude;
    long minTime = 10000;
    float minDistance = 0;

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            locationManager = (LocationManager)
                    getContext().getSystemService(Context.LOCATION_SERVICE);
            // Get the last location.
            //location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Log.d("LOCAIONMANGER", "KKKK");
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    public Usermap() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_usermap, container, false);
        mapView = (MapView) rootView.findViewById(R.id.mapView2);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Make map’s setting here.
        startLocationService();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new
                LatLng(latitude, longitude), 16));
        googleMap.addMarker(new MarkerOptions().position(
                new LatLng(latitude, longitude)).title("Current position"));
    }

    @Override
    public void onLocationChanged(final Location location) {
         //  getting location of user
        Log.d("Lat & Long", "" + latitude + "" + longitude);
        // do something with Latlng
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
        // do something
    }

    @Override
    public void onProviderDisabled(String provider) {
        // notify user "GPS or Network provider" not available
    }

    ////////////////////////////////
    private class GPSListener implements LocationListener {

        public void onLocationChanged(Location location) {
            String msg = "Latitude : " + latitude + "\nLongitude:" + longitude;
            Log.i("GPSLocationService", msg);
            //Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

    }
    private void startLocationService() {
        // set listener
        GPSListener gpsListener = new GPSListener();
        // get manager instance
        locationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);
        //Criteria 선언
        Criteria criteria = new Criteria();

        String provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            Log.e("TAG", "GPS is on");
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.d("Latitdue & Longitude", "" + latitude + " & " + longitude);
        }
        else{
            //This is what you need:
            locationManager.requestLocationUpdates(provider, 1000, 0, this);
        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime,
                minDistance,
                gpsListener);
        if (ActivityCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Permission","You should acquire Permission");
            return;         //Check your permission
        }
        Toast.makeText(getContext(), "Location Service started.\nyou can test using DDMS.", Toast.LENGTH_SHORT).show();
    }

}

