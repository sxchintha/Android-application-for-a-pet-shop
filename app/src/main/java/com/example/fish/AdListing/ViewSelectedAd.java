package com.example.fish.AdListing;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.fish.R;
import com.example.fish.customer.CustomerBuy;
import com.example.fish.customer.ViewItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class ViewSelectedAd extends AppCompatActivity {

    TextView tv_title, tv_location, tv_contact, tv_price, tv_description, tv_date;
    String title, location, contact, price, description, date;
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    String userID, childRef = "Advertisement", adID;
    ProgressBar progressBar;
    ImageSlider imageSlider;
    List<SlideModel> imageList;
    Button ad_call, select_ad;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_selected_ad);
        progressBar = findViewById(R.id.progressBar3);
        progressBar.setVisibility(View.VISIBLE);

        Advertisement adDet = (Advertisement) getIntent().getSerializableExtra("AD");
        adID = adDet.getKey();
        userID = adDet.getUID();
        StorageReference getImages = storageReference.child(childRef).child(userID).child(adID);

        title = adDet.getTitle();
        location = adDet.getLocation();
        contact = adDet.getContact().toString();
        price = adDet.getPrice().toString();
        description = adDet.getDescription();
        date = adDet.getDate();

        tv_title = findViewById(R.id.tv_selected_ad_title2);
        tv_location = findViewById(R.id.tv_selected_ad_location);
        tv_contact = findViewById(R.id.tv_selected_ad_contact);
        tv_price = findViewById(R.id.tv_selected_ad_price);
        tv_description = findViewById(R.id.tv_selected_ad_description);
        imageSlider = findViewById(R.id.img_selected_ad_image);
        tv_date = findViewById(R.id.tv_selected_ad_date);
        ad_call = findViewById(R.id.btn_selected_ad_call);
        select_ad = findViewById(R.id.button3);

        tv_title.setText(title);
        tv_location.setText(location);
        tv_contact.setText("+94"+contact);
        tv_price.setText(price);
        tv_description.setText(description);
        tv_date.setText(date);

        // Set images to ImageSlider
        imageList=new ArrayList<>();
        getImages.child("MainImage").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (!uri.equals(Uri.EMPTY)) {
                    imageList.add(new SlideModel(uri.toString()));
                    imageSlider.setImageList(imageList, true);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

        });

        getImages.child("Image2").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (!uri.equals(Uri.EMPTY)) {
                    imageList.add(new SlideModel(uri.toString()));
                    imageSlider.setImageList(imageList, true);
                }
            }
        });

        getImages.child("Image3").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (!uri.equals(Uri.EMPTY)) {
                    imageList.add(new SlideModel(uri.toString()));
                    imageSlider.setImageList(imageList, true);
                }
            }
        });

        // Call button
        ad_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: +94" +  contact));
                startActivity(intent);
            }
        });

        // Select button
        select_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ViewItem.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("AD", adDet);

                view.getContext().startActivity(intent);
            }
        });

    }

    public void goBack(View view) {
        finish();
    }
}