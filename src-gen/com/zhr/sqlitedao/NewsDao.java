package com.zhr.sqlitedao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.zhr.sqlitedao.News;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table NEWS.
*/
public class NewsDao extends AbstractDao<News, Long> {

    public static final String TABLENAME = "NEWS";

    /**
     * Properties of entity News.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Icon = new Property(1, String.class, "icon", false, "ICON");
        public final static Property Link = new Property(2, String.class, "link", false, "LINK");
        public final static Property Content = new Property(3, String.class, "content", false, "CONTENT");
        public final static Property Title = new Property(4, String.class, "title", false, "TITLE");
        public final static Property Category = new Property(5, String.class, "category", false, "CATEGORY");
    };


    public NewsDao(DaoConfig config) {
        super(config);
    }
    
    public NewsDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'NEWS' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'ICON' TEXT NOT NULL ," + // 1: icon
                "'LINK' TEXT NOT NULL ," + // 2: link
                "'CONTENT' TEXT NOT NULL ," + // 3: content
                "'TITLE' TEXT NOT NULL ," + // 4: title
                "'CATEGORY' TEXT NOT NULL );"); // 5: category
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'NEWS'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, News entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getIcon());
        stmt.bindString(3, entity.getLink());
        stmt.bindString(4, entity.getContent());
        stmt.bindString(5, entity.getTitle());
        stmt.bindString(6, entity.getCategory());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public News readEntity(Cursor cursor, int offset) {
        News entity = new News( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // icon
            cursor.getString(offset + 2), // link
            cursor.getString(offset + 3), // content
            cursor.getString(offset + 4), // title
            cursor.getString(offset + 5) // category
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, News entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setIcon(cursor.getString(offset + 1));
        entity.setLink(cursor.getString(offset + 2));
        entity.setContent(cursor.getString(offset + 3));
        entity.setTitle(cursor.getString(offset + 4));
        entity.setCategory(cursor.getString(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(News entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(News entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
