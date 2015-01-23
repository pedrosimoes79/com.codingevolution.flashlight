package com.codingevolution.flashlight;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.codingevolution.flashlight.*;

public class MainWidgetReceiver extends BroadcastReceiver {

	public static boolean isLightOn = false;
	private static Camera camera;
	private static Parameters params;
	
	// Get the camera
    public static void getCamera(Context context) {
        if (camera == null) {
            try {
                camera = Camera.open();
                params = camera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Camera Error. Failed to Open. Error: ", e.getMessage());
            }
        }
        
        if (camera == null) {
			Toast.makeText(context, R.string.no_camera, Toast.LENGTH_SHORT)
					.show();
		} 
    }
     
    // Turning On flash
    public static void turnOnFlash(Context context) {
        
    	getCamera(context);
    	
    	if (!isLightOn) {
            if (camera == null || params == null) {
                return;
            }
            
            params = camera.getParameters();
            params.setFlashMode(Parameters.FLASH_MODE_TORCH);
            try {
				camera.setParameters(params);
				camera.startPreview();
				isLightOn = true;
			} catch (Exception e) {
				Toast.makeText(context, R.string.no_flash,
						Toast.LENGTH_SHORT).show();
			}
        }
    	
    	RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.widget);
		
		if (isLightOn) {
			views.setImageViewResource(R.id.btnSwitchWidget,
					R.drawable.btn_switch_on);
		} else {
			views.setImageViewResource(R.id.btnSwitchWidget, 
					R.drawable.btn_switch_off);
		}
		
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		appWidgetManager.updateAppWidget(new ComponentName(context,MainWidgetProvider.class), views);
    }
 
    // Turning Off flash
    public static void turnOffFlash(Context context) {
        
    	getCamera(context);
    	
    	if (isLightOn) {
            if (camera == null || params == null) {
                return;
            }
            
            params = camera.getParameters();
            params.setFlashMode(Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isLightOn = false;
            
            if (camera != null) {
                camera.release();
                camera = null;
            }
        }
    	
    	RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.widget);
		
		if (isLightOn) {
			views.setImageViewResource(R.id.btnSwitchWidget,
					R.drawable.btn_switch_on);
		} else {
			views.setImageViewResource(R.id.btnSwitchWidget, 
					R.drawable.btn_switch_off);
		}
		
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		appWidgetManager.updateAppWidget(new ComponentName(context,MainWidgetProvider.class), views);
    }
    
	@Override
	public void onReceive(Context context, Intent intent) {
		
		Log.d("MainWidgetReceiver.onReceive", "Received");
		
		getCamera(context);
		
		if (isLightOn) {
			turnOffFlash(context);
		} else {
			turnOnFlash(context);
		}
	}
}
