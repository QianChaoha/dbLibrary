package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.myapplication.sqlitescore.BaseDao;
import com.example.myapplication.sqlitescore.DaoManageFactory;
import com.example.myapplication.sqlitescore.DbTable;
import com.example.myapplication.sqlitescore.UserDao;

public class MainActivity extends AppCompatActivity {
BaseDao baseDao=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        baseDao= DaoManageFactory.getInstance().getDataHelper(UserDao.class,User.class);
    }

    public void insert(View view){
        User user=new User();
        user.setName("张三");
        user.setPassword("123456");
        baseDao.insert(user);
    }
    public void update(View view){
        User user=new User();
        user.setName("zhangsan");
        user.setPassword("12345678");

        User where=new User();
        where.setName("张三");
        baseDao.update(user,where);
    }
}
