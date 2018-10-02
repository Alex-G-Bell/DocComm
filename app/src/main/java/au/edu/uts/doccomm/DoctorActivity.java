package au.edu.uts.doccomm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class DoctorActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    public void logout(View view) {
        mAuth.signOut();
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void viewPatients(View view) {
        Intent intent = new Intent(getApplicationContext(), ViewPatientsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        mAuth = FirebaseAuth.getInstance();
    }
}