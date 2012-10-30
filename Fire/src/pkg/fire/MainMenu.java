package pkg.fire;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainMenu extends Activity  {

	Button start;
	Button settings;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        
        start = (Button) findViewById(R.id.startBtn);
        settings = (Button) findViewById(R.id.settingBtn);
        
        start.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent startingPt = new Intent(MainMenu.this, PhotoMenu.class);
    			startActivity(startingPt);
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.d(this.getClass().getName(), "back button pressed");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    @Override
    public void onBackPressed() {
       Log.d("CDA", "onBackPressed Called");
       finish();
    }

}
