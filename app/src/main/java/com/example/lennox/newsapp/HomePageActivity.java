package com.example.lennox.newsapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.lennox.newsapp.fragments.HomeFragment;
import com.example.lennox.newsapp.fragments.NewsFragments;

public class HomePageActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public static final String API_KEY = "1aefe6a9-8256-4ed1-9705-078617ffba7b";
    public static final String NEWS_URL = "https://content.guardianapis.com/search?";
    private static final String TAG = HomePageActivity.class.getSimpleName();
    private MyParceable myClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        BottomNavigationView nav = findViewById(R.id.bottom_nav_menu);

        findViewById(R.id.iv_nav_drawer).setOnClickListener(this);
        findViewById(R.id.btn_next).setOnClickListener(this);
        findViewById(R.id.btn_previous).setOnClickListener(this);

        nav.setOnNavigationItemSelectedListener(this);
        displayFragment(new HomeFragment());
    }

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
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();

        switch (item.getItemId()) {
            case R.id.menu_home:
                fragment = new HomeFragment();
                break;
            case R.id.menu_world:
                bundle.putString("section", "world");
                fragment = new NewsFragments();
                fragment.setArguments(bundle);
                break;
                /*
            case R.id.menu_environment:
                fragment = new NewsFragments();
                break; */
            case R.id.menu_sports:
                bundle.putString("section", "sport");
                fragment = new NewsFragments();
                fragment.setArguments(bundle);
                break;
                /*
            case R.id.menu_tech:
                fragment = new NewsFragments();
                break; */
            default:
                break;
        }
        if (fragment != null) {
            displayFragment(fragment);
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_nav_drawer:
                Toast.makeText(this, "Drawer layout", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_next:
                Toast.makeText(this, "Next page", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_previous:
                Toast.makeText(this, "Previous page", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
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

}
