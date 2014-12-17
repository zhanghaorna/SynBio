package com.zhr.database;

import java.util.List;

import android.content.Context;



import com.zhr.sqlitedao.DaoSession;
import com.zhr.sqlitedao.News;
import com.zhr.sqlitedao.NewsDao;
import com.zhr.sqlitedao.NewsDao.Properties;
import com.zhr.synbio.MyApplication;

import de.greenrobot.dao.query.QueryBuilder;

public class DBNewsHelper {
	private static DBNewsHelper dbNewsHelper;
	private DaoSession daoSession;
	private NewsDao newsDao;
	
	public DBNewsHelper()
	{
		
	}
	
	public static DBNewsHelper getInstance(Context context)
	{
		if(dbNewsHelper == null)
		{
			dbNewsHelper = new DBNewsHelper();
			dbNewsHelper.daoSession = MyApplication.getDaoSession(context);
			dbNewsHelper.newsDao = dbNewsHelper.daoSession.getNewsDao();
		}
		return dbNewsHelper;
	}
	
	public long insertNews(News news)
	{
		return newsDao.insert(news);
	}
	
	public List<News> queryNews(String category)
	{
		if(category == "")
			return null;
		QueryBuilder<News> queryBuilder = newsDao.queryBuilder();
		
		queryBuilder.where(NewsDao.Properties.Category.eq(category)).orderDesc(Properties.Id);
		if(queryBuilder.count() > 10)
			return queryBuilder.list().subList(0, 10);
		else
			return queryBuilder.list();
		
	}
	
	public boolean queryNewsFromTitle(String title,String category)
	{
		if(title == "")
			return true;
		QueryBuilder<News> queryBuilder = newsDao.queryBuilder();
		queryBuilder.where(NewsDao.Properties.Title.eq(title),NewsDao.Properties.Category.eq(category));
		if(queryBuilder.count() > 0)
			return true;
		else
			return false;
	}
	
	public List<News> getMoreNews(News news)
	{
		QueryBuilder<News> queryBuilder = newsDao.queryBuilder();
		queryBuilder.where(NewsDao.Properties.Id.lt(news.getId()),NewsDao.Properties.Category.eq(news.getCategory())).orderDesc(Properties.Id);
		if(queryBuilder.count() > 10)
			return queryBuilder.list().subList(0, 10);
		else
			return queryBuilder.list();
	}
	
	public void deleteNews(String category)
	{
		QueryBuilder<News> queryBuilder = newsDao.queryBuilder();
		queryBuilder.where(NewsDao.Properties.Category.eq(category));
		List<News> news = queryBuilder.list();
		for(News ns:news)
			newsDao.delete(ns);
	}

}
