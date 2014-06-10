
package weibo4j.examples.oauth2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import weibo4j.Oauth;
import weibo4j.http.Response;
import weibo4j.model.PostParameter;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;
import weibo4j.util.BareBonesBrowserLaunch;
import weibo4j.util.MyHttpClient;
import weibo4j.util.WeiboConfig;

public class MyOAuth4Code {

    public static String SINA_PK = "EB2A38568661887FA180BDDB5CABD5F21C7BFD59C090CB2D24"
            + "5A87AC253062882729293E5506350508E7F9AA3BB77F4333231490F915F6D63C55FE2F08A49B353F444AD39"
            + "93CACC02DB784ABBB8E42A9B1BBFFFB38BE18D78E87A0E41B9B8F73A928EE0CCEE"
            + "1F6739884B9777E4FE9E88A1BBE495927AC4A799B3181D6442443";
    
    private static String getCurrentTime() {
        long servertime = new Date().getTime() / 1000;
        return String.valueOf(servertime);
    }
    
    public static PreLoginInfo getPreLoginInfo(Response response)
            throws IOException, WeiboException, JSONException {

        String getResp = response.asString();

        int firstLeftBracket = getResp.indexOf("(");
        int lastRightBracket = getResp.lastIndexOf(")");

        String jsonBody = getResp.substring(firstLeftBracket + 1,
                lastRightBracket);
        System.out.println(jsonBody);
        //return jsonBody;
        

        JSONObject jsonInfo = new weibo4j.org.json.JSONObject(jsonBody);
        PreLoginInfo info = new PreLoginInfo();
        info.nonce = jsonInfo.getString("nonce");
        info.pcid = jsonInfo.getString("pcid");
        info.pubkey = jsonInfo.getString("pubkey");
        info.retcode = jsonInfo.getInt("retcode");
        info.rsakv = jsonInfo.getString("rsakv");
        info.servertime = jsonInfo.getLong("servertime");
        return info;

    }
    public static void main(String[] args) throws WeiboException, IOException, JSONException, InterruptedException {
        Oauth oauth = new Oauth();
        // BareBonesBrowserLaunch.openURL(oauth.authorize("code",args[0],args[1]));
        // System.out.println(oauth.authorize("code",args[0],args[1]));
        // BareBonesBrowserLaunch.openURL(oauth.authorize("code","",""));

        MyHttpClient myclient = new MyHttpClient();

        System.out.println(Oauth.authorize("code", "", ""));
        Response response = myclient.post(Oauth.authorize("code", "", ""), new PostParameter[] {},
                false);
        System.out.println(response.getStatusCode());
        // https://api.weibo.com/oauth2/authorize?client_id=736045872&redirect_uri=http://www.baidu.com&response_type=code&state=&scope=

        Thread.sleep(2000);
        
        PostParameter pars[] = new PostParameter[] {
                new PostParameter("_", getCurrentTime()),
                new PostParameter("callback", "sinaSSOController.preloginCallBack"),
                new PostParameter("checkpin", "1"),
                new PostParameter("client", "ssologin.js(v1.4.5)"),
                new PostParameter("entry", "openapi"),
                new PostParameter("rsakt", "mod"),
                new PostParameter("su", "MTUzNjUwMzM4NjE=")
        };

        response = myclient.get("https://login.sina.com.cn/sso/prelogin.php", pars ,false);
        //System.out.println(response.getStatusCode());    
        
        // HttpPost post = new HttpPost(
        // "http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.2)");
        String temp = "https://login.sina.com.cn/sso/login.php?entry=openapi&" +
                "gateway=1&from=&savestate=0&useticket=1&pagerefer=" +
                "&ct=1800&s=1&vsnf=1&vsnval=&door=&appkey=1bpmO4&" +
                "su=MTUzNjUwMzM4NjE%3D&service=miniblog&servertime=1378265635" +
                "&nonce=36G189&pwencode=rsa2&rsakv=1330428213&" +
                "sp=a4a22e5df835fbac57514c707c0622ab30cb295cb1" +
                "cad40a9e252b89b258673cad8ea6121ebd2511" +
                "b966ddaa362bcc5435bb7b8a658b6548a08e93" +
                "c0a9d48ef1ee42c55e34351d6d6ce34963f9610e8" +
                "e3674390e29ee4a763c15ac6f76fa57ee646e1d1b31b4b" +
                "0b30c7c983a9f9ae04dd9583eae5627122639a3ce5db1e2a236&" +
                "encoding=UTF-8&" +
                "callback=sinaSSOController.loginCallBack&" +
                "cdult=2&domain=weibo.com&prelt=5179&" +
                "returntype=TEXT&client=ssologin.js(v1.4.5)" +
                "&_=1378265638332";

        PreLoginInfo info = getPreLoginInfo(response);
        // try {
        // info = getPreLoginBean(client);
        // } catch (HttpException e) {
        // System.out.println(e);
        // } catch (IOException e) {
        // System.out.println(e);
        // } catch (JSONException e) {
        // System.out.println(e);
        // }

        long servertime = info.servertime;
        String nonce = info.nonce;
        String pwdString = servertime + "\t" + nonce + "\n" + "Jhyjhl8588";
        String sp = new BigIntegerRSA().rsaCrypt(SINA_PK, "10001", pwdString);
        System.out.println(sp);

        Thread.sleep(2000);
        
        PostParameter parslog[] = new PostParameter[] {

                new PostParameter("_", getCurrentTime()),
                new PostParameter("entry", "openapi"),

                new PostParameter("gateway", "1"),
                new PostParameter("savestate", "0"),
                new PostParameter("useticket", "1"),
                new PostParameter("ct", "1800"),
                new PostParameter("domain", "weibo.com"),
                
                new PostParameter("s", "1"),
                new PostParameter("vsnf", "1"),
                new PostParameter("service", "miniblog"),
                
                new PostParameter("appkey", "1bpmO4"),
                
                new PostParameter("servertime", info.servertime),
                new PostParameter("nonce", info.nonce),
                
                new PostParameter("rsakv", info.rsakv),
                new PostParameter("pwencode", "rsa2"),
                new PostParameter("encoding", "UTF-8"),
                new PostParameter("callback", "sinaSSOController.loginCallBack"),
                new PostParameter("su", "MTUzNjUwMzM4NjE="),
                new PostParameter("sp", sp),
                new PostParameter("returntype", "TEXT"),
                new PostParameter("client", "ssologin.js(v1.4.5)"),
                new PostParameter("_", getCurrentTime())
        };   
        
        
        response = myclient.get("https://login.sina.com.cn/sso/login.php", parslog ,false);
        //System.out.println(response.getStatusCode());   
        
        
        
        //System.out.print("Hit enter when it's done.[Enter]:");
        //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //String code = br.readLine();
        //String code = sp;
        // String code = "91cc5fa4888a1b032b6c95b065a4ff55";
        //Log.logInfo("code: " + code);
//        try {
//            System.out.println(oauth.getAccessTokenByCode(code));
//        } catch (WeiboException e) {
//            if (401 == e.getStatusCode()) {
//                Log.logInfo("Unable to get the access token.");
//            } else {
//                e.printStackTrace();
//            }
//        }
    }
}
