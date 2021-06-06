package org.meicode.appfilm.API;

import org.meicode.appfilm.Utils.AppConstraint;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientExecutor {
    private static ClientExecutor executor_instance = null;
    public Retrofit retrofit;

    private ClientExecutor() {
        retrofit = new Retrofit.Builder()
                .baseUrl(AppConstraint.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ClientExecutor getInstance() {
        if (executor_instance == null)
            executor_instance = new ClientExecutor();

        return executor_instance;
    }
}
