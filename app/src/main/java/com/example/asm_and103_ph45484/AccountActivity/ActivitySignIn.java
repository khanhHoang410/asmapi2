package com.example.asm_and103_ph45484.AccountActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.asm_and103_ph45484.Home.ActivityHome;
import com.example.asm_and103_ph45484.databinding.ActivitySignInBinding;

public class ActivitySignIn extends AppCompatActivity {

    ActivitySignInBinding binding;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(ActivitySignIn.this, ActivityRegister.class);
            startActivity(intent);
        });

        binding.tvPorgotpass.setOnClickListener(v -> {
            Intent intent = new Intent(ActivitySignIn.this, ActivityForgotPassword.class);
            startActivity(intent);
        });

        binding.btnSignIn.setOnClickListener(v -> {
            String email = binding.edtEmail.getText().toString().trim();
            String password = binding.edtPass.getText().toString().trim();

            boolean error = false;
            if (email.isEmpty()) {
                binding.edtEmail.setError("Please enter your Email!");
                error = true;
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.edtEmail.setError("Wrong email format!");
                error = true;
            }
            if (password.isEmpty()) {
                binding.edtPass.setError("Please enter your Password!");
                error = true;
            } else if (password.length() < 6) {
                binding.edtPass.setError("Password must be at least 6 characters!");
                error = true;
            }

            if (!error) {
                // Proceed with sign in
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                // Sign in success
                                FirebaseUser user = mAuth.getCurrentUser();

                                // Optionally, you can check if the user is verified

                                Intent intent = new Intent(ActivitySignIn.this, ActivityHome.class);
                                startActivity(intent);
                                finish();  // Close the current activity
                                // Show message that email needs to be verified
                                Toast.makeText(ActivitySignIn.this, "Đăng nhập thành công.", Toast.LENGTH_SHORT).show();

                            } else {
                                // Sign in failed
                                Toast.makeText(ActivitySignIn.this, "Đăng nhập thất bại: " + task.getException().getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }
}
