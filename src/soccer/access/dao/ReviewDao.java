
package soccer.access.dao;

import java.util.ArrayList;
import java.util.Iterator;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.types.ObjectId;

import soccer.access.entity.ReviewEntity;
import soccer.access.interfaces.IReviewDao;
import soccer.access.util.JsonUtilForReview;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.dao.BasicDAO;
import com.google.code.morphia.query.Query;
import com.mongodb.Mongo;

public class ReviewDao extends BasicDAO<ReviewEntity, ObjectId> implements IReviewDao {

    private static final Log logger = LogFactory.getLog(ReviewDao.class);// LOG4J

    private Datastore ds;

    public static int WINDOWSIZE = 5;

    protected ReviewDao(Mongo mongo, Morphia morphia, String dbName, String user,
            String pw) {
        super(mongo, morphia, dbName);
        ds = DbAuthDao.createDatastoreAuth(mongo, morphia, dbName, user, pw);

    }

    @Override
    public boolean saveReviewInfo(ReviewEntity news) {
        if (news == null) {
            logger.debug("saveReviewInfo====no newsinfo");
            return false;
        }

        ds.save(news);
        return true;
    }

    @Override
    public boolean updateReviewInfo(ReviewEntity news) {
        return true;
    }

    @Override
    public boolean delReviewInfo(ObjectId id) {
        Query<ReviewEntity> query = ds.find(ReviewEntity.class).field("_id").equal(id);
        logger.debug("delReviewInfo query: " + query);

        ds.delete(query);
        return true;
    }

    @Override
    public long queryReviewInfo(int type, ArrayList<ReviewEntity> entitylist) {
        if (type == -1) {
            entitylist.addAll((ArrayList<ReviewEntity>) ds.createQuery(ReviewEntity.class)
                    .disableValidation().order("-date").limit(WINDOWSIZE).asList());
        } else {
            entitylist.addAll((ArrayList<ReviewEntity>) ds.createQuery(ReviewEntity.class)
                    .disableValidation().order("-date").offset(type * WINDOWSIZE)
                    .limit(WINDOWSIZE).asList());
        }

        logger.debug("query size:" + entitylist.size());

        long count = ds.find(ReviewEntity.class).countAll();

        return count;
    }

    @Override
    public long queryReviewInfoByParam(JSONObject object, int pageoffset,
            ArrayList<ReviewEntity> entitylist) {

        if (object == null) {
            logger.debug("delteContactRecord====no object");
            return 0;
        }
        @SuppressWarnings("rawtypes")
        Iterator keyIter = object.keys();
        String keystring;
        Object value;

        Query<ReviewEntity> query = null;
        while (keyIter.hasNext()) { // JSONObject
            keystring = (String) keyIter.next();
            value = object.get(keystring);
            logger.debug("queryReviewInfoByParam====keystring=====>"
                    + keystring);
            logger.debug("queryReviewInfoByParam====value=====>" + value);
            if (null == query) {
                if (keystring.equals(JsonUtilForReview.TITLE)) {
                    /* title */

                    query = ds.find(ReviewEntity.class)
                            .field(keystring).contains((String) value);
                } else {

                    query = ds.find(ReviewEntity.class)
                            .field(keystring).equal(value);
                }
            } else {
                if (keystring.equals(JsonUtilForReview.TITLE)) {

                    query = query.field(keystring).contains((String) value);
                } else {

                    query = query.field(keystring).equal(value);
                }
            }
        }

        if (null == query) {
            logger.debug("queryReviewInfoByParam==========query null=============>");
            return 0;
        }

        entitylist.addAll((ArrayList<ReviewEntity>) query
                .disableValidation().order("-date").offset(pageoffset * WINDOWSIZE)
                .limit(WINDOWSIZE).asList());

        long count = query.countAll();

        logger.debug("queryReviewInfoByParam=======query count======>" + count);
        return count;
    }
}
