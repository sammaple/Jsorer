package soccer.access.web.ctl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import soccer.access.interfaces.IFileDao;
import soccer.access.util.ImageScale;


@Controller
@Scope("session")
public class PicManagerControl {
	@Autowired
	IFileDao fileDao;

    private static final Log logger = LogFactory.getLog(PicManagerControl.class);// LOG4J打印
    /**
     * 生成缩略图
     * 
     * @return
     * @throws IOException
     */
    public static String generalReducePic(IFileDao fileDao, String path, String fileName,
            String reducefileName, String mimeType) throws IOException {
        String filePath = path + fileName;
        ImageScale imageScale = new ImageScale();
        InputStream in = new FileInputStream(filePath);
        BufferedImage b1 = ImageIO.read(in);
        logger.debug("generalReducePic processing fileId start!");
        if(b1 == null){
            return "";
        }
        logger.debug("generalReducePic processing fileId mid!");
        BufferedImage b2 = imageScale.imgageZoomOutBySize(b1, 180);
        InputStream rOut = imageScale.getImageStream(b2, mimeType);
        if(rOut == null){
            return "";
        }
        OutputStream os = new FileOutputStream(path + reducefileName);
        byte[] bs = new byte[rOut.available()];
        rOut.read(bs);
        os.write(bs);
        os.flush();
        os.close();
        in.close();

		Object id = fileDao.saveFile(reducefileName, "./tempFile/" +reducefileName);
		String fileId = id.toString();

        logger.debug("generalReducePic processing fileId ："+fileId);
        return fileId;
    }
	
    /**
     * 生成缩略图
     * 
     * @return
     * @throws IOException
     */
    public static String generalReducePic_large(IFileDao fileDao, String path, String fileName,
            String reducefileName, String mimeType) throws IOException {
        String filePath = path + fileName;
        ImageScale imageScale = new ImageScale();
        InputStream in = new FileInputStream(filePath);
        BufferedImage b1 = ImageIO.read(in);
        if(b1 == null){
            return "";
        }
        BufferedImage b2 = imageScale.imgageZoomOutBySize(b1, 720);
        InputStream rOut = imageScale.getImageStream(b2, mimeType);
        if(rOut == null){
            return "";
        }
        OutputStream os = new FileOutputStream(path + reducefileName);
        byte[] bs = new byte[rOut.available()];
        rOut.read(bs);
        os.write(bs);
        os.flush();
        os.close();
        in.close();

		Object id = fileDao.saveFile(reducefileName, "./tempFile/" +reducefileName);
		String fileId = id.toString();
		
        return fileId;
    }
	
    /**
     * 给HTTP发送文件使用的额
     * @param req
     * @param needReduce
     * @return
     */
	@RequestMapping("/putPic")
	@ResponseBody
	public String putPic(HttpServletRequest req,int needReduce){
		boolean ret = true;
		String fileId = "";
		String reduceFileId = "";
		File tempPath = new File("./tempFile/");
		if(!tempPath.exists()){
			tempPath.mkdirs();
		}
		
		try{
			ServletInputStream inputStream = req.getInputStream();
			String fileName = ""+UUID.randomUUID();
			String fileReduceName = ""+UUID.randomUUID();
			File tempFile = new File("./tempFile/" +fileName);
			if(!tempFile.exists()){
				tempFile.createNewFile();
			}
			FileOutputStream stream = new FileOutputStream(tempFile);
			
			byte temp[] = new byte[1024];
			int len = 0;
			while((len = inputStream.read(temp,0,1024)) != -1){
				stream.write(temp, 0, len);
			}
			stream.close();
			
			Object id = fileDao.saveFile(fileName, "./tempFile/" +fileName);
			fileId = id.toString();
			
			if(needReduce == 1){
				reduceFileId = generalReducePic(fileDao, "./tempFile/",fileName,fileReduceName,"JPEG");
			}
			
		}catch (IOException e){
			ret = false;
		}
		String result = "{result:";
		if (ret == true) {
			result += "true";
		} else {
			result += "false";
		}
		result += ",fileId:"+fileId;
		if(needReduce == 1){
			result += ",reduceFileId:"+reduceFileId;
		}
		result += "}";
		
		return result;
	}
	
	@RequestMapping("/getPic")
	@ResponseBody
	public String getPic(HttpServletResponse res, String fileId){
		int total = 0;
		try{
			InputStream input = fileDao.getFileInputStream(new ObjectId(fileId));
			if(input != null){

				res.reset();
				res.setContentType("image/jpg");
				ServletOutputStream outStream = res.getOutputStream();
				byte temp[] = new byte[1024];
				int byteLength = 0;
				while((byteLength = input.read(temp)) != -1){
					outStream.write(temp, 0, byteLength);
					total += byteLength;
				}
				System.out.println("total:"+total);
				outStream.flush();
			}
		}catch (IOException e){
			System.out.println("getPic err");
		}
		return "";
	}
}

