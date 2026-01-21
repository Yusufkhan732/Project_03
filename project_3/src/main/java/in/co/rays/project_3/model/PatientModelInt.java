package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.PatientDTO;
import in.co.rays.project_3.exception.ApplicationException;

public interface PatientModelInt {

	public long add(PatientDTO dto) throws ApplicationException;

	public void delete(PatientDTO dto);

	public void update(PatientDTO dto) throws ApplicationException;

	public PatientDTO findByPK(long pk);

	public List search(PatientDTO dto);

	public List search(PatientDTO dto, int pageNo, int pageSize);

}