package pkg.camera;

import pkg.fire.R;
import pkg.fire.R.layout;
import pkg.fire.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class CropPic extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_pic);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.crop_pic, menu);
        return true;
    }
}
