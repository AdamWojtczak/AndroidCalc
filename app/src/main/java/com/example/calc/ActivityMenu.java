package com.example.calc;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityMenu extends AppCompatActivity {

    Button btnSimple, btnScientific, btnAbout, btnExit;

    public void openActivitySimple() {
        Intent intent = new Intent(this, ActivitySimple.class);
        startActivity(intent);
    }

    public void openActivityScientific() {
        Intent intent = new Intent(this, ActivityScientific.class);
        startActivity(intent);
    }

    public void onButtonShowPopupWindowClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnSimple = findViewById(R.id.btnSimple);
        btnScientific = findViewById(R.id.btnScientific);
        btnAbout = findViewById(R.id.btnAbout);
        btnExit = findViewById(R.id.btnExit);

        btnSimple.setOnClickListener(v -> openActivitySimple());
        btnScientific.setOnClickListener(v -> openActivityScientific());
        btnExit.setOnClickListener(v -> {
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        });
        btnAbout.setOnClickListener(v -> onButtonShowPopupWindowClick(v));

    }
}
