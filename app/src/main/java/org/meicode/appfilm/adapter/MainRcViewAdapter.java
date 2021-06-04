package org.meicode.appfilm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.meicode.appfilm.R;
import org.meicode.appfilm.model.MovieCategory;
import org.meicode.appfilm.model.Movie;

import java.util.List;

public class MainRcViewAdapter extends RecyclerView.Adapter<MainRcViewAdapter.MainViewHolder> {
    Context cont;
    List<MovieCategory> CategoryList;

    public MainRcViewAdapter(Context cont, List<MovieCategory> cateList) {
        this.cont = cont;
        CategoryList = cateList;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(cont).inflate(R.layout.main_rcview_item_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        holder.CateName.setText(CategoryList.get(position).getCategoryTitle());
        setItemRecycler(holder.itemRcView, CategoryList.get(position).getCateItemm());
    }

    @Override
    public int getItemCount() {
        return CategoryList.size();
    }

    public static final class MainViewHolder extends RecyclerView.ViewHolder{
        TextView CateName;
        RecyclerView itemRcView;
        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews();
        }

        private void initViews() {
            CateName = (TextView) itemView.findViewById(R.id.itemCate);
            itemRcView = (RecyclerView) itemView.findViewById(R.id.item_rcView);
        }
    }
    private void setItemRecycler(RecyclerView rcView, List<Movie> ItemCate){
        ItemRcViewAdapter itemAdapter = new ItemRcViewAdapter(cont, ItemCate);
        rcView.setLayoutManager(new LinearLayoutManager(cont,RecyclerView.HORIZONTAL,false));
        rcView.setAdapter(itemAdapter);
    }

}
