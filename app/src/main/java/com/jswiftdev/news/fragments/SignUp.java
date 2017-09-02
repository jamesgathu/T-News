package com.jswiftdev.news.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.jswiftdev.news.R;
import com.jswiftdev.news.SignInActivity;
import com.jswiftdev.news.utils.C;
import com.jswiftdev.news.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by james on 9/2/17.
 */

public class SignUp extends Fragment {
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password_original)
    EditText etPasswordOriginal;
    @BindView(R.id.et_password_repeat)
    EditText etPasswordRepeat;

    public static SignUp newInstance() {
        Bundle args = new Bundle();
        SignUp fragment = new SignUp();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.page_sign_up, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.btn_sign_up)
    public void signUp() {
        if (!etEmail.getText().toString().isEmpty()
                && etPasswordOriginal.getText().toString().equals(etPasswordRepeat.getText().toString())
                && Utils.isEmailAddressValid(etEmail.getText().toString())) {
            ((SignInActivity) getActivity()).progressDialog.setMessage("Sign you up, please wait...");
            ((SignInActivity) getActivity()).progressDialog.show();

            ((SignInActivity) getActivity()).mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(),
                    etPasswordOriginal.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d(C.LOG_TAG, "Completed " + task.toString());
                    ((SignInActivity) getActivity()).progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    ((SignInActivity) getActivity()).proceed();
                    getActivity().finish();
                }
            });
        } else if (!Utils.isEmailAddressValid(etEmail.getText().toString())) {
            Toast.makeText(getActivity(), "Please enter a valid email address", Toast.LENGTH_LONG).show();
        } else if (!etPasswordOriginal.getText().toString().equals(etPasswordRepeat.getText().toString())) {
            Toast.makeText(getActivity(), "Password mismatch", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getActivity(), "Invalid sign up details", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.goto_login)
    public void gotoLogin() {
        ((SignInActivity) getActivity()).move(Login.newInstance());
    }


}
