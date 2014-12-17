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
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
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
import database.Edge;

/**
 * A simple demo. This shows more how to use jPCT-AE than it shows how to write
 * a proper application for Android. It includes basic activity management to
 * handle pause and resume...
 * 
 * @author EgonOlsen
 * 
 */
public class Activity_ReactionLayer extends Activity {

	
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
	
	private Object3D dummy=null;
	private List<Reaction> List_Reaction=new ArrayList<Reaction>();
	private List<Compound> List_Compound=new ArrayList<Compound>();
	private int pid;
	private SimpleVector superpos;
	private GLFont glFont;
	private GLFont glFont_pathwayname;
	private GLFont titleFont;
	private Point point;
	private Point point_pathwayname;
	private String title="Pathway";
	private String pathwayname;
	private String preActivity="";
	private List<List<Edge>> paths=null;
	private boolean Evertwo=false;
	private boolean Evermoved=false;
	
	protected void onCreate(Bundle savedInstanceState) {
		
		Intent mIntent=this.getIntent();
		Bundle b=mIntent.getExtras();
		pathwayname=b.getString("name");
		
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTypeface(Typeface.create((String)null, Typeface.BOLD));
		
		paint.setTextSize(16);
		glFont = new GLFont(paint);
		
		paint.setTextSize(40);
		titleFont=new GLFont(paint);
		
	    paint.setTextSize(16);
	    glFont_pathwayname=new GLFont(paint);
		point=titleFont.getStringBounds(title);
		point_pathwayname=glFont_pathwayname.getStringBounds(pathwayname);

		Logger.log("onCreate");
		
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
		
		pid=b.getInt("pid");
		superpos=(SimpleVector)b.getSerializable("pos");
		superpos.x*=2;
		superpos.y*=2;
		superpos.z*=2;
		preActivity=b.getString("Activity");
		paths=(List<List<Edge>>)b.getSerializable("paths");
		
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


	public boolean onTouchEvent(MotionEvent me) {
		
		if (me.getAction() == MotionEvent.ACTION_DOWN)
		{
			lastdis=0;
		}
		
		if(me.getPointerCount()==1)
		{
			if (me.getAction() == MotionEvent.ACTION_DOWN) {
				Evermoved=false;
				Evertwo=false;
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
					if(Evertwo||Evermoved)
						return true;
					longtouched=true;
					touched=false;
				}
				else
				{
					if(Evertwo||Evermoved)
						return true;
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
			    Config.farPlane=5000;
				world = new World();
				world.setAmbientLight(0, 0, 0);
				dummy=Object3D.createDummyObj();
				dummy.setOrigin(superpos);
				System.out.println("set dummy pos :"+superpos.toString());
				world.addObject(dummy);
				DBManager db=new DBManager(Activity_ReactionLayer.this);
				try {
					List_Compound=db.queryforvertex(pid);
					List_Reaction=db.queryforedge(pid, List_Compound);
					System.out.println("tga1");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(preActivity!=null&&preActivity.equals("Activity_SearchPath"))
				{
					System.out.println("tga2");
					RGBColor gray=new RGBColor(128,128,128);
					for(Compound c:List_Compound)
					{
						int flag=0;
						for(Edge e:paths.get(0))
						{
							if(e.sindex==c.getIndex()||e.eindex==c.getIndex())
							{
								flag=1;
								break;
							}
						}
						if(flag==1)
						{
							c.setAdditionalColor(RGBColor.RED);
						}
						else
						{
							c.setAdditionalColor(gray);
						}
						
					}
					for(Reaction R:List_Reaction)
					{
						int flag=0;
						for(Edge e:paths.get(0))
						{
							if(e.index==R.getIndex())
							{
								flag=1;
								break;
							}
						}
						if(flag==1)
						{
							R.setAdditionalColor(RGBColor.RED);
						}
						else
						{
							R.setAdditionalColor(gray);
						}
						
					}
					
				}
				for(int i=0;i<List_Compound.size();i++)
				{
					Object3D compound =List_Compound.get(i);
					compound.build();
					dummy.addChild(compound);
					world.addObject(compound);
					compound.strip();
				}
				for(int i=0;i<List_Reaction.size();i++)
				{
					Object3D reacton=List_Reaction.get(i);
					reacton.build();
					dummy.addChild(reacton);
					world.addObject(reacton);
					reacton.strip();
				}
				
				sun = new Light(world);
				sun.setIntensity(250, 250, 250);
				
				cam = world.getCamera();
				cam.setPosition(superpos);
				cam.moveCamera(Camera.CAMERA_MOVEOUT, 400);
				cam.lookAt(superpos);
				

				SimpleVector sv = new SimpleVector();
				sv.set(superpos);
				sv.y -= 100;
				sv.z -= 100;
				sun.setPosition(sv);
				MemoryHelper.compact();

			}


		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			// TODO Auto-generated method stub
			
		}

	

		public void onDrawFrame(GL10 gl) {
			if(touched==true)
			{
				SimpleVector dir = new SimpleVector( Interact2D.reproject2D3DWS( world.getCamera(), fb, posx,posy)).normalize();
				Object[] res=world.calcMinDistanceAndObject3D( world.getCamera().getPosition(),dir, 10000F);
				if(res[1]!=null&&(res[1] instanceof Compound))
				{
					Compound mCompound=(Compound)res[1];
					Intent mIntent=new Intent(Activity_ReactionLayer.this, Activity_Structure.class);
					mIntent.putExtra("uid", mCompound.getUID());
					mIntent.putExtra("name", mCompound.getName());
				
					startActivity(mIntent);
					
				}
				else if(res[1]!=null&&(res[1] instanceof Reaction))
				{
					Reaction r=(Reaction)res[1];
					System.out.println("click on edge and ecnumber is :"+r.ecnumber);
					Intent intent = new Intent();
			        intent.setAction("android.intent.action.VIEW");
			        Uri content_url = Uri.parse("http://enzyme.expasy.org/EC/"+r.ecnumber);
			        intent.setData(content_url);
			        startActivity(intent);
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
			if(mode!=0)
			{
				cam.moveCamera(mode, (float)scale*10);
				SimpleVector pos_cam=cam.getPosition();
				float dis=pos_cam.distance(superpos);
				if(dis<100)
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
					Compound mCompound=(Compound)res[1];
					String cname=mCompound.getName();
					glFont.blitString(fb, cname, 
							posx,posy, 10, RGBColor.WHITE);
				}
				
			}
			titleFont.blitString(fb, title, 
					(fb.getWidth()-point.x)/2,point.y, 10, RGBColor.WHITE);
			glFont_pathwayname.blitString(fb, pathwayname, (fb.getWidth()-point_pathwayname.x)/2,point.y+point_pathwayname.y+10, 10, RGBColor.WHITE);
			
			fb.display();
			
		}
	}
}
