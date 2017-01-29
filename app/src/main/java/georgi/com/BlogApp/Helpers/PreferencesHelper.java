package georgi.com.BlogApp.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;

import georgi.com.BlogApp.POJO.User;

import static georgi.com.BlogApp.Configs.ServerURLs.DEFAULT_USER_IMG;

public class PreferencesHelper {

    private String TAG = getClass().getSimpleName();
    private Context context;
    private SharedPreferences sharedPreferences;

    public PreferencesHelper(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("app_data", Context.MODE_PRIVATE);
    }

    public void setCookie(String cookie) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("JSESSIONID", cookie);
        editor.apply();

    }

    public String getCookie(){
        return sharedPreferences.getString("JSESSIONID", null);
    }

    public void setCustomKeyValue(String key, String value) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();

    }

    public String getCustomKeyValue(String key) {
        return sharedPreferences.getString(key, null);
    }
}