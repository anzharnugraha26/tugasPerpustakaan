package com.example.applicationperpustakaantugas.datasource;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "perpustakaan.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE buku ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "judul_buku TEXT," +
                "isbn TEXT, " +
                "tahun_terbit TEXT, " +
                "penerbit TEXT, " +
                "kategori TEXT , " +
                "rangkuman TEXT ," +
                "ratting REAL ," +
                "jumlah INTEGER )";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
