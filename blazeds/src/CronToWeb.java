//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.util.EntityUtils;

/**
 * How to send a request directly using {@link HttpClient}.
 *
 * @since 4.0
 */
public class CronToWeb {

    public static void main(String[] args) throws Exception {

        if (args == null || args.length == 0) {
            System.out.println("Useage : java CronToWeb http://.....");
            return;
        }
//        HttpClient httpclient = new DefaultHttpClient();
//        try {
//            HttpGet httpget = new HttpGet(args[0]);
//
//            System.out.println("executing request " + httpget.getURI());
//            HttpResponse response = httpclient.execute(httpget);
//            HttpEntity entity = response.getEntity();
//
//            System.out.println("----------------------------------------");
//            System.out.println(response.getStatusLine());
//            if (entity != null) {
//            	System.out.println(EntityUtils.toString(entity));
//            }
//            System.out.println("----------------------------------------");
//
//            // Do not feel like reading the response body
//            // Call abort on the request object
//            httpget.abort();
//        } finally {
//            // When HttpClient instance is no longer needed,
//            // shut down the connection manager to ensure
//            // immediate deallocation of all system resources
//            httpclient.getConnectionManager().shutdown();
//        }

    }

}

