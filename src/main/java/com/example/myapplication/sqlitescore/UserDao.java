package com.example.myapplication.sqlitescore;

/**
 * Created by Administrator on 2017-1-22.
 */
public class UserDao extends BaseDao {

    @Override
    public String createDatabase() {
        return "create table if not exists tb_user (name varchar(20),password varchar(10))";
    }
}
