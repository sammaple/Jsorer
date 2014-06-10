
package soccer.access.dao;

import soccer.access.entity.WeiboInfo;
import soccer.access.interfaces.IWeiboDao;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.query.Query;
import com.mongodb.Mongo;

public class WeiboDao implements IWeiboDao {

    private Datastore ds;

    public WeiboDao(Mongo mongo, Morphia morphia, String dbName, String userName, String pw) {
        ds = morphia.createDatastore(mongo, dbName);
        ds.ensureIndexes();
    }

    @Override
    public boolean save(WeiboInfo info) {
        ds.save(info);
        return true;
    }

    @Override
    public WeiboInfo find() {
        Query<WeiboInfo> query = ds.find(WeiboInfo.class);
        return query.get();
    }

}
