package vn.edu.usth.mcma.frontend.Showtimes.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.usth.mcma.R;
import vn.edu.usth.mcma.frontend.Showtimes.Models.PaymentMethod;

public class PaymentMethodAdapter extends RecyclerView.Adapter<PaymentMethodAdapter.PaymentMethodViewHolder> {
    private List<PaymentMethod> paymentMethods;
    private PaymentMethod selectedPaymentMethod;
    private OnPaymentMethodSelectedListener listener;

    public PaymentMethodAdapter(List<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public interface OnPaymentMethodSelectedListener {
        void onPaymentMethodSelected(PaymentMethod paymentMethod);
    }

    public void setOnPaymentMethodSelectedListener(OnPaymentMethodSelectedListener listener) {
        this.listener = listener;
    }

    // Method to set selected payment method and update UI
    public void setSelectedPaymentMethod(PaymentMethod paymentMethod) {
        // Find old position to notify item changed
        int oldPosition = -1;
        if (selectedPaymentMethod != null) {
            oldPosition = paymentMethods.indexOf(selectedPaymentMethod);
        }

        // Update selection
        selectedPaymentMethod = paymentMethod;

        // Notify changes
        if (oldPosition != -1) {
            notifyItemChanged(oldPosition);
        }
        if (paymentMethod != null) {
            notifyItemChanged(paymentMethods.indexOf(paymentMethod));
        }
    }

    // Method to get current selected payment method
    public PaymentMethod getSelectedPaymentMethod() {
        return selectedPaymentMethod;
    }

    @NonNull
    @Override
    public PaymentMethodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_payment_method, parent, false);
        return new PaymentMethodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentMethodViewHolder holder, int position) {
        PaymentMethod paymentMethod = paymentMethods.get(position);
        holder.paymentMethodIcon.setImageResource(paymentMethod.getIconResource());
        holder.paymentMethodName.setText(paymentMethod.getName());

        // Set text color to black
        holder.paymentMethodName.setTextColor(holder.itemView.getContext()
                .getResources().getColor(android.R.color.black));

        // Check if this item is selected
        boolean isSelected = paymentMethod.equals(selectedPaymentMethod);
        holder.paymentMethodRadio.setChecked(isSelected);

        // Create click listener for the entire item
        View.OnClickListener clickListener = v -> {
            setSelectedPaymentMethod(paymentMethod);
            if (listener != null) {
                listener.onPaymentMethodSelected(paymentMethod);
            }
        };

        // Set click listener for both the item view and radio button
        holder.itemView.setOnClickListener(clickListener);
        holder.paymentMethodRadio.setOnClickListener(clickListener);

        // Prevent radio button from handling clicks separately
        holder.paymentMethodRadio.setClickable(false);
    }

    @Override
    public int getItemCount() {
        return paymentMethods.size();
    }

    static class PaymentMethodViewHolder extends RecyclerView.ViewHolder {
        ImageView paymentMethodIcon;
        TextView paymentMethodName;
        RadioButton paymentMethodRadio;

        PaymentMethodViewHolder(View itemView) {
            super(itemView);
            paymentMethodIcon = itemView.findViewById(R.id.paymentMethodIcon);
            paymentMethodName = itemView.findViewById(R.id.paymentMethodName);
            paymentMethodRadio = itemView.findViewById(R.id.paymentMethodRadio);
        }
    }
}