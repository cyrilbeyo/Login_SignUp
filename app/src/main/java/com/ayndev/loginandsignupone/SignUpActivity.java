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

import com.ayndev.loginandsignupone.databases.OtakuDatabaseHelper;
import com.ayndev.loginandsignupone.databases.Otakunz;

public class SignUpActivity extends AppCompatActivity {

   private static final String TAG = SignUpActivity.class.getSimpleName();
   public static final String UNAME_RETURN = "com.ayndev.loginandsignupone.UNAME";
   public static final String PASS_RETURN = "com.ayndev.loginandsignupone.PASS";
   TextView _loginLink;
   EditText _etName, _etEmail, _etPassword;
   Button _btnCreateAccount;
   String name, email, pass;
   Handler handler;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_sign_up);

      _btnCreateAccount = (Button) findViewById(R.id.btn_create_account);
      _etName = (EditText) findViewById(R.id.et_sign_up_name);
      _etEmail = (EditText) findViewById(R.id.et_sign_up_email);
      _etPassword = (EditText) findViewById(R.id.et_sign_up_password);
      _loginLink = (TextView) findViewById(R.id.tv_login);
      _loginLink.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            finish();
         }
      });

      _btnCreateAccount.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            createAccount();
         }
      });
   }

   private void createAccount() {
      Log.d(TAG, "Sign up");

      if (!validateAccount()) {
         onSignUpFailed();
         return;
      }

      _btnCreateAccount.setEnabled(false);

      final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this,
            R.style.AppTheme_Dark_Dialog);
      progressDialog.setIndeterminate(true);
      progressDialog.setMessage("Creating Account...");
      progressDialog.show();

      getInputData();
      OtakuDatabaseHelper helper = new OtakuDatabaseHelper(this);
      Otakunz otakunz = new Otakunz(name, email, pass);

      if (helper.checkEmailRegistered(email)) {
         handler = new Handler();
         handler.postDelayed(
               new Runnable() {
                  @Override
                  public void run() {
                     progressDialog.dismiss();
                     Toast.makeText(SignUpActivity.this, "Email has been registered, please use another email",
                           Toast.LENGTH_LONG).show();
                  }
               }, 3000
         );
         _btnCreateAccount.setEnabled(true);
      } else {
         helper.insertOtakuMember(otakunz);

         handler = new Handler();
         handler.postDelayed(
               new Runnable() {
                  @Override
                  public void run() {
                     onSignUpSuccess();
                     progressDialog.dismiss();
                     Toast.makeText(SignUpActivity.this, "Register Success", Toast.LENGTH_LONG).show();
                  }
               }, 3000
         );
      }
   }

   private void onSignUpSuccess() {
      _btnCreateAccount.setEnabled(true);
      Intent data = new Intent();
      data.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
      data.putExtra(UNAME_RETURN, email);
      data.putExtra(PASS_RETURN, pass);
      setResult(RESULT_OK, data);
      finish();
   }

   private void onSignUpFailed() {
      Toast.makeText(this, "Sign up Failed", Toast.LENGTH_LONG).show();

      _btnCreateAccount.setEnabled(true);
   }

   private void getInputData() {
      name = _etName.getText().toString();
      email = _etEmail.getText().toString();
      pass = _etPassword.getText().toString();
   }

   private boolean validateAccount() {
      boolean isValid = true;
      getInputData();

      if (name.isEmpty() || name.length() < 3) {
         _etName.setError("at least 3 characters");
         isValid = false;
      } else {
         _etName.setError(null);
      }

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

      return isValid;
   }

}
