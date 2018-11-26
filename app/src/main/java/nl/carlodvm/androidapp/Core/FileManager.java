package nl.carlodvm.androidapp.Core;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

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

    public BufferedReader getFileInputStreamReader(String filename){
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(filename);
        } catch (FileNotFoundException e) {
            Log.e(FileManager.class.getSimpleName(), "File does not exist.");
        }
        BufferedInputStream ir = new BufferedInputStream(fis);
        BufferedReader br = new BufferedReader(new InputStreamReader(ir));
        return br;
    }

    public OutputStreamWriter getFileOutputStream(String filename){
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            Log.e(FileManager.class.getSimpleName(), "File does not exist.");
        }
        return new OutputStreamWriter(fos);
    }

}
