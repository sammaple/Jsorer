
package weibo4j.examples.timeline;

import weibo4j.Timeline;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.WeiboException;

public class GetMentions {

    public static void main(String[] args) {
        String access_token = "2.00MtKGHE0cJ4on3310fc7804wtYvcE";
        Timeline tm = new Timeline();
        tm.client.setToken(access_token);
        try {
//            Paging page = new Paging(1, 50,3620673731986796L);
            Paging page = new Paging(2, 9);
//            Paging page = new Paging(1, 50);
            StatusWapper status = tm.getMentions(page, 0, 0, 0);

            Log.logInfo(page.getCount() + ":" + page.getPage());

            // StatusWapper status = tm.getMentions();
            for (Status s : status.getStatuses()) {
                Log.logInfo("-------------------------------------------");
                Log.logInfo(s.toString());
                Log.logInfo(s.getId());
                Log.logInfo(s.getText());
                Log.logInfo(s.getThumbnailPic());
                Log.logInfo(s.getBmiddlePic());
                Log.logInfo(s.getUser().getName());
                
                if(s.getRetweetedStatus() != null){
                    Log.logInfo("原始图片");
                    Log.logInfo(s.getRetweetedStatus().getThumbnailPic());
                    Log.logInfo(s.getRetweetedStatus().getBmiddlePic());
                }
            }
            Log.logInfo("-----------------------------end--------------");
            System.out.println(status.getNextCursor());
            System.out.println(status.getPreviousCursor());
            System.out.println(status.getTotalNumber());
            System.out.println(status.getHasvisible());
        } catch (WeiboException e) {
            e.printStackTrace();
        }
    }

}
