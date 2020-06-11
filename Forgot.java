package com.example.mashwara.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mashwara.R;
import com.facebook.login.Login;

public class Forgot extends AppCompatActivity {

    private EditText mail;
    private String getEmail="saad@gmail.com";
    private Button reset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        mail=(EditText)findViewById(R.id.email);
        reset=(Button)findViewById(R.id.resetButton);


        getSupportActionBar();
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }



        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mail.getText().toString().equals(""))
                {
                    mail.setError("Empty Field!");

                }
                else if(mail.getText().toString().equals(getEmail))
                {
                    Toast.makeText(getApplicationContext(),R.string.success,Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(Forgot.this, Login.class);
                    startActivity(i);
                }else
                {
                    Toast.makeText(getApplicationContext(),R.string.errormsg,Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
