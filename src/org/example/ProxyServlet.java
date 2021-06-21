package org.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProxyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final String USER_AGENT = "Mozilla/5.0";   
    
    private static final String MY_URL = "http://localhost:8081";
    
    public ProxyServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                                //  Create Get request dynamically to remote server
    	String queryString = request.getQueryString();
    	queryString = (queryString == null) ? "" : "?"+queryString ;
    	
    	
        String new_url = MY_URL+request.getAttribute("uri")+queryString;
         
        System.out.println("new_url="+new_url);
        URL obj = new URL(new_url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//     // 設定是否從httpUrlConnection讀入，預設情況下是true;
//        con.setDoInput(true);
//        // 設定是否向httpUrlConnection輸出
//        con.setDoOutput(true);
//        // Post 請求不能使用快取
//        con.setUseCaches(false); // 設定請求內容型別
//        String boundary = "*****";
//        con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
//        
      
        
        // optional default is GET
        con.setRequestMethod("GET");
 
        Enumeration<String> names = request.getHeaderNames();
       
        List<String> list = Arrays.asList( "if-modified-since", "if-none-match");
        
        while(names.hasMoreElements())
        {
        	
        	String name = names.nextElement();
        	

        	if (list.contains(name))
        	{
        		System.out.println("*heads -> "+name+"="+ request.getHeader(name) +" (ignored.)");
        	}else {
        	System.out.println("heads -> "+name+"="+ request.getHeader(name));
        	
        	con.setRequestProperty(name,request.getHeader(name) );
        	}
        	
        }
         
        //con.setRequestProperty("Cache-Control",request.getHeader("no-store;") );
        
        //add Sending request
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + new_url);
        System.out.println("Response Code : " + responseCode);
        System.out.println("Response Msg : " + con.getResponseMessage());
        
        
        // handle with response Headers
        for (Map.Entry<String, List<String>> entries : con.getHeaderFields().entrySet()) {
            String values = "";
            for (String value : entries.getValue()) {
                values += value + ",";
            }
            System.out.println("Response headers ->"+ entries.getKey() + "=" +  values );
        	// overwrite
        	response.setHeader(entries.getKey()  , values);
        }
	        
        // handle with response Body
        InputStream is = null ; 
        if(responseCode ==200  ){
        	System.out.println("Getting inputstream");
          is = con.getInputStream();
       }else{
    	   System.out.println("Getting errorStream");
    	   is = con.getErrorStream();
       }
        
        if(is==null)
        	System.out.println("InputStream is null");
 
        ServletOutputStream sout = response.getOutputStream();
        
     // opens an output stream to save into file
        int BUFFER_SIZE = 4096;
        int bytesRead = -1;
        byte[] buffer = new byte[BUFFER_SIZE];
        //if (is !=null ) {
	        while ((bytesRead = is.read(buffer)) != -1) {
	        	sout.write(buffer, 0, bytesRead);
	        }
	        is.close();
        //}
        
       
        sout.flush();
        
        // handle with response code
        response.setStatus(responseCode);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                                //  Create Post request dynamically to remote server
        String url = "http://ipaddress:port/contextpath"+request.getAttribute("uri");
        
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
        //add reuqest header
        con.setRequestMethod("POST");
        Enumeration<String> names = request.getHeaderNames();
        while(names.hasMoreElements())
        {
        	String name = names.nextElement();
        	System.out.println("heads -> "+name+"="+ request.getHeader(name));
        	con.setRequestProperty(name,request.getHeader(name) );
        }
 
        StringBuilder sb = new StringBuilder();
          for(Entry<String, String[]> e : request.getParameterMap().entrySet()){
              if(sb.length() > 0){
                  sb.append('&');
              }
              String[] temp =e.getValue();
              for(String s:temp) {
                  sb.append(URLEncoder.encode(e.getKey(), "UTF-8")).append('=').append(URLEncoder.encode(s, "UTF-8"));  
              }
          }
        
        String urlParameters = sb.toString();
 
        
        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();
 
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);
 
        // handle with response Headers
        for (Map.Entry<String, List<String>> entries : con.getHeaderFields().entrySet()) {
            String values = "";
            for (String value : entries.getValue()) {
                values += value + ",";
            }
            System.out.println("Response headers ->"+ entries.getKey() + "=" +  values );
        	// overwrite
        	response.setHeader(entries.getKey()  , values);
        }
	        
        // handle with response Body
        InputStream is = null ; 
        if(responseCode == 200 ){
        	is = con.getInputStream();
       }else{
    	   is = con.getErrorStream();
       }
        InputStreamReader isreader = new InputStreamReader(is);
        BufferedReader in = new BufferedReader(isreader);
        String inputLine;
        StringBuffer response1 = new StringBuffer();
 
        ServletOutputStream sout = response.getOutputStream();
        
        while ((inputLine = in.readLine()) != null) {
            response1.append(inputLine);
            sout.write(inputLine.getBytes());
        }
        in.close();
        sout.flush();
        
        // handle with response code
        response.setStatus(responseCode);
    }

}