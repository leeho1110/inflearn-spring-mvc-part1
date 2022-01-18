package hello.servlet.basic.request;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "requestParamServlet", urlPatterns = "/request-param")
public class RequestParamServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.getParameterNames().asIterator()
			.forEachRemaining(paramName-> System.out.println("request = " + request.getParameter(paramName)));

		String userName = request.getParameter("userName");
		System.out.println("userName = " + userName);

		String[] userNames = request.getParameterValues("userName");
		for (String name : userNames) {
			System.out.println("name = " + name);
		}
	}
}
