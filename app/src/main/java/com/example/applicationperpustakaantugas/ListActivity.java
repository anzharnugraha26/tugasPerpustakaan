package com.example.applicationperpustakaantugas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.applicationperpustakaantugas.adapter.BukuItemAdapter;
import com.example.applicationperpustakaantugas.datasource.BukuDataSource;
import com.example.applicationperpustakaantugas.datasource.DBHelper;
import com.example.applicationperpustakaantugas.model.Buku;

import java.util.List;

public class ListActivity extends AppCompatActivity {
    private ListView bukulV;
    private BukuItemAdapter adapter;
    private SearchView searchViewLv ;

    private void loadDataBuku() {
        try {
            DBHelper dbHelper = new DBHelper(this);
            BukuDataSource bukuDataSource = new BukuDataSource(dbHelper);
            List<Buku> foundBukuList = bukuDataSource.getAll();
            adapter = new BukuItemAdapter(this, foundBukuList);
            bukulV.setAdapter(adapter);
            bukulV.setOnItemClickListener((parent, view, position, id) -> startDetail(position));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void startForm() {
        Intent intent = new Intent(this, FormActivity.class);
        startActivity(intent);
    }

    private void startFormEdit(int position){
        Intent intent = new Intent(this, FormActivity.class);
        Buku selectedBuku = adapter.getItem(position);
        intent.putExtra("id_buku" , selectedBuku.getId());
        startActivity(intent);
    }

    private void startDetail(int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        Buku selectedBuku = adapter.getItem(position);
        intent.putExtra("id_buku", selectedBuku.getId());
        startActivity(intent);
    }

    private void delete(Buku buku) {
        DBHelper dbHelper = new DBHelper(this);
        BukuDataSource dataSource = new BukuDataSource(dbHelper);
        dataSource.remove(buku);
        adapter.notifyDataSetChanged();
    }

    private void searchBuku(String keyword){
        try {
            DBHelper dbHelper = new DBHelper(this);
            BukuDataSource dataSource = new BukuDataSource(dbHelper);
            List<Buku> foundBukuList = dataSource.findByJudulNamaLike(keyword);

            if (!adapter.isEmpty()){
                adapter.clear();
            }
            adapter.addAll(foundBukuList);
            adapter.notifyDataSetChanged();
        } catch (Exception e){
            Toast.makeText(this, "unable to search buku caused by" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
//
    private void updateVisibilitySearchView(){
        int currentVisibility = searchViewLv.getVisibility();
        if (currentVisibility == View.GONE){
            searchViewLv.setVisibility(View.VISIBLE);
        } else{
            searchViewLv.setQuery("" , true);
            searchViewLv.setVisibility(View.GONE);
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_context, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int selectedPosition = info.position;
        switch (id) {
            case R.id.action_edit:
                startFormEdit(selectedPosition);
                break;
            case R.id.action_delete:
                delete(adapter.getItem(selectedPosition));
                Intent intent = new Intent(ListActivity.this, ListActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Delete Sukses", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int selectedMenuId = item.getItemId();
//        if (selectedMenuId == R.id.addBukuMenu) {
//            starForm();
//        }
        switch (selectedMenuId){
            case R.id.addBukuMenu:
                startForm();
                break;
            case R.id.search_menu:
                updateVisibilitySearchView();
                    break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        bukulV = findViewById(R.id.bukuLv);
        registerForContextMenu(bukulV);

        searchViewLv = findViewById(R.id.buku_sv);

        searchViewLv.setVisibility(View.GONE);
        searchViewLv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchBuku(newText);
                return true;
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadDataBuku();
    }
}