package com.fancoff.coffeemaker.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.fancoff.coffeemaker.net.RequstBean.Order;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ORDER".
*/
public class OrderDao extends AbstractDao<Order, Long> {

    public static final String TABLENAME = "ORDER";

    /**
     * Properties of entity Order.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Sale_time = new Property(0, String.class, "sale_time", false, "SALE_TIME");
        public final static Property _id = new Property(1, Long.class, "_id", true, "_id");
        public final static Property Id = new Property(2, int.class, "id", false, "ID");
        public final static Property Order_id = new Property(3, long.class, "order_id", false, "ORDER_ID");
    }


    public OrderDao(DaoConfig config) {
        super(config);
    }
    
    public OrderDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ORDER\" (" + //
                "\"SALE_TIME\" TEXT NOT NULL ," + // 0: sale_time
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 1: _id
                "\"ID\" INTEGER NOT NULL ," + // 2: id
                "\"ORDER_ID\" INTEGER NOT NULL );"); // 3: order_id
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ORDER\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Order entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getSale_time());
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(2, _id);
        }
        stmt.bindLong(3, entity.getId());
        stmt.bindLong(4, entity.getOrder_id());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Order entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getSale_time());
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(2, _id);
        }
        stmt.bindLong(3, entity.getId());
        stmt.bindLong(4, entity.getOrder_id());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1);
    }    

    @Override
    public Order readEntity(Cursor cursor, int offset) {
        Order entity = new Order( //
            cursor.getString(offset + 0), // sale_time
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // _id
            cursor.getInt(offset + 2), // id
            cursor.getLong(offset + 3) // order_id
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Order entity, int offset) {
        entity.setSale_time(cursor.getString(offset + 0));
        entity.set_id(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setId(cursor.getInt(offset + 2));
        entity.setOrder_id(cursor.getLong(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Order entity, long rowId) {
        entity.set_id(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Order entity) {
        if(entity != null) {
            return entity.get_id();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Order entity) {
        return entity.get_id() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
