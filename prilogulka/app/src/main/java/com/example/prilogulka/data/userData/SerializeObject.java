package com.example.prilogulka.data.userData;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializeObject<T> implements Serializable {

    private String CLASS_TAG = "SerializeObject";

    private Context context;

    public SerializeObject(Context context) {
        this.context = context;
    }

    public void writeObject(T obj, Class c) throws IOException {
        FileOutputStream fos = context.openFileOutput("temp.out", Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(obj);
        oos.flush();
        oos.close();

        Log.i(CLASS_TAG, "SERIALIZED");
    }

    public T readObject() throws IOException, ClassNotFoundException {
        FileInputStream fis = context.openFileInput("temp.out");
        ObjectInputStream is = new ObjectInputStream(fis);
        T newO = (T) is.readObject();
        is.close();
        fis.close();;

        Log.i(CLASS_TAG, "READ OBJ COMPLETE");

        return newO;
    }
}
