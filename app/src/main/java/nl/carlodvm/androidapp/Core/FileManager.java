package nl.carlodvm.androidapp.Core;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import nl.carlodvm.androidapp.R;

public class FileManager {
    private Context context;

    public FileManager(Context context){
        this.context = context;
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public File createFile(String filename){
        File file = new File(context.getFilesDir(), filename);
        return file;
    }

    public File createFile(String dir, String filename) {
        File file = new File(dir, filename);
        try {
            if (!file.exists())
                file.createNewFile();
        } catch (IOException e) {
            Log.e(FileManager.class.getSimpleName(), context.getString(R.string.FileCreationErrorMessage));
        }
        return file;
    }

    public boolean fileExists(String dir, String filename) {
        return new File(dir, filename).exists();
    }

    public boolean fileExists(String filename){
        return new File(filename).exists();
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
            Log.e(FileManager.class.getSimpleName(), context.getString(R.string.FileNotFoundErrorMessage));
        }
        return new OutputStreamWriter(fos);
    }

    public static void copy(File src, File dst) throws IOException {
        try (InputStream in = new FileInputStream(src)) {
            try (OutputStream out = new FileOutputStream(dst)) {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        }
    }

    public static void copy(InputStream src, OutputStream dst) throws IOException {
        try (InputStream in = src) {
            try (OutputStream out = dst) {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        }
    }
}
