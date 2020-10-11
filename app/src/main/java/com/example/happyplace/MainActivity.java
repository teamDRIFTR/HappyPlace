package com.example.happyplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.DialogInterface;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;



import yuku.ambilwarna.AmbilWarnaDialog;

public class MainActivity extends AppCompatActivity {

    private PaintView paintView;
    private int defaultColor;
    private int STORAGE_PERMISSION_CODE = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button button;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        paintView = findViewById(R.id.paintView);
        button = findViewById(R.id.change_color_button);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        SeekBar seekBar = findViewById(R.id.seekBar);
        final TextView textView =  findViewById(R.id.current_pen_size);

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        paintView.initialize(displayMetrics);

        textView.setText("Pen size: "+ seekBar.getProgress());
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                OpenColorMenu();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar , int progress , boolean fromUser) {
                paintView.SetBrushWidth(seekBar.getProgress());
                textView.setText("brush size= "+ seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    private void GetStoragePermission()
    {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
        {
            new AlertDialog.Builder(this)
                    .setTitle("Permission")
                    .setMessage("confirm to access storage gallery")
                    .setPositiveButton("OK" , new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog , int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("DENY" , new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog , int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();


        }else{
            ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
        }
    }

    //permission
    @Override
    public void onRequestPermissionsResult(int requestCode , @NonNull String[] permissions , @NonNull int[] grantResults) {
        if(requestCode == STORAGE_PERMISSION_CODE)
        {
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                Toast.makeText(this,"Acess Granted", Toast.LENGTH_LONG).show();
                }
                    else{
                    Toast.makeText(this,"DENIED", Toast.LENGTH_LONG).show();
                }
        }
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.clear_button:
                paintView.WipeScreen();
                return true;
            case R.id.undo_button:
                paintView.undoStroke();
                return true;
            case R.id.redo_button:
                paintView.RedoStroke();
                return true;
            case R.id.save_button:
               if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
               {
                   GetStoragePermission();
               }
               paintView.saveImage();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //color menu
    private void OpenColorMenu()
    {
        //menu for colors
        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this , defaultColor , new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                Toast.makeText(MainActivity.this, "unavailable", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog , int color) {
                defaultColor = color;
                paintView.SetBrushColor(color);
            }
        });
        ambilWarnaDialog.show();
    }

}
