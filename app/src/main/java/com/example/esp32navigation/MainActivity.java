package com.example.esp32navigation;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;
import android.Manifest;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.annotation.NonNull;
import android.util.Log;


public class MainActivity extends AppCompatActivity {
    BluetoothAdapter bluetoothAdapter;
    BluetoothSocket btSocket;
    OutputStream outputStream;
    TextView tvNavigation;
    String ESP32_MAC = "00:11:22:33:44:55"; // Replace with your ESP32 MAC Address
    LocationManager locationManager;
    Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvNavigation = findViewById(R.id.tvNavigation);

        requestBluetoothPermissions();
    }

    private void requestBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN},
                        1);
                return;
            } else {
                initializeBluetooth();
            }
        } else {
            initializeBluetooth();
        }
    }

    private void initializeBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        connectToESP32();
        startLocationUpdates();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Bluetooth", "Bluetooth permission granted.");
                initializeBluetooth(); // Mulai koneksi Bluetooth setelah izin diberikan
            } else {
                Log.e("Bluetooth", "Bluetooth permission denied.");
                tvNavigation.setText("Bluetooth permission needed!");
            }
        }
    }



    private void connectToESP32() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 1);
                return;
            }
        }

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : pairedDevices) {
            if (device.getAddress().equals(ESP32_MAC)) {
                try {
                    btSocket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                    btSocket.connect();
                    outputStream = btSocket.getOutputStream();
                    tvNavigation.setText(getString(R.string.connected));
                } catch (IOException e) {
                    tvNavigation.setText(getString(R.string.connection_failed));
                }
                break;
            }
        }
    }


    private void startLocationUpdates() {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double speed = location.getSpeed() * 3.6; // Convert to km/h
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    sendNavigationData("SPEED:" + speed + " LAT:" + latitude + " LON:" + longitude);
                }
                @Override public void onStatusChanged(String provider, int status, Bundle extras) {}
                @Override public void onProviderEnabled(String provider) {}
                @Override public void onProviderDisabled(String provider) {}
            });
        } catch (SecurityException e) {
            tvNavigation.setText(getString(R.string.gps_permission_needed));
        }
    }

    private void sendNavigationData(String data) {
        handler.post(() -> {
            try {
                if (outputStream != null) {
                    outputStream.write((data + "\n").getBytes());  // Add newline!
                    outputStream.flush();
                }
            } catch (IOException e) {
                tvNavigation.setText(getString(R.string.failed_send_data));
            }
        });
    }
}

