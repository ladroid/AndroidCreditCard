package com.example.lado.banksystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.stripe.android.model.Token;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by lado on 7/4/18.
 */

public class DataBaseTask extends AsyncTask<String, Void, String>{
    private static final String TAG = "DataBaseTask";

    // Just in case you want a ProgressDialog uncomment this and the associated code
    private ProgressDialog pDialog;

    private DataBaseTaskListener mListener;

    Context mContext;
    String mCommand = "";
    Token mToken;


    public DataBaseTask(Context context, String command, Token token, DataBaseTaskListener listener){
        this.mContext = context;
        this.mCommand = command;
        this.mToken = token;
        this.mListener = listener;
    }


    @Override
    protected String doInBackground(String... params) {
        String result = "";
        try {
            // Put your code here!!
            if (mCommand.equals("SANDTOKEN")) {
                try {
                    // Load CAs from an InputStream
                    // (could be from a resource or ByteArrayInputStream or ...)
                    Log.e("here", "haha");
                    CertificateFactory cf = CertificateFactory.getInstance("X.509");
                    Log.e("lol", "stop");
                    InputStream caInput = new BufferedInputStream(mContext.getAssets().open("cert.pem"));
                    //InputStream caInput = new BufferedInputStream(fileInputStream);
                    Certificate ca = cf.generateCertificate(caInput);
                    System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
                    Log.e("and here", "haha");
                    Log.e("ca", "ca= " + ((X509Certificate) ca).getSubjectDN().toString());

                    // Create a KeyStore containing our trusted CAs
                    String keyStoreType = KeyStore.getDefaultType();
                    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                    keyStore.load(null, null);
                    keyStore.setCertificateEntry("ca", ca);

                    // Create a TrustManager that trusts the CAs in our KeyStore
                    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                    TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
                    tmf.init(keyStore);

                    // Create an SSLContext that uses our TrustManager
                    SSLContext context = SSLContext.getInstance("TLS");
                    context.init(null, tmf.getTrustManagers(), null);

                    // Tell the URLConnection to use a SocketFactory from our SSLContext
                    //URL url = new URL(urlString);
                    URL url = new URL("https://192.168.1.59:4567/charge");

                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    connection.setSSLSocketFactory(context.getSocketFactory());

                    StringBuilder builder = new StringBuilder();
                    builder.append(URLEncoder.encode("stripeToken", "UTF-8"));
                    builder.append("=");
                    builder.append(URLEncoder.encode(mToken.getId(), "UTF-8"));
                    String urlParameters = builder.toString();

                    Log.e("String param ", urlParameters);

                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);

                    DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());

                    dStream.writeBytes(urlParameters);
                    dStream.flush();
                    dStream.close();

                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    StringBuilder responseOutput = new StringBuilder();

                    while ((line = br.readLine()) != null) {
                        Log.e("lol", "look me");
                        Log.e("DatabaseTask", line);
                        responseOutput.append(line);
                    }
                    br.close();
                    result = responseOutput.toString();
                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage());
                }
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
        return result;
    }



    @Override
    protected void onPostExecute(String result) {
        try{
            // In case you want a ProgressDialog
            pDialog.dismiss();

            // Trigger the listener for the call back sending the result
            mListener.onCompletedSendData(result);

            Log.e(TAG, "onPostExecute result: " + result);
        }
        catch(Exception ex){
            Log.e(TAG, ex.getMessage());
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // In case you want a ProgressDialog
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Sending Data...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }
}



