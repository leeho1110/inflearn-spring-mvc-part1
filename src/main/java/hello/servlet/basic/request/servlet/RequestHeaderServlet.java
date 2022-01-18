package hello.servlet.basic.request;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "requestHeaderServlet", urlPatterns = "/request-header")
public class RequestHeaderServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String method = request.getMethod();
		String protocol = request.getProtocol();
		String uri = request.getRequestURI();
		String remoteUser = request.getRemoteUser();

		Enumeration<String> headerNames = request.getHeaderNames();
		Cookie[] cookies = request.getCookies();
		HttpSession session = request.getSession();
	}

	
}
