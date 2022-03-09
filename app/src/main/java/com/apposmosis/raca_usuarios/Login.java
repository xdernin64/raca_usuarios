package com.apposmosis.raca_usuarios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private EditText email,contrasena;
    private Button btnlogin;
    private String emailtext="",passwordtext="";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Intent i = new Intent(Login.this, Asistencias.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else {
            // User is signed out
            Toast.makeText(Login.this, "Ningun usuario logeado", Toast.LENGTH_SHORT).show();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.txt_email);
        contrasena=findViewById(R.id.txt_pwd);
        btnlogin=findViewById(R.id.btn_register);

        mAuth=FirebaseAuth.getInstance();

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailtext=email.getText().toString();
                passwordtext=contrasena.getText().toString();
                if (emailtext.isEmpty()|| passwordtext.isEmpty())
                {
                    Toast.makeText(Login.this, "Hay un campo vacio escriba porfavor su usuario y contraseña ", Toast.LENGTH_SHORT).show();
                }
                else   {
                    loginuser();
                }

            }
        });


    }

    private void loginuser() {
        mAuth.signInWithEmailAndPassword(emailtext,passwordtext).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    startActivity(new Intent(Login.this,Asistencias.class));
                    finish();
                }
                else{
                    Toast.makeText(Login.this, "No se pudo iniciar sesion compruebe sus datos", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}