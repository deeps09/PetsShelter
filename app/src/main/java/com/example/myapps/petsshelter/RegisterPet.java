package com.example.myapps.petsshelter;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapps.petsshelter.data.PetsContract;
import com.example.myapps.petsshelter.data.PetsDBHelper;

public class RegisterPet extends AppCompatActivity {

    Spinner petGender;
    EditText petName;
    EditText petBreed;
    EditText petWeight;

    String mName, mBreed, mGender, mWeight;

    public static int COLUMN_INDEX_NAME = 1;
    public static int COLUMN_INDEX_BREED = 2;
    public static int COLUMN_INDEX_GENDER = 3;
    public static int COLUMN_INDEX_WEIGHT = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        petGender = (Spinner) findViewById(R.id.gender_spinner);
        petName = (EditText) findViewById(R.id.name_text);
        petBreed = (EditText) findViewById(R.id.breed_text);
        petWeight = (EditText) (EditText) findViewById(R.id.weight_text);

        String[] gender = {"Male", "Female", "Unknown"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,gender);
        petGender.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_insert: insertPet();
                break;
            case R.id.action_delete: deletePet();
                break;
            case R.id.action_submit: insertPet();
                break;
        }

        return true;


        //return super.onOptionsItemSelected(item);
    }

    public void insertPet(){

        mName = petName.getText().toString();
        mBreed = petBreed.getText().toString();
        mWeight = petWeight.getText().toString();
        mGender = petGender.getSelectedItem().toString();

        int genderid = -1;

        switch (mGender){
            case "Male": genderid = 1;
                break;
            case "Female": genderid = 2;
                break;
            case "Unknown": genderid = 0;
                break;
        }

        PetsDBHelper dbHelper = new PetsDBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PetsContract.PetsEntry.COLUMN_PET_NAME, mName);
        values.put(PetsContract.PetsEntry.COLUMN_PET_BREED, mBreed);
        values.put(PetsContract.PetsEntry.COLUMN_PET_GENDER, genderid);
        values.put(PetsContract.PetsEntry.COLUMN_PET_WEIGHT, mWeight);

        db.insertOrThrow(PetsContract.PetsEntry.TABLE_NAME, null, values);

        Toast.makeText(this, "Pet " + mName + " registered!", Toast.LENGTH_SHORT).show();
        //Snackbar.make(null, "Row Inserted", Snackbar.LENGTH_SHORT).show();

        db.close();
        dbHelper.close();
    }

    public void deletePet(){

        PetsDBHelper dbHelper = new PetsDBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(PetsContract.PetsEntry.TABLE_NAME, null, null);

        Toast.makeText(this, "All entried cleared", Toast.LENGTH_SHORT).show();

        db.close();
        dbHelper.close();
    }

}
