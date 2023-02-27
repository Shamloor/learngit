package util.mapreduce;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import util.HbaseUtil;

import java.io.IOException;

public class Average {
    private static String columnFamilyS = null;
    private static String keyS = null;
    private static String valueS = null;
    private static String outputColumnFamilyS = null;
    private static String outputColumnS = null;

    public static void set(String columnFamily,String key,String value,String outputColumnFamily,String outputColumn){
        columnFamilyS = columnFamily;
        keyS = key;
        valueS = value;
        outputColumnFamilyS = outputColumnFamily;
        outputColumnS = outputColumn;
    }
    public static class Mapper extends TableMapper<Text, IntWritable>
    {
        private Text key = new Text();
        private Text valus = new Text();
        private IntWritable one = new IntWritable();

        public void map(ImmutableBytesWritable row, Result values, Context context) throws IOException, InterruptedException {

            key.set(values.getValue(Bytes.toBytes(columnFamilyS), Bytes.toBytes(keyS)));//
            valus.set(values.getValue(Bytes.toBytes(columnFamilyS), Bytes.toBytes(valueS)));
            one=new IntWritable(Integer.parseInt(valus.toString()));
            context.write(key, one);
        }
    }
    // Reduce method
    public static class Reducer extends TableReducer<Text,IntWritable,ImmutableBytesWritable>
    {
        public void reduce(Text key, Iterable<IntWritable> values,Context context) throws IOException, InterruptedException {
            int count = 0;
            float sum = 0;
            float average = 0;

            for (IntWritable val:values){
                sum+=Integer.parseInt(val.toString());
                count++;
            }
            average = sum / (float) count;
            Put put = new Put(Bytes.toBytes(key.toString()));
            put.addColumn(Bytes.toBytes(outputColumnFamilyS),Bytes.toBytes(outputColumnS),Bytes.toBytes(String.valueOf(average)));
            context.write(null,put);
        }

    }
    // main method
    public static void main(String[] args) throws Exception {
        
        Average.set("AInfo","year_of_pub",
                "num_of_sales","cf","average");
        HbaseUtil.jobSubmission("Albums","Num_of_Sales",
                "cf","AverageSales", Mapper.class, Reducer.class,Text.class,IntWritable.class);
    }
}
