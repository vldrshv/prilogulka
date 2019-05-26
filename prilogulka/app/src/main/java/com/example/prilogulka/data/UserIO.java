package com.example.prilogulka.data;

import android.content.Context;

import com.example.prilogulka.data.service.UserService;
import com.example.prilogulka.data.userData.SerializeObject;
import com.example.prilogulka.data.userData.UserInfo;

import java.io.IOException;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserIO {
    private Context context;

    public UserIO(Context context) {
        this.context = context;
    }

    public UserInfo readUser(){
        SerializeObject<UserInfo> so = new SerializeObject<UserInfo>(context);
        UserInfo user = new UserInfo();
        try {
            user = so.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.getStackTrace();
        }
        return user;
    }
    public void writeUser(UserInfo user) {
        try {
            if (user.getUser() != null) {
                SerializeObject so = new SerializeObject(context);
                so.writeObject(user, user.getClass());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UserInfo getUserFromServer(String email) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://92.53.65.46:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserService service = retrofit.create(UserService.class);
        UserInfo user = null;
        try {
            user = service.getUser(email).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return user;
    }

    public double getMoney() {
        UserInfo user = readUser();
        return user == null ? 0 : user.getUser().getCurrent_balance();
    }
}
