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
import java.util.Iterator;

public class WordCount {  // Driver class or Main classs
    private static String columnFamilyS = null;
    private static String columnS = null;

    private static String outputColumnFamilyS = null;
    private static String outputColumnS = null;

    public static void set(String columnFamily,String column,String outputColumnFamily,String outputColumn){
        columnFamilyS = columnFamily;
        columnS = column;
        outputColumnFamilyS = outputColumnFamily;
        outputColumnS = outputColumn;
    }

    public static class Mapper extends TableMapper<Text, IntWritable> {

        public final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        public void map(ImmutableBytesWritable row, Result value, Context context) throws IOException, InterruptedException {
            String count = new String(value.getValue(Bytes.toBytes(columnFamilyS), Bytes.toBytes(columnS)));
            word.set(count);
            context.write(word, one);

        }

    }

    public static class Reducer extends TableReducer<Text, IntWritable, ImmutableBytesWritable> {

        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            Iterator<IntWritable> iterator = values.iterator();
            while (iterator.hasNext()) {
                sum = sum + iterator.next().get();
            }
            Put put = new Put(Bytes.toBytes((key).toString()));
            put.addColumn(Bytes.toBytes(outputColumnFamilyS), Bytes.toBytes(outputColumnS), Bytes.toBytes(String.valueOf(sum)));
            context.write(null, put);

        }

    }

    public static void main(String[] args) throws Exception {
        WordCount.set("AInfo","role","cf","wordcount");
        HbaseUtil.jobSubmission("Artists","Num_of_Roles","cf","RoleCount",WordCount.Mapper.class, WordCount.Reducer.class,Text.class,IntWritable.class);
    }
}