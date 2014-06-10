
package soccer.access.web.ctl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.code.morphia.query.Query;

import soccer.access.entity.ReviewEntity;
import soccer.access.server.ReviewInfoServer;
import soccer.access.util.JsonUtil;
import soccer.access.util.JsonUtilForReview;

@Controller
public class ReviewInfoControl {

    private static final Log logger = LogFactory.getLog(ReviewInfoControl.class);// LOG4J打印

    @Autowired
    ReviewInfoServer reviewInfoServer;

    private JSONObject json_org = new JSONObject();;

    private boolean IsStrNotEmpty(HttpServletRequest request, String str) {

        if (request.getParameter(str) == null) {
            return false;
        }

        if (request.getParameter(str).isEmpty()) {
            return false;
        }

        return true;
    }

    private boolean transEntity(HttpServletRequest request, ReviewEntity entity)
            throws UnsupportedEncodingException {

        if (IsStrNotEmpty(request, JsonUtilForReview.TITLE)) {
            entity.setTitle(new String(request.getParameter(JsonUtilForReview.TITLE)
                    .getBytes("iso8859-1"), "utf-8"));
        }

        if (IsStrNotEmpty(request, JsonUtilForReview.CONTENT)) {
            entity.setContent(new String(request.getParameter(
                    JsonUtilForReview.CONTENT).getBytes("iso8859-1"), "utf-8"));
        }

        if (IsStrNotEmpty(request, JsonUtilForReview.COMMENT)) {
            entity.setComment(new String(request.getParameter(JsonUtilForReview.COMMENT)
                    .getBytes("iso8859-1"), "utf-8"));
        }

        if (IsStrNotEmpty(request, JsonUtilForReview.PATH)) {
            entity.setPath(new String(request.getParameter(JsonUtilForReview.PATH)
                    .getBytes("iso8859-1"), "utf-8"));
        }

        if (IsStrNotEmpty(request, JsonUtilForReview.CHUPAN)) {
            entity.setChupan(new String(request.getParameter(JsonUtilForReview.CHUPAN)
                    .getBytes("iso8859-1"), "utf-8"));
        }

        if (IsStrNotEmpty(request, JsonUtilForReview.ZHONGPAN)) {
            entity.setZhongpan(new String(request.getParameter(JsonUtilForReview.ZHONGPAN)
                    .getBytes("iso8859-1"), "utf-8"));
        }

        if (IsStrNotEmpty(request, JsonUtilForReview.TREND)) {
            entity.setTrend(new String(request.getParameter(JsonUtilForReview.TREND)
                    .getBytes("iso8859-1"), "utf-8"));
        }

        if (IsStrNotEmpty(request, JsonUtilForReview.SELFCOMMET)) {
            entity.setSelfcommet(new String(request.getParameter(
                    JsonUtilForReview.SELFCOMMET).getBytes("iso8859-1"), "utf-8"));
        }

        return true;

    }

    @RequestMapping("/delete_reviewinfo")
    @ResponseBody
    public String handleDeleteRequest(HttpServletRequest request,
            HttpServletResponse response) throws UnsupportedEncodingException {

        String deleteString = request.getParameter(JsonUtilForReview.NEWSID);

        logger.debug("newsid is: " + deleteString);

        reviewInfoServer.delReviewInfo(deleteString);

        return JsonUtil.SendJsonResponse(true, "success");
    }

    private void putJson(JSONObject jObject, HttpServletRequest request, String str,
            String queryIgnoreString) {

        if (request.getParameter(str).isEmpty()
                || request.getParameter(str).equals(queryIgnoreString)) {
            return;
        }

        jObject.put(str, request.getParameter(str));
    }

    @RequestMapping("/query_reviewinfo")
    @ResponseBody
    public String handleQueryRequest(HttpServletRequest request,
            HttpServletResponse response) throws UnsupportedEncodingException {

        String queryString = request.getParameter(JsonUtilForReview.TYPE);

        ArrayList<ReviewEntity> entitylist = new ArrayList<ReviewEntity>();
        int querysum = -1;

        if (queryString.equals("all")) {
            logger.info("====query_reviewinfo====1==" + queryString);
            querysum = reviewInfoServer.queryReviewInfo(-1, entitylist);
        } else if (queryString.equals("offset")) {
            String pageindex = request.getParameter("page");
            logger.info("====query_reviewinfo====2==" + queryString + " index:" + pageindex);

            querysum = reviewInfoServer.queryReviewInfo(Integer.valueOf(pageindex), entitylist);
        } else if (queryString.equals("param")) {

            String pageindex = request.getParameter("page");
            String teamName = request.getParameter(JsonUtilForReview.TITLE);
            logger.info("====query_reviewinfo====3==" + queryString + " index:" + pageindex
                    + " querytitle:"
                    + teamName);

            putJson(json_org, request, JsonUtilForReview.TITLE, JsonUtilForReview.IGNORE);
            
            putJson(json_org, request, JsonUtilForReview.ZHUCHANG, JsonUtilForReview.IGNORE);
            putJson(json_org, request, JsonUtilForReview.CHUPAN, JsonUtilForReview.IGNORE);
            putJson(json_org, request, JsonUtilForReview.ZHONGPAN, JsonUtilForReview.IGNORE);
            putJson(json_org, request, JsonUtilForReview.TREND, JsonUtilForReview.IGNORE);

            querysum = reviewInfoServer.queryReviewInfoByParam(json_org,
                    Integer.valueOf(pageindex), entitylist);

            logger.debug("json_org str========"+json_org.toString());
            json_org.clear();
        }
        else {
            logger.error("=======invalidate parameters!");
        }

        String jsonString = "";
        String jsonString_ex = "";

        logger.debug("@====query_reviewinfo===handleQueryRequest11");

        if (entitylist.isEmpty()) {
            jsonString = "[" + "{\"sum\":\"" + 0 + "\" }"+ "]";
            return new String(jsonString.getBytes("utf-8"), "ISO-8859-1");
        }

        logger.debug("@====query_reviewinfo===handleQueryRequest22");

        for (ReviewEntity entity : entitylist) {
            String tmp = JsonUtilForReview.getReviewRecord(entity);
            jsonString += (tmp + ",");
            tmp = null;
        }

        if ("".equals(jsonString)) {// 字符串为空，处理
            logger.debug("@====query_reviewinfo===handleQueryRequest" + "===nolist===>");
            jsonString = "[" + "{\"sum\":\"" + 0 + "\" }"+ "]";
        } else {
            String str = "";
            str = "{\"sum\":\"" + querysum + "\" }";

            jsonString_ex = "[" + str + ","
                    + jsonString.substring(0, (jsonString.length() - 1)) + "]";
        }

        return new String(jsonString_ex.getBytes("utf-8"), "ISO-8859-1");
    }

    @RequestMapping("/add_reviewinfo")
    @ResponseBody
    public String handleAddRequest(ReviewEntity entity,
            HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException {

        // NewsEntity entity = new NewsEntity();
        // transEntity(request, entity);
        // URIEncoding="utf-8" 就可以不用字符转换了

        logger.debug("before add is" + entity);

        reviewInfoServer.addRiviewInfo(entity);

        logger.debug("after add is" + entity);

        return JsonUtil.SendJsonResponse(true, "success");

    }

}
