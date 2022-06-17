package com.example.capd;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.capd.CoordinatesCalculate.Data;

import java.util.List;

// 리사이클러뷰 생성을 위한 어뎁터
public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.DestinationViewHolder> {
    private static final String TAG = "capD";

    private List<Data> dataList;
    private Context context;


    public DestinationAdapter(List<Data> dataList){
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public DestinationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.destination_list_item, parent, false);
        DestinationViewHolder viewHolder = new DestinationViewHolder(view);
        context = parent.getContext();
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull DestinationViewHolder holder, final int position) {
        final Data item = dataList.get(position);
        holder.setItem(item);

        holder.destinationName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReconfirmDestinationActivity.class);
                String destination = dataList.get(position).getPlace_name();
                String longitude = dataList.get(position).getX();       // 경도
                String latitude = dataList.get(position).getY();        // 위도

                Log.d(TAG, "X:" + latitude);
                Log.d(TAG, "Y:" + longitude);

                intent.putExtra("destination", destination);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                context.startActivity(intent);
;            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }



    public class DestinationViewHolder extends RecyclerView.ViewHolder {

        TextView destinationName;

        public DestinationViewHolder(View itemView){
            super(itemView);
            destinationName = (TextView)itemView.findViewById(R.id.destinationName);

        }
        public void setItem(Data item){
            destinationName.setText(item.getPlace_name());
        }


    }


}

