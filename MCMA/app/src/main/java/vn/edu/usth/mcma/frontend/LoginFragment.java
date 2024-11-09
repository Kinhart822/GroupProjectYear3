package vn.edu.usth.mcma.frontend;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import vn.edu.usth.mcma.R;

public class LoginFragment extends Fragment {

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        editTextEmail = view.findViewById(R.id.editText);
        editTextPassword = view.findViewById(R.id.editText2);
        buttonLogin = view.findViewById(R.id.login_button);

        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();

            if (validateLogin(email, password)) {

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("123", getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.apply();

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            } else {

                Toast.makeText(getActivity(), "Enter email or phone number to log in", Toast.LENGTH_SHORT).show();
            }
        });

        TextView create_account = view.findViewById(R.id.create_account);
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(requireContext(), Register_Activity.class );
                startActivity(i);
            }
        });

        TextView forgot_password = view.findViewById(R.id.forgot_password);
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(requireContext(), ForgotPassword_Activity.class );
                startActivity(i);
            }
        });

        return view;
    }

    private boolean validateLogin(String email, String password) {
        return !email.isEmpty() && !password.isEmpty();
    }
}