package com.example.marija;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.marija.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class ChangeProfileImageActivity extends AppCompatActivity {

    private StorageReference mStorageRef,storageReference1;;
    private FirebaseStorage mFirebaseStorage;
    private ImageView profilna,malaProfilna;
    public Uri imguri;
    private StorageTask uploadTask;
    StorageReference riversRef;
    private Button btnChoose,save;
    DatabaseHandler databaseHandler;
    User u;
    StorageReference photoRef;
    String image_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile_image);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        setupActionBar();
        mStorageRef = FirebaseStorage.getInstance().getReference("Korisnici");
        mFirebaseStorage = FirebaseStorage.getInstance().getReference("Korisnici").getStorage();

        profilna = findViewById(R.id.profilnaSlika1);
        //malaProfilna = findViewById(R.id.slikaUsera1);
        btnChoose = findViewById(R.id.izaberiSliku1);
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filechooser();
            }
        });
        save = findViewById(R.id.sacuvajSliku);

        databaseHandler = new DatabaseHandler(ChangeProfileImageActivity.this);
        u = databaseHandler.findUser();

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(ChangeProfileImageActivity.this,"Slika se uploaduje!",Toast.LENGTH_SHORT).show();
                } else {
                    photoRef =  FirebaseStorage.getInstance().getReferenceFromUrl("gs://zakazimefirebasebaza.appspot.com/Korisnici/"+ u.getEmail()+ ".jpg");
                    if (photoRef == null) {
                        photoRef = mFirebaseStorage.getReferenceFromUrl("gs://zakazimefirebasebaza.appspot.com/Korisnici/" + u.getEmail()+ ".png");
                    }
                    photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // File deleted successfully
                           Fileuploader(u.getEmail());

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    final Intent mainIntent = new Intent(ChangeProfileImageActivity.this, MainActivity.class);
                                    startActivity(mainIntent);
                                    finish();
                                }
                            }, 4000);


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Uh-oh, an error occurred!
                       }
                    });



                }

            }
        });


    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        if (imguri != null)
            savedInstanceState.putString("path_to_picture", imguri.toString());

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){

        image_path = savedInstanceState.getString("path_to_picture");
        if (image_path != null) {
            Uri myUri = Uri.parse(image_path);
            imguri = myUri;
            profilna.setImageURI(myUri);
        }

    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public String getURLForResource (int resourceId) {
        return Uri.parse("android.resource://"+R.class.getPackage().getName()+"/" +resourceId).toString();
    }


    private void Fileuploader(String email) {
        if (Uri.EMPTY == imguri || imguri == null) {

            imguri = Uri.parse(getURLForResource(R.drawable.defaultuser));
            profilna.setImageURI(imguri);
            riversRef = mStorageRef.child(email+".jpg");
        } else {
            riversRef = mStorageRef.child(email+"."+getExtention(imguri));
            profilna.setImageURI(imguri);
        }

        uploadTask = riversRef.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(ChangeProfileImageActivity.this,"Slika je uploadovana uspesno!",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });

    }

    private String getExtention(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));


    }

    private void Filechooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null) {

            imguri=data.getData();
            profilna.setImageURI(imguri);
        }
    }

}
