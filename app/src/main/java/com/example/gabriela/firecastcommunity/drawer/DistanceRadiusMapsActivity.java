package com.example.gabriela.firecastcommunity.drawer;

import android.content.Intent;
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

    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
    private static final double DEFAULT_RADIUS_METERS = 1000000;
    private static final int MAX_HUE_DEGREES = 360;

    private GoogleMap mMap;

    private List<DraggableCircle> mCircles = new ArrayList<>(1);

    private SeekBar mFillHueBar;
    private Spinner mStrokePatternSpinner;

    private class DraggableCircle {
        private final Marker mCenterMarker;
        private final Circle mCircle;
        public DraggableCircle(LatLng center, double radiusMeters) {
            mCenterMarker = mMap.addMarker(new MarkerOptions()
                    .position(center)
                    .draggable(true));
            mCircle = mMap.addCircle(new CircleOptions()
                    .center(center)
                    .radius(radiusMeters)
                    .strokeWidth(10.2F)
                    .strokeColor(R.color.colorPrimary)
                    .fillColor(R.color.colorPrimaryDark)
                    .clickable(false));
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance_radius_maps);

        mFillHueBar = (SeekBar) findViewById(R.id.fillHueSeekBar);
        mFillHueBar.setMax(MAX_HUE_DEGREES);
        mFillHueBar.setProgress(MAX_HUE_DEGREES / 2);

        mStrokePatternSpinner = (Spinner) findViewById(R.id.strokePatternSpinner);
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
        mFillHueBar.setOnSeekBarChangeListener(this);

        mStrokePatternSpinner.setOnItemSelectedListener(this);

        DraggableCircle circle = new DraggableCircle(SYDNEY, DEFAULT_RADIUS_METERS);
        mCircles.add(circle);

        // Move the map so that it is centered on the initial circle
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SYDNEY, 4.0f));

        // Set up the click listener for the circle.
        map.setOnCircleClickListener(new OnCircleClickListener() {
            @Override
            public void onCircleClick(Circle circle) {
                }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (parent.getId() == R.id.strokePatternSpinner) {
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