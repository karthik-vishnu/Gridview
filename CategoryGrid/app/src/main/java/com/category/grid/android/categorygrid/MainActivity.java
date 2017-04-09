package com.category.grid.android.categorygrid;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* LinearLayout mainLayout = (LinearLayout)findViewById(R.id.main_layout);

        View view = getView(null,4, mainLayout);*/
     //   mainLayout.addView(view);

/*        View view1 = getView(null, 3, mainLayout);
        mainLayout.addView(view1);*/


        TagLayout tagLayout = (TagLayout) findViewById(R.id.tag_layout);
        LayoutInflater layoutInflater = getLayoutInflater();
        String tag;
        for (int i = 0; i <= 20; i++) {
            tag = "#tag" + i;
            View tagView = layoutInflater.inflate(R.layout.tag_layout, null, false);

            ImageView tagTextView = (ImageView) tagView.findViewById(R.id.tag_image);
            tagTextView.setBackgroundResource(R.drawable.five);
            tagLayout.addView(tagView);
        }
    }

    public View getView(View convertView, int limit, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.images_grid, parent, false);

        LinearLayout imagesGrid = (LinearLayout)view.findViewById(R.id.images_grid);

        View text = inflater.inflate(R.layout.grid_header, parent, false);
        parent.addView(text);

        for (int i=0; i<limit; i++) {
            if(i < 3) {
                View imageView = inflater.inflate(R.layout.image, imagesGrid, false);
                imagesGrid.addView(imageView);
            } else {
                parent.addView(view);
            }
        }
        return view;
    }

}
