package controller.mapreduce.WordCount;

import com.alibaba.fastjson.JSONObject;
import model.MostPopularVo;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import util.HbaseUtil;
import util.mapreduce.WordCount;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/WordCountRoles")
public class WordCountRoles extends HttpServlet {
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
        WordCount.set("AInfo","role","cf","wordcount");

        try {
            //提交作业到MapReduce分析
            HbaseUtil.jobSubmission("Artists","Num_of_Roles","cf","RoleCount",WordCount.Mapper.class, WordCount.Reducer.class,Text.class,IntWritable.class);
            //查询所有的数据
            List<MostPopularVo> WordCountTarget1 = HbaseUtil.getAllRows("Num_of_Roles", null);
            //将Java对象转换为JSON字符串存入到域中
            JSONObject WordCountTargetJson1 = new JSONObject();
            WordCountTargetJson1.put("rows", WordCountTarget1);
            System.out.println("分析完成");
            System.out.println(WordCountTargetJson1.toString());
            request.setAttribute("message","分析成功");
            request.setAttribute("wordcount1",WordCountTargetJson1);

        } catch (Exception e) {
            e.printStackTrace();
            // 跳转到 message.jsp
            System.out.println("分析失败");
            request.setAttribute("message","分析失败，请联系管理员");
        }finally {
            getServletContext().getRequestDispatcher("/WordCountRolesSuccess.jsp").forward(
                    request, response);
        }

    }
}
