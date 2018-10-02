package au.edu.uts.doccomm;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class ViewPatientProfileFromDoctor extends AppCompatActivity {

    private String patientID;
    TextView nameTV;
    TextView genderTV;
    TextView heightTV;
    TextView weightTV;
    TextView medicalConditionTV;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public void receivedDataPackets(View view) {
        Intent intent = new Intent(getApplicationContext(), DataPacketListFromDoctor.class);
        intent.putExtra("patientID", patientID);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient_profile_from_doctor);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        nameTV = findViewById(R.id.viewNameTV2);
        genderTV = findViewById(R.id.viewGenderTV2);
        heightTV = findViewById(R.id.viewHeightTV2);
        weightTV = findViewById(R.id.viewWeightTV2);
        medicalConditionTV = findViewById(R.id.viewMedicalConditionTV2);

        patientID = getIntent().getStringExtra("patientID");

        mDatabase.child(patientID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> user = (Map<String, Object>) dataSnapshot.getValue();
                nameTV.setText((String) user.get("firstName") + user.get("lastName"));
                genderTV.setText((String) user.get("gender"));
                heightTV.setText((String) user.get("height"));
                weightTV.setText((String) user.get("weight"));
                medicalConditionTV.setText((String) user.get("medicalCondition"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
