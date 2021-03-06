package com.tripper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tripper.db.entities.Day;
import com.tripper.db.entities.Trip;
import com.tripper.db.relationships.TripWithDaysAndDaySegments;



/* This class is the layout of the trip overview page with the bottomnavigation*/
public class TripOverview extends AppCompatActivity{
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tripoverview_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.trip_overview_home_button:
                Intent intent = new Intent(this, HomePage.class);
                this.startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);  //custom theme for app
        setContentView(R.layout.activity_trip_overview);
        BottomNavigationView bottomNav = findViewById(R.id.tripoverview_bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_map);
        }

        if(getIntent().getIntExtra("fragment_id", 123) == 2){
            bottomNav.setSelectedItemId(R.id.nav_diary);
        }
    }
    //This method is to set up the bottom navigation function
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener(){
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()){
                        case R.id.nav_map:
                            selectedFragment = new OverviewMapFragment();  // needs information to construct
                            selectedFragment.setArguments(getIntent().getExtras());
                            break;
                        case R.id.nav_plan:
                            selectedFragment = new OverviewPlanFragment();
                            selectedFragment.setArguments(getIntent().getExtras());
                            break;
                        case R.id.nav_diary:
                            selectedFragment = new OverviewDiaryFragment();
                            selectedFragment.setArguments(getIntent().getExtras());
                            break;


                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
             };
}
