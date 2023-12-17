package com.subhajit.whetherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    EditText et_city,et_country;
    TextView tv_result;
    private final String url="https://api.openweathermap.org/data/2.5/weather";
    private final String AppId="e53301e27efa0b66d05045d91b2742d3";
    DecimalFormat decimalFormat=new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_city=(EditText) findViewById(R.id.et_city);
        et_country=(EditText) findViewById(R.id.et_country);
        tv_result=(TextView) findViewById(R.id.tv_result);

    }

    public void GetWhetherDetails(View view) {
        String tempUrl="";
        String city=et_city.getText().toString().trim();
        String Country=et_country.getText().toString().trim();

        if(city.equals("")){
tv_result.setText("City Field Can not be Empty");
        }
        else{
            if(!Country.equals("")){
                tempUrl=url+"?q="+city+","+Country+"&appid="+AppId;
            }
            else{
                tempUrl=url+"?q="+city+"&appid="+AppId;
            }
            StringRequest stringRequest=new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                   // Log.d( "onResponse: ",response);
                    String output="";
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        String description = jsonObjectWeather.getString("description");
                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        double temp = jsonObjectMain.getDouble("temp") - 273.15;
                        double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                        float pressure = jsonObjectMain.getInt("pressure");
                        int humidity = jsonObjectMain.getInt("humidity");
                        JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                        String wind = jsonObjectWind.getString("speed");
                        JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                        String clouds = jsonObjectClouds.getString("all");
                        JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                        String countryName = jsonObjectSys.getString("country");
                        String cityName = jsonResponse.getString("name");
                        tv_result.setTextColor(Color.rgb(68,134,199));
                        output += "Current weather of " + cityName + " (" + countryName + ")"
                                + "\n Temp: " + decimalFormat.format(temp) + " °C"
                                + "\n Feels Like: " + decimalFormat.format(feelsLike) + " °C"
                                + "\n Humidity: " + humidity + "%"
                                + "\n Description: " + description
                                + "\n Wind Speed: " + wind + "m/s (meters per second)"
                                + "\n Cloudiness: " + clouds + "%"
                                + "\n Pressure: " + pressure + " hPa";
                        tv_result.setText(output);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }

    }
}