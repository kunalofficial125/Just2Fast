package com.just2fast.ushop;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;


public class extra_info extends AppCompatActivity {
    EditText firstName,lastName;
    String name;
    Button next_btn;
    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_info);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firstName = findViewById(R.id.firstName);
        next_btn = findViewById(R.id.next_btn);
        lastName = findViewById(R.id.lastName);

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(firstName.getText().toString().length()<5 && lastName.getText().toString().length()<5){
                    Toast.makeText(extra_info.this, "Please Enter Name More Than 5 Letter", Toast.LENGTH_SHORT).show();
                }
                else {
                    name = firstName.getText().toString() + " " + lastName.getText().toString();
                    Intent intent = getIntent();
                    String userPh = intent.getStringExtra("UserPh");
                    firebaseFirestore.collection(userPh).document("UserInfo").update("name",name,"name_state",1);
                    Log.d("UpdateComplete",userPh+" ");
                    Intent intent1 = new Intent(extra_info.this,MainActivity.class);
                    startActivity(intent1);
                    finish();
                }
            }
        });
    }
}