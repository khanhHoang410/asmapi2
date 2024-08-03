package com.example.asm_and103_ph45484.AccountActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.asm_and103_ph45484.R;
import com.example.asm_and103_ph45484.databinding.ActivityForgotPasswordBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class ActivityForgotPassword extends AppCompatActivity {

    ActivityForgotPasswordBinding binding;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        binding.btnReset.setOnClickListener(v -> {
            String email = binding.edtEmail.getText().toString().trim();

            boolean error = false;

            if (email.isEmpty()) {
                binding.edtEmail.setError("Vui lòng nhập Email của bạn!");
                error = true;
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.edtEmail.setError("Sai định dạng email!");
                error = true;
            }

            if (!error) {
                mAuth.fetchSignInMethodsForEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                if (task.getResult() != null && !task.getResult().getSignInMethods().isEmpty()) {
                                    showPasswordResetDialog(email);
                                } else {
                                    Toast.makeText(this, "Email không tồn tại!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(this, "Lỗi khi kiểm tra sự tồn tại của email", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e -> Toast.makeText(this, "Lỗi khi kiểm tra sự tồn tại của email", Toast.LENGTH_SHORT).show());
            }
        });
        binding.btnGoBack.setOnClickListener(v -> onBackPressed());
    }

    private void showPasswordResetDialog(String email) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_reset_password, null);
        builder.setView(dialogView);

        EditText edtNewPassword = dialogView.findViewById(R.id.edtNewPassword);
        EditText edtConfirmPassword = dialogView.findViewById(R.id.edtConfirmPassword);
        Button btnSubmit = dialogView.findViewById(R.id.btnSubmit);

        AlertDialog dialog = builder.create();

        btnSubmit.setOnClickListener(v -> {
            String newPassword = edtNewPassword.getText().toString().trim();
            String confirmPassword = edtConfirmPassword.getText().toString().trim();

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            } else {
                updatePassword(email, newPassword, dialog);
            }
        });

        dialog.show();
    }

    private void updatePassword(String email, String newPassword, AlertDialog dialog) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Đặt lại mật khẩu thành công", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        startActivity(new Intent(this, ActivitySignIn.class));
                    } else {
                        Toast.makeText(this, "Không thể đặt lại mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
