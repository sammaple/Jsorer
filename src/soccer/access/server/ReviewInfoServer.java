
package soccer.access.server;

import java.util.ArrayList;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.types.ObjectId;

import soccer.access.entity.ReviewEntity;
import soccer.access.interfaces.IReviewDao;

public class ReviewInfoServer {

    private static final Log logger = LogFactory.getLog(ReviewInfoServer.class);// LOG4J打印

    private IReviewDao reviewDao = null;

    public IReviewDao getReviewDao() {
        return reviewDao;
    }

    public void setReviewDao(IReviewDao reviewDao) {
        this.reviewDao = reviewDao;
    }

    public boolean addRiviewInfo(ReviewEntity entity) {

        boolean isOK = reviewDao.saveReviewInfo(entity);

        if (!isOK) {
            logger.debug("add new info unsuccessful=====!");
        }

        return isOK;
    }

    /**
     * @param type -1表示全查询 查询现在个数默认15个
     * @return
     */
    public int queryReviewInfo(int type, ArrayList<ReviewEntity> entitylist) {

        int querysum = (int)reviewDao.queryReviewInfo(type, entitylist);

        logger.debug("queryReviewInfo=====!");

        return querysum;
    }
    
    /**
     * @param type -1表示全查询 查询现在个数默认15个
     * @return
     */
    public int queryReviewInfoByParam(JSONObject json,int pageoffset, ArrayList<ReviewEntity> entitylist) {

        int querysum = (int)reviewDao.queryReviewInfoByParam(json,pageoffset, entitylist);

        logger.debug("queryReviewInfoByParam=====!");

        return querysum;
    }
    
    

    /**
     * @param type 0表示全查询 查询现在个数默认15个
     * @return
     */
    public boolean delReviewInfo(String str) {

        boolean delok = reviewDao.delReviewInfo(new ObjectId(str));

        logger.debug("delReviewInfo=====!");

        if (!delok) {

            return false;
        }

        return true;
    }

}
