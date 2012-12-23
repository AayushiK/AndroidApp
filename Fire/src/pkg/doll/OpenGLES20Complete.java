/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pkg.doll;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import org.xmlpull.v1.XmlPullParserException;

import pkg.fire.R;
import android.app.Activity;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

public class OpenGLES20Complete extends Activity {

    private GLSurfaceView mGLView;
    private float triangleCoords[] = {};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity
        try{
        	XmlResourceParser xrp = getResources().getXml(R.xml.graphic);
        	
        	int count = 0;
        	while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT){  
                if (xrp.getEventType() == XmlResourceParser.START_TAG){  
                    String s = xrp.getName();  
                    if (s.equals("point")) {  
                       count ++;
                    }  
                } 
                xrp.next();  
            }  
            xrp.close();
            
            Log.i("count","COUNT: " + count);
            
            triangleCoords = new float[count--];
            xrp = getResources().getXml(R.xml.graphic);
            
            count = 0;
            while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT){  
                if (xrp.getEventType() == XmlResourceParser.START_TAG){  
                    String s = xrp.getName();  
                    if (s.equals("point")) {
                    	try{
                    		float x = xrp.getAttributeFloatValue(null, "value", 0);
                    		triangleCoords[count] = x;
                    	}catch(Exception e){
                    		float x = xrp.getAttributeIntValue(null, "value", 0);
                        	triangleCoords[count] = x;
                    	}
                    //	 Log.i("val","Value: " + triangleCoords[count]);
                    	count++;
                    }  
                } 
                xrp.next();  
            }  
            xrp.close();
            Log.i("count","COUNT2: " + count);
        }catch(Exception e){
        	Log.e("loadXML", e.toString());
        }
        
        
        /*
        try{
	    	InputStream is = getResources().openRawResource(R.raw.vertexdata1);
	    	BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    	Scanner scanFile = new Scanner(reader);
	    	
	    	float num;
	    	int count = 0;
	    	while (scanFile.hasNext()){
	    		scanFile.next();
	    		count++;
	    	}
	    	
	    	triangleCoords = new float[count--];
	    	count = 0;
			is.close();
			reader.close();
			
	    	InputStream is2 = getResources().openRawResource(R.raw.vertexdata1);
	    	BufferedReader reader2 = new BufferedReader(new InputStreamReader(is2));
	    	scanFile = new Scanner(reader2);
	    	while (scanFile.hasNext()){
	    		num = scanFile.nextFloat();
	    		triangleCoords[count++] = num;
	    	}
	    	is2.close();
	    	reader2.close();
        } catch (IOException e) {
			e.printStackTrace();
		}
        
      */
        Log.e("fasdf", "DONE");  
        mGLView = new MyGLSurfaceView(this, triangleCoords);
        setContentView(mGLView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        mGLView.onResume();
    }
}

class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer mRenderer;

    public MyGLSurfaceView(Context context, float triangleCoords[]) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);
        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new MyGLRenderer(triangleCoords);
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    private final float TOUCH_SCALE_FACTOR = 180.0f / 640;
    private float mPreviousX;
    private float mPreviousY;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                // reverse direction of rotation above the mid-line
                if (y > getHeight() / 2) {
                  dx = dx * -1 ;
                }

                // reverse direction of rotation to left of the mid-line
                if (x < getWidth() / 2) {
                  dy = dy * -1 ;
                }

                mRenderer.mAngle += (dx + dy) * TOUCH_SCALE_FACTOR;  // = 180.0f / 320
                requestRender();
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }
}
