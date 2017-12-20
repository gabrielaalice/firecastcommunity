package com.example.gabriela.firecastcommunity.fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gabriela.firecastcommunity.MainActivity;
import com.example.gabriela.firecastcommunity.R;
import com.example.gabriela.firecastcommunity.data.FirecastDB;
import com.example.gabriela.firecastcommunity.domain.Occurrence;
import com.example.gabriela.firecastcommunity.domain.User;
import com.example.gabriela.firecastcommunity.utility.Constant;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.Manifest;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class MapsFragment extends Fragment  implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private MapView mapView;
    private static GoogleMap gMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private LocationRequest mLocationRequest;

    //private static final LatLng positionGabriela = new LatLng(-27.6000907,-48.526813);

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final double DEFAULT_RADIUS_METERS = 1000000;
    private static final int MAX_HUE_DEGREES = 360;
    static final List<LatLng> arrayMyLocation = new ArrayList<>();

    public MapsFragment(){
        arrayMyLocation.add(new LatLng(-27.6000907,-48.526813));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_maps_fragment, container, false);
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        return view;

    }
    
    private static Bitmap GetColorMarkerOccurrence(Occurrence occurrence) {
            
            Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.other_pin);
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);

            switch (occurrence.type.id){
                case 1:
                    icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.fire_pin);
                    resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);
                    return resizedBitmap;
                case 2:
                    icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.search_rescue_pin);
                    resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);
                    return resizedBitmap;
                case 3:
                    icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.dangerous_product_pin);
                    resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);
                    return resizedBitmap;
                case 4:
                    icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.fire_pin);
                    resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);
                    return resizedBitmap;
                case 5:
                    icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.paramedics_pin);
                    resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);
                    return resizedBitmap;
                case 6:
                    icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.other_pin);
                    resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);
                    return resizedBitmap;
                case 7:
                    icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.other_pin);
                    resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);
                    return resizedBitmap;
                case 8:
                    icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.car_accident_pin);
                    resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);
                    return resizedBitmap;
                case 9:
                    icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.other_pin);
                    resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);
                    return resizedBitmap;
                case 10:
                    icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.tree_cutting_pin);
                    resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);
                    return resizedBitmap;
                case 11:
                    icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.insect_control_pin);
                    resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);
                    return resizedBitmap;
            }
            return resizedBitmap;

        }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-27.6100646,-48.7646342),10));

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                gMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            gMap.setMyLocationEnabled(true);
        }
    }

    public static void UpdateMapMarkersRadius() {
        LatLng actualPosition = arrayMyLocation.get(0);
        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.user_pin);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);


        if(gMap!=null) {
            gMap.clear();

            gMap.addMarker(new MarkerOptions().position(actualPosition)
                    .title("Minha posição atual")
                    .icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap)));

            List<Occurrence> occurrences = OccurenceFragment.getListOccurrence();

            for (Occurrence occ : occurrences) {
                if (occ.latitude != null || occ.longitude != null) {
                    LatLng positionOcc = new LatLng(occ.latitude, occ.longitude);

                    gMap.addMarker(new MarkerOptions().position(positionOcc)
                            .title(occ.city.name + " / " + occ.description)
                            .icon(BitmapDescriptorFactory.fromBitmap(GetColorMarkerOccurrence(occ))));
                }
            }

            User user = MainActivity.getUser();

            if(user.getId_city_occurrence()!= Constant.ALL_CITIES_ID) {
                int radius = user.getRadiusKilometers();

                Circle circle = gMap.addCircle(new CircleOptions()
                        .center(actualPosition)
                        .radius(radius * 1000)
                        .strokeColor(Color.RED)
                        .fillColor(0x220000FF)
                        .strokeWidth(5));
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            try {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }catch (Exception ex){

            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Showing Current Location Marker on Map
        LatLng actualPosition = new LatLng(location.getLatitude(), location.getLongitude());
        arrayMyLocation.clear();
        arrayMyLocation.add(actualPosition);
        //actualPosition = positionGabriela;

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(actualPosition).title("Minha posição atual");
        gMap.moveCamera(CameraUpdateFactory.newLatLng(actualPosition));

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location locations = locationManager.getLastKnownLocation(provider);
        List<String> providerList = locationManager.getAllProviders();
        if (null != locations && null != providerList && providerList.size() > 0) {
            double longitude = locations.getLongitude();
            double latitude = locations.getLatitude();
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                List<Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (null != listAddresses && listAddresses.size() > 0) {

//                    Here we are finding , whatever we want our marker to show when clicked
                    String state = listAddresses.get(0).getAdminArea();
                    String country = listAddresses.get(0).getCountryName();
                    String subLocality = listAddresses.get(0).getSubLocality();
                    markerOptions.title("" + actualPosition + "," + subLocality + "," + state + "," + country);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = gMap.addMarker(markerOptions);

        //move map camera
        gMap.moveCamera(CameraUpdateFactory.newLatLng(actualPosition));
        gMap.animateCamera(CameraUpdateFactory.zoomTo(10));

        //this code stops location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

        UpdateMapMarkersRadius();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        gMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    public static LatLng getMyLocation(){
        return arrayMyLocation.get(0);
    }
}