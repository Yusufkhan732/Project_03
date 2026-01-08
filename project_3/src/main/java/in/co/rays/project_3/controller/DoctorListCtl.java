
package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.DoctorDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.model.DoctorModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

/**
 * DoctorListCtl is a servlet controller that manages listing, searching,
 * pagination, and deletion of Doctor records in the application.
 *
 * @author Yusuf Khan
 */
@WebServlet(name = "DoctorListCtl", urlPatterns = { "/ctl/DoctorListCtl" })
public class DoctorListCtl extends BaseCtl {

	private static final Logger log = Logger.getLogger(DoctorListCtl.class.getName());

	/**
	 * Preloads the list of doctor specialties (experties) for search filter
	 * dropdown.
	 *
	 * @param request the HttpServletRequest object
	 */
	@Override
	protected void preload(HttpServletRequest request) {
		log.info("Inside preload method of DoctorListCtl");
		DoctorModelInt model = ModelFactory.getInstance().getDoctorModel();
		try {
			List<DoctorDTO> doctorExpertiesList = model.list();
			HashMap<String, String> expertiesMap = new HashMap<>();
			Iterator<DoctorDTO> it = doctorExpertiesList.iterator();

			while (it.hasNext()) {
				DoctorDTO dto = it.next();
				expertiesMap.put(dto.getExperties(), dto.getExperties());
			}
			request.setAttribute("expertiesMap", expertiesMap);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Populates a {@link DoctorDTO} from search form parameters.
	 *
	 * @param request the HttpServletRequest object
	 * @return a DoctorDTO populated with search criteria
	 */
	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {
		log.info("Inside populateDTO method of DoctorListCtl");
		DoctorDTO dto = new DoctorDTO();
		dto.setName(DataUtility.getString(request.getParameter("name")));
		dto.setMobile(DataUtility.getString(request.getParameter("mobile")));
		dto.setExperties(DataUtility.getString(request.getParameter("experties")));
		return dto;
	}

	/**
	 * Handles HTTP GET requests to display the doctor list with optional search
	 * criteria.
	 *
	 * @param req  the HttpServletRequest object
	 * @param resp the HttpServletResponse object
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.info("Inside doGet method of DoctorListCtl");

		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
		DoctorDTO dto = (DoctorDTO) populateDTO(req);
		DoctorModelInt model = ModelFactory.getInstance().getDoctorModel();

		try {
			List<DoctorDTO> list = model.search(dto, pageNo, pageSize);
			List<DoctorDTO> next = model.search(dto, pageNo + 1, pageSize);

			if (list.isEmpty() || list == null) {
				ServletUtility.setErrorMessage("No Records found", req);
			}
			ServletUtility.setList(list, req);
			ServletUtility.setDto(dto, req);
			ServletUtility.setPageNo(pageNo, req);
			ServletUtility.setPageSize(pageSize, req);
			req.setAttribute("nextListSize", next.size());
		} catch (ApplicationException e) {
			e.printStackTrace();
			ServletUtility.handleException(e, req, resp);
			return;
		}

		ServletUtility.forward(getView(), req, resp);
	}

	/**
	 * Handles HTTP POST requests for searching, pagination, deletion, reset, and
	 * redirection to new doctor form.
	 *
	 * @param req  the HttpServletRequest object
	 * @param resp the HttpServletResponse object
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.info("Inside doPost method of DoctorListCtl");

		List<DoctorDTO> list = null;
		List<DoctorDTO> next = null;

		int pageNo = DataUtility.getInt(req.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(req.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		DoctorDTO dto = (DoctorDTO) populateDTO(req);
		DoctorModelInt model = ModelFactory.getInstance().getDoctorModel();

		String op = DataUtility.getString(req.getParameter("operation"));

		try {
			if (OP_SEARCH.equalsIgnoreCase(op) || OP_NEXT.equalsIgnoreCase(op) || OP_PREVIOUS.equalsIgnoreCase(op)) {
				if (OP_SEARCH.equalsIgnoreCase(op)) {
					pageNo = 1;
				} else if (OP_NEXT.equalsIgnoreCase(op)) {
					pageNo++;
				} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
					pageNo--;
				}
			} else if (OP_RESET.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.DOCTOR_LIST_CTL, req, resp);
				return;
			} else if (OP_NEW.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.DOCTOR_CTL, req, resp);
				return;
			} else if (OP_DELETE.equalsIgnoreCase(op)) {
				pageNo = 1;
				String[] ids = req.getParameterValues("ids");
				if (ids != null && ids.length > 0) {
					DoctorDTO deleteDTO = new DoctorDTO();
					for (String id : ids) {
						deleteDTO.setId(DataUtility.getLong(id));
						model.delete(deleteDTO);
						ServletUtility.setSuccessMessage("Deleted Successfully !!!", req);
					}
				} else {
					ServletUtility.setErrorMessage("Select Atleast one Checkbox", req);
				}
			} else if (OP_BACK.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.DOCTOR_LIST_CTL, req, resp);
				return;
			}

			list = model.search(dto, pageNo, pageSize);
			next = model.search(dto, pageNo + 1, pageSize);

			if (list.isEmpty() || list == null) {
				ServletUtility.setErrorMessage("No Records found", req);
			}
			ServletUtility.setList(list, req);
			ServletUtility.setDto(dto, req);
			ServletUtility.setPageNo(pageNo, req);
			ServletUtility.setPageSize(pageSize, req);
			req.setAttribute("nextListSize", next.size());
		} catch (ApplicationException e) {
			e.printStackTrace();
			ServletUtility.handleException(e, req, resp);
			return;
		}

		ServletUtility.forward(getView(), req, resp);
	}

	/**
	 * Returns the view (JSP page) associated with this controller.
	 *
	 * @return the doctor list view JSP path
	 */
	@Override
	protected String getView() {
		log.info("Inside getView method of DoctorListCtl");
		return ORSView.DOCTOR_LIST_VIEW;
	}
}
