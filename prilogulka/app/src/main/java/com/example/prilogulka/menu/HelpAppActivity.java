package com.example.prilogulka.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.TextView;

import com.example.prilogulka.R;

public class HelpAppActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.help_app_activity);
        setToolbar();

        textView = findViewById(R.id.textViewTest_HelpAppActivity);
        Intent intent = getIntent();
        int description = intent.getIntExtra("description", 0);

        if (description != 0) {
            textView.setText(description);
        }
    }

    public void setToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar_HelpAppActivity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }
}
