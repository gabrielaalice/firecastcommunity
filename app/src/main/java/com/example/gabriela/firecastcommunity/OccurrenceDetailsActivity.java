package com.example.gabriela.firecastcommunity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.gabriela.firecastcommunity.domain.Occurrence;
import com.example.gabriela.firecastcommunity.fragment.MapsFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class OccurrenceDetailsActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private LatLng actualPosition;
    Occurrence occurrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_occurrence_details);

            Bundle extras = getIntent().getExtras();

            if (extras != null) {
                occurrence = (Occurrence) extras.getSerializable("OccurrenceKey");

                TextView occurrence_type = findViewById(R.id.occurence_type);
                occurrence_type.setText(occurrence.type.name);

                TextView city = findViewById(R.id.city);
                city.setText(occurrence.city.name);

                actualPosition = MapsFragment.getMyLocation();

                SupportMapFragment mapFragment =
                        (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);

            }else{
                finish();
            }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.filter_occurence, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if( id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(actualPosition,10));

        if(mMap!=null) {
            mMap.clear();

            mMap.addMarker(new MarkerOptions().position(actualPosition)
                    .title("Minha posição atual")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

            LatLng positionOcc = new LatLng(occurrence.latitude, occurrence.longitude);
            mMap.addMarker(new MarkerOptions().position(positionOcc)
                    .title(occurrence.city.name + " / " + occurrence.description)
                    .icon(BitmapDescriptorFactory.fromBitmap(GetColorMarkerOccurrence(this,occurrence))));
        }
    }

    private static Bitmap GetColorMarkerOccurrence(Context context, Occurrence occurrence) {

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),R.drawable.other_pin);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);

        switch (occurrence.type.id){
            case 1:
                icon = BitmapFactory.decodeResource(context.getResources(),R.drawable.fire_pin);
                resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);
                return resizedBitmap;
            case 2:
                icon = BitmapFactory.decodeResource(context.getResources(),R.drawable.search_rescue_pin);
                resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);
                return resizedBitmap;
            case 3:
                icon = BitmapFactory.decodeResource(context.getResources(),R.drawable.dangerous_product_pin);
                resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);
                return resizedBitmap;
            case 4:
                icon = BitmapFactory.decodeResource(context.getResources(),R.drawable.fire_pin);
                resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);
                return resizedBitmap;
            case 5:
                icon = BitmapFactory.decodeResource(context.getResources(),R.drawable.paramedics_pin);
                resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);
                return resizedBitmap;
            case 6:
                icon = BitmapFactory.decodeResource(context.getResources(),R.drawable.other_pin);
                resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);
                return resizedBitmap;
            case 7:
                icon = BitmapFactory.decodeResource(context.getResources(),R.drawable.other_pin);
                resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);
                return resizedBitmap;
            case 8:
                icon = BitmapFactory.decodeResource(context.getResources(),R.drawable.car_accident_pin);
                resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);
                return resizedBitmap;
            case 9:
                icon = BitmapFactory.decodeResource(context.getResources(),R.drawable.other_pin);
                resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);
                return resizedBitmap;
            case 10:
                icon = BitmapFactory.decodeResource(context.getResources(),R.drawable.tree_cutting_pin);
                resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);
                return resizedBitmap;
            case 11:
                icon = BitmapFactory.decodeResource(context.getResources(),R.drawable.insect_control_pin);
                resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);
                return resizedBitmap;
        }
        return resizedBitmap;

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }
}
