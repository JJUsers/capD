package com.example.capd;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capd.entity.StopBusItem;

import java.util.ArrayList;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.ViewHolder> {

    private OnItemClickListener listener;
    private ArrayList<StopBusItem> items;

    public BusAdapter(OnItemClickListener listener, ArrayList<StopBusItem> items) {
        this.listener = listener;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bus, null);

        // Item 사이즈 조절
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);

        // ViewHolder 생성
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 방면
        if (TextUtils.isEmpty(this.items.get(position).brtVianame.content)) {
            holder.txtVia.setText("");
            holder.txtVia.setVisibility(View.GONE);
        } else {
            holder.txtVia.setText(this.items.get(position).brtVianame.content);
            holder.txtVia.setVisibility(View.VISIBLE);
        }

        // 버스번호
        String busNo = this.items.get(position).brtId.content;
        if (!this.items.get(position).brtClass.content.equals("0")) {
            busNo += "-" + this.items.get(position).brtClass.content;
        }
        holder.txtBusNo.setText(busNo);

        // 남은 정류장수
        String rStop = this.items.get(position).RStop.content + "개소 전";
        holder.txtRStop.setText(rStop);

        // 도착 예정시간
        if (Utils.isNumeric(this.items.get(position).RTime.content)) {
            long rTime = Long.parseLong(this.items.get(position).RTime.content);
            rTime *= 1000;
            holder.txtRTime.setText(Utils.getDisplayTime(rTime));
        } else {
            holder.txtRTime.setText("확인안됨");
        }
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtVia, txtBusNo, txtRStop, txtRTime;

        private ViewHolder(View view) {
            super(view);

            this.txtVia = view.findViewById(R.id.txtVia);
            this.txtBusNo = view.findViewById(R.id.txtBusNo);
            this.txtRStop = view.findViewById(R.id.txtRStop);
            this.txtRTime = view.findViewById(R.id.txtRTime);

            view.findViewById(R.id.btnRiding).setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // 리스트 선택
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(view, position);
            }
        }
    }
}
