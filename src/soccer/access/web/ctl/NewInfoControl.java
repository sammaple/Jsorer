
package soccer.access.web.ctl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.util.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import soccer.access.dao.GeneralInfoDao;
import soccer.access.entity.GeneralInfoEntity;
import soccer.access.entity.NewsEntity;
import soccer.access.entity.WeiboInfo;
import soccer.access.interfaces.IFileDao;
import soccer.access.interfaces.IWeiboDao;
import soccer.access.server.NewsInfoServer;
import soccer.access.server.WeiboInfoServer;
import soccer.access.util.JsonUtil;
import soccer.weibo.PushMessageSender;
import weibo4j.Oauth;
import weibo4j.http.AccessToken;
import weibo4j.model.WeiboException;

@Controller
public class NewInfoControl {

    private static final Log logger = LogFactory.getLog(NewInfoControl.class);// LOG4J打印

    @Autowired
    NewsInfoServer newsInfoServer;

    @Autowired
    WeiboInfoServer weiboInfoServer;

    @Autowired
    GeneralInfoDao generalInfoDao;

    @Autowired
    IFileDao fileDao;

    @Autowired
    IWeiboDao weiboDao;

    private boolean IsStrNotEmpty(HttpServletRequest request, String str) {

        if (request.getParameter(str) == null) {
            return false;
        }

        if (request.getParameter(str).isEmpty()) {
            return false;
        }

        return true;
    }

    private boolean transEntity(HttpServletRequest request, NewsEntity entity)
            throws UnsupportedEncodingException {

        if (IsStrNotEmpty(request, JsonUtil.LEAGUE)) {
            entity.setLeague(new String(request.getParameter(JsonUtil.LEAGUE)
                    .getBytes("iso8859-1"), "utf-8"));
        }

        if (IsStrNotEmpty(request, JsonUtil.TEAMNAME)) {
            entity.setTeamName(new String(request.getParameter(
                    JsonUtil.TEAMNAME).getBytes("iso8859-1"), "utf-8"));
        }

        if (IsStrNotEmpty(request, JsonUtil.TYPE)) {
            entity.setType(new String(request.getParameter(JsonUtil.TYPE)
                    .getBytes("iso8859-1"), "utf-8"));
        }

        if (IsStrNotEmpty(request, JsonUtil.TITLE)) {
            entity.setTitle(new String(request.getParameter(JsonUtil.TITLE)
                    .getBytes("iso8859-1"), "utf-8"));
        }

        if (IsStrNotEmpty(request, JsonUtil.KEYWORD)) {
            entity.setKeyword(new String(request.getParameter(JsonUtil.KEYWORD)
                    .getBytes("iso8859-1"), "utf-8"));
        }

        if (IsStrNotEmpty(request, JsonUtil.CONTENT)) {
            entity.setContent(new String(request.getParameter(JsonUtil.CONTENT)
                    .getBytes("iso8859-1"), "utf-8"));
        }

        if (IsStrNotEmpty(request, JsonUtil.COMMENT)) {
            entity.setComment(new String(request.getParameter(JsonUtil.COMMENT)
                    .getBytes("iso8859-1"), "utf-8"));
        }

        if (IsStrNotEmpty(request, JsonUtil.PREDICTION)) {
            entity.setPrediction(new String(request.getParameter(
                    JsonUtil.PREDICTION).getBytes("iso8859-1"), "utf-8"));
        }

        if (IsStrNotEmpty(request, JsonUtil.REVIEWINFO)) {
            entity.setReviewInfo(new String(request.getParameter(
                    JsonUtil.REVIEWINFO).getBytes("iso8859-1"), "utf-8"));
        }

        if (IsStrNotEmpty(request, JsonUtil.FROM)) {
            entity.setFrom(new String(request.getParameter(JsonUtil.FROM)
                    .getBytes("iso8859-1"), "utf-8"));
        }

        logger.debug("========" + request.getParameter(JsonUtil.ISPREDIC));
        logger.debug("========" + request.getParameter(JsonUtil.ISREVIEW));

        if (IsStrNotEmpty(request, JsonUtil.ISPREDIC)) {
            if (request.getParameter(JsonUtil.ISPREDIC).equals("true")) {
                entity.setWPrediction(true);
            }
        }

        if (IsStrNotEmpty(request, JsonUtil.ISREVIEW)) {
            if (request.getParameter(JsonUtil.ISREVIEW).equals("true")) {
                entity.setReview(true);
            }
        }

        return true;

    }

