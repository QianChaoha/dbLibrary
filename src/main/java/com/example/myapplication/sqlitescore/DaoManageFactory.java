package com.example.myapplication.sqlitescore;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2017-1-20.
 */
public class DaoManageFactory {
    private String path;
    private static DaoManageFactory instance = new DaoManageFactory(new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "logic.db"));
    private SQLiteDatabase database;

    private DaoManageFactory(File file) {
        this.path = file.getAbsolutePath();
        openDatabase(path);
    }

    public static DaoManageFactory getInstance() {
        return instance;
    }

    public synchronized  <T extends BaseDao<M>, M> T getDataHelper(Class<T> clazz, Class<M> entityClass) {
        BaseDao baseDao = null;
        try {
            baseDao = clazz.newInstance();
            baseDao.init(entityClass,database);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (T) baseDao;
    }

    private void openDatabase(String path) {
        database = SQLiteDatabase.openOrCreateDatabase(path, null);
    }
}
