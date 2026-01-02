<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.project_3.dto.EmployeeDTO"%>
<%@page import="in.co.rays.project_3.controller.EmployeeListCtl"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.HTMLUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
	"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Employee List</title>

<script src="<%=ORSView.APP_CONTEXT%>/js/jquery.min.js"></script>
<script type="text/javascript"
	src="<%=ORSView.APP_CONTEXT%>/js/CheckBox11.js"></script>

<style>
.hm {
	background-image: url('<%=ORSView.APP_CONTEXT%>/img/list2.jpg');
	background-repeat: no-repeat;
	background-attachment: fixed;
	background-size: cover;
	padding-top: 85px;
}

.text {
	text-align: center;
}
</style>
</head>

<%@include file="Header.jsp"%>

<body class="hm">

	<div>
		<form class="pb-5" action="<%=ORSView.EMPLOYEE_LIST_CTL%>"
			method="post">

			<jsp:useBean id="dto" class="in.co.rays.project_3.dto.EmployeeDTO"
				scope="request" />

			<%
				int pageNo = ServletUtility.getPageNo(request);
				int pageSize = ServletUtility.getPageSize(request);
				int index = ((pageNo - 1) * pageSize) + 1;
				int nextPageSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());

				List list = ServletUtility.getList(request);
				Iterator<EmployeeDTO> it = list.iterator();
			%>

			<%
				if (list.size() != 0) {
			%>

			<center>
				<h1 class="text-dark font-weight-bold pt-3">
					<u>Employee List</u>
				</h1>
			</center>

			<!-- Success Message -->
			<div class="row">
				<div class="col-md-4"></div>
				<%
					if (!ServletUtility.getSuccessMessage(request).equals("")) {
				%>
				<div class="col-md-4 alert alert-success alert-dismissible"
					style="background-color: #80ff80">
					<button type="button" class="close" data-dismiss="alert">&times;</button>
					<h4>
						<font color="#008000"> <%=ServletUtility.getSuccessMessage(request)%>
						</font>
					</h4>
				</div>
				<%
					}
				%>
				<div class="col-md-4"></div>
			</div>

			<!-- Error Message -->
			<div class="row">
				<div class="col-md-4"></div>
				<%
					if (!ServletUtility.getErrorMessage(request).equals("")) {
				%>
				<div class="col-md-4 alert alert-danger alert-dismissible">
					<button type="button" class="close" data-dismiss="alert">&times;</button>
					<h4>
						<font color="red"> <%=ServletUtility.getErrorMessage(request)%>
						</font>
					</h4>
				</div>
				<%
					}
				%>
				<div class="col-md-4"></div>
			</div>

			<!-- Search Panel -->
			<div class="row">

				<div class="col-sm-3"></div>

				<div class="col-sm-2">
					<input type="text" name="fullName" placeholder="Enter Full Name"
						class="form-control"
						value="<%=ServletUtility.getParameter("fullName", request)%>">
				</div>

				<div class="col-sm-2">
					<input type="text" name="username" placeholder="Enter Username"
						class="form-control"
						value="<%=ServletUtility.getParameter("username", request)%>">
				</div>

				<%-- <div class="col-sm-2">
		<input type="text" name="contactNo"
			placeholder="Enter Contact No" class="form-control"
			value="<%=ServletUtility.getParameter("contactNo", request)%>">
	</div> --%>

				<div class="col-sm-3">
					<input type="submit" class="btn btn-primary btn-md"
						name="operation" value="<%=EmployeeListCtl.OP_SEARCH%>"> <input
						type="submit" class="btn btn-dark btn-md" name="operation"
						value="<%=EmployeeListCtl.OP_RESET%>">
				</div>

				<div class="col-sm-2"></div>
			</div>

			<br>

			<!-- Table -->
			<div class="table-responsive">
				<table class="table table-bordered table-dark table-hover">
					<thead>
						<tr style="background-color: #8C8C8C;">
							<th width="10%"><input type="checkbox" id="select_all">
								Select All</th>
							<th width="5%" class="text">S.No</th>
							<th width="20%" class="text">Full Name</th>
							<th width="20%" class="text">Username</th>
							<th width="15%" class="text">Birth Date</th>
							<th width="15%" class="text">Contact No</th>
							<th width="5%" class="text">Edit</th>
						</tr>
					</thead>

					<tbody>
						<%
							while (it.hasNext()) {
									dto = it.next();
						%>
						<tr>
							<td align="center"><input type="checkbox" class="checkbox"
								name="ids" value="<%=dto.getId()%>"></td>
							<td class="text"><%=index++%></td>
							<td class="text"><%=dto.getFullName()%></td>
							<td class="text"><%=dto.getUsername()%></td>
							<td class="text"><%=DataUtility.getDateString(dto.getBirthDate())%>
							</td>
							<td class="text"><%=dto.getContactNo()%></td>
							<td class="text"><a href="EmployeeCtl?id=<%=dto.getId()%>">Edit</a>
							</td>
						</tr>
						<%
							}
						%>
					</tbody>
				</table>
			</div>

			<!-- Buttons -->
			<table width="100%">
				<tr>
					<td><input type="submit" name="operation"
						class="btn btn-warning btn-md"
						value="<%=EmployeeListCtl.OP_PREVIOUS%>"
						<%=pageNo > 1 ? "" : "disabled"%>></td>

					<td><input type="submit" name="operation"
						class="btn btn-primary btn-md" value="<%=EmployeeListCtl.OP_NEW%>">
					</td>

					<td><input type="submit" name="operation"
						class="btn btn-danger btn-md"
						value="<%=EmployeeListCtl.OP_DELETE%>"></td>

					<td align="right"><input type="submit" name="operation"
						class="btn btn-warning btn-md"
						value="<%=EmployeeListCtl.OP_NEXT%>"
						<%=(nextPageSize != 0) ? "" : "disabled"%>></td>
				</tr>
			</table>

			<%
				} else {
			%>

			<center>
				<h1 style="font-size: 40px; color: #162390;">Employee List</h1>
			</center>

			<br>

			<div style="padding-left: 48%;">
				<input type="submit" name="operation" class="btn btn-primary btn-md"
					value="<%=EmployeeListCtl.OP_BACK%>">
			</div>

			<%
				}
			%>

			<input type="hidden" name="pageNo" value="<%=pageNo%>"> <input
				type="hidden" name="pageSize" value="<%=pageSize%>">

		</form>
	</div>

</body>

<%@include file="FooterView.jsp"%>
</html>