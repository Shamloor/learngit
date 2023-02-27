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

@WebServlet("/WordCountCountries")
public class WordCountCountries extends HttpServlet {
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
        WordCount.set("AInfo","country","cf","wordcount");

        try {
            //提交作业到MapReduce分析
            HbaseUtil.jobSubmission("Artists","Num_of_Countries","cf","CountryCount",WordCount.Mapper.class, WordCount.Reducer.class,Text.class,IntWritable.class);
            //查询所有的数据
            List<MostPopularVo> WordCountTarget2 = HbaseUtil.getAllRows("Num_of_Countries", null);
            //将Java对象转换为JSON字符串存入到域中
            JSONObject WordCountTargetJson2 = new JSONObject();
            WordCountTargetJson2.put("rows", WordCountTarget2);
            System.out.println("分析完成");
            System.out.println(WordCountTargetJson2.toString());
            request.setAttribute("message","分析成功");
            request.setAttribute("wordcount2",WordCountTargetJson2);

        } catch (Exception e) {
            e.printStackTrace();
            // 跳转到 message.jsp
            System.out.println("分析失败");
            request.setAttribute("message","分析失败，请联系管理员");
        }finally {
            getServletContext().getRequestDispatcher("/WordCountCountriesSuccess.jsp").forward(
                    request, response);
        }

    }
}
