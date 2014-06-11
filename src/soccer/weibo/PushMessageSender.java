
package soccer.weibo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import soccer.access.dao.FileDao;
import soccer.access.dao.GeneralInfoDao;
import soccer.access.dao.WeiboDao;
import soccer.access.entity.GeneralInfoEntity;
import soccer.access.entity.WeiboInfo;
import soccer.access.interfaces.IFileDao;
import soccer.access.interfaces.IGeneralInfoDao;
import soccer.access.interfaces.IWeiboDao;
import soccer.access.util.Base64;
import soccer.access.util.Email;
import soccer.access.web.ctl.PicManagerControl;
import weibo4j.Favorite;
import weibo4j.examples.oauth2.Log;
import weibo4j.http.Response;
import weibo4j.model.Favorites;
import weibo4j.model.Paging;
import weibo4j.model.PostParameter;
import weibo4j.model.Status;
import weibo4j.model.WeiboException;
import weibo4j.util.MyHttpClient;

/**
 * @Description
 * @date 2012-3-30
 * @author jianghaiyang_Sw
 */
public class PushMessageSender extends Thread {
    // 1381161601000
    private static final int PAGENUM = 50;// 一页的个数

    public static final String TRUE = "true";

    public static final String NOTIFY = "notify";

    // public static final String KEYWORD = "@Jscorer工作室";
    public static final String KEYWORD = "@yuanlai10000";

    private static final Logger logger = Logger
            .getLogger(PushMessageSender.class);

    private static BlockingQueue<String> senderQueued = new LinkedBlockingQueue<String>();

    static PushMessageSender messageSender = null;

    private IWeiboDao weiboDao;

    private IGeneralInfoDao generalInfoDao;

    private IFileDao fileDao;

    public IWeiboDao getWeiboDao() {
        return weiboDao;
    }

    public void setWeiboDao(IWeiboDao weiboDao) {
        this.weiboDao = weiboDao;
    }

    public IGeneralInfoDao getGeneralInfoDao() {
        return generalInfoDao;
    }

    public void setGeneralInfoDao(IGeneralInfoDao generalInfoDao) {
        this.generalInfoDao = generalInfoDao;
    }

    public IFileDao getFileDao() {
        return fileDao;
    }

    public void setFileDao(IFileDao fileDao) {
        this.fileDao = fileDao;
    }

    public static boolean isThreadRunning() {

        boolean isAlive = getInstance().isAlive();

        logger.error("Hello,isThreadRunning~&&&&&&&&&~:" + isAlive);
        return isAlive;
    }

    public static void registerPush(String accountname, int type) {
        if (accountname == null || accountname.isEmpty()) {
            logger.error("registerPush has err parameters!");
        }

        // synchronized (getInstance()) {
        // getInstance().notify();
        // }
        try {
            senderQueued.put(NOTIFY);
        } catch (InterruptedException e) {
            logger.error("Hello,PushMessageSender registerPush err~~"
                    + e.toString());
        }
    }

    public synchronized static PushMessageSender getInstance() {
        if (messageSender == null) {
            messageSender = new PushMessageSender();
            messageSender.setDaemon(true);
            return messageSender;
        }
        logger.debug("PushMessageSender thread info:" + messageSender.isAlive());

        if (!messageSender.isAlive()) {/* 线程由于运行时异常退出 */
            messageSender.interrupt();
            messageSender = null;
            messageSender = new PushMessageSender();
            messageSender.setDaemon(true);
            messageSender.start();
        }
        return messageSender;
    }

    public void stopthread() {
        getInstance().interrupt();
    }

    public void getDaoFromApplicationContext() {

        logger.debug("getDaoFromApplicationContext:"
                + MyApplicationContextUtil.getContext());
        weiboDao = (WeiboDao) MyApplicationContextUtil.getContext().getBean(
                "weiboDao");
        generalInfoDao = (GeneralInfoDao) MyApplicationContextUtil.getContext()
                .getBean("generalInfoDao");
        fileDao = (FileDao) MyApplicationContextUtil.getContext().getBean(
                "fileDao");
    }

