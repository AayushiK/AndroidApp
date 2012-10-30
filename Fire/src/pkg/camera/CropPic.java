package pkg.camera;

import pkg.fire.R;
import pkg.fire.R.layout;
import pkg.fire.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class CropPic extends Activity implements OnTouchListener{

	SurfaceView sv;
	Button cropDone;
	Bitmap photo;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_pic);
        
        sv = (SurfaceView) findViewById(R.id.cropView);
        cropDone = (Button) findViewById(R.id.cropDoneBtn);
        cropDone.setOnTouchListener(this);
        
        if(getIntent().getExtras()!=null){
        	photo  = (Bitmap) getIntent().getParcelableExtra("photo");
        }
 /*       
        Canvas canvas = sv.getHolder().lockCanvas();
        canvas.drawBitmap(photo, 0, 0, null);
        sv.getHolder().unlockCanvasAndPost(canvas);*/
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.crop_pic, menu);
        return true;
    }

	public boolean onTouch(View arg0, MotionEvent arg1) {
		return false;
	}
}
