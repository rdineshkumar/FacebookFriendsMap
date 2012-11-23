package com.FBFriends.map.Facebook;

import com.FBFriends.map.Facebook.Facebook.DialogListener;


/**
 * Skeleton base class for RequestListeners, providing default error 
 * handling. Applications should handle these error conditions.
 *
 */
public abstract class BaseDialogListener implements DialogListener {

    public void onFacebookError(FacebookError e) {
       // e.printStackTrace();
    }

    public void onError(DialogError e) {
       // e.printStackTrace();        
    }

    public void onCancel() {        
    }
    
}
