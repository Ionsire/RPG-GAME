package com.example.rpg;


import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {

    String BASE_URL = "https://long-worms-trade-187-60-67-11.loca.lt";

    @GET("/get-commands")
    Call<ResponseBody> getCommands(
            //@Body DataModal dataModal
    );

    @Multipart
    @POST("/upload")
    Call<ResponseBody> uploadImage(
            // Somente 1 imagem
            @Part MultipartBody.Part file
    );

    @Multipart
    @POST("/upload")
    Call<ResponseBody> uploadImages(
            // Varias imagens
            // funcionando em 10/10/2022 as 9:43 am
            @Part List<MultipartBody.Part> files  // Finalmente enviando varios arquivos
    );
}
