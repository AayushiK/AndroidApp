package pkg.doll;
import javax.microedition.khronos.egl.EGLConfig;  
import javax.microedition.khronos.opengles.GL10;  

import pkg.fire.R;
  
import android.app.Activity;  
import android.content.Context;  
import android.content.pm.ActivityInfo;  
import android.opengl.GLSurfaceView;  
import android.opengl.GLU;  
import android.os.Bundle;  
  
public class AndroidOgreMesh extends Activity  
{  
    private GLSurfaceView mGLView;  
  
    /** Called when the activity is first created. */  
    @Override  
    public void onCreate(Bundle savedInstanceState)  
    {  
        super.onCreate(savedInstanceState);  
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  
        mGLView = new ClearGLSurfaceView(this);  
        setContentView(mGLView);  
    }  
  
    @Override  
    protected void onPause()  
    {  
        super.onPause();  
        mGLView.onPause();  
    }  
  
    @Override  
    protected void onResume()  
    {  
        super.onResume();  
        mGLView.onResume();  
    }  
}  
  
class ClearGLSurfaceView extends GLSurfaceView  
{  
    ClearRenderer mRenderer;  
  
    public ClearGLSurfaceView(Context context)  
    {  
        super(context);  
        mRenderer = new ClearRenderer(context, this);  
        setRenderer(mRenderer);  
    }  
}  
  
class ClearRenderer implements GLSurfaceView.Renderer  
{  
    private ClearGLSurfaceView view;  
    private DrawModel model;  
    private float angleZ = 0f;  
  
    public ClearRenderer(Context context, ClearGLSurfaceView view)  
    {  
        this.view = view;  
        model = new DrawModel(context.getResources().getXml(R.xml.sphere));  
    }  
  
    public void onSurfaceCreated(GL10 gl, EGLConfig config)  
    {  
        gl.glLoadIdentity();  
        GLU.gluPerspective(gl, 25.0f, (view.getWidth() * 1f) / view.getHeight(), 1, 100);  
        GLU.gluLookAt(gl, 0f, -10f, 6f, 0.0f, 0.0f, 0f, 0.0f, 1.0f, 1.0f);  
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);  
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);  
        gl.glEnable(GL10.GL_DEPTH_TEST);  
    }  
  
    public void onSurfaceChanged(GL10 gl, int w, int h)  
    {  
        gl.glViewport(0, 0, w, h);  
    }  
  
    public void onDrawFrame(GL10 gl)  
    {  
        gl.glClearColor(0f, 0f, 0f, 1.0f);  
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);  
        gl.glPushMatrix();  
        gl.glRotatef(angleZ, 0f, 0f, 1f);  
        model.draw(gl);  
        gl.glPopMatrix();  
        angleZ += 0.4f;  
    }  
}  