package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.PatientDTO;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.model.PatientModelInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

/**
 * Patient List Controller
 *
 * This controller handles Patient List functionality such as: - Search Patient
 * - Pagination (Next / Previous) - Delete Patient - Redirect to Add Patient
 * page
 *
 * @author Yusuf Khan
 */
@WebServlet(name = "PatientListCtl", urlPatterns = { "/ctl/PatientListCtl" })
public class PatientListCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(PatientListCtl.class);

	/**
	 * Populates PatientDTO from request parameters for search criteria.
	 *
	 * @param request HttpServletRequest
	 * @return PatientDTO
	 */
	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {

		PatientDTO dto = new PatientDTO();

		dto.setName(DataUtility.getString(request.getParameter("name")));
		dto.setMobileNo(DataUtility.getString(request.getParameter("mobileNo")));
		dto.setDisease(DataUtility.getString(request.getParameter("disease")));
		dto.setGender(DataUtility.getString(request.getParameter("gender")));

		populateBean(dto, request);
		return dto;
	}

	/**
	 * Handles GET request. Displays Patient list with pagination.
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("PatientListCtl doGet started");

		List list;
		List next;

		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		PatientDTO dto = (PatientDTO) populateDTO(request);
		PatientModelInt model = ModelFactory.getInstance().getPatientModel();

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

		} catch (Exception e) {
			log.error("Error in PatientListCtl doGet", e);
		}

		log.debug("PatientListCtl doGet completed");
	}

	/**
	 * Handles POST request. Performs Search, Pagination, Delete, New and Reset
	 * operations.
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("PatientListCtl doPost started");

		List list = null;
		List next = null;

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		PatientDTO dto = (PatientDTO) populateDTO(request);
		String op = DataUtility.getString(request.getParameter("operation"));
		String[] ids = request.getParameterValues("ids");

		PatientModelInt model = ModelFactory.getInstance().getPatientModel();

		if (OP_SEARCH.equalsIgnoreCase(op) || OP_NEXT.equalsIgnoreCase(op) || OP_PREVIOUS.equalsIgnoreCase(op)) {

			if (OP_SEARCH.equalsIgnoreCase(op)) {
				pageNo = 1;

			} else if (OP_NEXT.equalsIgnoreCase(op)) {
				pageNo++;

			} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
				pageNo--;
			}

		} else if (OP_NEW.equalsIgnoreCase(op)) {

			log.info("Redirecting to Patient Add Page");
			ServletUtility.redirect(ORSView.PATIENT_CTL, request, response);
			return;

		} else if (OP_RESET.equalsIgnoreCase(op)) {

			log.info("Patient list reset");
			ServletUtility.redirect(ORSView.PATIENT_LIST_CTL, request, response);
			return;

		} else if (OP_DELETE.equalsIgnoreCase(op)) {

			pageNo = 1;

			if (ids != null && ids.length > 0) {
				PatientDTO deleteDto = new PatientDTO();

				for (String id : ids) {
					deleteDto.setId(DataUtility.getLong(id));
					model.delete(deleteDto);
				}

				ServletUtility.setSuccessMessage("Data Successfully Deleted!", request);
				log.info("Patient records deleted successfully");

			} else {
				ServletUtility.setErrorMessage("Select at least one record", request);
			}
		}

		dto = (PatientDTO) populateDTO(request);

		list = model.search(dto, pageNo, pageSize);
		next = model.search(dto, pageNo + 1, pageSize);

		ServletUtility.setDto(dto, request);

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

		log.debug("PatientListCtl doPost completed");
	}

	/**
	 * Returns Patient List view page.
	 *
	 * @return Patient list view
	 */
	@Override
	protected String getView() {
		return ORSView.PATIENT_LIST_VIEW;
	}
}
