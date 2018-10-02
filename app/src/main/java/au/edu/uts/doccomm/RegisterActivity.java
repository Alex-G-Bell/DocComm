package au.edu.uts.doccomm;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RegisterActivity";

    private Button btnRegister;
    private EditText etEmail;
    private EditText etPassword;
    private TextView tvSignIn;
    private EditText etPhoneNumber;
    private EditText etFirstName;
    private EditText etLastName;
    private Spinner spGender;
    private EditText etHeight;
    private EditText etWeight;
    private TextView tvDateOfBirth;
    private EditText etMedicalCondition;
    private DatePickerDialog.OnDateSetListener mDateSetListener;


    private ProgressDialog progressDialog;

    public FirebaseAuth mAuth;
    public DatabaseReference mDatabase;

    public String id;

    public User user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);




        progressDialog = new ProgressDialog(this);

        //Check if the user is signed in, if the user is signed in
        //Then re-direct the user to Main page
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(),UserActivty.class));
        }

        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        btnRegister = (Button) findViewById(R.id.btnRegister);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvSignIn = (TextView) findViewById(R.id.tvSignIn);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        spGender = (Spinner) findViewById(R.id.spGender);
        etHeight = (EditText) findViewById(R.id.etHeight);
        etWeight = (EditText) findViewById(R.id.etWeight);
        tvDateOfBirth = (TextView) findViewById(R.id.tvDateOfBirth);
        etMedicalCondition = (EditText) findViewById(R.id.etMedicalCondition);


        tvDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        RegisterActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
            }


        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override

            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
                String date = month + "/" + day + "/" + year;
                tvDateOfBirth.setText(date);
            }

        };

        btnRegister.setOnClickListener(this);
        tvSignIn.setOnClickListener(this);
    }

    /* TODO: Implement if the user has already been signed in
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    */

    private void sendEmailVerification() {
        FirebaseUser firebaseUser = mAuth.getInstance().getCurrentUser();
        if(firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this,"Successfully Registered, Please check your email address for verfication email", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                        finish();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    } else {
                        Toast.makeText(RegisterActivity.this,"Something error with the email verification, Please Try again later",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    private void registerUser(){
        final String emailAddress = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        final String firstName = etFirstName.getText().toString().trim();
        final String lastName = etLastName.getText().toString().trim();
        final String dateOfBirth = tvDateOfBirth.getText().toString().trim();
        final String gender = spGender.getSelectedItem().toString().trim();
        final String phoneNumber = etPhoneNumber.getText().toString().trim();
        final String weight = etWeight.getText().toString().trim();
        final String height = etHeight.getText().toString().trim();
        final String medicalCondition = etMedicalCondition.getText().toString().trim();



        //Validation method that ensures that the user has entered email and password to register
        //When the user did not enter anything to the email field
        //Stop the except and provide an exception error
        if(TextUtils.isEmpty(firstName)) {
            Toast.makeText(this,"Please enter your first name", Toast.LENGTH_SHORT).show();
            etFirstName.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(lastName)) {
            Toast.makeText(this,"Please enter your last name", Toast.LENGTH_SHORT).show();
            etLastName.requestFocus();
            return;
        }

        if(!emailAddress.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"))
        {
            Toast.makeText(this,"Please enter your email in correct format", Toast.LENGTH_SHORT).show();
            etEmail.requestFocus();
            return;
        }


        if(TextUtils.isEmpty(emailAddress)) {
            Toast.makeText(this,"Please enter your email", Toast.LENGTH_SHORT).show();
            etEmail.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(password)) {
            Toast.makeText(this,"Please enter your password", Toast.LENGTH_SHORT).show();
            etPassword.requestFocus();
            return;
        }

        if(password.length() < 9) {
            Toast.makeText(this,"Please enter more than 8 characters for your password", Toast.LENGTH_SHORT).show();
            etPassword.requestFocus();
            return;
        }


        if(TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(this,"Please enter your phone number", Toast.LENGTH_SHORT).show();
            etPhoneNumber.requestFocus();
            return;
        }


        if(TextUtils.isEmpty(dateOfBirth)) {
            Toast.makeText(this,"Please enter your date of birth", Toast.LENGTH_SHORT).show();
            tvDateOfBirth.requestFocus();
            return;
        }


        if(TextUtils.isEmpty(height)) {
            Toast.makeText(this,"Please enter your height", Toast.LENGTH_SHORT).show();
            etHeight.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(weight)) {
            Toast.makeText(this,"Please enter your weight", Toast.LENGTH_SHORT).show();
            etWeight.requestFocus();
            return;
        }

        //If the validation is successful, show registration progress
        progressDialog.setMessage("Registering in Process, Please Wait");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(emailAddress,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Sign in success, update UI with the signed-in
                            FirebaseUser userAuth = mAuth.getCurrentUser();

                            id = userAuth.getUid();
                            user = new User( id, firstName, lastName, emailAddress, password, gender, phoneNumber,dateOfBirth, weight, height, medicalCondition, "patient");
                            mDatabase.child(id).setValue(user);
                            progressDialog.dismiss();
                            sendEmailVerification();
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this,"Registration Unsuccessful",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


//
//    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//        month = month + 1;
//        month = month + 1;
//
//        Log.d(TAG, "onDateSet: dd/mm/yyyy " + day + "/" + month + "/" + year);
//    }



    @Override

  //  @Override

    public void onClick(View view) {
//        if (view == tvDateOfBirth) {
//
//            Calendar cal= Calendar.getInstance();
//            int year = cal.get(Calendar.YEAR);
//            int month = cal.get(Calendar.MONTH);
//            int day = cal.get(Calendar.DATE);
//
//            DatePickerDialog dialog = new DatePickerDialog(
//                    RegisterActivity.this,
//                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
//                    mDateSetListener,
//                    year,month,year);
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            dialog.show();
//        }
//
//        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//                month = month + 1;
//                Log.d(TAG, "onDateSet: dd/mm/yyyy " + day + "/" + month + "/" + year);
//            }
//        };

        if (view == btnRegister) {
            registerUser();
        }

        if (view == tvSignIn) {
            startActivity(new Intent(this, LoginActivity.class));
        }

//
//        if(view.getId() == R.id.mapsButton){
//            Intent intent2 = new Intent(RegisterActivity.this, FacilitiesMapsActivity.class);
//            startActivity(intent2);
//        }

    }




}
