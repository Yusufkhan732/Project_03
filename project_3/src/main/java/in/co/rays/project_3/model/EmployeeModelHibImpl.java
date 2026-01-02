package in.co.rays.project_3.model;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.EmployeeDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

public class EmployeeModelHibImpl implements EmployeeModelInt {

	public long add(EmployeeDTO dto) throws ApplicationException, DuplicateRecordException {

		/* log.debug("EmployeeModel hib start"); */

		EmployeeDTO existDto = null;
		existDto = findByUsername(dto.getUsername());
		if (existDto != null) {
			throw new DuplicateRecordException("login id already exist");
		}
		Session session = HibDataSource.getSession();
		Transaction tx = null;
		try {

			int pk = 0;
			tx = session.beginTransaction();

			session.save(dto);
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			// TODO: handle exception
			if (tx != null) {
				tx.rollback();

			}
			throw new ApplicationException("Exception in Employee Add " + e.getMessage());
		} finally {
			session.close();
		}
		/* log.debug("Model add End"); */
		return dto.getId();

	}

	public void update(EmployeeDTO dto) throws ApplicationException, DuplicateRecordException {
		Session session = null;
		Transaction tx = null;
		EmployeeDTO existDto = findByUsername(dto.getUsername());
		// Check if updated LoginId already exist
		if (existDto != null && existDto.getId() != dto.getId()) {
			throw new DuplicateRecordException("Username is already exist");
		}

		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(dto);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new ApplicationException("Exception in Employee update" + e.getMessage());
		} finally {
			session.close();
		}
	}

	public void delete(EmployeeDTO dto) throws ApplicationException {
		Session session = null;
		Transaction tx = null;
		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.delete(dto);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new ApplicationException("Exception in Employee Delete" + e.getMessage());
		} finally {
			session.close();
		}
	}

	public EmployeeDTO findByPK(long pk) throws ApplicationException {
		Session session = null;
		EmployeeDTO dto = null;
		try {
			session = HibDataSource.getSession();
			dto = (EmployeeDTO) session.get(EmployeeDTO.class, pk);

		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in getting employee by pk");
		} finally {
			session.close();
		}
		return dto;
	}

	public EmployeeDTO findByUsername(String username) throws ApplicationException {
		Session session = null;
		EmployeeDTO dto = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(EmployeeDTO.class);
			criteria.add(Restrictions.eq("username", username));
			List list = criteria.list();
			if (list.size() == 1) {
				dto = (EmployeeDTO) list.get(0);
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new ApplicationException("Exception in getting employee by username " + e.getMessage());

		} finally {
			session.close();
		}

		return dto;
	}

	public List list() throws ApplicationException {
		return search(null, 0, 0);
	}

	public List search(EmployeeDTO dto, int pageNo, int pageSize) throws ApplicationException {

		Session session = null;
		List list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(EmployeeDTO.class);
			if (dto != null) {
				if (dto.getId() != null) {
					criteria.add(Restrictions.like("id", dto.getId()));
				}
				if (dto.getFullName() != null && dto.getFullName().length() > 0) {
					criteria.add(Restrictions.like("fullName", dto.getFullName() + "%"));
				}

				if (dto.getUsername() != null && dto.getUsername().length() > 0) {
					criteria.add(Restrictions.like("username", dto.getUsername() + "%"));
				}
				if (dto.getPassword() != null && dto.getPassword().length() > 0) {
					criteria.add(Restrictions.like("password", dto.getPassword() + "%"));
				}

				if (dto.getBirthDate() != null && dto.getBirthDate().getTime() > 0) {
					criteria.add(Restrictions.eq("birthDate", dto.getBirthDate()));
				}

				if (dto.getContactNo() != null && dto.getContactNo().length() > 0) {
					criteria.add(Restrictions.like("contactNo", dto.getContactNo() + "%"));
				}

			}
			// if pageSize is greater than 0
			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}
			list = criteria.list();
		} catch (HibernateException e) {
			throw new ApplicationException("Exception in Employee search");
		} finally {
			session.close();
		}

		return list;
	}

}
