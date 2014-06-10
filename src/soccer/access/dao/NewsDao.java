
package soccer.access.dao;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.types.ObjectId;

import soccer.access.entity.NewsEntity;
import soccer.access.interfaces.INewsDao;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.dao.BasicDAO;
import com.google.code.morphia.query.Query;
import com.mongodb.Mongo;

public class NewsDao extends BasicDAO<NewsEntity, ObjectId> implements INewsDao {

    private static final Log logger = LogFactory.getLog(NewsDao.class);// LOG4J

    public static int WINDOWSIZE = 5;

    private Datastore ds;

    protected NewsDao(Mongo mongo, Morphia morphia, String dbName, String user,
            String pw) {
        super(mongo, morphia, dbName);
        ds = DbAuthDao.createDatastoreAuth(mongo, morphia, dbName, user, pw);

    }

    @Override
    public boolean saveNewInfo(NewsEntity news) {
        if (news == null) {
            logger.debug("saveNewInfo====no newsinfo");
            return false;
        }

        ds.save(news);
        return true;
    }

    @Override
    public boolean updateNewInfo(NewsEntity news) {

        return false;
    }

    @Override
    public boolean delNewInfo(ObjectId id) {

        Query<NewsEntity> query = ds.find(NewsEntity.class).field("_id").equal(id);
        logger.debug("delNewInfo query: " + query);

        ds.delete(query);

        return true;
    }

    @Override
    public long queryNewInfo(int type, ArrayList<NewsEntity> entitylist) {

        if (type == -1) {
            entitylist.addAll((ArrayList<NewsEntity>) ds.createQuery(NewsEntity.class)
                    .disableValidation().order("-date").limit(WINDOWSIZE).asList());
        } else {
            entitylist.addAll((ArrayList<NewsEntity>) ds.createQuery(NewsEntity.class)
                    .disableValidation().order("-date").offset(type * WINDOWSIZE)
                    .limit(WINDOWSIZE).asList());
        }

        logger.debug("query size:" + entitylist.size());

        long count = ds.find(NewsEntity.class).countAll();

        return count;
    }

    @Override
    public long queryNewInfoByParam(String str, int pageoffset, ArrayList<NewsEntity> entitylist) {

        Query<NewsEntity> q = ds.find(NewsEntity.class);

        logger.debug("query str:" + str);
        q.or(q.criteria("title").contains(str), q.criteria("teamName").contains(str),
                q.criteria("content").contains(str));

        entitylist.addAll((ArrayList<NewsEntity>) q.disableValidation()
                .order("-date").offset(pageoffset * WINDOWSIZE).limit(WINDOWSIZE).asList());

        logger.debug("query size:" + entitylist.size());

        long count = ds.find(NewsEntity.class).field("teamName")
                .contains(str).countAll();

        return count;
    }
}
