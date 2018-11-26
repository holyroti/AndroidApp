package nl.carlodvm.androidapp.Core;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class FileManager {
    private Context context;

    public FileManager(Context context){
        this.context = context;
    }

    public File createFile(String filename){
        File file = new File(context.getFilesDir(), filename);
        return file;
    }

    public boolean fileExists(String filename){
        return createFile(filename).exists();
    }

    public FileInputStream getFileInputStream(String filename){
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(filename);
        } catch (FileNotFoundException e) {
            Log.e(FileManager.class.getSimpleName(), "File does not exist.");
        }
        return fis;
    }

    public FileOutputStream getFileOutputStream(String filename){
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            Log.e(FileManager.class.getSimpleName(), "File does not exist.");
        }
        return fos;
    }

}
