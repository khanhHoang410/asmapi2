package com.example.asm_and103_ph45484.Cart.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import com.example.asm_and103_ph45484.APIService;
import com.example.asm_and103_ph45484.Cart.FragmentCart;
import com.example.asm_and103_ph45484.Cart.Model.BillModel;
import com.example.asm_and103_ph45484.R;
import com.example.asm_and103_ph45484.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder> {

    private List<BillModel> billList;
    private Context context;
    private FragmentCart fragmentCart;


    public BillAdapter(List<BillModel> billList, Context context, FragmentCart fragmentCart) {
        this.billList = billList;
        this.context = context;
        this.fragmentCart = fragmentCart;
    }
    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new BillViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, @SuppressLint("RecyclerView") int position) {
        BillModel billModel = billList.get(position);
        holder.txtName.setText(billModel.getProductName());
        holder.txtPrice.setText(String.valueOf(billModel.getProductPrice()) + "$");
        holder.txtCate.setText(billModel.getProductCate());
        holder.edtKg.setText(String.valueOf(billModel.getProductWeight()));

        Glide.with(holder.itemView.getContext())
                .load(billModel.getProductImage())
                .into(holder.imgProduct);

        holder.btnDelete.setOnClickListener(v -> {
            APIService apiService = RetrofitClient.getInstance().create(APIService.class);
            Call<Void> deleteCall = apiService.deleteBill(billModel.get_id());
            deleteCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        // Xóa hóa đơn thành công, cập nhật danh sách
                        billList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, billList.size());

                        // Notify FragmentCart to update total price
                        if (fragmentCart != null) {
                            fragmentCart.updateBill();
                        }
                    } else {
                        Toast.makeText(v.getContext(), "Xoá không thành công.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(v.getContext(), "Lỗi mạng.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return billList.size();
    }

    class BillViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice, txtCate;
        EditText edtKg;
        ImageView imgProduct, btnDelete;

        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            txtPrice = itemView.findViewById(R.id.txt_price);
            txtCate = itemView.findViewById(R.id.txt_cate);
            edtKg = itemView.findViewById(R.id.edt_kg);
            imgProduct = itemView.findViewById(R.id.img_product);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
