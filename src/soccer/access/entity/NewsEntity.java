
package soccer.access.entity;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;

@Entity(value = "NewsInfo", noClassnameStored = true)
public class NewsEntity {

    @Id
    private ObjectId newsId;

    @Indexed(value = IndexDirection.ASC, name = "league", unique = false, sparse = true)
    private String league;// 联赛
    /*
     * 0 中超 ;1 日职业 ;2 日乙 ;3 韩职; 4 日本天皇杯 ;5 亚洲杯; 6 亚冠; 7 英超; 8 英冠; 9 英甲; 10 英联赛杯;
     * 11 英社区盾杯; 12 德甲; 13 意甲; 14 西甲; 15 法甲; 16 世界杯; 17 欧洲杯; 18 欧冠; 19 欧洲联赛; 20
     * 荷甲; 21 法乙; 22 荷乙; 23 瑞典甲; 24 挪威甲;25 美职联;26 美公开赛;27 德乙;28 葡超;29 苏格兰超;30
     * 巴甲;31 阿甲;32 其他
     */

    @Indexed(value = IndexDirection.ASC, name = "teamName", unique = false, sparse = true)
    private String teamName;// 球队名称

    private int season;// 赛季 暂时由服务器侧提供
    /* 2012 表示2012-2013赛季 */

    @Indexed(value = IndexDirection.ASC, name = "date", unique = false, sparse = true)
    private String date;// 时间 暂时由服务器侧提供 单位ms

    private String type;// 新闻类型
    /**
     * 0 资讯; 1 八卦; 2 伤停; 3 转会; 4 比赛预测; 5 其他
     */

    private int rank;// 新闻星级
    /**
     * 5 最高; 4 3 2 1
     */

    /**
     * 20最高
     */

    private String title;// 标题
    private String keyword;// 关键字或者标签
    private String content;// 内容
    private String comment;// 备注


    private String pic;// 图片（暂时还是存服务器侧路径吧）
    private String from;// 新闻来源

    private boolean isWPrediction;// 该场比赛是否需要复盘
    private String prediction;// 比赛预测
    private int factor;// 如果有比赛预测的话，保险系数(16-20)

    private boolean isReview;// 该场比赛是否需要复盘
    private String reviewInfo;// 比赛分析重点

    private String extStr_1;// 字符串预留1
    private String extStr_2;// 字符串预留2
    private String extStr_3;// 字符串预留3
    private String extStr_4;// 字符串预留4
    private String extStr_5;// 字符串预留5
    private String extStr_6;// 字符串预留6

    private String extInt_1;// 数字预留1
    private String extInt_2;// 数字预留2
    private String extInt_3;// 数字预留3
    private String extInt_4;// 数字预留4
    private String extInt_5;// 数字预留5
    private String extInt_6;// 数字预留6

    public ObjectId getNewsId() {
        return newsId;
    }

    public void setNewsId(ObjectId newsId) {
        this.newsId = newsId;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getFactor() {
        return factor;
    }

    public void setFactor(int factor) {
        this.factor = factor;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isReview() {
        return isReview;
    }

    public void setReview(boolean isReview) {
        this.isReview = isReview;
    }

    public String getReviewInfo() {
        return reviewInfo;
    }

    public void setReviewInfo(String reviewInfo) {
        this.reviewInfo = reviewInfo;
    }

    public String getExtStr_1() {
        return extStr_1;
    }

    public void setExtStr_1(String extStr_1) {
        this.extStr_1 = extStr_1;
    }

    public String getExtStr_2() {
        return extStr_2;
    }

    public void setExtStr_2(String extStr_2) {
        this.extStr_2 = extStr_2;
    }

    public String getExtStr_3() {
        return extStr_3;
    }

    public void setExtStr_3(String extStr_3) {
        this.extStr_3 = extStr_3;
    }

    public String getExtStr_4() {
        return extStr_4;
    }

    public void setExtStr_4(String extStr_4) {
        this.extStr_4 = extStr_4;
    }

    public String getExtStr_5() {
        return extStr_5;
    }

    public void setExtStr_5(String extStr_5) {
        this.extStr_5 = extStr_5;
    }

    public String getExtStr_6() {
        return extStr_6;
    }

    public void setExtStr_6(String extStr_6) {
        this.extStr_6 = extStr_6;
    }

    public String getExtInt_1() {
        return extInt_1;
    }

    public void setExtInt_1(String extInt_1) {
        this.extInt_1 = extInt_1;
    }

    public String getExtInt_2() {
        return extInt_2;
    }

    public void setExtInt_2(String extInt_2) {
        this.extInt_2 = extInt_2;
    }

    public String getExtInt_3() {
        return extInt_3;
    }

    public void setExtInt_3(String extInt_3) {
        this.extInt_3 = extInt_3;
    }

    public String getExtInt_4() {
        return extInt_4;
    }

    public void setExtInt_4(String extInt_4) {
        this.extInt_4 = extInt_4;
    }

    public String getExtInt_5() {
        return extInt_5;
    }

    public void setExtInt_5(String extInt_5) {
        this.extInt_5 = extInt_5;
    }

    public String getExtInt_6() {
        return extInt_6;
    }

    public void setExtInt_6(String extInt_6) {
        this.extInt_6 = extInt_6;
    }

    public boolean isWPrediction() {
		return isWPrediction;
	}

	public void setWPrediction(boolean isWPrediction) {
		this.isWPrediction = isWPrediction;
	}

	public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public String toString() {
        return "NewsEntity [newsId=" + newsId + ", league=" + league + ", teamName=" + teamName
                + ", season=" + season + ", date=" + date + ", type=" + type + ", rank=" + rank
                + ", title=" + title + ", keyword=" + keyword + ", content=" + content
                + ", comment=" + comment + ", pic=" + pic + ", from=" + from + ", isWPrediction="
                + isWPrediction + ", prediction=" + prediction + ", factor=" + factor
                + ", isReview=" + isReview + ", reviewInfo=" + reviewInfo + "]";
    }
}
