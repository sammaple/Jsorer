
package soccer.access.entity;

import java.text.SimpleDateFormat;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Id;

@Embedded
public class WeiboInfo {

    @Id
    private ObjectId id;

    String last_setcode_time;

    String last_getAccessTokenSuccessful_time;

    String last_syncweibo_time;

    String access_token;// weibo token

    String access_token_expired_time;

    String last_code;// 最近一次的weibo code

    String weibo_uid;// 最近一次更新的uid,必须是更新成功以后才会赋值

    boolean isRunningOk;

    boolean getAccessTokenOk;
    
    String lastErrorReason;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getLast_setcode_time() {
        return last_setcode_time;
    }

    public void setLast_setcode_time(String last_setcode_time) {
        this.last_setcode_time = last_setcode_time;
    }

    public String getLast_syncweibo_time() {
        return last_syncweibo_time;
    }

    public void setLast_syncweibo_time(String last_syncweibo_time) {
        this.last_syncweibo_time = last_syncweibo_time;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getLast_code() {
        return last_code;
    }

    public void setLast_code(String last_code) {
        this.last_code = last_code;
    }

    public boolean isRunningOk() {
        return isRunningOk;
    }

    public void setRunningOk(boolean isRunningOk) {
        this.isRunningOk = isRunningOk;
    }

    public boolean isGetAccessTokenOk() {
        return getAccessTokenOk;
    }

    public void setGetAccessTokenOk(boolean getAccessTokenOk) {
        this.getAccessTokenOk = getAccessTokenOk;
    }

    public String getAccess_token_expired_time() {
        return access_token_expired_time;
    }

    public void setAccess_token_expired_time(String access_token_expired_time) {
        this.access_token_expired_time = access_token_expired_time;
    }

    public String getLast_getAccessTokenSuccessful_time() {
        return last_getAccessTokenSuccessful_time;
    }

    public void setLast_getAccessTokenSuccessful_time(String last_getAccessTokenSuccessful_time) {
        this.last_getAccessTokenSuccessful_time = last_getAccessTokenSuccessful_time;
    }

    public String getWeibo_uid() {
        return weibo_uid;
    }

    public void setWeibo_uid(String weibo_uid) {
        this.weibo_uid = weibo_uid;
    }

    public String getLastErrorReason() {
		return lastErrorReason;
	}

	public void setLastErrorReason(String lastErrorReason) {
		this.lastErrorReason = lastErrorReason;
	}

	@Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");

        String last_setcode_time_temp = "";
        if (last_setcode_time == null || last_setcode_time.isEmpty()) {

        } else {
            long date1 = Long.parseLong(last_setcode_time);
            last_setcode_time_temp = formatter.format(new java.util.Date(date1));
        }

        String last_getAccessTokenSuccessful_time_temp = "";
        if (last_getAccessTokenSuccessful_time == null
                || last_getAccessTokenSuccessful_time.isEmpty()) {

        } else {
            long date2 = Long.parseLong(last_getAccessTokenSuccessful_time);
            last_getAccessTokenSuccessful_time_temp = formatter
                    .format(new java.util.Date(date2));
        }

        String last_syncweibo_time_temp = "";
        if (last_syncweibo_time == null || last_syncweibo_time.isEmpty()) {

        } else {
            long date3 = Long.parseLong(last_syncweibo_time);
            last_syncweibo_time_temp = formatter.format(new java.util.Date(date3));
        }
        String access_token_expired_time_temp = "";
        if (access_token_expired_time == null || access_token_expired_time.isEmpty()) {

        } else {
            long date4 = Long.parseLong(access_token_expired_time);
            access_token_expired_time_temp = formatter.format(new java.util.Date(date4));
        }

        return "</br>" +
                "</br>" +
                "id = " + id + ";</br>"
                + "上一次设置code时间                = " + last_setcode_time_temp + ";</br>"
                + "上一次设置的code        = " + last_code + ";</br>"
                + "</br>"
                + "上一次成功获取token时间    = " + last_getAccessTokenSuccessful_time_temp + ";</br>"
                + "有效token              = " + access_token + ";</br>"
                + "token的有效期                            = " + access_token_expired_time_temp
                + ";</br>"
                + "</br>"

                + "上一次系统更新微博时间      = " + last_syncweibo_time_temp + ";</br>"
                + "UID                    = " + weibo_uid + ";</br>"
                + "isRunningOk            = " + isRunningOk + ";</br>"
                + "AccessToken状态                      = " + getAccessTokenOk + ";</br>"
                + "lastErrorReason        = " + lastErrorReason;
    }
}