    @RequestMapping("/delete_newinfo")
    @ResponseBody
    public String handleDeleteRequest(HttpServletRequest request,
            HttpServletResponse response) throws UnsupportedEncodingException {

        String deleteString = request.getParameter(JsonUtil.NEWSID);

        logger.debug("newsid is: " + deleteString);

        newsInfoServer.delNewInfo(deleteString);

        return JsonUtil.SendJsonResponse(true, "success");
    }

    @RequestMapping("/query_newinfo")
    @ResponseBody
    public String handleQueryRequest(HttpServletRequest request,
            HttpServletResponse response) throws UnsupportedEncodingException {

        String queryString = request.getParameter(JsonUtil.TYPE);

        ArrayList<NewsEntity> entitylist = new ArrayList<NewsEntity>();
        int querysum = -1;

        if (queryString.equals("all")) {
            logger.info("====query_newinfo====1==" + queryString);
            querysum = newsInfoServer.queryNewInfo(-1, entitylist);
        } else if (queryString.equals("offset")) {
            String pageindex = request.getParameter("page");
            logger.info("====query_newinfo====2==" + queryString + " " + pageindex);

            querysum = newsInfoServer.queryNewInfo(Integer.valueOf(pageindex), entitylist);
        } else if (queryString.equals("param")) {

            String pageindex = request.getParameter("page");
            String teamName = request.getParameter("teamName");
            logger.info("====query_newinfo====3==" + queryString + " " + teamName);

            querysum = newsInfoServer.queryNewInfoByParam(teamName, Integer.valueOf(pageindex),
                    entitylist);
        }
        else {
            logger.error("=======invalidate parameters!");
        }

        String jsonString = "";
        String jsonString_ex = "";

        logger.debug("@=======handleQueryRequest11");

        if (entitylist.isEmpty()) {
            jsonString = "[" + "{\"sum\":\"" + 0 + "\" }" + "]";
            return new String(jsonString.getBytes("utf-8"), "ISO-8859-1");
        }

        logger.debug("@=======handleQueryRequest22");

        for (NewsEntity entity : entitylist) {
            String tmp = JsonUtil.getNewsRecord(entity);
            jsonString += (tmp + ",");
            tmp = null;
        }

        if ("".equals(jsonString)) {// 字符串为空，处理
            logger.debug("@=======handleQueryRequest" + "===nolist===>");
            jsonString = "[" + "{\"sum\":\"" + 0 + "\" }" + "]";
        } else {
            String str = "";
            str = "{\"sum\":\"" + querysum + "\" }";

            jsonString_ex = "[" + str + ","
                    + jsonString.substring(0, (jsonString.length() - 1)) + "]";
        }

        return new String(jsonString_ex.getBytes("utf-8"), "ISO-8859-1");
    }

