package vn.edu.usth.mcma.frontend.Showtimes.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import vn.edu.usth.mcma.R;
import vn.edu.usth.mcma.frontend.Showtimes.Models.Seat;
import vn.edu.usth.mcma.frontend.Showtimes.Models.SeatType;

public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.SeatViewHolder> {
    private List<List<Seat>> seatLayout;
    private OnSeatSelectedListener listener;
    private List<Seat> selectedSeats = new ArrayList<>();

    public interface OnSeatSelectedListener {
        void onSeatSelected(Seat seat);
    }

    public SeatAdapter(List<List<Seat>> seatLayout, OnSeatSelectedListener listener) {
        this.seatLayout = seatLayout;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.seat_selection_item, parent, false);
        return new SeatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeatViewHolder holder, int position) {
        int rowIndex = position / 17;
        int seatIndexInRow = position % 17;

        if (rowIndex < seatLayout.size() && seatIndexInRow < seatLayout.get(rowIndex).size()) {
            Seat seat = seatLayout.get(rowIndex).get(seatIndexInRow);

            // Set seat label
            holder.seatTextView.setText(generateSeatLabel(rowIndex, seatIndexInRow));

            // Set background based on seat type and selection state
            updateSeatBackground(holder, seat);

            // Handle seat selection
            holder.itemView.setOnClickListener(v -> {
                if (seat.isAvailable() && seat.getType() != SeatType.SOLD) {
                    toggleSeatSelection(seat, holder);
                }
            });
        }
    }

    private void updateSeatBackground(SeatViewHolder holder, Seat seat) {
        // Default background based on seat type
        int backgroundResId = seat.getType().getDrawableResId();

        // Check if seat is selected
        if (selectedSeats.contains(seat)) {
            backgroundResId = R.drawable.ic_seat_selecting;
        }

        holder.itemView.setBackgroundResource(backgroundResId);
        holder.itemView.setEnabled(seat.isAvailable());
    }

    private void toggleSeatSelection(Seat seat, SeatViewHolder holder) {
        if (selectedSeats.contains(seat)) {
            selectedSeats.remove(seat);
        } else {
            selectedSeats.add(seat);
        }
        notifyItemChanged(getPosition(seat));
        listener.onSeatSelected(seat);
    }

    private int getPosition(Seat seat) {
        for (int row = 0; row < seatLayout.size(); row++) {
            int index = seatLayout.get(row).indexOf(seat);
            if (index != -1) {
                return row * 17 + index;
            }
        }
        return -1;
    }

    private String generateSeatLabel(int rowIndex, int seatIndexInRow) {
        char rowLetter = (char) ('A' + rowIndex);
        return (rowIndex == seatLayout.size() - 1) ?
                rowLetter + "-" + (seatIndexInRow + 1) :
                String.valueOf(seatIndexInRow + 1);
    }

    @Override
    public int getItemCount() {
        return seatLayout.stream().mapToInt(List::size).sum();
    }

    static class SeatViewHolder extends RecyclerView.ViewHolder {
        TextView seatTextView;

        SeatViewHolder(@NonNull View itemView) {
            super(itemView);
            seatTextView = itemView.findViewById(R.id.seatTextView);
        }
    }
}