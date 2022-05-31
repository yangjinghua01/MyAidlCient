package com.example.myinput;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Rect;
import android.os.Bundle;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.IRemoteService;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "ClientActivity";
    private IRemoteService iRemoteService;
    private Button mBindServiceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBindServiceButton = findViewById(R.id.btn_bind_service);
        mBindServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction("com.example.myapplication.action");
                    intent.setPackage("com.example.myapplication");
                    bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

            }
        });
    }

    ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");
            iRemoteService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected");
            iRemoteService = IRemoteService.Stub.asInterface(service);
            int currentPid = Process.myPid();
            Log.d(TAG, "currentPID: " + currentPid + ", remotePID: " );
            try {
                iRemoteService.basicTypes(12, 123, true, 123.4f, 123.45, "服务端你好，我是客户端");
                List<Rect> rect = iRemoteService.getRect();
                Log.i(TAG, "onServiceConnected: "+rect.toString());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mBindServiceButton.setText("Unbind Service");
        }
    };
}
