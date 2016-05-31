package com.example.acer.pedometer_BMI;

/**
 * @author Gayathiri Shriram , Shravya Mahankali
 * @version 1.0
 * @since version 1.0
 * <p/>
 * purpose:
 * User progress is another class used to show the progress of the user
 * through a graphical representation.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserProgress extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    /* Index to the data array.
    */
    private int i;
    /**
     * Variable to save the number of steps pf user over the past 15 workouts.
     */
    private int data[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_progress);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        /**
         * initializing the value of index to 14.
         */
        i=14;
        /**
         * initializing the data array.
         */
        data=new int[i+1];
        /**
         * reading the workout file.
         */
        try {
            read("workout");
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         * Declaring the GraphView object to chart the user's progress
         */
        GraphView graph = (GraphView) findViewById(R.id.graph);
        /**
         * Plotting a linear graph of the number of workouts to the number of steps.
         */
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, data[0]),
                new DataPoint(1, data[1]),
                new DataPoint(2, data[2]),
                new DataPoint(3, data[3]),
                new DataPoint(4, data[4]),
                new DataPoint(5, data[5]),
                new DataPoint(6, data[6]),
                new DataPoint(7, data[7]),
                new DataPoint(8, data[8]),
                new DataPoint(9, data[9]),
                new DataPoint(10, data[10]),
                new DataPoint(11, data[11]),
                new DataPoint(12, data[12]),
                new DataPoint(13, data[13]),
                new DataPoint(14, data[14])
        });
        graph.addSeries(series);
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
        /* Inflate the menu; this adds items to the action bar if it is present. */
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        Handle action bar item clicks here. The action bar will
        automatically handle clicks on the Home/Up button, so long
        as you specify a parent activity in AndroidManifest.xml.
        */
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
        /* Handle navigation view item clicks here. */
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            /* Handle the camera action */
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(this, UserData.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(this, UserProgress.class);
            startActivity(intent);
            return true;

        }
        else if (id == R.id.map) {
            Intent intent = new Intent(this, GPSActivity.class);
            startActivity(intent);
            return true;

        }
        else if (id == R.id.gps) {
            Intent intent = new Intent(this, GeoLocator.class);
            startActivity(intent);
            return true;

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /**
     * The read method reads the user data from the file and initializes it to the data array.
     * @param file Name of the file.
     * @throws IOException
     */
    public void read(String file) throws IOException {
        /**
         * To create a new file in case the file does not exist.
         */
        FileOutputStream f_test = openFileOutput(file, MODE_APPEND);
        f_test.close();
        /**
         * Reading from the file.
         */
        try {
            FileInputStream f=openFileInput(file);
            BufferedReader br=new BufferedReader(new InputStreamReader(f));
            StringBuffer s=new StringBuffer();
            String m;

            while(((m=br.readLine())!=null)&&(i>=0)){
                s.append(m+'\n');
                data[i]=Integer.parseInt(m);
                i--;
            }

            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
