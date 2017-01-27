package georgi.com.BlogApp.Activities;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import georgi.com.BlogApp.Threads.Security.CheckAuthentication;

public class MainActivity extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        CheckAuthentication checkAuthentication = new CheckAuthentication(this);
        checkAuthentication.execute();
    }
}
