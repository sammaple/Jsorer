
package soccer.access.dao;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
/**
 * 
 * 
 * @author 
 */
public abstract class DbAuthDao {

    public static Datastore createDatastoreAuth(Mongo mongo, Morphia morphia, String dbName,
            String user, String pw)
    {
        Datastore ds = null;
        ds = morphia.createDatastore(mongo, dbName);
        ds.ensureIndexes();
        if (user != null && pw != null)
        {
            if (!user.isEmpty() && !pw.isEmpty())
            {
                ds.getDB().authenticate(user, pw.toCharArray());
            }
        }

        return ds;
    }

}
