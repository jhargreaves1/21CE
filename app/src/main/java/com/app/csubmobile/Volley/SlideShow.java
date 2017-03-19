package com.app.csubmobile.Volley;

/**
 * Created by captnemo on 3/18/2017.
 */
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import com.app.csubmobile.R;

public class SlideShow  extends AppCompatActivity {
    Animation fadein, fadeout;
    ViewFlipper viewFlipper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slideshow);
        viewFlipper = (ViewFlipper) this.findViewById(R.id.bckgrndViewFlipper1);
        fadein = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in);
        fadeout = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out);
        viewFlipper.setInAnimation(fadein);
        viewFlipper.setOutAnimation(fadeout);
//sets auto flipping
        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(2000);
        viewFlipper.startFlipping();
    }
}