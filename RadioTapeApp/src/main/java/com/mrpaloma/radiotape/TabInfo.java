package com.mrpaloma.radiotape;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by MicheleMaccini on 04/02/2015.
 */
public class TabInfo extends Fragment {

    ImageView imgRadioInfo = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View oFragmentView = inflater.inflate(R.layout.tab_info, container, false);

        //String appName = getResources().getString(R.string.app_name);
        String text = getResources().getString(R.string.txtInfoApp);
        //text += " <a href='http://www.radiotape.net/'>" + appName + "</a><br/>";
        //text += " <a href='https://twitter.com/radiotapenet/'>Twitter</a><br/>";
        //text += " <a href='https://www.facebook.com/RadioTapeNet/'>Facebook</a>";

        imgRadioInfo = (ImageView)oFragmentView.findViewById(R.id.imageInfo);
        if (imgRadioInfo != null) { new ImageLoadTask(getString(R.string.urlImageInfo), imgRadioInfo).execute(); }

        TextView textView =(TextView)oFragmentView.findViewById(R.id.txtInfoApp);
        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(Html.fromHtml(text));

        ImageView imgT = (ImageView)oFragmentView.findViewById(R.id.iconTwitter);
        imgT.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://twitter.com/radiotapenet/"));
                startActivity(intent);
            }
        });

        ImageView imgF = (ImageView)oFragmentView.findViewById(R.id.iconFacebook);
        imgF.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.facebook.com/RadioTapeNet/"));
                startActivity(intent);
            }
        });

        ImageView imgR = (ImageView)oFragmentView.findViewById(R.id.iconRadioTape);
        imgR.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://www.radiotape.net/"));
                startActivity(intent);
            }
        });

        return oFragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}
