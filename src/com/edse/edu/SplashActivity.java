package com.edse.edu;
 
import java.util.Timer;
import java.util.TimerTask;
 

 
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
 
public class SplashActivity extends Activity
{
 
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_splash);
 
        TimerTask wait = new TimerTask()
        {
 
            @Override
            public void run()
            {
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
 
        };
        
        Timer timer = new Timer();
        timer.schedule(wait, 3000);
    }
 
}