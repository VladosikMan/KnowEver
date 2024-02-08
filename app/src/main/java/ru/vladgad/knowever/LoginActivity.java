package ru.vladgad.knowever;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText email,password;
    private Button registr,signIn,regButt,regExit;
    private FirebaseAuth mAuth;
    private TextView regEmail;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity);
        init();
    }
    private void init(){
        regEmail = findViewById(R.id.regEmail);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        registr = findViewById(R.id.registr);
        registr.setOnClickListener(this);
        signIn = findViewById(R.id.signIn);
        signIn.setOnClickListener(this);
        regButt = findViewById(R.id.regButt);
        regButt.setOnClickListener(this);
        regExit = findViewById(R.id.regExit);
        regExit.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
    }
    private void showSigned(String s){
        FirebaseUser user = mAuth.getCurrentUser();
        if(user.isEmailVerified()) {
            regEmail.setVisibility(View.VISIBLE);
            regButt.setVisibility(View.VISIBLE);
            regExit.setVisibility(View.VISIBLE);
            email.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            registr.setVisibility(View.GONE);
            signIn.setVisibility(View.GONE);
            regEmail.setText("Вы вошли как " + s);
        }else{
            Toast.makeText(getApplicationContext(),"Проверьте email",Toast.LENGTH_SHORT).show();
        }
    }
    private void notSigned(){
        regEmail.setVisibility(View.GONE);
        regButt.setVisibility(View.GONE);
        regExit.setVisibility(View.GONE);
        email.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
        registr.setVisibility(View.VISIBLE);
        signIn.setVisibility(View.VISIBLE);
    }
    @Override
    protected void onStart() {
        super.onStart();
      FirebaseUser cUser = mAuth.getCurrentUser();//если пользователь не автозиров = null
        if(cUser!=null){
            //пользователь  зарегестриван
            showSigned(cUser.getEmail());
            Toast.makeText(this,"User not null "+ cUser.getEmail(),Toast.LENGTH_SHORT).show();
            Log.d("mLog","User not null");
        }else{
            //нет пользваотеля
            notSigned();
            Log.d("mLog","User null");
            Toast.makeText(this,"User  null",Toast.LENGTH_SHORT).show();
        }
    }

    public void sendEmailVer(){
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Проверьте email",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Нет отправке",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //SugnUp - регистрация
            case R.id.registr:{
                //------------------------------------------------------------------
                if(!TextUtils.isEmpty(email.getText().toString())||!TextUtils.isEmpty(password.getText().toString())){
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //Пользователь зарегистрировался
                                sendEmailVer();
                                Log.d("mLog","Пользователь зарегестрировался");
                                Toast.makeText(getApplicationContext(),"User SignUp Successful",Toast.LENGTH_SHORT).show();
                                Log.d("mLog","User SighUp");
                            }else{
                                Log.d("mLog","Fale");
                                Toast.makeText(getApplicationContext(),"User SignUp Failed",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(getApplicationContext(),"Please, enter Email and Password",Toast.LENGTH_SHORT).show();
                    Log.d("mLog","isEmpty");
                }
                //------------------------------------------------------
                break;
            }
            //SignIn - вход
            case R.id.signIn:{
                if(!TextUtils.isEmpty(email.getText().toString())||!TextUtils.isEmpty(password.getText().toString())) {
                    mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //Пользователь зарегистрировался
                                Log.d("mLog","Пользователь зарегестрировался");
                                Toast.makeText(getApplicationContext(),"User SignIn Successful",Toast.LENGTH_SHORT).show();
                                Log.d("mLog","User SighUp");
                                showSigned(email.getText().toString());
                            }else{
                                Log.d("mLog","Fale");
                                Toast.makeText(getApplicationContext(),"User SignIn Failed",Toast.LENGTH_SHORT).show();
                                notSigned();
                            }
                        }
                    });
                }else {
                    Toast.makeText(getApplicationContext(),"Please, enter Email and Password",Toast.LENGTH_SHORT).show();
                }
                break;
            }
            //переход в основную среду
            case R.id.regButt:{
                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(i);
                break;
            }
            case R.id.regExit:{
                FirebaseAuth.getInstance().signOut();
                notSigned();
                break;
            }
        }
    }
}
