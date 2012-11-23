package com.FBFriends.map;

import android.graphics.Bitmap;

public class UserData {
	private String userName;
	private String userId;
	private Bitmap userPicture;
	private String city;
	private String state;
	private String country;
	
	public void setUserName(String userName){
		this.userName=userName;
	}
	public String getUserName(){
		return this.userName;	
	}
	
	public void setUserId(String userId){
		this.userId=userId;
	}
	public String getUserId(){
		return this.userId;	
	}
	
	public void setUserPicture(Bitmap userPicture){
		this.userPicture=userPicture;
	}
	public Bitmap getUserPicture(){
		return this.userPicture;	
	}
	
	public void setCity(String city){
		this.city=city;
	}
	public String getCity(){
		return this.city;	
	}
	
	public void setState(String state){
		this.state=state;
	}
	public String getState(){
		return this.state;	
	}
	
	public void setCountry(String country){
		this.country=country;
	}
	public String getCountry(){
		return this.country;	
	}

}
