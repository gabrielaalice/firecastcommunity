package com.example.gabriela.firecastcommunity.drawer;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.example.gabriela.firecastcommunity.MainActivity;
import com.example.gabriela.firecastcommunity.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCircleClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class DistanceRadiusMapsActivity extends FragmentActivity
        implements OnSeekBarChangeListener, GoogleMap.OnMarkerDragListener,
        OnMapReadyCallback {


    private static final LatLng positionGabriela = new LatLng(-27.6000907,-48.526813);

    private GoogleMap mMap;
    private TextView txtSeekBar;
    private SeekBar distanceSeekBar;
    private Spinner citySpinner;
    private Button saveButton;
    private Circle circle;

    private class DraggableCircle {
        private final Marker mCenterMarker;

        public DraggableCircle(LatLng center, double radiusMeters) {
            mCenterMarker = mMap.addMarker(new MarkerOptions()
                    .position(center)
                    .draggable(true));

            UpdateCircleMap();
        }

        public boolean onMarkerMoved(Marker marker) {
            if (marker.equals(mCenterMarker)) {
                circle.setCenter(marker.getPosition());
                circle.setRadius(distanceSeekBar.getProgress() * 1000);
                return true;
            }

            return false;
        }

        public void onStyleChange() {
          //  mCircle.setStrokeColor(R.color.colorPrimaryDark);
            circle.setRadius(distanceSeekBar.getProgress() * 1000);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance_radius_maps);

        distanceSeekBar = (SeekBar) findViewById(R.id.distance_km_seek);
        citySpinner = (Spinner) findViewById(R.id.city_spinner);
        saveButton = (Button) findViewById(R.id.btn_save);
        txtSeekBar = (TextView) findViewById(R.id.txtSeekBar);

        distanceSeekBar.setMax(100);
        distanceSeekBar.setProgress(distanceSeekBar.getProgress() * 1000);
        distanceSeekBar.setOnSeekBarChangeListener(FilterDistance());

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public SeekBar.OnSeekBarChangeListener FilterDistance() {
        return new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                txtSeekBar.setText("Distância (Raio): " + String.valueOf(progress) +" km");
                UpdateCircleMap();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //tvSb.setVisibility(View.VISIBLE);

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //tvSb.setVisibility(View.GONE);
            }
        };
    }

    private void UpdateCircleMap() {
        circle = mMap.addCircle(new CircleOptions()
                .radius(distanceSeekBar.getProgress() * 1000)
                .strokeColor(Color.RED)
                .fillColor(0x220000FF)
                .clickable(true));
    }

    ;
    @Override
    public void onMapReady(GoogleMap map) {
        map.setContentDescription(getString(R.string.app_name));

        mMap = map;
        mMap.setOnMarkerDragListener(this);

        distanceSeekBar.setOnSeekBarChangeListener(FilterDistance());
        DraggableCircle circle = new DraggableCircle(positionGabriela, distanceSeekBar.getProgress() * 1000);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(positionGabriela, 4.0f));

        map.setOnCircleClickListener(new OnCircleClickListener() {
            @Override
            public void onCircleClick(Circle circle) {
                circle.setStrokeColor(circle.getStrokeColor() ^ 0x00ffffff);
            }
        });

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // Don't do anything here.
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // Don't do anything here.
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        onMarkerMoved(marker);
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        onMarkerMoved(marker);
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        onMarkerMoved(marker);
    }

    private void onMarkerMoved(Marker marker) {
//        for (DraggableCircle draggableCircle : mCircles) {
//            if (draggableCircle.onMarkerMoved(marker)) {
//                break;
//            }
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.save_preference_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_save) {
            return true;
        }
        if( id == android.R.id.home){
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}