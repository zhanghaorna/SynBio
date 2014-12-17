package com.example.metnet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

import com.threed.jpct.SimpleVector;
import com.zhr.synbio.R;

import database.DBManager;
import database.Edge;
import database.Pathway;
import database.Vertex;
import android.app.Activity;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class Activity_SearchPath extends Activity{

	private List<Vertex> compound_index_from=null;//有Name得到的index集合
	private List<Vertex> compound_index_to=null;
	
	private List<String>compound=null;//所有化合物的name
	private List<String>compound_to=null;
	
	private String CName_From;
	private String CName_To;
	
	private List<List<Edge>> paths=null;//完整的路径信息
	private List<Integer> paths_pid=null;//路径所在的pid
	
	private List<Edge>edges=null;
	private List<Edge>path=null;
	
	
	private Spinner mSpinner_from=null;
	private Spinner mSpinner_to=null;
	private Button Button_Search=null;
	private ListView listview_result=null;
	private TextView noresultmatched=null;
	
	private boolean find=false;
	
	private int lis_to_flag=-1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_searchpath);
		Init();	
		mSpinner_from=(Spinner)findViewById(R.id.layout_searchpath_spinner_from);
		mSpinner_to=(Spinner)findViewById(R.id.layout_searchpath_spinner_to);
		Button_Search=(Button)findViewById(R.id.layout_searchpath_button_search);
		listview_result=(ListView)findViewById(R.id.layout_searchpath_listview_result);
		noresultmatched=(TextView)findViewById(R.id.layout_searchpath_textview_noresultmatched);
		listview_result.setVisibility(0);
		mSpinner_from.setAdapter(new Adapter_List(compound));
		mSpinner_to.setAdapter(new Adapter_List(compound));
		mSpinner_from.setOnItemSelectedListener(new lis_from());
		mSpinner_to.setOnItemSelectedListener(new lis_to());
		Button_Search.setOnClickListener(new lis_serach());
		
	}
	
	private void Init()
	{
		DBManager db=new DBManager(this);
		compound=db.QueryForCompoundName();
		db.closeDB();
		Collections.sort(compound, new sortbyname());
	}
	
	private void Search()
	{
		/*CName_From="RstB";
		CName_To="RstA-P<SUP>asp52</SUP>";*/
		paths_pid=null;
		if(CName_From==null)
			CName_From=compound.get(0);
		if(CName_To==null)
			CName_To=compound.get(0);
		if(CName_From==CName_To)
			return;
		paths_pid=new ArrayList<Integer>();
		DBManager db=new DBManager(this);
		compound_index_from=db.QueryForCompoundbyName(CName_From);
		compound_index_to=db.QueryForCompoundbyName(CName_To);
		db.closeDB();
		for(int i=0;i<compound_index_from.size();i++)
		{
			for(int j=0;j<compound_index_to.size();j++)
			{
				if(compound_index_from.get(i).pid==compound_index_to.get(j).pid)//两个化合物在同一个pathway中
				{
					paths_pid.add(compound_index_from.get(i).pid);
				}
			}
		}
	}
	
	
	private void searchpath_init(int pid,int index_from,int index_to)
	{
		paths=new ArrayList<List<Edge>>();
        DBManager db=new DBManager(this);
        edges=db.QueryForEdgesbypid(pid);
        db.closeDB();
        path=new ArrayList<Edge>();
        find=false;
        searchpath(index_from, index_to);
	}
	
	private void searchpath(int start,int end)
	{
		if(find)
			return;
		System.out.println("search start index:"+start+"  end index:"+end);
		if(start==end)
		{
			List<Edge>result=new ArrayList(path);
			paths.add(result);
			find=true;
			return;
		}
		for(Edge edge:edges)
		{
			if(find)
				return;
			if(edge.sindex==start)
			{
				int flag=0;
				for(Edge edgeinpath:path)
				{
					if(edgeinpath.sindex==edge.eindex||edgeinpath.eindex==edge.eindex)
					{
						flag=1;
						break;
					}
						
				}
				if(flag==1)
					continue;
				path.add(edge);
				searchpath(edge.eindex,end);
				path.remove(edge);
				
			}
			else if(edge.eindex==start)
			{
				int flag=0;
				for(Edge edgeinpath:path)
				{
					if(edgeinpath.sindex==edge.sindex||edgeinpath.eindex==edge.sindex)
					{
						flag=1;
						break;
					}
				}
				if(flag==1)
					continue;
				Edge e=new Edge();
				e.sindex=edge.eindex;
				e.eindex=edge.sindex;
				e.index=edge.index;
				path.add(e);
				searchpath(edge.sindex, end);
				path.remove(e);
			}
		}
	}
	
	class lis_from implements OnItemSelectedListener
	{
    
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			lis_to_flag++;
			System.out.println("lis_from OnItemSelectedListener happen");
			TextView text=(TextView)arg1;
		    CName_From=(String) text.getText();
		    System.out.println("CName_from: "+CName_From);
			DBManager db=new DBManager(Activity_SearchPath.this);
			compound_to=db.QueryForCompoundName_respectly(CName_From);
			db.closeDB();
			if(compound_to.size()==0||compound_to==null)
			{
				compound_to=new ArrayList<String>();
				compound_to.add("null");
			}
			Adapter_List mAdapter_List=new Adapter_List(compound_to);
			mSpinner_to.setAdapter(mAdapter_List);
			//mAdapter_List.notifyDataSetChanged();
			System.out.println("lis_from OnItemSelectedListener finish");
			mSpinner_to.setOnItemSelectedListener(new lis_to());
			// TODO Auto-generated method stub
			
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class lis_to implements OnItemSelectedListener
	{

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if(arg1==null)
				return;
			System.out.println("lis_to OnItemSelectedListener happen");
			TextView text=(TextView)arg1;
		    CName_To=(String) text.getText();
		    System.out.println("CName_To: "+CName_To);
			
			// TODO Auto-generated method stub
			
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class lis_res implements OnItemClickListener
	{

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			int pid=paths_pid.get(arg2);
			int index_from = 0;
			int index_to = 0;
			for(Vertex v:compound_index_from)
			{
				if(v.pid==pid)
					index_from=v.index;
			}
			for(Vertex v:compound_index_to)
			{
				if(v.pid==pid)
					index_to=v.index;
			}
			searchpath_init(pid, index_from, index_to);
			//searchpath_init(0, 0, 3);
			Intent mIntent=new Intent(Activity_SearchPath.this, Activity_ReactionLayer.class);
			Bundle b=new Bundle();
			DBManager db=new DBManager(Activity_SearchPath.this);
			Pathway mPathway=db.QueryForPathwayByIndex(pid+1);
			db.closeDB();
			SimpleVector pos=new SimpleVector(mPathway.x,mPathway.y,mPathway.z);
			b.putInt("pid",pid);
			b.putSerializable("pos",pos);
			b.putString("name",mPathway.name );
			b.putString("Activity", "Activity_SearchPath");
			b.putSerializable("paths", (Serializable) paths);
			mIntent.putExtras(b);
			startActivity(mIntent);
			/*for(int i=0;i<paths.size();i++)
			{
				List<Edge> l=paths.get(i);
				for(int j=0;j<l.size();j++)
				{
					Edge e=l.get(j);
					System.out.println("  "+e.sindex+"  "+e.eindex+"  ");
				}
			}	*/
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}

		
	}
	
	class lis_serach implements OnClickListener
	{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			//searchpath_init(0, 0, 3);
			/*for(int i=0;i<paths.size();i++)
			{
				List<Edge> l=paths.get(i);
				for(int j=0;j<l.size();j++)
				{
					Edge e=l.get(j);
					System.out.println("  "+e.sindex+"  "+e.eindex+"  ");
				}
			}*/
			Search();
			if(paths_pid!=null&&paths_pid.size()>0)
			{
				noresultmatched.setVisibility(View.GONE);
				listview_result.setAdapter(baseAdapter_result);
				listview_result.setVisibility(View.VISIBLE);
				listview_result.setOnItemClickListener(new lis_res());
			}
			else
			{
				listview_result.setVisibility(View.GONE);
				noresultmatched.setText("No path matched");
				noresultmatched.setVisibility(View.VISIBLE);
			}
			
		}
		
	}
	
	
	/*private BaseAdapter baseAdapter_compound=new BaseAdapter() {
		
		public View getView(int position, View convertView, ViewGroup parent) {
			
			TextView tv = new TextView(Activity_SearchPath.this);
			tv.setText(compound.get(position));// 设置内容
			tv.setTextSize(24);
			return tv;
		}
		
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return compound.get(position);
		}
		
		public int getCount() {
			// TODO Auto-generated method stub
			return compound.size();
		}
	}; */
	
	class Adapter_List extends BaseAdapter
	{
		private List<String> mlist;
		public Adapter_List(List<String> list) {
			mlist=list;
			// TODO Auto-generated constructor stub
		}
		public void updatedata(List<String> list)
		{
			mlist=list;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return mlist.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mlist.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			TextView tv = new TextView(Activity_SearchPath.this);
			tv.setText(mlist.get(position));// 设置内容
			tv.setTextSize(24);
			return tv;
		}
		
	}
	
	private BaseAdapter baseAdapter_result=new BaseAdapter() {
		
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			TextView tv=new TextView(Activity_SearchPath.this);
			String re="Result "+position+":reactions in pathway "+paths_pid.get(position);
			tv.setText(re);
			tv.setTextSize(24);
			return tv;
		}
		
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return paths_pid.get(position);
		}
		
		public int getCount() {
			// TODO Auto-generated method stub
			return paths_pid.size();
		}
	};
	
	
	
	
	
	
	private class sortbyname implements Comparator
	{

		public int compare(Object lhs, Object rhs) {
			// TODO Auto-generated method stub
			String s1=(String)lhs;
			String s2=(String)rhs;
			return s1.compareTo(s2);
		}
		
	}

}
