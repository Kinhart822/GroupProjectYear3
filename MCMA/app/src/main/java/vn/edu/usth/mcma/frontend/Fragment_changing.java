package vn.edu.usth.mcma.frontend;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import vn.edu.usth.mcma.frontend.Home.HomeFragment;
import vn.edu.usth.mcma.frontend.Showtimes.UI.LaunchtimeFragment;
import vn.edu.usth.mcma.frontend.Store.UI.StoreFragment;

public class Fragment_changing extends FragmentStateAdapter {
    public Fragment_changing(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new vn.edu.usth.mcma.frontend.Showtimes.UI.LaunchtimeFragment();
            case 2:
                return new vn.edu.usth.mcma.frontend.Store.UI.StoreFragment();
            case 3:
                return new vn.edu.usth.mcma.frontend.Feedback.FeedbackFragment();
            case 4:
                return new vn.edu.usth.mcma.frontend.Personal.PersonalFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {

        return 4;
    }
}
