package weibo4j.examples.favorites;

import java.util.List;

import weibo4j.Favorite;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.Favorites;
import weibo4j.model.Paging;
import weibo4j.model.WeiboException;

public class GetFavorites {

	public static void main(String[] args) {
		String access_token = "2.00MtKGHE0cJ4on5d581146e6fuTwzC";
		Favorite fm = new Favorite();
		fm.client.setToken(access_token);
		Paging page = new Paging(2, 100);
		try {
			List<Favorites> favors = fm.getFavorites(page);
			System.out.println("个数："+favors.size());
			for(Favorites s : favors){
				Log.logInfo(s.toString());
			}
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}

}
