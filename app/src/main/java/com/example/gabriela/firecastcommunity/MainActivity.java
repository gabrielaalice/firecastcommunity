package com.example.gabriela.firecastcommunity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Icon;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.gabriela.firecastcommunity.data.BancoDados;
import com.example.gabriela.firecastcommunity.data.FirecastApi;
import com.example.gabriela.firecastcommunity.data.FirecastClient;
import com.example.gabriela.firecastcommunity.domain.City;
import com.example.gabriela.firecastcommunity.domain.Occurrence;
import com.example.gabriela.firecastcommunity.domain.OccurrenceType;
import com.example.gabriela.firecastcommunity.drawer.AboutUsActivity;
import com.example.gabriela.firecastcommunity.drawer.DistanceRadiusMapsActivity;
import com.example.gabriela.firecastcommunity.drawer.FilterOcActivity;
import com.example.gabriela.firecastcommunity.drawer.NotificationActivity;
import com.example.gabriela.firecastcommunity.drawer.OccurenceTypeUserActivity;
import com.example.gabriela.firecastcommunity.drawer.RegisterErrorActivity;
import com.example.gabriela.firecastcommunity.drawer.RegisterUserActivity;
import com.example.gabriela.firecastcommunity.drawer.ShareAppActivity;
import com.example.gabriela.firecastcommunity.fragment.MapsFragment;
import com.example.gabriela.firecastcommunity.fragment.OccurenceFragment;
import com.example.gabriela.firecastcommunity.fragment.RadioFragment;
import com.example.gabriela.firecastcommunity.helper.DistanceCalculator;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static br.com.zbra.androidlinq.Linq.stream;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView navigation;
    Drawer drawer;
    AccountHeader headerResult;
    ImageButton filter;
    Dialog dialog;
    private static final long ID_ABOUT_US = 100;
    private static final long ID_OCCURRENCE_TYPE = 200;
    private static final long ID_DISTANCE = 300;
    private static final long ID_NOTIFICATION = 400;
    private static final long ID_SHARE_APP = 500;
    private static final long ID_REPORT_ERROR = 600;
    private static final int ID_MANAGE_PROFILE = 100000;

    ScheduledExecutorService executor;
    Runnable periodicTask;
    final List<Occurrence> listOccurenceEnabled = new ArrayList<>();
    private LatLng actualPosition;

    private static final LatLng positionGabriela = new LatLng(-27.6000907,-48.526813);

    private void changeFragment(int position) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment newFragment = null;
        switch (position){
            case 0:
                newFragment = new MapsFragment(10, listOccurenceEnabled);
                break;
            case 1:
                newFragment = new RadioFragment();
                break;
            case 2:
                newFragment = new OccurenceFragment(listOccurenceEnabled);
                break;
        }

        fragmentTransaction.replace(R.id.fragmentContainer, newFragment);
        fragmentTransaction.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    changeFragment(0);
                   break;
                case R.id.navigation_dashboard:
                    changeFragment(1);
                    break;
                case R.id.navigation_notifications:
                    changeFragment(2);
                    break;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment newFragment = new MapsFragment();
        fragmentTransaction.replace(R.id.fragmentContainer, newFragment);
        fragmentTransaction.commit();

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Create Dialog
        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.popup_filter);
        //method call
        isFirstTime();

        //aa view drawer
        final PrimaryDrawerItem itemTypeOccurrence = new PrimaryDrawerItem()
                .withName("Tipo de ocorrência")
                .withIdentifier(ID_OCCURRENCE_TYPE)
                .withBadgeStyle(new BadgeStyle()
                        .withTextColor(Color.WHITE)
                        .withColorRes(R.color.md_orange_700));

        final PrimaryDrawerItem itemDistance = new PrimaryDrawerItem()
                .withName("Fixar abrangência")
                .withIdentifier(ID_DISTANCE)
                .withBadgeStyle(new BadgeStyle()
                        .withTextColor(Color.WHITE)
                        .withColorRes(R.color.md_orange_700));

        final PrimaryDrawerItem itemNotification = new PrimaryDrawerItem()
                .withName("Notificações")
                //.withBadge("2")
                .withIdentifier(ID_NOTIFICATION)
                .withBadgeStyle(new BadgeStyle()
                        .withTextColor(Color.WHITE)
                        .withColorRes(R.color.md_orange_700));

        final PrimaryDrawerItem itemShareApp = new PrimaryDrawerItem()
                .withName("Compartilhar APP")
                .withIdentifier(ID_SHARE_APP)
                .withIcon(R.drawable.ic_menu_share)
                .withBadgeStyle(new BadgeStyle()
                        .withTextColor(Color.WHITE)
                        .withColorRes(R.color.md_orange_700));

        final PrimaryDrawerItem itemReportError = new PrimaryDrawerItem()
                .withName("Reportar erro")
                .withIdentifier(ID_REPORT_ERROR)
                .withIcon(R.drawable.ic_menu_manage)
                .withBadgeStyle(new BadgeStyle()
                        .withTextColor(Color.WHITE)
                        .withColorRes(R.color.md_orange_700));

        final PrimaryDrawerItem itemAboutUs = new PrimaryDrawerItem()
                .withName("Sobre nós")
                .withIcon(R.drawable.ic_info_black_24dp)
                .withIdentifier(ID_ABOUT_US);

        final ProfileSettingDrawerItem manageAccount = new ProfileSettingDrawerItem()
                .withName("Editar cadastro")
                .withIdentifier(ID_MANAGE_PROFILE);
