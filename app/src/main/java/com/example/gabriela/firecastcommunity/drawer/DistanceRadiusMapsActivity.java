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

import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;


public class DistanceRadiusMapsActivity extends FragmentActivity
        implements OnSeekBarChangeListener, GoogleMap.OnMarkerDragListener,
        OnMapReadyCallback {


    private static final LatLng positionGabriela = new LatLng(-27.6000907,-48.526813);

    private GoogleMap mMap;

    private List<DraggableCircle> mCircles = new ArrayList<>(1);
    private SeekBar distanceSeekBar;
    private Spinner citySpinner;
    private Button saveButton;

    private class DraggableCircle {
        private final Marker mCenterMarker;
        private final Circle mCircle;
        private double mRadiusMeters;



        public DraggableCircle(LatLng center, double radiusMeters) {
            mRadiusMeters = radiusMeters;
            mCenterMarker = mMap.addMarker(new MarkerOptions()
                    .position(center)
                    .draggable(true));
            ;
            mCircle = mMap.addCircle(new CircleOptions()
                    .center(center)
                    .radius(100000)
                    .strokeColor(Color.RED)
                    .fillColor(0x220000FF)
                    .clickable(true));
        }

        public boolean onMarkerMoved(Marker marker) {
            if (marker.equals(mCenterMarker)) {
                mCircle.setCenter(marker.getPosition());
                mCircle.setRadius(distanceSeekBar.getProgress() * 1000);
                return true;
            }

            return false;
        }

        public void onStyleChange() {
          //  mCircle.setStrokeColor(R.color.colorPrimaryDark);
            mCircle.setRadius(distanceSeekBar.getProgress() * 1000);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance_radius_maps);

         distanceSeekBar = (SeekBar) findViewById(R.id.distance_km_seek);
        citySpinner = (Spinner) findViewById(R.id.city_spinner);
        saveButton = (Button) findViewById(R.id.btn_save);

        distanceSeekBar.setMax(100);
        distanceSeekBar.setProgress(distanceSeekBar.getProgress() * 1000);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public SeekBar.OnSeekBarChangeListener FilterDistance() {
        return new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                distanceSeekBar = seekBar;

                for (DraggableCircle draggableCircle : mCircles) {
                    draggableCircle.onStyleChange();
                }

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
    };
    @Override
    public void onMapReady(GoogleMap map) {
        map.setContentDescription(getString(R.string.app_name));

        mMap = map;
        mMap.setOnMarkerDragListener(this);

        distanceSeekBar.setOnSeekBarChangeListener(FilterDistance());
        DraggableCircle circle = new DraggableCircle(positionGabriela, distanceSeekBar.getProgress() * 1000);
        mCircles.add(circle);

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
        for (DraggableCircle draggableCircle : mCircles) {
            if (draggableCircle.onMarkerMoved(marker)) {
                break;
            }
        }
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

}