package vn.edu.usth.mcma.frontend.Showtimes.Adapters;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import vn.edu.usth.mcma.R;
import vn.edu.usth.mcma.frontend.Showtimes.Models.ComboItem;

public class ComboAdapter extends RecyclerView.Adapter<ComboAdapter.ComboViewHolder> {
    private List<ComboItem> comboItems;
    private OnTotalPriceChangedListener listener;

    public ComboAdapter(List<ComboItem> comboItems) {
        this.comboItems = comboItems;
    }

    // Custom listener interface for price updates
    public interface OnTotalPriceChangedListener {
        void onTotalPriceChanged(List<ComboItem> comboItems);
    }
    public void setTotalPriceChangedListener(OnTotalPriceChangedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ComboViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.select_item_combo, parent, false);
        return new ComboViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComboViewHolder holder, int position) {
        ComboItem item = comboItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return comboItems != null ? comboItems.size() : 0;
    }

    public List<ComboItem> getSelectedComboItems() {
        return comboItems.stream()
                .filter(item -> item.getQuantity() > 0)
                .collect(Collectors.toList());
    }

    // Updates the quantity of a ComboItem and recalculates the total price
    private void updateQuantity(int position, int delta) {
        ComboItem item = comboItems.get(position);
        int newQuantity = Math.max(0, item.getQuantity() + delta);

        if (newQuantity != item.getQuantity()) {
            item.setQuantity(newQuantity);
            notifyItemChanged(position); // Update only the modified item
            if (listener != null) {
                listener.onTotalPriceChanged(comboItems); // Notify the listener to update the total price
            }
        }
    }

    // ViewHolder class for ComboItem
    class ComboViewHolder extends RecyclerView.ViewHolder {
        private TextView nameText;
        private TextView priceText;
        private TextView quantityText;
        private ImageView plusButton;
        private ImageView minusButton;
        private ImageView comboImage;

        ComboViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.combo_name);
            priceText = itemView.findViewById(R.id.combo_price);
            quantityText = itemView.findViewById(R.id.combo_quantity);
            plusButton = itemView.findViewById(R.id.plus_button);
            minusButton = itemView.findViewById(R.id.minus_button);
            comboImage = itemView.findViewById(R.id.combo_image);

            // Setting up button click listeners to adjust quantity
            plusButton.setOnClickListener(v -> updateQuantity(getAdapterPosition(), 1));
            minusButton.setOnClickListener(v -> updateQuantity(getAdapterPosition(), -1));
        }

        // Binds data to the view holder elements
        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        void bind(ComboItem item) {
            nameText.setText(item.getName());
            priceText.setText(String.format("$%.2f", item.getPrice()));
            quantityText.setText(String.valueOf(item.getQuantity()));

            // Check if image URL is null or empty, then load default image, otherwise load from URL
            if (item.getImageUrl() == null || item.getImageUrl().isEmpty()) {
                comboImage.setImageResource(R.drawable.combo_image_1);
            } else {
                Glide.with(itemView.getContext())
                        .load(item.getImageUrl())
                        .placeholder(R.drawable.combo_image_1) // Placeholder while loading image
                        .error(R.drawable.combo_image_1) // Fallback image if image URL fails to load
                        .into(comboImage);
            }
        }

    }
}