package com.example.applicationperpustakaantugas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.applicationperpustakaantugas.datasource.BukuDataSource;
import com.example.applicationperpustakaantugas.datasource.DBHelper;
import com.example.applicationperpustakaantugas.model.Buku;

public class DetailActivity extends AppCompatActivity {
    TextView tJudul, tIsbn , tahunTerbitT , tPenerbit , tKategori , tRangkuman ;

    private void loadDetailBuku(Long idBuku){
        try {
            DBHelper dbHelper = new DBHelper(this);
            BukuDataSource dataSource = new BukuDataSource(dbHelper);
            Buku buku = dataSource.findById(idBuku);
            tJudul.setText(buku.getJudul_buku());
            tIsbn.setText(buku.getIsbn());
            tahunTerbitT.setText(buku.getTahun_terbit());
            tPenerbit.setText(buku.getPenerbit());
            tKategori.setText(buku.getKategori());
            tRangkuman.setText(buku.getRangkuman());
        } catch (Exception e){
            Toast.makeText(this, "text", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int selectedMenuId = item.getItemId();
        if (selectedMenuId == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tJudul = findViewById(R.id.judulT);
        tIsbn = findViewById(R.id.isbnT);
        tahunTerbitT = findViewById(R.id.tTahunTerbitT);
        tPenerbit = findViewById(R.id.penerbitT);
        tKategori = findViewById(R.id.kategoriT);
        tRangkuman = findViewById(R.id.rangkumanT);

        long receivedBuku = getIntent().getLongExtra("id_buku", -1);
        if (receivedBuku == -1){
            Toast.makeText(this, "tdk menerima data buku", Toast.LENGTH_SHORT).show();
        } else {
                loadDetailBuku(receivedBuku);
        }
    }
}