package com.example.gabriela.firecastcommunity.drawer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
        import android.os.Bundle;

import com.example.gabriela.firecastcommunity.AutoCompleteTextViewAdapter;
import com.example.gabriela.firecastcommunity.MainActivity;
        import com.example.gabriela.firecastcommunity.R;
import com.example.gabriela.firecastcommunity.data.DataBaseTemp;
import com.example.gabriela.firecastcommunity.data.FirecastDB;
import com.example.gabriela.firecastcommunity.domain.City;
import com.example.gabriela.firecastcommunity.domain.OccurrenceType;
import com.example.gabriela.firecastcommunity.domain.User;
import com.example.gabriela.firecastcommunity.fragment.MapsFragment;
import com.example.gabriela.firecastcommunity.helper.MetodsHelpers;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.GoogleMap.OnCircleClickListener;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
        import com.google.android.gms.maps.model.CircleOptions;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.Marker;
        import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
        import android.view.Menu;
        import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
        import android.widget.Button;
        import android.widget.SeekBar;
        import android.widget.SeekBar.OnSeekBarChangeListener;
        import android.widget.Spinner;
        import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
        import java.util.List;

import static br.com.zbra.androidlinq.Linq.stream;
import static com.facebook.FacebookSdk.getApplicationContext;

public class DistanceRadiusMapsActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private SeekBar seekbar;
    private AutoCompleteTextView cityAutoComplete;
    private TextView txtSeekBarRadius;
    private User user;
    private FirecastDB repository;
    private List<City> cities;
    private LatLng actualPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance_radius_maps);

        repository =new FirecastDB(this);
        user = repository.getUser();
        actualPosition = MapsFragment.getMyLocation();

        SetSeekBar();
        SetAutoCompleteCities();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void SetAutoCompleteCities() {
        cities = DataBaseTemp.cities();
        cityAutoComplete = findViewById(R.id.cityAutoComplete);

        City city = getCityFromId(user.getId_city_occurrence());

        if (city != null){
            cityAutoComplete.setText(city.name);
        }

        ArrayAdapter adapterCities = new AutoCompleteTextViewAdapter(this,android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, stream(cities).select(x->x.name).toList());
        cityAutoComplete.setAdapter(adapterCities);
        cityAutoComplete.setVisibility(View.VISIBLE);
        cityAutoComplete.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
                cityAutoComplete.showDropDown();
                cityAutoComplete.requestFocus();

                return false;
            }
        });
    }

    public void SetSeekBar(){
        seekbar = findViewById(R.id.distance_km_seek);
        seekbar.setMax(100);

        int radius = user.getRadiusKilometers();

        seekbar.setProgress(radius);
        seekbar.setOnSeekBarChangeListener(ChangeSeekBar());

        txtSeekBarRadius = (TextView) findViewById(R.id.txtSeekBar);
        txtSeekBarRadius.setText("Distância (Raio): " + String.valueOf(seekbar.getProgress()) +" km");
    }

    public SeekBar.OnSeekBarChangeListener ChangeSeekBar() {
        return new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                txtSeekBarRadius.setText("Distância (Raio): " + String.valueOf(progress) +" km");
                UpdateCircleRadiusKM();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        };
    }

    private void UpdateCircleRadiusKM() {
        if(mMap!=null) {
            mMap.clear();

            mMap.addMarker(new MarkerOptions().position(actualPosition)
                    .title("Minha posição atual")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

            int radius = seekbar!=null ? seekbar.getProgress() : user.getRadiusKilometers();

            Circle circle = mMap.addCircle(new CircleOptions()
                    .center(actualPosition)
                    .radius(radius * 1000)
                    .strokeColor(Color.RED)
                    .fillColor(0x220000FF)
                    .strokeWidth(5));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(actualPosition,10));

        UpdateCircleRadiusKM();
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
            if(SaveChanges()) {
                Intent i = new Intent(this, MainActivity.class);
                setResult(Activity.RESULT_OK, i);
                finish();
                return true;
            }
            return false;
        }
        if( id == android.R.id.home){
            Intent i = new Intent(this, MainActivity.class);
            setResult(Activity.RESULT_CANCELED, i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean SaveChanges() {
        String cityName = cityAutoComplete.getText().toString();
        City city = getCityFromName(cityName);

        if(city == null){
            Toast.makeText(this,"Cidade inválida", Toast.LENGTH_LONG).show();
            return false;
        }else {
            user.setId_city_occurrence(city.id);
            user.setRadiusKilometers(seekbar.getProgress());

            return repository.UpdateUser(user);
        }
    }

    private City getCityFromName(String name) {
        return stream(cities).firstOrNull(x-> MetodsHelpers.normalizeString(x.name)
                .equalsIgnoreCase(MetodsHelpers.normalizeString(name)));
    }

    private City getCityFromId(int id_city) {
        return stream(cities).firstOrNull(x-> x.id == id_city);
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
