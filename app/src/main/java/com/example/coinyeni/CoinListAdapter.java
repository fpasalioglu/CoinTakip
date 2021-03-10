package com.example.coinyeni;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coinyeni.interfaces.CustomItemClickListener;
import com.example.coinyeni.models.Coin;

import java.util.ArrayList;

public class CoinListAdapter extends RecyclerView.Adapter<CoinListAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView karText;
        private LinearLayout itemLayout;

        public ViewHolder(View view) {
            super(view);
            itemLayout = (LinearLayout) view.findViewById(R.id.item);
            title = (TextView) view.findViewById(R.id.title);
            karText = (TextView) view.findViewById(R.id.textViewKar);
        }
    }

    ArrayList<Coin> coinList;
    CustomItemClickListener listener;

    public CoinListAdapter(ArrayList<Coin> coinList, CustomItemClickListener listener) {
        this.coinList = coinList;
        this.listener = listener;
    }

    @Override
    public CoinListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.title.setText(coinList.get(position).getAd());
        holder.karText.setText(coinList.get(position).getYuzdeKar());

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return coinList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setItems(ArrayList<Coin> coinList){
        this.coinList = coinList;
    }
}


