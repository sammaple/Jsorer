
package soccer.access.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.types.ObjectId;

import soccer.access.entity.GeneralInfoEntity;
import soccer.access.interfaces.IGeneralInfoDao;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.query.Query;
import com.mongodb.Mongo;

public class GeneralInfoDao implements IGeneralInfoDao {

    private Datastore ds;

    private static final Log logger = LogFactory.getLog(GeneralInfoDao.class);// LOG4J

    public GeneralInfoDao(Mongo mongo, Morphia morphia, String dbName, String userName, String pw) {
        ds = morphia.createDatastore(mongo, dbName);
    }

    public boolean save(GeneralInfoEntity generalInfo) {
        ds.save(generalInfo);
        return true;
    }

    public GeneralInfoEntity find(ObjectId id) {
        Query<GeneralInfoEntity> query = ds.find(GeneralInfoEntity.class, "id", id);
        return query.get();
    }

    public void query1(Query<GeneralInfoEntity> query, String[] p) {
        query.or(query.criteria("place").contains(p[0]));
    }

    public void query2(Query<GeneralInfoEntity> query, String[] p) {
        query.or(query.criteria("place").contains(p[0]),
                query.criteria("place").contains(p[1]));
    }

    public void query3(Query<GeneralInfoEntity> query, String[] p) {
        query.or(query.criteria("place").contains(p[0]),
                query.criteria("place").contains(p[1]),
                query.criteria("place").contains(p[2]));
    }

    public void query4(Query<GeneralInfoEntity> query, String[] p) {
        query.or(query.criteria("place").contains(p[0]),
                query.criteria("place").contains(p[1]),
                query.criteria("place").contains(p[2]),
                query.criteria("place").contains(p[3]));
    }

    public void query5(Query<GeneralInfoEntity> query, String[] p) {
        query.or(query.criteria("place").contains(p[0]),
                query.criteria("place").contains(p[1]),
                query.criteria("place").contains(p[2]),
                query.criteria("place").contains(p[3]),
                query.criteria("place").contains(p[4]));
    }

    public void query6(Query<GeneralInfoEntity> query, String[] p) {
        query.or(query.criteria("place").contains(p[0]),
                query.criteria("place").contains(p[1]),
                query.criteria("place").contains(p[2]),
                query.criteria("place").contains(p[3]),
                query.criteria("place").contains(p[4]),
                query.criteria("place").contains(p[5]));
    }

    @Override
    public List<GeneralInfoEntity> findTopGeneralInfo(int numm, String date) {

        Query<GeneralInfoEntity> q = ds.find(GeneralInfoEntity.class).field("date").
                greaterThanOrEq(String.valueOf(Integer.valueOf(date) - 30));

        q.order("-attetion").limit(numm);

        if (q.countAll() == 0) {
            return new ArrayList<GeneralInfoEntity>();
        }

        return q.asList();
    }

    @Override
    public boolean delete(ObjectId id) {
        Query<GeneralInfoEntity> query = ds.find(GeneralInfoEntity.class, "id", id);
        ds.delete(query.get());
        return true;
    }

    @Override
    public GeneralInfoEntity findByWeiboID(String weiboid) {
        Query<GeneralInfoEntity> query = ds.find(GeneralInfoEntity.class, "weibo_uid", weiboid);
        return query.get();
    }

    @Override
    public long queryInfo(int type, ArrayList<GeneralInfoEntity> entitylist) {

        if (type == -1) {
            entitylist.addAll((ArrayList<GeneralInfoEntity>) ds
                    .createQuery(GeneralInfoEntity.class)
                    .disableValidation().order("-time").limit(NewsDao.WINDOWSIZE).asList());
        } else {
            entitylist.addAll((ArrayList<GeneralInfoEntity>) ds
                    .createQuery(GeneralInfoEntity.class)
                    .disableValidation().order("-time").offset(type * NewsDao.WINDOWSIZE)
                    .limit(NewsDao.WINDOWSIZE).asList());
        }

        logger.debug("query size:" + entitylist.size());

        long count = ds.find(GeneralInfoEntity.class).countAll();

        return count;
    }

    @Override
    public long queryInfoByParam(String str, int pageoffset, ArrayList<GeneralInfoEntity> entitylist) {

        Query<GeneralInfoEntity> q = ds.find(GeneralInfoEntity.class);

        logger.debug("query str:" + str);
        q.field("title").contains(str);

        entitylist.addAll((ArrayList<GeneralInfoEntity>) q.disableValidation().order("-time")
                .offset(pageoffset * NewsDao.WINDOWSIZE).limit(NewsDao.WINDOWSIZE)
                .asList());

        logger.debug("query size:" + entitylist.size());

        long count = ds.find(GeneralInfoEntity.class).field("title")
                .contains(str).countAll();

        return count;
    }

	@Override
	public long queryAll(ArrayList<GeneralInfoEntity> entitylist) {

        entitylist.addAll((ArrayList<GeneralInfoEntity>) ds
                .createQuery(GeneralInfoEntity.class).disableValidation().asList());

        logger.debug("queryAll size:" + entitylist.size());

        long count = ds.find(GeneralInfoEntity.class).countAll();

        return count;

	}
}
