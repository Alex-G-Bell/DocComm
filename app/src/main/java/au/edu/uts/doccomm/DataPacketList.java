package au.edu.uts.doccomm;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataPacketList extends AppCompatActivity {

    ListView packetLV;

    public ArrayList<String> dataPacketText;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String id;


    public String dataPacketString(HashMap<String, String> dataPacket) {
        String dataPackets = "Name: " + dataPacket.get("name") + " Gender: " + dataPacket.get("gender") + "\n" +
                "Height: " + dataPacket.get("height") + " Weight: " + dataPacket.get("weight") + "\n" +
                "Medical Condition: " + dataPacket.get("medicalCondition") + "\n" +
                "Addition medical information: " + dataPacket.get("medicalData");

        return dataPackets;
    }

    private void collectDataPackets(Map<String,Object> users) {
        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){
            //Get dataPacket
            HashMap<String, String> dataPacket = (HashMap<String,String>) entry.getValue();
            dataPacketText.add(dataPacketString(dataPacket));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_packet_list);

        dataPacketText = new ArrayList<>();

        packetLV = findViewById(R.id.dataPacketLV);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataPacketText);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        id = mAuth.getCurrentUser().getUid();

        DatabaseReference ref = mDatabase.child(id);
        ref.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("DataPacket")) {
                            collectDataPackets((Map<String,Object>) dataSnapshot.child("DataPacket").getValue());
                        }
                        packetLV.setAdapter(arrayAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        //handle databaseError
                    }
                });
    }
}
