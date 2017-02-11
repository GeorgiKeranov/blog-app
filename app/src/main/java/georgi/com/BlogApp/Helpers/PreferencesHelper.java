package georgi.com.BlogApp.Helpers;

import android.content.Context;
import android.content.SharedPreferences;


// This class is used for less boilerplate code and easier access to SharedPreferences.
public class PreferencesHelper {

    private String TAG = getClass().getSimpleName();
    private Context context;
    private SharedPreferences sharedPreferences;

    public PreferencesHelper(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("app_data", Context.MODE_PRIVATE);
    }

    // This method is used to save cookie in shared preferences.
    public void setCookie(String cookie) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Cookie", cookie);
        editor.apply();

    }

    // This method is used to get the saved cookie in shared preferences.
    // If there is not a cookie it is returning "NO-COOKIE".
    public String getCookie(){
        return sharedPreferences.getString("Cookie", "NO-COOKIE");
    }


    // This method is used to save custom key values.
    public void setCustomKeyValue(String key, String value) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();

    }

    // This mthod is used to get custom value by key.
    public String getCustomKeyValue(String key) {
        return sharedPreferences.getString(key, null);
    }
}