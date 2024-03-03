package com.example.androidgames;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private final List<String> menuItems;
    private final View.OnClickListener onClickListener;

    public MenuAdapter(List<String> menuItems, View.OnClickListener onClickListener) {
        this.menuItems = menuItems;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = menuItems.get(position);
        holder.buttonView.setText(item);

        if (item.equalsIgnoreCase("2048")) {
            holder.imageView.setImageResource(R.drawable.photo_2048);
        } else if (item.equalsIgnoreCase("Senku")) {
            holder.imageView.setImageResource(R.drawable.photo_senku);
        }

        holder.itemView.setTag(item);
        holder.itemView.setOnClickListener(onClickListener);
        holder.buttonView.setTag(item);
        holder.buttonView.setOnClickListener(onClickListener);
    }


    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        Button buttonView;

        ViewHolder(View itemView) {
            super(itemView);
            buttonView = itemView.findViewById(R.id.itemButton);
            imageView = itemView.findViewById(R.id.itemIcon);
        }
    }
}