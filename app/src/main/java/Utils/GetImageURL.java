package Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;

public class GetImageURL extends AsyncTask<String, Void, Bitmap> {
    ImageView imageView;
    public GetImageURL(ImageView imageView)
    {
        this.imageView = imageView;
    }

    protected Bitmap doInBackground(String...urls){
        String imgUrl = urls[0];
        Bitmap bitmap = null;

        try {
            InputStream inputStream = new java.net.URL(imgUrl).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        }catch (Exception e) {}

        return bitmap;
    }

    protected void onPostExecute(Bitmap result)
    {
        imageView.setImageBitmap(result);
    }
}
