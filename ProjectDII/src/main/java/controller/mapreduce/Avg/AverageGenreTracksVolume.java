package controller.mapreduce.Avg;

import com.alibaba.fastjson.JSONObject;
import model.MostPopularVo;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import util.HbaseUtil;
import util.mapreduce.Average;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/AverageGenreTracksVolume")
public class AverageGenreTracksVolume extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //1,调用Average
        /**
         *  参数1,列族
         *  参数2，列（需要统计的列）
         *  参数3，列族
         *  参数4，列
         * */
        Average.set("AInfo","genre","num_of_tracks","cf","average");

        try {
            //提交作业到MapReduce分析
            HbaseUtil.jobSubmission("Albums","Num_of_Tracks","cf","AverageTracks", Average.Mapper.class, Average.Reducer.class,Text.class,IntWritable.class);
            //查询所有的数据
            List<MostPopularVo> averageTarget2 = HbaseUtil.getAllRows("Num_of_Tracks", null);
            //将Java对象转换为JSON字符串存入到域中
            JSONObject averageTargetJson2 = new JSONObject();
            averageTargetJson2.put("rows", averageTarget2);
            System.out.println("分析完成");
            System.out.println(averageTargetJson2.toString());
            request.setAttribute("message","分析成功，当前展示所有曲风的平均音轨数量");
            request.setAttribute("average2",averageTargetJson2.toString());
        } catch (Exception e) {
            e.printStackTrace();
            // 跳转到 message.jsp
            System.out.println("分析失败");
            request.setAttribute("message","分析失败，请联系管理员");
        }finally {
            
            getServletContext().getRequestDispatcher("/AverageGenreTracksVolumeSuccess.jsp").forward(
                    request, response);
        }

    }
}