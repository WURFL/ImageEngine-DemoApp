package com.scientiamobile.imageenginedemo;

import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import okhttp3.OkHttpClient;

public class ImageEngineDemo extends AppCompatActivity {

    final static String BASE_IMAGE_ENGINE_URL = "http://try.imgeng.in";
    final static String DEFAULT_IMAGE_URL = "www.scientiamobile.com/page/wp-content/uploads/2017/02/HomePage-Demo-Woman-iPad-Redov3.png";

    ImageView v;
    Picasso picasso;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_engine_demo);

        // we detect the display width in pixel and set it as default value for the input text field
        TextView tv = (TextView) findViewById(R.id.editText);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        tv.setText(""+ size.x);


        v = (ImageView) findViewById(R.id.imageView);


        // You need the following two lines of code only if you want to override the default Android user agent with your custom version.
        // If you don't need to override user-agent you can just use a plain and simple Picasso.with(getApplicationContext()).load(url) call to get your image
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new UserAgentInterceptor()).build();
        picasso = new Picasso.Builder(this).downloader(new OkHttp3Downloader(client)).build();

        // We use plain ImageEngine call first, and let it detect the best size
        picasso.load(BASE_IMAGE_ENGINE_URL + "/https://"+ DEFAULT_IMAGE_URL).into(v);


        // The use a text field to enter the dimension of an image we want to use.
        // The send button
        Button b = (Button) findViewById(R.id.button);
        //Set an event listener on the "OK" button.
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // The input text field
                TextView tv = (TextView) findViewById(R.id.editText);
                ImageView iv = (ImageView) findViewById(R.id.imageView);

                String w = "";
                if (!"".equals(tv.getText())){
                    w = tv.getText().toString();
                }

                String url = BASE_IMAGE_ENGINE_URL;
                int networkType = getNetworkType();
                // If we are under a slow network, we request an image compression of 50
                if(networkType == TelephonyManager.NETWORK_TYPE_GPRS || networkType == TelephonyManager.NETWORK_TYPE_EDGE){
                    url += "/w_"+ w + "/cmpr_50/https://" + DEFAULT_IMAGE_URL;
                }
                else {
                     url += "/w_"+ w +"/https://" + DEFAULT_IMAGE_URL;
                }


                picasso.with(getApplicationContext()).load(url).into(iv);
            }
        });


    }

    /*
     * Detects the network subtype.
     * Subtype can be GPRS, EDGE, etc., while type is MOBILE/WIFI/WIMAX etc.
     */
    int getNetworkType(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
        if(ni != null){
            return ni.getSubtype();
        }
        return -1;
    }
}
