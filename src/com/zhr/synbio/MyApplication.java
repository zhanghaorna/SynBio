package com.zhr.synbio;







import com.zhr.sqlitedao.DaoMaster;
import com.zhr.sqlitedao.DaoMaster.OpenHelper;
import com.zhr.sqlitedao.DaoSession;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application{
	private static MyApplication mInstance;
	private static DaoMaster daoMaster;
	private static DaoSession daoSession;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		if(mInstance == null)
			mInstance = this;
		
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}
	
	public static DaoMaster getDaoMaster(Context context)
	{
        if (daoMaster == null) {  
            OpenHelper helper = new DaoMaster.DevOpenHelper(context,CommonUtil.DB_NAME, null);  
            daoMaster = new DaoMaster(helper.getWritableDatabase());  
        }  
        return daoMaster; 
	}
	
    public static DaoSession getDaoSession(Context context) {  
        if (daoSession == null) {  
            if (daoMaster == null) {  
                daoMaster = getDaoMaster(context);  
            }  
            daoSession = daoMaster.newSession();  
        }  
        return daoSession;  
    }

}