/*
        Bundle inBundle = getIntent().getExtras();

        if(inBundle!=null) {
            String name = inBundle.get("name").toString();
            String surname = inBundle.get("surname").toString();

            //TODO email e picture facebook
            String imageUrl = inBundle.get("imageUrl").toString();
            //  new MapActivity.DownloadImage((ImageView)findViewById(R.id.profileImage)).execute(imageUrl);

            final IProfile profile = new ProfileDrawerItem()
                    .withName(name + " " + surname)
                    //.withEmail(email)
                    .withIcon("https://avatars3.githubusercontent.com/u/1476232?v=3&s=460")
                    .withIdentifier(100);

            headerResult = new AccountHeaderBuilder()
                    .withActivity(this)
                    .withTranslucentStatusBar(true)
                    .withHeaderBackground(R.color.colorPrimary)
                    .addProfiles(
                            profile,
                            manageAccount,
                            new ProfileSettingDrawerItem()
                                    .withName("Logout")
                                    //.withIcon(R.drawable.logo)
                                    .withIdentifier(ID_LOGOUT)

                    )
                    .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                        @Override
                        public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                            if (profile instanceof IDrawerItem && profile.getIdentifier() == ID_MANAGE_PROFILE) {
                                configuraItensHeaderDrawer();
                            }
                            if (profile instanceof IDrawerItem && profile.getIdentifier() == ID_LOGOUT) {
                                configuraItensLogoutDrawer();
                            }
                            return false;
                        }
                    })
                    .withSavedInstance(savedInstanceState)
                    .build();
             }
*/

        drawer = new DrawerBuilder()
                .withAccountHeader(headerResult)
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        /*
                        new SectionDrawerItem().withName("Ações do Usuário"),
                        itemDistance,
                        itemTypeOccurrence,
                        itemNotification,
                        */
                        new SectionDrawerItem().withName("Ações do Sistema"),
                        itemAboutUs,
                        itemShareApp,
                        itemReportError
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        configuraItensDrawer(position, drawerItem);
                        return true;
                    }
                })
                .build();

        //executor = Executors.newSingleThreadScheduledExecutor();
        //periodicTask = new Runnable() {
            //public void run() {
