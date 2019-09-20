package com.example.lennox.newsapp.activities;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.example.lennox.newsapp.BottomNavigationBehavior;
import com.example.lennox.newsapp.DarkModePrefManager;
import com.example.lennox.newsapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;

import com.example.lennox.newsapp.fragments.HomeFragment;
import com.example.lennox.newsapp.fragments.NewsFragments;

public class HomePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String API_KEY = "1aefe6a9-8256-4ed1-9705-078617ffba7b";
    public static final String NEWS_URL = "https://content.guardianapis.com/search?";
    private static final String TAG = HomePageActivity.class.getSimpleName();
    private MyParceable myClass;

    private BottomNavigationView bottomNavigationView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            Fragment fragment = null;
            Bundle bundle = new Bundle();
            switch (item.getItemId()) {
                case R.id.menu_world:
                    bundle.putString("section", "world");
                    fragment = new NewsFragments();
                    fragment.setArguments(bundle);
                    displayFragment(fragment);
                    return true;
                case R.id.menu_sports:
                    bundle.putString("section", "sport");
                    fragment = new NewsFragments();
                    fragment.setArguments(bundle);
                    displayFragment(fragment);
                    return true;
                case R.id.navigationHome:
                    displayFragment(new HomeFragment());
                    return true;
                case R.id.menu_tech:
                    bundle.putString("section", "technology");
                    fragment = new NewsFragments();
                    fragment.setArguments(bundle);
                    displayFragment(fragment);
                    return true;
                case R.id.navigationMenu:
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.openDrawer(GravityCompat.START);
                    return true;

            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check which theme user has set as preferred
        if (new DarkModePrefManager(this).isNightMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        bottomNavigationView.setSelectedItemId(R.id.navigationHome);

//        SectionsPagerAdapter vpAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
//        ViewPager vp = findViewById(R.id.vp_shift_sections);
//        vp.setAdapter(vpAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * @param fragment object passed from the @OnItemSelectedListener()
     * @method replaces the current fragment with the selected one
     */
    private void displayFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment).commit();
    }

    //Saves state of app
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("obj", myClass);
    }

    //Used to prevent calling onCreate() after screen rotation
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        myClass = savedInstanceState.getParcelable("obj");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_camera:
                break;
            case R.id.setting:
                startActivity(new Intent(this, Settings.class));
                break;
            case R.id.nav_manage:
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_dark_mode:
                //code for setting dark mode
                //true for dark mode, false for day mode, currently toggling on each click
                DarkModePrefManager darkModePrefManager = new DarkModePrefManager(this);
                darkModePrefManager.setDarkMode(!darkModePrefManager.isNightMode());
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                recreate();
                break;
            default:
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static class MyParceable implements Parcelable {
        private int mData;

        @Override
        public int describeContents() {
            return 0;
        }

        // save object in parcel
        @Override
        public void writeToParcel(Parcel out, int flags) {
            out.writeInt(mData);
        }

        public static final Parcelable.Creator<MyParceable> CREATOR =
                new Parcelable.Creator<MyParceable>() {
                    @Override
                    public MyParceable createFromParcel(Parcel in) {
                        return new MyParceable(in);
                    }

                    @Override
                    public MyParceable[] newArray(int size) {
                        return new MyParceable[size];
                    }
                };

        // recreate object from parcel
        private MyParceable(Parcel in) {
            mData = in.readInt();
        }
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = null;
            Bundle bundle = new Bundle();

            switch (i){
                case 1:
                    bundle.putString("section", "world");
                    fragment = new NewsFragments();
                    fragment.setArguments(bundle);
                    displayFragment(fragment);
                    break;
                case 2:
                    bundle.putString("section", "sport");
                    fragment = new NewsFragments();
                    fragment.setArguments(bundle);
                    displayFragment(fragment);
                    break;
                case 3:
                    displayFragment(new HomeFragment());
                    break;
                case 4:
                    bundle.putString("section", "technology");
                    fragment = new NewsFragments();
                    fragment.setArguments(bundle);
                    break;
            }
            return null;
        }


        @Override
        public int getCount() {
            return 0;
        }
    }

}
