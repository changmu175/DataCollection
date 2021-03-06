package com.ycm.kata.datacollection.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.ycm.kata.datacollection.model.entity.ProjectEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "PROJECT_ENTITY".
*/
public class ProjectEntityDao extends AbstractDao<ProjectEntity, Long> {

    public static final String TABLENAME = "PROJECT_ENTITY";

    /**
     * Properties of entity ProjectEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property ProjectName = new Property(1, String.class, "projectName", false, "PROJECT_NAME");
        public final static Property CheckDate = new Property(2, long.class, "checkDate", false, "CHECK_DATE");
        public final static Property UpdateTime = new Property(3, long.class, "updateTime", false, "UPDATE_TIME");
        public final static Property UnitEngineering = new Property(4, String.class, "unitEngineering", false, "UNIT_ENGINEERING");
        public final static Property Block = new Property(5, String.class, "block", false, "BLOCK");
        public final static Property PilNo = new Property(6, String.class, "pilNo", false, "PIL_NO");
        public final static Property Remark = new Property(7, String.class, "remark", false, "REMARK");
        public final static Property Defects = new Property(8, String.class, "defects", false, "DEFECTS");
        public final static Property ImagePath = new Property(9, String.class, "imagePath", false, "IMAGE_PATH");
    }


    public ProjectEntityDao(DaoConfig config) {
        super(config);
    }
    
    public ProjectEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PROJECT_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"PROJECT_NAME\" TEXT," + // 1: projectName
                "\"CHECK_DATE\" INTEGER NOT NULL ," + // 2: checkDate
                "\"UPDATE_TIME\" INTEGER NOT NULL ," + // 3: updateTime
                "\"UNIT_ENGINEERING\" TEXT," + // 4: unitEngineering
                "\"BLOCK\" TEXT," + // 5: block
                "\"PIL_NO\" TEXT," + // 6: pilNo
                "\"REMARK\" TEXT," + // 7: remark
                "\"DEFECTS\" TEXT," + // 8: defects
                "\"IMAGE_PATH\" TEXT);"); // 9: imagePath
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PROJECT_ENTITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ProjectEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String projectName = entity.getProjectName();
        if (projectName != null) {
            stmt.bindString(2, projectName);
        }
        stmt.bindLong(3, entity.getCheckDate());
        stmt.bindLong(4, entity.getUpdateTime());
 
        String unitEngineering = entity.getUnitEngineering();
        if (unitEngineering != null) {
            stmt.bindString(5, unitEngineering);
        }
 
        String block = entity.getBlock();
        if (block != null) {
            stmt.bindString(6, block);
        }
 
        String pilNo = entity.getPilNo();
        if (pilNo != null) {
            stmt.bindString(7, pilNo);
        }
 
        String remark = entity.getRemark();
        if (remark != null) {
            stmt.bindString(8, remark);
        }
 
        String defects = entity.getDefects();
        if (defects != null) {
            stmt.bindString(9, defects);
        }
 
        String imagePath = entity.getImagePath();
        if (imagePath != null) {
            stmt.bindString(10, imagePath);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ProjectEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String projectName = entity.getProjectName();
        if (projectName != null) {
            stmt.bindString(2, projectName);
        }
        stmt.bindLong(3, entity.getCheckDate());
        stmt.bindLong(4, entity.getUpdateTime());
 
        String unitEngineering = entity.getUnitEngineering();
        if (unitEngineering != null) {
            stmt.bindString(5, unitEngineering);
        }
 
        String block = entity.getBlock();
        if (block != null) {
            stmt.bindString(6, block);
        }
 
        String pilNo = entity.getPilNo();
        if (pilNo != null) {
            stmt.bindString(7, pilNo);
        }
 
        String remark = entity.getRemark();
        if (remark != null) {
            stmt.bindString(8, remark);
        }
 
        String defects = entity.getDefects();
        if (defects != null) {
            stmt.bindString(9, defects);
        }
 
        String imagePath = entity.getImagePath();
        if (imagePath != null) {
            stmt.bindString(10, imagePath);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public ProjectEntity readEntity(Cursor cursor, int offset) {
        ProjectEntity entity = new ProjectEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // projectName
            cursor.getLong(offset + 2), // checkDate
            cursor.getLong(offset + 3), // updateTime
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // unitEngineering
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // block
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // pilNo
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // remark
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // defects
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9) // imagePath
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ProjectEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setProjectName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCheckDate(cursor.getLong(offset + 2));
        entity.setUpdateTime(cursor.getLong(offset + 3));
        entity.setUnitEngineering(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setBlock(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setPilNo(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setRemark(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setDefects(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setImagePath(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(ProjectEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(ProjectEntity entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(ProjectEntity entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
