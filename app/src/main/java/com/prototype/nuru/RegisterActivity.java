package com.prototype.nuru;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.prototype.nuru.helpers.InputValidation;
import com.prototype.nuru.models.User;
import com.prototype.nuru.sql.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = RegisterActivity.this;

    private EditText editTextFName;
    private EditText editTextLName;
    private EditText editTextPhone;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;

    private Button register;
    private TextView login1;

    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        editTextFName = (EditText) findViewById(R.id.fname);
        editTextLName = (EditText) findViewById(R.id.lname);
        editTextPhone = (EditText) findViewById(R.id.phone);
        editTextEmail = (EditText) findViewById(R.id.email1);
        editTextPassword = (EditText) findViewById(R.id.password1);
        editTextConfirmPassword = (EditText) findViewById(R.id.password2);

        register = (Button) findViewById(R.id.register);
        login1 = (TextView) findViewById(R.id.login1);

    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        register.setOnClickListener(this);
        login1.setOnClickListener(this);

    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        inputValidation = new InputValidation(activity);
        databaseHelper = new DatabaseHelper(activity);
        user = new User();

    }


    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.register:
                postDataToSQLite();
                break;

            case R.id.login1:
                finish();
                break;
        }
    }

    /**
     * This method is to validate the input text fields and post data to SQLite
     */
    private void postDataToSQLite() {
        if (!inputValidation.isInputEditTextFilled(editTextFName, getString(R.string.error_message_name))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(editTextLName, getString(R.string.error_message_name))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(editTextPhone, getString(R.string.error_message_name))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(editTextEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(editTextEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(editTextPassword, getString(R.string.error_message_password))) {
            return;
        }
        if (!inputValidation.isInputEditTextMatches(editTextPassword,editTextConfirmPassword,
                getString(R.string.error_password_match))) {
            return;
        }

        if (!databaseHelper.checkUser(editTextEmail.getText().toString().trim())) {

            user.setFname(editTextFName.getText().toString().trim());
            user.setLname(editTextLName.getText().toString().trim());
            user.setPhone(editTextPhone.getText().toString().trim());
            user.setEmail(editTextEmail.getText().toString().trim());
            user.setPassword(editTextPassword.getText().toString().trim());

            databaseHelper.addUser(user);

            // Toast to show success message that record saved successfully
            Toast.makeText(this, getString(R.string.success_message), Toast.LENGTH_LONG).show();
            emptyInputEditText();
            Intent login = new Intent(activity, LoginActivity.class);
            startActivity(login);


        } else {
            // Snack Bar to show error message that record already exists
            Toast.makeText(this, getString(R.string.error_email_exists), Toast.LENGTH_LONG).show();
        }


    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        editTextFName.setText(null);
        editTextLName.setText(null);
        editTextPhone.setText(null);
        editTextEmail.setText(null);
        editTextPassword.setText(null);
        editTextConfirmPassword.setText(null);
    }
}
