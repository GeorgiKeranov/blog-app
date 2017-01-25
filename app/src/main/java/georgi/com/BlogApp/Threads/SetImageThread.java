package georgi.com.BlogApp.Threads;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static georgi.com.BlogApp.Configs.ServerURLs.USER_IMAGES_URL;

public class SetImageThread extends AsyncTask<String, Void, Bitmap> {

    private ImageView userImage;


    public SetImageThread(ImageView userImage){
        this.userImage = userImage;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        try {
            String urlForImage = USER_IMAGES_URL + strings[0] + "/" + strings[1];
            urlForImage = urlForImage.replace(" ", "%20");

            URL url = new URL(urlForImage);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            return bmp;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        userImage.setImageBitmap(bitmap);
    }
}
