package com.example.acer.pedometer_BMI;

/**
 * @author Gayathiri Shriram , Shravya Mahankali
 * @version 1.0
 * @since version 1.0
 * <p/>
 * purpose:
 * Home class is the Main class that depicts all the functionality of the application.
 * It shows the steps, duration, speed, distance, calories and frequency.
 * Result of the application is displayed in this class.
 */

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    /**
     * Display number of steps
     */
    private TextView textViewSteps;
    /**
     * Display the duration of workout.
     */
    private TextView duration;
    /**
     * Display the average speed during the workout.
     */
    private TextView speed;
    /**
     * Displays the total distance covered during the workout.
     */
    private TextView distance;
    /**
     * Displays the calories burnt during the workout.
     */
    private TextView calories;
    /**
     * Displays the number of steps taken per minute.
     */
    private TextView frequency;
    /**
     * To store the previous value of Y acceleration.
     */
    private float previousY;
    /**
     * To store the current value of Y acceleration.
     */
    private float currentY;
    /**
     * Store total number of steps.
     */
    private int numSteps;
    /**
     * The threshold value to compute whether a step has been taken or not.
     */
    private double threshold;
    /**
     * To get the accelerometer sensor reading.
     */
    private SensorManager sensorManager;
    /**
     * Get the User's weight
     */
    private double weight;
    /**
     * Get the User's height
     */
    private double height;
    /**
     * Get the User's age
     */
    private int age;
    /**
     * Get the User's gender
     */
    private String gender;
    /**
     * Get the total distance
     */
    private double totDist;
    /**
     * Toggle button to display start and stop.
     */
    private ToggleButton toggleButton;
    /**
     * Get the total time of the workout.
     */
    private long totTime;
    /**
     * To display the time elapsed during the workout.
     */
    private Chronometer chronometer;

    private CharSequence mTitle;
    /**
     * Initializes the layout and all the component and variables.
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        textViewSteps = (TextView) findViewById(R.id.textViewSteps);
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        duration = (TextView) findViewById(R.id.duration);
        chronometer=(Chronometer)findViewById(R.id.chronometer);
        distance=(TextView)findViewById(R.id.distance);
        speed=(TextView)findViewById(R.id.speed);
        calories=(TextView)findViewById(R.id.calories);
        frequency=(TextView)findViewById(R.id.frequency);
        gender="";
        /**
         * Get the user's information.
         */
        try {
            read("userData");
        } catch (IOException e) {
            e.printStackTrace();
        }

        previousY = 0;
        currentY = 0;
        numSteps = 0;
