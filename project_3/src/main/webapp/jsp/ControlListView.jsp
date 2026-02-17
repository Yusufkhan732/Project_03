<%@page import="in.co.rays.project_3.dto.ControlDTO"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.controller.ControlListCtl"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Control List</title>

<script src="<%=ORSView.APP_CONTEXT%>/js/jquery.min.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/CheckBox11.js"></script>

</head>

<%@include file="Header.jsp"%>

<body class="hm">

	<form action="<%=ORSView.CONTROL_LIST_CTL%>" method="post">

		<jsp:useBean id="dto" class="in.co.rays.project_3.dto.ControlDTO"
			scope="request"></jsp:useBean>

		<%
			int pageNo = ServletUtility.getPageNo(request);
			int pageSize = ServletUtility.getPageSize(request);
			int index = ((pageNo - 1) * pageSize) + 1;
			int nextPageSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());

			List list = ServletUtility.getList(request);
			Iterator<ControlDTO> it = list.iterator();
		%>

		<center>
			<h1 class="text-dark font-weight-bold pt-3">
				<u>Control List</u>
			</h1>
		</center>

		<!-- Search Section -->
		<div class="row mt-3" align="center">

			<div class="col-sm-3">
				<input type="text" name="versionName"
					placeholder="Enter Version Name" class="form-control"
					value="<%=ServletUtility.getParameter("versionName", request)%>">
			</div>

			<div class="col-sm-3">
				<select name="versionStatus" class="form-control">
					<option value="">--Select Status--</option>
					<option value="Active">Active</option>
					<option value="Inactive">Inactive</option>
				</select>
			</div>

			<div class="col-sm-3">
				<input type="submit" class="btn btn-primary" name="operation"
					value="<%=ControlListCtl.OP_SEARCH%>"> <input type="submit"
					class="btn btn-dark" name="operation"
					value="<%=ControlListCtl.OP_RESET%>">
			</div>

		</div>

		<br>

		<!-- Table -->
		<div class="table-responsive">
			<table class="table table-bordered table-dark table-hover">

				<thead>
					<tr>
						<th><input type="checkbox" id="select_all"> Select
							All</th>
						<th>S.NO</th>
						<th>Version Name</th>
						<th>Release Notes</th>
						<th>Release Date</th>
						<th>Status</th>
						<th>Edit</th>
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

						<td><%=index++%></td>
						<td><%=dto.getVersionName()%></td>
						<td><%=dto.getReleaseNotes()%></td>
						<td><%=DataUtility.getDateString(dto.getReleaseDate())%></td>
						<td><%=dto.getVersionStatus()%></td>

						<td><a href="ControlCtl?id=<%=dto.getId()%>">Edit</a></td>

					</tr>
					<%
						}
					%>
				</tbody>

			</table>
		</div>

		<!-- Pagination -->
		<table width="100%">
			<tr>
				<td><input type="submit" name="operation"
					class="btn btn-warning" value="<%=ControlListCtl.OP_PREVIOUS%>"
					<%=pageNo == 1 ? "disabled" : ""%>></td>

				<td><input type="submit" name="operation"
					class="btn btn-primary" value="<%=ControlListCtl.OP_NEW%>">
				</td>

				<td><input type="submit" name="operation"
					class="btn btn-danger" value="<%=ControlListCtl.OP_DELETE%>">
				</td>

				<td align="right"><input type="submit" name="operation"
					class="btn btn-warning" value="<%=ControlListCtl.OP_NEXT%>"
					<%=(nextPageSize != 0) ? "" : "disabled"%>></td>
			</tr>
		</table>

		<input type="hidden" name="pageNo" value="<%=pageNo%>"> <input
			type="hidden" name="pageSize" value="<%=pageSize%>">

	</form>

</body>
<%@include file="FooterView.jsp"%>
</html>
