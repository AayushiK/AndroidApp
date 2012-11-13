package pkg.camera;

import pkg.fire.BurnDoll;
import pkg.fire.R;
import pkg.fire.R.layout;
import pkg.fire.R.menu;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class CropPic extends Activity implements OnTouchListener{

	Panel sv;
	Button cropDone;
	ImageButton incCrop, decCrop;
	Bitmap photo;
	Uri imgUri;
	
	private float scale;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        setContentView(R.layout.crop_pic);
        scale = getResources().getDisplayMetrics().density;
        
        LinearLayout layout = (LinearLayout) findViewById(R.id.cropViewLayout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
       
          
        if(getIntent().getExtras()!=null){
        //	photo  = (Bitmap) getIntent().getParcelableExtra("photo");
        	imgUri = (Uri) getIntent().getParcelableExtra("photoUri");
        	
        	this.getContentResolver().notifyChange(imgUri, null);
		    ContentResolver cr = this.getContentResolver();
		    try{
		        photo = android.provider.MediaStore.Images.Media.getBitmap(cr, imgUri);
		        
		        if(photo.getWidth() > photo.getHeight()){
		        	int bmpW = (int) (390 * scale + 0.5f);
			        int bmpH = (int) ((float)photo.getHeight() * (float)((float)bmpW/(float)photo.getWidth()));
		        	float scaleWH = (float) bmpH / photo.getHeight();
		        	
		        	// create a matrix for the manipulation
		            Matrix matrix = new Matrix();
		            // resize the bit map
		            matrix.postScale(scaleWH, scaleWH);
		            // rotate the Bitmap
		            matrix.postRotate(90);
		            photo = Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight(), matrix, true);
		        }else{
		        	int bmpH = (int) (390 * scale + 0.5f);
			        int bmpW = (int) ((float)photo.getWidth() * (float)((float)bmpH/(float)photo.getHeight()));
		        	photo = Bitmap.createScaledBitmap(photo, bmpW, bmpH, false); 
		        }
		    } catch (Exception e) {
		        Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
		        Log.d("ImgURI", "Failed to load", e);
		    }
			
        }
        
        sv = new Panel(this);
        sv.setOnTouchListener(this);
        layout.addView(sv, params);
        
        cropDone = (Button) findViewById(R.id.cropDoneBtn);
        cropDone.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Bitmap croppedImage = Bitmap.createBitmap(photo, 
						sv.getCropX1(), sv.getCropY1(), 
						sv.getCropX2()-sv.getCropX1(), sv.getCropY2()-sv.getCropY1());
				Intent i = new Intent(CropPic.this, BurnDoll.class);
				i.putExtra("picture", croppedImage);
				startActivity(i);
				finish();
			}
		});
        
        //set crop size adjusting buttons
        incCrop = (ImageButton) findViewById(R.id.rzBiggerBtn);
        decCrop = (ImageButton) findViewById(R.id.rzSmallerBtn);
        incCrop.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				sv.incCropSize();
			}
		});
        decCrop.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				sv.decCropSize();
			}
		});
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.crop_pic, menu);
        return true;
    }

	public boolean onTouch(View arg0, MotionEvent arg1) {
		return false;
	}
	
	public int dpToPixel(int dp){
		return (int) (dp * scale + 0.5f);
	}
	
	/*** PANEL CLASS ***/
	
	class Panel extends SurfaceView implements SurfaceHolder.Callback {
		
		PanelThread thread;
		Paint paint = new Paint();
		
		private int cropX1, cropX2, cropY1, cropY2 = 0;
		private int cropSize = 100;
		
	    public Panel(Context context) {
	        super(context);
	        getHolder().addCallback(this);
	        thread = new PanelThread(getHolder(), this);
	        
	        cropX1 = dpToPixel(photo.getHeight()/2 - dpToPixel(cropSize)/2);
	        cropX2 = cropX1 + dpToPixel(cropSize);
	        cropY1 = dpToPixel(photo.getWidth()/2 - dpToPixel(cropSize)/2);
	        cropY2 = cropY1 + dpToPixel(cropSize);
	    }
	 
	    @Override
	    public void onDraw(Canvas canvas) {
	    	//"clear" canvas
	    	canvas.drawColor(Color.BLACK);
	    	
            canvas.drawBitmap(photo, 0, 0, null);
           
            paint.setColor(Color.YELLOW);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(3);
            canvas.drawRect(cropX1, cropY1, cropX2, cropY2, paint);
         
	    }
	    
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			
			int eX = (int) event.getX();
			int eY = (int) event.getY();
			
			//moves the square and bounce it back from the boundary
			boundaryBounce(eX, eY);
			
			cropX2 = cropX1 + dpToPixel(cropSize);
			cropY2 = cropY1 + dpToPixel(cropSize);
			
			return true;
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			// TODO Auto-generated method stub
			
		}

		public void surfaceCreated(SurfaceHolder holder) {
			thread.setRunning(true);
			thread.start();
			
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			boolean retry = true;
		    thread.setRunning(false);
		    while (retry) {
		        try {
		            thread.join();
		            retry = false;
		        } catch (InterruptedException e) {
		            e.printStackTrace();		        }
		    }
		}

		public int getCropX1() {
			return cropX1;
		}

		public void setCropX1(int cropX1) {
			this.cropX1 = cropX1;
		}

		public int getCropX2() {
			return cropX2;
		}

		public void setCropX2(int cropX2) {
			this.cropX2 = cropX2;
		}

		public int getCropY1() {
			return cropY1;
		}

		public void setCropY1(int cropY1) {
			this.cropY1 = cropY1;
		}

		public int getCropY2() {
			return cropY2;
		}

		public void setCropY2(int cropY2) {
			this.cropY2 = cropY2;
		}
		
		public int getCropSize(){
			return cropSize;
		}
		
		public void setCropSize(int sz){
			this.cropSize = sz;
		}
		
		public void incCropSize(){
			if(cropSize + dpToPixel(10) < photo.getWidth() && cropSize + dpToPixel(10) < photo.getHeight()){
				cropSize += dpToPixel(10);
				
				boundaryBounce(cropX1 + cropSize/2, cropY1 + cropSize/2);
				cropX2 = cropX1 + cropSize;
				cropY2 = cropY1 + cropSize;
			}
		}
		
		public void decCropSize(){
			if(cropSize - dpToPixel(10) > dpToPixel(50)){
				cropSize -= dpToPixel(10);
				cropX2 = cropX1 + cropSize;
				cropY2 = cropY1 + cropSize;
			}
		}
		
		public void boundaryBounce(int eX, int eY){
			//check if the touch was out of the picture
			if((eX - (cropSize/2)) < 0){
				cropX1 = 0;
			}else if(eX + (cropSize/2) > photo.getWidth()){
				cropX1 = photo.getWidth() - cropSize;
			}else{
				cropX1 = eX - (cropSize/2);
			}
			
			if(eY - (cropSize/2) < 0){
				cropY1 = 0;
			}else if(eY + (cropSize/2) > photo.getHeight()){
				cropY1 = photo.getHeight() - cropSize;
			}else{
				cropY1 = eY - (cropSize/2);
			}
		}
		
	}
	
	/** PanelThread Class **/
	
	class PanelThread extends Thread {
	    private SurfaceHolder _surfaceHolder;
	    private Panel _panel;
	    private boolean _run = false;
	 
	    public PanelThread(SurfaceHolder surfaceHolder, Panel panel) {
	        _surfaceHolder = surfaceHolder;
	        _panel = panel;
	    }
	 
	    public void setRunning(boolean run) {
	        _run = run;
	    }
	 
	    @Override
	    public void run() {
	    	Canvas c;
	        while (_run) {
	            c = null;
	            try {
	                c = _surfaceHolder.lockCanvas(null);
	                synchronized (_surfaceHolder) {
	                    _panel.onDraw(c);
	                }
	            } finally {
	                // do this in a finally so that if an exception is thrown
	                // during the above, we don't leave the Surface in an
	                // inconsistent state
	                if (c != null) {
	                    _surfaceHolder.unlockCanvasAndPost(c);
	                }
	            }
	        }
	    }
	}
	
}
