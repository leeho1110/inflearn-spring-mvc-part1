package hello.servlet.basic.response;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "responseHeaderServlet", urlPatterns = "/response-header")
public class ResponseHeaderServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws
		ServletException,
		IOException {

		// [status-line]
		response.setStatus(HttpServletResponse.SC_OK);

		// [response-headers]
		response.setContentType("text/pain");
		response.setCharacterEncoding("utf-8");

		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("my-header", "hello");

		// [Cookie]
		Cookie cookie = new Cookie("key", "value");
		cookie.setMaxAge(600);
		response.addCookie(cookie);

		// [redirect]
		response.sendRedirect("/basic/hello-form.html");

		// [response-message-body]
		PrintWriter writer = response.getWriter();
		writer.println("ok");

	}

}
