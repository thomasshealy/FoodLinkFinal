package edu.unc.thomasshealy.foodlinkfinal;

import android.support.annotation.NonNull;
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

public class RegisterUserActivity extends AppCompatActivity {

    public EditText email;
    public EditText password;
    public EditText confirmPassword;
    public FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        email = (EditText) this.findViewById(R.id.email);
        password = (EditText) this.findViewById(R.id.passswordReg);
        confirmPassword = (EditText) this.findViewById(R.id.confirmPassword);

        Button registerButton = (Button) this.findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(password.getText().toString().equals(confirmPassword.getText().toString())){
                    createUser(email.getText().toString(), password.getText().toString());
                }
                else{
                    Toast.makeText(RegisterUserActivity.this, "Passwords must match.",
                            Toast.LENGTH_SHORT).show();
                }

            }

        });



    }

    public void createUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            System.out.println("Created new user success");
                            //updateUI(user);
                        } else {
                            // If sign, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            System.out.println("Error: " + task.getException());
                            System.out.println("failed to create new user");
                            Toast.makeText(RegisterUserActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }
}
