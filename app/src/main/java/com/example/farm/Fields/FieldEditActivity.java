package com.example.farm.Fields;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.farm.R;

public class FieldEditActivity extends AppCompatActivity {
    public static final String EXTRA_EDIT_FIELD_NAME = "pb.edu.pl.EDIT_FIELD_NAME";
    public static final String EXTRA_EDIT_FIELD_CROPS = "pb.edu.pl.EDIT_FIELD_CROPS";
    public static final float EXTRA_EDIT_FIELD_SURFACE = Float.parseFloat("pb.edu.pl.EDIT_FIELD_SURFACE");
    public static final String EXTRA_EDIT_FIELD_DESCRIPTION = "pb.edu.pl.EDIT_FIELD_DESCRIPTION";

    private EditText editName;
    private EditText editCrop;
    private EditText editSurface;
    private EditText editDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_field);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        editName = findViewById(R.id.edit_field_name);
        editCrop = findViewById(R.id.edit_field_crop);
        editSurface = findViewById(R.id.edit_field_surface);
        editDescription = findViewById(R.id.edit_field_description);

        if (getIntent().hasExtra(EXTRA_EDIT_FIELD_NAME)) {
            editName.setText(getIntent().getStringExtra(EXTRA_EDIT_FIELD_NAME));
            editCrop.setText(getIntent().getStringExtra(EXTRA_EDIT_FIELD_CROPS));
            editSurface.setText(getIntent().getStringExtra(String.valueOf(EXTRA_EDIT_FIELD_SURFACE)));
            editDescription.setText(getIntent().getStringExtra(EXTRA_EDIT_FIELD_DESCRIPTION));
        }
        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(e -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(editName.getText())
                    || TextUtils.isEmpty(editCrop.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                String name = editName.getText().toString();
                replyIntent.putExtra(EXTRA_EDIT_FIELD_NAME, name);
                String crop = editCrop.getText().toString();
                replyIntent.putExtra(EXTRA_EDIT_FIELD_CROPS, crop);
                float surface = Float.parseFloat(editSurface.getText().toString());
                replyIntent.putExtra(String.valueOf(EXTRA_EDIT_FIELD_SURFACE), surface);
                setResult(RESULT_OK, replyIntent);
                String description = editDescription.getText().toString();
                replyIntent.putExtra(EXTRA_EDIT_FIELD_DESCRIPTION, description);
            }
            finish();
        });
    }
}
