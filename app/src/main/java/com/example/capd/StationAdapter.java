package com.example.capd;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capd.entity.StationItem;

import java.util.ArrayList;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.ViewHolder> {

    private ArrayList<StationItem> items;

    private CheckBox ckSelectBefore;            // 이전 선택 정보
    private int beforePosition = -1;            // 이전 선택 position

    public StationAdapter(ArrayList<StationItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_station, null);

        // Item 사이즈 조절
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);

        // ViewHolder 생성
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 참고사항
        if (TextUtils.isEmpty(this.items.get(position).reMark.content)) {
            holder.txtReMark.setText("");
            holder.txtReMark.setVisibility(View.GONE);
        } else {
            holder.txtReMark.setText(this.items.get(position).reMark.content);
            holder.txtReMark.setVisibility(View.VISIBLE);
        }

        // 정류장명
        String stationName = TextUtils.isEmpty(this.items.get(position).stopKname.content) ?
                "" : this.items.get(position).stopKname.content;
        holder.txtStationName.setText(stationName);

        // 정류장 id
        String stationId = TextUtils.isEmpty(this.items.get(position).stopStandardid.content) ?
                "" : this.items.get(position).stopStandardid.content;
        holder.txtStationId.setText(stationId);

        // 선택 여부
        holder.ckSelect.setChecked(this.items.get(position).selected);
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        CheckBox ckSelect;
        TextView txtReMark, txtStationName, txtStationId;

        public ViewHolder(View view) {
            super(view);

            this.ckSelect = view.findViewById(R.id.ckSelect);
            this.txtReMark = view.findViewById(R.id.txtReMark);
            this.txtStationName = view.findViewById(R.id.txtStationName);
            this.txtStationId = view.findViewById(R.id.txtStationId);

            view.setOnClickListener(this);
            this.ckSelect.setOnCheckedChangeListener(this);
        }

        @Override
        public void onClick(View view) {
            // 정류장을 클릭시 체크박스 선택하기
            if (this.ckSelect.isEnabled()) {
                this.ckSelect.setChecked(!this.ckSelect.isChecked());
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            int position = getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) {
                return;
            }

            items.get(position).selected = isChecked;

            if (isChecked) {
                if (beforePosition != position && beforePosition != -1) {
                    if (ckSelectBefore != null) {
                        // 이전 선택 clear
                        ckSelectBefore.setChecked(false);
                    }
                }

                // 체크된 항목 저장
                beforePosition = position;
                ckSelectBefore = ckSelect;
            }
        }
    }
}
