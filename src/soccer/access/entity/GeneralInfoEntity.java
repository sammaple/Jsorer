
package soccer.access.entity;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

/**
 * 微博抓取信息表
 * 
 * @author Administrator
 */
@Entity("GeneralInfo")
public class GeneralInfoEntity {
    @Id
    private ObjectId id;

    private String title;

    private String pic;

    private String weibo_uid;// 微博ID

    private String originImageUrl;// 原图片

    private String text;// 目前的设计中title和text就是同一个东西，网页获取的时候只要获取一个就行了

    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getOriginImageUrl() {
        return originImageUrl;
    }

    public void setOriginImageUrl(String originImageUrl) {
        this.originImageUrl = originImageUrl;
    }

    public String getWeibo_uid() {
        return weibo_uid;
    }

    public void setWeibo_uid(String weibo_uid) {
        this.weibo_uid = weibo_uid;
    }

}
