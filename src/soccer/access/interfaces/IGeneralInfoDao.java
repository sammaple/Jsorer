
package soccer.access.interfaces;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import soccer.access.entity.GeneralInfoEntity;

public interface IGeneralInfoDao {
    public boolean save(GeneralInfoEntity generalInfo);

    public GeneralInfoEntity find(ObjectId id);

    public GeneralInfoEntity findByWeiboID(String weiboid);

    public boolean delete(ObjectId id);

    public List<GeneralInfoEntity> findTopGeneralInfo(int numm, String date);

    public long queryInfo(int type, ArrayList<GeneralInfoEntity> entitylist);

    public long queryInfoByParam(String str, int pageoffset,
            ArrayList<GeneralInfoEntity> entitylist);
    

    public long queryAll(ArrayList<GeneralInfoEntity> entitylist);
}