/**
 * Setting the threshold value to 9 which is ideal for walking.
 */
        threshold = 9;
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
     * The start method is invoked when the user clicks on the toggle button
     * @param V
     * @throws IOException
     */
    public void start(View V) throws IOException {
        /**
         * Checking whether the user has entered all his information before starting the app.
         */
        if(!gender.equals("")){
            boolean on = ((ToggleButton) V).isChecked();
            /**
             * When the user clicks on Start Workout button.
             */
            if (on) {
                clearAll();
                enableAccelerometerListening();
                chronometer.start();
            }
            /**
             * When the user clicks on stop workout.
             */
            else {
                write("workout");
                totTime=(SystemClock.elapsedRealtime() - chronometer.getBase())/1000;
                chronometer.stop();

                displayDistance();
                displaySpeed();
                displayCalories();
                displayFrequency();

            }}
        /**
         * If the user has not entered the required information.
         */
        else{
            Toast.makeText(getApplicationContext(),"Please Enter Data First",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Enabling the accelerometer sensor.
     */
    private void enableAccelerometerListening() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Displaying the number of steps a user has taken.
     */
    public void displaySteps(){
        textViewSteps.setText("STEPS" + '\n' + String.valueOf(numSteps));
    }
    /**
     *Displaying the distance the user has walked
     */
    public void displayDistance(){
        /**
         * If the user is Male the step length is different.
         */
        if(gender.equals("Male")){
            totDist=height*0.415*numSteps;
        }
        /**
         * If the user is female the step length is different
         */
        else{
            totDist=height*0.413*numSteps;
        }
        /**
         * Displaying the distance covered.
         */
        if(String.valueOf(totDist).length()>4)
            distance.setText("DISTANCE" + '\n' + String.valueOf(totDist).substring(0, 4) + " m");
        else distance.setText("DISTANCE" + '\n' + String.valueOf(totDist) + " m");
    }
    /**
     * This method displays the average speed of the user during the workout.
     */
    public void displaySpeed(){
        if(String.valueOf((totDist)/totTime).length()>4)
            speed.setText("SPEED"+'\n'+String.valueOf((totDist)/totTime).substring(0, 4)+" m/s");
        else speed.setText("SPEED"+'\n'+String.valueOf((totDist) / totTime)+" m/s");
    }
    /**
     *Displays the total calories burnt by the user throughout the workout.
     */
    public void displayCalories(){
        int heartRate=220-age;
        double cal=0.0;
        if(numSteps!=0){
            /**
             * The amount of calories burnt depends on user's weight, height, age and gender.
             */
            if(gender.equals("Male")){
                cal=((age*0.2017)-(weight*0.09036)+(heartRate*0.6309)-55.0969)*(totTime/(4.184*60));
            }
            else{
                cal=((age*0.074)-(weight*0.05741)+(heartRate*0.4472)-20.4022)*(totTime/(4.184*60));
            }}

        if(String.valueOf(cal).length()>4)
            calories.setText("CALORIES"+'\n'+String.valueOf(cal).substring(0, 4)+" cal");
        else calories.setText("CALORIES"+'\n'+String.valueOf(cal)+" cal");
    }

    /**
     * Displays the steps taken by the user per minute.
     */
    public void displayFrequency(){
        frequency.setText("FREQ."+'\n'+String.valueOf((numSteps*60)/totTime)+" steps/min");

    }

    /**
     * This method clears the display screen after the workout.
     */
    public void clearAll(){
        chronometer.setBase(SystemClock.elapsedRealtime());
        numSteps = 0;
        displaySteps();
        distance.setText("DISTANCE"+'\n'+"0 m");
        speed.setText("SPEED" + '\n' + "0 m/s");
        calories.setText("CALORIES" + '\n' + "0 cal");
        frequency.setText("FREQ." + '\n' + "0 steps/min");
    }

    /**
     * Inner class to get the accelerometer readings.
     */
    private SensorEventListener sensorEventListener = new SensorEventListener() {
        /**
         * This method gets the Y acceleration value to compute the no. of step.
         * @param event
         */
        @Override
        public void onSensorChanged(SensorEvent event) {
            /**
             * Variable to get the current Y acceleration
             */
            float y = event.values[1];
            currentY = y;
            /**
             * Comparing the previous value of acceleration with the current value.
             */
            if (Math.abs(currentY - previousY) > threshold) {
                numSteps++;
                displaySteps();
                displayDistance();
            }
            previousY = y;
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    /**
     * This method saves the number of steps taken by the user to a file.
     * @param file name of the file.
     * @throws IOException
     */
    public void write(String file) throws IOException {
        /**
         * To avoid the file doesn't exist exception
         */
        FileOutputStream f_test = openFileOutput(file, MODE_APPEND);
        f_test.close();
        /**
         * Getting all the previous workout values from the file.
         */
        FileInputStream fInp = openFileInput(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fInp));
        String m;
        StringBuffer s = new StringBuffer();
        while ((m = br.readLine()) != null) {
            s.append(m + '\n');
        }
        fInp.close();
        /**
         * Appending the current value to the beginning of the file.
         */
        FileOutputStream f = openFileOutput(file, MODE_PRIVATE);
        f.write((String.valueOf(numSteps) + '\n'+s).getBytes());
        f.close();
        Toast.makeText(getApplicationContext(), "Workout Saved", Toast.LENGTH_LONG).show();

    }

    /**
     * This method reads the user's information and initializes them to the appropriate variable.
     * @param file
     * @throws IOException
     */
    public void read(String file) throws IOException {
        FileInputStream fInp = openFileInput(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fInp));
        /**
         * variable to get all the values from the file.
         */
        String m;
        int num = 1;
        while ((m = br.readLine()) != null) {
            if (num == 1)
                weight = Double.parseDouble(m);
            if (num == 2)
                height = Double.parseDouble(m);
            if (num == 3)
                gender = m;
            if (num == 4)
                age = Integer.parseInt(m);
            num++;
        }
    }
}
