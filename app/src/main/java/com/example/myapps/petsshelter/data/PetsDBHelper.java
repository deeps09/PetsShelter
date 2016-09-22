package com.example.myapps.petsshelter.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.myapps.petsshelter.data.PetsContract.PetsEntry;

/**
 * Created by Deepesh_Gupta1 on 09/01/2016.
 */
public class PetsDBHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "shelter.db";
    public static int DATABASE_VERSION = 3;

    public static String CREATE_SQL_PETS_TABLE =
            "CREATE TABLE " + PetsEntry.TABLE_NAME + " (" +
                    PetsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PetsEntry.COLUMN_PET_NAME + " TEXT NOT NULL, " +
                    PetsEntry.COLUMN_PET_BREED + " TEXT NOT NULL, " +
                    PetsEntry.COLUMN_PET_GENDER + " INTEGER NOT NULL DEFAULT 0, " +
                    PetsEntry.COLUMN_PET_WEIGHT + " INTEGER NOT NULL )";

    public static String DROP_TABLE =
            "DROP TABLE IF EXISTS " + PetsEntry.TABLE_NAME;

    public PetsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
}

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SQL_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
}
