package au.edu.uts.doccomm;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class DoctorProfileViewActivity extends AppCompatActivity {
    String id;
    private DatabaseReference mDatabase;

    TextView nameTV;
    TextView genderTV;
    TextView occupationTV;
    TextView contactNumberTV;
    Button pairDoctorButton;
    Boolean pairedView;

    public void pairDoctor(View view) {
        final Intent intent = new Intent(getApplicationContext(), UserActivty.class);
        intent.putExtra("doctorID", id);

        final String patientID = getIntent().getStringExtra("patientID");
        mDatabase.child(patientID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mDatabase.child(id).child("patients").child(patientID).setValue(true);
                mDatabase.child(patientID).child("doctors").child(id).setValue(true);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile_view);

        pairedView = getIntent().getBooleanExtra("pairedView", false);

        id = getIntent().getExtras().getString("doctorID");
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        nameTV = findViewById(R.id.doctorNameTV);
        genderTV = findViewById(R.id.doctorGenderTV);
        occupationTV = findViewById(R.id.doctorOccupationTV);
        contactNumberTV = findViewById(R.id.doctorContactNumberTV);
        pairDoctorButton = findViewById(R.id.pairDoctorBtn);

        if(pairedView) {
            pairDoctorButton.setVisibility(View.INVISIBLE);
        }

        mDatabase.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> doctor = (Map<String, Object>) dataSnapshot.getValue();
                nameTV.setText(doctor.get("firstName") + " " + doctor.get("lastName"));
                genderTV.setText((String) doctor.get("gender"));
                occupationTV.setText((String) doctor.get("occupation"));
                contactNumberTV.setText((String) doctor.get("phoneNumber"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
