package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.ControlDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

public interface ControlModelInt {

	public long add(ControlDTO dto) throws ApplicationException, DuplicateRecordException;

	public void delete(ControlDTO dto) throws ApplicationException;

	public void update(ControlDTO dto) throws ApplicationException, DuplicateRecordException;

	public List list() throws ApplicationException;

	public List list(int pageNo, int pageSize) throws ApplicationException;

	public List search(ControlDTO dto) throws ApplicationException;

	public List search(ControlDTO dto, int pageNo, int pageSize) throws ApplicationException;

	public ControlDTO findByPK(long pk) throws ApplicationException;

	public ControlDTO findByVersionName(String versionName) throws ApplicationException;
}
