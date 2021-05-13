package org.meicode.appfilm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.meicode.appfilm.R;
import org.meicode.appfilm.model.AllCategory;
import org.meicode.appfilm.model.CategoryItem;

import java.util.ArrayList;

public class MainRcViewAdapter extends RecyclerView.Adapter<MainRcViewAdapter.MainViewHolder> {
    Context cont;
    ArrayList<AllCategory> CateList;

    public MainRcViewAdapter(Context cont, ArrayList<AllCategory> cateList) {
        this.cont = cont;
        CateList = cateList;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(cont).inflate(R.layout.main_rcview_item_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        holder.CateName.setText(CateList.get(position).getCategoryTitle());
        setItemRecycler(holder.itemRcView, CateList.get(position).getCateItemm());
    }

    @Override
    public int getItemCount() {
        return CateList.size();
    }
    public static final class MainViewHolder extends RecyclerView.ViewHolder{
        TextView CateName;
        RecyclerView itemRcView;
        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            CateName = (TextView) itemView.findViewById(R.id.itemCate);
            itemRcView = (RecyclerView) itemView.findViewById(R.id.item_rcView);

        }
    }
    private void setItemRecycler(RecyclerView rcView, ArrayList<CategoryItem> ItemCate){
        ItemRcViewAdapter itemAdapter = new ItemRcViewAdapter(cont,ItemCate);
        rcView.setLayoutManager(new LinearLayoutManager(cont,RecyclerView.HORIZONTAL,false));
        rcView.setAdapter(itemAdapter);
    }

}
