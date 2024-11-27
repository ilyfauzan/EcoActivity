package com.example.ecoactivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FormActivity extends AppCompatActivity {

    private Spinner kondisiSpinner;
    private ImageView backButton;
    private Button predictButton;
    private EditText ageInput; // EditText untuk usia pengguna
    private EditText nameInput; // EditText untuk nama pengguna
    private boolean isFirstSelection = true;
    private RadioGroup genderGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        backButton = findViewById(R.id.backButton);
        kondisiSpinner = findViewById(R.id.kondisiInput);
        predictButton = findViewById(R.id.PredictButton);
        genderGroup = findViewById(R.id.genderGroup); // Inisialisasi RadioGroup untuk gender
        ageInput = findViewById(R.id.ageInput); // EditText untuk input usia
        nameInput = findViewById(R.id.nameInput); // EditText untuk input nama

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.kondisi_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kondisiSpinner.setAdapter(adapter);

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(FormActivity.this, WeatherActivity.class);
            startActivity(intent);
            finish();
        });

        kondisiSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirstSelection) {
                    isFirstSelection = false;
                } else if (position == 0) {
                    Toast.makeText(FormActivity.this, "Silakan pilih kondisi yang sesuai", Toast.LENGTH_SHORT).show();
                    kondisiSpinner.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Ambil data dari Intent
        Intent intent = getIntent();
        double temperature = intent.getDoubleExtra("temperature", 0.0);
        double humidity = intent.getDoubleExtra("humidity", 0.0);
        double windSpeed = intent.getDoubleExtra("windSpeed", 0.0);
        double precipitation = intent.getDoubleExtra("precipitation", 0.0);
        double uvIndex = intent.getDoubleExtra("uvIndex", 0.0);
        double visibility = intent.getDoubleExtra("visibility", 0.0);

        Log.d("FormActivity", "Temperature: " + temperature);
        Log.d("FormActivity", "Humidity: " + humidity);
        Log.d("FormActivity", "WindSpeed: " + windSpeed);
        Log.d("FormActivity", "Precipitation: " + precipitation);
        Log.d("FormActivity", "UV Index: " + uvIndex);
        Log.d("FormActivity", "Visibility: " + visibility);

        predictButton.setOnClickListener(v -> {
            if (kondisiSpinner.getSelectedItemPosition() == 0) {
                Toast.makeText(FormActivity.this, "Silakan isi kolom terlebih dahulu", Toast.LENGTH_SHORT).show();
            } else if (genderGroup.getCheckedRadioButtonId() == -1) {
                // Cek apakah gender sudah dipilih
                Toast.makeText(FormActivity.this, "Silakan pilih gender terlebih dahulu", Toast.LENGTH_SHORT).show();
            } else {
                // Ambil usia pengguna dari EditText
                String ageText = ageInput.getText().toString().trim();
                String nameText = nameInput.getText().toString().trim();

                if (nameText.isEmpty()) {
                    Toast.makeText(FormActivity.this, "Silakan masukkan nama Anda", Toast.LENGTH_SHORT).show();
                } else if (ageText.isEmpty()) {
                    Toast.makeText(FormActivity.this, "Silakan masukkan usia Anda", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        int age = Integer.parseInt(ageText);
                        if (age < 0 || age > 120) {
                            Toast.makeText(FormActivity.this, "Usia tidak valid", Toast.LENGTH_SHORT).show();
                        } else {
                            // Ambil gender yang dipilih
                            int selectedGenderId = genderGroup.getCheckedRadioButtonId();
                            RadioButton selectedGenderButton = findViewById(selectedGenderId);
                            String selectedGender = selectedGenderButton.getText().toString();

                            // Ambil kondisi yang dipilih
                            String selectedCondition = kondisiSpinner.getSelectedItem().toString();
                            fetchPrediction(temperature, humidity, windSpeed, precipitation, uvIndex, visibility, age, nameText, selectedCondition, selectedGender);
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(FormActivity.this, "Usia harus berupa angka", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void fetchPrediction(double temperature, double humidity, double windSpeed,
                                 double precipitation, double uvIndex, double visibility, int age, String name, String condition, String gender) {
        OkHttpClient client = new OkHttpClient();

        // Bangun URL dengan parameter yang benar
        String url = "https://ecoactivity.my.id/predict?" +
                "temperature=" + temperature +
                "&humidity=" + humidity +
                "&windSpeed=" + windSpeed +
                "&precipitation=" + precipitation +
                "&uvIndex=" + uvIndex +
                "&visibility=" + visibility;

        // Log URL untuk memastikan formatnya benar
        Log.d("FormActivity", "Request URL: " + url);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("FormActivity", "Network Error: " + e.getMessage(), e);
                runOnUiThread(() -> Toast.makeText(FormActivity.this, "Gagal mengambil prediksi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    Log.d("FormActivity", "Response Data: " + responseData);

                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        String status = jsonObject.getString("status");
                        String result = jsonObject.getString("result");

                        runOnUiThread(() -> {
                            if ("SUCCESS".equals(status)) {
                                // Kirim hasil prediksi, usia, nama, kondisi, dan gender ke ResultActivity
                                Intent intent = new Intent(FormActivity.this, ResultActivity.class);
                                intent.putExtra("prediction", result);
                                intent.putExtra("age", age); // Kirim usia ke ResultActivity
                                intent.putExtra("name", name); // Kirim nama ke ResultActivity
                                intent.putExtra("kondisi", condition); // Kirim kondisi ke ResultActivity
                                intent.putExtra("gender", gender); // Kirim gender ke ResultActivity
                                startActivity(intent);
                            } else {
                                Toast.makeText(FormActivity.this, "Prediksi gagal: " + result, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (JSONException e) {
                        Log.e("FormActivity", "JSON Parsing Error: " + e.getMessage());
                        Log.e("FormActivity", "Response Body: " + responseData);
                        runOnUiThread(() -> Toast.makeText(FormActivity.this, "Kesalahan parsing JSON", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    Log.e("FormActivity", "Response failed: " + response.code() + ", " + response.message());
                    runOnUiThread(() -> Toast.makeText(FormActivity.this, "Respon gagal: " + response.message(), Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}
