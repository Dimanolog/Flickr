

package com.github.dimanolog.flickr.util;


import android.util.Log;
import com.github.dimanolog.flickr.BuildConfig;

public class LogUtil {

  public static  void  message(int priority, String tag, String message){
       if (BuildConfig.DEBUG) {
           Log.println(priority,tag,message);
       }
   }
}
