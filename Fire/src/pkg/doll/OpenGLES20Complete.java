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

      /*  try{
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
        
        XmlResourceParser xrp = getResources().getXml(R.xml.colorcube);
        float[] coords = null;  
        float[] colcoords = null;  
        short[] icoords = null;  
        float[] ncoords = null;  
        int vertexIndex = 0;  
        int colorIndex = 0;  
        int faceIndex = 0;  
        int normalIndex = 0;  
        
        int faceCount = 0;  
        
        try {  
            while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT){  
                if (xrp.getEventType() == XmlResourceParser.START_TAG){  
                    String s = xrp.getName();  
                    if (s.equals("faces")) {  
                        int i = xrp.getAttributeIntValue(null, "count", 0);  
                        // now we know how many faces, so we know how large the  
                        // triangle array should be  
                        faceCount = i * 3; // three vertexes per face v1,v2,v3  
                        icoords = new short[faceCount];  
                    }  
                    if (s.equals("geometry")){  
                        int i = xrp.getAttributeIntValue(null, "vertexcount", 0);  
                        // now we know how many vertexes, so we know how large  
                        // the vertex, normal, texture and color arrays should be  
                        // three vertex attributes per vertex x,y,z  
                        coords = new float[i * 3];  
                        // three normal attributes per vertex x,y,z  
                        ncoords = new float[i * 3];  
                        // four color attributes per vertex r,g,b,a  
                        colcoords = new float[i * 4];  
                    }  
                    if (s.equals("position")) { 
                    	
                        float x = xrp.getAttributeFloatValue(null, "x", 0);  
                        float y = xrp.getAttributeFloatValue(null, "y", 0);  
                        float z = xrp.getAttributeFloatValue(null, "z", 0);  
                        if (coords != null)  {  
                            coords[vertexIndex++] = x;  
                            coords[vertexIndex++] = y;  
                            coords[vertexIndex++] = z;  
                        }  
                    }  
                    if (s.equals("normal")) {  
                        float x = xrp.getAttributeFloatValue(null, "x", 0);  
                        float y = xrp.getAttributeFloatValue(null, "y", 0);  
                        float z = xrp.getAttributeFloatValue(null, "z", 0);  
                        if (ncoords != null) {  
                            ncoords[normalIndex++] = x;  
                            ncoords[normalIndex++] = y;  
                            ncoords[normalIndex++] = z;  
                        }  
                    }  
                    if (s.equals("face")){  
                        short v1 = (short) xrp.getAttributeIntValue(null, "v1", 0);  
                        short v2 = (short) xrp.getAttributeIntValue(null, "v2", 0);  
                        short v3 = (short) xrp.getAttributeIntValue(null, "v3", 0);  
                        if (icoords != null)  {  
                            icoords[faceIndex++] = v1;  
                            icoords[faceIndex++] = v2;  
                            icoords[faceIndex++] = v3;  
                        }  
                    }  
                    if (s.equals("colour_diffuse"))  
                    {  
                        String colorVal = (String) xrp.getAttributeValue(null, "value");  
                        String[] colorVals = colorVal.split(" ");  
                        colcoords[colorIndex++] = Float.parseFloat(colorVals[0]);  
                        colcoords[colorIndex++] = Float.parseFloat(colorVals[1]);  
                        colcoords[colorIndex++] = Float.parseFloat(colorVals[2]);  
                        colcoords[colorIndex++] = 1f;  
                    }  
                }  
                else if (xrp.getEventType() == XmlResourceParser.END_TAG) {  
                    ;  
                }  
                else if (xrp.getEventType() == XmlResourceParser.TEXT)  
                {  
                    ;  
                }  
                xrp.next();  
            }  
            xrp.close();  
        }  
        catch (XmlPullParserException xppe)  {  
            Log.e("TAG", "Failure of .getEventType or .next, probably bad file format");  
            xppe.toString();  
        }  
        catch (IOException ioe){  
            Log.e("TAG", "Unable to read resource file");  
            ioe.printStackTrace();  
        } catch(Exception e){
        	 Log.e("TAG", "Unable to read resource file");  
             e.printStackTrace(); 
        }
        
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
