package util.mapreduce;

import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;

import org.apache.hadoop.io.Text;
import util.HbaseUtil;

import java.io.IOException;
import java.util.Iterator;
import java.util.TreeMap;

public class TopN {
        private static String columnFamilyS1 = null;
        private static String columnFamilyS2 = null;
        private static String keyS = null;
        private static String valueS = null;
        private static String outputColumnFamilyS = null;
        private static String outputColumnS = null;

        public static void set(String columnFamily1,String key,String columnFamily2, String value,String outputColumnFamily,String outputColumn){
            columnFamilyS1 = columnFamily1;
            columnFamilyS2 = columnFamily2;
            keyS = key;
            valueS = value;
            outputColumnFamilyS = outputColumnFamily;
            outputColumnS = outputColumn;
        }
        // Map method
        public static class Mapper extends TableMapper<Text, Text>
        {
            private Text key = new Text();
            private Text value = new Text();
            TreeMap<Double,Text> tree;
            @Override
            protected void cleanup(Context context)  throws IOException, InterruptedException {
                //output tree items as mapper output value
                // 假设map是TreeMap对象

// map中的key是String类型，value是Integer类型

                Text value = null;

                Double key = null;

                Iterator iter = tree.entrySet().iterator();

                while(iter.hasNext()) {

                   java.util.Map.Entry entry = (java.util.Map.Entry)iter.next();
                    // 获取key
                    key = (Double) entry.getKey();
                    // 获取value
                    value = (Text) entry.getValue();
                    context.write(value, new Text(key.toString()));
                }

            }
            public void map(ImmutableBytesWritable row, Result values, Context context) throws IOException, InterruptedException {

                key.set(values.getValue(Bytes.toBytes(columnFamilyS1), Bytes.toBytes(keyS)));//名称
                value.set(values.getValue(Bytes.toBytes(columnFamilyS2), Bytes.toBytes(valueS)));//值
                String str = value.toString();
                try {
                    tree.put(Double.valueOf(str),new Text(key));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (tree.size() > 5)
                    tree.remove(tree.firstKey());
            }
            protected void setup(Context context)
                    throws IOException, InterruptedException {
                // super.setup(context);
                tree=new TreeMap<Double,Text>();
            }
        }

        // Reduce method
        public static class Reducer extends TableReducer<Text, Text,ImmutableBytesWritable> {
            @Override
            protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
                // TODO Auto-generated method stub
                for (Text text : values) {
                    Put put = new Put(Bytes.toBytes(String.valueOf(key)));
                    put.addColumn(Bytes.toBytes(outputColumnFamilyS), Bytes.toBytes(outputColumnS), Bytes.toBytes(text.toString()));
                    context.write(null, put);
                }
            }

        }

    public static void main(String[] args) throws Exception {
        
        TopN.set("TInfo","row","TInfo","value","cf","topN");
        HbaseUtil.jobSubmission("Test","topNTargetTest","cf","bestScore", Mapper.class, Reducer.class,Text.class, Text.class);
        
    }



}
