package com.ayndev.loginandsignupone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    public static final int REQUEST_SIGNUP = 0;
    private static final String TAG = LoginActivity.class.getSimpleName();
    String email, pass;
    TextView _signUpLink;
    EditText _etEmail, _etPassword;
    Button _btnLogin;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        _etEmail = (EditText) findViewById(R.id.et_login_email);
        _etPassword = (EditText) findViewById(R.id.et_login_password);
        _btnLogin = (Button) findViewById(R.id.btn_login);
        _signUpLink = (TextView) findViewById(R.id.tv_sign_up);

        _btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);

                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    private void login() {
        Log.d(TAG, "Login");

        if (!validateAccount()) {
            onLoginFailed();
            return;
        }

        _btnLogin.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        handler = new Handler();
        handler.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        onLoginSuccess();
                        progressDialog.dismiss();
                        logging();
                    }
                }, 3000
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                String usName = data.getExtras().getString(SignUpActivity.UNAME_RETURN);
                String passWord = data.getExtras().getString(SignUpActivity.PASS_RETURN);

                _etEmail.setText(usName);
                _etPassword.setText(passWord);
            }
        }
    }

    private void logging() {
        updateEmailPass();

        OtakuDatabaseHelper helper = new OtakuDatabaseHelper(this);
        String temp = helper.searchPasswordFromEmail(email);
        if (pass.equals(temp)) {
            Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
        }
    }

    private void onLoginSuccess() {
        _btnLogin.setEnabled(true);
        // go to Profile Activity
    }

    private void onLoginFailed() {
        Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show();

        _btnLogin.setEnabled(true);
    }

    private void updateEmailPass() {
        email = _etEmail.getText().toString();
        pass = _etPassword.getText().toString();
    }

    private boolean validateAccount() {
        boolean isValid = true;

        updateEmailPass();

        // checking email and password from database
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _etEmail.setError("enter a valid email address");
            isValid = false;
        } else {
            _etEmail.setError(null);
        }

        if (pass.isEmpty() || pass.length() < 4 || pass.length() > 10) {
            _etPassword.setError("between 4 and 10 alphanumeric characters");
            isValid = false;
        } else {
            _etPassword.setError(null);
        }

        Log.v(TAG, isValid + "");

        return isValid;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}
