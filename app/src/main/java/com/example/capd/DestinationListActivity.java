package com.example.capd;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capd.CoordinatesCalculate.Coordinates;
import com.example.capd.CoordinatesCalculate.GetCoordinatesRepo;


// 목적지 검색 후 결과 리스트 액티비티
public class DestinationListActivity extends AppCompatActivity {

    String destination;

    RecyclerView recyclerView;
    DestinationAdapter destinationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.destination_list);

        Intent intent = new Intent(this.getIntent());
        destination = intent.getStringExtra("destination");


        recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        GetCoordinatesRepo.getINSTANCE().getCoordinatesList(destination, 1, 10, new GetCoordinatesRepo.CoordinatesResponseListener() {
            @Override
            public void onSuccessResponse(Coordinates coordinatesData) {

                Coordinates c = coordinatesData;
                destinationAdapter = new DestinationAdapter(c.getDocumentsList());

                recyclerView.setAdapter(destinationAdapter);

            }

            @Override
            public void onFailResponse() {
                Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
            }
        });





    }
}
