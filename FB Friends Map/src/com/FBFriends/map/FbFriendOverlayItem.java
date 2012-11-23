package com.FBFriends.map;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.maps.MapView;
import com.customoverlay.mapviewballoons.BalloonItemizedOverlay;
import com.customoverlay.mapviewballoons.BalloonOverlayView;


public class FbFriendOverlayItem<Item extends CustomImageOverLayItem> extends BalloonItemizedOverlay<CustomImageOverLayItem>{

	private ArrayList<CustomImageOverLayItem> mCustomOverLayItem=new ArrayList<CustomImageOverLayItem>();
	private MapView mMapView;
	private Context mContext;
	
	public FbFriendOverlayItem(Drawable defaultMarker, MapView mapView) {
		super(boundCenter( defaultMarker), mapView);
		mMapView=mapView;
		mContext=mapView.getContext();
	}
	
	public void addCustomOverLay(CustomImageOverLayItem cOverLayItem){
		mCustomOverLayItem.add(cOverLayItem);
		populate();
	}

	@Override
	protected CustomImageOverLayItem createItem(int i) {
		return mCustomOverLayItem.get(i);
	}

	@Override
	public int size() {
		return mCustomOverLayItem.size();
	}

	@Override
	protected boolean onBalloonTap(int index, CustomImageOverLayItem item) {
		return super.onBalloonTap(index, item);
	}

	@Override
	protected BalloonOverlayView<CustomImageOverLayItem> createBalloonOverlayView() {
		return new MultiCustomOverlaySetView<CustomImageOverLayItem>(getMapView().getContext(), getBalloonBottomOffset());
	}

}
