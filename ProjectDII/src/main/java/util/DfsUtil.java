package util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

public class DfsUtil  {
    private static Configuration con = null;
    private static final String URI_LINE =  "hdfs://master:9000";
    private static final String USER =  "root";
    private static final String PATH = "/user/root/master/";
    static{
        con = new Configuration();
    }
}
