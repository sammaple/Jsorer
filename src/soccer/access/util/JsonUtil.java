
package soccer.access.util;

import java.io.UnsupportedEncodingException;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import soccer.access.entity.GeneralInfoEntity;
import soccer.access.entity.NewsEntity;

public class JsonUtil {

    private static final Logger logger = Logger.getLogger(JsonUtil.class);
    public static String LEAGUE = "league";
    public static String TEAMNAME = "teamName";
    public static String TYPE = "type";
    public static String TITLE = "title";
    public static String KEYWORD = "keyword";
    public static String CONTENT = "content";
    public static String COMMENT = "comment";
    public static String PREDICTION = "prediction";
    public static String REVIEWINFO = "reviewInfo";
    public static String DATE = "date";
    public static String PIC = "pic";

    public static String FROM = "from";
    public static String FACTOR = "factor";
    public static String RANK = "rank";

    public static String NEWSID = "newsid";

    public static String ISPREDIC = "isWPrediction";
    public static String ISREVIEW = "isReview";

    /**
     * 发送失败时的Http响应
     * 
     * @param response Http响应
     * @throws UnsupportedEncodingException
     */
    public static String SendJsonResponse(Boolean successful, String title)
            throws UnsupportedEncodingException {

        String str = "";

        str = "{\"successful\":\"" + successful + "\",\"title\":\"" + title + "\" }";

        return new String(str.getBytes("utf-8"), "ISO-8859-1");

    }

    public static String getNewsRecord(NewsEntity temp)
            throws UnsupportedEncodingException
    {
        // String str = null;
        ObjectId newsId;
        String league;// 联赛

        String teamName;// 球队名称

        int season;// 赛季 暂时由服务器侧提供
        /* 2012 表示2012-2013赛季 */

        String date;// 时间 暂时由服务器侧提供 单位ms

        String type;// 新闻类型
        /**
         * 0 资讯; 1 八卦; 2 伤停; 3 转会; 4 比赛预测; 5 其他
         */

        int rank;// 新闻星级
        /**
         * 5 最高; 4 3 2 1
         */

        String title;// 标题
        String keyword;// 关键字或者标签
        String content;// 内容
        String comment;// 备注

        String pic;// 图片（暂时还是存服务器侧路径吧）
        String from;// 新闻来源

        boolean isPrediction;// 该场比赛是否需要复盘
        String prediction;// 比赛预测
        int factor;// 如果有比赛预测的话，保险系数(16-20)

        boolean isReview;// 该场比赛是否需要复盘
        String reviewInfo;// 比赛分析重点

        newsId = temp.getNewsId();
        league = temp.getLeague();
        teamName = temp.getTeamName();
        date = temp.getDate();
        type = temp.getType();
        title = temp.getTitle();
        keyword = temp.getKeyword();
        content = temp.getContent();
        comment = temp.getComment();
        pic = temp.getPic();
        prediction = temp.getPrediction();
        reviewInfo = temp.getReviewInfo();
        from = temp.getFrom();

        rank = temp.getRank();
        factor = temp.getFactor();

        isPrediction = temp.isWPrediction();
        isReview = temp.isReview();

        JSONObject json_org = new JSONObject();

        logger.debug("ObjectId is :" + newsId.toString());
        buildContactsJSONString(json_org, NEWSID, newsId.toString());
        buildContactsJSONString(json_org, ISPREDIC, isPrediction);
        buildContactsJSONString(json_org, ISREVIEW, isReview);

        buildContactsJSONString(json_org, LEAGUE, league);
        buildContactsJSONString(json_org, TEAMNAME, teamName);
        buildContactsJSONString(json_org, TYPE, type);
        buildContactsJSONString(json_org, TITLE, title);
        buildContactsJSONString(json_org, KEYWORD, keyword);
        buildContactsJSONString(json_org, CONTENT, content);
        buildContactsJSONString(json_org, COMMENT, comment);
        buildContactsJSONString(json_org, PREDICTION, prediction);
        buildContactsJSONString(json_org, REVIEWINFO, reviewInfo);

        buildContactsJSONString(json_org, DATE, date);
        buildContactsJSONString(json_org, PIC, pic);

        buildContactsJSONString(json_org, FROM, from);

        buildContactsJSONString(json_org, FACTOR, factor);
        buildContactsJSONString(json_org, RANK, rank);

        return json_org.toString();// new
                                   // String(str.getBytes("utf-8"),"ISO-8859-1");
    }

    /**
     * 微博信息组装
     * 
     * @param temp
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getWeiboRecord(GeneralInfoEntity temp)
            throws UnsupportedEncodingException
    {
        // String str = null;
        ObjectId newsId;

        String date;// 时间 暂时由服务器侧提供 单位ms

        String title;// 标题

        String pic;// 图片（暂时还是存服务器侧路径吧）

        newsId = temp.getId();
        date = temp.getTime();
        title = temp.getTitle();
        pic = temp.getPic();

        JSONObject json_org = new JSONObject();

        logger.debug("ObjectId is :" + newsId.toString());
        buildContactsJSONString(json_org, NEWSID, newsId.toString());
        buildContactsJSONString(json_org, TITLE, title);// BASE64加密内容
        // buildContactsJSONString(json_org, TITLE,
        // Base64.encodeToString(title.getBytes("UTF-8"), Base64.NO_WRAP));//
        // BASE64加密内容

        buildContactsJSONString(json_org, DATE, date);
        buildContactsJSONString(json_org, PIC, pic);

        return json_org.toString();// new
                                   // String(str.getBytes("utf-8"),"ISO-8859-1");
    }

    private static void buildContactsJSONString(JSONObject json_org, String key, Object vaule)
    {
        if (key == null || vaule == null)
        {
            return;
        }
        json_org.put(key, vaule);
    }
}
