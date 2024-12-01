package com.example.asm_and103_ph45484.Cart;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.asm_and103_ph45484.APIService;
import com.example.asm_and103_ph45484.Cart.Adapter.BillAdapter;
import com.example.asm_and103_ph45484.Cart.Model.BillModel;
import com.example.asm_and103_ph45484.Notification.HistoryModel;
import com.example.asm_and103_ph45484.R;
import com.example.asm_and103_ph45484.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCart extends Fragment {

    private RecyclerView recyclerView;
    private BillAdapter billAdapter;
    private List<BillModel> billList;
    private TextView txtTotalPrice;
    APIService apiService;
    Button btnPay;
    private double totalPrice;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        recyclerView = view.findViewById(R.id.rcBill);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        txtTotalPrice = view.findViewById(R.id.txt_total);

        apiService = RetrofitClient.getInstance().create(APIService.class);



        fetchTotalPrice();
        loadBills();

        btnPay = view.findViewById(R.id.btnPay);
        btnPay.setOnClickListener(v -> {
            // Thêm hóa đơn vào lịch sử
            Call<Void> addToHistoryCall = apiService.addBillsToHistory();
            addToHistoryCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        // Xóa tất cả hóa đơn sau khi thêm vào lịch sử thành công
                        Call<Void> deleteAllBillsCall = apiService.deleteAllBills();
                        deleteAllBillsCall.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(getContext(), "Bills added to history and deleted successfully", Toast.LENGTH_SHORT).show();
                                    // Cập nhật danh sách hóa đơn và tổng tiền
                                    updateBill();
                                } else {
                                    Toast.makeText(getContext(), "Failed to delete bills: " + response.message(), Toast.LENGTH_SHORT).show();
                                    Log.e("API_ERROR", "Response code: " + response.code());
                                    Log.e("API_ERROR", "Response message: " + response.message());
                                    Log.e("API_ERROR", "Response error body: " + response.errorBody());
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e("API_ERROR", "Request failed", t);
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "Failed to add bills to history: " + response.message(), Toast.LENGTH_SHORT).show();
                        Log.e("API_ERROR", "Response code: " + response.code());
                        Log.e("API_ERROR", "Response message: " + response.message());
                        Log.e("API_ERROR", "Response error body: " + response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", "Request failed", t);
                }
            });
        });



        return view;
    }



    // Trong FragmentCart hoặc Activity tương ứng



    private void fetchTotalPrice() {
        Call<TotaPriceResponse> call = apiService.getTotalPrice();
        call.enqueue(new Callback<TotaPriceResponse>() {
            @Override
            public void onResponse(Call<TotaPriceResponse> call, Response<TotaPriceResponse> response) {
                if (response.isSuccessful()) {
                    totalPrice = response.body().getTotalPrice();
                    txtTotalPrice.setText("Total Price" + "\n" + totalPrice + "$");
                }else {
                    Toast.makeText(getContext(), "Failed to fetch total price", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TotaPriceResponse> call, Throwable t) {

            }
        });
    }



    private void loadBills() {
        APIService apiService = RetrofitClient.getInstance().create(APIService.class);
        Call<List<BillModel>> call = apiService.getBill();
        call.enqueue(new Callback<List<BillModel>>() {
            @Override
            public void onResponse(Call<List<BillModel>> call, Response<List<BillModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    billList = response.body();
                    billAdapter = new BillAdapter(billList, getContext(), FragmentCart.this);
                    recyclerView.setAdapter(billAdapter);

                    fetchTotalPrice();
                }
            }

            @Override
            public void onFailure(Call<List<BillModel>> call, Throwable t) {
                // Handle failure
            }
        });
    }



    public void updateBill() {
        // Re-fetch the bills and total price after an update (addition or deletion)
        loadBills();
    }
}
