package georgi.com.BlogApp.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHelper {

    private String TAG = getClass().getSimpleName();
    private Context context;

    public PreferencesHelper(Context context){
        this.context = context;
    }

    public void setCookie(String cookie) {

        SharedPreferences sharPref = context.getSharedPreferences("Cookies", 0);
        SharedPreferences.Editor editor = sharPref.edit();
        editor.putString("JSESSIONID", cookie);
        editor.apply();

    }

    public String getCookie(){
        SharedPreferences sharPref = context.getSharedPreferences("Cookies", 0);
        return sharPref.getString("JSESSIONID", null);
    }
}