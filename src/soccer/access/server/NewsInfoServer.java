
package soccer.access.server;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.types.ObjectId;

import antlr.collections.List;

import soccer.access.entity.NewsEntity;
import soccer.access.interfaces.INewsDao;
import soccer.access.web.ctl.NewInfoControl;

public class NewsInfoServer {

    private static final Log logger = LogFactory.getLog(NewsInfoServer.class);// LOG4J打印

    private INewsDao newsDao = null;

    public INewsDao getNewsDao() {
        return newsDao;
    }

    public void setNewsDao(INewsDao newsDao) {
        this.newsDao = newsDao;
    }

    public boolean addNewInfo(NewsEntity entity) {

        boolean isOK = newsDao.saveNewInfo(entity);

        if (!isOK) {
            logger.debug("add new info unsuccessful=====!");
        }

        return isOK;
    }

    /**
     * @param type -1表示全查询 查询现在个数默认15个
     * @return
     */
    public int queryNewInfo(int type, ArrayList<NewsEntity> entitylist) {

        int querysum = (int)newsDao.queryNewInfo(type, entitylist);

        logger.debug("queryNewInfo=====!");

        return querysum;
    }
    
    /**
     * @param type -1表示全查询 查询现在个数默认15个
     * @return
     */
    public int queryNewInfoByParam(String str,int pageoffset, ArrayList<NewsEntity> entitylist) {

        int querysum = (int)newsDao.queryNewInfoByParam(str,pageoffset, entitylist);

        logger.debug("queryNewInfoByParam=====!");

        return querysum;
    }
    
    

    /**
     * @param type 0表示全查询 查询现在个数默认15个
     * @return
     */
    public boolean delNewInfo(String str) {

        boolean delok = newsDao.delNewInfo(new ObjectId(str));

        logger.debug("delNewInfo=====!");

        if (!delok) {

            return false;
        }

        return true;
    }

}
