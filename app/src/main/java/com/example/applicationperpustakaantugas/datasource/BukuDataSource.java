package com.example.applicationperpustakaantugas.datasource;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.applicationperpustakaantugas.model.Buku;

import java.util.ArrayList;
import java.util.List;

public class BukuDataSource {
    private final DBHelper dbHelper;

    public BukuDataSource(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    private ContentValues convertToContenValues(Buku buku) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", buku.getId());
        contentValues.put("judul_buku", buku.getJudul_buku());
        contentValues.put("isbn", buku.getIsbn());
        contentValues.put("tahun_terbit", buku.getTahun_terbit());
        contentValues.put("penerbit", buku.getPenerbit());
        contentValues.put("kategori", buku.getKategori());
        contentValues.put("rangkuman", buku.getRangkuman());
        contentValues.put("ratting", buku.getRatting());
        contentValues.put("jumlah" , buku.getJumlah());
        return contentValues;

    }


    private Buku convertToBuku(Cursor cursor) {
        Buku buku = new Buku();
        buku.setId(cursor.getLong(0));
        buku.setJudul_buku(cursor.getString(1));
        buku.setIsbn(cursor.getString(2));
        buku.setTahun_terbit(cursor.getString(3));
        buku.setPenerbit(cursor.getString(4));
        buku.setKategori(cursor.getString(5));
        buku.setRangkuman(cursor.getString(6));
        buku.setRatting(cursor.getFloat(7));
        buku.setJumlah(cursor.getInt(8));
        return buku;
    }


    public void save(Buku buku) {
        ContentValues contentValues = convertToContenValues(buku);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.insert("buku", null, contentValues);
        database.close();
    }

    public List<Buku> getAll() {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT *FROM buku", null);
        List<Buku> foundBukuList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Buku buku = convertToBuku(cursor);
            foundBukuList.add(buku);
        }
        cursor.close();
        database.close();
        return foundBukuList;
    }

    public Buku findById(Long id) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT *FROM buku WHERE id=?", new String[]{String.valueOf(id)});

        boolean found = cursor.getCount() > 0;
        if (found) {
            cursor.moveToNext();
            Buku buku = convertToBuku(cursor);
            cursor.close();
            database.close();
            return buku;
        } else {
            throw new RuntimeException("data buku dengan id" + id + "tidak ditemukan");
        }
    }

    public void remove(Buku buku) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete("buku", "id=?", new String[]{Long.toString(buku.getId())});
        database.close();
    }

    public void update(Buku buku) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        ContentValues contentValues = convertToContenValues(buku);
        database.update("buku", contentValues, "id=?", new String[]{String.valueOf(buku.getId())});
        database.close();
    }

    public List<Buku> findByJudulNamaLike(String keyword) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM buku WHERE" +
                " judul_buku LIKE ? OR penerbit LIKE ? ", new String[]{
                "%" + keyword + "%", "%" + keyword + "%"});

        List<Buku> bukus = new ArrayList<>();
        while (cursor.moveToNext()) {
            Buku buku = convertToBuku(cursor);
            bukus.add(buku);
        }
        cursor.close();
        database.close();
        return bukus;
    }


}
