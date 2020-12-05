package com.example.assignment1;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class StorageManager {
    public void saveInternal(Activity activity, String toStore){
        FileOutputStream fileOutputStream = null;
        try {
            File file = activity.getFilesDir();
            fileOutputStream = activity.openFileOutput("quizResults.txt", Context.MODE_APPEND);
            fileOutputStream.write(toStore.getBytes());

            return;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveInternalNew(Activity activity, String toStore){
        FileOutputStream fileOutputStream = null;
        try {
            File file = activity.getFilesDir();
            fileOutputStream = activity.openFileOutput("quizResults.txt", Context.MODE_PRIVATE);
            fileOutputStream.write(toStore.getBytes());

            return;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String loadFromInternal(Activity activity){
        String loadedData = "";
        try {
            FileInputStream fileInputStream =  activity.openFileInput("quizResults.txt");
            int read = -1;
            StringBuffer buffer = new StringBuffer();
            while((read =fileInputStream.read())!= -1){
                buffer.append((char)read);
            }
            Log.d("Code", buffer.toString());

            loadedData = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loadedData;
    }
}
