package com.FBFriends.map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

public class FriendsMap extends MapActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friends_map);
		MapView mapView=(MapView)findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		
		Drawable drawable = getResources().getDrawable(R.drawable.iamhere);
    	FbFriendOverlayItem<CustomImageOverLayItem> customItemizedOverLay=new FbFriendOverlayItem<CustomImageOverLayItem>(drawable, mapView);
    	
        List<Overlay> mapOverLay=mapView.getOverlays();

        
		Log.d("List Length","--->"+Utility.fbFriendsData.size()+"----->"+Utility.fbFriendsData.entrySet().size());
		Iterator<String> iterator=Utility.fbFriendsData.keySet().iterator();
		for(Entry<String, ArrayList<UserData>> mapEntry : Utility.fbFriendsData.entrySet()){
			
		    String key = mapEntry.getKey();
		    ArrayList<UserData> value =Utility.fbFriendsData.get(key);
		    Log.d("KEY","----->"+key);
		    double lat=Double.parseDouble(key.split("\\|")[0]);
		    double lon=Double.parseDouble(key.split("\\|")[1]);
		   
		    for(UserData userdata:value){
		    	
		    	Log.d("Friemd Name","--->"+userdata.getUserName());
		    	GeoPoint point1=new GeoPoint((int)(lat * 1E6),(int)(lon * 1E6));
	        	CustomImageOverLayItem customImageOverlay1=new CustomImageOverLayItem(point1, userdata.getUserName(), Utility.addressList.get(key),userdata.getUserPicture(),value);
	        	customItemizedOverLay.addCustomOverLay(customImageOverlay1);
		    }
		    //put key value to use
		}
		
		
		mapOverLay.add(customItemizedOverLay);
		
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
