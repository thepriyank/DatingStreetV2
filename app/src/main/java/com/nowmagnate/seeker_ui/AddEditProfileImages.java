package com.nowmagnate.seeker_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nowmagnate.seeker_ui.util.GradientStatusBar;
import com.squareup.picasso.Picasso;

public class AddEditProfileImages extends AppCompatActivity {

    //toolbar Views
    private TextView toolbarTitle;
    private ImageView toolbarBack;

    //Image Picker Layout
    private ImageView ProfileImage1;
    private ImageView ProfileImage2;
    private ImageView ProfileImage3;
    private ImageView ProfileImage4;
    private ImageView ProfileImage5;
    private ImageView ProfileImage6;

    private TextView ProfileTag1;
    private TextView ProfileTag2;
    private TextView ProfileTag3;
    private TextView ProfileTag4;
    private TextView ProfileTag5;
    private TextView ProfileTag6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_profile_images);

        toolbarBack = findViewById(R.id.back);
        toolbarTitle = findViewById(R.id.title);
        ProfileImage1 = findViewById(R.id.profile_image_1);
        ProfileImage2 = findViewById(R.id.profile_image_2);
        ProfileImage3 = findViewById(R.id.profile_image_3);
        ProfileImage4 = findViewById(R.id.profile_image_4);
        ProfileImage5 = findViewById(R.id.profile_image_5);
        ProfileImage6 = findViewById(R.id.profile_image_6);

        ProfileTag1 = findViewById(R.id.profile_image_1_tag);
        ProfileTag2 = findViewById(R.id.profile_image_2_tag);
        ProfileTag3 = findViewById(R.id.profile_image_3_tag);
        ProfileTag4 = findViewById(R.id.profile_image_4_tag);
        ProfileTag5 = findViewById(R.id.profile_image_5_tag);
        ProfileTag6 = findViewById(R.id.profile_image_6_tag);

        GradientStatusBar.setStatusBarGradiant(this);

        toolbarTitle.setText("EDIT PROFILE");

        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ProfileImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onImagePickerItemClick(ProfileImage1);
            }
        });

        ProfileImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onImagePickerItemClick(ProfileImage2);
            }
        });

        ProfileImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onImagePickerItemClick(ProfileImage3);
            }
        });

        ProfileImage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onImagePickerItemClick(ProfileImage4);
            }
        });

        ProfileImage5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onImagePickerItemClick(ProfileImage5);
            }
        });

        ProfileImage6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onImagePickerItemClick(ProfileImage6);
            }
        });
    }

    public void onImagePickerItemClick(ImageView i){
        Picasso.get().load(R.drawable.placeholder).fit().into(i);
    }
}
