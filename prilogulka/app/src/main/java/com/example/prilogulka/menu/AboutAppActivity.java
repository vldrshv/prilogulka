package com.example.prilogulka.menu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.prilogulka.R;

public class AboutAppActivity extends AppCompatActivity implements Button.OnClickListener{

    Button buttonTermsOfUse, buttonPrivacyPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        buttonPrivacyPolicy = findViewById(R.id.buttonPrivacyPolicy);
        buttonTermsOfUse = findViewById(R.id.buttonTermsOfUse);
        buttonPrivacyPolicy.setOnClickListener(this);
        buttonTermsOfUse.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        String uri = "";
        switch(v.getId()) {
            case R.id.buttonTermsOfUse:
                uri = "https://docs.google.com/document/d/1aBNQP2mH7kXOg2aVJdbWgeu_ezxP6THWeYG2Ev75Pow/edit?usp=sharing";
                break;
            case R.id.buttonPrivacyPolicy:
                uri = "https://docs.google.com/document/d/15Zdd2byTbWrjsb94NYJtgl8ylEzffDDzTjVqVAzOTpc/edit?usp=sharing";
                break;
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(browserIntent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed(){
        getSupportFragmentManager().popBackStack();
        super.onBackPressed();
    }
}
