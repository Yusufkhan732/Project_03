
<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.project_3.util.HTMLUtility"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.controller.PatientCtl"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Patient view</title>
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
	background-size: 100%;
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
		<form action="<%=ORSView.PATIENT_CTL%>" method="post">
			<jsp:useBean id="dto" class="in.co.rays.project_3.dto.PatientDTO"
				scope="request"></jsp:useBean>
			<div class="row pt-3">
				<div class="col-md-4 mb-4"></div>
				<div class="col-md-4 mb-4">
					<div class="card input-group-addon">
						<div class="card-body">

							<%
								long id = DataUtility.getLong(request.getParameter("id"));

								if (id > 0) {

									System.out.println(id + "dfghj");
							%>
							<h3 class="text-center default-text text-primary">Update
								Patient</h3>
							<%
								} else {
							%>
							<h3 class="text-center default-text text-primary">Add
								Patient</h3>
							<%
								}
							%>
							<div>
								<%
									HashMap map1 = (HashMap) request.getAttribute("map1");
								%>

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

								<input type="hidden" name="id" value="<%=dto.getId()%>">
								<input type="hidden" name="createdBy"
									value="<%=dto.getCreatedBy()%>"> <input type="hidden"
									name="modifiedBy" value="<%=dto.getModifiedBy()%>"> <input
									type="hidden" name="createdDatetime"
									value="<%=DataUtility.getTimestamp(dto.getCreatedDatetime())%>">
								<input type="hidden" name="modifiedDatetime"
									value="<%=DataUtility.getTimestamp(dto.getModifiedDatetime())%>">
							</div>

							<!-- Name -->
							<span class="pl-sm-5"><b>Name</b><span style="color: red;">*</span></span><br>
							<div class="col-sm-12">
								<div class="input-group">
									<div class="input-group-prepend">
										<div class="input-group-text">
											<i class="fa fa-user grey-text"></i>
										</div>
									</div>
									<input type="text" class="form-control" name="name"
										placeholder="Enter Name"
										value="<%=DataUtility.getStringData(dto.getName())%>">
								</div>
							</div>
							<font color="red" class="pl-sm-5"><%=ServletUtility.getErrorMessage("name", request)%></font></br>

							<!-- Mobile No -->
							<span class="pl-sm-5"><b>Mobile No</b><span
								style="color: red;">*</span></span><br>
							<div class="col-sm-12">
								<div class="input-group">
									<div class="input-group-prepend">
										<div class="input-group-text">
											<i class="fa fa-phone-square grey-text"></i>
										</div>
									</div>
									<input type="text" class="form-control" maxlength="10"
										name="mobileNo" placeholder="Mobile No"
										value="<%=DataUtility.getStringData(dto.getMobileNo())%>">
								</div>
							</div>
							<font color="red" class="pl-sm-5"><%=ServletUtility.getErrorMessage("mobileNo", request)%></font></br>

							<!-- Disease -->
							<%
								HashMap map = (HashMap) request.getAttribute("map");
							%>

							<!-- Disease -->
							<span class="pl-sm-5"> <b>Disease</b><span
								style="color: red;">*</span>
							</span><br>

							<div class="col-sm-12">
								<div class="input-group">
									<div class="input-group-prepend">
										<div class="input-group-text">
											<i class="fa fa-heartbeat grey-text"></i>
										</div>
									</div>
									<%=HTMLUtility.getList("disease", dto.getDisease(), map)%>
								</div>
							</div>

							<font color="red" class="pl-sm-5"> <%=ServletUtility.getErrorMessage("disease", request)%>
							</font>

							<!-- Gender -->
							<span class="pl-sm-5"><b>Gender</b><span
								style="color: red;">*</span></span><br>
							<div class="col-sm-12">
								<div class="input-group">
									<div class="input-group-prepend">
										<div class="input-group-text">
											<i class="fa fa-venus-mars grey-text"></i>
										</div>
									</div>
									<%=HTMLUtility.getList("gender", dto.getGender(), map1)%>
								</div>
							</div>
							<font color="red" class="pl-sm-5"><%=ServletUtility.getErrorMessage("gender", request)%></font></br>

							<!-- Date of Visit -->
							<span class="pl-sm-5"><b>Date Of Visit</b><span
								style="color: red;">*</span></span><br>
							<div class="col-sm-12">
								<div class="input-group">
									<div class="input-group-prepend">
										<div class="input-group-text">
											<i class="fa fa-calendar grey-text"></i>
										</div>
									</div>
									<input type="text" id="datepicker2" name="dateOfVisit"
										class="form-control" placeholder="Date Of Visit" readonly
										value="<%=DataUtility.getDateString(dto.getDateOfVisit())%>">
								</div>
							</div>
							<font color="red" class="pl-sm-5"><%=ServletUtility.getErrorMessage("dateOfVisit", request)%></font></br>

							<!-- Submit Buttons -->
							<div class="text-center">
								<%
									if (dto.getId() != null && dto.getId() > 0) {
								%>
								<input type="submit" name="operation"
									class="btn btn-success btn-md"
									value="<%=PatientCtl.OP_UPDATE%>"> <input type="submit"
									name="operation" class="btn btn-warning btn-md"
									value="<%=PatientCtl.OP_CANCEL%>">
								<%
									} else {
								%>
								<input type="submit" name="operation"
									class="btn btn-success btn-md" value="<%=PatientCtl.OP_SAVE%>">
								<input type="submit" name="operation"
									class="btn btn-warning btn-md" value="<%=PatientCtl.OP_RESET%>">
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
