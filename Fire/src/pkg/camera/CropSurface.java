package pkg.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CropSurface extends SurfaceView implements SurfaceHolder.Callback{

	SurfaceHolder ourHolder;
	Thread ourThread = null;
	boolean isRunning = false;
	
	Bitmap pic;
	
	public CropSurface(Context context, Bitmap pic) {
		super(context);
		this.pic = pic;
	}
/*
	public void run() {
		while(isRunning){
			if(!ourHolder.getSurface().isValid())
				continue;
			Canvas canvas = ourHolder.lockCanvas();
			canvas.drawRGB(194, 224, 255);

			canvas.drawBitmap(pic, 20, 20, null);

			ourHolder.unlockCanvasAndPost(canvas);
		}
		
	}*/

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

}
