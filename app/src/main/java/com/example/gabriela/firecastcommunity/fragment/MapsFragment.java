package com.example.gabriela.firecastcommunity.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.example.gabriela.firecastcommunity.MainActivity;
import com.example.gabriela.firecastcommunity.R;
import com.example.gabriela.firecastcommunity.domain.Occurrence;
import com.example.gabriela.firecastcommunity.domain.OccurrenceType;
import com.example.gabriela.firecastcommunity.drawer.DistanceRadiusMapsActivity;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private MapView mapView;
    private GoogleMap gMap;
    private Integer radiusUser;

    private static final LatLng positionGabriela = new LatLng(-27.6000907,-48.526813);

    private static final double DEFAULT_RADIUS_METERS = 1000000;
    private static final int MAX_HUE_DEGREES = 360;
    LatLng actualPosition = new LatLng(-27.6000907,-48.526813);
    private GoogleMap mMap;
    List<Occurrence> occurrences;

    public MapsFragment(){}

    @SuppressLint("ValidFragment")
    public MapsFragment(Integer radiusUser, List<Occurrence> occurrences){
        this.radiusUser = radiusUser;
        this.occurrences = occurrences;
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


    private class DraggableCircle {

        public DraggableCircle(LatLng center, double radiusMeters) {
            if(mMap!=null)
            {
                mMap.clear();
                Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.user_pin);
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);

                mMap.addMarker(new MarkerOptions().position(actualPosition)
                        .title("Minha posição atual")
                        .icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap)));


                for (Occurrence occ : occurrences) {
                    if (occ.latitude != null || occ.longitude != null) {
                        LatLng positionOcc = new LatLng(occ.latitude, occ.longitude);
                        mMap.addMarker(new MarkerOptions().position(positionOcc)
                                .title(occ.city.name + " / " + occ.description)
                                .icon(BitmapDescriptorFactory.fromBitmap(GetColorMarkerOccurrence(occ))));

                    }
                }

                if (true) {//checkBoxRadiusDistance.isChecked()) {

                    Circle circle = mMap.addCircle(new CircleOptions()
                            .center(actualPosition)
                            .radius(radiusUser * 1000)
                            .strokeColor(Color.RED)
                            .fillColor(0x220000FF)
                            .strokeWidth(5));
                }
            }
        }
    }

        private Bitmap GetColorMarkerOccurrence(Occurrence occurrence) {
            
            Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.other_pin);
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);

            switch (occurrence.type.id){
                case 1:
                    icon = BitmapFactory.decodeResource(getResources(),R.drawable.fire_pin);
                    resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);
                    return resizedBitmap;
                case 2:
                    icon = BitmapFactory.decodeResource(getResources(),R.drawable.search_rescue_pin);
                    resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);
                    return resizedBitmap;
                case 3:
                    icon = BitmapFactory.decodeResource(getResources(),R.drawable.dangerous_product_pin);
                    resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);
                    return resizedBitmap;
                case 4:
                    icon = BitmapFactory.decodeResource(getResources(),R.drawable.fire_pin);
                    resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);
                    return resizedBitmap;
                case 5:
                    icon = BitmapFactory.decodeResource(getResources(),R.drawable.paramedics_pin);
                    resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);
                    return resizedBitmap;
                case 6:
                    icon = BitmapFactory.decodeResource(getResources(),R.drawable.other_pin);
                    resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);
                    return resizedBitmap;
                case 7:
                    icon = BitmapFactory.decodeResource(getResources(),R.drawable.other_pin);
                    resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);
                    return resizedBitmap;
                case 8:
                    icon = BitmapFactory.decodeResource(getResources(),R.drawable.car_accident_pin);
                    resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);
                    return resizedBitmap;
                case 9:
                    icon = BitmapFactory.decodeResource(getResources(),R.drawable.other_pin);
                    resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);
                    return resizedBitmap;
                case 10:
                    icon = BitmapFactory.decodeResource(getResources(),R.drawable.tree_cutting_pin);
                    resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);
                    return resizedBitmap;
                case 11:
                    icon = BitmapFactory.decodeResource(getResources(),R.drawable.insect_control_pin);
                    resizedBitmap = Bitmap.createScaledBitmap(icon, 70, 100, false);
                    return resizedBitmap;
            }
            return resizedBitmap;

        }

    @Override
    public void onMapReady(GoogleMap map) {
        // Override the default content description on the view, for accessibility mode.
//        map.setContentDescription(getString(R.string.title_activity_distance_radius_maps));

        mMap = map;

        if(radiusUser!=null) {
            DraggableCircle circle = new DraggableCircle(positionGabriela, radiusUser * 1000);
        }

        // Move the map so that it is centered on the initial circle
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(positionGabriela, 10));

        // Set up the click listener for the circle.
        map.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
            @Override
            public void onCircleClick(Circle circle) {
            }
        });


    }

}