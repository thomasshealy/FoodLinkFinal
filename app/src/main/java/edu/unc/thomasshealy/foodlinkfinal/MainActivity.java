package edu.unc.thomasshealy.foodlinkfinal;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    //public boolean wasAuthenticated = false;
    public EditText username;
    public EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) this.findViewById(R.id.username);
        password = (EditText) this.findViewById(R.id.password);

        FirebaseAuth.getInstance().signOut();


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                   // Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Intent intent = new Intent(MainActivity.this, VolunteerMap.class);
                    startActivity(intent);
                } else {
                    // User is signed out
                  //  Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }


        };
        Button loginButton = (Button) this.findViewById(R.id.login);

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                authenticateUser(username.getText().toString(), password.getText().toString());


            }

        });

        Button registerButton = (Button) this.findViewById(R.id.register);

        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, RegisterUserActivity.class);
                startActivity(intent);


            }

        });

    }

    public void authenticateUser(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                           // Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            System.out.println("Authentication successful");
                           // wasAuthenticated = true;
                           // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Username and/or Password is incorrect.",
                                    Toast.LENGTH_SHORT).show();
                           // wasAuthenticated = false;
                           // updateUI(null);
                        }

                        // ...
                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}