package com.example.myapps.petsshelter.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.myapps.petsshelter.data.PetsContract.PetEntry;

/**
 * Created by Deepesh_Gupta1 on 09/22/2016.
 */
public class PetProvider extends ContentProvider {

    private PetsDBHelper mDbHelper;
    private static final int PETS = 100;
    private static final int PET_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(PetsContract.CONTENT_AUTHORITY, PetsContract.PATH_PETS, PETS);

        sUriMatcher.addURI(PetsContract.CONTENT_AUTHORITY, PetsContract.PATH_PETS + "/#", PET_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new PetsDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);

        switch (match) {
            case PETS:
                cursor = db.query(PetEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case PET_ID:
                selection = PetEntry._ID + " = ?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(PetEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return PetEntry.CONTENT_LIST_TYPE;
            case PET_ID:
                return PetEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        String name = contentValues.getAsString(PetEntry.COLUMN_PET_NAME);
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Pet requires a name");
        }

        Integer weight = contentValues.getAsInteger(PetEntry.COLUMN_PET_WEIGHT);
        if (weight != null && weight < 0) {
            throw new IllegalArgumentException("Pet requires valid weight");
        }

        Integer gender = contentValues.getAsInteger(PetEntry.COLUMN_PET_GENDER);
        if (!isValidGender(gender) || gender == null) {
            throw new IllegalArgumentException("Pet requires valid gender");
        }

        int match = sUriMatcher.match(uri);
        Uri mUri = null;

        switch (match) {
            case PETS:
                mUri = insertPet(uri, contentValues);
                break;
            default:
                throw new IllegalArgumentException("Cannot execute unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return mUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = mDbHelper.getWritableDatabase();
        int row = -1;
        int match = sUriMatcher.match(uri);

        switch (match) {
            case PETS:
                row = sqLiteDatabase.delete(PetEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PET_ID:
                selection = PetEntry._ID + "= ?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                row = sqLiteDatabase.delete(PetEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Delete is not supported by URI " + uri);
        }

        if (row > 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return row;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int row = -1;
        Log.i("Update URI", values.toString() + "\n" + uri.toString());

        if (values.containsKey(PetEntry.COLUMN_PET_NAME)) {
            String name = values.getAsString(PetEntry.COLUMN_PET_NAME);
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Pet requires a name");
            }
        }

        if (values.containsKey(PetEntry.COLUMN_PET_WEIGHT)) {
            Integer weight = values.getAsInteger(PetEntry.COLUMN_PET_WEIGHT);
            if (weight != null && weight < 0) {
                throw new IllegalArgumentException("Pet requires valid weight");
            }
        }

        if (values.containsKey(PetEntry.COLUMN_PET_GENDER)) {
            Integer gender = values.getAsInteger(PetEntry.COLUMN_PET_GENDER);
            if (!isValidGender(gender) || gender == null) {
                throw new IllegalArgumentException("Pet requires valid gender");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase sqLiteDatabase = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        switch (match) {
            case PETS:
                row = sqLiteDatabase.update(PetEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case PET_ID:
                selection = PetEntry._ID + "= ?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                row = sqLiteDatabase.update(PetEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Update is not supported by URI " + uri);
        }
        if (row > 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return row;
    }

    private Uri insertPet(Uri uri, ContentValues values) {

        SQLiteDatabase sqLiteDatabase = mDbHelper.getWritableDatabase();
        long id = sqLiteDatabase.insert(PetEntry.TABLE_NAME, null, values);

        sqLiteDatabase.close();
        return ContentUris.withAppendedId(uri, id);
    }

    public static boolean isValidGender(int gender) {
        if (gender == 0 || gender == 1 || gender == 2)
            return true;
        return false;
    }
}
