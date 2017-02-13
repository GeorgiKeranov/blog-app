package georgi.com.BlogApp.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import georgi.com.BlogApp.Threads.Security.CheckAuthentication;

// This activity is the starting point of the application.
public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Thread that checks if user is authenticated with the server.
        // And starts new activity depending on that if the user is authenticated.
        CheckAuthentication checkAuthentication = new CheckAuthentication(this);
        checkAuthentication.execute();
    }
}
