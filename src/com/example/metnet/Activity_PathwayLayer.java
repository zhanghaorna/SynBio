package com.example.metnet;

import glfont.GLFont;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Paint.FontMetricsInt;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
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

import database.DBManager;

/**
 * A simple demo. This shows more how to use jPCT-AE than it shows how to write
 * a proper application for Android. It includes basic activity management to
 * handle pause and resume...
 * 
 * @author EgonOlsen
 * 
 */
public class Activity_PathwayLayer extends Activity {

	// Used to handle pause and resume...
	private static Activity_PathwayLayer master = null;

	private GLSurfaceView mGLView;
	private MyRenderer renderer = null;
	private FrameBuffer fb = null;
	private World world = null;
	private RGBColor back = new RGBColor(0,0,0);

	private float touchTurn = 0;
	private float touchTurnUp = 0;
	private boolean touched=false;
	private boolean longtouched=false;
	private double scale=0;
	private int mode=0;
	
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
	private long touchdowntime;
	private PathWay selectedpathway=null;
	private GLFont glFont;
	private GLFont buttonFont;
	private GLFont titleFont;
	private Point point;
	private String title="Network by Pathway";
	private boolean Evertwo=false;
	private boolean Evermoved=false;
	
	private static final Rect[] BUTTON_BOUNDS = new Rect[2];
	
	private boolean Rotate=false;
	
	private Object3D dummy=null;
	public  List<PathWay> List_PathWay=new ArrayList<PathWay>();
	
