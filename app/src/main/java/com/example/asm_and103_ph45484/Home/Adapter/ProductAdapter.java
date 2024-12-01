package com.example.asm_and103_ph45484.Home.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import com.example.asm_and103_ph45484.APIService;
import com.example.asm_and103_ph45484.Home.Fragment.FragmentProduct;
import com.example.asm_and103_ph45484.Home.Model.ProductModel;
import com.example.asm_and103_ph45484.Home.Activity.ActivityProductDetails;
import com.example.asm_and103_ph45484.R;
import com.example.asm_and103_ph45484.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<ProductModel>  productList;
    private Context context;

    public ProductAdapter(Context context, List<ProductModel> productList) {
        this.productList = productList;
        this.context = context;
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
        holder.btnXoa.setOnClickListener(v -> {
            deleteProduct(productModel.get_id(), position);
        });
        holder.btnSua.setOnClickListener(v -> {
            showEditDialog(productModel);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice;
        ImageView imgProduct;
        Button btnXoa,btnSua;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            txtPrice = itemView.findViewById(R.id.txt_price);
            imgProduct = itemView.findViewById(R.id.img_product);
            btnXoa = itemView.findViewById(R.id.btnXoa);
            btnSua = itemView.findViewById(R.id.btnSua);
        }
    }

    private void deleteProduct(String productId, int position) {
        APIService apiService = RetrofitClient.getInstance().create(APIService.class);
        apiService.deleteProduct(productId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Xóa sản phẩm khỏi danh sách
                    productList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, productList.size());
                    Toast.makeText(context, "Sản phẩm đã được xóa!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Xóa sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showEditDialog(ProductModel product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_product, null);
        builder.setView(dialogView);

        EditText editTextName = dialogView.findViewById(R.id.editTextName);
        EditText editTextPrice = dialogView.findViewById(R.id.editTextPrice);
        EditText editTextWeight = dialogView.findViewById(R.id.editTextWeight);
        EditText editTextCate = dialogView.findViewById(R.id.editTextCate);
        EditText editTextDes = dialogView.findViewById(R.id.editTextDes);
        EditText editTextImage = dialogView.findViewById(R.id.editTextImage);
        Button buttonSave = dialogView.findViewById(R.id.buttonSave);

        // Gán giá trị cho các trường
        editTextName.setText(product.getName());
        editTextPrice.setText(String.valueOf(product.getPrice()));
        editTextWeight.setText(String.valueOf(product.getWeight()));
        editTextCate.setText(product.getCate());
        editTextDes.setText(product.getDes());
        editTextImage.setText(product.getImage());

        AlertDialog dialog = builder.create();

        buttonSave.setOnClickListener(v -> {
            // Lấy dữ liệu từ các trường nhập liệu
            String name = editTextName.getText().toString();
            double price = Double.parseDouble(editTextPrice.getText().toString());
            double weight = Double.parseDouble(editTextWeight.getText().toString());
            String cate = editTextCate.getText().toString();
            String des = editTextDes.getText().toString();
            String image = editTextImage.getText().toString();

            // Cập nhật sản phẩm
            ProductModel updatedProduct = new ProductModel(product.get_id(), name, price, weight, cate, des, image);
            updateProduct(updatedProduct, dialog);
        });

        dialog.show();
    }
    private void updateProduct(ProductModel updatedProduct, AlertDialog dialog) {
        APIService apiService = RetrofitClient.getInstance().create(APIService.class);
        apiService.updateProduct(updatedProduct.get_id(), updatedProduct).enqueue(new Callback<ProductModel>() {
            @Override
            public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int position = productList.indexOf(updatedProduct);
                    productList.set(position, response.body()); // Cập nhật với dữ liệu từ API
                    notifyItemChanged(position);
                    Toast.makeText(context, "Sản phẩm đã được cập nhật!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, "Cập nhật sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductModel> call, Throwable t) {
                Toast.makeText(context, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
