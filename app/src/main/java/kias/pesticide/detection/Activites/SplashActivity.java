package kias.pesticide.detection.Activites;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import kias.pesticide.detection.R;


public class SplashActivity extends AppCompatActivity implements Animation.AnimationListener {
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    //    ImageView i;
    LinearLayout i;

    Animation animMoveToTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        i = (LinearLayout) findViewById(R.id.linearLayout);



        animMoveToTop = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_animatin);
        animMoveToTop.setAnimationListener(this);


        verifyPermissions();
//        i.startAnimation(animMoveToTop);
    }

    private void verifyPermissions() {
        String[] permissions = {Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};


        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[1]) == PackageManager.PERMISSION_GRANTED
                ) {

            i.startAnimation(animMoveToTop);

        }

        else
        {

            ActivityCompat.requestPermissions(SplashActivity.this, permissions, 101);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        verifyPermissions();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        startActivity(new Intent(this, CameraActivity.class));
        SplashActivity.this.finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }


}
