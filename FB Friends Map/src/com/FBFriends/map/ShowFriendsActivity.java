package com.FBFriends.map;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import com.FBFriends.map.Facebook.AsyncFacebookRunner;
import com.FBFriends.map.Facebook.BaseRequestListener;
import com.FBFriends.map.Facebook.FacebookError;
import com.FBFriends.map.Facebook.SessionEvents;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ShowFriendsActivity extends Activity {

	RelativeLayout userDetail;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fbfreinds);
		
		TextView userName=(TextView)findViewById(R.id.userName);
		ImageView userImg=(ImageView)findViewById(R.id.userImage);
		userDetail=(RelativeLayout)findViewById(R.id.userDetail);
		
		
		for(UserData userData : Utility.userDataList){
			userName.setText(""+userData.getUserName());
			userImg.setImageBitmap(userData.getUserPicture());
		}
		
		Button showFriends=(Button)findViewById(R.id.showFriends);
		showFriends.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				if(Utility.mFacebook.isSessionValid()){
					startActivity(new Intent(ShowFriendsActivity.this,FbFriendsList.class));
				}else{
					showAlert();
				}
				
			}
		});
		
		Button showFriendsMap=(Button)findViewById(R.id.showFrindsMap);
		showFriendsMap.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(Utility.mFacebook.isSessionValid()){
					if( !Utility.FriendsDataWithLocation.isEmpty()){
						startActivity(new Intent(ShowFriendsActivity.this,FriendsMap.class));
						Log.d("loc Length",""+Utility.FriendsDataWithLocation.size());
					}else{
						AlertDialog.Builder builder=new AlertDialog.Builder(ShowFriendsActivity.this);
						builder.setTitle("Message");
						builder.setMessage("Friend's location not available");
						builder.setNegativeButton("Ok", null);
						
						AlertDialog dialog=builder.create();
						dialog.show();
						//Toast.makeText(ShowFriendsActivity.this, "Friend's location not available", 0).show();
					}
				}else{
					showAlert();
				}
			}
		});
		
		final Button logOut=(Button)findViewById(R.id.logoutBtn);
		logOut.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SessionEvents.onLogoutBegin();
                AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(Utility.mFacebook);
                asyncRunner.logout(ShowFriendsActivity.this, new BaseRequestListener() {
                	public void onComplete(String response, Object state) {
                		
                		Log.d("FB LOGOUT","FB logout successfully");
                		SessionEvents.onLogoutFinish();
                		ShowFriendsActivity.this.runOnUiThread(new Runnable() {
							public void run() {
								userDetail.setVisibility(LinearLayout.GONE);
								logOut.setVisibility(LinearLayout.INVISIBLE);
							}
						});
					}
					public void onMalformedURLException(MalformedURLException e, Object state) {
					}
					public void onIOException(IOException e, Object state) {
					}
					public void onFileNotFoundException(FileNotFoundException e, Object state) {
					}
					public void onFacebookError(FacebookError e, Object state) {
					}
				});
			}
		});
		
	}
	private void showAlert(){
		AlertDialog.Builder builder =new AlertDialog.Builder(this);
    	builder.setTitle("Message");
    	builder.setMessage("Please Login Facebook to continue App.");
    	builder.setNegativeButton("Ok", null);
    	
    	AlertDialog dialog=builder.create();
    	dialog.show();
	}
}
