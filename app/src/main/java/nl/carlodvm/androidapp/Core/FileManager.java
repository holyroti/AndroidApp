package nl.carlodvm.androidapp.Core;

import android.content.Context;
import android.util.Log;

import java.io.*;

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

    public FileOutputStream getFileOutputStream(String filename) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            Log.e(FileManager.class.getSimpleName(), "File does not exist.");
        }
        return fos;
    }

    public OutputStreamWriter getFileOutputStreamWriter(String filename) {
        return new OutputStreamWriter(getFileOutputStream(filename));
    }

    public ObjectOutputStream getObjectOutputStream(String filename) {
        ObjectOutputStream objS = null;
        try {
            objS = new ObjectOutputStream(getFileOutputStream(filename));
        } catch (IOException e) {
            Log.e(FileManager.class.getSimpleName(), "ObjectOutputStream could not be created.");
        }
        return objS;
    }

}
