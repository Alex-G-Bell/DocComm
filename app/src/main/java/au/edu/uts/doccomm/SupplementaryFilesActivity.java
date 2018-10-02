package au.edu.uts.doccomm;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class SupplementaryFilesActivity extends AppCompatActivity {
    private ImageView iv_image;
    private Uri imageUri;

    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplementary_files);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        mStorageRef = FirebaseStorage.getInstance().getReference();


        iv_image = (ImageView) findViewById(R.id.iv_image);

        File file = new File("/data/data/au.edu.uts.doccomm","supplementaryFiles");
        if(!(file.exists())){
        file.mkdir();}

        findViewById(R.id.createFile).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){


                // create a text file in supplementary file  path: /data/data/au.edu.uts.doccomm/supplementaryFiles
                AlertDialog.Builder inputDialog =
                        new AlertDialog.Builder(SupplementaryFilesActivity.this);
                final View dialogView = LayoutInflater.from(SupplementaryFilesActivity.this).inflate(R.layout.dialog_customize,null);
                inputDialog.setTitle("Create text file");
                inputDialog.setView(dialogView);
                inputDialog.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText1 = (EditText) dialogView.findViewById(R.id.textFileTiltle);
                        EditText editText2 = (EditText) dialogView.findViewById(R.id.textFileContent);
                        String fileName = editText1.getText().toString();
                        String fileContent = editText2.getText().toString();
                        if(!(fileName.equals(""))) {
                            File file = new File("/data/data/au.edu.uts.doccomm/supplementaryFiles", fileName+".txt");
                            try {
                                file.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        try{
                        FileWriter fileWriter = new FileWriter("/data/data/au.edu.uts.doccomm/supplementaryFiles/"+fileName+".txt");
                        fileWriter.flush();
                        fileWriter.write(fileContent);
                        fileWriter.close();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                inputDialog.show();
            }
        });

        final Button chooseImage = (Button) findViewById(R.id.viewFile);
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(v);
            }
        });

        final Button uploadImage = (Button) findViewById(R.id.upLoadImage);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage(v);
            }
        });

    }

    private void uploadImage(View v){
        if(!(imageUri==null)){
            StorageReference imageRef = mStorageRef.child(userID+"image/supplementaryPhoto.jpg");
            imageRef.putFile(imageUri);
            Toast toast = Toast.makeText(SupplementaryFilesActivity.this,"uploaded", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void chooseImage(View v) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_CANCELED) {
                    return;
                }
                try {
                    imageUri = data.getData();
                    Uri imageUri = data.getData();
                    Log.e("TAG", imageUri.toString());
                    iv_image.setImageURI(imageUri);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}
