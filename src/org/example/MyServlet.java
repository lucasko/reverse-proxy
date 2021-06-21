//package org.example;
//
//
//import java.io.IOException;
//import java.io.PrintWriter;
//
//import javax.servlet.RequestDispatcher;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//@WebServlet("/test")
//public class MyServlet extends HttpServlet {
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//
//   	// RequestDispatcher rd = req.getRequestDispatcher("http://hivdb.stanford.edu/DR/txt/A3GF.txt");
//	 
//
//    	System.out.println("\n\n");
//    	System.out.println("getRequestURL="+req.getRequestURL());
//    	System.out.println("getRequestURI="+req.getRequestURI());
//    	System.out.println("getRemoteHost="+req.getRemoteHost());
//    	System.out.println(req.getRemoteHost());
//    	System.out.println("getContextPath="+req.getContextPath());
//    	System.out.println(req.getServletContext());
//    	System.out.println(req.getServerName());
//    	System.out.println(req.getServletPath());
//    	System.out.println(req.getServletContext());
//    	
//    	
//    	
//    	RequestDispatcher rd = req.getRequestDispatcher("test.jsp");
//    	
//    	 rd.forward(req, resp);
//        //https://hivdb.stanford.edu/DR/txt/
//        
//    }
//}