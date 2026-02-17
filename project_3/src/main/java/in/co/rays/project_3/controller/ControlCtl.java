package in.co.rays.project_3.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.ControlDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.ControlModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(urlPatterns = { "/ctl/ControlCtl" })
public class ControlCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(ControlCtl.class);

	@Override
	protected boolean validate(HttpServletRequest request) {

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("versionName"))) {
			request.setAttribute("versionName", PropertyReader.getValue("error.require", "Version Name"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("releaseNotes"))) {
			request.setAttribute("releaseNotes", PropertyReader.getValue("error.require", "Release Notes"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("releaseDate"))) {
			request.setAttribute("releaseDate", PropertyReader.getValue("error.require", "Release Date"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("versionStatus"))) {
			request.setAttribute("versionStatus", PropertyReader.getValue("error.require", "Version Status"));
			pass = false;
		}

		return pass;
	}

	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {

		ControlDTO dto = new ControlDTO();

		dto.setVersionName(request.getParameter("versionName"));
		dto.setReleaseNotes(request.getParameter("releaseNotes"));

		dto.setReleaseDate(DataUtility.getDate(request.getParameter("releaseDate")));

		dto.setVersionStatus(request.getParameter("versionStatus"));

		populateBean(dto, request);

		return dto;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String op = request.getParameter("operation");
		long id = DataUtility.getLong(request.getParameter("id"));

		ControlModelInt model = ModelFactory.getInstance().getControlModel();

		if (id > 0 ) {

			try {
				ControlDTO dto = model.findByPK(id);
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

		ControlModelInt model = ModelFactory.getInstance().getControlModel();

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

			ControlDTO dto = (ControlDTO) populateDTO(request);

			try {

				if (id > 0) {
					dto.setId(id);
					model.update(dto);
					ServletUtility.setSuccessMessage("Record Successfully Updated", request);
				} else {
					model.add(dto);
					ServletUtility.setSuccessMessage("Record Successfully Saved", request);
				}

				ServletUtility.setDto(dto, request);

			} catch (ApplicationException e) {

				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;

			} catch (DuplicateRecordException e) {

				ServletUtility.setDto(dto, request);
				ServletUtility.setErrorMessage("Version Name Already Exists", request);
			}

		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.CONTROL_CTL, request, response);
			return;

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.CONTROL_LIST_CTL, request, response);
			return;
		}

		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected String getView() {
		return ORSView.CONTROL_VIEW;
	}
}
