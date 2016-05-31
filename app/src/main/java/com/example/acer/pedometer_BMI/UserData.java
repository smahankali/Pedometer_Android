package com.example.acer.pedometer_BMI;

/**
 * @author Gayathiri Shriram , Shravya Mahankali
 * @version 1.0
 * @since version 1.0
 * <p/>
 * purpose:
 * User data class is used to get the data from the user and calculate the BMI.
 * Details like height, weight, age and gender is received as input from the user.
 * result is displayed on the screen.
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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;

public class UserData extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    /**
     * To provide the gender options to the user.
     */
    private Spinner gender;
    /**
     *To provide the gender options to the user
     */
    private String[] state = { "Male", "Female"};
    /**
     * Variable to store the gender
     */
    private String gen;
    /**
     * Variable to store the height
     */
    private double height;
    /**
     * Variable to store the weight
     */
    private double weight;
    /**
     * Variable to store the age
     */
    private int age;
    /**
     * To get the height from the user
     */
    private EditText ht;
    /**
     * To get the weight from the user
     */
    private EditText wt;
    /**
     * To get the age from the user
     */
    private EditText ag;
    /**
     * To display the BMI to the user
     */
    private TextView BMI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        wt=(EditText)findViewById(R.id.weight);
        ht=(EditText)findViewById(R.id.height);
        ag=(EditText)findViewById(R.id.age);
        BMI=(TextView)findViewById(R.id.BMI);

        setSupportActionBar(toolbar);
        gender=(Spinner) findViewById(R.id.spinner);
        /**
         * initializing the drop down option
         */
        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, state);

        adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter_state);
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
        getMenuInflater().inflate(R.menu.home, menu);
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
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
     * This method is invoked when the ok button is pressed by the user
     * @param v
     * @throws IOException
     */
    public void getValues(View v) throws IOException {
        if(String.valueOf(wt.getText()).equals("")||String.valueOf(ht.getText()).equals("")||String.valueOf(ag.getText()).equals("")){
            Toast.makeText(getApplicationContext(), "PLEASE ENTER ALL THE FIELDS", Toast.LENGTH_LONG).show();
        }
        /**
         * Displays the BMI and writes the user data to a file.
         */
        else{
            weight=Double.parseDouble(String.valueOf(wt.getText()));
            height=Double.parseDouble(String.valueOf(ht.getText()));
            age=Integer.parseInt(String.valueOf(ag.getText()));
            gen = (String) gender.getSelectedItem();
            write("userData");
            /**
             * Displays the BMI to the user
             */
            BMI.setText("" + weight / (height * height));
            if(weight/(height*height)<18.5)
                Toast.makeText(getApplicationContext(),"You are under weight", Toast.LENGTH_LONG).show();
            else if((weight/(height*height)>=18.5)&&(weight/(height*height)<=24.9))
                Toast.makeText(getApplicationContext(),"You have a normal weight", Toast.LENGTH_LONG).show();
            else if((weight/(height*height)>=25.0))
                Toast.makeText(getApplicationContext(),"You are overweight", Toast.LENGTH_LONG).show();


        }}

    /**
     * This method writes the user data to the file
     * @param file
     * @throws IOException
     */
    public void write(String file) throws IOException {
        FileOutputStream f= openFileOutput(file,MODE_PRIVATE);
        f.write((String.valueOf(weight)+'\n'+String.valueOf(height)+'\n'+gen+'\n'+String.valueOf(age)).getBytes());
        f.close();
    }


}
