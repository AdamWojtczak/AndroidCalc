package com.example.calc;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.util.LinkedList;
import java.util.List;


public class ActivitySimple extends AppCompatActivity {

    Button btn0,btn1,btn3,btn2,btn4,btn5,btn6,
            btn7,btn8,btn9,btnChngmark,btnPlus,
            btnMinus,btnDivision,btnMultiply,
            btnEqual,btnClear,btnDot,btnBksp;
    TextView tvInput, history;
    String process, history_text = "";
    List<Button> numbers;

    public static boolean isNumeric(String str) {
        return str != null && str.matches("[-+]?\\d*\\.?\\d+");
    }

    public boolean validate_click(Button btn) {

        process = tvInput.getText().toString();

        if (btn.equals(btnDot)) {
            if(process.contains(".")) {
                Toast.makeText(getApplicationContext(),"Number already contains dot", Toast.LENGTH_SHORT).show();
                return false;
            }
            else {
                return true;
            }
        }

        if( process != null) {
            if (!numbers.contains(btn) && !isNumeric(process.substring(process.length()-1)) ) {
                Toast.makeText(getApplicationContext(), "You just made a typo", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    public String evaluate() {
        process = tvInput.getText().toString();
        String tempHistory = process;

        process = process.replaceAll("×","*");
        process = process.replaceAll("÷","/");

        Context rhino = Context.enter();

        rhino.setOptimizationLevel(-1);

        String finalResult = "";

        try {
            Scriptable scriptable = rhino.initStandardObjects();
            finalResult = rhino.evaluateString(scriptable,process,"javascript",1,null).toString();
        }catch (Exception e){
            finalResult="0";
        }

        history_text = tempHistory + '\n' + '=' + finalResult + "\n\n" + history_text;
        if(history != null) {
            history.setText(history_text);
        }

        return finalResult;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_simple);

        btn0 = findViewById(R.id.btn0);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);
        btn9 = findViewById(R.id.btn9);

        btnPlus = findViewById(R.id.btnPlus);
        btnMinus = findViewById(R.id.btnMinus);
        btnDivision = findViewById(R.id.btnDivision);
        btnMultiply = findViewById(R.id.btnMultiply);

        btnEqual = findViewById(R.id.btnEqual);

        btnClear = findViewById(R.id.btnClear);
        btnDot = findViewById(R.id.btnDot);
        btnChngmark = findViewById(R.id.btnChngmark);
        btnBksp = findViewById(R.id.btnBksp);

        tvInput = findViewById(R.id.tvInput);
        history = findViewById(R.id.history);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvInput.setText("");
            }
        });


        class Listener implements View.OnClickListener {

            @Override
            public void onClick(View v) {
                process = tvInput.getText().toString();
                tvInput.setText(process + v.toString().charAt(v.toString().length()-2));
            }
        }

        Listener number_action = new Listener();

        numbers = new LinkedList<Button>();
        numbers.add(btn0);
        numbers.add(btn1);
        numbers.add(btn2);
        numbers.add(btn3);
        numbers.add(btn4);
        numbers.add(btn5);
        numbers.add(btn6);
        numbers.add(btn7);
        numbers.add(btn8);
        numbers.add(btn9);

        for(Button n:numbers)
        {
            n.setOnClickListener(number_action);
        }

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process = tvInput.getText().toString();
//                if (validate_click(btnPlus)) {
                    tvInput.setText(process + "+");
//                }
            }
        });


        btnMinus.setOnClickListener(v -> {
            process = tvInput.getText().toString();
            if (validate_click(btnMinus)) {
                tvInput.setText(process + "-");
            }
        });

        btnMultiply.setOnClickListener(v -> {
            process = tvInput.getText().toString();
            if (validate_click(btnMultiply)) {
                tvInput.setText(process + "×");
            }
        });

        btnDivision.setOnClickListener(v -> {
            process = tvInput.getText().toString();
            if (validate_click(btnDivision)) {
                tvInput.setText(process + "÷");
            }
        });

        btnDot.setOnClickListener(v -> {
            process = tvInput.getText().toString();
            if (validate_click(btnDot)) {
                tvInput.setText(process + ".");
            }
        });

        btnChngmark.setOnClickListener(v -> {
            process = tvInput.getText().toString();
            if (validate_click(btnChngmark)) {
                tvInput.setText(new Double(new Double(evaluate()) / 100).toString());
            }
        });

        btnBksp.setOnClickListener(v -> {
            process = tvInput.getText().toString();
            if (process.length() > 0) {
                process = process.substring(0,process.length()-1);
                tvInput.setText(process);
            }
        });

        btnEqual.setOnClickListener(v -> tvInput.setText(evaluate()));

        if (savedInstanceState != null) {
            process = savedInstanceState.getString("process");
            tvInput.setText(process);
            history_text = savedInstanceState.getString("history_text");
            if (history != null) {
                history.setText(history_text);
            }
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        process = tvInput.getText().toString();
        if(history != null) {
            history_text = history.getText().toString();
        }
        outState.putString("history_text", history_text);
        outState.putString("process", process);
    }
}