    @RequestMapping("/add_newinfo")
    @ResponseBody
    public String handleAddRequest(NewsEntity entity,
            HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException {

        // NewsEntity entity = new NewsEntity();
        // transEntity(request, entity);

        logger.debug("before add is" + entity);

        if (IsStrNotEmpty(request, JsonUtil.ISPREDIC)) {
            if (request.getParameter(JsonUtil.ISPREDIC).equals("true")) {
                entity.setWPrediction(true);
            }
        }

        if (IsStrNotEmpty(request, JsonUtil.ISREVIEW)) {
            if (request.getParameter(JsonUtil.ISREVIEW).equals("true")) {
                entity.setReview(true);
            }
        }

        newsInfoServer.addNewInfo(entity);

        logger.debug("after add is" + entity);

        return JsonUtil.SendJsonResponse(true, "success");

    }

    @RequestMapping("/updatepic")
    @ResponseBody
    public String updatepic(HttpServletRequest request,
            HttpServletResponse response) throws UnsupportedEncodingException {

        logger.debug("updatepic processing!");

        boolean ret = true;
        String fileId = "";
        String reduceFileId = "";
        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            // 获得文件：
            MultipartFile file = multipartRequest.getFile("fileToUpload");
            // 获得文件名：
            String filename = file.getOriginalFilename();
            logger.debug("updatepic processing getOriginalFilename: " + filename);
            // 获得输入流：
            InputStream input = file.getInputStream();
            logger.debug("updatepic processing input: " + input);
            // 写入文件

            String fileName = "" + UUID.randomUUID();
            logger.debug("updatepic processing fileName: " + fileName);
            String fileReduceName_large = "" + UUID.randomUUID();
            String fileReduceName = "" + UUID.randomUUID();

            File tempFile_dir = new File("./tempFile/");
            if (!tempFile_dir.exists()) {
                tempFile_dir.mkdirs();
            }

            File tempFile = new File("./tempFile/" + fileName);
            if (!tempFile.exists()) {
                tempFile.createNewFile();
            }
            FileOutputStream stream = new FileOutputStream(tempFile);

            byte temp[] = new byte[1024];
            int len = 0;
            while ((len = input.read(temp, 0, 1024)) != -1) {
                stream.write(temp, 0, len);
                logger.debug("updatepic processing 2! ");
            }
            stream.close();

            // Object id = fileDao.saveFile(fileName, "./tempFile/" +fileName);
            // fileId = id.toString();
            // fileId = PicManagerControl.generalReducePic_large(fileDao,
            // "./tempFile/",fileName,fileReduceName_large,"JPEG");
            logger.debug("updatepic processing 3! ");
            reduceFileId = PicManagerControl.generalReducePic(fileDao, "./tempFile/", fileName,
                    fileReduceName, "JPEG");

        } catch (Exception e) {
            ret = false;
            reduceFileId = "文件处理异常";

            StringWriter sb = new StringWriter();
            PrintWriter writer = new PrintWriter(sb);
            e.printStackTrace(writer);
            logger.debug("updatepic processing 4! " + sb.toString());
        }

        if (reduceFileId == null || reduceFileId.isEmpty()) {
            ret = false;
            reduceFileId = "reduceFileId 文件处理异常";
        }

        return JsonUtil.SendJsonResponse(ret, reduceFileId);
    }

