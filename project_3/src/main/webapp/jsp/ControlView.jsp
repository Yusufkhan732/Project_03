<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.project_3.util.HTMLUtility"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.controller.ControlCtl"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User view</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<style type="text/css">
i.css {
	border: 2px solid #8080803b;
	padding-left: 10px;
	padding-bottom: 11px;
	background-color: #ebebe0;
}

.input-group-addon {
	box-shadow: 9px 8px 7px #001a33;
}

.hm {
	background-image: url('<%=ORSView.APP_CONTEXT%>/img/user1.jpg');
	background-repeat: no-repeat;
	background-attachment: fixed;
	background-size: cover;
	padding-top: 75px;

	/* background-size: 100%; */
}
</style>

</head>
<body class="hm">
	<div class="header">
		<%@include file="Header.jsp"%>
		<%@include file="calendar.jsp"%>
		
	</div>
	<div>
		<main>
		<form action="<%=ORSView.CONTROL_CTL%>" method="post">
			<jsp:useBean id="dto" class="in.co.rays.project_3.dto.ControlDTO"
				scope="request"></jsp:useBean>
			<div class="row pt-3">
				<div class="col-md-4 mb-4"></div>
				<div class="col-md-4 mb-4">
					<div class="card input-group-addon">
						<div class="card-body">

							<%
								long id = DataUtility.getLong(request.getParameter("id"));
								if (id > 0) {
							%>
							<h3 class="text-center default-text text-primary">Update
								Control</h3>
							<%
								} else {
							%>
							<h3 class="text-center default-text text-primary">Add
								Control</h3>
							<%
								}
							%>

							<!-- Success/Error Messages -->
							<H4 align="center">
								<%
									if (!ServletUtility.getSuccessMessage(request).equals("")) {
								%>
								<div class="alert alert-success alert-dismissible">
									<button type="button" class="close" data-dismiss="alert">&times;</button>
									<%=ServletUtility.getSuccessMessage(request)%>
								</div>
								<%
									}
								%>
							</H4>
							<H4 align="center">
								<%
									if (!ServletUtility.getErrorMessage(request).equals("")) {
								%>
								<div class="alert alert-danger alert-dismissible">
									<button type="button" class="close" data-dismiss="alert">&times;</button>
									<%=ServletUtility.getErrorMessage(request)%>
								</div>
								<%
									}
								%>
							</H4>

							<!-- Hidden Fields -->
							<input type="hidden" name="id" value="<%=dto.getId()%>">
							<input type="hidden" name="createdBy"
								value="<%=dto.getCreatedBy()%>"> <input type="hidden"
								name="modifiedBy" value="<%=dto.getModifiedBy()%>"> <input
								type="hidden" name="createdDatetime"
								value="<%=DataUtility.getTimestamp(dto.getCreatedDatetime())%>">
							<input type="hidden" name="modifiedDatetime"
								value="<%=DataUtility.getTimestamp(dto.getModifiedDatetime())%>">

							<!-- Version Name -->
							<span class="pl-sm-5"><b>Version Name</b><span
								style="color: red;">*</span></span><br>
							<div class="col-sm-12">
								<div class="input-group">
									<div class="input-group-prepend">
										<div class="input-group-text">
											<i class="fa fa-tag grey-text"></i>
										</div>
									</div>
									<input type="text" class="form-control" name="versionName"
										placeholder="Enter Version Name"
										value="<%=DataUtility.getStringData(dto.getVersionName())%>">
								</div>
							</div>
							<font color="red" class="pl-sm-5"><%=ServletUtility.getErrorMessage("versionName", request)%></font></br>

							<!-- Release Notes -->
							<span class="pl-sm-5"><b>Release Notes</b><span
								style="color: red;">*</span></span><br>
							<div class="col-sm-12">
								<textarea class="form-control" name="releaseNotes"
									placeholder="Enter Release Notes"><%=DataUtility.getStringData(dto.getReleaseNotes())%></textarea>
							</div>
							<font color="red" class="pl-sm-5"><%=ServletUtility.getErrorMessage("releaseNotes", request)%></font></br>

							<!-- Release Date -->
							<span class="pl-sm-5"><b>Release Date</b> <span
								style="color: red;">*</span></span></br>
							<div class="col-sm-12">
								<div class="input-group">
									<div class="input-group-prepend">
										<div class="input-group-text">
											<i class="fa fa-calendar grey-text" style="font-size: 1rem;"></i>
										</div>
									</div>
									<input type="text" id="datepicker2" name="releaseDate"
										class="form-control" placeholder="release Date Enter Date "
										readonly="readonly"
										value="<%=DataUtility.getDateString(dto.getReleaseDate())%>">
								</div>
							</div>
							<font color="red" class="pl-sm-5"> <%=ServletUtility.getErrorMessage("releaseDate", request)%></font></br>

							<!-- Version Status -->
							<span class="pl-sm-5"><b>Version Status</b><span
								style="color: red;">*</span></span><br>
							<div class="col-sm-12">
								<div class="input-group">
									<div class="input-group-prepend">
										<div class="input-group-text">
											<i class="fa fa-info-circle grey-text"></i>
										</div>
									</div>
									<%
										HashMap<String, String> statusMap = new HashMap<>();
										statusMap.put("Active", "Active");
										statusMap.put("Inactive", "Inactive");
									%>
									<%=HTMLUtility.getList("versionStatus", dto.getVersionStatus(), statusMap)%>
								</div>
							</div>
							<font color="red" class="pl-sm-5"><%=ServletUtility.getErrorMessage("versionStatus", request)%></font></br>

							<!-- Submit Buttons -->
							<div class="text-center">
								<%
									if (id > 0) {
								%>
								<input type="submit" name="operation"
									class="btn btn-success btn-md"
									value="<%=ControlCtl.OP_UPDATE%>"> <input type="submit"
									name="operation" class="btn btn-warning btn-md"
									value="<%=ControlCtl.OP_CANCEL%>">
								<%
									} else {
								%>
								<input type="submit" name="operation"
									class="btn btn-success btn-md" value="<%=ControlCtl.OP_SAVE%>">
								<input type="submit" name="operation"
									class="btn btn-warning btn-md" value="<%=ControlCtl.OP_RESET%>">
								<%
									}
								%>
							</div>

						</div>
					</div>
				</div>
		</form>
		</main>
		<div class="col-md-4 mb-4"></div>
	</div>
</body>
<%@include file="FooterView.jsp"%>
</html>
