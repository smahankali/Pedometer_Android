package com.example.acer.pedometer_BMI;

/**
 * @author Gayathiri Shriram , Shravya Mahankali
 * @version 1.0
 * @since version 1.0
 * <p/>
 * purpose:
 * This class gives the current location of the user using GPS.
 * GPSTracker class is used..
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
/**This class gives the current location of the user using GPS*/
public class GPSActivity extends Activity {

    Button btnShowLocation;

    /* GPSTracker class */
    GPSTracker gps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        btnShowLocation = (Button) findViewById(R.id.btnShowLocation);

        /* show location button click event */
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                /* create class object */
                gps = new GPSTracker(GPSActivity.this);

                /* check if GPS enabled */
                if(gps.canGetLocation()){

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    /* \n is for new line */
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                }else{
                    /**
                     * can't get location
                     * GPS or Network is not enabled
                     * Ask user to enable GPS/network in settings
                     */

                    gps.showSettingsAlert();
                }

            }
        });
    }

}