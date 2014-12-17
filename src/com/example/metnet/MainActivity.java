package com.example.metnet;

import glfont.GLFont;

import java.io.InputStream;
import java.lang.reflect.Field;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;

import com.threed.jpct.Camera;
import com.threed.jpct.CollisionEvent;
import com.threed.jpct.CollisionListener;
import com.threed.jpct.Config;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Interact2D;
import com.threed.jpct.Light;
import com.threed.jpct.Logger;
import com.threed.jpct.Object3D;
import com.threed.jpct.Polyline;
import com.threed.jpct.Primitives;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;
import com.threed.jpct.util.BitmapHelper;
import com.threed.jpct.util.MemoryHelper;
import com.zhr.synbio.R;

import database.DBManager;
import database.Init;

/**
 * A simple demo. This shows more how to use jPCT-AE than it shows how to write
 * a proper application for Android. It includes basic activity management to
 * handle pause and resume...
 * 
 * @author EgonOlsen
 * 
 */
public class MainActivity extends Activity {

	// Used to handle pause and resume...
	private static MainActivity master = null;

	private GLSurfaceView mGLView;
	private MyRenderer renderer = null;
	private FrameBuffer fb = null;
	private World world = null;
	private RGBColor back = new RGBColor(10,10,10,0);

	private float touchTurn = 0;
	private float touchTurnUp = 0;
	private boolean touched=false;
	private boolean longtouched=false;
	private double scale=0;
	private int mode=0;

	private Object3D sphere;
	
	private int fps = 0;
	private Camera cam = null;
	private Light sun = null;
	
	private float xpos = -1;
	private float ypos = -1;
	private int posx;
	private int posy;
	private float posx1=0;
	private float posy1=0;
	private float posx2=0;
	private float posy2=0;
	private double lastdis=0;
	private GLFont glFont;
	private Point point;
	private String title="Ecoli System";
	private long touchdowntime;
	private long touchuptime;
	private boolean Evertwo=false;
	private boolean Evermoved=false;
	
