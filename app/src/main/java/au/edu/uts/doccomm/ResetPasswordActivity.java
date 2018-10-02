package au.edu.uts.doccomm;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText etResetEmail;
    private Button btnResetPassword;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        etResetEmail = (EditText) findViewById(R.id.etResetEmail);
        btnResetPassword = (Button) findViewById(R.id.btnResetPassword);
        mAuth = FirebaseAuth.getInstance();

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = etResetEmail.getText().toString().trim();
                if(TextUtils.isEmpty(userEmail)){
                    Toast.makeText(ResetPasswordActivity.this,"Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(ResetPasswordActivity.this,"Password has been reset, please check your email", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ResetPasswordActivity.this,LoginActivity.class));
                            } else {
                                Toast.makeText(ResetPasswordActivity.this,"Problem in sending a reset password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
