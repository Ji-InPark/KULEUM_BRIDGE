package com.KonDuckJoa.kuleumbridge.Animation;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.ImageView;

import com.KonDuckJoa.kuleumbridge.R;

// 애니메이션 담당 클래스
public class AnimationProgress extends Dialog {
    Context context;
    ImageView imageView;
    AnimationDrawable animationDrawable;

    public AnimationProgress(Context activity) {
        super(activity);
        context = activity;
        InitProgress();
    }
    
    public void InitProgress(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        setContentView(R.layout.loading_layout);

        imageView = findViewById(R.id.progresss);
        imageView.setBackgroundResource(R.drawable.loading_animation);
        animationDrawable = (AnimationDrawable)imageView.getBackground();

        animationDrawable.start();
    }
}
