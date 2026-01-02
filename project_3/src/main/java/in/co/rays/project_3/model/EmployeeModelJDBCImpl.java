package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.EmployeeDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

public class EmployeeModelJDBCImpl implements EmployeeModelInt {

	@Override
	public long add(EmployeeDTO dto) throws ApplicationException, DuplicateRecordException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void update(EmployeeDTO dto) throws ApplicationException, DuplicateRecordException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(EmployeeDTO dto) throws ApplicationException {
		// TODO Auto-generated method stub

	}

	@Override
	public EmployeeDTO findByPK(long pk) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EmployeeDTO findByUsername(String username) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List list() throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List search(EmployeeDTO dto, int pageNo, int pageSize) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

}