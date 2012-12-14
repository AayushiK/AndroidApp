package pkg.fire;

import pkg.camera.Camera;
import pkg.camera.CropPic;
import pkg.doll.AndroidOgreMesh;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PhotoMenu extends Activity implements OnClickListener{

	Button defaultBtn;
	Button cameraBtn;
	Button albumBtn;
	Button returnBtn;
	
	//Result
	final static int PICK_IMAGE = 0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_menu);
        
        defaultBtn = (Button) findViewById(R.id.DefaultButton);
        cameraBtn = (Button) findViewById(R.id.CameraButton);
        albumBtn = (Button) findViewById(R.id.AlbumButton);
        returnBtn = (Button) findViewById(R.id.BackButton);
        
        defaultBtn.setOnClickListener(this);
        cameraBtn.setOnClickListener(this);
        albumBtn.setOnClickListener(this);
        returnBtn.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.photo_menu, menu);
        return true;
    }

	public void onClick(View v) {
		switch(v.getId()){
		case R.id.AlbumButton:
			Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, 0);
			break;
		case R.id.BackButton:
			Intent i = new Intent(PhotoMenu.this, MainMenu.class);
			startActivity(i);
			break;
		case R.id.CameraButton:
			Intent cam = new Intent(PhotoMenu.this, Camera.class);
			startActivity(cam);
			break;
		case R.id.DefaultButton:
			Intent defaultI = new Intent(PhotoMenu.this, AndroidOgreMesh.class);
			startActivity(defaultI);
			break;
		
		}
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == PICK_IMAGE && resultCode == RESULT_OK){
			Uri targetUri = data.getData();
			Intent cropIntent = new Intent(PhotoMenu.this, CropPic.class);
			cropIntent.putExtra("photoUri", targetUri);
			startActivity(cropIntent);
		}
		
	}
	
	
	
}
