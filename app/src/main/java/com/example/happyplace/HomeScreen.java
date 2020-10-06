package com.example.happyplace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeScreen extends AppCompatActivity {
private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        button = (Button) findViewById(R.id.PaintButton);
button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        openMainActivity();
    }
});
    }
    public void openMainActivity()
    {
        Intent intent = new Intent(this, MainActivity.class );
        startActivity(intent);
    }
}
