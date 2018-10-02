package au.edu.uts.doccomm;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ChangeProfileActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String id;

    EditText firstName;
    EditText lastName;
    RadioButton male;
    RadioButton female;
    EditText weight;
    EditText height;
    EditText medicalCondition;
    EditText phoneNumber;

    public void updateProfile(View view) {
        Map<String, Object> updateUser = new HashMap<>();
        updateUser.put("firstName", firstName.getText().toString());
        updateUser.put("lastName", lastName.getText().toString());
        updateUser.put("height", height.getText().toString());
        updateUser.put("weight", weight.getText().toString());
        updateUser.put("medicalCondition", medicalCondition.getText().toString());

        if(male.isChecked()) {
            updateUser.put("gender", "Male");
        } else {
            updateUser.put("gender", "Female");
        }

        mDatabase.child(id).updateChildren(updateUser);

        Toast.makeText(ChangeProfileActivity.this,"Updated", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(), ViewProfileActivity.class);
        startActivity(intent);

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        id = mAuth.getCurrentUser().getUid();

        firstName = findViewById(R.id.firstNameEt);
        lastName = findViewById(R.id.lastNameEt);
        male = findViewById(R.id.maleRb);
        female = findViewById(R.id.femaleRb);
        height = findViewById(R.id.viewHeightET);
        weight = findViewById(R.id.viewWeightET);
        medicalCondition = findViewById(R.id.viewMedicalConditionET);
        phoneNumber = findViewById(R.id.viewPhoneNumberET);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        id = mAuth.getCurrentUser().getUid();

        mDatabase.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                firstName.setText(user.getFirstName());
                lastName.setText(user.getLastName());
                height.setText(user.getHeight());
                weight.setText(user.getWeight());
                medicalCondition.setText(user.getMedicalCondition());
                phoneNumber.setText(user.getPhoneNumber());

                if(user.getGender().equals("Male")) {
                    male.setChecked(true);
                }
                else {
                    female.setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
