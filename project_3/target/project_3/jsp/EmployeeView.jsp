<%@page import="in.co.rays.project_3.controller.EmployeeCtl"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Employee View</title>
<meta name="viewport" content="width=device-width, initial-scale=1">

<style type="text/css">
i.css {
	border: 2px solid #8080803b;
	padding-left: 10px;
	padding-bottom: 11px;
	background-color: #ebebe0;
}

.p4 {
	background-image: url('<%=ORSView.APP_CONTEXT%>/img/user1.jpg');
	background-repeat: no-repeat;
	background-attachment: fixed;
	background-size: cover;
	padding-top: 75px;
}
</style>
</head>

<body class="p4">

	<div class="header">
		<%@include file="Header.jsp"%>
		<%@include file="calendar.jsp"%>
	</div>

	<main>
	<form action="<%=ORSView.EMPLOYEE_CTL%>" method="post">

		<div class="row pt-3 pb-4">

			<jsp:useBean id="dto" class="in.co.rays.project_3.dto.EmployeeDTO"
				scope="request" />

			<div class="col-md-4 mb-4"></div>

			<div class="col-md-4 mb-4">
				<div class="card">
					<div class="card-body">

						<%
							long id = DataUtility.getLong(request.getParameter("id"));
							if (id > 0) {
						%>
						<h3 class="text-center text-primary">Update Employee</h3>
						<%
							} else {
						%>
						<h3 class="text-center text-primary">Add Employee</h3>
						<%
							}
						%>

						<!-- Success Message -->
						<h4 align="center">
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
						</h4>

						<!-- Error Message -->
						<h4 align="center">
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
						</h4>

						<!-- Hidden Fields -->
						<input type="hidden" name="id" value="<%=dto.getId()%>"> <input
							type="hidden" name="createdBy" value="<%=dto.getCreatedBy()%>">
						<input type="hidden" name="modifiedBy"
							value="<%=dto.getModifiedBy()%>"> <input type="hidden"
							name="createdDatetime"
							value="<%=DataUtility.getTimestamp(dto.getCreatedDatetime())%>">
						<input type="hidden" name="modifiedDatetime"
							value="<%=DataUtility.getTimestamp(dto.getModifiedDatetime())%>">

						<!-- Full Name -->
						<span class="pl-sm-5"><b>Full Name</b><span
							style="color: red">*</span></span><br>
						<div class="col-sm-12">
							<div class="input-group">
								<div class="input-group-prepend">
									<div class="input-group-text">
										<i class="fa fa-user grey-text"></i>
									</div>
								</div>
								<input type="text" name="fullName" class="form-control"
									placeholder="Enter Full Name"
									value="<%=DataUtility.getStringData(dto.getFullName())%>">
							</div>
						</div>
						<font color="red" class="pl-sm-5"> <%=ServletUtility.getErrorMessage("fullName", request)%>
						</font><br>

						<!-- Username -->
						<span class="pl-sm-5"><b>Username</b><span
							style="color: red">*</span></span><br>
						<div class="col-sm-12">
							<div class="input-group">
								<div class="input-group-prepend">
									<div class="input-group-text">
										<i class="fa fa-id-card grey-text"></i>
									</div>
								</div>
								<input type="text" name="username" class="form-control"
									placeholder="Enter Username"
									value="<%=DataUtility.getStringData(dto.getUsername())%>">
							</div>
						</div>
						<font color="red" class="pl-sm-5"> <%=ServletUtility.getErrorMessage("username", request)%>
						</font><br>

						<!-- Password -->
						<span class="pl-sm-5"><b>Password</b><span
							style="color: red">*</span></span><br>
						<div class="col-sm-12">
							<div class="input-group">
								<div class="input-group-prepend">
									<div class="input-group-text">
										<i class="fa fa-lock grey-text"></i>
									</div>
								</div>
								<input type="password" name="password" class="form-control"
									placeholder="Enter Password"
									value="<%=DataUtility.getStringData(dto.getPassword())%>">
							</div>
						</div>
						<font color="red" class="pl-sm-5"> <%=ServletUtility.getErrorMessage("password", request)%>
						</font><br>

						<!-- Birth Date -->
						<span class="pl-sm-5"><b>Birth Date</b><span
							style="color: red">*</span></span><br>
						<div class="col-sm-12">
							<div class="input-group">
								<div class="input-group-prepend">
									<div class="input-group-text">
										<i class="fa fa-calendar grey-text"></i>
									</div>
								</div>
								<input type="text" id="datepicker2" name="birthDate"
									class="form-control" readonly="readonly"
									placeholder="Select Birth Date"
									value="<%=DataUtility.getDateString(dto.getBirthDate())%>">
							</div>
						</div>
						<font color="red" class="pl-sm-5"> <%=ServletUtility.getErrorMessage("birthDate", request)%>
						</font><br>

						<!-- Contact No -->
						<span class="pl-sm-5"><b>Contact No</b><span
							style="color: red">*</span></span><br>
						<div class="col-sm-12">
							<div class="input-group">
								<div class="input-group-prepend">
									<div class="input-group-text">
										<i class="fa fa-phone grey-text"></i>
									</div>
								</div>
								<input type="text" name="contactNo" class="form-control"
									placeholder="Enter Contact Number"
									value="<%=DataUtility.getStringData(dto.getContactNo())%>">
							</div>
						</div>
						<font color="red" class="pl-sm-5"> <%=ServletUtility.getErrorMessage("contactNo", request)%>
						</font><br> <br>

						<!-- Buttons -->
						<div class="text-center">
							<%
								if (id > 0) {
							%>
							<input type="submit" name="operation"
								class="btn btn-success btn-md"
								value="<%=EmployeeCtl.OP_UPDATE%>"> <input type="submit"
								name="operation" class="btn btn-warning btn-md"
								value="<%=EmployeeCtl.OP_CANCEL%>">
							<%
								} else {
							%>
							<input type="submit" name="operation"
								class="btn btn-success btn-md" value="<%=EmployeeCtl.OP_SAVE%>">
							<input type="submit" name="operation"
								class="btn btn-warning btn-md" value="<%=EmployeeCtl.OP_RESET%>">
							<%
								}
							%>
						</div>

					</div>
				</div>
			</div>

			<div class="col-md-4 mb-4"></div>

		</div>
	</form>
	</main>

	<%@include file="FooterView.jsp"%>

</body>
</html>
