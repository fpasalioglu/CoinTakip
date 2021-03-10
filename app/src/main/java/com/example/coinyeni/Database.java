package com.example.coinyeni;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class Database extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cointakip.db";
    private static final String TABLE_NAME = "coin_listesi";

    private static final String COIN_ID = "id";
    private static final String COIN_ADI = "coin_adi";
    private static final String COIN_MIKTARI = "miktar";
    private static final String COIN_FIYATI = "fiyat";
    private static final String COIN_TYPE = "tip";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COIN_ADI + " TEXT,"
                + COIN_MIKTARI + " TEXT,"
                + COIN_FIYATI + " TEXT,"
                + COIN_TYPE + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    public void coinSil(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COIN_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    public void coinEkle(String coin_adi, String coin_miktari, String alis_fiyat, boolean tip) {
        String type="BTC";
        if (tip)
            type="TRY";

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COIN_ADI, coin_adi);
        values.put(COIN_MIKTARI, coin_miktari);
        values.put(COIN_FIYATI, alis_fiyat);
        values.put(COIN_TYPE, type);

        db.insertOrThrow(TABLE_NAME, null, values);
        db.close();
    }


    public HashMap<String, String> coinDetay(int id){
        HashMap<String,String> coin = new HashMap<String,String>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME+ " WHERE id="+id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            coin.put(COIN_ADI, cursor.getString(1));
            coin.put(COIN_MIKTARI, cursor.getString(2));
            coin.put(COIN_FIYATI, cursor.getString(3));
            coin.put(COIN_TYPE, cursor.getString(4));
        }
        cursor.close();
        db.close();
        return coin;
    }

    public  ArrayList<HashMap<String, String>> coinler(){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> coinlist = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }

                coinlist.add(map);
            } while (cursor.moveToNext());
        }
        db.close();

        return coinlist;
    }

    public void coinDuzenle(String coin_adi, String miktar, String fiyat, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COIN_ADI, coin_adi);
        values.put(COIN_MIKTARI, miktar);
        values.put(COIN_FIYATI, fiyat);

        // updating row
        db.update(TABLE_NAME, values, COIN_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
    }
}
