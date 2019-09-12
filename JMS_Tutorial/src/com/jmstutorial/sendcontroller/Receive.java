package com.jmstutorial.sendcontroller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Receive
 */
public class Receive extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Connection con =null;
		
		try {
			Context initcx = new InitialContext();
			Queue que = (Queue) initcx.lookup("java:/que");
			Destination dest = que;
			
			QueueConnectionFactory qcf = (QueueConnectionFactory) initcx.lookup("java:/ConnectionFactory");
			con = qcf.createConnection();
			
			Session session = con.createSession(false,Session.AUTO_ACKNOWLEDGE);
			MessageConsumer consumer = session.createConsumer(dest);
			con.start();
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<html><body>");
			
			while(true) {
				Message m = consumer.receive(1);
				if(m != null) {
					if(m instanceof TextMessage) {
						TextMessage message = (TextMessage) m;
						System.out.println("Reading Message : "+message.getText());
						out.println("Reading Message : "+message.getText()+"<br>");
					}
					
				}
				else {
					break;
				}
				
			}
			out.println(" To send message please <a href=jmshome.html>Click Here!</a>");
			out.println("</body></html>");
			
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				con.close();
			}
			catch(JMSException e) {
				e.printStackTrace();
			}
		}
		
		
	}

}
