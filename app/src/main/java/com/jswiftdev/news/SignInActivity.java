package com.jswiftdev.news;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.jswiftdev.news.fragments.Login;

/**
 * swaps between {@link Login} and {@link com.jswiftdev.news.fragments.SignUp}
 * to allow for registration and signing of users
 */
public class SignInActivity extends AppCompatActivity {
    /**
     * allows authentication and registration of users
     */
    public FirebaseAuth mAuth;

    /**
     * to be shown during calls to the server
     */
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(SignInActivity.this);
        move(Login.newInstance());
    }


    /**
     * for moving from one fragment to another
     *
     * @param fragment intended destination of the caller
     */
    public void move(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.container, fragment).commitAllowingStateLoss();
    }

    /**
     * called once user authentication is done
     */
    public void proceed() {
        startActivity(new Intent(SignInActivity.this, MainActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }
}
