
package soccer.access.interfaces;

import soccer.access.entity.WeiboInfo;

public interface IWeiboDao {
    public boolean save(WeiboInfo info);

    public WeiboInfo find();
}
