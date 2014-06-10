
package soccer.access.interfaces;

import java.util.ArrayList;

import net.sf.json.JSONObject;

import org.bson.types.ObjectId;

import soccer.access.entity.ReviewEntity;

public interface IReviewDao {
    public boolean saveReviewInfo(ReviewEntity news);

    public boolean updateReviewInfo(ReviewEntity news);

    public boolean delReviewInfo(ObjectId id);

    public long queryReviewInfo(int type,ArrayList<ReviewEntity> entitylist);

    public long queryReviewInfoByParam(JSONObject object, int pageoffset, ArrayList<ReviewEntity> entitylist);
}
