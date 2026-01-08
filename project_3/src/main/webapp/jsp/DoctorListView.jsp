<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.project_3.controller.DoctorListCtl"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.HTMLUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.dto.DoctorDTO"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>

<!DOCTYPE html>
<html>
<head>
<title>Doctor List</title>

<script src="<%=ORSView.APP_CONTEXT%>/js/jquery.min.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/CheckBox11.js"></script>

<style>
.hm {
	background-image: url('<%=ORSView.APP_CONTEXT%>/img/al.jpg');
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

	<form action="<%=ORSView.DOCTOR_LIST_CTL%>" method="post">

		<jsp:useBean id="dto" class="in.co.rays.project_3.dto.DoctorDTO"
			scope="request"></jsp:useBean>

		<%
			int pageNo = ServletUtility.getPageNo(request);
			int pageSize = ServletUtility.getPageSize(request);
			int index = ((pageNo - 1) * pageSize) + 1;
			int nextPageSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());

			HashMap expertiesMap = (HashMap) request.getAttribute("expertiesMap");

			List list = ServletUtility.getList(request);
			Iterator<DoctorDTO> it = list.iterator();

			if (list.size() != 0) {
		%>

		<center>
			<h1 class="text-dark font-weight-bold pt-3">
				<u>Doctor List</u>
			</h1>
		</center>

		<!-- SUCCESS -->
		<%
			if (!ServletUtility.getSuccessMessage(request).equals("")) {
		%>
		<div class="row">
			<div class="col-md-4"></div>
			<div class="col-md-4 alert alert-success alert-dismissible"
				style="background-color: #80ff80">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				<font color="green"><%=ServletUtility.getSuccessMessage(request)%></font>
			</div>
			<div class="col-md-4"></div>
		</div>
		<%
			}
		%>


		<%
			if (!ServletUtility.getErrorMessage(request).equals("")) {
		%>
		<div class="row">
			<div class="col-md-4"></div>
			<div class="col-md-4 alert alert-danger alert-dismissible">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				<font color="red"><%=ServletUtility.getErrorMessage(request)%></font>
			</div>
			<div class="col-md-4"></div>
		</div>
		<%
			}
		%>


		<div class="row">

			<div class="col-sm-2"></div>

			<div class="col-sm-2">
				<input type="text" name="name" class="form-control"
					placeholder="Enter Name"
					value="<%=ServletUtility.getParameter("name", request)%>">
			</div>

			&nbsp;

			<div class="col-sm-2">
				<input type="text" name="mobile" class="form-control"
					placeholder="Enter Mobile"
					value="<%=ServletUtility.getParameter("mobile", request)%>">
			</div>

			&nbsp;

			<div class="col-sm-3">
				<%=HTMLUtility.getList("experties", String.valueOf(dto.getExperties()), expertiesMap)%>
			</div>

			<div class="col-sm-2">
				<input type="submit" class="btn btn-primary btn-md" name="operation"
					value="<%=DoctorListCtl.OP_SEARCH%>"> <input type="submit"
					class="btn btn-dark btn-md" name="operation"
					value="<%=DoctorListCtl.OP_RESET%>">
			</div>

			<div class="col-sm-1"></div>
		</div>

		<br>


		<div class="table-responsive">
			<table class="table table-bordered table-dark table-hover">

				<thead>
					<tr style="background-color: #8C8C8C;">
						<th width="10%"><input type="checkbox" id="select_all">
							Select All</th>
						<th width="5%" class="text">S.NO</th>
						<th width="20%" class="text">Name</th>
						<th width="15%" class="text">DOB</th>
						<th width="15%" class="text">Mobile</th>
						<th width="20%" class="text">Experties</th>
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
						<td class="text"><%=dto.getName()%></td>
						<td class="text"><%=DataUtility.getDateString(dto.getDob())%></td>
						<td class="text"><%=dto.getMobile()%></td>
						<td class="text"><%=dto.getExperties()%></td>
						<td class="text"><a href="DoctorCtl?id=<%=dto.getId()%>">Edit</a>
						</td>
					</tr>
					<%
						}
					%>
				</tbody>

			</table>
		</div>

		<table width="100%">
			<tr>
				<td><input type="submit" class="btn btn-warning btn-md"
					name="operation" value="<%=DoctorListCtl.OP_PREVIOUS%>"
					<%=pageNo == 1 ? "disabled" : ""%>></td>

				<td><input type="submit" class="btn btn-primary btn-md"
					name="operation" value="<%=DoctorListCtl.OP_NEW%>"></td>

				<td><input type="submit" class="btn btn-danger btn-md"
					name="operation" value="<%=DoctorListCtl.OP_DELETE%>"></td>

				<td align="right"><input type="submit"
					class="btn btn-warning btn-md" name="operation"
					value="<%=DoctorListCtl.OP_NEXT%>"
					<%=nextPageSize != 0 ? "" : "disabled"%>></td>
			</tr>
		</table>

		<%
			}
		%>

		<input type="hidden" name="pageNo" value="<%=pageNo%>"> <input
			type="hidden" name="pageSize" value="<%=pageSize%>">

	</form>

</body>
<%@include file="FooterView.jsp"%>
</html>
