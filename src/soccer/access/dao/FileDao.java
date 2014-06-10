package soccer.access.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.bson.types.ObjectId;

import soccer.access.interfaces.IFileDao;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

public class FileDao implements IFileDao {
    DB db;
    Mongo mongo;
    String dbName;

    public FileDao(Mongo mongo, String dbName, String userName, String pw) {
        this.mongo = mongo;
        this.dbName = dbName;
        this.db = mongo.getDB(dbName);
    }

    public Object saveFile(String fileName, String path) {
        File file = new File(path);
        try {
            FileInputStream fileStream = new FileInputStream(file);

            GridFS gfsFile = new GridFS(db, "file");
            GridFSInputFile inputFile = gfsFile.createFile(fileStream);
            inputFile.setFilename(fileName);
            inputFile.save();
            return inputFile.getId();
        } catch (FileNotFoundException e) {

        }

        return null;
    }

    public Object saveFileStream(InputStream fileStream) {
        try {
            GridFS gfsFile = new GridFS(db, "file");
            GridFSInputFile inputFile = gfsFile.createFile(fileStream);
            inputFile.save();
            return inputFile.getId();
        } catch (Exception e) {

        }

        return null;
    }

    public InputStream getFileInputStream(ObjectId id) {
        GridFS gfsFile = new GridFS(db, "file");
        GridFSDBFile gridFile = gfsFile.findOne(id);
        if (gridFile == null) {
            return null;
        }
        return gridFile.getInputStream();
    }

    @Override
    public boolean deleteFile(ObjectId id) {

        GridFS gfsFile = new GridFS(db, "file");
        gfsFile.remove(id);

        return true;
    }
}
