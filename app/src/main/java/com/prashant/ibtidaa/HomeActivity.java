package com.prashant.ibtidaa;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.prashant.ibtidaa.homeScreenFragments.BookmarkFragment;
import com.prashant.ibtidaa.homeScreenFragments.DashboardFragment;
import com.prashant.ibtidaa.homeScreenFragments.ExploreFragment;
import com.prashant.ibtidaa.homeScreenFragments.UserFragment;

import org.jetbrains.annotations.NotNull;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DashboardFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
            Fragment fragmentSelected = null;
            switch (item.getItemId()){
                case R.id.nav_home:
                    fragmentSelected = new DashboardFragment();
                    break;
                case R.id.nav_explore:
                    fragmentSelected = new ExploreFragment();
                    break;
                case R.id.nav_bookmark:
                    fragmentSelected = new BookmarkFragment();
                    break;
                case R.id.nav_user:
                    fragmentSelected = new UserFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragmentSelected).commit();
            return true;
        }
    };

 /*   @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exiting Ibtidaa")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Exit", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("Cancel", null)
                .show();
    }*/
}
