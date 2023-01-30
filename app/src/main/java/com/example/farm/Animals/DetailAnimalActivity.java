package com.example.farm.Animals;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.farm.R;
import com.example.farm.converter.DataConverter;
import com.example.farm.database.entity.Animal;
import com.example.farm.database.view.AnimalViewModel;


public class DetailAnimalActivity extends AppCompatActivity {
    public static final String EXTRA_EDIT_ANIMAL_NAME = "pb.edu.pl.EDIT_ANIMAL_NAME";
    public static final String EXTRA_EDIT_ANIMAL_GENDER = "pb.edu.pl.EDIT_ANIMAL_GENDER";
    public static final String EXTRA_EDIT_ANIMAL_INDEX_ID = "pb.edu.pl.EDIT_ANIMAL_INDEX_ID";
    public static final CharSequence EXTRA_EDIT_ANIMAL_PHOTO = "pb.edu.pl.EDIT_ANIMAL_PHOTO";

    private TextView name;
    private TextView gender;
    private TextView index_id;
    private ImageView photo;
    private Bitmap bmpImage;
    private Button back;
    private Button edit;


    public static final int NEW_BOOK_ACTIVITY_REQUEST_CODE = 1;
    public static final int DETAILS_OF_BOOK_ACTIVITY_REQUEST_CODE = 2;


    private AnimalViewModel animalViewModel;
    private Animal editedAnimal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_animal);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        name = findViewById(R.id.txtName);
        gender = findViewById(R.id.txtGender);
        index_id = findViewById(R.id.txtIndex_id);
        photo = findViewById(R.id.imgOfAnimal1);

        if (getIntent().hasExtra(EXTRA_EDIT_ANIMAL_NAME)) {
            name.setText(getIntent().getStringExtra(EXTRA_EDIT_ANIMAL_NAME));
            gender.setText(getIntent().getStringExtra(EXTRA_EDIT_ANIMAL_GENDER));
            index_id.setText(getIntent().getStringExtra(EXTRA_EDIT_ANIMAL_INDEX_ID));
            photo.setImageBitmap(DataConverter.convertToImage(getIntent().getByteArrayExtra((String) EXTRA_EDIT_ANIMAL_PHOTO)));
            bmpImage = DataConverter.convertToImage(getIntent().getByteArrayExtra((String) EXTRA_EDIT_ANIMAL_PHOTO));

        }
        back = findViewById(R.id.btnBack);
        back.setOnClickListener(e -> {
            finish();
        });
    }
}
