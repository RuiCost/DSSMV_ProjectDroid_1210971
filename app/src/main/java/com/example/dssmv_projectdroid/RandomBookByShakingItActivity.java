package com.example.dssmv_projectdroid;

import adapter.ListViewAdapterToLovedBooks;
import adapter.MyDAtabaseHelper;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.*;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import service.RequestService;
import java.util.Random;
import java.util.List;

public class RandomBookByShakingItActivity  extends AppCompatActivity implements SensorEventListener {

    private TextView xTextView , yTextView,zTextView;
    private SensorManager sensorManager;

    private Sensor accelerometerSensor;

    private boolean isAccelerometerSensorAvailable, itIsNotFirstTime=false;

    private float currentX , currentY ,currentZ, lastX, lastY, lastZ , xDifference, yDifference,zDifference;

    private float shakeThreshold = 8f;

    private Vibrator vibrator;

    private String randomISBN;

    private Integer randomNum;

    private Integer randomPage;

    private boolean isProcessingShake = false;

    private static final long SHAKE_DELAY_MS = 2000; // 2 segundos

    private long lastShakeTime = 0;

    Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_book_by_shaking_it);

        /*xTextView = findViewById(R.id.xTEXT);
        yTextView = findViewById(R.id.yTEXT);
        zTextView = findViewById(R.id.zTEXT);
         */
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager =(SensorManager) getSystemService(Context.SENSOR_SERVICE);


        // VER SE O TELEMOVEL POSSUI acelerômetro disponível !!!!
        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null) {


            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); // funcao para obter o acelerômetro disponivel
            isAccelerometerSensorAvailable=true;
        } else {
            xTextView.setText("ACCELEROMETER NOT AVAILABLE");
            isAccelerometerSensorAvailable=false;
        }



    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        /*
        xTextView.setText(sensorEvent.values[0]+"m/s2");
        yTextView.setText(sensorEvent.values[1]+"m/s2");
        zTextView.setText(sensorEvent.values[2]+"m/s2");
        */
        currentX = sensorEvent.values[0];
        currentY = sensorEvent.values[1];
        currentZ = sensorEvent.values[2];

        if(itIsNotFirstTime && !isProcessingShake) {

            xDifference = Math.abs(lastX - currentX);
            yDifference = Math.abs(lastY - currentY);
            zDifference = Math.abs(lastZ - currentZ);

            // CHECAR SE O TELEMOVEL MEXEU ATÈ A UM DETERMINADO VALOR

            if((xDifference > shakeThreshold && yDifference > shakeThreshold) ||
                    (xDifference > shakeThreshold && zDifference > shakeThreshold) ||
                    (yDifference > shakeThreshold && zDifference > shakeThreshold)){

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));

                    if (!isProcessingShake) {
                        isProcessingShake = true;
                        new GetRandomISBNTask().execute();
                    }
                }

            }

        }

        lastX = currentX;
        lastY= currentY;
        lastZ = currentZ;

        itIsNotFirstTime = true;

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isAccelerometerSensorAvailable) {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        // Reset the isProcessingShake variable to false when the activity is resumed
        isProcessingShake = false;
        itIsNotFirstTime = false; // Reset the itIsNotFirstTime variable as well
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the sensor listener when the activity is paused
        sensorManager.unregisterListener(this, accelerometerSensor);
    }


    // AsyncTask para buscar o ISBN aleatório em background
    private class GetRandomISBNTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            // Fazer a chamada ao serviço para obter o ISBN aleatório
            randomNum = random.nextInt(2);
            randomPage = random.nextInt(3);
            return RequestService.get_RANDOM_ISBN("http://193.136.62.24/v1/search?page="+randomPage+"&query=" + randomNum);
        }

        @Override
        protected void onPostExecute(String randomISBN) {

            if (randomISBN != null) {
                // Se o ISBN aleatório foi obtido com sucesso, inicie a atividade InfBookActivity
                Intent intent = new Intent(RandomBookByShakingItActivity.this, InfBookActivity.class);
                intent.putExtra("ISBN", randomISBN);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Something went wrong, try again pls", Toast.LENGTH_LONG).show();
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isProcessingShake = false;
                }
            }, SHAKE_DELAY_MS);
        }
    }

}
