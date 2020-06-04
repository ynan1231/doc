package xx;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpUtil {
    static int k=0;

    /**
     *          记得机上依赖
     *          <dependency>
     *             <groupId>commons-httpclient</groupId>
     *             <artifactId>commons-httpclient</artifactId>
     *             <version>3.1</version>
     *         </dependency>
     * @param args
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException, InterruptedException {

//        ExecutorService service = Executors.newFixedThreadPool(10);
//        CountDownLatch countDownLatch = new CountDownLatch(10);
//        for (int i=0;i<10;i++) {
//            Runnable thread = new Runnable() {
//                public void run() {
//                    try {
//                        get();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            };
//            countDownLatch.countDown();
//            service.execute(thread);
//        }
//        countDownLatch.await();
//        service.shutdown();

        get();
    }

    public static void get() throws IOException {
        HttpClient httpClient = new HttpClient();
        //PostMethod postMethod= new PostMethod("http://localhost:8000/index");
        GetMethod postMethod = new GetMethod("http://localhost:8000/index");
        //getMethod.addRequestHeader("Cookie","mark=oldmanxx");

        int statusCode = httpClient.executeMethod(postMethod);
        byte[] responseBody = postMethod.getResponseBody();
        String response = new String(responseBody, "utf-8");
        System.out.println("-----------response:" + response);
    }
}
