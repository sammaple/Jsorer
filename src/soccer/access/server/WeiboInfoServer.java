
package soccer.access.server;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.types.ObjectId;

import soccer.access.entity.GeneralInfoEntity;
import soccer.access.interfaces.IGeneralInfoDao;

public class WeiboInfoServer {

    private static final Log logger = LogFactory.getLog(WeiboInfoServer.class);// LOG4J打印

    private IGeneralInfoDao generalInfoDao = null;

    public IGeneralInfoDao getGeneralInfoDao() {
        return generalInfoDao;
    }

    public void setGeneralInfoDao(IGeneralInfoDao generalInfoDao) {
        this.generalInfoDao = generalInfoDao;
    }

    /**
     * @param type -1表示全查询 查询现在个数默认15个
     * @return
     */
    public int queryNewInfo(int type, ArrayList<GeneralInfoEntity> entitylist) {

        int querysum = (int) generalInfoDao.queryInfo(type, entitylist);

        logger.debug("queryNewInfo=====!");

        return querysum;
    }

    /**
     * @param type -1表示全查询 查询现在个数默认15个
     * @return
     */
    public int queryNewInfoByParam(String str, int pageoffset,
            ArrayList<GeneralInfoEntity> entitylist) {

        int querysum = (int) generalInfoDao.queryInfoByParam(str, pageoffset, entitylist);

        logger.debug("queryNewInfoByParam=====!");

        return querysum;
    }

    /**
     * @param type 0表示全查询 查询现在个数默认15个
     * @return
     */
    public boolean delWeiboInfo(String str) {

        boolean delok = generalInfoDao.delete(new ObjectId(str));

        logger.debug("delWeiboInfo=====!");

        if (!delok) {
            return false;
        }

        return true;
    }

}
