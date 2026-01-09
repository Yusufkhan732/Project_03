package in.co.rays.project_3.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.util.ServletUtility;

/**
 * Error functionality controller.perform error page operation
 * 
 * @author Yusuf Khan
 *
 */
@WebServlet(name = "ErrorCtl", urlPatterns = { "/ErrorCtl" })
public class ErrorCtl extends BaseCtl {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Concept of Display logic
	 *
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Exception e = (Exception) request.getAttribute("exception");

		if (e instanceof ApplicationException) {
			ServletUtility.setErrorMessage("Database service is temporarily unavailable. Please try later.", request);
		} else {
			ServletUtility.setErrorMessage("Something went wrong. Contact administrator.", request);
		}

		ServletUtility.forward(ORSView.ERROR_VIEW, request, response);
	}

	/**
	 * Concept of submit logic
	 *
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		ServletUtility.forward(getView(), request, response);

	}

	@Override
	protected String getView() {
		return ORSView.ERROR_VIEW;
	}

}
