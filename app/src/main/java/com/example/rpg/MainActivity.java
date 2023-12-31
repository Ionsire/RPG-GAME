package com.example.rpg;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.rpg.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private static final int STORAGE_PERMISSION_CODE = 101;

    AnimationDrawable frameAnimation;
    AnimationDrawable mainFrameAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);


        ImageView img = findViewById(R.id.imageView2);
        img.setBackgroundResource(R.drawable.animation);
        frameAnimation = (AnimationDrawable) img.getBackground();


        ImageView mainImg = findViewById(R.id.imageView);
        mainImg.setBackgroundResource(R.drawable.main_screen_animation);
        mainFrameAnimation = (AnimationDrawable) mainImg.getBackground();


        // Testando service
        if(!foregroundServiceRunning()) {
            Intent serviceIntent = new Intent(this, ForegroundService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent);
            }
        }

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Algum comando
                //frameAnimation.start();
            }
        });

        // Playing the Game
        findViewById(R.id.imageView4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //frameAnimation.start();
        mainFrameAnimation.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission]
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { permission }, requestCode);
        }
        else {
            System.out.println("Permissao já concedida");
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            System.out.println("Garantidas:"+ grantResults.length);
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean foregroundServiceRunning(){
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service: activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if(ForegroundService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }



}