    /**
     * setGetAccessTokenOk重新设置为TRUE
     * 
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/restartWeiboService")
    @ResponseBody
    public String restartWeiboService()
            throws UnsupportedEncodingException {

        String rS = "true";
        PushMessageSender.getInstance();

        System.out.println("weibo:restartWeiboService尝试重新启动服务!");

        WeiboInfo weibo = weiboDao.find();
        if (weibo != null) {
            weibo.setGetAccessTokenOk(true);
            weiboDao.save(weibo);
        } else {
            rS = "false";
        }

        return new String(rS.getBytes("UTF-8"), "ISO-8859-1");

    }

    /**
     * 获取系统微博的状况
     * 
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/web_getCurrentSystemWeiboStatus")
    @ResponseBody
    public String getCurrentSystemWeiboStatus()
            throws UnsupportedEncodingException {

        StringBuffer buffer = new StringBuffer();
        buffer.append("{");
        buffer.append("\"status\":\"true\",");

        boolean running = PushMessageSender.isThreadRunning();
        StringBuffer content = new StringBuffer("微博状态:");

        WeiboInfo weibo = weiboDao.find();
        weibo.setRunningOk(running);//更新状态
        weiboDao.save(weibo);
        
        if (running) {
            content.append("微博线程运行【正常】;");
        } else {
            content.append("微博线程运行【异常】;");
        }

        if (weibo != null) {
            content.append(weibo.toString());
        }

        buffer.append("\"content\":\"" + Base64.encode(content.toString().getBytes("UTF-8")) + "\"}");

        String rS = Base64.encode(buffer.toString().getBytes("UTF-8"));
        return new String(rS.getBytes("UTF-8"), "ISO-8859-1");

    }

    @RequestMapping("/web_setWeiboCode")
    @ResponseBody
    public String setWeiboCode(String code)
            throws UnsupportedEncodingException {

        StringBuffer buffer = new StringBuffer();

        WeiboInfo weibo = weiboDao.find();
        if (weibo == null) {
            weibo = new WeiboInfo();
        }
        weibo.setLast_code(code);
        Date date = new Date();
        weibo.setLast_setcode_time(String.valueOf(date.getTime()));

        System.out.println("setWeiboCode code: " + code);
        Oauth oauth = new Oauth();
        AccessToken accessToken = null;
        try {
            accessToken = oauth.getAccessTokenByCode(code);
            buffer.append("设置code,获取accessToken成功！");
            weibo.setAccess_token(accessToken.getAccessToken());
            weibo.setLast_getAccessTokenSuccessful_time(String.valueOf(date.getTime()));
            long tt = date.getTime() + Long.valueOf(accessToken.getExpireIn());
            weibo.setAccess_token_expired_time(String.valueOf(tt));

            weibo.setWeibo_uid(accessToken.getUid());

            weibo.setGetAccessTokenOk(true);

        } catch (WeiboException e) {
            e.printStackTrace();
            buffer.append("设置code,获取accessToken失败！");
            weibo.setGetAccessTokenOk(false);
        }
        weiboDao.save(weibo);

        System.out.println("accessToken:打印结果" + accessToken);
        System.out.println("weibo:打印结果" + weibo);

        return new String(buffer.toString().getBytes("UTF-8"), "ISO-8859-1");

    }

    @RequestMapping("/delete_weiboinfo")
    @ResponseBody
    public String handleWeiboDeleteRequest(HttpServletRequest request,
            HttpServletResponse response) throws UnsupportedEncodingException {

        String deleteString = request.getParameter(JsonUtil.NEWSID);

        logger.debug("delete_weiboinfo newsid is: " + deleteString);

        weiboInfoServer.delWeiboInfo(deleteString);

        return JsonUtil.SendJsonResponse(true, "success");
    }

    @RequestMapping("/query_weiboinfo")
    @ResponseBody
    public String handleWeiboQueryRequest(HttpServletRequest request,
            HttpServletResponse response) throws UnsupportedEncodingException {

        String queryString = request.getParameter(JsonUtil.TYPE);

        ArrayList<GeneralInfoEntity> entitylist = new ArrayList<GeneralInfoEntity>();
        int querysum = -1;

        if (queryString.equals("all")) {
            logger.info("====query_newinfo====1==" + queryString);
            querysum = weiboInfoServer.queryNewInfo(-1, entitylist);
        } else if (queryString.equals("offset")) {
            String pageindex = request.getParameter("page");
            logger.info("====query_newinfo====2==" + queryString + " " + pageindex);

            querysum = weiboInfoServer.queryNewInfo(Integer.valueOf(pageindex), entitylist);
        } else if (queryString.equals("param")) {

            String pageindex = request.getParameter("page");
            String teamName = request.getParameter("teamName");
            logger.info("====query_newinfo====3==" + queryString + " " + teamName);

            querysum = weiboInfoServer.queryNewInfoByParam(teamName, Integer.valueOf(pageindex),
                    entitylist);
        }
        else {
            logger.error("=======invalidate parameters!");
        }

        String jsonString = "";
        String jsonString_ex = "";

        logger.debug("@=======handleQueryRequest11");

        if (entitylist.isEmpty()) {
            jsonString = "[" + "{\"sum\":\"" + 0 + "\" }" + "]";
            return new String(jsonString.getBytes("utf-8"), "ISO-8859-1");
        }

        logger.debug("@=======handleQueryRequest22");

        for (GeneralInfoEntity entity : entitylist) {
            String tmp = JsonUtil.getWeiboRecord(entity);
            jsonString += (tmp + ",");
            tmp = null;
        }

        if ("".equals(jsonString)) {// 字符串为空，处理
            logger.debug("@=======handleQueryRequest" + "===nolist===>");
            jsonString = "[" + "{\"sum\":\"" + 0 + "\" }" + "]";
        } else {
            String str = "";
            str = "{\"sum\":\"" + querysum + "\" }";

            jsonString_ex = "[" + str + ","
                    + jsonString.substring(0, (jsonString.length() - 1)) + "]";
        }

        return new String(jsonString_ex.getBytes("utf-8"), "ISO-8859-1");
    }

    @RequestMapping("/convertTime")
    @ResponseBody
    public String convertTime(HttpServletRequest request,
            HttpServletResponse response) throws UnsupportedEncodingException, ParseException {

	    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
	    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
	    
	    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
	    sdf2.setTimeZone(TimeZone.getTimeZone("GMT"));

        ArrayList<GeneralInfoEntity> entitylist = new ArrayList<GeneralInfoEntity>();
	    generalInfoDao.queryAll(entitylist);
	    
	    for(GeneralInfoEntity entity:entitylist){
		    String str = entity.getTime();
		    Date date = sdf.parse(str);
		    entity.setTime(sdf2.format(date));
		    
		    generalInfoDao.save(entity);
	    }
	    
	    
	    return "ok";
    }
    
}
