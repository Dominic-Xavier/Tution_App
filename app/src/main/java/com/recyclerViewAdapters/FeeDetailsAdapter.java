package com.recyclerViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tutionapp.R;

import java.util.List;

public class FeeDetailsAdapter extends RecyclerView.Adapter<FeeDetailsAdapter.ViewHolder> {

    Context context;
    List<String> mon_yer, paid, balance, totalAmount;
    OnFeeDetailsClick feeDetailsClick;

    public FeeDetailsAdapter(Context context, List<String> mon_yer, List<String> paid, List<String> balance, List<String> totalAmount,
                             OnFeeDetailsClick feeDetailsClick){
        this.context = context;
        this.mon_yer = mon_yer;
        this.paid = paid;
        this.balance = balance;
        this.totalAmount = totalAmount;
        this.feeDetailsClick = feeDetailsClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fee_details_design, parent, false);
        FeeDetailsAdapter.ViewHolder viewHolder = new FeeDetailsAdapter.ViewHolder(view, feeDetailsClick);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.month_year.setText(mon_yer.get(position));
        holder.feesPaid.setText(paid.get(position));
        holder.feesBalance.setText(balance.get(position));
        holder.feesTotal.setText(totalAmount.get(position));
    }

    @Override
    public int getItemCount() {
        return mon_yer.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        AppCompatTextView month_year, feesPaid, feesBalance, feesTotal;
        LinearLayoutCompat feesLayout;

        public ViewHolder(@NonNull View itemView, OnFeeDetailsClick feeDetailsClick) {
            super(itemView);

            feesLayout = itemView.findViewById(R.id.feeLayout);
            month_year = itemView.findViewById(R.id.fee_year_and_month);
            feesPaid = itemView.findViewById(R.id.fee_paid);
            feesBalance = itemView.findViewById(R.id.fee_received);
            feesTotal = itemView.findViewById(R.id.fee_total);

            feesLayout.setOnClickListener(v -> {
                feeDetailsClick.onClick(getAdapterPosition());
            });
        }
    }

    @FunctionalInterface
    interface OnFeeDetailsClick{
        void onClick(int position);
    }
}