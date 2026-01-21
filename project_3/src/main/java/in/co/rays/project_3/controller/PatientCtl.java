package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.PatientDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.model.PatientModelInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

/**
 * Patient Controller
 *
 * This controller handles Patient module operations such as: - Add Patient -
 * Update Patient - View Patient details
 *
 * It also performs: - Preloading dropdown data (Disease, Gender) - Form
 * validation - Request processing (GET / POST)
 *
 * @author Yusuf Khan
 */
@WebServlet(urlPatterns = { "/ctl/PatientCtl" })
public class PatientCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(PatientCtl.class);

	/**
	 * Preloads disease and gender data and sets it into request scope for
	 * dropdowns.
	 *
	 * @param request HttpServletRequest
	 */
	@Override
	protected void preload(HttpServletRequest request) {

		log.info("PatientCtl preload started");

		HashMap<String, String> map = new HashMap<>();
		map.put("Malaria", "Malaria");
		map.put("Diabetes Mellitus", "Diabetes Mellitus");
		map.put("Hypertension", "Hypertension");
		map.put("Tuberculosis", "Tuberculosis");
		map.put("Asthma", "Asthma");
		map.put("COVID-19", "COVID-19");
		map.put("Cancer", "Cancer");
		map.put("Influenza", "Influenza");
		map.put("Heart Attack", "Heart Attack");
		request.setAttribute("map", map);

		HashMap<String, String> map1 = new HashMap<>();
		map1.put("Male", "Male");
		map1.put("Female", "Female");
		request.setAttribute("map1", map1);

		log.info("PatientCtl preload completed");
	}

	/**
	 * Validates patient form input fields.
	 *
	 * @param request HttpServletRequest
	 * @return true if validation passes, otherwise false
	 */
	@Override
	public boolean validate(HttpServletRequest request) {

		log.debug("PatientCtl validation started");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("name"))) {
			request.setAttribute("name", PropertyReader.getValue("error.require", "Name"));
			pass = false;
		} else if (!DataValidator.isName(request.getParameter("name"))) {
			request.setAttribute("name", "Invalid Name");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("dateOfVisit"))) {
			request.setAttribute("dateOfVisit", PropertyReader.getValue("error.require", "Date Of Visit"));
			pass = false;
		} else if (!DataValidator.isDate(request.getParameter("dateOfVisit"))) {
			request.setAttribute("dateOfVisit", "Invalid Date of Visit");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("disease"))) {
			request.setAttribute("disease", PropertyReader.getValue("error.require", "Disease"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("mobileNo"))) {
			request.setAttribute("mobileNo", PropertyReader.getValue("error.require", "Mobile No"));
			pass = false;
		} else if (!DataValidator.isPhoneLength(request.getParameter("mobileNo"))) {
			request.setAttribute("mobileNo", "Mobile No must have 10 digits");
			pass = false;
		} else if (!DataValidator.isPhoneNo(request.getParameter("mobileNo"))) {
			request.setAttribute("mobileNo", "Invalid Mobile No");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("gender"))) {
			request.setAttribute("gender", PropertyReader.getValue("error.require", "Gender"));
			pass = false;
		}

		log.debug("PatientCtl validation completed : " + pass);
		return pass;
	}

	/**
	 * Populates PatientDTO from request parameters.
	 *
	 * @param request HttpServletRequest
	 * @return populated PatientDTO object
	 */
	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {

		log.debug("PatientCtl populateDTO started");

		PatientDTO dto = new PatientDTO();

		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setName(DataUtility.getString(request.getParameter("name")));
		dto.setDateOfVisit(DataUtility.getDate(request.getParameter("dateOfVisit")));
		dto.setDisease(DataUtility.getString(request.getParameter("disease")));
		dto.setMobileNo(DataUtility.getString(request.getParameter("mobileNo")));
		dto.setGender(DataUtility.getString(request.getParameter("gender")));

		populateBean(dto, request);

		log.debug("PatientDTO populated successfully");
		return dto;
	}

	/**
	 * Handles GET request. Used to load patient data for update.
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.info("PatientCtl doGet started");

		String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));

		PatientModelInt model = ModelFactory.getInstance().getPatientModel();

		if (id > 0 || op != null) {
			PatientDTO dto = model.findByPK(id);
			ServletUtility.setDto(dto, request);
			log.info("Patient data loaded for ID : " + id);
		}

		ServletUtility.forward(getView(), request, response);
	}

	/**
	 * Handles POST request. Performs Save, Update, Reset, and Cancel operations.
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.info("PatientCtl doPost started");

		String op = DataUtility.getString(request.getParameter("operation"));
		PatientModelInt model = ModelFactory.getInstance().getPatientModel();

		if (OP_SAVE.equalsIgnoreCase(op)) {

			log.info("Patient save operation started");

			PatientDTO dto = (PatientDTO) populateDTO(request);

			try {
				model.add(dto);
				ServletUtility.setDto(dto, request);
				ServletUtility.setSuccessMessage("Patient Successfully Saved", request);
			} catch (ApplicationException e) {
				log.error("Database error while saving patient", e);
				ServletUtility.setErrorMessage(e.getMessage(), request); // message from handleException
				ServletUtility.forward(getView(), request, response);
				return;
			} catch (Exception e) {
				log.error("Unexpected error while saving patient", e);
				ServletUtility.setErrorMessage("Database Server is down. Please try after some time", request);
				ServletUtility.forward(getView(), request, response);
				return;
			}

		} else if (OP_UPDATE.equalsIgnoreCase(op)) {

			log.info("Patient update operation started");

			PatientDTO dto = (PatientDTO) populateDTO(request);

			try {
				model.update(dto);
				ServletUtility.setDto(dto, request);
				ServletUtility.setSuccessMessage("Patient Successfully Updated", request);
			} catch (ApplicationException e) {
				log.error("Database error while saving patient", e);
				ServletUtility.setErrorMessage(e.getMessage(), request); // message from handleException
				ServletUtility.forward(getView(), request, response);
				return;
			} catch (Exception e) {
				log.error("Unexpected error while saving patient", e);
				ServletUtility.setErrorMessage("Database Server is down. Please try after some time", request);
				ServletUtility.forward(getView(), request, response);
				return;

			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			log.info("Patient operation cancelled");
			ServletUtility.redirect(ORSView.PATIENT_LIST_CTL, request, response);
			return;

		} else if (OP_RESET.equalsIgnoreCase(op)) {

			log.info("Patient form reset");
			ServletUtility.redirect(ORSView.PATIENT_CTL, request, response);
			return;
		}

		ServletUtility.forward(getView(), request, response);
	}

	/**
	 * Returns Patient view page.
	 *
	 * @return patient view
	 */
	@Override
	protected String getView() {
		return ORSView.PATIENT_VIEW;
	}
}
