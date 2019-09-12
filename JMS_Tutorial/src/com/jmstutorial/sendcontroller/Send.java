package com.jmstutorial.sendcontroller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
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
 * Servlet implementation class Send
 */
public class Send extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String msg = request.getParameter("msg");
		
		Connection con = null;
		
		try {
			Context initcx = new InitialContext();
			Queue que = (Queue) initcx.lookup("java:/que");
			Destination dest = (Destination)que;
			
			QueueConnectionFactory qcf = (QueueConnectionFactory) initcx.lookup("java:/ConnectionFactory");
			con = qcf.createConnection();
			
			Session session = con.createSession(false,Session.AUTO_ACKNOWLEDGE);
			MessageProducer producer = session.createProducer(dest);
			TextMessage message = session.createTextMessage(msg);
			producer.send(message);
			
			System.out.println("Message Sent : "+message.getText());
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<html><body>");
			out.println("Message sent successfully.");
			out.println("Click <a href=Receive>here</a> to receive the message.");
			out.println("</body></html>");
		} 
		catch (Exception e) {
			System.err.println("Exception occured "+e.toString());
		}
		
		finally {
			try
			{
				con.close();
			}
			catch(JMSException e){
				e.printStackTrace();
			}
		}
		
	}

}
