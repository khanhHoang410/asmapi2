package com.example.asm_and103_ph45484.Home.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import com.example.asm_and103_ph45484.APIService;
import com.example.asm_and103_ph45484.AccountActivity.ActivityAccount;
import com.example.asm_and103_ph45484.AddProductActivity;
import com.example.asm_and103_ph45484.Home.ActivityHome;
import com.example.asm_and103_ph45484.Home.Adapter.ProductAdapter;
import com.example.asm_and103_ph45484.Home.Model.ProductModel;
import com.example.asm_and103_ph45484.R;
import com.example.asm_and103_ph45484.RetrofitClient;
import com.example.asm_and103_ph45484.databinding.FragmentProductBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentProduct extends Fragment {

    FragmentProductBinding binding;
    List<ProductModel> productlList;
    ProductAdapter productAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productlList = new ArrayList<>();
        productAdapter = new ProductAdapter(getContext(),productlList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        binding.rcProduct.setLayoutManager(gridLayoutManager);
        binding.rcProduct.setAdapter(productAdapter);

        Button buttonAddProduct = binding.getRoot().findViewById(R.id.buttonAddProduct);
        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddProductActivity.class);
                startActivity(intent);
            }
        });

        APIService apiService = RetrofitClient.getInstance().create(APIService.class);
        apiService.getProduct().enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                if (response.isSuccessful()) {
                    List<ProductModel> products = response.body();
                    productAdapter = new ProductAdapter(getContext(),products);
                    binding.rcProduct.setAdapter(productAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<ProductModel>> call, Throwable t) {
                Log.e("Main", t.getMessage());
            }
        });

    }
}