package weibo4j.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import weibo4j.http.Response;
import weibo4j.org.json.JSONArray;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;

public class Favorites extends WeiboResponse implements java.io.Serializable{

	private static final long serialVersionUID = 3355536191107298448L;
	private Date favoritedTime;                        //添加收藏的时间
	private String favoritedTime_org;                        //添加收藏的时间原始值
	private Status status;                             //收藏的status
	private List<FavoritesTag> tags;                   //收藏的tags
	private static int totalNumber;
	public Favorites(Response res) throws WeiboException{
		super(res);
		JSONObject json = null;
		try {
			json = res.asJSONObject();
			favoritedTime = parseDate(json.getString("favorited_time"), "EEE MMM dd HH:mm:ss z yyyy");
			favoritedTime_org = String.valueOf(favoritedTime.getTime());
			if(!json.isNull("status")){
				status = new Status(json.getJSONObject("status"));
			}
			if(!json.isNull("tags")){
				JSONArray list = json.getJSONArray("tags");
				int size = list.length();
				List<FavoritesTag> tag = new ArrayList<FavoritesTag>(size);
				for (int i = 0;i< size;i++){
					tag.add(new FavoritesTag(list.getJSONObject(i)));
				}
			}
		} catch (JSONException je) {
			throw new WeiboException(je.getMessage() + ":" + json.toString(), je);
		}
	} 
	Favorites(JSONObject json) throws WeiboException, JSONException{
		favoritedTime = parseDate(json.getString("favorited_time"), "EEE MMM dd HH:mm:ss z yyyy");
		favoritedTime_org = String.valueOf(favoritedTime.getTime());
		if(!json.isNull("status")){
			status = new Status(json.getJSONObject("status"));
		}
		if(!json.isNull("tags")){
			JSONArray list = json.getJSONArray("tags");
			int size = list.length();
			tags = new ArrayList<FavoritesTag>(size);
			for (int i = 0;i< size;i++){
				tags.add(new FavoritesTag(list.getJSONObject(i)));
			}
		}

	}
	public static List<Favorites> constructFavorites(Response res) throws WeiboException{
		try {
			JSONArray list = res.asJSONObject().getJSONArray("favorites");
			int size = list.length();
			List<Favorites> favorites = new ArrayList<Favorites>(size);
			for (int i = 0; i < size; i++) {
				favorites.add(new Favorites(list.getJSONObject(i)));
			}
			totalNumber =res.asJSONObject().getInt("total_number");
			return favorites;
		} catch (JSONException jsone) {
			throw new WeiboException(jsone);
		} 
	}
	
	public Status getStatus() {
		return status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
	
	public List<FavoritesTag> getTags() {
		return tags;
	}
	
	public void setTags(List<FavoritesTag> tags) {
		this.tags = tags;
	}

	public Date getFavoritedTime() {
		return favoritedTime;
	}

	public void setFavoritedTime(Date favoritedTime) {
		this.favoritedTime = favoritedTime;
	}
	
	public String getFavoritedTime_org() {
		return favoritedTime_org;
	}
	public void setFavoritedTime_org(String favoritedTime_org) {
		this.favoritedTime_org = favoritedTime_org;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
		+ ((favoritedTime == null) ? 0 : favoritedTime.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Favorites other = (Favorites) obj;
		if (favoritedTime == null) {
			if (other.favoritedTime != null)
				return false;
		} else if (!favoritedTime.equals(other.favoritedTime))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Favorites [" +
				"favoritedTime_org=" + favoritedTime_org + 
				",favorited_time=" + favoritedTime + 
				", status=" + status.toString() + 
				", FavoritesTag=" + ((tags==null)?"null":tags.toString()) + 
				", total_number = "+totalNumber+
				"]";
	}

}
