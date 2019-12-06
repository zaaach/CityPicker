package com.zaaach.citypicker.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.zaaach.citypicker.model.City;

import java.util.List;

import static com.zaaach.citypicker.db.DBConfig.LATEST_DB_NAME;
import static com.zaaach.citypicker.db.DBConfig.TABLE_NAME;

/**
 * 创建日期：2019/4/1 0001on 下午 1:46
 * 描述：通过List集合接收数据源
 * @author Vincent
 * QQ：3332168769
 * 备注：
 */
public class CustomDBManager extends DefaultDBManager {
    public CustomDBManager(Context context, List<City> source) {
        super(context);
        updateDao(source);
    }

    private void updateDao(List<City> source) {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + LATEST_DB_NAME, null);

        try {
            db.delete(TABLE_NAME, null, null);

            db.beginTransaction();
            SQLiteStatement stat = db.compileStatement("insert into " + TABLE_NAME + " ('c_name','c_pinyin','c_code','c_province') VALUES (?,?, ?, ?)");
            for (int i = 0; i < source.size(); i++) {
                City city = source.get(i);
                stat.bindString(1, city.getName());
                stat.bindString(2, city.getPinyin());
                stat.bindString(3, city.getCode());
                stat.bindString(4, city.getProvince());
                stat.executeInsert();
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }


}
