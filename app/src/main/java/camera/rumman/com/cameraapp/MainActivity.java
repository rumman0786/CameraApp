package camera.rumman.com.cameraapp;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;


public class MainActivity extends Activity {

    private static String logTag = "CameraApp" ;
    private static int TAKE_PICTURE = 1 ;
    private Uri imageUri;
    public int count = 1 ;
    private ImageView imageView ;
    public static final String CameraPREFERENCES = "CameraPrefs" ;
    public static final String CountKey = "countKey";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
        setContentView(R.layout.activity_main);
    }

    private void initialize() {
        imageView = (ImageView) findViewById(R.id.img_camera);
        sharedPreferences = getSharedPreferences(CameraPREFERENCES , Context.MODE_PRIVATE);
        if(sharedPreferences.contains(CountKey)){
            count = sharedPreferences.getInt(CountKey, 0);
        }
    }

    public void takePicture(View view) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File imageFolder = new File(Environment.getExternalStorageDirectory(), "CameraApp");
//        File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "img_"+count+".jpg");
        imageFolder.mkdirs();
        File photo = new File(imageFolder, "img_"+count+".jpg");
        count++;
        Editor editor = sharedPreferences.edit();
        editor.putInt(CountKey , count);
        editor.commit();
        imageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent,TAKE_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent intent){
        super.onActivityResult(requestCode,resultCode,intent);

        if (resultCode == Activity.RESULT_OK){
            Uri selectedUri = imageUri ;
            getContentResolver().notifyChange(imageUri, null);

            ContentResolver contentResolver = getContentResolver();
            Bitmap bitmap;
            try{
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedUri);
                imageView.setImageBitmap(bitmap);
                Toast.makeText(this,"Capture Successfull",Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Log.wtf(logTag ,"Error occured");
            }
        }
    }
}

