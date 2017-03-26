package com.app.csubmobile.Volley;

// Created by captnemo on 3/18/2017.

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.app.csubmobile.R;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SlideShow  extends AppCompatActivity {
    Animation fadein, fadeout, zoomin;
    ViewFlipper viewFlipper;
    int[] images = {
            R.drawable.admin_east ,
            R.drawable.admin_west ,
            R.drawable.admin_west_a ,
            R.drawable.admin_west_b ,
            R.drawable.ampitheatre ,
            R.drawable.bicyclestation ,
            R.drawable.book_store ,
            R.drawable.busdevclass ,
            R.drawable.bus_dev_off ,
            R.drawable.cafe ,
            R.drawable.childcare ,
            R.drawable.childcare_b ,
            R.drawable.classroom_bduilding ,

            R.drawable.counselling ,
            R.drawable.disabilities_services ,
            R.drawable.dobry ,
            R.drawable.dobry_b ,
            R.drawable.donahoe_hall ,
            R.drawable.dore_theatre ,
            R.drawable.education ,
            R.drawable.educational_oppourtunity ,
            R.drawable.education_b ,
            R.drawable.entwood ,
            R.drawable.extended_university ,
            R.drawable.fab ,
            R.drawable.fab_b ,
            R.drawable.facilities ,
            R.drawable.facultytower ,
            R.drawable.faculty_tower ,
            R.drawable.faculty_tower_a ,
            R.drawable.finearts ,
            R.drawable.healthservices ,
            R.drawable.highschool_equivalency ,
            R.drawable.human_resource ,
            R.drawable.icarrdo ,
            R.drawable.kit_fox1  ,
            R.drawable.kit_fox2  ,
            R.drawable.kit_fox3  ,
            R.drawable.kit_fox4  ,
            R.drawable.kit_fox5  ,
            R.drawable.kit_fox6  ,
            R.drawable.laundry ,
            R.drawable.leadership_development ,
            R.drawable.lecture_building ,
            R.drawable.lecture_building_b ,
            R.drawable.library ,
            R.drawable.lorien ,
            R.drawable.lorien2 ,
            R.drawable.music_building ,
            R.drawable.new_construction ,
            R.drawable.numenor ,
            R.drawable.numenor_a ,
            R.drawable.numenor_b ,
            R.drawable.nursinged ,
            R.drawable.pe ,
            R.drawable.peets ,
            R.drawable.performingarts ,
            R.drawable.performingarts_b ,
            R.drawable.police ,
            R.drawable.pond_a ,
            R.drawable.pond_b ,
            R.drawable.pond_c ,
            R.drawable.president ,
            R.drawable.procurement ,
            R.drawable.repographics ,
            R.drawable.rivendell ,
            R.drawable.rohan ,
            R.drawable.satellite_control_plant ,
            R.drawable.sci_1 ,
            R.drawable.sci_2 ,
            R.drawable.sci_2b ,
            R.drawable.sci_3 ,
            R.drawable.sci_3b ,
            R.drawable.shipping ,
            R.drawable.studentrec ,
            R.drawable.studentservices ,
            R.drawable.student_union ,
            R.drawable.swimming_pool ,
            R.drawable.swimming_pool  ,
            R.drawable.telecommunications ,
            R.drawable.tennis ,
            R.drawable.tennis2 ,
            R.drawable.tennis_shower_locker ,
            R.drawable.university_advancement ,
            R.drawable.university_grill ,
            R.drawable.university_grill2 ,
            R.drawable.visual_arts ,
            R.drawable.visual_arts_a ,
            R.drawable.water_tower ,
            R.drawable.well_core ,
            R.drawable.writingresourcelab ,
            R.drawable.writing_labplus
    };
    String[] captions = {
            "Admin east" ,
            "Admin west" ,
            "admin west" ,
            "Admin west" ,
            "Ampitheatre" ,
            " Bicycle station " ,
            " Book store " ,
            " Business developement class " ,
            " Busines dev. office " ,
            " Cafe " ,
            " Childcare " ,
            " Childcare " ,
            " Classroom Building " ,

            " Counselling " ,
            " Disabilities Services " ,
            " Dobry " ,
            " Dobry " ,
            " Donahoe hall " ,
            " Dore theatre " ,
            " Education " ,
            " Educational oppourtunity " ,
            " Education " ,
            " Entwood " ,
            " Extended university " ,
            " Fab lab " ,
            " Fab lab" ,
            " Facilities " ,
            " Faculty tower " ,
            " Faculty tower " ,
            " Faculty tower " ,
            " Fine arts " ,
            " Health services " ,
            " Highschool equivalency " ,
            " Human resource " ,
            " icarrdo " ,
            " kit fox  " ,
            " kit fox  " ,
            " kit fox  " ,
            " kit fox  " ,
            " kit fox  " ,
            " kit fox  " ,
            " Laundry " ,
            " Leadership development " ,
            " Lecture building " ,
            " Lecture building " ,
            " Library " ,
            " Lorien " ,
            " Lorien " ,
            " Music building " ,
            " New construction " ,
            " Numenor " ,
            " Numenor " ,
            " Numenor " ,
            " Nursing Education " ,
            " P.E. " ,
            " Peets " ,
            " Performing Arts " ,
            " Performing Arts " ,
            " Police " ,
            " Pond " ,
            " Pond " ,
            " Pond " ,
            " President " ,
            " Procurement " ,
            " Repographics " ,
            " Rivendell " ,
            " Rohan " ,
            " satellite control plant " ,
            " Science 1 " ,
            " Science 2 " ,
            " Science 2 " ,
            " Science 3 " ,
            " Science 3 " ,
            " shipping " ,
            " student recreation " ,
            " student services " ,
            " student union " ,
            " swimming pool " ,
            " swimming pool  " ,
            " telecommunications " ,
            " tennis " ,
            " tennis " ,
            " tennis shower locker " ,
            " University advancement " ,
            " University grill " ,
            " University grill " ,
            " visual arts " ,
            " visual arts " ,
            " water tower " ,
            " well core " ,
            " writing resource lab " ,
            " writing lab plus"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        fadein = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadein.setInterpolator(new DecelerateInterpolator());
        //zoomin = AnimationUtils.loadAnimation(this, android.R.anim.a);
        fadeout = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        fadeout.setInterpolator(new AccelerateInterpolator());
        setContentView(R.layout.slideshow);
        new CountDownTimer(1500000, 5000) { // 5000 = 5 sec
            public void onTick(long millisUntilFinished) {
                ImageView pic = (ImageView) findViewById(R.id.ripple_view);
                TextView caption = (TextView) findViewById(R.id.slide_caption);
                int rnd = new Random().nextInt(images.length);
                pic.startAnimation(fadein);
                pic.setImageResource(images[rnd]);
                caption.startAnimation(fadein);
                caption.setText(captions[rnd]);
            }
            public void onFinish() {
                ImageView pic = (ImageView) findViewById(R.id.ripple_view);
                pic.startAnimation(fadeout);
            }
        }.start();

    }

    }