	protected void onCreate(Bundle savedInstanceState) {
		
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTypeface(Typeface.create((String)null, Typeface.BOLD));
		
		paint.setTextSize(40);
		glFont = new GLFont(paint);
		point=glFont.getStringBounds(title);
		/*SharedPreferences sharedPrefrences=this.getSharedPreferences("MetNet", 0);
		int firststart=sharedPrefrences.getInt("firststart", 0);
	    if(firststart==0)
	    {
			Init mInit=new Init(this);
			mInit.start();
			Editor editor=sharedPrefrences.edit();
			editor.putInt("firststart", 1);
			editor.commit();
	    }*/
		Logger.log("onCreate");
		if (master != null) {
			copy(master);
		}
		super.onCreate(savedInstanceState);
		mGLView = new GLSurfaceView(getApplication());
		mGLView.setEGLConfigChooser(new GLSurfaceView.EGLConfigChooser() {
			public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
				int[] attributes = new int[] { EGL10.EGL_DEPTH_SIZE, 16,
						EGL10.EGL_NONE };
				EGLConfig[] configs = new EGLConfig[1];
				int[] result = new int[1];
				egl.eglChooseConfig(display, attributes, configs, 1, result);
				return configs[0];
			}
		});
		renderer = new MyRenderer();
		mGLView.setRenderer(renderer);
		setContentView(mGLView);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mGLView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mGLView.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	private void copy(Object src) {
		try {
			Logger.log("Copying data from master Activity!");
			Field[] fs = src.getClass().getDeclaredFields();
			for (Field f : fs) {
				f.setAccessible(true);
				f.set(this, f.get(src));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public boolean onTouchEvent(MotionEvent me) {
		
		if (me.getAction() == MotionEvent.ACTION_DOWN)
		{
			lastdis=0;
		}
		if(me.getPointerCount()==1)
		{
			if (me.getAction() == MotionEvent.ACTION_DOWN) {
				Evertwo=false;
				Evermoved=false;
				touchdowntime=System.currentTimeMillis();
				xpos=me.getX();
				ypos=me.getY();
				posx=(int)me.getX();
				posy=(int)me.getY();		
			}
			if(me.getAction()==MotionEvent.ACTION_MOVE);
			{
				
				System.out.println("Action move");
				float dx=me.getX()-xpos;
				float dy=me.getY()-ypos;
				xpos=me.getX();
				ypos=me.getY();
				if(posy>15)
				{
				touchTurn=dx/100f;
				touchTurnUp=dy/100f;
				}
				
				if(Math.abs(dx)>5||Math.abs(dy)>5)
					Evermoved=true;
			}
			if(me.getAction()==MotionEvent.ACTION_UP)
			{
				float upposx=me.getX();
				float upposy=me.getY();
				if(posy<15&&Math.abs(posx-upposx)<20&&Math.abs(posy-upposy)>50)
				{
					Intent mIntent=new Intent(this,Activity_About.class);
					startActivity(mIntent);
					return true;
				}
				long deltime=System.currentTimeMillis()-touchdowntime;
				if(deltime>1000)
				{	
					if(Evertwo||Evermoved)
						return true;
					longtouched=true;
					touched=false;
				}
				else
				{
					
					if(Evertwo||Evermoved)
						return true;
					System.out.println("touched happen");
					touched=true;
					longtouched=false;
				}	
			}
			
		}
		if(me.getPointerCount()==2)
		{
			
			Evertwo=true;
			if (me.getAction() == MotionEvent.ACTION_MOVE) {
				posx1 = me.getX(0);
				posy1 = me.getY(0);
				posx2 = me.getX(1);
				posy2 = me.getY(1);
				double dis = Math.sqrt((posx1 - posx2) * (posx1 - posx2)
						+ (posy1 - posy2) * (posy1 - posy2));
				if (lastdis == 0) 
				{
					lastdis = dis;
				}
				else 
				{
					
					double delt = dis - lastdis;
					
					if (delt > 0) {
						mode = Camera.CAMERA_MOVEIN;
						scale = delt * 0.1;
					} else if (delt < 0) {
						mode = Camera.CAMERA_MOVEOUT;
						scale = - delt * 0.1;
					}
				}
				lastdis = dis;
			}
		}
		
		

		try {
			Thread.sleep(15);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;

	}

	/*protected boolean isFullscreenOpaque() {
		return true;
	}*/
	
	class lis implements CollisionListener
	{

		public void collision(CollisionEvent arg0) {
			Intent mIntent=new Intent(MainActivity.this,Activity_PathwayLayer.class);
			//Intent mIntent=new Intent(MainActivity.this,Activity_SearchPath.class);
			startActivity(mIntent);
		}

		public boolean requiresPolygonIDs() {
			// TODO Auto-generated method stub
			return false;
		}
		
	}
	
	
	class MyRenderer implements GLSurfaceView.Renderer {

		public MyRenderer() {
		}

		public void onSurfaceChanged(GL10 gl, int w, int h) {
			if (fb != null) {
				fb.dispose();
			}
			fb = new FrameBuffer(gl, w, h);
			
			
			if (master == null) {
				Config.farPlane=5000;
				world = new World();
				world.setAmbientLight(0, 0, 0);
				
				Texture texture = new Texture(BitmapHelper.rescale(BitmapHelper.convert(getResources().getDrawable(R.drawable.map2)), 64, 64));
				TextureManager.getInstance().addTexture("texture", texture);
				sphere=Primitives.getSphere(10);
				sphere.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
				sphere.addCollisionListener(new lis());
				sphere.calcTextureWrapSpherical();
				sphere.setTexture("texture");
				sphere.strip();
				sphere.build();
				world.addObject(sphere);

				sun = new Light(world);
				sun.setIntensity(400, 400, 400);
				
				cam = world.getCamera();
				cam.moveCamera(Camera.CAMERA_MOVEOUT, 50);
				cam.lookAt(new SimpleVector(0,0,0));
				world.setAmbientLight(255, 255, 255);

				SimpleVector sv = new SimpleVector();
				sv.set(new SimpleVector(0,0,0));
				sv.y -= 100;
				sv.z -= 100;
				sun.setPosition(cam.getPosition());
				MemoryHelper.compact();

				if (master == null) {
					Logger.log("Saving master Activity!");
					master = MainActivity.this;
				}
			}
		}

		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			
		}

		public void onDrawFrame(GL10 gl) {
			if(touched==true)
			{
				SimpleVector dir = new SimpleVector( Interact2D.reproject2D3DWS( world.getCamera(), fb, posx,posy)).normalize();
				world.calcMinDistance( world.getCamera().getPosition(),dir, 10000F);	
				touched=false;
			}
			if (touchTurn != 0&&mode==0) {
				sphere.rotateY(-touchTurn);
				touchTurn = 0;
			}

			if (touchTurnUp != 0&&mode==0) {
				sphere.rotateX(-touchTurnUp);
				touchTurnUp = 0;
			}
			if(mode!=0)
			{
			
				cam.moveCamera(mode, (float)scale);
				SimpleVector pos_cam=cam.getPosition();
				float dis=pos_cam.distance(new SimpleVector(0,0,0));
				if(dis<20)
					cam.moveCamera(Camera.CAMERA_MOVEOUT,  (float)scale);
				mode=0;
				scale=0;
			}
			fb.clear(back);
			world.renderScene(fb);
			world.draw(fb);
			glFont.blitString(fb, title, 
					(fb.getWidth()-point.x)/2,point.y, 10, RGBColor.WHITE);
			fb.display();
		}
	}
}
