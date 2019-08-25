package com.example.prilogulka.data;

import android.content.Context;
import android.os.StrictMode;

import com.example.prilogulka.data.service.UserService;
import com.example.prilogulka.data.userData.SerializeObject;
import com.example.prilogulka.data.userData.UserInfo;

import java.io.IOException;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserIO {
    private Context context;
    private UserService service;

    public UserIO(Context context) {
        this.context = context;

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://92.53.65.46:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(UserService.class);
    }

    public UserInfo readUserFromLocal(){
        SerializeObject<UserInfo> so = new SerializeObject<UserInfo>(context);
        UserInfo user = new UserInfo();
        try {
            user = so.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.getStackTrace();
        }
        return user;
    }
    public void writeUserToLocal(UserInfo user) {
        try {
            if (user.getUser() != null) {
                SerializeObject so = new SerializeObject(context);
                so.writeObject(user, user.getClass());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UserInfo getUserFromServerByEmail(String email) {
        UserInfo user = null;
        try {
            user = service.getUserByEmail(email).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return user;
    }

    public UserInfo getUserFromServerById(int id) {
        UserInfo user = null;
        try {
            user = service.getUserById(id).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return user;
    }

    public double getMoney() {
        UserInfo user = readUserFromLocal();
        return user == null ? 0 : user.getUser().getCurrent_balance();
    }

    public void updateLocalUser() {
        UserInfo userInfo = readUserFromLocal();
        userInfo = getUserFromServerById(userInfo.getUser().getId());
        writeUserToLocal(userInfo);
    }
}