try {
    buscarOcorrencias();
    //}
    //};
}finally{    changeFragment(0);}
    }

    private void buscarOcorrencias() {
        listOccurenceEnabled.removeAll(listOccurenceEnabled);
        FirecastClient fire = new FirecastClient();
        FirecastApi api = fire.retrofit.create(FirecastApi.class);
        List<City> listCities = BancoDados.cities();

        for (City cidade : listCities) {
            api.getOccurrences(cidade.name)
                    .enqueue(new Callback<List<Occurrence>>() {

                        public void onResponse(Call<List<Occurrence>> call, Response<List<Occurrence>> response) {

                                List<Occurrence> list = response.body();
                                if (list != null) {
                                    List<Integer> listIds = stream(listOccurenceEnabled).select(c -> c.id).toList();
                                    List<Occurrence> listList = stream(list).where(c -> !listIds.contains(c.id)).toList();

                                    for (Occurrence occ : listList) {
                                        actualPosition = positionGabriela;
                                            Double distance = new DistanceCalculator()
                                                    .distancia(actualPosition, getLocation(occ));
                                            if (distance == 0 || distance < 0) {
                                                occ.distance = null;
                                            } else {
                                                occ.distance = distance / 1000;
                                            }
                                    }

//                                    listList.forEach(occ->
//                                            occ.distance = new DistanceCalculator()
//                                                    .distance(actualPosition, getLocation(occ)));

                                    listOccurenceEnabled.addAll(listList);
                                }
                            }

                        @Override
                        public void onFailure(Call<List<Occurrence>> call, Throwable t) {

                        }
                    });
    }
    }



    private Occurrence getLocation(Occurrence occurrence) {
        if (occurrence.latitude != null || occurrence.longitude != null) {
            return occurrence;
        }
        return null;
    }

    //Navigation Drawer.
    private void configuraItensDrawer(int position, IDrawerItem drawerItem) {
        try {
            switch ((int) drawerItem.getIdentifier()) {

                case (int) ID_ABOUT_US:
                    Intent about_us_intent = new Intent(this, AboutUsActivity.class);
                    startActivity(about_us_intent);
                    finish();
                    break;
                case (int) ID_DISTANCE:
                    Intent distance_intent = new Intent(this, DistanceRadiusMapsActivity.class);
                    startActivity(distance_intent);
                    finish();
                    break;
                case (int) ID_NOTIFICATION:
                    Intent notification_intent = new Intent(this, NotificationActivity.class);
                    startActivity(notification_intent);
                    finish();
                    break;
                case (int) ID_OCCURRENCE_TYPE:
                    Intent occurrence_type_intent = new Intent(this, OccurenceTypeUserActivity.class);
                    startActivity(occurrence_type_intent);
                    finish();
                    break;
                case (int) ID_SHARE_APP:
                    Intent share_app_intent = new Intent(this, ShareAppActivity.class);
                    startActivity(share_app_intent);
                    finish();
                    break;
                case (int) ID_REPORT_ERROR:
                    Intent report_error_intent = new Intent(this, RegisterErrorActivity.class);
                    startActivity(report_error_intent);
                    finish();
                    break;


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        drawer.closeDrawer();
    }

    private void configuraItensHeaderDrawer(){
        Intent i = new Intent(this, RegisterUserActivity.class);
        startActivity(i);
        finish();
    }

    private void configuraItensLogoutDrawer(){
        //LoginManager.getInstance().logOut();
        //  Intent login = new Intent(MainActivity.this, LoginActivity.class);
        //startActivity(login);
        //  finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    //TODO picture facebook
    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImage(ImageView bmImage){
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls){
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try{
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            }catch (Exception e){
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result){
            bmImage.setImageBitmap(result);
        }

    }

    // menu filterActivity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, FilterOcActivity.class);
            startActivity(i);
            finish();
            //return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isFirstTime() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        if (!ranBefore) {
            //show dialog if app never launch
            dialog.show();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.commit();
        }
        return !ranBefore;
    }
}