	protected void onCreate(Bundle savedInstanceState) {

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTypeface(Typeface.create((String)null, Typeface.BOLD));
		
		paint.setTextSize(16);
		glFont = new GLFont(paint);
		
		paint.setTextSize(40);
		titleFont=new GLFont(paint);
		point=titleFont.getStringBounds(title);
		
		paint.setTextSize(50);
		buttonFont=new GLFont(paint);

		
		Logger.log("onCreate");

		if (master != null) {
			copy(master);
		}
		
		super.onCreate(savedInstanceState);
		mGLView = new GLSurfaceView(getApplication());
		mGLView.setEGLConfigChooser(new GLSurfaceView.EGLConfigChooser() {
			public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
				// Ensure that we get a 16bit framebuffer. Otherwise, we'll fall
				// back to Pixelflinger on some device (read: Samsung I7500)
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

	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 1, 1, "Search");
		menu.add(0,2,2,"Search Path");
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 1) {
			Intent mIntent=new Intent(this,Activity_Search.class);
			startActivity(mIntent);
		} 
		else if(item.getItemId()==2)
		{
			Intent mIntent=new Intent(this,Activity_SearchPath.class);
			startActivity(mIntent);
		}
		return true;
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
				touched=false;
				longtouched=false;
				touchdowntime=System.currentTimeMillis();
				//touched=true;
				xpos=me.getX();
				ypos=me.getY();
				posx=(int)me.getX();
				posy=(int)me.getY();		
			}
			if(me.getAction()==MotionEvent.ACTION_MOVE);
			{
				float dx=me.getX()-xpos;
				float dy=me.getY()-ypos;
				xpos=me.getX();
				ypos=me.getY();
				touchTurn=dx/100f;
				touchTurnUp=dy/100f;
				if(Math.abs(dx)>5||Math.abs(dy)>5)
					Evermoved=true;
			}
			if(me.getAction()==MotionEvent.ACTION_UP)
			{
				long deltime=System.currentTimeMillis()-touchdowntime;
				if(deltime>1000)
				{
					if(Evertwo==true||Evermoved)
						return true;
					longtouched=true;
					touched=false;
				
				}
				else
				{
					if(Evertwo==true||Evermoved)
						return true;
					touched=true;
					longtouched=false;
					if(BUTTON_BOUNDS[0].contains(posx,posy))
					{
						Rotate=!Rotate;
					}
					else if(BUTTON_BOUNDS[1].contains(posx,posy))
					{
						Intent mIntent=new Intent(this,Activity_TabHost.class);
						startActivity(mIntent);
					}
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

	protected boolean isFullscreenOpaque() {
		return true;
	}
	
	
	class MyRenderer implements GLSurfaceView.Renderer {

		private long time = System.currentTimeMillis();

		public MyRenderer() {
		}

		public void onSurfaceChanged(GL10 gl, int w, int h) {
			if (fb != null) {
				fb.dispose();
			}
			fb = new FrameBuffer(gl, w, h);
			BUTTON_BOUNDS[0] = new Rect(0, h-100, 100, h); 
			BUTTON_BOUNDS[1] = new Rect(w-100, h-100, w, h); 

			if (master == null) {
				Config.farPlane=5000;
				world = new World();
				world.setAmbientLight(0, 0, 0);
				dummy=Object3D.createDummyObj();
				world.addObject(dummy);
				DBManager db=new DBManager(Activity_PathwayLayer.this);
				try {
					List_PathWay=db.queryforpathway();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				for(int i=0;i<List_PathWay.size();i++)
				{
					Object3D pathway =List_PathWay.get(i);
					pathway.build();
					dummy.addChild(pathway);
					world.addObject(pathway);
					pathway.strip();
					System.out.println("world has load :"+i);
				}
				

				sun = new Light(world);
				sun.setIntensity(250, 250, 250);
				
				cam = world.getCamera();
				cam.moveCamera(Camera.CAMERA_MOVEOUT, 600);
				cam.lookAt(new SimpleVector(0,0,0));

				SimpleVector sv = new SimpleVector();
				sv.set(dummy.getTransformedCenter());
				sv.y -= 100;
				sv.z -= 100;
				sun.setPosition(sv);
				MemoryHelper.compact();

				if (master == null) {
					Logger.log("Saving master Activity!");
					master = Activity_PathwayLayer.this;
				}
			}
		}

		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			
		}

		public void onDrawFrame(GL10 gl) {
			
			if(touched==true)
			{
				SimpleVector dir = new SimpleVector( Interact2D.reproject2D3DWS( world.getCamera(), fb, posx,posy)).normalize();
				Object[] res=world.calcMinDistanceAndObject3D( world.getCamera().getPosition(),dir, 10000F);
				if(res[1]!=null)
				{
					PathWay res3d=(PathWay)res[1];
					Intent mIntent=new Intent(Activity_PathwayLayer.this,Activity_ReactionLayer.class);
					Bundle b=new Bundle();
					b.putInt("pid", res3d.getPid());
					b.putSerializable("pos", res3d.getPosition());
					b.putString("name", res3d.getName());
					mIntent.putExtras(b);
					startActivity(mIntent);
				}
				touched=false;
			}
			
			if (touchTurn != 0&&mode==0) {
				dummy.rotateY(-touchTurn);
				touchTurn = 0;
			}
			

			if (touchTurnUp != 0&&mode==0) {
				dummy.rotateX(-touchTurnUp);
				touchTurnUp = 0;
			}
			
			if(Rotate)
			{
				dummy.rotateY(0.02f);
			}
			
			
			if(mode!=0)
			{
				
				cam.moveCamera(mode, (float)scale*10);
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
			if(longtouched==true)
			{
				SimpleVector dir = new SimpleVector( Interact2D.reproject2D3DWS( world.getCamera(), fb, posx,posy)).normalize();
				Object[] res=world.calcMinDistanceAndObject3D( world.getCamera().getPosition(),dir, 10000F);
				if(res[1]!=null)
				{
					selectedpathway=(PathWay)res[1];
					String pname=selectedpathway.getName();
					glFont.blitString(fb, pname, 
							posx,posy, 10, RGBColor.WHITE);
				}
			}
			
			titleFont.blitString(fb, title, 
					(fb.getWidth()-point.x)/2,point.y, 10, RGBColor.WHITE);
			
			buttonFont.blitString(fb,"R",30,fb.getHeight()-40,10,RGBColor.WHITE);
			buttonFont.blitString(fb, "S", fb.getWidth()-50, fb.getHeight()-40, 10, RGBColor.WHITE);
			
			
			fb.display();
			
			
			if (System.currentTimeMillis() - time >= 1000) {
				Logger.log(fps + "fps");
				fps = 0;
				time = System.currentTimeMillis();
			}
			fps++;
		}
	}
}
