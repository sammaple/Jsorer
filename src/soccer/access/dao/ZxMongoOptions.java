
package soccer.access.dao;

import com.mongodb.MongoOptions;

public class ZxMongoOptions extends MongoOptions {

    public ZxMongoOptions() {
        super();
        /*connectionsPerHost*threadsAllowedToBlockForConnectionMultiplier = 4000*/
        autoConnectRetry = true;
        connectionsPerHost = 100;
        maxWaitTime = 5000;
        socketTimeout = 0;
        connectTimeout = 15000;
        threadsAllowedToBlockForConnectionMultiplier = 10;
        
    }

}
