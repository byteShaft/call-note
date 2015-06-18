package com.byteshaft.callnote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelpers {

    private SQLiteDatabase mDbHelper;
    private SqliteHelpers mSqliteHelpers;

    public DataBaseHelpers(Context context) {
        mSqliteHelpers = new SqliteHelpers(context);
    }

    String checkIfItemAlreadyExistInDatabase(String note) {
        String value = null;
        mDbHelper = mSqliteHelpers.getReadableDatabase();
        String whereClause =  SqliteHelpers.NOTES_COLUMN +" = ?";
        String[] whereArgs = new String[] {note};
        Cursor cursor = mDbHelper.query(SqliteHelpers.TABLE_NAME, null, whereClause, whereArgs,
                null, null, null);
        while (cursor.moveToNext()) {
            value =  cursor.getString(cursor.getColumnIndex(SqliteHelpers.NOTES_COLUMN));
        }
        return value;
    }

    void createNewEntry(String[] value, String note, String desc, String image, String date) {
        mDbHelper = mSqliteHelpers.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (String val : value) {
            values.put(SqliteHelpers.NUMBER_COLUMN, val);
            values.put(SqliteHelpers.NOTES_COLUMN, note);
            values.put(SqliteHelpers.PICTURE_COLUMN, image);
            values.put(SqliteHelpers.DATE_COLUMN, date);
            values.put(SqliteHelpers.DESCRIPTION, desc);
            mDbHelper.insert(SqliteHelpers.TABLE_NAME, null, values);
            Log.i(Helpers.LOG_TAG, "created New Entry");
        }
        closeDatabase();
    }

    void clickUpdate(String id, String[] number, String note, String desc, String image, String date) {
        ContentValues values = new ContentValues();
        for (String val : number) {
            values.put(SqliteHelpers.NUMBER_COLUMN, val);
            values.put(SqliteHelpers.NOTES_COLUMN, note);
            values.put(SqliteHelpers.PICTURE_COLUMN, image);
            values.put(SqliteHelpers.DATE_COLUMN, date);
            values.put(SqliteHelpers.DESCRIPTION, desc);
            mDbHelper.update(SqliteHelpers.TABLE_NAME, values, SqliteHelpers.ID_COLUMN + "=" + id, null);
            Log.i(Helpers.LOG_TAG, "Updated.......");
        }
        closeDatabase();
    }

    void updateData(String[] number, String note, String desc, String image, String date) {
        ContentValues values = new ContentValues();
        for (String val : number) {
            values.put(SqliteHelpers.NUMBER_COLUMN, val);
            values.put(SqliteHelpers.NOTES_COLUMN, note);
            values.put(SqliteHelpers.PICTURE_COLUMN, image);
            values.put(SqliteHelpers.DATE_COLUMN, date);
            values.put(SqliteHelpers.DESCRIPTION, desc);
            String selection = SqliteHelpers.NOTES_COLUMN + " LIKE ?"; // where ID column = rowId (that is, selectionArgs)
            String[] selectionArgs = { note };
            mDbHelper.update(SqliteHelpers.TABLE_NAME, values, selection,
                    selectionArgs);
            Log.i(Helpers.LOG_TAG, "Updated.......");
        }
        closeDatabase();
    }

    void deleteItem(String column, String value, boolean val) {
        mDbHelper = mSqliteHelpers.getWritableDatabase();
        mDbHelper.delete(SqliteHelpers.TABLE_NAME, column + " = ?", new String[]{value});
        if (val) {
            mDbHelper.execSQL("delete from " + SqliteHelpers.TABLE_NAME + " where " +
                    SqliteHelpers.NUMBER_COLUMN + " ='" + value + "'");
            Log.i("sqlite", "All Entries deleted");
        }
        Log.i(Helpers.LOG_TAG, "Entry deleted");
    }

    void closeDatabase() {
        mSqliteHelpers.close();
        Log.i(Helpers.LOG_TAG, "close database");
    }

    String getIconLinkForNumber(String value) {
        String uri = null;
        mDbHelper = mSqliteHelpers.getReadableDatabase();
        Cursor cursor = mDbHelper.rawQuery(
                "select " + SqliteHelpers.DESCRIPTION + " from " + SqliteHelpers.TABLE_NAME +
                        " where " + SqliteHelpers.NUMBER_COLUMN + " like ?",
                new String[]{"%" + value + "%"});
        while(cursor.moveToNext()) {
            uri = (cursor.getString(cursor.getColumnIndex(SqliteHelpers.NOTES_COLUMN)));
            Log.i(Helpers.LOG_TAG, " Data retrieved .....");
        }
        return uri;
    }

    ArrayList<String> getTitleFromNumber(String value) {
        mDbHelper = mSqliteHelpers.getReadableDatabase();
        Cursor cursor = mDbHelper.rawQuery(
                "select " + SqliteHelpers.NOTES_COLUMN + " from " + SqliteHelpers.TABLE_NAME +
                        " where " + SqliteHelpers.NUMBER_COLUMN + " like ?",
                new String[]{"%" + value + "%"});
        ArrayList<String> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            list.add(cursor.getString(cursor.getColumnIndex(SqliteHelpers.NOTES_COLUMN)));
            Log.i(Helpers.LOG_TAG, " Data retrieved .....");
        }
        return list;
    }

    ArrayList<String> getSummaryFromNumber(String value) {
        mDbHelper = mSqliteHelpers.getReadableDatabase();
        Cursor cursor = mDbHelper.rawQuery(
                "select " + SqliteHelpers.DESCRIPTION + " from " + SqliteHelpers.TABLE_NAME +
                        " where " + SqliteHelpers.NUMBER_COLUMN + " like ?",
                new String[]{"%" + value + "%"});
        ArrayList<String> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            list.add(cursor.getString(cursor.getColumnIndex(SqliteHelpers.DESCRIPTION)));
            Log.i(Helpers.LOG_TAG, " Data retrieved ,,,,,,");
        }
        return list;
    }

    String[] retrieveNoteDetails(String value) {
        mDbHelper = mSqliteHelpers.getReadableDatabase();
                String whereClause =  SqliteHelpers.NOTES_COLUMN +" = ?";
        String[] whereArgs = new String[] {value};
        Cursor cursor = mDbHelper.query(SqliteHelpers.TABLE_NAME, null, whereClause, whereArgs,
                null, null, null);
        String[] list = new String[5];
        while(cursor.moveToNext()) {
            list[0] = (cursor.getString(cursor.getColumnIndex(SqliteHelpers.ID_COLUMN)));
            list[1] = (cursor.getString(cursor.getColumnIndex(SqliteHelpers.NOTES_COLUMN)));
            list[2] = (cursor.getString(cursor.getColumnIndex(SqliteHelpers.DESCRIPTION)));
            list[3] = (cursor.getString(cursor.getColumnIndex(SqliteHelpers.DATE_COLUMN)));
            list[4] = (cursor.getString(cursor.getColumnIndex(SqliteHelpers.PICTURE_COLUMN)));
            Log.i(Helpers.LOG_TAG, " Data retrieved ....");
        }
        return list;
    }

    List<String> getLastNoteForContact(String value) {
        mDbHelper = mSqliteHelpers.getWritableDatabase();
        String whereClause = SqliteHelpers.NUMBER_COLUMN + " = ?";
        String[] whereArgs = new String[]{value};
        Cursor cursor = mDbHelper.query(SqliteHelpers.TABLE_NAME, null, whereClause, whereArgs,
                null, null, SqliteHelpers.NOTES_COLUMN + " DESC ", "1");
        List<String> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            list.add(cursor.getString(cursor.getColumnIndex(SqliteHelpers.NUMBER_COLUMN)));
            list.add(cursor.getString(cursor.getColumnIndex(SqliteHelpers.NOTES_COLUMN)));
            list.add(cursor.getString(cursor.getColumnIndex(SqliteHelpers.PICTURE_COLUMN)));
            Log.i(Helpers.LOG_TAG, " Data retrieved");
        }
        return list;
    }

    ArrayList<String> getNumberFromNote(String note) {
        mDbHelper = mSqliteHelpers.getReadableDatabase();
        String query = "SELECT * FROM " + SqliteHelpers.TABLE_NAME;
        Cursor cursor = mDbHelper.rawQuery(query, null);
        ArrayList<String> arrayList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String itemname = cursor.getString(cursor.getColumnIndex(
                    SqliteHelpers.NOTES_COLUMN));
            if (itemname.equals(note)) {
                String number = cursor.getString(cursor.getColumnIndex(
                        SqliteHelpers.NUMBER_COLUMN));
                arrayList.add(number);
            }
        }
        return arrayList;
    }

    ArrayList<String> getAllNumbers() {
        mDbHelper = mSqliteHelpers.getWritableDatabase();
        String query = "SELECT * FROM " + SqliteHelpers.TABLE_NAME;
        Cursor cursor = mDbHelper.rawQuery(query, null);
        ArrayList<String> arrayList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String itemname = cursor.getString(cursor.getColumnIndex(
                    SqliteHelpers.NUMBER_COLUMN));
            if (itemname != null) {
                arrayList.add(itemname);
            }
        }
        return arrayList;
    }

    ArrayList<String> getAllPresentNotes() {
        mDbHelper = mSqliteHelpers.getWritableDatabase();
        String query = "SELECT * FROM " + SqliteHelpers.TABLE_NAME+ " ORDER BY "+
                SqliteHelpers.DATE_COLUMN+" DESC";
        Cursor cursor = mDbHelper.rawQuery(query, null);
        ArrayList<String> arrayList = new ArrayList<>();
        while (cursor.moveToNext()) {
              String itemname = cursor.getString(cursor.getColumnIndex(
              SqliteHelpers.NOTES_COLUMN));
               if (itemname != null) {
                  arrayList.add(itemname);
               }
        }
        return arrayList;
    }

    ArrayList<String> getDescriptions() {
        mDbHelper = mSqliteHelpers.getWritableDatabase();
        String query = "SELECT * FROM " + SqliteHelpers.TABLE_NAME + " ORDER BY "+
                SqliteHelpers.DATE_COLUMN+" DESC";
        Cursor cursor = mDbHelper.rawQuery(query, null);
        ArrayList<String> arrayList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String itemname = cursor.getString(cursor.getColumnIndex(
                    SqliteHelpers.DESCRIPTION));
            if (itemname != null) {
                arrayList.add(itemname);
            }
        }
        return arrayList;
    }
}
