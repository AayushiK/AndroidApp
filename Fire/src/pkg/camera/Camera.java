package pkg.camera;

import java.io.File;

import pkg.fire.MainMenu;
import pkg.fire.PhotoMenu;
import pkg.fire.R;
import pkg.fire.R.layout;
import pkg.fire.R.menu;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Camera extends Activity implements View.OnClickListener {

	Bitmap bmp;
	Uri imgUri;
	Uri mImageCaptureUri1;
	
	ImageView display;
	Button retakeBtn;
	Button doneBtn;
	
	
	final static int CAMERA_CAPTURE = 0;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);
        
        display = (ImageView) findViewById(R.id.photo_display);
        retakeBtn = (Button) findViewById(R.id.retake_btn);
        doneBtn = (Button) findViewById(R.id.done_btn);
        
        retakeBtn.setOnClickListener(this);
        doneBtn.setOnClickListener(this);
        
        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        
        File photo;
        try{
            // place where to store camera taken picture
            photo = this.createTemporaryFile("picture", ".jpg");
            photo.delete();
        }catch(Exception e){
            Log.v("CreateTempFile", "Can't create file to take picture!");
            Toast.makeText(this, "Please check SD card! Image shot is impossible!", Toast.LENGTH_LONG).show();
            return;
        }
        
        mImageCaptureUri1 = Uri.fromFile(photo);
        i.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri1);
        //start camera intent
        startActivityForResult(i, CAMERA_CAPTURE);
     
    }

    
    private File createTemporaryFile(String part, String ext) throws Exception{
        File tempDir= Environment.getExternalStorageDirectory();
        tempDir=new File(tempDir.getAbsolutePath()+"/.temp/");
        if(!tempDir.exists())
        {
            tempDir.mkdir();
        }
        return File.createTempFile(part, ext, tempDir);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.camera, menu);
        return true;
    }

	public void onClick(View v) {
		switch(v.getId()){
		case R.id.done_btn:
			Intent cropIntent = new Intent(Camera.this, CropPic.class);
			//cropIntent.putExtra("photo", bmp);
			cropIntent.putExtra("photoUri", mImageCaptureUri1);
			bmp.recycle();
			startActivity(cropIntent);
			break;
		case R.id.retake_btn:
			Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(i, CAMERA_CAPTURE);
			break;
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			this.getContentResolver().notifyChange(mImageCaptureUri1, null);
		    ContentResolver cr = this.getContentResolver();
		    try{
		        bmp = android.provider.MediaStore.Images.Media.getBitmap(cr, mImageCaptureUri1);
		        
		        final float scale = getResources().getDisplayMetrics().density;
		        int bmpH = (int) ((380 - 0.5f) / scale);
		        int bmpW = (int) ((float)bmp.getWidth() * ((float)bmpH/(float)bmp.getHeight()));   
		        
		        //check if the image needs to be rotated
		  
		        if(bmp.getWidth() > bmp.getHeight()){
		        	float scaleWH = (float) bmpH / bmp.getHeight();
		        	
		        	// create a matrix for the manipulation
		            Matrix matrix = new Matrix();
		            // resize the bit map
		            matrix.postScale(scaleWH, scaleWH);
		            // rotate the Bitmap
		            matrix.postRotate(90);
		            display.setImageBitmap(Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true));
		        }else{
			        display.setImageBitmap(Bitmap.createScaledBitmap(bmp, bmpW, bmpH, false));
		        }
		    } catch (Exception e){
		        Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
		        Log.d("ImgURI", "Failed to load", e);
		    }
			

		}
	}
	

}
