package pkg.fire;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.Menu;
import android.widget.ImageView;

public class BurnDoll extends Activity {
	
	ImageView display;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.burn_doll);
        display = (ImageView) findViewById(R.id.picDisplay);
       
        if(getIntent().getExtras()!=null){
        	Bitmap pic = (Bitmap)getIntent().getParcelableExtra("picture");
        	display.setImageBitmap(pic);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.burn_doll, menu);
        return true;
    }
}