//new Example().execute();
//        AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
//            @Override
//            protected String doInBackground(String... params) {
//                Log.e("Stop here", "Stop here");
//
//                String echoData = "";
//
//                if (command.equals("SANDTOKEN")) {
//                    try {
//                        // Load CAs from an InputStream
//                        // (could be from a resource or ByteArrayInputStream or ...)
//                        CertificateFactory cf = CertificateFactory.getInstance("X.509");
//
//                        // My CRT file that I put in the assets folder
//                        // I got this file by following these steps:
//                        // * Go to https://littlesvr.ca using Firefox
//                        // * Click the padlock/More/Security/View Certificate/Details/Export
//                        // * Saved the file as littlesvr.crt (type X.509 Certificate (PEM))
//                        // The MainActivity.context is declared as:
//                        // public static Context context;
//                        // And initialized in MainActivity.onCreate() as:
//                        // MainActivity.context = getApplicationContext();
//
////                        String fileName = "Download/cert.pem";
////                        String path = Environment.getExternalStorageDirectory()+"/"+fileName;
////                        File file = new File(path);
////                        FileInputStream fileInputStream = new FileInputStream(file);
//
//                        InputStream caInput = new BufferedInputStream(DataBaseTask.this.getAssets().open("cert.pem"));
//                        Certificate ca = cf.generateCertificate(caInput);
//                        System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
//
//                        // Create a KeyStore containing our trusted CAs
//                        String keyStoreType = KeyStore.getDefaultType();
//                        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
//                        keyStore.load(null, null);
//                        keyStore.setCertificateEntry("ca", ca);
//
//                        // Create a TrustManager that trusts the CAs in our KeyStore
//                        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
//                        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
//                        tmf.init(keyStore);
//
//                        // Create an SSLContext that uses our TrustManager
//                        SSLContext context = SSLContext.getInstance("TLS");
//                        context.init(null, tmf.getTrustManagers(), null);
//
//                        // Tell the URLConnection to use a SocketFactory from our SSLContext
//                        //URL url = new URL(urlString);
//                        //HttpsURLConnection urlConnection = (HttpsURLConnection)url.openConnection();
//                        //urlConnection.setSSLSocketFactory(context.getSocketFactory());
//                        //URL url = new URL("https://.../StripeConnection.php"); //there is a connection between the code and PHP script. This is tested.
//                        URL url = new URL("https://192.168.1.59:4567/charge");
//
//                        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
//                        connection.setSSLSocketFactory(context.getSocketFactory());
//
//                        StringBuilder builder = new StringBuilder();
//                        builder.append(URLEncoder.encode("stripeToken", "UTF-8"));
//                        builder.append("=");
//                        builder.append(URLEncoder.encode(token.getId(), "UTF-8"));
//                        String urlParameters = builder.toString();
//
//                        Log.e("String param ", urlParameters);
//
//                        connection.setRequestMethod("POST");
//                        connection.setDoOutput(true);
//
//                        DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//
//                        dStream.writeBytes(urlParameters);
//                        dStream.flush();
//                        dStream.close();
//
//                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                        String line = "";
//                        StringBuilder responseOutput = new StringBuilder();
//
//                        while ((line = br.readLine()) != null) {
//                            Log.e("DatabaseTask", line);
//                            responseOutput.append(line);
//                        }
//                        br.close();
//                        echoData = responseOutput.toString();
//
//                    } catch (MalformedURLException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (CertificateException e) {
//                        e.printStackTrace();
//                    } catch (NoSuchAlgorithmException e) {
//                        e.printStackTrace();
//                    } catch (KeyStoreException e) {
//                        e.printStackTrace();
//                    } catch (KeyManagementException e) {
//                        e.printStackTrace();
//                    }
//                }
//                return echoData;
//            }
//            @Override
//            protected void onPostExecute(String mData) {
//                Log.e("DatabaseTask", "onPostExecute result: " + mData);
//            }
//        };
//        if(Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/) {
//            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        } else {
//            task.execute();
//        }



//{
//    @Override
//    protected String doInBackground(String... params) {
//        Log.e("Start here", "Start or not");
//        String echoData = "";
//        if(command.equals("SENDTOKEN")) {
//
//            org.apache.http.client.HttpClient httpclient = new DefaultHttpClient();
//            HttpPost httpPost = new HttpPost("http://localhost:4567/charge");
//
//            try {
//
//                //add data
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
//                //nameValuePairs.add(new BasicNameValuePair("data", data[0]));
//                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//                //execute http post
//                HttpResponse response = httpclient.execute(httpPost);
//
//                StringBuilder builder = new StringBuilder();
//                builder.append(URLEncoder.encode("stripeToken", "UTF-8"));
//                builder.append("=");
//                builder.append(URLEncoder.encode(token.getId(), "UTF-8"));
//                String urlParameters = builder.toString();
//
//                Log.e("urlParameters", urlParameters);
//
//                connection.setRequestMethod("/charge");
//                connection.setDoOutput(true);
//                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//
//                dStream.writeBytes(urlParameters);
//                dStream.flush();
//                dStream.close();
//
//                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                String line = "";
//                StringBuilder responseOutput = new StringBuilder();
//
//                while ((line = br.readLine()) != null) {
//                    Log.e("DatabaseTask", line);
//                    responseOutput.append(line);
//                }
//                br.close();
//
//                echoData = responseOutput.toString();
//            }
//            catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//
//    @Override
//    protected String doInBackground(String... strings) {
//
//        Log.e("Stop here", "Stop here");
//
//        String echoData = "";
//
//        if (command.equals("SANDTOKEN")) {
//            try {
//                //URL url = new URL("https://.../StripeConnection.php"); //there is a connection between the code and PHP script. This is tested.
//                URL url = new URL("http://localhost:4567/charge");
//
//                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
//
//                StringBuilder builder = new StringBuilder();
//                builder.append(URLEncoder.encode("stripeToken", "UTF-8"));
//                builder.append("=");
//                builder.append(URLEncoder.encode(token.getId(), "UTF-8"));
//                String urlParameters = builder.toString();
//
//                Log.e("String param ", urlParameters);
//
//                connection.setRequestMethod("POST");
//                connection.setDoOutput(true);
//                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//
//                dStream.writeBytes(urlParameters);
//                dStream.flush();
//                dStream.close();
//
//                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                String line = "";
//                StringBuilder responseOutput = new StringBuilder();
//
//                while ((line = br.readLine()) != null) {
//                    Log.e("DatabaseTask", line);
//                    responseOutput.append(line);
//                }
//                br.close();
//                echoData = responseOutput.toString();
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return echoData;
//    }
//
//    protected void onPostExecute(String mData) {
//        Log.e("DatabaseTask", "onPostExecute result: " + mData);
//    }
//        }


