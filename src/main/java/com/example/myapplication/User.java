package com.example.myapplication;

import com.example.myapplication.sqlitescore.DbTable;

/**
 * Created by Administrator on 2017-1-21.
 */
//给DbTable中的成员变量value赋值(表名tableName)
//@DbTable(value ="tb_user",a=2)
@DbTable("tb_user")
public class User {
    public String name;
    public String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
