package in.visibleinfotech.viplfieldapplications.docs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;
import java.io.InputStream;

import in.visibleinfotech.viplfieldapplications.MainConstant;
import in.visibleinfotech.viplfieldapplications.R;

public class ImageActivty extends AppCompatActivity {
    final int portNumber = 21;
    ImageView imageView1, imageView2;
    TextView l_noTv;
    String l_no;
    String image, image2;

    MainConstant c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc_activity_image_activty);
        imageView1 = findViewById(R.id.imageView1);
        l_no = getIntent().getStringExtra("doc_id");
        c = new MainConstant(this);
        image = c.getFtpPath() + "doc_" + l_no + ".jpg";
        Log.d("myname", image);
        new RetrieveFeedTask().execute();

    }


    class RetrieveFeedTask extends AsyncTask<String, Void, Bitmap> {
        Dialog dialog;
        private Exception exception;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(ImageActivty.this, "Loading Image", "Please wait...");
        }

        protected Bitmap doInBackground(String... urls) {
            Bitmap bitmap = null;

            try {
                FTPClient ftp = null;


                ftp = new FTPClient();
                ftp.connect(c.getFtpServer(), portNumber);
                Log.d("myname", "Connected. Reply: " + ftp.getReplyString());

                ftp.login(c.getFtpUser(), c.getFtpPass());
                Log.d("myname", "Logged in");
                ftp.setFileType(FTP.BINARY_FILE_TYPE);
                Log.d("myname", "Downloading");
                ftp.enterLocalPassiveMode();
                InputStream inStream = ftp.retrieveFileStream(image);
                // ftpClient.storeFile(fileName, buffIn);
                bitmap = BitmapFactory.decodeStream(inStream);

            } catch (IOException e) {
                Log.d("myname", e.getMessage());
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap bitmap) {
            // TODO: check this.exception
            // TODO: do something with the feed
            if (bitmap != null) {
                imageView1.setImageBitmap(bitmap);
            }
            dialog.dismiss();
        }
    }
}
