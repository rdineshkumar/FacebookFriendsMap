package com.FBFriends.map;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import com.FBFriends.map.Facebook.AsyncFacebookRunner;
import com.FBFriends.map.Facebook.BaseRequestListener;
import com.FBFriends.map.Facebook.Facebook;
import com.FBFriends.map.Facebook.FacebookError;
import com.FBFriends.map.Facebook.LoginButton;
import com.FBFriends.map.Facebook.SessionEvents;
import com.FBFriends.map.Facebook.SessionStore;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FBFriendsMapActivity extends Activity {
	
	
	String APP_ID="219012584794170";
    String[] permissions = { "offline_access", "publish_stream", "user_photos", "publish_checkins","photo_upload" ,"user_location"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Utility.mFacebook=new Facebook(APP_ID);
        Utility.mAsyncFbRunner=new AsyncFacebookRunner(Utility.mFacebook);
        
        final TextView progressText=(TextView)findViewById(R.id.progressText);
        progressText.setVisibility(LinearLayout.INVISIBLE);
        
        final LoginButton FbLoginBtn=(LoginButton)findViewById(R.id.fbLoginLutton);
        FbLoginBtn.init(this, Utility.mFacebook,permissions, R.drawable.login);
        
        SessionStore.restore(Utility.mFacebook, this);
		SessionEvents.addAuthListener(new SessionEvents.AuthListener() {
			
			public void onAuthSucceed() {
				Toast.makeText(FBFriendsMapActivity.this, "onAuthSucceed", 0).show();
				if(Utility.mFacebook.isSessionValid()){
		        	progressText.setVisibility(LinearLayout.VISIBLE);
		        	FbLoginBtn.setVisibility(LinearLayout.GONE);
		        	getFbUserData();
		        	getFbFriendsData();
		        	
		        }
				
			}
			public void onAuthFail(String error) {
				//Toast.makeText(FBFriendsMapActivity.this, "onAuthFail", 0).show();
			}
		});
		
		SessionEvents.addLogoutListener(new SessionEvents.LogoutListener(){

			public void onLogoutBegin() {
				
			}
			public void onLogoutFinish() {
				
			}});
        
        if(Utility.mFacebook.isSessionValid()){
        	
        	progressText.setVisibility(LinearLayout.VISIBLE);
        	FbLoginBtn.setVisibility(LinearLayout.GONE);
        	getFbUserData();
        	getFbFriendsData();
        	//getFbOnlineFriends();
        	
        }else{
        	AlertDialog.Builder builder =new AlertDialog.Builder(this);
        	builder.setTitle("Message");
        	builder.setMessage("Please Login Facebook to continue App.");
        	builder.setNegativeButton("Ok", null);
        	
        	AlertDialog dialog=builder.create();
        	dialog.show();
        
        	progressText.setVisibility(LinearLayout.GONE);
        	FbLoginBtn.setVisibility(LinearLayout.VISIBLE);
        }
    }

	private void getFbUserData() {
		String query = "SELECT uid, name, pic_square,birthday,hometown_location,current_location FROM user WHERE uid = me() ";
        Bundle params = new Bundle();
        params.putString("method", "fql.query");
        params.putString("query", query);
        
        Utility.mAsyncFbRunner.request(null, params, new FriendsRequestListener("user"));
	}
	private void getFbFriendsData(){
		String query = "select name,hometown_location , current_location, uid, pic_square ,birthday from user where uid in (select uid2 from friend where uid1=me()) order by name";
        Bundle params = new Bundle();
        params.putString("method", "fql.query");
        params.putString("query", query);
        Utility.mAsyncFbRunner.request(null, params, new FriendsRequestListener("Friends"));
	}
	
	private void getFbOnlineFriends(){
		String query="SELECT name,uid FROM user WHERE online_presence IN ('active', 'idle') AND uid IN ( SELECT uid2 FROM friend WHERE uid1 = me())";
		Bundle params = new Bundle();
	    params.putString("method", "fql.query");
	    params.putString("query", query);
	    Utility.mAsyncFbRunner.request(null, params, new FriendsRequestListener("online"));
	}
	
	private class FriendsRequestListener extends BaseRequestListener{
		String userType;
		public FriendsRequestListener(String userType){
			this.userType=userType;
		}

		public void onComplete(final String response, Object state) {
			FBFriendsMapActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					new Utility().parseUserData(response,userType,FBFriendsMapActivity.this);
					//new Utility().storeUserData(response);
				}
			});
		}
		public void onIOException(IOException e, Object state) {
		}

		public void onFileNotFoundException(FileNotFoundException e,Object state) {
		}

		public void onMalformedURLException(MalformedURLException e,Object state) {
		}

		public void onFacebookError(FacebookError e, Object state) {
		}
		
	}
	
}