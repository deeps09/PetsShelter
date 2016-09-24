package com.example.myapps.petsshelter;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapps.petsshelter.data.PetsContract.PetEntry;

public class RegisterPet extends AppCompatActivity {

    Spinner petGender;
    EditText petName;
    EditText petBreed;
    EditText petWeight;

    String mName = null, mBreed, mGender, mWeight;
    private final static String EDIT_TITLE = "Edit Pet Info";
    boolean insert = true;
    int _id;
    Uri receivedUri = null;

    private boolean mPetHasChanged = false;

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
        petWeight = (EditText) findViewById(R.id.weight_text);

        petGender.setOnTouchListener(mTouchListener);
        petName.setOnTouchListener(mTouchListener);
        petBreed.setOnTouchListener(mTouchListener);
        petWeight.setOnTouchListener(mTouchListener);

        String[] gender = {"Male", "Female", "Unknown"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, gender);
        petGender.setAdapter(adapter);

        Intent intent = getIntent();
        receivedUri = intent.getData();

        if (receivedUri != null) {
            Cursor cursor = getContentResolver().query(receivedUri, null, null, null, null);
            cursor.moveToFirst();
            // Setting up variables
            _id = cursor.getInt(cursor.getColumnIndex(PetEntry._ID));
            mName = cursor.getString(cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME));
            mBreed = cursor.getString(cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED));
            mWeight = cursor.getString(cursor.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT));
            mGender = cursor.getString(cursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER));

            // Updating views
            this.setTitle(EDIT_TITLE);
            petName.setText(mName);
            petBreed.setText(mBreed);
            petGender.setSelection(Integer.valueOf(mGender), true);
            petWeight.setText(mWeight);
            insert = false;
            Log.i("ID of Row", String.valueOf(_id));
        }
    }

    @Override
    public void onBackPressed() {
        if (!mPetHasChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        };
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public boolean onNavigateUp() {
        Toast.makeText(this, "Up", Toast.LENGTH_SHORT).show();
        return super.onNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item_dummy = (MenuItem) menu.findItem(R.id.action_dummy_data);
        item_dummy.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_dummy_data:
                savePet();
                break;
            /*case R.id.action_delete:
                deletePet();
                break;*/
            case R.id.action_submit:
                savePet();
                break;
            case android.R.id.home:
                if (!mPetHasChanged) {
                    NavUtils.navigateUpFromSameTask(RegisterPet.this);
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(RegisterPet.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
            case R.id.action_delete:
                new AlertDialog.Builder(this)
                        .setMessage("Confirm delete !!")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletePet();
                    }
                }).setTitle("Delete..").create().show();
        }

        return true;
        //return super.onOptionsItemSelected(item);
    }

    public void savePet() {

        Uri uri;
        ContentValues values = getContentValues();

        if (values != null) {
            if (this.getTitle().equals(EDIT_TITLE)) {
                if (mPetHasChanged == false){
                    Toast.makeText(this, "Nothing to update !", Toast.LENGTH_SHORT).show();
                    return;
                }
                int rows = getContentResolver().update(ContentUris.withAppendedId(PetEntry.CONTENT_URI, _id),
                        values,
                        PetEntry._ID + "= ?",
                        new String[]{String.valueOf(_id)});

                if (rows > 0) {
                    Toast.makeText(this, rows + " Record(s) updated", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "No records updated", Toast.LENGTH_SHORT).show();
                }

            } else {
                uri = getContentResolver().insert(PetEntry.CONTENT_URI, values);
                Toast.makeText(this, "Pet " + mName + " registered! \n" + uri.toString(), Toast.LENGTH_LONG).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            //Snackbar.make(getWindow().getDecorView() , "Please fill all", Snackbar.LENGTH_SHORT).show();
        }
    }

    public void deletePet() {

        int row = getContentResolver().delete(receivedUri, null, null);

        Toast.makeText(this, row + " Record Deleted!", Toast.LENGTH_SHORT).show();
        finish();

        /*PetsDBHelper dbHelper = new PetsDBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(PetEntry.TABLE_NAME, null, null);

        Toast.makeText(this, "All entried cleared", Toast.LENGTH_SHORT).show();

        db.close();
        dbHelper.close();*/
    }

    private ContentValues getContentValues() {

        ContentValues values = null;
        mName = petName.getText().toString();
        mBreed = petBreed.getText().toString();
        mWeight = petWeight.getText().toString();
        mGender = petGender.getSelectedItem().toString();

        if (TextUtils.isEmpty(mName) || TextUtils.isEmpty(mWeight)) { //(mName!= "" && (TextUtils.isEmpty(mName) || mBreed == "" || mGender == "")){
            values = null;
        } else {

            int genderId = -1;

            switch (mGender) {
                case "Male":
                    genderId = 0;
                    break;
                case "Female":
                    genderId = 1;
                    break;
                case "Unknown":
                    genderId = 2;
                    break;
            }
            values = new ContentValues();
            values.put(PetEntry.COLUMN_PET_NAME, mName);
            values.put(PetEntry.COLUMN_PET_BREED, mBreed);
            values.put(PetEntry.COLUMN_PET_GENDER, genderId);
            values.put(PetEntry.COLUMN_PET_WEIGHT, mWeight);
        }
        return values;
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mPetHasChanged = true;
            return false;
        }
    };

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Discard the unsaved data !!");
        builder.setPositiveButton("Discard", discardButtonClickListener);
        builder.setNegativeButton("Keep Editing", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null)
                    dialog.dismiss();
            }
        }).create().show();
    }
}
