package in.visibleinfotech.viplfieldapplications.attendance;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import in.visibleinfotech.viplfieldapplications.ConnectionStr;
import in.visibleinfotech.viplfieldapplications.MainConstant;
import in.visibleinfotech.viplfieldapplications.R;

public class DieselActivity extends AppCompatActivity {
    String slipNum;
    String imageFilePath = null;
    ImageView mImageView;
    boolean image = false;
    String quantity, amount;
    MainConstant c;
    EditText amountET, quantityET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.att_activity_diesel);

        amountET = findViewById(R.id.dieselAmountET);
        quantityET = findViewById(R.id.dieselQuantityET);
        mImageView = findViewById(R.id.imageView1);
        c = new MainConstant(this);
    }

    public void captureImage(View view) {
        openCameraIntent();
    }

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.d("myname", ex.getMessage());
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "in.visibleinfotech.viplfieldapplications.provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, 100);
            }
        }
    }

    private File createImageFile() throws IOException {

        String imageFileName = "Diesel_1920_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }

    public void submit(View view) {
        if (!image) {
            Toast.makeText(this, "Capture Image First", Toast.LENGTH_SHORT).show();
            return;

        }
        quantity = quantityET.getText().toString();
        amount = amountET.getText().toString();

        if (quantity.isEmpty()) {
            quantityET.setError("Enter Quantity first");
            quantityET.requestFocus();
            return;
        }
        if (amount.isEmpty()) {
            amountET.setError("Enter Amount first");
            amountET.requestFocus();
            return;
        }


        new InsertDieselEntry().execute();

    }

    public String convertImage(Bitmap image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        Log.d("myane", "" + byteArrayOutputStream.toByteArray());
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
//        return byteArrayOutputStream.toByteArray();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            image = true;
            Glide.with(this).load(imageFilePath).into(mImageView);
        } else {
            image = false;
            Glide.with(this).load(android.R.drawable.ic_menu_camera).into(mImageView);
        }
    }

    private class InsertDieselEntry extends AsyncTask<String, String, Integer> {
        String msg = "No Internet Connection";
        boolean flag = false, success = false;
        ProgressDialog progress;
        String encodingImage;
        File file;

        @SuppressLint("MissingPermission")
        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(DieselActivity.this);
            progress.setMessage("Loading..please wait.....");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.setMax(100);
            progress.show();
            file = persistImage((((BitmapDrawable) mImageView.getDrawable()).getBitmap()));
        }

        @Override
        protected Integer doInBackground(String... strings) {
            int i = 0;
            ConnectionStr conStr = new ConnectionStr();
            MainConstant c = new MainConstant(DieselActivity.this);
            try {
                if (c.getIp() != null) {
                    Connection connect = conStr.connectionclasss(c.getDbUser(), c.getDbPass(), c.getDbName(), c.getIp(), c.getInstance());
                    if (connect == null) {
                        flag = true;
                        msg = "Unable to connect to the Server";
                    } else {
                        Statement stmt = connect.createStatement();


                        String usr_id = c.geteCode();
                        String query = "insert into vipl_empdiesel (D_empcode,D_oilQty,D_amount) values" +
                                "('" + usr_id + "','" + quantity + "','" + amount + "')";
                        Log.d("myname", query);
                        i = stmt.executeUpdate(query);
                        msg = "Record updated";
                        success = true;
                        FTPClient ftp = new FTPClient();
                        MainConstant mainConstant = new MainConstant(DieselActivity.this);
                        ftp.connect(mainConstant.getFtpServer(), 21);
                        Log.d("myname_371", "Connected. Reply: " + ftp.getReplyString());
                        ftp.login(mainConstant.getFtpUser(), mainConstant.getFtpPass());
                        ftp.enterLocalPassiveMode(); // important!
                        ftp.setFileType(FTP.BINARY_FILE_TYPE);
                        FileInputStream in = new FileInputStream(file);
                        Log.d("myname", "uploading");
                        String imageName = usr_id + "_.jpg";
                        boolean result = ftp.storeFile(mainConstant.getFtpPath() + "diesel_" + imageName, in);
                        in.close();
                        ftp.logout();
                        ftp.disconnect();

                    }

                }
            } catch (Exception e) {
                Log.d("myname", e.toString());
                msg = e.getMessage();
            }
            return i;
        }

        @Override
        protected void onPostExecute(Integer i) {
            if (i > 0) {
                image = false;
                Glide.with(DieselActivity.this).load(android.R.drawable.ic_menu_camera).into(mImageView);
                Toast.makeText(DieselActivity.this, "Record Saved Successfully", Toast.LENGTH_SHORT).show();
                quantityET.setText("");
                amountET.setText("");
                File fdelete = new File(imageFilePath);
                if (fdelete.exists()) {
                    if (fdelete.delete()) {
                        System.out.println("file Deleted :" + imageFilePath);
                    } else {
                        System.out.println("file not Deleted :" + imageFilePath);
                    }
                }
            } else {
                Toast.makeText(DieselActivity.this, "Unable to mark attendance at this moment : " + msg, Toast.LENGTH_SHORT).show();
            }
            progress.dismiss();
        }
    }

    private File persistImage(Bitmap bitmap) {
        File filesDir = getFilesDir();
        File file = new File(filesDir, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()) + ".jpg");
        OutputStream os;
        try {
            os = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return file;
    }

}
