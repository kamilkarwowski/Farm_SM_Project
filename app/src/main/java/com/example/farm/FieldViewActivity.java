package com.example.farm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

import com.example.farm.database.entity.Field;
import com.example.farm.database.view.FieldViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class FieldViewActivity extends AppCompatActivity {
    public static final int NEW_FIELD_ACTIVITY_REQUEST_CODE = 1;
    public static final int DETAILS_OF_FIELD_ACTIVITY_REQUEST_CODE = 2;


    private FieldViewModel fieldViewModel;
    private Field editedField;
    private LiveData<List<Field>> fields;
    ArrayAdapter adapterArray;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field);

        RecyclerView recyclerView = findViewById(R.id.recyclerview2);
        final FieldAdapter adapter = new FieldAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fieldViewModel = ViewModelProviders.of(this).get(FieldViewModel.class);
        fieldViewModel.findAllField().observe(this, adapter::setFields);

        FloatingActionButton addAnimalButton = findViewById(R.id.add_button);
        addAnimalButton.setOnClickListener(v -> {
            Intent intent = new Intent(FieldViewActivity.this, EditAnimalActivity.class);
            startActivityForResult(intent, NEW_FIELD_ACTIVITY_REQUEST_CODE);
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

//        MenuItem menuItem = findViewById(R.id.menu_change);
//        menuItem.setIcon(R.drawable.baseline_area_chart_24);

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

    private void searchDatabase(String query){
        String search = "%"+query+"%";
        ArrayList<Field> fieldList = new ArrayList<>();
        for(Field field : fields.getValue()){
            if (field.getName().toLowerCase().contains(query.toLowerCase()) || field.getCrop().toLowerCase().contains(query.toLowerCase())){
                fieldList.add(field);
            }
        }
        //RecyclerView recyclerView = findViewById(R.id.recyclerview);

        RecyclerView view = findViewById(R.id.recyclerview2);
        FieldAdapter fieldAdapter = new FieldAdapter();
        fieldAdapter.setFields(fieldList);
        view.setAdapter(fieldAdapter);
        view.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.menu_item_search || id==R.id.menu_change) {
//            return true;
//        }

        switch (item.getItemId()){
            case R.id.menu_change:
                System.out.println("Clicked");
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_FIELD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Field field = new Field(data.getStringExtra(FieldEditActivity.EXTRA_EDIT_FIELD_NAME),
                    Float.parseFloat(data.getStringExtra(String.valueOf(FieldEditActivity.EXTRA_EDIT_FIELD_SURFACE))),
                    data.getStringExtra(FieldEditActivity.EXTRA_EDIT_FIELD_CROPS),
                    data.getStringExtra(FieldEditActivity.EXTRA_EDIT_FIELD_DESCRIPTION));
            fieldViewModel.insertField(field);
            Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.field_added),
                    Snackbar.LENGTH_LONG).show();
        } else if (requestCode == DETAILS_OF_FIELD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            editedField.setName(data.getStringExtra(FieldEditActivity.EXTRA_EDIT_FIELD_NAME));
            editedField.setSurface(Float.parseFloat(data.getStringExtra(String.valueOf(FieldEditActivity.EXTRA_EDIT_FIELD_SURFACE))));
            editedField.setCrop(data.getStringExtra(FieldEditActivity.EXTRA_EDIT_FIELD_CROPS));
            editedField.setDescription(data.getStringExtra(FieldEditActivity.EXTRA_EDIT_FIELD_DESCRIPTION));
            fieldViewModel.updateField(editedField);
            editedField = null;
            Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.field_edited),
                    Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(findViewById(R.id.coordinator_layout),
                            getString(R.string.empty_not_saved),
                            Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    private class FieldHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView surfaceTextView;
        private final TextView cropTextView;
        private Field field;
        //private BreakIterator AnimalTextView;

        public FieldHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.field_item_list, parent, false));

            nameTextView = itemView.findViewById(R.id.field_name);
            surfaceTextView =itemView.findViewById(R.id.field_surface);
            cropTextView = itemView.findViewById(R.id.field_crop);
            View animalItem = itemView.findViewById(R.id.book_item);
            //Button edit = findViewById(R.id.btnEdit);
            animalItem.setOnLongClickListener(v -> {
                fieldViewModel.deleteField(field);
                Snackbar.make(findViewById(R.id.coordinator_layout),
                                getString(R.string.field_deleted),
                                Snackbar.LENGTH_LONG)
                        .show();
                return true;
            });
            //trzeba layoout dodać u mnie się zjebało + Activity
//            animalItem.setOnClickListener(v -> {
//                editedField = field;
//                Intent intent = new Intent(FieldViewActivity.this, DetailAnimalActivity.class);
//                intent.putExtra(FieldEditActivity.EXTRA_EDIT_FIELD_NAME, nameTextView.getText());
//                intent.putExtra(String.valueOf(FieldEditActivity.EXTRA_EDIT_FIELD_SURFACE), surfaceTextView.getText());
//                intent.putExtra(FieldEditActivity.EXTRA_EDIT_FIELD_CROPS, cropTextView.getText());
//                intent.putExtra(FieldEditActivity.EXTRA_EDIT_FIELD_DESCRIPTION, field.getDescription());
//                startActivity(intent);
//            });

            animalItem.findViewById(R.id.btnEdit).setOnClickListener(v -> {
                editedField = field;
                Intent intent = new Intent(FieldViewActivity.this, FieldEditActivity.class);
                intent.putExtra(FieldEditActivity.EXTRA_EDIT_FIELD_NAME, nameTextView.getText());
                intent.putExtra(String.valueOf(FieldEditActivity.EXTRA_EDIT_FIELD_SURFACE), surfaceTextView.getText());
                intent.putExtra(FieldEditActivity.EXTRA_EDIT_FIELD_CROPS, cropTextView.getText());
                intent.putExtra(FieldEditActivity.EXTRA_EDIT_FIELD_DESCRIPTION, field.getDescription());
                startActivityForResult(intent, DETAILS_OF_FIELD_ACTIVITY_REQUEST_CODE);
            });

        }

        public void bind(Field field) {
            nameTextView.setText(field.getName());
            cropTextView.setText(field.getCrop());
            surfaceTextView.setText(String.valueOf(field.getSurface()));
            this.field = field;
        }
    }

    private class FieldAdapter extends RecyclerView.Adapter<FieldHolder> {

        private List<Field> fields;

        @NonNull
        @Override
        public FieldHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new FieldHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(FieldHolder holder, int position) {
            if (fields != null) {
                Field field = fields.get(position);
                holder.bind(field);
            } else {
                Log.d("MainActivity", "No fields");
            }
        }

        public int getItemCount() {
            if (fields != null) {
                return fields.size();
            } else {
                return 0;
            }
        }

        void setFields(List<Field> fields) {
            this.fields = fields;
            notifyDataSetChanged();
        }
    }
}
