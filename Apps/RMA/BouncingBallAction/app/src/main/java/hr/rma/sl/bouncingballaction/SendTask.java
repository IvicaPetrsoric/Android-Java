package hr.rma.sl.bouncingballaction;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Sandi on 9.3.2016..
 */
public class SendTask extends AsyncTask<String, String, String> {

    String urlAddress;
    String user;
    String gadget;
    String datetime;
    long highscore;

    private HttpURLConnection conn;
    public static final int CONNECTION_TIMEOUT = 15 * 1000;
    private NetworkOperationFinished myNetworkOpeartionListener;
    String finalResponse="";

    // Constructor
    public SendTask(String url, String user, String gadget, String datetime, long highscore) {
        this.urlAddress = url;
        this.user = user;
        this.gadget = gadget;
        this.datetime = datetime;
        this.highscore = highscore;
    }


    public interface NetworkOperationFinished {
        void onNetworkOperationFinished(String response);
    }

    public void setNetworkOperationFinished(NetworkOperationFinished inputListener){
        this.myNetworkOpeartionListener = inputListener;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // do stuff before posting data
    }


    @Override
    protected String doInBackground(String... strings) {
        try {
            postData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(String lenghtOfFile) {
        // do stuff after posting data
        System.out.println("Sent from Android...");

        // tell parent activity that network operation finished!
        if (myNetworkOpeartionListener != null)
            myNetworkOpeartionListener.onNetworkOperationFinished(finalResponse);
    }


    // Method that sends data (in background)
    private void postData() {
        finalResponse="";

        try {
            System.out.println("Sending to: " + urlAddress);

            String values = preparePostData();
            System.out.println("To send: " + values);

            URL url = new URL(null, urlAddress);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(CONNECTION_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            //conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            conn.connect();

            OutputStream os = conn.getOutputStream();
            OutputStreamWriter osWriter = new OutputStreamWriter(os, "UTF-8");
            BufferedWriter writer = new BufferedWriter(osWriter);
            writer.write(values); // output!
            writer.flush();
            writer.close();
            os.close();

            // Response:
            InputStream is = conn.getInputStream();
            InputStreamReader isReader = new InputStreamReader(is, "UTF-8");
            BufferedReader reader = new BufferedReader(isReader);

            String result = "";
            String line = "";
            while ((line = reader.readLine()) != null) {
                result += line;
            }
            System.out.println("Server Response: " + result);
            Thread.sleep(1000);

            if (result.equals("200"))
            {
                finalResponse=result;
            }

        } catch (MalformedURLException e) {
            finalResponse="";
        } catch (IOException e) {
            finalResponse="";
        } catch (InterruptedException e) {
            finalResponse="";
        }
    }


    public String preparePostData() {
        StringBuilder builder = new StringBuilder();
        try {
            builder.append(URLEncoder.encode("applicationid", "UTF-8"));
            builder.append("=");
            builder.append(URLEncoder.encode("BouncingBall", "UTF-8"));
            builder.append("&");
            builder.append(URLEncoder.encode("user", "UTF-8"));
            builder.append("=");
            builder.append(URLEncoder.encode(user, "UTF-8"));
            builder.append("&");
            builder.append(URLEncoder.encode("gadget", "UTF-8"));
            builder.append("=");
            builder.append(URLEncoder.encode(gadget, "UTF-8"));
            builder.append("&");
            builder.append(URLEncoder.encode("datetime", "UTF-8"));
            builder.append("=");
            builder.append(URLEncoder.encode(datetime, "UTF-8"));
            builder.append("&");
            builder.append(URLEncoder.encode("highscore", "UTF-8"));
            builder.append("=");
            builder.append(URLEncoder.encode(Long.toString(highscore), "UTF-8"));
        } catch (UnsupportedEncodingException e) {

        }
        return builder.toString();
    }


}


