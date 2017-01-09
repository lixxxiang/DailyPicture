package com.example.administrator.pic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

public class guide3Activity extends Activity {
    private Button choosepic;
    private Button start;
    private TourGuide mTutorialHandler;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide3);

        choosepic = (Button) findViewById(R.id.choosepic);
        start = (Button) findViewById(R.id.start);

        Animation enterAnimation = new AlphaAnimation(0f, 1f);
        enterAnimation.setDuration(600);
        enterAnimation.setFillAfter(true);

        Animation exitAnimation = new AlphaAnimation(1f, 0f);
        exitAnimation.setDuration(600);
        exitAnimation.setFillAfter(true);

        mTutorialHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(new Pointer())
                .setToolTip(new ToolTip()
                        .setTitle("图片选择")
                        .setDescription("点击查看大图/左右滑动选择图片").setBackgroundColor(Color.parseColor("#919191"))
                        .setGravity(Gravity.TOP)
                )
                .setOverlay(new Overlay()
                        .setEnterAnimation(enterAnimation)
                        .setExitAnimation(exitAnimation)
                        .setStyle(Overlay.Style.NoHole)
                );

        choosepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTutorialHandler.cleanUp();
                mTutorialHandler.setToolTip(new ToolTip().setTitle("演示结束！").setBackgroundColor(Color.parseColor("#919191")).setDescription("点击进入主程序，请尽情体验使用！")
                        .setGravity(Gravity.TOP|Gravity.LEFT)).playOn(start);
            }
        });


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTutorialHandler.cleanUp();
                try {
                    Thread.sleep(100);
                    Intent intent = new Intent().setClass(guide3Activity.this, MainActivity.class);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        mTutorialHandler.playOn(choosepic);
    }
}