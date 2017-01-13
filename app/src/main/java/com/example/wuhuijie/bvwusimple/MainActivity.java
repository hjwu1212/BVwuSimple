package com.example.wuhuijie.bvwusimple;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import junit.framework.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class MainActivity extends Activity  implements SensorEventListener{
    private String Tag = "SetpCounter";
    private SensorManager mSensorManager;
    private Sensor mStepCounter;
    private TextView stepCounter_tv;
    private Button startStep_bt;
    private Button stopStep_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mStepCounter = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        stepCounter_tv = (TextView) findViewById(R.id.textView2);
        startStep_bt = (Button) findViewById(R.id.start);
        stopStep_bt = (Button) findViewById(R.id.end);
    }
    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy){

    }

    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            return hexString.toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public final void onSensorChanged(SensorEvent event){
        int valut = (int)event.values[0];
        Log.d( Tag,"" + valut );
        String stepcounter_t = Integer.toString((int) valut);
        Log.d(Tag, " int: " + stepcounter_t);
        stepCounter_tv.setText(stepcounter_t);
    }

    @Override
    public boolean  onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void startCountStep(View view){
        Log.d(Tag, "Enter startcountStep function");
        String str;
        str = sHA1(this);
        Log.d(Tag, str);
        mSensorManager.registerListener(this, mStepCounter, SensorManager.SENSOR_DELAY_FASTEST);
        stopStep_bt.setEnabled(true);
        startStep_bt.setEnabled(false);
    }

    public void stopCountStep(View view){
        Log.d(Tag, "Enter stopCountStep function");
        mSensorManager.unregisterListener(this);
        startStep_bt.setEnabled(true);
        stopStep_bt.setEnabled(false);
        sendMassageToMap();
    }

    public void sendMassageToMap(){
        Intent mapIntent = new Intent(this, MapActivity.class);
        startActivity(mapIntent);
    }

    public void sendMassageToTest(){
        Intent testIntent = new Intent(this, TestInentActivity.class);
        startActivity(testIntent);
    }
}
