
package in.co.rays.project_3.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import in.co.rays.project_3.dto.DoctorDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DatabaseException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.DoctorModelInt;
import in.co.rays.project_3.util.JDBCDataSource;

public class DoctorModelJDBCImpl implements DoctorModelInt {

	private static Logger log = Logger.getLogger(DoctorModelJDBCImpl.class.getName());

	@Override
	public long add(DoctorDTO dto) throws ApplicationException, DuplicateRecordException {
		log.info("DoctorModel add started");
		Connection conn = null;
		long pk = 0;

		DoctorDTO exist = findByName(dto.getName());
		if (exist != null) {
			throw new DuplicateRecordException("Doctor already exists");
		}

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			pk = getNextPk();

			PreparedStatement pstmt = conn.prepareStatement("insert into st_doctor values(?,?,?,?,?,?,?,?,?)");

			pstmt.setLong(1, pk);
			pstmt.setString(2, dto.getName());
			pstmt.setDate(3, new java.sql.Date(dto.getDob().getTime()));
			pstmt.setString(4, dto.getMobile());
			pstmt.setString(5, dto.getExperties());
			pstmt.setString(6, dto.getCreatedBy());
			pstmt.setString(7, dto.getModifiedBy());
			pstmt.setTimestamp(8, dto.getCreatedDatetime());
			pstmt.setTimestamp(9, dto.getModifiedDatetime());

			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();

			log.info("Doctor added successfully");

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Add rollback failed");
			}
			log.severe("Exception in add Doctor");
			throw new ApplicationException("Exception in add Doctor");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk;
	}

	@Override
	public void delete(DoctorDTO dto) throws ApplicationException {
		log.info("DoctorModel delete started");
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement("delete from st_doctor where id = ?");
			pstmt.setLong(1, dto.getId());

			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();

			log.info("Doctor deleted successfully");

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Delete rollback failed");
			}
			log.severe("Exception in delete Doctor");
			throw new ApplicationException("Exception in delete Doctor");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	@Override
	public void update(DoctorDTO dto) throws ApplicationException, DuplicateRecordException {
		log.info("DoctorModel update started");
		Connection conn = null;

		DoctorDTO exist = findByName(dto.getName());
		if (exist != null && exist.getId() != dto.getId()) {
			throw new DuplicateRecordException("Doctor already exists");
		}

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement(
					"update st_doctor set name=?, dob=?, mobile=?, experties=?, created_by=?, modified_by=?, created_datetime=?, modified_datetime=? where id=?");

			pstmt.setString(1, dto.getName());
			pstmt.setDate(2, new java.sql.Date(dto.getDob().getTime()));
			pstmt.setString(3, dto.getMobile());
			pstmt.setString(4, dto.getExperties());
			pstmt.setString(5, dto.getCreatedBy());
			pstmt.setString(6, dto.getModifiedBy());
			pstmt.setTimestamp(7, dto.getCreatedDatetime());
			pstmt.setTimestamp(8, dto.getModifiedDatetime());
			pstmt.setLong(9, dto.getId());

			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();

			log.info("Doctor updated successfully");

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Update rollback failed");
			}
			log.severe("Exception in update Doctor");
			throw new ApplicationException("Exception in update Doctor");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	@Override
	public List<DoctorDTO> list() throws ApplicationException {
		return search(null, 0, 0);
	}

	@Override
	public List<DoctorDTO> search(DoctorDTO dto, int pageNo, int pageSize) throws ApplicationException {
		log.info("DoctorModel search started");
		Connection conn = null;

		StringBuffer sql = new StringBuffer("select * from st_doctor where 1=1");
		List<DoctorDTO> list = new ArrayList<DoctorDTO>();

		if (dto != null) {

			if (dto.getId() > 0) {
				sql.append(" and id = " + dto.getId());
			}
			if (dto.getName() != null && dto.getName().length() > 0) {
				sql.append(" and name like '" + dto.getName() + "%'");
			}
			if (dto.getDob() != null) {
				sql.append(" and dob like '" + new java.sql.Date(dto.getDob().getTime()) + "%'");
			}
			if (dto.getMobile() != null && dto.getMobile().length() > 0) {
				sql.append(" and mobile = " + dto.getMobile());
			}
			if (dto.getExperties() != null && dto.getExperties().length() > 0) {
				sql.append(" and experties like '" + dto.getExperties() + "%'");
			}
		}

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + ", " + pageSize);
		}

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				dto = new DoctorDTO();
				dto.setId(rs.getLong(1));
				dto.setName(rs.getString(2));
				dto.setDob(rs.getDate(3));
				dto.setMobile(rs.getString(4));
				dto.setExperties(rs.getString(5));
				dto.setCreatedBy(rs.getString(6));
				dto.setModifiedBy(rs.getString(7));
				dto.setCreatedDatetime(rs.getTimestamp(8));
				dto.setModifiedDatetime(rs.getTimestamp(9));

				list.add(dto);
			}

			pstmt.close();
			rs.close();

		} catch (Exception e) {
			log.severe("Exception in search Doctor");
			throw new ApplicationException("Exception in search Doctor");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return list;
	}

	@Override
	public DoctorDTO findByPK(long pk) throws ApplicationException {
		log.info("DoctorModel findByPK started");
		Connection conn = null;
		DoctorDTO dto = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_doctor where id = ?");
			pstmt.setLong(1, pk);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new DoctorDTO();
				dto.setId(rs.getLong(1));
				dto.setName(rs.getString(2));
				dto.setDob(rs.getDate(3));
				dto.setMobile(rs.getString(4));
				dto.setExperties(rs.getString(5));
				dto.setCreatedBy(rs.getString(6));
				dto.setModifiedBy(rs.getString(7));
				dto.setCreatedDatetime(rs.getTimestamp(8));
				dto.setModifiedDatetime(rs.getTimestamp(9));
			}

			pstmt.close();
			rs.close();

		} catch (Exception e) {
			log.severe("Exception in findByPK Doctor");
			throw new ApplicationException("Exception in findByPK Doctor");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return dto;
	}

	@Override
	public DoctorDTO findByName(String name) throws ApplicationException {
		log.info("DoctorModel findByName started");
		Connection conn = null;
		DoctorDTO dto = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_doctor where name = ?");
			pstmt.setString(1, name);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new DoctorDTO();
				dto.setId(rs.getLong(1));
				dto.setName(rs.getString(2));
				dto.setDob(rs.getDate(3));
				dto.setMobile(rs.getString(4));
				dto.setExperties(rs.getString(5));
				dto.setCreatedBy(rs.getString(6));
				dto.setModifiedBy(rs.getString(7));
				dto.setCreatedDatetime(rs.getTimestamp(8));
				dto.setModifiedDatetime(rs.getTimestamp(9));
			}

			pstmt.close();
			rs.close();

		} catch (Exception e) {
			log.severe("Exception in findByName Doctor");
			throw new ApplicationException("Exception in findByName Doctor");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return dto;
	}

	private long getNextPk() throws DatabaseException {
		Connection conn = null;
		long pk = 0;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_doctor");
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				pk = rs.getLong(1);
			}

			pstmt.close();
			rs.close();

		} catch (Exception e) {
			throw new DatabaseException("Exception in getting next PK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk + 1;
	}
}
