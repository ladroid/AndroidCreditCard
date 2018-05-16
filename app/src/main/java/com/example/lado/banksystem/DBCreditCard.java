package com.example.lado.banksystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lado on 29/3/18.
 */

public class DBCreditCard extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "CreditInfoDB.db";
    public static final String TABLE_NAME = "CreditInfo";
    public static final String KEY_ID = "id";
    public static final String KEY_CARDNUMBER = "CardNumber";
    public static final String KEY_CVV = "CVV";
    public static final String KEY_DATE = "Date";
    public static final String[] COLUMNS = {KEY_ID, KEY_CARDNUMBER, KEY_CVV, KEY_DATE };
    public DBCreditCard(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "( " + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        KEY_CARDNUMBER + " TEXT, " + KEY_CVV + " INTEGER, " + KEY_DATE + " INTEGER " + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void deleteOne(CreditInfo creditInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id = ?", new String[] {String.valueOf(creditInfo.getId())});
        db.close();
    }
    public CreditInfo getCreditInfo(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, COLUMNS, "id = ?", new String[]{String.valueOf(id)},null, null, null, null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        CreditInfo creditInfo = new CreditInfo();
        creditInfo.setId(Integer.parseInt(cursor.getString(0)));
        creditInfo.setCardNumber(cursor.getString(1));
        creditInfo.setCVV(Integer.parseInt(cursor.getString(2)));
        creditInfo.setDate(Integer.parseInt(cursor.getString(3)));

        return creditInfo;
    }

    public List<CreditInfo> allCredits() {
        List<CreditInfo> creditInfos = new LinkedList<CreditInfo>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        CreditInfo creditInfo = null;
        if(cursor.moveToFirst()) {
            do {
                creditInfo = new CreditInfo();
                creditInfo.setId(Integer.parseInt(cursor.getString(0)));
                creditInfo.setCardNumber(cursor.getString(1));
                creditInfo.setCVV(Integer.parseInt(cursor.getString(2)));
                creditInfo.setDate(Integer.parseInt(cursor.getString(3)));
                creditInfos.add(creditInfo);
            } while (cursor.moveToNext());
        }
        return creditInfos;
    }
    public void addcredit(CreditInfo creditInfo) {
        SQLiteDatabase db =  this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CARDNUMBER, creditInfo.getCardNumber());
        contentValues.put(KEY_CVV, creditInfo.getCVV());
        contentValues.put(KEY_DATE, creditInfo.getDate());
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }
    public int update(CreditInfo creditInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CARDNUMBER, creditInfo.getCardNumber());
        contentValues.put(KEY_CVV, creditInfo.getCVV());
        contentValues.put(KEY_DATE, creditInfo.getDate());
        int i = db.update(TABLE_NAME, contentValues, "id = ? ", new String[]{String.valueOf(creditInfo.getId())});
        db.close();
        return i;
    }
    public void Delete() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }
}
