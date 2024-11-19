package vn.edu.usth.mcma.frontend.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.edu.usth.mcma.R;

public class NowShowingFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout
        View v = inflater.inflate(R.layout.fragment_now_showing, container, false);

        // Khởi tạo RecyclerView
        RecyclerView recyclerView = v.findViewById(R.id.recyclerview_now_showing);

        // Sử dụng LinearLayoutManager theo hướng ngang
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Tạo danh sách phim "Now Showing"
        List<FilmItem> items = new ArrayList<>();
        items.add(new FilmItem("Grace Morgan", "(Horror)", R.drawable.movie1));
        items.add(new FilmItem("Isabella Lewis", "(Comedy)", R.drawable.movie3));
        items.add(new FilmItem("Evelyn", "(Sci-Fi)", R.drawable.movie4));
        items.add(new FilmItem("Jack", "(Action)", R.drawable.movie5));
        items.add(new FilmItem("Tino", "(Horror)", R.drawable.movie7));

        // Thiết lập Adapter và sự kiện click
        FilmAdapter adapter = new FilmAdapter(requireContext(), items, position -> {
            FilmItem selectedFilm = items.get(position);
            Toast.makeText(requireContext(), "Selected Film: " + selectedFilm.getName(), Toast.LENGTH_SHORT).show();
        });
        recyclerView.setAdapter(adapter);

        return v;
    }
}
