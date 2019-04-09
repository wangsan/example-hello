package com.wangsan.study.sqlite;

import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.sqlite.Function;
import org.sqlite.SQLiteException;

/**
 * @author wangsan
 * @date 2018/02/26
 */
public class SqliteTest {

    @Test
    public void testConnect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite::memory:";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    @Test
    public void testCrudFile() {
        testCrud("sample.db");
    }

    @Test
    public void testCrudMem() {
        testCrud(":memory:");
    }

    public void testCrud(String db) {
        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:" + db);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("drop table if exists person");
            statement.executeUpdate("create table person (id integer, name string)");
            statement.executeUpdate("insert into person values(1, 'leo')");
            statement.executeUpdate("insert into person values(2, 'yui')");
            ResultSet rs = statement.executeQuery("select * from person");
            while (rs.next()) {
                // read the result set
                System.out.println("name = " + rs.getString("name"));
                System.out.println("id = " + rs.getInt("id"));
            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e);
            }
        }
    }

    @Test
    public void testMemShare() throws Exception {
        String db = "file:memdb1?mode=memory&cache=shared";
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + db);
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.

        statement.executeUpdate("drop table if exists person");
        statement.executeUpdate("create table person (id integer, name string)");
        statement.executeUpdate("insert into person values(1, 'leo')");
        statement.executeUpdate("insert into person values(2, 'yui')");
        ResultSet rs = statement.executeQuery("select * from person");
        while (rs.next()) {
            // read the result set
            System.out.println("name = " + rs.getString("name"));
            System.out.println("id = " + rs.getInt("id"));
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Connection connThread = DriverManager.getConnection("jdbc:sqlite:" + db);
                    Statement statThread = connThread.createStatement();
                    ResultSet rsThread = statThread.executeQuery("select count(*) from person");
                    System.out.println("same process other connection size is " + rsThread.getInt(1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //        String db2 = "file:memdb2?mode=memory&cache=shared";
        //        Connection connection2 = DriverManager.getConnection("jdbc:sqlite:" + db2);
        //        connection2.

        TimeUnit.SECONDS.sleep(100);
    }

    @Test(expected = SQLiteException.class)
    public void testMemShareAnotherProcess() throws Exception {
        String db = "file:memdb1?mode=memory&cache=shared";
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + db);
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.

        ResultSet rs = statement.executeQuery("select * from person");
        while (rs.next()) {
            // read the result set
            System.out.println("name = " + rs.getString("name"));
            System.out.println("id = " + rs.getInt("id"));
        }

        TimeUnit.SECONDS.sleep(10);

    }

    @Test
    public void testBlob() throws Exception {
        String db = "file:memdb?mode=memory&cache=shared";
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + db);

        createBlob(connection, 3);

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from personfeature");
        while (rs.next()) {
            // read the result set
            System.out.println("name = " + rs.getString("name"));
            System.out.println("id = " + rs.getInt("id"));
            System.out.println("feature = " + Arrays.toString(rs.getBytes("feature")));
        }
    }

    public void createBlob(Connection connection, int size) throws Exception {
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.

        statement.executeUpdate("drop table if exists personfeature");
        statement.executeUpdate(
                "create table personfeature (id int PRIMARY KEY NOT NULL, name string, feature blob, featureMod "
                        + "double)");

        for (int i = 0; i < size; i++) {
            String insertSql = "insert into personfeature values(?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
            preparedStatement.setInt(1, i);
            preparedStatement.setString(2, i % 2 + "");
            float[] randomFloat = randomFloat();
            preparedStatement.setBytes(3, float2byte(randomFloat));
            preparedStatement.setDouble(4, mod(randomFloat));

            preparedStatement.executeUpdate();
        }

    }

    @Test
    public void testExUdf() throws Exception {
        Connection connection = udfConn();
        createBlob(connection, 3);

        String cexUdf =
                "select id,name,feature,featureMod,cosine_distance_ex(feature,?,featureMod,?) from personfeature";
        String cblasexUdf =
                "select id,name,feature,featureMod,cosine_distance_blas_ex(feature,?,featureMod,?) from personfeature";

        PreparedStatement preparedStatement = connection.prepareStatement(cblasexUdf);

        float[] floats = randomFloat127();
        preparedStatement.setBytes(1, float2byte(floats));
        preparedStatement.setDouble(2, mod(floats));

        preparedStatement.execute();
        final ResultSet rs = preparedStatement.getResultSet();

        while (rs.next()) {
            // read the result set
            System.out.println("name = " + rs.getString("name"));
            System.out.println("id = " + rs.getInt("id"));
            System.out.println("feature = " + Arrays.toString(rs.getBytes("feature")));
            System.out.println("featureMod = " + rs.getDouble("featureMod"));
            System.out.println("distance = " + rs.getDouble(5));
        }

    }

    @Test
    public void testUdf() throws Exception {
        Connection connection = udfConn();
        createBlob(connection, 3);

        String javaUdf = "select id,name,feature,distance(feature,?) from personfeature";
        String cUdf = "select id,name,feature,cosine_distance(feature,?) from personfeature";
        String cblasUdf = "select id,name,feature,cosine_distance_blas(feature,?) from personfeature";

        PreparedStatement preparedStatement = connection.prepareStatement(javaUdf);
        preparedStatement.setBytes(1, randomByte127());
        preparedStatement.execute();
        final ResultSet rs = preparedStatement.getResultSet();

        while (rs.next()) {
            // read the result set
            System.out.println("name = " + rs.getString("name"));
            System.out.println("id = " + rs.getInt("id"));
            System.out.println("feature = " + Arrays.toString(rs.getBytes("feature")));
            System.out.println("distance = " + rs.getDouble(4));
        }

    }

    private Connection udfConn() throws Exception {
        String db = "file:memdb?mode=memory&cache=shared";
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + db);

        Function.create(connection, "distance", new Function() {
            @Override
            protected void xFunc() throws SQLException {
                if (args() != 2) {
                    throw new SQLException(
                            "distance(blob,blob): Invalid argument count. Requires 2, but found " + args());
                }
                try {
                    byte[] bytesA = value_blob(0);
                    byte[] bytesB = value_blob(1);

                    double cosWangwu = MyFunctions.cosBytes(bytesA, bytesB);
                    result(cosWangwu);
                } catch (Exception exception) {
                    exception.printStackTrace();
                    throw new SQLException(
                            "distance(blob,blob): One of Arguments is invalid: " + exception.getLocalizedMessage());
                }
            }
        });
        return connection;
    }

    @Test
    public void testUdfOneMillion() throws Exception {
        Connection connection = udfConn();
        long createStart = System.currentTimeMillis();
        createBlob(connection, 1_000_000);
        System.err.println("create use time ms : " + (System.currentTimeMillis() - createStart));

        PreparedStatement preparedStatement =
                connection.prepareStatement(
                        "select id,name,feature,cosine_distance(feature,?) from personfeature where "
                                + " name='1'"
                                + " order by cosine_distance(feature,?) desc limit 6");

        preparedStatement.setBytes(1, randomByte127());
        preparedStatement.setBytes(2, randomByte127());
        long start = System.currentTimeMillis();
        preparedStatement.execute();
        System.err.println("query1 use time ms : " + (System.currentTimeMillis() - start));

        PreparedStatement preparedStatement2 =
                connection.prepareStatement(
                        "select count(id) from personfeature where cosine_distance(feature,?) >0.9");
        preparedStatement2.setBytes(1, randomByte127());
        long query2start = System.currentTimeMillis();
        preparedStatement2.execute();
        System.err.println("query2 size " + preparedStatement2.getResultSet().getInt(1));
        System.err.println("query2 use time ms : " + (System.currentTimeMillis() - query2start));

        PreparedStatement preparedStatement21 =
                connection.prepareStatement(
                        "select count(id) from personfeature where cosine_distance_ex(feature,?,featureMod,?) >0.9");
        float[] floats = randomFloat127();
        preparedStatement21.setBytes(1, float2byte(floats));
        preparedStatement21.setDouble(2, mod(floats));
        long query21start = System.currentTimeMillis();
        preparedStatement21.execute();
        System.err.println("query21 size " + preparedStatement21.getResultSet().getInt(1));
        System.err.println("query21 use time ms : " + (System.currentTimeMillis() - query21start));

        PreparedStatement preparedStatement22 =
                connection.prepareStatement(
                        "select count(id) from personfeature where cosine_distance_blas_ex(feature,?,featureMod,?) >0"
                                + ".9");
        preparedStatement22.setBytes(1, float2byte(floats));
        preparedStatement22.setDouble(2, mod(floats));
        long query22start = System.currentTimeMillis();
        preparedStatement22.execute();
        System.err.println("query22 size " + preparedStatement22.getResultSet().getInt(1));
        System.err.println("query22 use time ms : " + (System.currentTimeMillis() - query22start));

        PreparedStatement preparedStatement3 =
                connection.prepareStatement(
                        "select count(id) from personfeature where id >?");
        preparedStatement3.setInt(1, 500000);
        long query3start = System.currentTimeMillis();
        preparedStatement3.execute();
        System.err.println("query3 size " + preparedStatement3.getResultSet().getInt(1));
        System.err.println("query3 use time ms : " + (System.currentTimeMillis() - query3start));

        PreparedStatement preparedStatement4 =
                connection.prepareStatement(
                        "select count(id) from personfeature where name='1'");
        long query4start = System.currentTimeMillis();
        preparedStatement4.execute();
        System.err.println("query4 size " + preparedStatement4.getResultSet().getInt(1));
        System.err.println("query4 use time ms : " + (System.currentTimeMillis() - query4start));
    }

    private Random random = new Random();

    public byte[] randomByte() {
        ByteBuffer buffer = ByteBuffer.allocate(4 * 128);
        for (int j = 0; j < 128; j++) {
            buffer.put(MyFunctions.floatToByte(128 * random.nextFloat()));
        }
        return buffer.array();
    }

    public byte[] randomByte127() {
        ByteBuffer buffer = ByteBuffer.allocate(4 * 128);
        for (int j = 0; j < 128; j++) {
            buffer.put(MyFunctions.floatToByte(127));
        }
        return buffer.array();
    }

    public byte[] nullByte() {
        return new byte[0];
    }

    public byte[] float2byte(float[] floats) {
        ByteBuffer buffer = ByteBuffer.allocate(4 * 128);
        for (float f : floats) {
            buffer.put(MyFunctions.floatToByte(f));
        }
        return buffer.array();
    }

    public float[] randomFloat127() {
        float[] aa = new float[128];
        for (int i = 0; i < 128; i++) {
            aa[i] = 127;
        }
        return aa;
    }

    public float[] randomFloat() {
        float[] aa = new float[128];
        for (int i = 0; i < 128; i++) {
            aa[i] = random.nextFloat() * 128;
        }
        return aa;
    }

    public double mod(float[] a) {
        double sum = 0.0;
        for (float v : a) {
            sum += v * v;
        }
        return Math.sqrt(sum);
    }

}
