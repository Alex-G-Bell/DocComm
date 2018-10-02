package au.edu.uts.doccomm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewPatientsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private String doctorID;

    ListView patientListView;

    public ArrayList<String> patientList;
    public ArrayList<String> patientIDList;

    public String patientToString(Map<String, Object> dataPacket) {
        String patientInfo = dataPacket.get("firstName") + " " + dataPacket.get("lastName");

        return patientInfo;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_hire);

        patientListView = findViewById(R.id.doctorLV);
        patientList = new ArrayList<>();
        patientIDList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        doctorID = mAuth.getCurrentUser().getUid();

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, patientList);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> user;
                for (DataSnapshot snapshot : dataSnapshot.child(doctorID).child("patients").getChildren()) {
                    String patientID = snapshot.getKey();
                    user = (Map<String, Object>) dataSnapshot.child(patientID).getValue();
                    patientList.add(patientToString(user));
                    patientIDList.add((String) user.get("userId"));
                }
                patientListView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        patientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
                Intent intent = new Intent(getApplicationContext(), ViewPatientProfileFromDoctor.class);
                intent.putExtra("doctorID", mAuth.getCurrentUser().getUid());
                intent.putExtra("patientID", patientIDList.get(position));
                startActivity(intent);
            }
        });
    }
}
