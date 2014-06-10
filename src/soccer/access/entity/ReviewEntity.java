
package soccer.access.entity;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;

@Entity(value = "ReviewInfo", noClassnameStored = true)
public class ReviewEntity {

    @Id
    private ObjectId newsId;

    @Indexed(value = IndexDirection.ASC, name = "date", unique = false, sparse = true)
    private String date;// 比赛时间

    private String path;// 复盘路径

    private String title;// 标题
    private String content;// 内容
    private String comment;// 备注

    private String chupan;// 初始盘口
    
    private String zhongpan;// 终盘

    private String trend;// 盘口走向
    /**
     * 升盘; 
     * 降盘; 
     * U型盘口振荡； 
     * 反U型盘口振荡； 
     * 盘不动U型水位振荡; 
     * 盘不动反U型水位振荡; 
     * 盘不动水位线性上调; 
     * 盘不动水位线性下调; 
     * 其他
     */

    private String selfcommet;// 个人总结盘形
    /**
     * 诱盘 
     * 阻盘 
     * 笋盘 
     * 正常盘 
     * 其他
     */
    

    private String zhuchang;// 默认开盘主让/客让


    private String rshengfu;// 让球方胜平负
    private String pshengfu;// 盘口赢走输
    private String bifen;// 让球方比分（含半场）
    private String bshengfu;// 半全场胜负
    private String daxiao;// 大小球以2.5为例
    
    
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
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getChupan() {
        return chupan;
    }
    public void setChupan(String chupan) {
        this.chupan = chupan;
    }
    public String getZhongpan() {
        return zhongpan;
    }
    public void setZhongpan(String zhongpan) {
        this.zhongpan = zhongpan;
    }
    public String getTrend() {
        return trend;
    }
    public void setTrend(String trend) {
        this.trend = trend;
    }
    public String getSelfcommet() {
        return selfcommet;
    }
    public void setSelfcommet(String selfcommet) {
        this.selfcommet = selfcommet;
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
    public String getZhuchang() {
        return zhuchang;
    }
    public void setZhuchang(String zhuchang) {
        this.zhuchang = zhuchang;
    }
    public String getRshengfu() {
        return rshengfu;
    }
    public void setRshengfu(String rshengfu) {
        this.rshengfu = rshengfu;
    }
    public String getPshengfu() {
        return pshengfu;
    }
    public void setPshengfu(String pshengfu) {
        this.pshengfu = pshengfu;
    }
    public String getBifen() {
        return bifen;
    }
    public void setBifen(String bifen) {
        this.bifen = bifen;
    }
    public String getBshengfu() {
        return bshengfu;
    }
    public void setBshengfu(String bshengfu) {
        this.bshengfu = bshengfu;
    }
    public String getDaxiao() {
        return daxiao;
    }
    public void setDaxiao(String daxiao) {
        this.daxiao = daxiao;
    }
    @Override
    public String toString() {
        return "ReviewEntity [newsId=" + newsId + ", date=" + date + ", path=" + path + ", title="
                + title + ", content=" + content + ", comment=" + comment + ", chupan=" + chupan
                + ", zhongpan=" + zhongpan + ", trend=" + trend + ", selfcommet=" + selfcommet
                + ", zhuchang=" + zhuchang + ", rshengfu=" + rshengfu + ", pshengfu=" + pshengfu
                + ", bifen=" + bifen + ", bshengfu=" + bshengfu + ", daxiao=" + daxiao + "]";
    } 

}
