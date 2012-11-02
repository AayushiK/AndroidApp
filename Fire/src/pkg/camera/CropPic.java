package pkg.camera;

import pkg.fire.R;
import pkg.fire.R.layout;
import pkg.fire.R.menu;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class CropPic extends Activity implements OnTouchListener{

	Panel sv;
	Button cropDone;
	Bitmap photo;
	Uri imgUri;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        setContentView(R.layout.crop_pic);
        LinearLayout layout = (LinearLayout) findViewById(R.id.cropViewLayout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        Panel sv = new Panel(this);
        sv.setOnTouchListener(this);
        layout.addView(sv, params);
       // setContentView(new Panel(this));
    
        /*  setContentView(R.layout.crop_pic);
        sv = (CropSurface) findViewById(R.id.cropView);
        cropDone = (Button) findViewById(R.id.cropDoneBtn);
        cropDone.setOnTouchListener(this);
    */    
        if(getIntent().getExtras()!=null){
        //	photo  = (Bitmap) getIntent().getParcelableExtra("photo");
        	imgUri = (Uri) getIntent().getParcelableExtra("photoUri");
        	
        	this.getContentResolver().notifyChange(imgUri, null);
		    ContentResolver cr = this.getContentResolver();
		    try{
		        photo = android.provider.MediaStore.Images.Media.getBitmap(cr, imgUri);
		        photo = Bitmap.createScaledBitmap(photo, photo.getWidth()/4, photo.getHeight()/4, false);
		    } catch (Exception e)
		    {
		        Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
		        Log.d("ImgURI", "Failed to load", e);
		    }
			
        }
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.crop_pic, menu);
        return true;
    }

	public boolean onTouch(View arg0, MotionEvent arg1) {
		return false;
	}
	
	class Panel extends View {
	    public Panel(Context context) {
	        super(context);
	    }
	 
	    @Override
	    public void onDraw(Canvas canvas) {
            canvas.drawBitmap(photo, 10, 10, null);
            //photo.get
	    }
	}
	
}
