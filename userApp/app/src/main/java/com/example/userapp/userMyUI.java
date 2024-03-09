package com.example.userapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class userMyUI extends AppCompatActivity {

    private Button myButton;
    private Button drugButton;
    private Button exitButton;

    private TextView idText;
    private TextView emailText;
    private TextView nameText;
    private TextView sexText;
    private TextView ageText;
    private TextView phoneText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_my_ui);
        initUserMy();

        drugButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(userMyUI.this, overall_main.class);
                startActivity(intent);
                userMyUI.this.finish();
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllData.trueUserCode = "";
                AllData.userKey = false;
                Intent intent = new Intent(userMyUI.this, LogUi.class);
                startActivity(intent);
                userMyUI.this.finish();
            }
        });
    }

    private void initUserMy() {
        myButton = findViewById(R.id.myButtonMy);
        drugButton = findViewById(R.id.drugButtonMy);
        exitButton = findViewById(R.id.exitButton);
        idText = findViewById(R.id.iDText);
        emailText = findViewById(R.id.emailText);
        nameText = findViewById(R.id.nameText);
        sexText = findViewById(R.id.sexText);
        ageText = findViewById(R.id.ageText);
        phoneText = findViewById(R.id.phoneText);

        idText.setText(AllData.trueUserCode);
        emailText.setText(AllData.userData.get(AllData.trueUserCode).getUserEmail());
        nameText.setText(AllData.userData.get(AllData.trueUserCode).getUserName());
        sexText.setText(AllData.userData.get(AllData.trueUserCode).getUserSex());
        int userAge = AllData.userData.get(AllData.trueUserCode).getUserAge();
        String s = String.valueOf(userAge);
        ageText.setText(s);
        phoneText.setText(AllData.userData.get(AllData.trueUserCode).getUserPhone());

    }
}
