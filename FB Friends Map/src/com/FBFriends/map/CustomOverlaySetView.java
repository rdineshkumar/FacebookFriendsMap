package com.FBFriends.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.customoverlay.mapviewballoons.BalloonOverlayView;

public class CustomOverlaySetView<Item extends OverlayItem> extends BalloonOverlayView<CustomImageOverLayItem> {

	private TextView LocTitle,LocSnippet;
	private ImageView LocImage,closeImg;
	
	public CustomOverlaySetView(Context context, int balloonBottomOffset) {
		super(context, balloonBottomOffset);
	}

	@Override
	protected void setupView(Context context, ViewGroup parent) {
		//super.setupView(context, parent);
		LayoutInflater layoutInflator=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE) ;
		final View view =layoutInflator.inflate(R.layout.balloon_overlay, parent);
		
		LocTitle=(TextView)view.findViewById(R.id.balloon_item_title);
		LocSnippet=(TextView)view.findViewById(R.id.balloon_item_snippet);
		closeImg =(ImageView)view.findViewById(R.id.balloon_close);
		LocImage = (ImageView)view.findViewById(R.id.locIndicatorImg);
		
		closeImg.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//Toast.makeText(getContext(), "U Click cloase", 0).show();
				view.setVisibility(View.GONE);
			}
		});
		
		
	}

	@Override
	public void setData(CustomImageOverLayItem item) {
		//super.setData(item);
		
		LocTitle.setText(""+item.getTitle());
		LocSnippet.setText(""+item.getSnippet());
		LocImage.setVisibility(View.VISIBLE);
		//LocImage.setImageDrawable(item.getImageView());
		LocImage.setImageBitmap(item.getImageView());
	}

	@Override
	protected void setBalloonData(CustomImageOverLayItem item, ViewGroup parent) {
		super.setBalloonData(item, parent);
	}

}
