
package soccer.access.interfaces;

import java.io.InputStream;

import org.bson.types.ObjectId;

public interface IFileDao {
    public InputStream getFileInputStream(ObjectId id);

    public Object saveFile(String fileName, String path);

    public Object saveFileStream(InputStream fileStream);

    public boolean deleteFile(ObjectId id);
}
