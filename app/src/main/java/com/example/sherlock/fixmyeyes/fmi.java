package com.example.sherlock.fixmyeyes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;

public class fmi extends AppCompatActivity {

    private Button open_btn;
    private ImageView open_eyes;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    private static final int PICK_IMAGE = 100;
    public String mCurrentPhotoPath;
    ProgressDialog dialog;
    Compressor compressor;
    private static SimpleDateFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fmi);
        dialog = new ProgressDialog(this);
        compressor = new Compressor(this);
        dialog.setTitle("Wait");
        dialog.setMessage("Getting things ready for you...");
        open_btn = findViewById(R.id.open_btn);
        open_eyes = findViewById(R.id.open_eyes);
        open_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_photo_selection_dialog();
            }
        });
    }

    private void send_pic(File file, String fb_id){
        dialog.show();
        Log.e("initial size",Long.toString(file.length()/1024));
        try{
            file = compressor.compressToFile(file);
            Log.e("Final size",Long.toString(file.length()/1024));
        }
        catch (IOException e){
            Toast.makeText(this, "Something unexpected happened", Toast.LENGTH_SHORT).show();
        }        
        Log.e("sendingpic",file.toString());
        final RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part fbody = MultipartBody.Part.createFormData("userfile", file.getName(), reqFile);
        RequestBody fbid = RequestBody.create(MediaType.parse("text/plain"), fb_id);
        df = new SimpleDateFormat( "yyyy-MM-dd'T'hh:mm:ssz");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        Log.e("CUR",df.format(new Date()));
        Call<response_POJO> callback =  retrofit_service.get_service().send(fbody,fbid);

        callback.enqueue(new Callback<response_POJO>() {
            @Override
            public void onResponse(Call<response_POJO> call, retrofit2.Response<response_POJO> response) {
                Log.e("CUR",df.format(new Date()));
                dialog.dismiss();
                try {
                    Log.e("Image url","http://35.196.121.21"+response.body().getMessage());
                    Picasso.with(getBaseContext()).load("http://35.196.121.21"+response.body().getMessage())
                            .placeholder(R.drawable.userdp)
                            .error(R.drawable.userdp).into(open_eyes);
                }
                catch (Exception e){
                    e.fillInStackTrace();
                }
            }

            @Override
            public void onFailure(Call<response_POJO> call, Throwable t) {
                Log.e("CUR",df.format(new Date()));
                dialog.dismiss();
                if(t instanceof SocketTimeoutException){
                    Log.e("CHECK","SOCKET TIME OUT");
                }

                if(t instanceof IOException){
                    Log.e("CHECK","IO EXCEPTION");
                }

                if(t instanceof NullPointerException){
                    Log.e("CHECK","NULL POINTER");
                }

                if(t instanceof Exception){
                    Log.e("CHECK","OTHERS");
                }

                Toast.makeText(fmi.this, "Link is broken, Check internet connectivity", Toast.LENGTH_SHORT).show();
                Log.e("CHECK",call.toString());
                Log.e("FAILURE", "RESPONSE");

            }

        });
    }

    public void create_photo_selection_dialog() {
        Log.e("CRETEED","PJHPIO SLECT");
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.dialog_for_photo_option_selection, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder.setView(promptsView);
        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        Button camera_btn = promptsView.findViewById(R.id.camera);
        Button gallery_btn =promptsView.findViewById(R.id.gallery);

        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                    dispatchTakePictureIntent();
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(getBaseContext(), "Camera hardware not available", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            }
        });
        alertDialog.show();
        gallery_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
                alertDialog.dismiss();
            }
        });
    }

    private void openGallery() {
        Intent gallery =
                new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                Log.e("photofile", photoFile.toString());
                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                        "com.example.sherlock.fixmyeyes.provider",
                        photoFile);
                mCurrentPhotoPath = photoFile.getAbsolutePath();
                List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    grantUriPermission(packageName, photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                Log.e("photoURI", photoURI.toString());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void setPic() {


        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        // Determine how much to scale down the image
        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        Log.e("SETTING PATH",mCurrentPhotoPath);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            galleryAddPic();
            setPic();
        } else if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri imageUri = data.getData();
            SharedPreferences mSharedPreferences = getSharedPreferences("mySharedPreferences",MODE_PRIVATE);
            File file = new File(getRealPathFromUri(this,imageUri));
            send_pic(file,mSharedPreferences.getString("USER_ID","987654312"));
          /*  new android.os.Handler().postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            SharedPreferences mSharedPreferences = getSharedPreferences("mySharedPreferences",MODE_PRIVATE);
                            Log.e("URL","http://35.196.121.21/edited_persist/"+mSharedPreferences.getString("USER_ID","")+".jpg");
                            Picasso.with(getBaseContext()).load("http://35.196.121.21/edited_persist/"+mSharedPreferences.getString("USER_ID","")+".jpg")
                                    .placeholder(R.drawable.userdp)
                                    .error(R.drawable.userdp).into(open_eyes);
                            Log.e("NOW","I am getting executed");
                        }
                    },60000
            );*/
        }
    }


    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        SharedPreferences mSharedPreferences = getSharedPreferences("mySharedPreferences",MODE_PRIVATE);
        send_pic(f,mSharedPreferences.getString("USER_ID",""));
        /*new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences mSharedPreferences = getSharedPreferences("mySharedPreferences",MODE_PRIVATE);
                        Log.e("URL","http://35.196.121.21/edited_persist/"+mSharedPreferences.getString("USER_ID","")+".jpg");
                        Picasso.with(getBaseContext()).load("http://35.196.121.21/edited_persist/"+mSharedPreferences.getString("USER_ID","")+".jpg")
                                .placeholder(R.drawable.userdp)
                                .error(R.drawable.userdp).into(open_eyes);
                        Log.e("NOW","I am getting executed");

                    }
                },60000
        );*/
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


}


