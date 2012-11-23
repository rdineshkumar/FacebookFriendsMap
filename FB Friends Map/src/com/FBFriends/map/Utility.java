package com.FBFriends.map;

import java.io.BufferedInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.sax.StartElementListener;
import android.util.Log;

import com.FBFriends.map.Facebook.AsyncFacebookRunner;
import com.FBFriends.map.Facebook.Facebook;


public class Utility {

	public static Facebook mFacebook;
	public static AsyncFacebookRunner mAsyncFbRunner;
	public static ArrayList<UserData> userDataList=new ArrayList<UserData>();
	public static ArrayList<UserData> FriendsData=new ArrayList<UserData>();
	public static ArrayList<UserData> FriendsDataWithLocation=new ArrayList<UserData>();
	public static HttpClient httpclient = null;
	
	public static LinkedHashMap<String, ArrayList<UserData>> fbFriendsData;
	public static LinkedHashMap<String, String> addressList;
	
	public void parseUserData(final String response,final String dataType,final FBFriendsMapActivity context){
		
		AsyncTask<String, Void, Void> parserTask=new AsyncTask<String, Void, Void>(){
			@Override
			protected Void doInBackground(String... params) {
				
				if(dataType.equals("user")){
					userDataList.clear();
				}else{
					FriendsData.clear();
					FriendsDataWithLocation.clear();
					fbFriendsData=new LinkedHashMap<String, ArrayList<UserData>>();
			        addressList=new LinkedHashMap<String, String>();
				}
				
				Log.d("ME",""+response);
				JSONArray jsonArray;
				UserData userData;
				try {
					
					jsonArray = new JSONArray(response);
					for(int index=0 ; index<jsonArray.length() ; index++){
						
						userData=new UserData();
						userData.setUserId(jsonArray.getJSONObject(index).getString("uid"));
						userData.setUserName(jsonArray.getJSONObject(index).getString("name"));
						userData.setUserPicture(getBitmap(jsonArray.getJSONObject(index).getString("pic_square")));
						
						String homeTown=jsonArray.getJSONObject(index).getString("hometown_location");
						String currentLocation=jsonArray.getJSONObject(index).getString("current_location");

						
						if((currentLocation != null )&& (!currentLocation .equals("null"))){
							JSONObject jsonObj=new JSONObject(currentLocation);
							
							userData.setCity(jsonObj.getString("city"));
							userData.setState(jsonObj.getString("state"));
							userData.setCountry(jsonObj.getString("country"));
							
							if( !dataType.equals("user")){
								FriendsDataWithLocation.add(userData);
								setFbFriendsLocation(context,userData);
							}
							
						}else{ 
							if((homeTown != null )&& (!homeTown .equals("null"))){
									JSONObject jsonObj=new JSONObject(homeTown);
									userData.setCity(jsonObj.getString("city"));
									userData.setState(jsonObj.getString("state"));
									userData.setCountry(jsonObj.getString("country"));
									
									if( !dataType.equals("user")){
										FriendsDataWithLocation.add(userData);
										setFbFriendsLocation(context,userData);
									}
								}
						}
						//Log.d("userData","Name="+userName+" picUrl="+picUrl+" City="+city+" State="+State+" Country="+country);
						if(dataType.equals("user")){
							userDataList.add(userData);
						}else{
							FriendsData.add(userData);
						}
					}
									
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return null;
		}

			@Override
			protected void onPostExecute(Void result) {
				//super.onPostExecute(result);
				if(dataType.equals("Friends")){
					Intent intent=new Intent(context, ShowFriendsActivity.class);
					context.startActivity(intent);
					context.finish();
				}
			}};
		
		parserTask.execute(response);
		
				
	}
	
	public void setFbFriendsLocation(Context context,UserData friendsData)
	{	
			Log.d("Name","---->"+friendsData.getUserName());
			String address="";
			if(friendsData.getCity() != null && !friendsData.getCity().equals("null")){
				address=friendsData.getCity();
			}
			if(friendsData.getState()!= null && !friendsData.getState().equals("null")){
				address=address+","+friendsData.getState();
			}
			if(friendsData.getCountry() != null && !friendsData.getCountry().equals("null")){
				address=address+","+friendsData.getCountry();
			}
			
			
			Log.d("Address","--------->"+address);
			
			List<Address> foundGeocode = null;
	    	/* find the addresses  by using getFromLocationName() method with the given address*/
	    	try {
				foundGeocode = new Geocoder(context).getFromLocationName(address, 1);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	    	
	    	if(foundGeocode != null){
		    	if(!foundGeocode.isEmpty()){
			    	double lat=foundGeocode.get(0).getLatitude(); //getting latitude
			    	double lon=foundGeocode.get(0).getLongitude();//getting longitude
			    	
			    	Log.d(address,lat+"|"+lon);
			    	if(fbFriendsData.get(lat+"|"+lon) == null){
			    		ArrayList<UserData> tempUserData=new ArrayList<UserData>();
			    		tempUserData.add(friendsData);
			    		fbFriendsData.put(lat+"|"+lon, tempUserData);
			    		addressList.put(lat+"|"+lon, address);
			    	}else{
			    		ArrayList<UserData> tempUserData=fbFriendsData.get(lat+"|"+lon);
			    		//Log.d("user name",""+friendsData.getUserName());
			    		tempUserData.add(friendsData);
			    		fbFriendsData.put(lat+"|"+lon, tempUserData);
			    	}
		    	}
	    	}
	}
	 
	
	 private Bitmap getBitmap(String url) {
	        Bitmap bm = null;
	        try {
	            URL aURL = new URL(url);
	            URLConnection conn = aURL.openConnection();
	            conn.connect();
	            InputStream is = conn.getInputStream();
	            BufferedInputStream bis = new BufferedInputStream(is);
	            bm = BitmapFactory.decodeStream(new FlushedInputStream(is));
	            bis.close();
	            is.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            if (httpclient != null) {
	                try {
						((BufferedInputStream)httpclient).close();
					} catch (IOException e) {
						e.printStackTrace();
					}
	            }
	        }
	        return bm;
	    }
	 
	 private class FlushedInputStream extends FilterInputStream {
	        
		 public FlushedInputStream(InputStream inputStream) {
	            super(inputStream);
	        }

	        @Override
	        public long skip(long n) throws IOException {
	            long totalBytesSkipped = 0L;
	            while (totalBytesSkipped < n) {
	                long bytesSkipped = in.skip(n - totalBytesSkipped);
	                if (bytesSkipped == 0L) {
	                    int b = read();
	                    if (b < 0) {
	                        break; // we reached EOF
	                    } else {
	                        bytesSkipped = 1; // we read one byte
	                    }
	                }
	                totalBytesSkipped += bytesSkipped;
	            }
	            return totalBytesSkipped;
	        }
	    }
	 
}
