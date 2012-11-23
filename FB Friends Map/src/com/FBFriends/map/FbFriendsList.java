package com.FBFriends.map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FbFriendsList extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fb_friends_list);
		Button back=(Button)findViewById(R.id.backButton);
		back.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				finish();
			}
		});
		ListView listView=(ListView)findViewById(R.id.listView1);
		listView.setAdapter(new FbFriendAdapter(this));
		
	}
	
private class FbFriendAdapter extends BaseAdapter{
	LayoutInflater inflater;
	public  FbFriendAdapter(Context context){
		this.inflater=(LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return Utility.FriendsData.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View fbView=convertView;
		if(convertView == null){
			fbView=inflater.inflate(R.layout.fblist, null);
			ViewHolder viewHolder=new ViewHolder();
			viewHolder.friendImage=(ImageView)fbView.findViewById(R.id.friendImage);
			viewHolder.friendName=(TextView)fbView.findViewById(R.id.friendName);
			viewHolder.friendLocation=(TextView)fbView.findViewById(R.id.friendLocation);
			fbView.setTag(viewHolder);		
			
		}
		Log.d("CHECK",""+Utility.FriendsData.get(position).getUserName());
		ViewHolder viewHolder=(ViewHolder)fbView.getTag();
		try{
			
			viewHolder.friendImage.setImageBitmap(Utility.FriendsData.get(position).getUserPicture());
			viewHolder.friendName.setText(""+Utility.FriendsData.get(position).getUserName());
			if(Utility.FriendsData.get(position).getCity() != null){
				if(!Utility.FriendsData.get(position).getCity().equals("null")){// && Utility.FriendsData.get(position).getCity() != null){
					String address=Utility.FriendsData.get(position).getCity()+","+
							Utility.FriendsData.get(position).getState()+","+
							Utility.FriendsData.get(position).getCountry();
					viewHolder.friendLocation.setText(""+address);
				}
				viewHolder.friendLocation.setTextColor(getResources().getColor(R.color.darkgreen));

				
			}else{
				viewHolder.friendLocation.setText("Location not available");
				viewHolder.friendLocation.setTextColor(getResources().getColor(R.color.red));
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return fbView;
	}
	
}

private class ViewHolder{
	ImageView friendImage;
	TextView friendName;
	TextView friendLocation;
}
}
