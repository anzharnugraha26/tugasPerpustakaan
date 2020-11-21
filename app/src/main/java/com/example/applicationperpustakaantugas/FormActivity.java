package com.example.applicationperpustakaantugas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.SpinnerAdapter;
import android.widget.Toast;


import com.example.applicationperpustakaantugas.datasource.DBHelper;
import com.example.applicationperpustakaantugas.datasource.BukuDataSource;
import com.example.applicationperpustakaantugas.model.Buku;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class FormActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextInputEditText etJudul, etIsbn, etTahunTerbit, rangkuman;
    private RadioGroup rKategori;
    private AppCompatSpinner etPenerbit;
    private AppCompatButton btnSave;

    private Buku receivedBuku;

    private void loadDetailDataBuku(Long idBuku) {
        try {
            DBHelper dbHelper = new DBHelper(this);
            BukuDataSource dataSource = new BukuDataSource(dbHelper);
            receivedBuku = dataSource.findById(idBuku);
            etJudul.setText(receivedBuku.getJudul_buku());
            etIsbn.setText(receivedBuku.getIsbn());
            etTahunTerbit.setText(receivedBuku.getTahun_terbit());
            rangkuman.setText(receivedBuku.getRangkuman());

            String kategoriR = receivedBuku.getKategori();
            if (kategoriR.equals("Buku Agama")) {
                rKategori.check(R.id.buku_agama_Rb);
            } else if (kategoriR.equals("Buku Komputer")) {
                rKategori.check(R.id.buku_komputer_Rb);
            } else if (kategoriR.equals("Novel")) {
                rKategori.check(R.id.novel_Rb);
            } else {
                rKategori.check(R.id.lain2);
            }

            SpinnerAdapter adapter = etPenerbit.getAdapter();
            if (adapter instanceof ArrayAdapter) {
                int position = ((ArrayAdapter) adapter).getPosition(receivedBuku.getPenerbit());
                etPenerbit.setSelection(position);
            }
            btnSave.setText("Upadate");
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initDataBuku(Buku buku) {
        String inputJudul = etJudul.getText().toString().trim();
        String inputIsbn = etIsbn.getText().toString().trim();
        String inputTahunterbit = etTahunTerbit.getText().toString().trim();
        String selectPenerbit = etPenerbit.getSelectedItem().toString();
        String inputRangkuman = rangkuman.getText().toString();

        String selectedKategori = null;
        if (rKategori.getCheckedRadioButtonId() == R.id.buku_agama_Rb) {
            selectedKategori = "Buku Agama";
        } else if (rKategori.getCheckedRadioButtonId() == R.id.buku_komputer_Rb) {
            selectedKategori = "Buku Komputer";
        } else if (rKategori.getCheckedRadioButtonId() == R.id.novel_Rb) {
            selectedKategori = "Novel";
        } else {
            selectedKategori = "Lain-lain";
        }


        buku.setJudul_buku(inputJudul);
        buku.setIsbn(inputIsbn);
        buku.setTahun_terbit(inputTahunterbit);
        buku.setPenerbit(selectPenerbit);
        buku.setKategori(selectedKategori);
        buku.setRangkuman(inputRangkuman);


    }

    private void addNewBuku() {
        Buku buku = new Buku();
        initDataBuku(buku);
        try {
            DBHelper dbHelper = new DBHelper(this);
            BukuDataSource datasource = new BukuDataSource(dbHelper);
            datasource.save(buku);
            Toast.makeText(this, "sukses", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "gagal", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateBuku() {
        initDataBuku(receivedBuku);
        try {
            DBHelper dbHelper = new DBHelper(this);
            BukuDataSource dataSource = new BukuDataSource(dbHelper);
            dataSource.update(receivedBuku);
            Toast.makeText(this, "Data Berhasil di Update", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void save() {
        if (receivedBuku != null) {
            updateBuku();
        } else {
            addNewBuku();
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int selectedMenuId = item.getItemId();
        if (selectedMenuId == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etJudul = findViewById(R.id.tJudul);
        etIsbn = findViewById(R.id.tIsbn);
        etTahunTerbit = findViewById(R.id.tTahunTerbit);
        etPenerbit = findViewById(R.id.penerbitSp);
        rKategori = findViewById(R.id.kategoriRb);
        rangkuman = findViewById(R.id.tRangkuman);
        btnSave = findViewById(R.id.btnSimpan);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        etTahunTerbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        long receivedBuku = getIntent().getLongExtra("id_buku", -1);
        if (receivedBuku == -1) {
            Toast.makeText(this, "Tidak menerima data buku", Toast.LENGTH_SHORT).show();
        } else {
            loadDetailDataBuku(receivedBuku);
        }

    }

    private void showDatePickerDialog() {
        Calendar today = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();


    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String selectedDate = String.format("%s-%s-%s", dayOfMonth, (month + 1), year);
        etTahunTerbit.setText(selectedDate);
    }
}