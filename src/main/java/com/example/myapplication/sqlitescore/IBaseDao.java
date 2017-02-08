package com.example.myapplication.sqlitescore;

/**
 * Created by Administrator on 2017-1-19.
 */
public interface IBaseDao<T> {
    Long insert(T entity);
    int update(T entity,T where);

}
