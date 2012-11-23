package com.FBFriends.map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.customoverlay.mapviewballoons.BalloonOverlayView;
import com.google.android.maps.OverlayItem;

public class MultiCustomOverlaySetView<Item extends OverlayItem> extends BalloonOverlayView<CustomImageOverLayItem> {
	
	private TextView LocTitle,LocSnippet;
	private ImageView LocImage,closeImg;
	LinearLayout outerLayout;
	View view;
	Context mContext;
	public MultiCustomOverlaySetView(Context context, int balloonBottomOffset) {
		super(context, balloonBottomOffset);
		this.mContext=context;
	}

	@Override
	protected void setupView(Context context, ViewGroup parent) {
		//super.setupView(context, parent);
		LayoutInflater layoutInflator=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE) ;
		view =layoutInflator.inflate(R.layout.multi_ballon_overlay, parent);
		outerLayout=(LinearLayout)view.findViewById(R.id.ballon_outer_layout);
		closeImg =(ImageView)view.findViewById(R.id.balloon_close);
		LocSnippet=(TextView)view.findViewById(R.id.balloon_item_snippet);

		
		
		closeImg.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				//Toast.makeText(getContext(), "U Click cloase", 0).show();
				outerLayout.removeAllViews();
				view.setVisibility(View.GONE);
			}
		});
				
	}

	@Override
	public void setData(CustomImageOverLayItem item) {
		//super.setData(item);
		outerLayout.removeAllViews();
		LocSnippet.setText(""+item.getSnippet());
		for(UserData userData : item.getUserData()){
			LayoutInflater layoutInflator=(LayoutInflater)mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE) ;
			View v =layoutInflator.inflate(R.layout.multi_pane_view, null);
			
			LocTitle=(TextView)v.findViewById(R.id.balloon_item_title);
			//LocSnippet=(TextView)v.findViewById(R.id.balloon_item_snippet);
			LocImage = (ImageView)v.findViewById(R.id.locIndicatorImg);
			
			LocTitle.setText(""+userData.getUserName());
			//LocSnippet.setText(""+item.getSnippet());
			LocImage.setVisibility(View.VISIBLE);
			//LocImage.setImageDrawable(item.getImageView());
			LocImage.setImageBitmap(userData.getUserPicture());
			outerLayout.addView(v);
			
		}
				
	}

	@Override
	protected void setBalloonData(CustomImageOverLayItem item, ViewGroup parent) {
		super.setBalloonData(item, parent);		
	} 

}