//class Example extends AsyncTask<String, Void, String> {
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//        Log.e("AsyncTask", "onPreExecute");
//    }
//
//    @Override
//    protected String doInBackground(String... params) {
//        //final Context contexting = DataBaseTask.this;
//        //Log.e("Context", contexting.toString());
//        Log.e("Stop here", "Stop here");
//
//        String echoData = "";
//
//        if (command.equals("SANDTOKEN")) {
//            try {
//                // Load CAs from an InputStream
//                // (could be from a resource or ByteArrayInputStream or ...)
//                CertificateFactory cf = CertificateFactory.getInstance("X.509");
//
////                    String fileName = "Download/cert.pem";
////                    String path = Environment.getExternalStorageDirectory()+"/"+fileName;
////                    File file = new File(path);
////                    FileInputStream fileInputStream = new FileInputStream(file);
//
//                InputStream caInput = new BufferedInputStream(contexttt.getAssets().open("cert.pem"));
//                //InputStream caInput = new BufferedInputStream(fileInputStream);
//                Certificate ca = cf.generateCertificate(caInput);
//                System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
//                Log.e("ca", "ca= " + ((X509Certificate) ca).getSubjectDN());
//
//                // Create a KeyStore containing our trusted CAs
//                String keyStoreType = KeyStore.getDefaultType();
//                KeyStore keyStore = KeyStore.getInstance(keyStoreType);
//                keyStore.load(null, null);
//                keyStore.setCertificateEntry("ca", ca);
//
//                // Create a TrustManager that trusts the CAs in our KeyStore
//                String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
//                TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
//                tmf.init(keyStore);
//
//                // Create an SSLContext that uses our TrustManager
//                SSLContext context = SSLContext.getInstance("TLS");
//                context.init(null, tmf.getTrustManagers(), null);
//
//                // Tell the URLConnection to use a SocketFactory from our SSLContext
//                //URL url = new URL(urlString);
//                URL url = new URL("https://192.168.1.59:4567/charge");
//
//                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
//                connection.setSSLSocketFactory(context.getSocketFactory());
//
//                StringBuilder builder = new StringBuilder();
//                builder.append(URLEncoder.encode("stripeToken", "UTF-8"));
//                builder.append("=");
//                builder.append(URLEncoder.encode(token.getId(), "UTF-8"));
//                String urlParameters = builder.toString();
//
//                Log.e("String param ", urlParameters);
//
//                connection.setRequestMethod("POST");
//                connection.setDoOutput(true);
//
//                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//
//                dStream.writeBytes(urlParameters);
//                dStream.flush();
//                dStream.close();
//
//                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                String line = "";
//                StringBuilder responseOutput = new StringBuilder();
//
//                while ((line = br.readLine()) != null) {
//                    Log.e("DatabaseTask", line);
//                    responseOutput.append(line);
//                }
//                br.close();
//                echoData = responseOutput.toString();
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (CertificateException e) {
//                e.printStackTrace();
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            } catch (KeyStoreException e) {
//                e.printStackTrace();
//            } catch (KeyManagementException e) {
//                e.printStackTrace();
//            } catch (NullPointerException e) {
//                e.printStackTrace();
//            }
//        }
//        return echoData;
//    }
//    @Override
//    protected void onPostExecute(String mData) {
//        Log.e("DatabaseTask", "onPostExecute result: " + mData);
//    }
//}
