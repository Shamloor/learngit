import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;

import util.HbaseUtil;

import java.util.HashSet;

public class test {
    public static void main(String[] args) throws Exception {
        /*HbaseUtil.createTable("Test","TInfo");
        Connection connection = ConnectionFactory.createConnection(HbaseUtil.conf);
        Table table = connection.getTable(TableName.valueOf("Test"));
        for(int i = 1; i < 100000; i ++ ) 
        {
            HbaseUtil.putData(connection, table, String.valueOf(i), "TInfo","row",String.valueOf(i));
            HbaseUtil.putData(connection, table, String.valueOf(i), "TInfo","value",String.valueOf(100000-i));
        }*/
        
        
        //计算平均值
        Connection connection = ConnectionFactory.createConnection(HbaseUtil.conf);
        Table table = connection.getTable(TableName.valueOf("Albums"));
        
        
        
        
        //查看是否有重复元素
        /*double[] ai = new double[100010];
        for (int i = 1; i <= 100000; i++) {
            
            ai[i] = HbaseUtil.getData(connection, table, String.valueOf(i),"AInfo","num_of_sales");
        }
        findDupicateInArray(ai);
        
        
    }
    public static void findDupicateInArray(double[] a) {
        int count=0;
        for(int j=0;j<a.length;j++) {
            for(int k =j+1;k<a.length;k++) {
                if(a[j]==a[k]) {
                    count++;
                }
            }
            if(count!=0)
                if(a[j] > 990000)
                System.out.println( "重复元素 : " +  a[j] );
            count = 0;
        }*/
    }
    
}
