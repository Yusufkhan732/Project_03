package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.EmployeeDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.model.EmployeeModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(name = "EmployeeListCtl", urlPatterns = { "/ctl/EmployeeListCtl" })
public class EmployeeListCtl extends BaseCtl {
	private static Logger log = Logger.getLogger(EmployeeListCtl.class);

	@Override
	protected void preload(HttpServletRequest request) {

		EmployeeModelInt model = ModelFactory.getInstance().getEmployeeModel();

		try {
			List list = model.list();
			request.setAttribute("empList", list);
		} catch (Exception e) {
			log.error(e);
		}
	}

	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {

		EmployeeDTO dto = new EmployeeDTO();

		dto.setFullName(DataUtility.getString(request.getParameter("fullName")));
		dto.setUsername(DataUtility.getString(request.getParameter("username")));
		dto.setBirthDate(DataUtility.getDate(request.getParameter("birthDate")));
		dto.setContactNo(DataUtility.getString(request.getParameter("contactNo")));

		populateBean(dto, request);
		return dto;
	}

	/**
	 * Display logic
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("EmployeeListCtl doGet Start");

		List list;
		List next;

		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		EmployeeDTO dto = (EmployeeDTO) populateDTO(request);

		EmployeeModelInt model = ModelFactory.getInstance().getEmployeeModel();

		try {
			list = model.search(dto, pageNo, pageSize);
			next = model.search(dto, pageNo + 1, pageSize);

			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found", request);
			}

			if (next == null || next.size() == 0) {
				request.setAttribute("nextListSize", 0);
			} else {
				request.setAttribute("nextListSize", next.size());
			}

			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}

		log.debug("EmployeeListCtl doGet End");
	}

	/**
	 * Submit logic
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("EmployeeListCtl doPost Start");

		List list;
		List next;

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		EmployeeDTO dto = (EmployeeDTO) populateDTO(request);

		String op = DataUtility.getString(request.getParameter("operation"));
		String[] ids = request.getParameterValues("ids");

		EmployeeModelInt model = ModelFactory.getInstance().getEmployeeModel();

		try {

			if (OP_SEARCH.equalsIgnoreCase(op) || OP_NEXT.equalsIgnoreCase(op) || OP_PREVIOUS.equalsIgnoreCase(op)) {

				if (OP_SEARCH.equalsIgnoreCase(op)) {
					pageNo = 1;
				} else if (OP_NEXT.equalsIgnoreCase(op)) {
					pageNo++;
				} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
					pageNo--;
				}

			} else if (OP_NEW.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.EMPLOYEE_CTL, request, response);
				return;

			} else if (OP_RESET.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.EMPLOYEE_LIST_CTL, request, response);
				return;

			} else if (OP_DELETE.equalsIgnoreCase(op)) {

				pageNo = 1;

				if (ids != null && ids.length > 0) {
					EmployeeDTO deleteDto = new EmployeeDTO();

					for (String id : ids) {
						deleteDto.setId(DataUtility.getLong(id));
						model.delete(deleteDto);
					}
					ServletUtility.setSuccessMessage("Employee Deleted Successfully", request);
				} else {
					ServletUtility.setErrorMessage("Select at least one record", request);
				}
			}

			if (OP_BACK.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.EMPLOYEE_LIST_CTL, request, response);
				return;
			}

			dto = (EmployeeDTO) populateDTO(request);

			list = model.search(dto, pageNo, pageSize);
			next = model.search(dto, pageNo + 1, pageSize);

			if (list == null || list.size() == 0) {
				if (!OP_DELETE.equalsIgnoreCase(op)) {
					ServletUtility.setErrorMessage("No record found", request);
				}
			}

			if (next == null || next.size() == 0) {
				request.setAttribute("nextListSize", 0);
			} else {
				request.setAttribute("nextListSize", next.size());
			}

			ServletUtility.setList(list, request);
			ServletUtility.setDto(dto, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}

		log.debug("EmployeeListCtl doPost End");
	}

	@Override
	protected String getView() {
		return ORSView.EMPLOYEE_LIST_VIEW;
	}

}
