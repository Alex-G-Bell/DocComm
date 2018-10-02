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

public class ViewProfileActivity extends AppCompatActivity {


    public FirebaseAuth mAuth;
    public DatabaseReference mDatabase;
    public String id;

    TextView viewName;
    TextView viewGender;
    TextView viewHeight;
    TextView viewWeight;
    TextView viewMedicalCondition;
    TextView viewPhoneNumber;

    public void changeProfile(View view) {
        Intent intent = new Intent(getApplicationContext(), ChangeProfileActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        viewName = findViewById(R.id.viewNameTV);
        viewGender= findViewById(R.id.viewGenderTV);
        viewHeight= findViewById(R.id.viewHeightTV);
        viewWeight= findViewById(R.id.viewWeightTV);
        viewMedicalCondition= findViewById(R.id.viewMedicalConditionTV);
        viewPhoneNumber = findViewById(R.id.viewPhoneNumberTV);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        id = mAuth.getCurrentUser().getUid();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child(id).getValue(User.class);
                String name = user.getFirstName() + " " + user.getLastName();
                viewName.setText(name);
                viewGender.setText(user.getGender());
                viewHeight.setText(user.getHeight() + "cm");
                viewWeight.setText(user.getWeight() + "kg");
                viewMedicalCondition.setText(user.getMedicalCondition());
                viewPhoneNumber.setText(user.getPhoneNumber());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
