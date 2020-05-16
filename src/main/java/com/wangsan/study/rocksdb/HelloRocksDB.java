package com.wangsan.study.rocksdb;


import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

public class HelloRocksDB {
    public static void main(String[] args) {
        // a static method that loads the RocksDB C++ library.
        RocksDB.loadLibrary();

        // the Options class contains a set of configurable DB options
        // that determines the behaviour of the database.
        try (final Options options = new Options().setCreateIfMissing(true)) {
            // a factory method that returns a RocksDB instance
            String path = HelloRocksDB.class.getResource("").getPath().substring(1) + "testRocksDB";
            System.out.println(path);
            try (final RocksDB db = RocksDB.open(options, path)) {
                writeAndRead(db);
                // do something
            }
        } catch (RocksDBException e) {
            // do some error handling
            e.printStackTrace();
        }
    }

    public static void writeAndRead(RocksDB db) throws RocksDBException {
        byte[] key1 = "foo".getBytes();
        byte[] key2 = "bar".getBytes();
        // some initialization for key1 and key2
        db.put(key1, key1);
        db.put(key2, key2);


        final byte[] value = db.get(key1);
        System.out.println(String.format("key1 values is %s ", new String(value)));
        if (value != null) {  // value == null if key1 does not exist in db.
            db.put(key2, value);
        }
        db.remove(key1);
        System.out.println(String.format("key2 values is %s ", new String(db.get(key2))));
    }
}
