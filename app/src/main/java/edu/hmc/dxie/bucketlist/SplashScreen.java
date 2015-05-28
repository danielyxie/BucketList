package edu.hmc.dxie.bucketlist;

/**
 * Created by kjanderson2 on 4/26/15.
 */

        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.os.Handler;
        import android.view.animation.Animation;
        import android.view.animation.AnimationUtils;
        import android.widget.TextView;

public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView mainText = (TextView) findViewById(R.id.text_splashText);
        Animation animFadeIn;
        //mainText.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.enter_from_left));
        animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.enter_from_left);
        mainText.startAnimation(animFadeIn);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, ListActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}
