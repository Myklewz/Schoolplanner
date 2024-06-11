package com.example.splanner;

import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.splanner.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_todo, R.id.nav_calendar)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Get shared preferences for color settings
        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        int primaryColor = sharedPreferences.getInt("primary_color", ContextCompat.getColor(this, R.color.default_primary));

        // Set the toolbar color
        binding.appBarMain.toolbar.setBackgroundColor(primaryColor);

        // Update the drawer header background color
        updateDrawerHeaderBackground(primaryColor);
    }

    private void updateDrawerHeaderBackground(int primaryColor) {
        // Load the drawable
        GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(this, R.drawable.side_nav_bar);
        if (drawable != null) {
            // Change the gradient colors
            int[] colors = {primaryColor, primaryColor, primaryColor}; // Use primary color for all gradient stops
            drawable.setColors(colors);
            binding.navView.getHeaderView(0).setBackground(drawable);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
