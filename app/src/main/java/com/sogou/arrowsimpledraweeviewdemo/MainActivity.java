package com.sogou.arrowsimpledraweeviewdemo;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.sogou.arrowsdview.ArrowSimpleDraweeView;

public class MainActivity extends Activity {

    ArrowSimpleDraweeView image_right_arrow;
    ArrowSimpleDraweeView image_left_arrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        image_right_arrow = (ArrowSimpleDraweeView) findViewById(R.id.image_right_arrow);
        image_left_arrow = (ArrowSimpleDraweeView)findViewById(R.id.image_left_arrow);

        Uri uri = new Uri.Builder().scheme("res").path(String.valueOf(R.drawable.baby)).build();
        int imageSuitWidth = 500;
        int imageSuitHeight = 500;
        image_left_arrow.showImage(uri,imageSuitWidth,imageSuitHeight);
        image_right_arrow.showImage(uri,imageSuitWidth,imageSuitHeight);
    }
}
