package util;


import model.MostPopularVo;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.log4j.BasicConfigurator;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HbaseUtil {
    //创建Hadoop以及HBased管理配置对象
    public static Configuration conf;
    //在HBase中管理,访问表需要先创建HBaseAdmin对象
    public static Admin admin;

    static {
        conf = new Configuration();
        conf.set("hbase.zookeeper.quorum", "192.168.177.128,192.168.177.129,192.168.177.130");
        conf.set("fs.defaultFS","hdfs://master:9000");
        
        Connection conn = null;
        try {
            conn = ConnectionFactory.createConnection(conf);
            admin = conn.getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 1.判断表是否已经存在
     *
     * @param
     */
    public static boolean isExists(String tableName) throws Exception {

        return admin.tableExists(TableName.valueOf(tableName));
    }

    /**
     * 2.创建表
     *
     * @param tableName
     * @param columnFamilys
     */
    public static void createTable(String tableName, String... columnFamilys) throws Exception {
        //判断表是否存在
        if (isExists(tableName)) {
            System.out.println("表" + tableName + "已存在!");
            System.exit(0);
        } else {
            //创建表属性对象  ,表名需要转乘字节  TableName.valueOf(tableName)
            TableDescriptorBuilder tdb = TableDescriptorBuilder.newBuilder(TableName.valueOf(tableName));
            for (String cf : columnFamilys) {
                tdb.setColumnFamily(ColumnFamilyDescriptorBuilder.of(cf)).build();
            }
            admin.createTable(tdb.build());
            System.out.println("表" + tableName + "创建成功!");
        }
    }

    /**
     * 3.删除表
     *
     * @param tableName
     * @throws Exception
     */
    public static void dropTable(String tableName) throws Exception {
        //如果一个表不存在,删除的话会抛异常
        if (isExists(tableName)) {
            //将表变为不可用
            admin.disableTable(TableName.valueOf(tableName));
            //删除表
            admin.deleteTable(TableName.valueOf(tableName));
            System.out.println("表" + tableName + "删除成功!");
        } else {
            System.out.println("表" + tableName + "不存在!");
        }
    }


    /**
     * 4.获取扫描对象
     *
     * @param tableName
     */
    public static ResultScanner getAllRows(String tableName) throws Exception {
        Connection conn = ConnectionFactory.createConnection(conf);
        Table table = conn.getTable(TableName.valueOf(tableName));
        //得到用于扫描region的对象
        Scan scan = new Scan();
        //使用HTable得到resultScanner实现类的对象
        return table.getScanner(scan);
    }

    /**
     * 5.提交任务
     *
     * @param sourceTable
     * @param targetTable
     * @param columnFamily
     * @param jobName
     * @param Map
     * @param Reduce
     * @param outputKey
     * @param outputValue
     */ 
    public static void jobSubmission(String sourceTable,String targetTable, String columnFamily,
                                     String jobName, Class<?> Map, Class<?> Reduce, Class<?> outputKey, Class<?> outputValue) throws Exception {
        BasicConfigurator.configure();
        if (isExists(targetTable)) {
            //如果表不存在，删除表里的数据
            dropTable(targetTable);
        }
        //如果表不存在，创建表
        createTable(targetTable, columnFamily);

        Job job = new Job(conf, jobName);
        job.setJarByClass(HbaseUtil.class);
        Scan scan = new Scan();
        scan.setCaching(100);        // 1 is the default in Scan, which will be bad for MapReduce jobs
        scan.setCacheBlocks(false);  // don't set to true for MR jobs
        TableMapReduceUtil.initTableMapperJob(
                sourceTable,      // input table
                scan,            // Scan instance to control CF and attribute selection
                (Class<? extends TableMapper>) Map,   // mapper class
                (Class<? extends Writable>) outputKey,
                (Class<? extends Writable>) outputValue,        // mapper output value
                job);
        TableMapReduceUtil.initTableReducerJob(
                targetTable,      // output table
                (Class<? extends TableReducer>) Reduce,             // reducer class
                job);
        boolean b = job.waitForCompletion(true);
    }

    /**
     * 6.获取所有行 存储于自定义类中
     *
     * @param targetTable
     * @param hbaseUtil
     */
    public static List<MostPopularVo> getAllRows(String targetTable, HbaseUtil hbaseUtil) throws Exception {
        List<MostPopularVo> mostPopulars = new ArrayList<>();
        ResultScanner results = getAllRows(targetTable);
        /** 返回rk下边的所有单元格*/
        for(Result result : results){
            //获取所有单元格数据
            Cell[] cells = result.rawCells();
            for(Cell cell : cells){
                MostPopularVo mostPopular = new MostPopularVo();
                mostPopular.setRowkey(Bytes.toString(CellUtil.cloneRow(cell)));
                mostPopular.setFamily(Bytes.toString(CellUtil.cloneFamily(cell)));
                mostPopular.setColumn(Bytes.toString(CellUtil.cloneQualifier(cell)));
                mostPopular.setValue(Bytes.toString(CellUtil.cloneValue(cell)));
                mostPopulars.add(mostPopular);
            }
        }   
        System.out.println(mostPopulars);
        return mostPopulars;
    }

    /**
     * 7.插入数据
     * 用于取平均值 设为private 为了节约时间不每次都创建销毁连接 
     *
     * @param connection
     * @param table
     * @param rowKey
     * @param cf
     * @param cn
     * @param value
     */
    //    向表插入数据
    private static void putData(Connection connection,Table table,String rowKey,String cf,String cn,
                               String value) throws IOException {
//        创建put对象
        Put put = new Put(Bytes.toBytes(rowKey));
//        给put对象赋值
        put.addColumn(Bytes.toBytes(cf),Bytes.toBytes(cn),Bytes.toBytes(value));
//        插入数据
        table.put(put);

    }
    
    
    /**
     * 7.获取某单元的数据
     * 用于取平均值 设为private 为了节约时间不每次都创建销毁连接 
     *
     * @param connection
     * @param table
     * @param rowKey
     * @param cf
     * @param cn
     * 
     */
    private static double getData(Connection connection,Table table,String rowKey,String cf,String cn) throws IOException {

        Get get = new Get(Bytes.toBytes(rowKey));
        get.addFamily(Bytes.toBytes(cf));
        get.addColumn(Bytes.toBytes(cf),Bytes.toBytes(cn));
        get.setMaxVersions(5);
        Result result = table.get(get);
        double score = 0;
        for (Cell cell : result.rawCells()){
            score = Double.parseDouble(Bytes.toString(CellUtil.cloneValue(cell)));
        }
        
        return score;
    }
    /**
     * 8.求评分的平均值
     * 在此处创建连接，使用getData()与putData()方法 
     * 在test.java中使用此函数，因计算时间太长不适合展现在网页上
     *
     * @param
     */
    public static void getAverageScore () throws IOException {
        DecimalFormat df = new DecimalFormat("0.0");
        Connection connection = ConnectionFactory.createConnection(conf);
        Table table = connection.getTable(TableName.valueOf("Albums"));
        Integer rowkey;
        double avg;
        try {
            //计算平均分并保留两位小数点
            for (rowkey = 1; rowkey <= 100000; rowkey++) {
                avg = Double.parseDouble(df.format((getData(connection, table, String.valueOf(rowkey), "Critic", "rolling_stone_critic")
                        + getData(connection, table, String.valueOf(rowkey), "Critic", "mtv_critic")
                        + getData(connection, table, String.valueOf(rowkey), "Critic", "music_maniac_critic")) / 3));
                //存储平均分数
                putData(connection, table, String.valueOf(rowkey), "Critic", "average_critic", String.valueOf(avg));
                System.out.println("[" + rowkey + "]  " + avg);
            }
        } catch (IOException e) {
            System.out.println("平均分计算失败");
            e.printStackTrace();
        }
        System.out.println("平均分计算成功");
        table.close();
    }
    
    
}
