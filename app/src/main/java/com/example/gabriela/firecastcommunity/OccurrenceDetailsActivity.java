package com.example.gabriela.firecastcommunity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.gabriela.firecastcommunity.domain.Occurrence;
import com.example.gabriela.firecastcommunity.fragment.MapsFragment;
import com.example.gabriela.firecastcommunity.utility.Constant;
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
    TextView cars,location, reference, city, referenceTitle, occurrence_type, description;
    View underlineReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_occurrence_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

            Bundle extras = getIntent().getExtras();

            if (extras != null) {
                occurrence = (Occurrence) extras.getSerializable("OccurrenceKey");

                occurrence_type = findViewById(R.id.occurence_type);
                occurrence_type.setText(occurrence.type.name);

                cars = (TextView) findViewById(R.id.cardoccurrenceitem__cars);
                location = (TextView) findViewById(R.id.cardoccurrenceitem__location);
                location.setText(occurrence.adressStreet + ", " + occurrence.addressNumber);
                reference = (TextView) findViewById(R.id.cardoccurrenceitem__reference);
                reference.setText(occurrence.addressReferencePoint);
                city = (TextView) findViewById(R.id.cardoccurrenceitem__city);
                city.setText(occurrence.city.name + ", " + occurrence.addressNeighborhood);
                referenceTitle = (TextView) findViewById(R.id.cardoccurrencetitle__reference);
                underlineReference = findViewById(R.id.underline__reference);
                description = (TextView) findViewById(R.id.cardoccurrenceitem__details);
                description.setText(occurrence.description);

                actualPosition = MapsFragment.getMyLocation();

                SupportMapFragment mapFragment =
                        (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);

            }else{
                finish();
            }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(actualPosition,10));

        if(mMap!=null) {
            mMap.clear();

            if(actualPosition!=null) {

                mMap.addMarker(new MarkerOptions().position(actualPosition)
                        .title("Minha posição atual")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
            }

            if(occurrence.latitude!=null && occurrence.longitude!=null) {
                LatLng positionOcc = new LatLng(occurrence.latitude, occurrence.longitude);
                mMap.addMarker(new MarkerOptions().position(positionOcc)
                        .title(occurrence.city.name + " / " + occurrence.description)
                        .icon(BitmapDescriptorFactory.fromBitmap(GetColorMarkerOccurrence(this, occurrence))));
            }
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
    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                finish();

                break;

            default:
                break;
        }

        return true;
    }

}
