package com.romina.staffmanager.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.romina.staffmanager.R;
import com.romina.staffmanager.Utils;
import com.romina.staffmanager.fragment.AdminHomeFragment;
import com.romina.staffmanager.fragment.StaffsFragment;
import com.romina.staffmanager.fragment.TasksFragment;


public class AdminHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.home);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new AdminHomeFragment()).addToBackStack("admin_home_fragment").commit();
        getSupportActionBar().setTitle("Home");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        getSupportFragmentManager().popBackStack();
        switch (item.getItemId()){
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new AdminHomeFragment()).addToBackStack("admin_home_fragment").commit();
                getSupportActionBar().setTitle("Home");
                break;
            case R.id.staffs:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new StaffsFragment()).addToBackStack("staffs_fragment").commit();
                getSupportActionBar().setTitle("Staffs");
                break;
            case R.id.tasks:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new TasksFragment()).addToBackStack("tasks_fragment").commit();
                getSupportActionBar().setTitle("Tasks");
                break;
            case R.id.nav_logout:
                SharedPreferences sharedPreferences=getSharedPreferences(Utils.SHARED_PREFERENCE,
                        MODE_PRIVATE);
                sharedPreferences.edit()
                        .remove(Utils.FIRST_NAME)
                        .remove(Utils.LAST_NAME)
                        .remove(Utils.USERNAME)
                        .remove(Utils.USER_TYPE)
                        .remove(Utils.POSITION)
                        .apply();
                Intent intent=new Intent(AdminHomeActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
