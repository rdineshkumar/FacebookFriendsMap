package com.FBFriends.map;

import java.util.ArrayList;

import android.R.drawable;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class CustomImageOverLayItem  extends OverlayItem{

	private Bitmap mDrawable;
	private ArrayList<UserData> userData;
	public CustomImageOverLayItem(GeoPoint point, String title, String snippet,Bitmap bitmap) {
		super(point, title, snippet);
		this.mDrawable=bitmap;
		
	}
	public CustomImageOverLayItem(GeoPoint point, String title, String snippet,Bitmap bitmap,ArrayList<UserData> userData) {
		super(point, title, snippet);
		this.mDrawable=bitmap;
		this.userData=userData;
		
	}
	
	public void setImageView(Bitmap drawable){
		this.mDrawable=drawable;
	}
	
	public Bitmap getImageView(){
		return mDrawable;
	}
	
	public void setUserData(ArrayList<UserData> userData){
		this.userData=userData;
	}
	public ArrayList<UserData> getUserData(){
		return this.userData;
	}

}
