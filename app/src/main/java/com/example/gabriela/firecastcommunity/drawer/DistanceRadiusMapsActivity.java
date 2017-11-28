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
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class DistanceRadiusMapsActivity extends FragmentActivity implements OnSeekBarChangeListener,
        OnItemSelectedListener, OnMapReadyCallback {

    private static final LatLng positionGabriela = new LatLng(-27.6000907,-48.526813);

    private static final double DEFAULT_RADIUS_METERS = 1000000;
    private static final int MAX_HUE_DEGREES = 360;
    LatLng actualPosition;
    private GoogleMap mMap;
    private Circle mCircle;
    private List<DraggableCircle> mCircles = new ArrayList<>(1);

    private SeekBar radiusSeekbar;
    private Spinner citySpinner;


    private class DraggableCircle {
        private final Marker mCenterMarker;

        public DraggableCircle(LatLng center, double radiusMeters) {
            mCenterMarker = mMap.addMarker(new MarkerOptions()
                    .position(center)
                    .draggable(true));

            mCircle = mMap.addCircle(new CircleOptions()
                    .center(center)
                    .radius(radiusSeekbar.getProgress() * 1000)
                    .strokeWidth(5)
                    .strokeColor(Color.RED)
                    .fillColor(0x220000FF)
                    .clickable(false));

        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance_radius_maps);

        radiusSeekbar = (SeekBar) findViewById(R.id.radiusSeekbar);
        radiusSeekbar.setMax(MAX_HUE_DEGREES);
        radiusSeekbar.setProgress(MAX_HUE_DEGREES / 2);

        radiusSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCircle.setRadius(progress);
            }

            @Override
            public void onStartTrackingTouch(final SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
            }
        });

        citySpinner = (Spinner) findViewById(R.id.citySpinner);
        //LIST OF CITIES
        /*
        mStrokePatternSpinner.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,
                getResourceStrings(PATTERN_TYPE_NAME_RESOURCE_IDS)));
*/

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        // Override the default content description on the view, for accessibility mode.
        map.setContentDescription(getString(R.string.title_activity_distance_radius_maps));

        mMap = map;
        radiusSeekbar.setOnSeekBarChangeListener(this);

        citySpinner.setOnItemSelectedListener(this);

        DraggableCircle circle = new DraggableCircle(positionGabriela, radiusSeekbar.getProgress() * 1000);
        mCircles.add(circle);

        // Move the map so that it is centered on the initial circle
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(positionGabriela, 5.0f));

        // Set up the click listener for the circle.
        map.setOnCircleClickListener(new OnCircleClickListener() {
            @Override
            public void onCircleClick(Circle circle) {
                }
        });


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (parent.getId() == R.id.city_spinner) {
            for (DraggableCircle draggableCircle : mCircles) {
                //   draggableCircle.setStrokePattern(getSelectedPattern(pos));
            }
        }
    }



    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Don't do anything here.
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
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}