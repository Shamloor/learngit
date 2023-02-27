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

@WebServlet("/TopSalesVolume")
public class TopSalesVolume extends HttpServlet {
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
        TopN.set("AInfo","album_title","AInfo","num_of_sales","cf","topN");

        try {
            //提交作业到MapReduce分析
            HbaseUtil.jobSubmission("Albums","TopNSales","cf","bestAlbum", TopN.Mapper.class, TopN.Reducer.class,Text.class, Text.class);
            //查询所有的数据
            List<MostPopularVo> TopNTarget1 = HbaseUtil.getAllRows("TopNSales", null);
            //将Java对象转换为JSON字符串存入到域中
            JSONObject TopNJson1 = new JSONObject();
            TopNJson1.put("rows", TopNTarget1);
            System.out.println("分析完成");
            System.out.println(TopNJson1.toString());
            request.setAttribute("message","分析成功，当前展示销量最好的100首专辑");
            request.setAttribute("TopN1",TopNJson1.toString());
        } catch (Exception e) {
            e.printStackTrace();
            // 跳转到 message.jsp
            System.out.println("分析失败");
            request.setAttribute("message","分析失败，请联系管理员");
        }finally {
            
            getServletContext().getRequestDispatcher("/TopSalesVolumeSuccess.jsp").forward(
                    request, response);
        }

    }
}