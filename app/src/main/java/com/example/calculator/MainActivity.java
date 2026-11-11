package com.example.calculator;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final int MAX_DIGITS = 15;

    private static final String OPT_ADD = "+";
    private static final String OPT_SUB = "-";
    private static final String OPT_MUL = "*";
    private static final String OPT_DIV = "/";

    private TextView txtDisplay;
    private String currentInput = "0";

    private double firstOperand = 0;
    private String pendingOperation = null;
    private boolean startNewInput = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtDisplay = findViewById(R.id.txtDisplay);

        int[] digitIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        };
        for (int id : digitIds) {
            findViewById(id).setOnClickListener(v -> {
                Button button = (Button) v;
                appendDigit(button.getText().toString());
            });
        }

        findViewById(R.id.btnDot).setOnClickListener(v -> appendDot());
        findViewById(R.id.btnC).setOnClickListener(v -> clearAll());
        findViewById(R.id.btnCe).setOnClickListener(v -> clearEntry());
        findViewById(R.id.btnBackspace).setOnClickListener(v -> deleteLastChar());
        findViewById(R.id.btnAdd).setOnClickListener(v -> setOperation(OPT_ADD));
        findViewById(R.id.btnSub).setOnClickListener(v -> setOperation(OPT_SUB));
        findViewById(R.id.btnMul).setOnClickListener(v -> setOperation(OPT_MUL));
        findViewById(R.id.btnDiv).setOnClickListener(v -> setOperation(OPT_DIV));
        findViewById(R.id.btnEquals).setOnClickListener(v -> calculate());
        findViewById(R.id.btnSign).setOnClickListener(v -> toggleSign());
        findViewById(R.id.btnSqrt).setOnClickListener(v -> applySqrt());
        findViewById(R.id.btnInverse).setOnClickListener(v -> applyInverse());
        findViewById(R.id.btnPercent).setOnClickListener(v -> applyPercent());

    }

    private void appendDigit(String digit) {
        if (startNewInput) {
            currentInput = "0";
            startNewInput = false;
        }
        if (currentInput.length() >= MAX_DIGITS) return;
        if (currentInput.equals("0")) {
            currentInput = digit;
        } else {
            currentInput += digit;
        }
        updateDisplay();
    }

    private void appendDot() {
        if (startNewInput) {
            currentInput = "0";
            startNewInput = false;
        }
        if (!currentInput.contains(".")) {
            currentInput += ".";
            updateDisplay();
        }
    }

    private void deleteLastChar() {
        if (currentInput.length() <= 1) {
            currentInput = "0";
        } else {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
        }
        updateDisplay();
    }

    private void clearEntry() {
        currentInput = "0";
        updateDisplay();
    }

    private void clearAll() {
        currentInput = "0";
        firstOperand = 0;
        pendingOperation = null;
        startNewInput = false;
        updateDisplay();
    }

    private void setOperation(String operation) {
        if (pendingOperation != null && !startNewInput) {
            calculate();
        }
        firstOperand = Double.parseDouble(currentInput);
        pendingOperation = operation;
        startNewInput = true;
    }

    private void calculate() {
        if (pendingOperation == null) return;
        double secondOperand = Double.parseDouble(currentInput);
        double result = 0;

        switch (pendingOperation) {
            case OPT_ADD:
                result = firstOperand + secondOperand;
                break;
            case OPT_SUB:
                result = firstOperand - secondOperand;
                break;
            case OPT_MUL:
                result = firstOperand * secondOperand;
                break;
            case OPT_DIV:
                if (secondOperand == 0) {
                    currentInput = "0";
                    pendingOperation = null;
                    startNewInput = true;
                    updateDisplay();
                    return;
                }
                result = firstOperand / secondOperand;
                break;
        }

        currentInput = formatResult(result);
        pendingOperation = null;
        startNewInput = true;
        updateDisplay();
    }

    private void toggleSign() {
        if (currentInput.equals("0")) return;
        if (currentInput.startsWith("-")) {
            currentInput = currentInput.substring(1);
        } else {
            currentInput = "-" + currentInput;
        }
        updateDisplay();
    }

    private void applySqrt() {
        double value = Double.parseDouble(currentInput);
        if (value < 0) {
            currentInput = "0";
        } else {
            currentInput = formatResult(Math.sqrt(value));
        }
        startNewInput = true;
        updateDisplay();
    }

    private void applyInverse() {
        double value = Double.parseDouble (currentInput);
        if (value == 0) {
            currentInput = "0";
        } else {
            currentInput = formatResult(1 / value);
        }
        startNewInput = true;
        updateDisplay();
    }

    private void applyPercent() {
        double value = Double.parseDouble(currentInput);
        currentInput = formatResult(firstOperand * value / 100);
        startNewInput = true;
        updateDisplay();
    }

    private String formatResult(double value) {
        if (value == (long) value) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }

    private void updateDisplay() {
        txtDisplay.setText(currentInput);
    }
}