    private boolean getHttpWeiboFileHttpURLConnection(String sURL_in, String fileName) {
        String sURL = sURL_in;

        int nStartPos = 0;
        int nRead = 0;

        // String sPath = "e:\\temp";

        try {

            URL url = new URL(sURL); // 打开连接
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();// 获得文件长度
            long nEndPos = getFileSize(sURL);
            // RandomAccessFile oSavedFile = new RandomAccessFile(sPath, "rw");
            File oSavedFile = new File("./tempFile/" + fileName);
            if (!oSavedFile.exists()) {
                oSavedFile.createNewFile();
            }
            FileOutputStream stream = new FileOutputStream(oSavedFile);
            httpConnection
                    .setRequestProperty("User-Agent", "Internet Explorer");
            String sProperty = "bytes=" + nStartPos + "-";
            // 告诉服务器book.rar这个文件从nStartPos字节开始传
            httpConnection.setRequestProperty("RANGE", sProperty);
            System.out.println(sProperty);
            InputStream input = httpConnection.getInputStream();
            byte[] b = new byte[1024];// 读取网络文件,写入指定的文件中
            while ((nRead = input.read(b, 0, 1024)) > 0 && nStartPos < nEndPos) {
                stream.write(b, 0, nRead);
                nStartPos += nRead;
            }
            httpConnection.disconnect();
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean getHttpWeiboFile(String sURL_in, String fileName) {
        String sURL = sURL_in;

        int nStartPos = 0;
        int nRead = 0;

        // String sPath = "e:\\temp";

        try {
            MyHttpClient cl = new MyHttpClient();
            Response res = cl.get(sURL_in, new PostParameter[] {}, false);
            File oSavedFile = new File("./tempFile/" + fileName);
            if (!oSavedFile.exists()) {
                oSavedFile.createNewFile();
            }
            FileOutputStream stream = new FileOutputStream(oSavedFile);
            stream.write(res.getResponseAsString().getBytes());
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // 获得文件长度
    public static long getFileSize(String sURL) {
        int nFileLength = -1;
        try {
            URL url = new URL(sURL);
            HttpURLConnection httpConnection = (HttpURLConnection) url
                    .openConnection();
            httpConnection
                    .setRequestProperty("User-Agent", "Internet Explorer");
            int responseCode = httpConnection.getResponseCode();
            if (responseCode >= 400) {
                System.err.println("Error Code : " + responseCode);
                return -2; // -2 represent access is error
            }
            String sHeader;
            for (int i = 1;; i++) {
                sHeader = httpConnection.getHeaderFieldKey(i);
                if (sHeader != null) {
                    if (sHeader.equals("Content-Length")) {
                        nFileLength = Integer.parseInt(httpConnection
                                .getHeaderField(sHeader));
                        break;
                    }
                } else
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(nFileLength);
        return nFileLength;
    }

    public boolean putPic(GeneralInfoEntity qInfo, String sURL_in) {
        int index = 0;

        File tempPath = new File("./tempFile/");
        if (!tempPath.exists()) {
            tempPath.mkdirs();
        }

        String fileName = "" + UUID.randomUUID();
        String fileReduceName = "" + UUID.randomUUID();

        while (true) {
            index++;

            if (getHttpWeiboFileHttpURLConnection(sURL_in, fileName)) {
                logger.debug("Hello,PushMessageSender 获取原始图片OK~");
                break;
            }
            if (index > 3) {

                logger.debug("Hello,PushMessageSender 获取原始图片错误超过3次~");
                return false;
            }
        }

        String fileId = "";
        String reduceFileId = "";

        try {

            /*
             * Object id = fileDao.saveFile(fileName, "./tempFile/" + fileName);
             * fileId = id.toString();
             */

            reduceFileId = PicManagerControl.generalReducePic(fileDao, "./tempFile/", fileName,
                    fileReduceName, "JPEG");

            logger.debug("Hello,PushMessageSender 获取图片处理fileId:" + fileId + ",reduceFileId"
                    + reduceFileId);

        } catch (IOException e) {
            logger.debug("Hello,PushMessageSender 获取图片处理过程错误~");
            return false;
        }
        qInfo.setPic(reduceFileId);
        qInfo.setOriginImageUrl(reduceFileId);// 改用缩略图

        return true;
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            logger.debug("Hello,PushMessageSender maybe wait~~");

            try {
                Thread.sleep(60000);// 轮询时间
            } catch (Exception e) {
                logger.error("Hello,PushMessageSender sleep err~~"
                        + e.toString());
            }// 等待两秒缓冲

            logger.debug("Hello,PushMessageSender work~~");

            WeiboInfo weiboInfo = weiboDao.find();
            if (weiboInfo == null) {

                logger.debug("Hello,PushMessageSender weiboInfo is null~~");
                continue;
            }

            if (!weiboInfo.isGetAccessTokenOk()) {

                logger.debug("Hello,PushMessageSender isGetAccessTokenOk is false~~");
                // weiboInfo.setGetAccessTokenOk(true);
                // weiboDao.save(weiboInfo);
                continue;
            }

            String access_token = weiboInfo.getAccess_token();
            if (access_token == null || access_token.isEmpty()) {

                logger.debug("Hello,PushMessageSender access_token is null~~");
                continue;
            }

            /*
             * Timeline tm = new Timeline(); tm.client.setToken(access_token);
             */
            Favorite fm = new Favorite();
            fm.client.setToken(access_token);
            String lastUser_FavarTime = weiboInfo.getWeibo_uid();
            String temp_lastFavarTime = "";
            String lastest_FavarTime = "";
            int index = 1;
            boolean firstin = true;
            while (true) {

                /*
                 * if (index >= 1) {// temp not in looping break; }
                 */

                try {
                    Paging page = null;
                    /*
                     * if (lastUser_FavarTime == null || lastUser_FavarTime
                     * .isEmpty()) { page = new Paging(index, PAGENUM); if
                     * (index % 3 == 0) {
                     * logger.debug("Hello,PushMessageSender sleep in index/3~~"
                     * ); try { Thread.sleep(30000); } catch (Exception e) {
                     * logger.error("Hello,PushMessageSender sleep in index/3~~"
                     * + e.toString()); }// 等待两秒缓冲 } } else { page = new
                     * Paging(index, PAGENUM, Long.valueOf(lastUser_FavarTime
                     * )); if (index % 3 == 0) {
                     * logger.debug("Hello,PushMessageSender sleep in index/3~~2："
                     * ); try { Thread.sleep(30000); } catch (Exception e) {
                     * logger
                     * .error("Hello,PushMessageSender sleep in index/3~~2：" +
                     * e.toString()); }// 等待两秒缓冲 } }
                     */

                    page = new Paging(index, PAGENUM);
                    if (index % 3 == 0) {
                        logger.debug("Hello,PushMessageSender sleep in index/3~~");
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            logger.error("Hello,PushMessageSender sleep in index/3~~"
                                    + e.toString());
                        }// 等待两秒缓冲
                    }

                    List<Favorites> favors = fm.getFavorites(page);

                    // StatusWapper status = tm.getMentions(page, 0, 0, 0);

                    Log.logInfo(page.getCount() + ":" + page.getPage());

                    // StatusWapper status = tm.getMentions();
                    for (Favorites ss : favors) {
                        Status s = ss.getStatus();
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            logger.error("Hello,PushMessageSender sleep in getStatuses~~"
                                    + e.toString());
                        }// 等待两秒缓冲

                        if (firstin) {
                            firstin = false;
                            lastest_FavarTime = ss.getFavoritedTime_org();// 时间戳
                        }
                        Log.logInfo("-------------------------------------------");
                        Log.logInfo(s.toString());
                        Log.logInfo(s.getId());
                        Log.logInfo(s.getText());
                        Log.logInfo(s.getThumbnailPic());
                        Log.logInfo(s.getBmiddlePic());
                        if(s.getUser() == null){//防止微博被删除导致的空指针
                        	continue;
                        }
                        Log.logInfo(s.getUser().getName());
                        

                        if (s.getRetweetedStatus() != null) {
                            Log.logInfo("原始图片>>>>>>>>>>>>>>>>>");
                            Log.logInfo(s.getRetweetedStatus().getThumbnailPic());
                            Log.logInfo(s.getRetweetedStatus().getBmiddlePic());
                        }

                        temp_lastFavarTime = ss.getFavoritedTime_org();// 时间戳
                                                                       // 不管后面如何发展，temp_lastFavarTime还是要纪录的

                        /*
                         * Date nowTime = new Date(); SimpleDateFormat
                         * time = new
                         * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); int
                         * date = (int) (nowTime.getTime() / (24 * 60 *
                         * 60 * 1000));
                         */

                        GeneralInfoEntity qInfo = generalInfoDao.findByWeiboID(s.getId());

                        if (qInfo == null) {// 如果没有则正常添加

                            GeneralInfoEntity generalInfo = new GeneralInfoEntity();
                            generalInfo.setPic("");
                            generalInfo.setOriginImageUrl("");

                            String str = s.getText();
                            String strtmep = str.trim();
                            if (strtmep.contains("//")) {// 二次转发或者以上
                                Log.logInfo("二次转发或者以上&&&&&&");
                                //strtmep = strtmep.substring(0, strtmep.indexOf("//"));
                            } else {// 原创或者第一次转发
                                Log.logInfo("原创或者第一次转发&&&&&&&&&");
                            }

                            String title = strtmep.trim();

                            if (s.getRetweetedStatus() != null) {
                            	title += "//"
                                        + s.getRetweetedStatus()
                                                .getText() + "------转发自新浪微博";
                            } else {
                            	title += "------转发自新浪微博";
                            }

                            /*try {
                                generalInfo.setText(Base64
                                        .encodeToString(str.getBytes("UTF-8"),
                                                Base64.NO_WRAP));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }*/
                            generalInfo.setWeibo_uid(s.getId());
                            

                    	    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
                    	    sdf2.setTimeZone(TimeZone.getTimeZone("GMT"));
                    	    
                            generalInfo.setTime("" + sdf2.format(ss.getFavoritedTime()));
                            // generalInfo.setTitle("转载自新浪微博:" +
                            // time.format(nowTime));
                            generalInfo.setTitle(title);// title和text目前是同一个字符串
                            generalInfo.setText(title);

                            String url = s.getOriginalPic();
                            if (url == null || url.isEmpty()) {
                                if (s.getRetweetedStatus() != null) {
                                    url = s.getRetweetedStatus().getBmiddlePic();
                                }
                            }

                            Log.logInfo("----->>>>>----图片URL:" + url);

                            try {
                                Thread.sleep(3000);
                            } catch (Exception e) {
                                logger.error("Hello,PushMessageSender sleep in getStatuses~~"
                                        + e.toString());
                            }// 等待两秒缓冲
                            
                            if (url != null && !url.isEmpty()) {
                                if(!url.endsWith(".gif")){
                                    boolean getpic = putPic(generalInfo, url);
                                    Log.logInfo("-----------------------------图片处理成功----------"
                                            + getpic);
                                }else{

                                    Log.logInfo("-----------------------------GIF图片不处理---------");
                                }
                            }


                            generalInfoDao.save(generalInfo);

                            if (lastUser_FavarTime == null
                                    || lastUser_FavarTime.isEmpty()) {

                                Log.logInfo("-----------------------------lastUser_FavarTime  is null----------");
                            } else {

                                Log.logInfo("--比较--temp_lastFavarTime----lastUser_FavarTime ------"
                                        + temp_lastFavarTime
                                        + ":"
                                        + lastUser_FavarTime);
                                if (Long.valueOf(temp_lastFavarTime).compareTo(Long
                                        .valueOf(lastUser_FavarTime)) <= 0) {
                                    Log.logInfo("-----------------------------get ok quit----【【【新增的退出】】】----------");

                                    weiboInfo.setWeibo_uid(lastest_FavarTime);
                                    Date date1 = new Date();
                                    weiboInfo.setLast_syncweibo_time(String.valueOf(date1.getTime()));
                                    weiboDao.save(weiboInfo);
                                    break;
                                }
                            }
                        } else {
                            Log.logInfo("------------------重复的帖子：-------------"
                                    + s.getId());
                            

                            if (lastUser_FavarTime == null
                                    || lastUser_FavarTime.isEmpty()) {

                                Log.logInfo("-----------------------------lastUser_FavarTime  is null-----333-----");
                            } else {

                                Log.logInfo("--比较--temp_lastFavarTime----lastUser_FavarTime ---333---"
                                        + temp_lastFavarTime
                                        + ":"
                                        + lastUser_FavarTime);
                                if (Long.valueOf(temp_lastFavarTime).compareTo(Long
                                        .valueOf(lastUser_FavarTime)) <= 0) {
                                    Log.logInfo("-----------------------------get ok quit----【【【新增的退出333】】】----------");

                                    weiboInfo.setWeibo_uid(lastest_FavarTime);
                                    Date date1 = new Date();
                                    weiboInfo.setLast_syncweibo_time(String.valueOf(date1.getTime()));
                                    weiboDao.save(weiboInfo);
                                    break;
                                }
                            }
                        }

                    }
                    Log.logInfo("------------------temp_lastFavarTime-------------"
                            + temp_lastFavarTime);
                    Log.logInfo("总数:" + index++ + ":" + String.valueOf(favors.size()));

                    if (favors.size() < PAGENUM) {
                        if (firstin) {
                            Log.logInfo("-----------------------------get ok quit----firstin & getTotalNumber 0----------");
                            break;
                        }
                        Log.logInfo("-----------------------------get ok quit----< 100------must be out----");

                        weiboInfo.setWeibo_uid(lastest_FavarTime);
                        Date date = new Date();
                        weiboInfo.setLast_syncweibo_time(String.valueOf(date
                                .getTime()));
                        weiboDao.save(weiboInfo);
                        break;

                    }else{

                        if (lastUser_FavarTime == null
                                || lastUser_FavarTime.isEmpty()) {

                            Log.logInfo("-----------------------------lastUser_FavarTime  is null----222------");
                        } else {

                            Log.logInfo("--比较--temp_lastFavarTime----lastUser_FavarTime ---222---"
                                    + temp_lastFavarTime + ":" + lastUser_FavarTime);
                            if (Long.valueOf(temp_lastFavarTime).compareTo(Long
                                    .valueOf(lastUser_FavarTime)) <= 0) {
                                Log.logInfo("-----------------------------get ok quit----【【【新增的退出222】】】----------");

                                weiboInfo.setWeibo_uid(lastest_FavarTime);
                                Date date1 = new Date();
                                weiboInfo.setLast_syncweibo_time(String.valueOf(date1.getTime()));
                                weiboDao.save(weiboInfo);
                                break;
                            }
                        }
                    }

                } catch (WeiboException e) {
                    //e.printStackTrace();
                    StringWriter out = new StringWriter();
                    PrintWriter pwrite = new PrintWriter(out);
                    e.printStackTrace(pwrite);
                    Log.logInfo("-----------------WeiboException------------异常--------------");
                    weiboInfo.setGetAccessTokenOk(false);
                    weiboInfo.setLastErrorReason(out.toString());
                    
                    if(out.toString().contains("connect timed")){
                    	weiboInfo.setGetAccessTokenOk(true);
                        Email.sendMail("juling.jhy@alibaba-inc.com", "超时重新获取");
                        weiboDao.save(weiboInfo);
                        continue;
                    }else{

                        Email.sendMail("juling.jhy@alibaba-inc.com", out.toString());
                    }

                    weiboInfo.setRunningOk(false);//更新状态
                    weiboDao.save(weiboInfo);
                    
                    break;
                }
                Log.logInfo("-----------------------------round end--------------");
            }
        }
        logger.debug("Hello,PushMessageSender End~~");
    }
}
