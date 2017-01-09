package com.example.administrator.pic;

import android.app.Application;
import com.example.administrator.pic.baidu.location.service.LocationService;


public class PicApplication extends Application {
	public LocationService locationService;
	@Override
	public void onCreate() {
		super.onCreate();
		locationService = new LocationService(getApplicationContext());
	}
}