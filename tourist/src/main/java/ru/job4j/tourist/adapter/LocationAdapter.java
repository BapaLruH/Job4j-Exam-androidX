package ru.job4j.tourist.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.job4j.tourist.R;
import ru.job4j.tourist.db.model.ModelLocation;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ModelLocationViewHolder> {

    private List<ModelLocation> items;
    private SelectItemListener callback;

    public LocationAdapter(SelectItemListener listener) {
        this.items = new ArrayList<>();
        this.callback = listener;
    }

    public void setItems(List<ModelLocation> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void addItem(ModelLocation location) {
        items.add(location);
        notifyItemInserted(items.size());
    }

    public void removeItem(ModelLocation location) {
        int position = getItemPosition(location);
        if (position != -1) {
            items.remove(location);
            notifyItemRemoved(position);
        }
    }

    private int getItemPosition(ModelLocation location) {
        int position = -1;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equals(location)) {
                position = i;
                break;
            }
        }
        return position;
    }

    @NonNull
    @Override
    public ModelLocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.location, parent, false);
        return new ModelLocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModelLocationViewHolder holder, int position) {
        final ModelLocation location = this.items.get(position);
        holder.tvTitle.setText(String.valueOf(location.title));
        holder.tvLongitude.setText(String.valueOf(location.longitude));
        holder.tvLatitude.setText(String.valueOf(location.latitude));
        holder.view.setOnClickListener(v -> callback.onItemSelect(location));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ModelLocationViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView tvTitle;
        TextView tvLongitude;
        TextView tvLatitude;

        public ModelLocationViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            tvTitle = view.findViewById(R.id.title);
            tvLongitude = view.findViewById(R.id.longitude);
            tvLatitude = view.findViewById(R.id.latitude);
        }
    }

    public interface SelectItemListener {
        void onItemSelect(ModelLocation location);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (callback != null) {
            callback = null;
        }
    }
}
