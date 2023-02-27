package controller.mapreduce.TopN;

import com.alibaba.fastjson.JSONObject;
import model.MostPopularVo;
import org.apache.hadoop.io.Text;
import util.HbaseUtil;
import util.mapreduce.TopN;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/TopScore")
public class TopScore extends HttpServlet {
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
        TopN.set("AInfo","album_title","Critic","average_critic","cf","topN");

        try {
            //提交作业到MapReduce分析
            HbaseUtil.jobSubmission("Albums","topNTarget","cf","bestScore", TopN.Mapper.class, TopN.Reducer.class,Text.class, Text.class);
            //查询所有的数据
            List<MostPopularVo> TopNTarget2 = HbaseUtil.getAllRows("topNTarget", null);
            //将Java对象转换为JSON字符串存入到域中
            JSONObject TopNJson2 = new JSONObject();
            TopNJson2.put("rows", TopNTarget2);
            System.out.println("分析完成");
            System.out.println(TopNJson2.toString());
            request.setAttribute("message","分析成功，当前展示评分最好的专辑");
            request.setAttribute("TopN2",TopNJson2.toString());
        } catch (Exception e) {
            e.printStackTrace();
            // 跳转到 message.jsp
            System.out.println("分析失败");
            request.setAttribute("message","分析失败，请联系管理员");
        }finally {
            getServletContext().getRequestDispatcher("/TopScoreSuccess.jsp").forward(
                    request, response);
        }

    }
}