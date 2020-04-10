package com.example.ac_twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edtloginemail,edtloginpassword;
    private Button btnloginactivity,btnsignupactivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        setTitle("Log In");

        edtloginemail=findViewById(R.id.edtloginemail);
        edtloginpassword=findViewById(R.id.edtloginpassword);
        btnloginactivity=findViewById(R.id.btnloginactivity);
        btnsignupactivity=findViewById(R.id.btnsignupactivity);

        btnloginactivity.setOnClickListener(this);
        btnsignupactivity.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btnloginactivity:

                ParseUser.logInInBackground(edtloginemail.getText().toString(), edtloginpassword.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {

                        if(user!=null && e==null){

                            FancyToast.makeText(LoginActivity.this,user.get("username")+" is logged in successfully", FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                            transitiontoSocialMediaActivity();

                        }else{
                            FancyToast.makeText(LoginActivity.this,e.getMessage(), Toast.LENGTH_SHORT,FancyToast.ERROR,true);

                        }

                    }
                });

                break;


            case R.id.btnsignupactivity:

                Intent intent=new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);

                break;
        }
    }
    public void transitiontoSocialMediaActivity(){
        Intent intent=new Intent(LoginActivity.this, TwitterUsers.class);
        startActivity(intent);
        finish();
}}
