package com.example.myapplication.sqlitescore;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017-1-21.
 */
public abstract class BaseDao<T> implements IBaseDao<T> {
    private SQLiteDatabase database;
    private boolean isInit = false;
    private String tableName;
    private Class<T> entityClass;
    private Map<String, Field> cacheMap;

    //凡是操作数据库，都要防止线程同步问题
    public synchronized void init(Class<T> entity, SQLiteDatabase database) {
        if (!isInit) {
            this.database = database;
            this.entityClass = entity;
            //如果entityClass传递的是User实体类,拿到的就是 @DbTable("tb_user") 中的"tb_user"
            this.tableName = entityClass.getAnnotation(DbTable.class).value();
            database.execSQL(createDatabase());
            cacheMap = new HashMap<>();
            initCacheMap();
        }
    }

    @Override
    public Long insert(T entity) {
        Map<String, String> map = getValue(entity);
        long result = database.insert(tableName, null, getContentValues(map));
        System.out.println("result   " + result);
        return result;
    }

    private ContentValues getContentValues(Map<String, String> map) {
        ContentValues values = new ContentValues();
        Set<String> keySet = map.keySet();
        Iterator<String> iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = map.get(key);
            if (value != null) {
                values.put(key, value);
            }
        }
        return values;
    }

    /**
     * 将name-->李四这种对应关系塞到HashMap里
     *
     * @param entity
     * @return
     */
    private Map<String, String> getValue(T entity) {
        Map<String, String> result = new HashMap<>();
        Iterator<Field> iterator = cacheMap.values().iterator();
        while (iterator.hasNext()) {
            Field colmunFiled = iterator.next();
            String cacheKey = null;
            String cacheValue = null;
            //为空的话表明没有设置注解
            if (colmunFiled.getAnnotation(DbFiled.class) != null) {
                cacheKey = colmunFiled.getAnnotation(DbFiled.class).value();
            } else {
                cacheKey = colmunFiled.getName();
            }
            try {
                if (colmunFiled.get(entity) == null) {
                    continue;
                }
                //通过反射拿到成员变量的值
                cacheValue = colmunFiled.get(entity).toString();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            result.put(cacheKey, cacheValue);
        }
        return result;
    }

    @Override
    public int update(T entity, T where) {
        //要更新成的最终数据
        Map<String, String> map = getValue(where);
        //where
        Condition condition = new Condition(getValue(where));
        int result = database.update(tableName, getContentValues(map), condition.whereClause, condition.whereArgs);
        return result;
    }

    class Condition {
        private String whereClause;
        private String[] whereArgs;

        public Condition(Map<String, String> map) {
            ArrayList list = new ArrayList();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(" 1==1 ");
            Set<String> keySet = map.keySet();
            Iterator<String> iterator = keySet.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = map.get(key);
                if (value != null) {
                    stringBuilder.append(" and " + key + " =? ");
                    list.add(value);
                }
            }
            whereClause = stringBuilder.toString();
            whereArgs = (String[]) list.toArray(new String[list.size()]);
        }
    }

    /**
     * 将实体类的field和表的列名一一对应
     */
    protected void initCacheMap() {
        String sql = "select * from " + tableName + " limit 1,0";
        Cursor cursor = null;
        try {
            cursor = database.rawQuery(sql, null);
            String[] columnNames = cursor.getColumnNames();
            Field[] fields = entityClass.getFields();
            for (Field field : fields) {
                //在类的外面获取此类的私有成员变量的value时,需要调用下面方法，否则会报异常
                field.setAccessible(true);
            }
            for (String columnName : columnNames) {

                for (Field field : fields) {
                    String fileName;
                    //应用层设置注解
                    if (field.getAnnotation(DbFiled.class) != null) {
                        fileName = field.getAnnotation(DbFiled.class).value();
                    } else {
                        fileName = field.getName();
                    }
                    if (columnName.equals(fileName)) {
                        //对应起来
                        cacheMap.put(columnName, field);

                    }
                }
            }
        } catch (Exception e) {
        } finally {
            cursor.close();
        }
    }


    public abstract String createDatabase();
}
