package com.codingevolution.flashlight;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.widget.ImageButton;
 
public class MainActivity extends Activity {
 
    ImageButton btnSwitch;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // flash switch button
        btnSwitch = (ImageButton) findViewById(R.id.btnSwitch);
 
        // get the camera
        MainWidgetReceiver.getCamera(this.getBaseContext());
         
        // displaying button image
        toggleButtonImage();
         
        // Switch button click event to toggle flash on/off
        btnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainWidgetReceiver.isLightOn) {
                    // turn off flash
                    turnOffFlash();
                } else {
                    // turn on flash
                    turnOnFlash();
                }
            }
        });
        
        lockOrientation();
    }
 
	@SuppressWarnings("deprecation")
	private void toggleBackground() {
    	View v = findViewById(R.id.main_layout);
    	Display display = getWindowManager().getDefaultDisplay(); 
    	int width = display.getWidth();  // deprecated
    	int height = display.getHeight();  // deprecated
    	
        if(width > 0) {
            GradientDrawable g = new GradientDrawable(Orientation.TL_BR, new int[] { MainWidgetReceiver.isLightOn ? Color.rgb(119, 119, 102) : Color.rgb(0, 0, 0), Color.rgb(0, 0, 0) });
            g.setGradientType(GradientDrawable.RADIAL_GRADIENT);
            g.setGradientRadius(width < height ? width : height);
            
            v.setBackgroundDrawable(g); // deprecated
        }
    }
     
    // Turning On flash
    private void turnOnFlash() {
    	if (!MainWidgetReceiver.isLightOn) {
            MainWidgetReceiver.turnOnFlash(this.getBaseContext());
             
            // changing button/switch image
            toggleButtonImage();
        }
        
        toggleBackground();
    }
 
    // Turning Off flash
    private void turnOffFlash() {
    	if (MainWidgetReceiver.isLightOn) {
    		MainWidgetReceiver.turnOffFlash(this.getBaseContext());
             
            // changing button/switch image
            toggleButtonImage();
        }
        
        toggleBackground();
    }
     
    /*
     * Toggle switch button images
     * changing image states to on / off
     * */
    private void toggleButtonImage(){
        if(MainWidgetReceiver.isLightOn){
            btnSwitch.setImageResource(R.drawable.btn_switch_on);
        }else{
            btnSwitch.setImageResource(R.drawable.btn_switch_off);
        }
    }
 
    @Override
    protected void onResume() {
        super.onResume();
         
        // on resume turn on the flash
        if(MainWidgetReceiver.isLightOn)
            turnOnFlash();
    }
 
    @Override
    protected void onStart() {
        super.onStart();
         
        turnOnFlash();
        
        // on starting the app get the camera params
        toggleButtonImage();
        toggleBackground();
    }
    
    protected void lockOrientation() {
    	Display display = getWindowManager().getDefaultDisplay(); 
    	int rotation = display.getRotation();
        int tempOrientation = this.getResources().getConfiguration().orientation;
        int orientation = 0;
        switch(tempOrientation)
        {
        case Configuration.ORIENTATION_LANDSCAPE:
            if(rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90)
                orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            else
                orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
            break;
        case Configuration.ORIENTATION_PORTRAIT:
            if(rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_270)
                orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            else
                orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
        }
        this.setRequestedOrientation(orientation);
    }
}