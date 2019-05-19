package com.example.prilogulka.data;

import android.content.Context;

import com.example.prilogulka.data.userData.SerializeObject;
import com.example.prilogulka.data.userData.UserInfo;

import java.io.IOException;

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
    public void writeUser(UserInfo user, Context context) {
        try {
            if (user.getUser() != null) {
                SerializeObject so = new SerializeObject(context);
                so.writeObject(user, user.getClass());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
