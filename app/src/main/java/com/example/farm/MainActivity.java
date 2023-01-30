package com.example.farm;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farm.Animals.DetailAnimalActivity;
import com.example.farm.Animals.EditAnimalActivity;
import com.example.farm.Fields.FieldViewActivity;
import com.example.farm.Weather.WeatherActivity;
import com.example.farm.converter.DataConverter;
import com.example.farm.database.entity.Animal;
import com.example.farm.database.view.AnimalViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static final int NEW_BOOK_ACTIVITY_REQUEST_CODE = 1;
    public static final int DETAILS_OF_BOOK_ACTIVITY_REQUEST_CODE = 2;


    private AnimalViewModel animalViewModel;
    private Animal editedAnimal;
    private LiveData<List<Animal>> animals;
    ArrayAdapter adapterArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final AnimalAdapter adapter = new AnimalAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        animalViewModel = ViewModelProviders.of(this).get(AnimalViewModel.class);
        animalViewModel.findAll().observe(this, adapter::setAnimals);

        FloatingActionButton addAnimalButton = findViewById(R.id.add_button);
        addAnimalButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditAnimalActivity.class);
            startActivityForResult(intent, NEW_BOOK_ACTIVITY_REQUEST_CODE);
        });

        animals = animalViewModel.findAll();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchDatabase(s);
                return true;
            }
        });
        return true;
    }

    private void searchDatabase(String query) {
        String search = "%" + query + "%";
        ArrayList<Animal> animalList = new ArrayList<>();
        for (Animal animal : animals.getValue()) {
            if (animal.getName().toLowerCase().contains(query.toLowerCase()) || animal.getIndex_id().toLowerCase().contains(query.toLowerCase())) {
                animalList.add(animal);
            }
        }

        RecyclerView view = findViewById(R.id.recyclerview);
        AnimalAdapter animalAdapter = new AnimalAdapter();
        animalAdapter.setAnimals(animalList);
        view.setAdapter(animalAdapter);
        view.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        switch (item.getItemId()) {
            case R.id.menu_change:
                System.out.println("Clicked");
                Intent intent = new Intent(MainActivity.this, FieldViewActivity.class);
                startActivity(intent);
                return true;

            case R.id.menu_weather:
                System.out.println("Weather");
                Intent intent1 = new Intent(MainActivity.this, WeatherActivity.class);
                intent1.putExtra("SHOW_WELCOME", true);
                startActivity(intent1);
                return true;

            case R.id.menu_geo:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.geoportal.gov.pl")));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_BOOK_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Animal animal = new Animal(data.getStringExtra(EditAnimalActivity.EXTRA_EDIT_ANIMAL_NAME),
                    data.getStringExtra(EditAnimalActivity.EXTRA_EDIT_ANIMAL_GENDER),
                    data.getStringExtra(EditAnimalActivity.EXTRA_EDIT_ANIMAL_INDEXID),
                    data.getByteArrayExtra((String) EditAnimalActivity.EXTRA_EDIT_ANIMAL_PHOTO));
            animalViewModel.insert(animal);
            Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.animal_added),
                    Snackbar.LENGTH_LONG).show();
        } else if (requestCode == DETAILS_OF_BOOK_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            editedAnimal.setName(data.getStringExtra(EditAnimalActivity.EXTRA_EDIT_ANIMAL_NAME));
            editedAnimal.setGender(data.getStringExtra(EditAnimalActivity.EXTRA_EDIT_ANIMAL_GENDER));
            editedAnimal.setIndex_id(data.getStringExtra(EditAnimalActivity.EXTRA_EDIT_ANIMAL_INDEXID));
            editedAnimal.setAnimalPhoto(data.getByteArrayExtra((String) EditAnimalActivity.EXTRA_EDIT_ANIMAL_PHOTO));
            animalViewModel.update(editedAnimal);
            editedAnimal = null;
            Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.animal_edited),
                    Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(findViewById(R.id.coordinator_layout),
                            getString(R.string.empty_not_saved),
                            Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    private class AnimalHolder extends RecyclerView.ViewHolder {
        private final TextView animalNameTextView;
        private final TextView animalGenderTextView;
        private final TextView animalIndexidTextView;
        private Animal animal;
        private Bitmap bitmap;
        private ImageView animalImageView;

        public AnimalHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.animal_item_list, parent, false));

            bitmap = null;
            animalNameTextView = itemView.findViewById(R.id.animal_name);
            animalGenderTextView = itemView.findViewById(R.id.animal_gender);
            animalIndexidTextView = itemView.findViewById(R.id.animal_index);
            animalImageView = itemView.findViewById(R.id.imgOfAnimal);

            View animalItem = itemView.findViewById(R.id.book_item);
            animalItem.setOnLongClickListener(v -> {
                animalViewModel.delete(animal);
                Snackbar.make(findViewById(R.id.coordinator_layout),
                                getString(R.string.animal_deleted),
                                Snackbar.LENGTH_LONG)
                        .show();
                return true;
            });
            animalItem.setOnClickListener(v -> {
                editedAnimal = animal;
                animalImageView.setDrawingCacheEnabled(true);
                animalImageView.buildDrawingCache();
                bitmap = Bitmap.createBitmap(animalImageView.getDrawingCache());

                Intent intent = new Intent(MainActivity.this, DetailAnimalActivity.class);
                intent.putExtra(DetailAnimalActivity.EXTRA_EDIT_ANIMAL_NAME, animalNameTextView.getText());
                intent.putExtra(DetailAnimalActivity.EXTRA_EDIT_ANIMAL_GENDER, animalGenderTextView.getText());
                intent.putExtra(DetailAnimalActivity.EXTRA_EDIT_ANIMAL_INDEX_ID, animalIndexidTextView.getText());
                intent.putExtra((String) DetailAnimalActivity.EXTRA_EDIT_ANIMAL_PHOTO, DataConverter.convertToByteArray(bitmap));
                startActivity(intent);
            });

            animalItem.findViewById(R.id.btnEdit).setOnClickListener(v -> {
                editedAnimal = animal;
                animalImageView.setDrawingCacheEnabled(true);
                animalImageView.buildDrawingCache();
                bitmap = Bitmap.createBitmap(animalImageView.getDrawingCache());

                Intent intent = new Intent(MainActivity.this, EditAnimalActivity.class);
                intent.putExtra(EditAnimalActivity.EXTRA_EDIT_ANIMAL_NAME, animalNameTextView.getText());
                intent.putExtra(EditAnimalActivity.EXTRA_EDIT_ANIMAL_GENDER, animalGenderTextView.getText());
                intent.putExtra(EditAnimalActivity.EXTRA_EDIT_ANIMAL_INDEXID, animalIndexidTextView.getText());
                intent.putExtra((String) EditAnimalActivity.EXTRA_EDIT_ANIMAL_PHOTO, DataConverter.convertToByteArray(bitmap));
                startActivityForResult(intent, DETAILS_OF_BOOK_ACTIVITY_REQUEST_CODE);
            });

        }

        public void bind(Animal animal) {
            animalNameTextView.setText(animal.getName());
            animalIndexidTextView.setText(animal.getIndex_id());
            animalGenderTextView.setText(animal.getGender());

            if(animal.getAnimalPhoto() != null){
                animalImageView.setImageBitmap(DataConverter.convertToImage(animal.getAnimalPhoto()));
            }
            this.animal = animal;
        }
    }

    private class AnimalAdapter extends RecyclerView.Adapter<AnimalHolder> {

        private List<Animal> animals;

        @NonNull
        @Override
        public AnimalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new AnimalHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(AnimalHolder holder, int position) {
            if (animals != null) {
                Animal animal = animals.get(position);
                holder.bind(animal);
            } else {
                Log.d("MainActivity", "No animals");
            }
        }

        public int getItemCount() {
            if (animals != null) {
                return animals.size();
            } else {
                return 0;
            }
        }

        void setAnimals(List<Animal> animals) {
            this.animals = animals;
            notifyDataSetChanged();
        }
    }
}