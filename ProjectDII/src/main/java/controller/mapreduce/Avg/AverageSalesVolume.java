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

@WebServlet("/AverageSalesVolume")
public class AverageSalesVolume extends HttpServlet {
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
        Average.set("AInfo","year_of_pub","num_of_sales","cf","topN");

        try {
            //提交作业到MapReduce分析
            HbaseUtil.jobSubmission("Albums","Num_of_Sales","cf","AverageSales", Average.Mapper.class, Average.Reducer.class,Text.class,IntWritable.class);
            //查询所有的数据
            List<MostPopularVo> averageTarget1 = HbaseUtil.getAllRows("Num_of_Sales", null);
            //将Java对象转换为JSON字符串存入到域中
            JSONObject averageTargetJson1 = new JSONObject();
            averageTargetJson1.put("rows", averageTarget1);
            System.out.println("分析完成");
            
            request.setAttribute("message","分析成功，当前展示所有年份的销量");
            request.setAttribute("average1",averageTargetJson1);
        } catch (Exception e) {
            e.printStackTrace();
            // 跳转到 message.jsp
            System.out.println("分析失败");
            request.setAttribute("message","分析失败，请联系管理员");
        }finally {
            getServletContext().getRequestDispatcher("/AverageSalesVolumeSuccess.jsp").forward(
                    request, response);
        }

    }
}