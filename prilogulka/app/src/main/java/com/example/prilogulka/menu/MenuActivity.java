package com.example.prilogulka.menu;


import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.prilogulka.R;
import com.example.prilogulka.data.managers.SharedPreferencesManager;
import com.example.prilogulka.data.userData.SerializeObject;
import com.example.prilogulka.data.userData.UserInfo;
import com.example.prilogulka.data_base.VideoDAO;
import com.example.prilogulka.menu.fragments.ActivatedCardsFragment;
import com.example.prilogulka.menu.fragments.ConnectUsFragment;
import com.example.prilogulka.menu.fragments.GiftsManagerFragment;
import com.example.prilogulka.menu.fragments.HelpWithAppFragment;
import com.example.prilogulka.menu.fragments.PersonalDataFragment;
import com.example.prilogulka.menu.fragments.WatchingVideoFragment;

import java.io.IOException;


public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String CLASS_TAG = "MenuActivity";

    private SharedPreferencesManager spManager;
    //private ActionsDAO actionsDAO;
    private String email;
    private UserInfo user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        initGlobalVars();
        initToolbarAndMenu();
        initMenuHeader();
        initDataBases();

        /*  Задаем начальный фрагмент    */
        setFragment(new HelpWithAppFragment());
    }

    /*  Инициализация */
    private void initGlobalVars() {
        spManager = new SharedPreferencesManager(this);

//        actionsDAO = new ActionsDAO(this);
        email = spManager.getActiveUser();

        SerializeObject<UserInfo> so = new SerializeObject<UserInfo>(this);
        user = new UserInfo();
        try {
            user = so.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.getStackTrace();
        }
        if (user != null)
            Log.i(CLASS_TAG, user.toString());
    }
    private void initToolbarAndMenu() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        /*  Рисуем меню */
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }
    private void initMenuHeader() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        TextView userNameHeader = headerView.findViewById(R.id.nav_header_user_name);
        userNameHeader.setText(user.getUser().getName() + " " + user.getUser().getLastname());

        TextView userEmailHeader = headerView.findViewById(R.id.nav_header_user_email);
        userEmailHeader.setText(user.getUser().getEmail());

        TextView userCoefHeader = headerView.findViewById(R.id.nav_header_user_coef);
        float coeff = (float) (spManager.getWPCoefficient() * (spManager.getQuestionnaire() ? 1.2 : 1));
        userCoefHeader.setText(coeff + "");

        /* начальное значение выделленой строки меню    */
        navigationView.setCheckedItem(R.id.nav_help);
    }
    private void initDataBases() {
        VideoDAO videoDAO = new VideoDAO(this);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction
                .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out)
                .replace(R.id.flContent, fragment)
                .commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_right_corner, menu);
        menu.getItem(0).setVisible(false);
//        menu.getItem(0).setTitle("Состояние счета: " + (actionsDAO.getUserMoney(email)));
//        Log.i("MENU_ACTIVITY", (actionsDAO.getUserMoney(email)) + "");

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()){
            case R.id.nav_person:
                fragment = new PersonalDataFragment();
                break;
            case R.id.nav_video:
                fragment = new WatchingVideoFragment();
                break;
            case R.id.nav_gifts:
                fragment = new GiftsManagerFragment();
                break;
            case R.id.nav_gifts_activated:
                fragment = new ActivatedCardsFragment();
                break;
            case R.id.nav_help:
                fragment = new HelpWithAppFragment();
                break;
            case R.id.nav_connect_us:
                fragment = new ConnectUsFragment();
                break;
        }

        setFragment(fragment);
        item.setChecked(true);
        setTitle(item.getTitle());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
}






