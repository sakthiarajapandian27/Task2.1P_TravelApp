package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerCategory, spinnerFrom, spinnerTo;
    EditText editValue;
    TextView textResult;
    Button btnConvert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        editValue = findViewById(R.id.editValue);
        textResult = findViewById(R.id.textResult);
        btnConvert = findViewById(R.id.btnConvert);

        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.category_array,
                android.R.layout.simple_spinner_item
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selected = parent.getItemAtPosition(position).toString();
                int arrayId;

                if (selected.equals("Currency")) {
                    arrayId = R.array.currency_array;
                } else if (selected.equals("Fuel")) {
                    arrayId = R.array.fuel_array;
                } else {
                    arrayId = R.array.temperature_array;
                }

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                        MainActivity.this,
                        arrayId,
                        android.R.layout.simple_spinner_item
                );

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinnerFrom.setAdapter(adapter);
                spinnerTo.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String inputStr = editValue.getText().toString();

                if (inputStr.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter value", Toast.LENGTH_SHORT).show();
                    return;
                }

                double input;

                try {
                    input = Double.parseDouble(inputStr);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Invalid input", Toast.LENGTH_SHORT).show();
                    return;
                }

                String category = spinnerCategory.getSelectedItem().toString();
                String from = spinnerFrom.getSelectedItem().toString();
                String to = spinnerTo.getSelectedItem().toString();

                if (from.equals(to)) {
                    textResult.setText("Result: " + input);
                    return;
                }

                double result = 0;

                if (category.equals("Currency")) {

                    double usd = 0;

                    if (from.equals("USD")) usd = input;
                    else if (from.equals("AUD")) usd = input / 1.55;
                    else if (from.equals("EUR")) usd = input / 0.92;
                    else if (from.equals("JPY")) usd = input / 148.50;
                    else if (from.equals("GBP")) usd = input / 0.78;

                    if (to.equals("USD")) result = usd;
                    else if (to.equals("AUD")) result = usd * 1.55;
                    else if (to.equals("EUR")) result = usd * 0.92;
                    else if (to.equals("JPY")) result = usd * 148.50;
                    else if (to.equals("GBP")) result = usd * 0.78;

                } else if (category.equals("Fuel")) {

                    if (input < 0) {
                        Toast.makeText(MainActivity.this, "Invalid value", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (from.equals("mpg") && to.equals("km/L")) {
                        result = input * 0.425;
                    } else if (from.equals("km/L") && to.equals("mpg")) {
                        result = input / 0.425;
                    }

                } else if (category.equals("Temperature")) {

                    if (from.equals("C") && to.equals("F")) {
                        result = (input * 1.8) + 32;
                    } else if (from.equals("F") && to.equals("C")) {
                        result = (input - 32) / 1.8;
                    } else if (from.equals("C") && to.equals("K")) {
                        result = input + 273.15;
                    }
                }

                textResult.setText("Result: " + String.format("%.2f", result));
            }
        });
    }
}