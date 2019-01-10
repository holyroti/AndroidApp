package nl.carlodvm.androidapp.Core;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DestinationBitmapReader {
    private Activity activity;
    String[] fileNames = {
            "1_LinksVoor.png",
            "2_LinksAchter.png",
            "3_RechtsVoor.png",
            "4_RechtsAchter.png",
            "5_Voordeur.png"
    };

    public DestinationBitmapReader(Activity activity) {
        this.activity = activity;
    }

    public Map<Integer, Bitmap> ReadBitmaps() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        HashMap<Integer, Bitmap> bitmapHashMap = new HashMap<Integer, Bitmap>();
        for (String fileName : fileNames) {
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(activity.getAssets().open(fileName));
                bitmapHashMap.put(Integer.parseInt(fileName.split("_")[0]), bitmap);
            } catch (IOException e) {
                Log.e(DestinationBitmapReader.class.getSimpleName(), "Could not find bitmaps.");
            }
        }
        return bitmapHashMap;
    }
}
