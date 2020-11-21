package com.example.applicationperpustakaantugas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatRatingBar;

import com.example.applicationperpustakaantugas.R;
import com.example.applicationperpustakaantugas.model.Buku;

import java.util.List;

public class BukuItemAdapter extends ArrayAdapter<Buku> {
    public BukuItemAdapter(Context context, List<Buku> objects) {
        super(context, R.layout.item_buku, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            listItemView = inflater.inflate(R.layout.item_buku, parent, false);
        }

        TextView etJudul = listItemView.findViewById(R.id.tJudulBuku);
        TextView etNovel = listItemView.findViewById(R.id.tNovel);
        TextView etPenerbit = listItemView.findViewById(R.id.tPenerbit);
        AppCompatRatingBar appCompatRatingBar = listItemView.findViewById(R.id.rRatingb);

        Buku buku = getItem(position);
        etJudul.setText(buku.getJudul_buku());
        etNovel.setText(buku.getKategori());
        etPenerbit.setText(buku.getPenerbit());
        appCompatRatingBar.setRating(buku.getRatting());

        return listItemView;
    }
}
