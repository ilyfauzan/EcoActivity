package com.example.ecoactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.text.DecimalFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class WeatherActivity extends AppCompatActivity {

    // UI components for city weather details
    private TextView cityNameTextView, weatherDescriptionTextView, weatherDetailsTextView;
    // UI components for forecast data
    private TextView tvNowTime, tv11Time, tv12Time, tvNowTemp, tv11Temp, tv12Temp;
    private TextView tvTodayTime, tvTomorrowTime, tvDayAfterTomorrowTime;
    private TextView tvTodayTemp, tvTomorrowTemp, tvDayAfterTomorrowTemp;
    private TextView humidityTextView, windSpeedTextView;

    private Button goActivityBtn;
    private ImageView ivNowIcon, iv11Icon, iv12Icon, ivTodayIcon, ivTomorrowIcon, ivDayAfterTomorrowIcon;

    private final String API_KEY = "ddW3GB7rDaI2YPgS3HqVrbA3LO7wrGZi";
    private String LATITUDE = "-6.200000";
    private String LONGITUDE = "106.816666";
    private FusedLocationProviderClient fusedLocationClient;

    private double currentTemperature, currentHumidity, currentWindSpeed, currentPrecipitation, currentUvIndex, currentVisibility;
    private final String CITY_NAME = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SharedPreferences sharedPreferences = getSharedPreferences("location_data", MODE_PRIVATE);
        LATITUDE = sharedPreferences.getString("latitude", "-6.200000"); // Default to your default value if not found
        LONGITUDE = sharedPreferences.getString("longitude", "106.816666");


        // Request location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }


        // Initialize city weather detail UI components
        cityNameTextView = findViewById(R.id.cityNameTextView);
        weatherDescriptionTextView = findViewById(R.id.weatherDescriptionTextView);
        weatherDetailsTextView = findViewById(R.id.weatherDetailsTextView);
        humidityTextView = findViewById(R.id.humidityTextView);
        windSpeedTextView = findViewById(R.id.windSpeedTextView);


        // Initialize forecast UI components
        tvNowTime = findViewById(R.id.tvNow);
        tv11Time = findViewById(R.id.tv11Time);
        tv12Time = findViewById(R.id.tv12Time);

        tvNowTemp = findViewById(R.id.tvNowTemp);
        tv11Temp = findViewById(R.id.tv11Temp);
        tv12Temp = findViewById(R.id.tv12Temp);

        tvTodayTime = findViewById(R.id.tvTodayTime);
        tvTomorrowTime = findViewById(R.id.tvTomorrowTime);
        tvDayAfterTomorrowTime = findViewById(R.id.tvDayAfterTomorrowTime);

        tvTodayTemp = findViewById(R.id.tvTodayTemp);
        tvTomorrowTemp = findViewById(R.id.tvTomorrowTemp);
        tvDayAfterTomorrowTemp = findViewById(R.id.tvDayAfterTomorrowTemp);

        ivNowIcon = findViewById(R.id.ivNowIcon);
        iv11Icon = findViewById(R.id.iv11Icon);
        iv12Icon = findViewById(R.id.iv12Icon);
        ivTodayIcon = findViewById(R.id.ivTodayIcon);
        ivTomorrowIcon = findViewById(R.id.ivTomorrowIcon);
        ivDayAfterTomorrowIcon = findViewById(R.id.ivDayAfterTomorrowIcon);
        goActivityBtn = findViewById(R.id.GoActivityBtn);

        goActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pastikan data cuaca sudah diperoleh sebelum melanjutkan
                if (currentTemperature != 0.0) {
                    Intent intent = new Intent(WeatherActivity.this, FormActivity.class);
                    intent.putExtra("temperature", currentTemperature);
                    intent.putExtra("humidity", currentHumidity);
                    intent.putExtra("windSpeed", currentWindSpeed);
                    intent.putExtra("precipitation", currentPrecipitation);
                    intent.putExtra("uvIndex", currentUvIndex);
                    intent.putExtra("visibility", currentVisibility);

                    Log.d("WeatherActivity", "Temperature: " + currentTemperature);
                    Log.d("WeatherActivity", "Humidity: " + currentHumidity);
                    Log.d("WeatherActivity", "WindSpeed: " + currentWindSpeed);
                    Log.d("WeatherActivity", "Precipitation: " + currentPrecipitation);
                    Log.d("WeatherActivity", "UV Index: " + currentUvIndex);
                    Log.d("WeatherActivity", "Visibility: " + currentVisibility);

                    startActivity(intent);
                } else {
                    Toast.makeText(WeatherActivity.this, "Data cuaca belum tersedia. Tunggu sebentar.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Set dynamic times and day names
        setDynamicTimes();
        setDynamicDayNames();

        // Fetch both current weather and forecast data
        fetchCurrentWeatherData();
        fetchForecastData();
    }

    private void setDynamicTimes() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        tvNowTime.setText("Now");

        calendar.add(Calendar.HOUR_OF_DAY, 1);
        tv11Time.setText(timeFormat.format(calendar.getTime()));

        calendar.add(Calendar.HOUR_OF_DAY, 1);
        tv12Time.setText(timeFormat.format(calendar.getTime()));
    }

    private void getCurrentLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Save the location to SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("location_data", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("latitude", String.valueOf(location.getLatitude()));
                            editor.putString("longitude", String.valueOf(location.getLongitude()));
                            editor.apply();

                            // Update LATITUDE and LONGITUDE with the current location
                            LATITUDE = String.valueOf(location.getLatitude());
                            LONGITUDE = String.valueOf(location.getLongitude());

                            fetchCityName(location.getLatitude(), location.getLongitude());

                            // Fetch weather data with updated location
                            fetchCurrentWeatherData();
                            fetchForecastData();
                        } else {
                            Toast.makeText(WeatherActivity.this, "Unable to get location.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void setDynamicDayNames() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

        tvTodayTime.setText("Now");

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        tvTomorrowTime.setText(dayFormat.format(calendar.getTime()));

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        tvDayAfterTomorrowTime.setText(dayFormat.format(calendar.getTime()));
    }

    private void fetchCurrentWeatherData() {
        OkHttpClient client = new OkHttpClient();
        String url = "https://api.tomorrow.io/v4/weather/realtime?location=" + LATITUDE + "," + LONGITUDE + "&apikey=" + API_KEY;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("accept", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                updateCityWeatherUI(CITY_NAME, "Request Failed", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    updateCityWeatherUI(CITY_NAME, "Response not successful", response.message());
                    return;
                }

                String responseData = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    JSONObject values = jsonObject.getJSONObject("data").getJSONObject("values");

                    // Get the current temperature
                    currentTemperature = values.getDouble("temperature");
                    currentUvIndex = values.getDouble("uvIndex");
                    currentHumidity = values.getDouble("humidity");
                    currentPrecipitation = values.optDouble("precipitationIntensity", 0.0);
                    currentVisibility = values.getDouble("visibility");
                    currentWindSpeed = values.getDouble("windSpeed");

                    // Format the temperature to show only one digit
                    DecimalFormat df = new DecimalFormat("#");
                    String formattedTemp = df.format(currentTemperature);

                    // Get weather description
                    int weatherCode = values.getInt("weatherCode");
                    String weatherDescription = getWeatherDescription(weatherCode);
                    String weatherDetails = formattedTemp + "°";

                    // Update the UI for current weather
                    updateCityWeatherUI(CITY_NAME, weatherDescription, weatherDetails);

                    // Update the temperature in UI components (tvNowTemp and tvTodayTemp)
                    new Handler(Looper.getMainLooper()).post(() -> {
                        tvNowTemp.setText(formattedTemp + "°");
                        tvTodayTemp.setText(formattedTemp + "°");
                        String formattedHumidity = "Humidity: " + currentHumidity + "%";
                        String formattedWindSpeed = "Wind Speed: " + currentWindSpeed + " km/h";

                        humidityTextView.setText(formattedHumidity);
                        windSpeedTextView.setText(formattedWindSpeed);
                    });

                } catch (JSONException e) {
                    updateCityWeatherUI(CITY_NAME, "JSON Parsing Error", e.getMessage());
                }
            }
        });
    }

    private void fetchForecastData() {
        OkHttpClient client = new OkHttpClient();
        String url = "https://api.tomorrow.io/v4/weather/forecast?location=" + LATITUDE + "," + LONGITUDE + "&apikey=" + API_KEY;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("accept", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("MainActivity", "Forecast Request Failed", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        JSONArray timelines = jsonObject.getJSONObject("timelines").getJSONArray("hourly");

                        JSONObject data11 = timelines.getJSONObject(0);
                        JSONObject data12 = timelines.getJSONObject(1);
                        JSONObject tomorrowData = timelines.getJSONObject(2);
                        JSONObject dayAfterTomorrowData = timelines.getJSONObject(3);

                        final String temp11 = formatTemperature(data11.getJSONObject("values").getString("temperature"));
                        final String temp12 = formatTemperature(data12.getJSONObject("values").getString("temperature"));
                        final String tomorrowTemp = formatTemperature(tomorrowData.getJSONObject("values").getString("temperature"));
                        final String dayAfterTomorrowTemp = formatTemperature(dayAfterTomorrowData.getJSONObject("values").getString("temperature"));

                        new Handler(Looper.getMainLooper()).post(() -> {
                            tv11Temp.setText(temp11 + "°");
                            tv12Temp.setText(temp12 + "°");
                            tvTomorrowTemp.setText(tomorrowTemp + "°");
                            tvDayAfterTomorrowTemp.setText(dayAfterTomorrowTemp + "°");

                            ivNowIcon.setImageResource(R.drawable.cloud_icon);
                            iv11Icon.setImageResource(R.drawable.cloud_icon);
                            iv12Icon.setImageResource(R.drawable.cloud_icon);
                            ivTodayIcon.setImageResource(R.drawable.cloud_icon);
                            ivTomorrowIcon.setImageResource(R.drawable.cloud_icon);
                            ivDayAfterTomorrowIcon.setImageResource(R.drawable.cloud_icon);
                        });
                    } catch (JSONException e) {
                        Log.e("MainActivity", "Forecast JSON Parsing Error", e);
                    }
                }
            }
        });
    }

    private void fetchCityName(double latitude, double longitude) {
        OkHttpClient client = new OkHttpClient();
        String apiKey = "d48d8e89c7ef4ef2a784bc73b98d7e42";
        String url = "https://api.opencagedata.com/geocode/v1/json?q="
                + latitude + "," + longitude + "&key=" + apiKey;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("WeatherActivity", "Geocoding Request Failed", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        JSONArray results = jsonObject.getJSONArray("results");

                        if (results.length() > 0) {
                            JSONObject components = results.getJSONObject(0)
                                    .getJSONObject("components");

                            String cityName = components.optString("city", "Unknown City");

                            // Update UI with the city name
                            new Handler(Looper.getMainLooper()).post(() -> {
                                cityNameTextView.setText(cityName);
                            });
                        }
                    } catch (JSONException e) {
                        Log.e("WeatherActivity", "Geocoding JSON Parsing Error", e);
                    }
                }
            }
        });
    }


    // Helper function to format the temperature
    private String formatTemperature(String temperature) {
        DecimalFormat df = new DecimalFormat("#");
        double temp = Double.parseDouble(temperature);
        return df.format(temp);
    }


    private void updateCityWeatherUI(final String cityName, final String description, final String details) {
        new Handler(Looper.getMainLooper()).post(() -> {
            cityNameTextView.setText(cityName);
            weatherDescriptionTextView.setText(description);
            weatherDetailsTextView.setText(details);
        });
    }

    private String getWeatherDescription(int weatherCode) {
        switch (weatherCode) {
            case 1000: return "Clear Sky";
            case 1001: return "Cloudy";
            case 1100: return "Mostly Clear";
            case 1101: return "Partly Cloudy";
            case 1102: return "Mostly Cloudy";
            case 2000: return "Fog";
            case 2100: return "Light Fog";
            case 3000: return "Light Wind";
            case 3001: return "Windy";
            case 3002: return "Strong Wind";
            case 4000: return "Drizzle";
            case 4001: return "Rain";
            case 4200: return "Light Rain";
            case 4201: return "Heavy Rain";
            case 5000: return "Snow";
            case 5001: return "Flurries";
            case 5100: return "Light Snow";
            case 5101: return "Heavy Snow";
            case 6000: return "Freezing Drizzle";
            case 6001: return "Freezing Rain";
            case 6200: return "Light Freezing Rain";
            case 6201: return "Heavy Freezing Rain";
            case 7000: return "Ice Pellets";
            case 7101: return "Heavy Ice Pellets";
            case 7102: return "Light Ice Pellets";
            case 8000: return "Thunderstorm";
            default: return "Unknown Condition";
        }
    }
}
