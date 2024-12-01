package com.example.asm_and103_ph45484;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asm_and103_ph45484.Home.Model.ProductModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity {
    private EditText editTextName, editTextPrice, editTextWeight, editTextCate, editTextDes, editTextImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        editTextName = findViewById(R.id.editTextName);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextWeight = findViewById(R.id.editTextWeight);
        editTextCate = findViewById(R.id.editTextCate);
        editTextDes = findViewById(R.id.editTextDes);
        editTextImage = findViewById(R.id.editTextImage);
        Button buttonSave = findViewById(R.id.buttonSave);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProduct();

            }
        });
    }

    private void saveProduct() {
        String name = editTextName.getText().toString();
        double price = Double.parseDouble(editTextPrice.getText().toString());
        double weight = Double.parseDouble(editTextWeight.getText().toString());
        String cate = editTextCate.getText().toString();
        String des = editTextDes.getText().toString();
        String image = editTextImage.getText().toString();

        ProductModel productModel = new ProductModel(null,name, price, weight, cate, des, image);

        APIService apiService = RetrofitClient.getInstance().create(APIService.class);
        apiService.createProduct(productModel).enqueue(new Callback<ProductModel>() {
            @Override
            public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddProductActivity.this, "Sản phẩm đã được thêm!", Toast.LENGTH_SHORT).show();
                    finish(); // Quay lại fragment trước đó
                } else {
                    Toast.makeText(AddProductActivity.this, "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductModel> call, Throwable t) {
                Toast.makeText(AddProductActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}