package in.co.rays.project_3.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.EmployeeDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.EmployeeModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(urlPatterns = { "/ctl/EmployeeCtl" })
public class EmployeeCtl extends BaseCtl {
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(EmployeeCtl.class);

	@Override
	protected boolean validate(HttpServletRequest request) {

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("fullName"))) {
			request.setAttribute("fullName", PropertyReader.getValue("error.require", "fullName"));
			pass = false;
		} else if (!DataValidator.isName(request.getParameter("fullName"))) {
			request.setAttribute("fullName", "Full Name must contain alphabets only");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("username"))) {
			request.setAttribute("username", PropertyReader.getValue("error.require", "username"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("password"))) {
			request.setAttribute("password", PropertyReader.getValue("error.require", "password"));
			pass = false;
		} else if (!DataValidator.isPasswordLength(request.getParameter("password"))) {
			request.setAttribute("password", "Password should be 8 to 12 characters");
			pass = false;
		} else if (!DataValidator.isPassword(request.getParameter("password"))) {
			request.setAttribute("password", "Password Must contain uppercase, lowercase, digit & special character");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("birthDate"))) {
			request.setAttribute("birthDate", PropertyReader.getValue("error.require", "birthDate"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("contactNo"))) {
			request.setAttribute("contactNo", PropertyReader.getValue("error.require", "contactNo"));
			pass = false;
		} else if (!DataValidator.isPhoneNo(request.getParameter("contactNo"))) {
			request.setAttribute("contactNo", "Invalid Contact Number");
			pass = false;
		}

		return pass;
	}

	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {

		EmployeeDTO dto = new EmployeeDTO();

		dto.setFullName(DataUtility.getString(request.getParameter("fullName")));
		dto.setUsername(DataUtility.getString(request.getParameter("username")));
		dto.setPassword(DataUtility.getString(request.getParameter("password")));
		dto.setBirthDate(DataUtility.getDate(request.getParameter("birthDate")));
		dto.setContactNo(DataUtility.getString(request.getParameter("contactNo")));

		populateBean(dto, request);
		return dto;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		long id = DataUtility.getLong(request.getParameter("id"));

		EmployeeModelInt model = ModelFactory.getInstance().getEmployeeModel();

		if (id > 0) {
			try {
				EmployeeDTO dto = model.findByPK(id);
				ServletUtility.setDto(dto, request);
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		}

		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String op = request.getParameter("operation");
		long id = DataUtility.getLong(request.getParameter("id"));

		EmployeeModelInt model = ModelFactory.getInstance().getEmployeeModel();

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

			EmployeeDTO dto = (EmployeeDTO) populateDTO(request);

			try {
				if (id > 0) {
					dto.setId(id);
					model.update(dto);
					ServletUtility.setSuccessMessage("Employee Updated Successfully", request);
				} else {
					model.add(dto);
					ServletUtility.setSuccessMessage("Employee Added Successfully", request);
				}

				ServletUtility.setDto(dto, request);

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setDto(dto, request);
				ServletUtility.setErrorMessage("Username Already Exists", request);
			}

		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.EMPLOYEE_CTL, request, response);
			return;

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.EMPLOYEE_LIST_CTL, request, response);
			return;
		}

		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected String getView() {
		return ORSView.EMPLOYEE_VIEW;
	}

}
