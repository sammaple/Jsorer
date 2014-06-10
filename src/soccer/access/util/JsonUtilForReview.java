
package soccer.access.util;

import java.io.UnsupportedEncodingException;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import soccer.access.entity.ReviewEntity;

public class JsonUtilForReview {

    private static final Logger logger = Logger.getLogger(JsonUtilForReview.class);

    public static String TYPE = "type";

    public static String NEWSID = "newsid";

    public static String DATE = "date";
    public static String PATH = "path";// 复盘路径

    public static String TITLE = "title";
    public static String CONTENT = "content";
    public static String COMMENT = "comment";

    public static String CHUPAN = "chupan";
    public static String ZHONGPAN = "zhongpan";
    public static String TREND = "trend";
    public static String SELFCOMMET = "selfcommet";

    public static String ZHUCHANG = "zhuchang";

    public static String RSHENGFU = "rshengfu";// 让球方胜平负
    public static String PSHENGFU = "pshengfu";// 盘口赢走输
    public static String BIFEN = "bifen";// 比分（含半场）
    public static String BSHENGFU = "bshengfu";// 半全场胜负
    public static String DAXIAO = "daxiao";// 大小球以2.5为例

    public static String IGNORE = "任意";
    

    public static String getReviewRecord(ReviewEntity temp)
            throws UnsupportedEncodingException
    {
        ObjectId newsId;

        String date;// 比赛时间
        String path;// 复盘路径

        String title;// 标题
        String content;// 内容
        String comment;// 备注

        String chupan;// 初始盘口
        String zhongpan;// 终盘
        String trend;// 盘口走向
        String selfcommet;// 个人总结盘形

        String zhuchang;// 默认开盘主让/客让

        String rshengfu;// 让球方胜平负
        String pshengfu;// 盘口赢走输
        String bifen;// 让球方比分（含半场）
        String bshengfu;// 半全场胜负
        String daxiao;// 大小球以2.5为例

        newsId = temp.getNewsId();
        date = temp.getDate();
        path = temp.getPath();
        title = temp.getTitle();
        content = temp.getContent();
        comment = temp.getComment();
        chupan = temp.getChupan();
        zhongpan = temp.getZhongpan();
        trend = temp.getTrend();
        selfcommet = temp.getSelfcommet();

        zhuchang = temp.getZhuchang();

        rshengfu = temp.getRshengfu();// 让球方胜平负
        pshengfu = temp.getPshengfu();// 盘口赢走输
        bifen = temp.getBifen();// 比分（含半场）
        bshengfu = temp.getBshengfu();// 半全场胜负
        daxiao = temp.getDaxiao();// 大小球以2.5为例

        JSONObject json_org = new JSONObject();

        logger.debug("ObjectId is :" + newsId.toString());
        buildContactsJSONString(json_org, NEWSID, newsId.toString());
        buildContactsJSONString(json_org, DATE, date);

        buildContactsJSONString(json_org, PATH, path);
        buildContactsJSONString(json_org, TITLE, title);
        buildContactsJSONString(json_org, CONTENT, content);
        buildContactsJSONString(json_org, COMMENT, comment);

        buildContactsJSONString(json_org, CHUPAN, chupan);

        buildContactsJSONString(json_org, ZHONGPAN, zhongpan);
        buildContactsJSONString(json_org, TREND, trend);
        buildContactsJSONString(json_org, SELFCOMMET, selfcommet);

        buildContactsJSONString(json_org, ZHUCHANG, zhuchang);

        buildContactsJSONString(json_org, RSHENGFU, rshengfu);
        buildContactsJSONString(json_org, PSHENGFU, pshengfu);
        buildContactsJSONString(json_org, BIFEN, bifen);
        buildContactsJSONString(json_org, BSHENGFU, bshengfu);
        buildContactsJSONString(json_org, DAXIAO, daxiao);

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
