package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.DoctorDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

public interface DoctorModelInt {

	/**
	 * Interface of Doctor model
	 * 
	 *  @author Yusuf Khan
	 *
	 */
	public long add(DoctorDTO dto) throws ApplicationException, DuplicateRecordException;

	public void delete(DoctorDTO dto) throws ApplicationException;

	public void update(DoctorDTO dto) throws ApplicationException, DuplicateRecordException;

	public List list() throws ApplicationException;

	public List search(DoctorDTO dto, int pageNo, int pageSize) throws ApplicationException;

	public DoctorDTO findByPK(long pk) throws ApplicationException;

	public DoctorDTO findByName(String name) throws ApplicationException;
}
