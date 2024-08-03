package com.example.asm_and103_ph45484.Home.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import com.example.asm_and103_ph45484.Home.Model.ProductModel;
import com.example.asm_and103_ph45484.Home.Activity.ActivityProductDetails;
import com.example.asm_and103_ph45484.R;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<ProductModel>  productList;

    public ProductAdapter(List<ProductModel> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_products, parent, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductModel productModel = productList.get(position);
        holder.txtName.setText(productModel.getName());
        holder.txtPrice.setText(String.valueOf(productModel.getPrice()) + ' '+"$ / Kg");
        Glide.with(holder.itemView.getContext())
                .load(productModel.getImage())
                .into(holder.imgProduct);

        holder.imgProduct.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), ActivityProductDetails.class);
            // Truyền dữ liệu sản phẩm qua Intent
            intent.putExtra("productName", productModel.getName());
            intent.putExtra("productPrice", productModel.getPrice());
            intent.putExtra("productImage", productModel.getImage());
            intent.putExtra("productCate", productModel.getCate());
            intent.putExtra("productDes", productModel.getDes());
            intent.putExtra("productWeight", productModel.getWeight());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice;
        ImageView imgProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            txtPrice = itemView.findViewById(R.id.txt_price);
            imgProduct = itemView.findViewById(R.id.img_product);
        }
    }



}
