package com.jswiftdev.news.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.jswiftdev.news.R;
import com.jswiftdev.news.SignInActivity;
import com.jswiftdev.news.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Login extends Fragment {
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;


    public static Login newInstance() {
        Bundle args = new Bundle();
        Login fragment = new Login();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.page_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = ((SignInActivity) getActivity()).mAuth.getCurrentUser();
        if (currentUser != null) {
            ((SignInActivity) getActivity()).proceed();
            getActivity().finish();
        }
    }

    @OnClick(R.id.tv_Create_account)
    public void gotoSignUp() {
        ((SignInActivity) getActivity()).move(SignUp.newInstance());
    }

    @OnClick(R.id.btn_login)
    public void login() {
        if (!etEmail.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty() &&
                Utils.isEmailAddressValid(etEmail.getText().toString())) {
            ((SignInActivity) getActivity()).progressDialog.setMessage("Logging in, please wait...");
            ((SignInActivity) getActivity()).progressDialog.show();

            ((SignInActivity) getActivity()).mAuth.signInWithEmailAndPassword(etEmail.getText().toString(),
                    etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    ((SignInActivity) getActivity()).progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    ((SignInActivity) getActivity()).proceed();
                }
            });
        }
    }

}
