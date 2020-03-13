package com.nowmagnate.seeker;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.Vector;

public class ProfileImagesAdapter extends PagerAdapter {

    Context context;
    private Vector<String> profileImages;

    public ProfileImagesAdapter(Context context, Vector<String> profileImages) {
        this.context = context;
        this.profileImages = profileImages;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
       ImageView itemImage = new ImageView(container.getContext());
        Glide.with(context).load(profileImages.get(position)).into(itemImage);
        container.addView(itemImage,0);
//        itemImage.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View view) {
//               Intent fullimgIntent = new Intent(context, FullImage.class);
//               fullimgIntent.putExtra("imgurl",profileImages.get(position));
//               context.startActivity(fullimgIntent);
//           }
//       });

       return  itemImage;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView)object);
    }

    @Override
    public int getCount() {
        return profileImages.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }
}
