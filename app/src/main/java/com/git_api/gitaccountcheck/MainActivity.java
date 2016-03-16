package com.git_api.gitaccountcheck;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Deepak Kumar on 16-03-2016.
 *
 */


public class MainActivity extends AppCompatActivity {

    private EditText mUsernameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsernameEditText = (EditText)findViewById(R.id.input_username);
        Button mCheckButton = (Button)findViewById(R.id.checkButton);

        mCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int usernameLength = mUsernameEditText.getText().toString().length();
                if (usernameLength == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter github username...", Toast.LENGTH_LONG).show();
                } else {
                    String username = mUsernameEditText.getText().toString();
                    Intent intent = new Intent(MainActivity.this, UserActivity.class);
                    intent.putExtra("KEY_USERNAME", username);
                    startActivity(intent);
                }
            }
        });
    }
}
