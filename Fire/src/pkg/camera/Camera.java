package pkg.camera;

import pkg.fire.MainMenu;
import pkg.fire.PhotoMenu;
import pkg.fire.R;
import pkg.fire.R.layout;
import pkg.fire.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Camera extends Activity implements View.OnClickListener {

	Bitmap bmp;
	ImageView display;
	Button retakeBtn;
	Button doneBtn;
	final static int cameraData = 0;
	
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
		startActivityForResult(i, cameraData);
        
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
			cropIntent.putExtra("photo", bmp);
			startActivity(cropIntent);
			break;
		case R.id.retake_btn:
			Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(i, cameraData);
			break;
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			Bundle extras = data.getExtras();
			bmp = (Bitmap) extras.get("data");
			display.setImageBitmap(bmp);
			
		}
	}

}
