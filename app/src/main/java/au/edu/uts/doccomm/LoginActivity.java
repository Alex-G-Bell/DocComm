package au.edu.uts.doccomm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSignIn;
    private EditText etEmailAddress;
    private EditText etPassword;
    private TextView tvSignUp;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private TextView tvResetPassword;
    public DatabaseReference mDatabase;
    public String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        //If the user is already logged in
     /*   if(mAuth.getCurrentUser() != null){
            finish();
            id = mAuth.getCurrentUser().getUid();
             mDatabase.child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Map<String, Object> user = (Map<String, Object>) dataSnapshot.getValue();
                    String userType = (String) user.get("userType");
                    if(userType.equals("patient")) {
                        startActivity(new Intent(LoginActivity.this, UserActivty.class));
                    }
                    else {
                        startActivity(new Intent(LoginActivity.this, DoctorActivity.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }*/

        progressDialog = new ProgressDialog(this);

        etEmailAddress = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        tvSignUp = (TextView) findViewById(R.id.tvSignUp);
        tvResetPassword = (TextView) findViewById(R.id.tvResetPassword);

        btnSignIn.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
        tvResetPassword.setOnClickListener(this);
    }

    public void checkEmailVerification() {
        FirebaseUser firebaseUser = mAuth.getInstance().getCurrentUser();
        Boolean emailFlag = firebaseUser.isEmailVerified();
        id = mAuth.getCurrentUser().getUid();

        if(emailFlag) {
            mDatabase.child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Map<String, Object> user = (Map<String, Object>) dataSnapshot.getValue();
                    String userType = (String) user.get("userType");
                    if(userType.equals("patient")) {
                        startActivity(new Intent(LoginActivity.this, UserActivty.class));
                    }
                    else {
                        startActivity(new Intent(LoginActivity.this, DoctorActivity.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        } else {
            Toast.makeText(this, "Verify your email address", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
        }
    }


    private void userLogin(){
        String emailAddress = etEmailAddress.getText().toString().trim();
        String password = etPassword.getText().toString().trim();


        //Validation method that ensures that the user has entered email and password to register
        //When the user did not enter anything to the email field
        //Stop the except and provide an exception error
        if(TextUtils.isEmpty(emailAddress)) {
            Toast.makeText(this,"Please enter your email", Toast.LENGTH_SHORT).show();
            etEmailAddress.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(password)) {
            Toast.makeText(this,"Please enter your password", Toast.LENGTH_SHORT).show();
            etEmailAddress.requestFocus();
            return;
        }

        //If the validation is successful, show login progress
        progressDialog.setMessage("Logging in is in Process, Please Wait");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(emailAddress,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if(task.isCanceled()) {
                            Toast.makeText(getBaseContext(),"Something went wrong in the system", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(!task.isSuccessful()) {
                            Toast.makeText(getBaseContext(),"Incorrect Username or Password", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(task.isSuccessful()){
                            checkEmailVerification();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if (view == btnSignIn) {
            userLogin();
        }

        if (view == tvResetPassword) {
            finish();
            startActivity(new Intent(this,ResetPasswordActivity.class));
        }

        if(view == tvSignUp) {
            finish();
            startActivity(new Intent(this,RegisterActivity.class));
        }
    }
}
