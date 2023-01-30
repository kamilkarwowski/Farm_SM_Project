package com.example.farm.Animals;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.farm.R;
import com.example.farm.converter.DataConverter;

public class EditAnimalActivity extends AppCompatActivity {

    public static final String EXTRA_EDIT_ANIMAL_NAME = "pb.edu.pl.EDIT_ANIMAL_NAME";
    public static final String EXTRA_EDIT_ANIMAL_GENDER = "pb.edu.pl.EDIT_ANIMAL_GENDER";
    public static final String EXTRA_EDIT_ANIMAL_INDEXID = "pb.edu.pl.EDIT_ANIMAL_INDEXID";
    public static final CharSequence EXTRA_EDIT_ANIMAL_PHOTO = "pb.edu.pl.EDIT_ANIMAL_PHOTO" ;

    private static final int CAMERA_INTENT = 5000;
    private static final int CAMERA_REQUEST_CODE = 6000;

    private EditText editName;
    private EditText editGender;
    private EditText editIndex;
    private ImageView editPhoto;
    private Bitmap bmpImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_animal);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        editName = findViewById(R.id.edit_animal_title);
        editGender = findViewById(R.id.edit_animal_gender);
        editIndex = findViewById(R.id.edit_animal_index);
        editPhoto = findViewById(R.id.editPhoto);
        if (getIntent().hasExtra(EXTRA_EDIT_ANIMAL_NAME)) {
            editName.setText(getIntent().getStringExtra(EXTRA_EDIT_ANIMAL_NAME));
            editGender.setText(getIntent().getStringExtra(EXTRA_EDIT_ANIMAL_GENDER));
            editIndex.setText(getIntent().getStringExtra(EXTRA_EDIT_ANIMAL_INDEXID));
            editPhoto.setImageBitmap(DataConverter.convertToImage(getIntent().getByteArrayExtra((String) EXTRA_EDIT_ANIMAL_PHOTO)));
            bmpImage = DataConverter.convertToImage(getIntent().getByteArrayExtra((String) EXTRA_EDIT_ANIMAL_PHOTO));
        }
        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(e -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(editName.getText())
                    || TextUtils.isEmpty(editGender.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                String name = editName.getText().toString();
                replyIntent.putExtra(EXTRA_EDIT_ANIMAL_NAME, name);
                String gender = editGender.getText().toString();
                replyIntent.putExtra(EXTRA_EDIT_ANIMAL_GENDER, gender);
                String index = editIndex.getText().toString();
                replyIntent.putExtra(EXTRA_EDIT_ANIMAL_INDEXID, index);

                if(bmpImage == null) {
                    editPhoto.setDrawingCacheEnabled(true);
                    editPhoto.buildDrawingCache();
                    bmpImage = Bitmap.createBitmap(editPhoto.getDrawingCache());
                }
                byte[] animalPhoto = DataConverter.convertToByteArray(bmpImage);
                replyIntent.putExtra((String) EXTRA_EDIT_ANIMAL_PHOTO, animalPhoto);

                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });

        Button buttonPick = findViewById(R.id.btnImgPick);
        buttonPick.setOnClickListener(v->takePhoto());
    }




    public void takePhoto() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            startActivityForResult(intent, CAMERA_INTENT);

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_INTENT:
                assert data != null;
                bmpImage = (Bitmap) data.getExtras().get("data");
                editPhoto.setImageBitmap(bmpImage);
                break;
        }
    }

}

