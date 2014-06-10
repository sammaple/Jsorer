
package soccer.access.interfaces;

import java.util.ArrayList;

import org.bson.types.ObjectId;
import soccer.access.entity.NewsEntity;

public interface INewsDao {
    public boolean saveNewInfo(NewsEntity news);

    public boolean updateNewInfo(NewsEntity news);

    public boolean delNewInfo(ObjectId id);

    public long queryNewInfo(int type,ArrayList<NewsEntity> entitylist);

    public long queryNewInfoByParam(String str, int pageoffset, ArrayList<NewsEntity> entitylist);
}
