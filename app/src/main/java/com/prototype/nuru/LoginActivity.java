package com.prototype.nuru;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.prototype.nuru.helpers.InputValidation;
import com.prototype.nuru.models.User;
import com.prototype.nuru.sql.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {
    private final AppCompatActivity activity = LoginActivity.this;

    private EditText editTextUsername;
    private EditText editTextPassword;

    private Button alogin;

    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        initViews();
        initObjects();
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {

        editTextUsername = (EditText) findViewById(R.id.username);
        editTextPassword = (EditText) findViewById(R.id.password3);

        alogin = (Button) findViewById(R.id.alogin);

    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        databaseHelper = new DatabaseHelper(activity);
        inputValidation = new InputValidation(activity);

    }

    /**
     * This method is to validate the input text fields and verify login credentials from SQLite
     */
    public void verifyFromSQLite(View view) {
        if (!inputValidation.isInputEditTextFilled(editTextUsername, getString(R.string.error_message_username))) {
            return;
        }

        if (!inputValidation.isInputEditTextFilled(editTextPassword, getString(R.string.error_message_password))) {
            return;
        }

        if (databaseHelper.checkUser(editTextUsername.getText().toString().trim()
                , editTextPassword.getText().toString().trim())) {

            User user = databaseHelper.fetchUser(editTextUsername.getText().toString().trim()
                    , editTextPassword.getText().toString().trim());

            Intent home = new Intent(activity, MainActivity.class);
            home.putExtra("user", user.getId());
            emptyInputEditText();
            startActivity(home);


        } else {
            // Snack Bar to show success message that record is wrong
            Toast.makeText(this, getString(R.string.error_valid_username_password), Toast.LENGTH_LONG).show();
        }
    }

    public void register(View view) {
        // Navigate to RegisterActivity
        Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intentRegister);
        finish();
    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        editTextUsername.setText(null);
        editTextPassword.setText(null);
    }
}
