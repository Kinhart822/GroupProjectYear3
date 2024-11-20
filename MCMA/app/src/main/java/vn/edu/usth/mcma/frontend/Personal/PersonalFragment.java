package vn.edu.usth.mcma.frontend.Personal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import vn.edu.usth.mcma.R;
import vn.edu.usth.mcma.frontend.MainActivity;

public class PersonalFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_personal, container, false);

        // Find the ImageButton by its ID (assuming you have an ImageButton in your layout)
        ImageButton closeButton = v.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity() instanceof vn.edu.usth.mcma.frontend.MainActivity){
                    ((vn.edu.usth.mcma.frontend.MainActivity) getActivity()).close_to_home_page();
                }
            }
        });

        LinearLayout logout_button = v.findViewById(R.id.account_information_log_out);
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment loginFragment = new vn.edu.usth.mcma.frontend.Login.LoginFragment();
                FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(android.R.id.content, loginFragment);
                fragmentTransaction.commit();

            }
        });

        LinearLayout to_edit_update = v.findViewById(R.id.account_information_edit_update);
        to_edit_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(requireContext(), vn.edu.usth.mcma.frontend.Personal.Edit_Update_Account_Info_Activity.class);
                startActivity(i);
            }
        });



        return v;
    }

}
