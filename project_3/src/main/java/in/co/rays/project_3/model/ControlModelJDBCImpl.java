package in.co.rays.project_3.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.ControlDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DatabaseException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.JDBCDataSource;

public class ControlModelJDBCImpl implements ControlModelInt {

	private static Logger log = Logger.getLogger(ControlModelJDBCImpl.class);

	// ===================== NEXT PK =====================
	public long nextPK() throws DatabaseException {

		long pk = 0;
		Connection con = null;

		try {
			con = JDBCDataSource.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT MAX(ID) FROM ST_CONTROL");

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				pk = rs.getLong(1);
			}

			rs.close();
			ps.close();

		} catch (Exception e) {
			throw new DatabaseException("Database Exception " + e);
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		return pk + 1;
	}

	// ===================== ADD =====================
	public long add(ControlDTO dto) throws ApplicationException, DuplicateRecordException {

		long pk = 0;
		Connection conn = null;

		ControlDTO duplicate = findByVersionName(dto.getVersionName());

		if (duplicate != null) {
			throw new DuplicateRecordException("Version Name already exists");
		}

		try {
			conn = JDBCDataSource.getConnection();
			pk = nextPK();

			conn.setAutoCommit(false);

			PreparedStatement ps = conn.prepareStatement("INSERT INTO ST_CONTROL VALUES(?,?,?,?,?,?,?,?,?)");

			ps.setLong(1, pk);
			ps.setString(2, dto.getVersionName());
			ps.setString(3, dto.getReleaseNotes());
			ps.setDate(4, new java.sql.Date(dto.getReleaseDate().getTime()));
			ps.setString(5, dto.getVersionStatus());
			ps.setString(6, dto.getCreatedBy());
			ps.setString(7, dto.getModifiedBy());
			ps.setTimestamp(8, dto.getCreatedDatetime());
			ps.setTimestamp(9, dto.getModifiedDatetime());

			ps.executeUpdate();
			conn.commit();
			ps.close();

		} catch (Exception e) {

			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Rollback Exception " + ex.getMessage());
			}

			throw new ApplicationException("Exception in adding Control");

		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return pk;
	}

	// ===================== DELETE =====================
	public void delete(ControlDTO dto) throws ApplicationException {

		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement ps = conn.prepareStatement("DELETE FROM ST_CONTROL WHERE ID=?");

			ps.setLong(1, dto.getId());
			ps.executeUpdate();

			conn.commit();
			ps.close();

		} catch (Exception e) {

			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Rollback Exception");
			}

			throw new ApplicationException("Exception in deleting Control");

		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	// ===================== UPDATE =====================
	public void update(ControlDTO dto) throws ApplicationException, DuplicateRecordException {

		Connection conn = null;

		ControlDTO exist = findByVersionName(dto.getVersionName());

		if (exist != null && exist.getId() != dto.getId()) {
			throw new DuplicateRecordException("Version Name already exists");
		}

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement ps = conn.prepareStatement(
					"UPDATE ST_CONTROL SET VERSION_NAME=?,RELEASE_NOTES=?,RELEASE_DATE=?,VERSION_STATUS=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?");

			ps.setString(1, dto.getVersionName());
			ps.setString(2, dto.getReleaseNotes());
			ps.setDate(3, new java.sql.Date(dto.getReleaseDate().getTime()));
			ps.setString(4, dto.getVersionStatus());
			ps.setString(5, dto.getCreatedBy());
			ps.setString(6, dto.getModifiedBy());
			ps.setTimestamp(7, dto.getCreatedDatetime());
			ps.setTimestamp(8, dto.getModifiedDatetime());
			ps.setLong(9, dto.getId());

			ps.executeUpdate();
			conn.commit();
			ps.close();

		} catch (Exception e) {

			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Rollback Exception");
			}

			throw new ApplicationException("Exception in updating Control");

		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	// ===================== FIND BY PK =====================
	public ControlDTO findByPK(long pk) throws ApplicationException {

		Connection conn = null;
		ControlDTO dto = null;

		try {
			conn = JDBCDataSource.getConnection();

			PreparedStatement ps = conn.prepareStatement("SELECT * FROM ST_CONTROL WHERE ID=?");

			ps.setLong(1, pk);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				dto = new ControlDTO();

				dto.setId(rs.getLong(1));
				dto.setVersionName(rs.getString(2));
				dto.setReleaseNotes(rs.getString(3));
				dto.setReleaseDate(rs.getDate(4));
				dto.setVersionStatus(rs.getString(5));
				dto.setCreatedBy(rs.getString(6));
				dto.setModifiedBy(rs.getString(7));
				dto.setCreatedDatetime(rs.getTimestamp(8));
				dto.setModifiedDatetime(rs.getTimestamp(9));
			}

			rs.close();
			ps.close();

		} catch (Exception e) {
			throw new ApplicationException("Exception in findByPK Control");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return dto;
	}

	// ===================== FIND BY VERSION NAME =====================
	public ControlDTO findByVersionName(String versionName) throws ApplicationException {

		Connection conn = null;
		ControlDTO dto = null;

		try {
			conn = JDBCDataSource.getConnection();

			PreparedStatement ps = conn.prepareStatement("SELECT * FROM ST_CONTROL WHERE VERSION_NAME=?");

			ps.setString(1, versionName);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				dto = new ControlDTO();

				dto.setId(rs.getLong(1));
				dto.setVersionName(rs.getString(2));
				dto.setReleaseNotes(rs.getString(3));
				dto.setReleaseDate(rs.getDate(4));
				dto.setVersionStatus(rs.getString(5));
				dto.setCreatedBy(rs.getString(6));
				dto.setModifiedBy(rs.getString(7));
				dto.setCreatedDatetime(rs.getTimestamp(8));
				dto.setModifiedDatetime(rs.getTimestamp(9));
			}

			rs.close();
			ps.close();

		} catch (Exception e) {
			throw new ApplicationException("Exception in findByVersionName");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return dto;
	}

	@Override
	public List list() throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List list(int pageNo, int pageSize) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List search(ControlDTO dto) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List search(ControlDTO dto, int pageNo, int pageSize) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}
}
