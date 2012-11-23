/*
 * Copyright 2010 Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.FBFriends.map.Facebook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import com.FBFriends.map.R;
import com.FBFriends.map.Facebook.BaseRequestListener;
import com.FBFriends.map.Facebook.SessionEvents.AuthListener;
import com.FBFriends.map.Facebook.SessionEvents.LogoutListener;
import com.FBFriends.map.Facebook.Facebook.DialogListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class LoginButton extends LinearLayout {
    
    private Facebook mFb;
    private Handler mHandler;
    private SessionListener mSessionListener = new SessionListener();
    private String[] mPermissions;
    private Activity mActivity;
    
    public LoginButton(Context context) {
        super(context);
    }
    
    public LoginButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public LoginButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
    }
    
   public void init(final Activity activity, final Facebook fb) {
    	init(activity, fb);
    }
    public void init(final Activity activity, final Facebook fb,int Resource) {
    	mActivity=activity;
    	mFb=fb;
    	mHandler=new Handler();
    	setBackgroundResource(fb.isSessionValid() ? R.drawable.logout :  R.drawable.login);

		drawableStateChanged();
		SessionEvents.addAuthListener(mSessionListener);
		SessionEvents.addLogoutListener(mSessionListener);
		//getSessionInfo();
		    	
    }
    public void init(final Activity activity, final Facebook fb, final String[] permissions, int Resource) 
    {
        mActivity = activity;
        mFb = fb;
        mPermissions = permissions;
        mHandler = new Handler();
        
        setBackgroundColor(Color.TRANSPARENT);
        //setAdjustViewBounds(true);
        setBackgroundResource(Resource);
        setBackgroundResource(fb.isSessionValid() ?
                         R.drawable.logout : 
                         R.drawable.login);
        
        drawableStateChanged();
        SessionEvents.addAuthListener(mSessionListener);
        SessionEvents.addLogoutListener(mSessionListener);
        setOnClickListener(new ButtonOnClickListener());
       // getSessionInfo();
    }
    



    private final class ButtonOnClickListener implements OnClickListener {
        
        public void onClick(View arg0) {
            if (mFb.isSessionValid()) {
                SessionEvents.onLogoutBegin();
                AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(mFb);
                asyncRunner.logout(getContext(), new LogoutRequestListener());
            } else {
                mFb.authorize(mActivity, mPermissions,new LoginDialogListener());
            }
        }
    }

    private void getSessionInfo() {
    	
    	if (mFb.isSessionValid()) {
            SessionEvents.onLogoutBegin();
            //AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(mFb);
            //asyncRunner.logout(getContext(), new LogoutRequestListener());
            Toast.makeText(getContext(), "**********Session is Valid", 0).show();
        } else {
            mFb.authorize(mActivity, mPermissions,new LoginDialogListener());
            Toast.makeText(getContext(), "**********Session is Not Valid", 0).show();
        }
	}

	/*private final class ButtonOnClickListener implements OnClickListener {
        
        public void onClick(View arg0) {
            if (mFb.isSessionValid()) {
                SessionEvents.onLogoutBegin();
                AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(mFb);
                asyncRunner.logout(getContext(), new LogoutRequestListener());
                Toast.makeText(getContext(), "Session is Valid", 0).show();
            } else {
                mFb.authorize(mActivity, mPermissions,new LoginDialogListener());
                Toast.makeText(getContext(), "Session is Not Valid", 0).show();
            }
        }
    }*/

    private final class LoginDialogListener implements DialogListener {
        public void onComplete(Bundle values) {
            SessionEvents.onLoginSuccess();
            Log.d("LoginSuccess", "");
           
        }

        public void onFacebookError(FacebookError error) {
            SessionEvents.onLoginError(error.getMessage());
        }
        
        public void onError(DialogError error) {
            SessionEvents.onLoginError(error.getMessage());
        }

        public void onCancel() {
            SessionEvents.onLoginError("Action Canceled");
        }
    }
    
    private class LogoutRequestListener extends BaseRequestListener {
        public void onComplete(String response, Object state) {
            // callback should be run in the original thread, 
            // not the background thread
            mHandler.post(new Runnable() {
                public void run() {
                    SessionEvents.onLogoutFinish();
                }
            });
        }

		public void onIOException(IOException e, Object state) {
			
		}

		public void onFileNotFoundException(FileNotFoundException e,
				Object state) {
			
		}

		public void onMalformedURLException(MalformedURLException e,
				Object state) {
			
		}

		public void onFacebookError(FacebookError e, Object state) {
			
		}
    }
    
    private class SessionListener implements AuthListener, LogoutListener {
        
        public void onAuthSucceed() {
            setBackgroundResource(R.drawable.logout);
            SessionStore.save(mFb, getContext());
        }

        public void onAuthFail(String error) {
        }
        
        public void onLogoutBegin() {           
        }
        
        public void onLogoutFinish() {
            SessionStore.clear(getContext());
            setBackgroundResource(R.drawable.login);
        }
    }
    
}
