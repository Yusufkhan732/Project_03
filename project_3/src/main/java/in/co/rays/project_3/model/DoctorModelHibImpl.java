
package in.co.rays.project_3.model;

import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import in.co.rays.project_3.dto.DoctorDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.DoctorModelInt;
import in.co.rays.project_3.util.HibDataSource;

public class DoctorModelHibImpl implements DoctorModelInt {
	/**
	 * Hibernate implements of Doctor model
	 * 
	 *  @author Yusuf Khan
	 *
	 */

	private static Logger log = Logger.getLogger(DoctorModelHibImpl.class.getName());

	@Override
	public long add(DoctorDTO dto) throws ApplicationException, DuplicateRecordException {
		log.info("DoctorModelHibImpl add started");

		DoctorDTO existDto = findByName(dto.getName());
		if (existDto != null) {
			throw new DuplicateRecordException("Doctor name already exists");
		}

		Session session = HibDataSource.getSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.save(dto);
			tx.commit();
			log.info("Doctor added successfully");

		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			log.severe("Exception in Doctor add");
			throw new ApplicationException("Exception in Doctor add " + e.getMessage());
		} finally {
			session.close();
		}

		return dto.getId();
	}

	@Override
	public void delete(DoctorDTO dto) throws ApplicationException {
		log.info("DoctorModelHibImpl delete started");

		Session session = null;
		Transaction tx = null;

		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.delete(dto);
			tx.commit();
			log.info("Doctor deleted successfully");

		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			log.severe("Exception in Doctor delete");
			throw new ApplicationException("Exception in Doctor delete " + e.getMessage());
		} finally {
			session.close();
		}
	}

	@Override
	public void update(DoctorDTO dto) throws ApplicationException, DuplicateRecordException {
		log.info("DoctorModelHibImpl update started");

		DoctorDTO existDto = findByName(dto.getName());
		if (existDto != null && existDto.getId() != dto.getId()) {
			throw new DuplicateRecordException("Doctor name already exists");
		}

		Session session = null;
		Transaction tx = null;

		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(dto);
			tx.commit();
			log.info("Doctor updated successfully");

		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			log.severe("Exception in Doctor update");
			throw new ApplicationException("Exception in Doctor update " + e.getMessage());
		} finally {
			session.close();
		}
	}

	@Override
	public DoctorDTO findByPK(long pk) throws ApplicationException {
		log.info("DoctorModelHibImpl findByPK started");

		Session session = null;
		DoctorDTO dto = null;

		try {
			session = HibDataSource.getSession();
			dto = (DoctorDTO) session.get(DoctorDTO.class, pk);

		} catch (HibernateException e) {
			log.severe("Exception in findByPK Doctor");
			throw new ApplicationException("Exception in getting Doctor by PK");
		} finally {
			session.close();
		}

		return dto;
	}

	@Override
	public DoctorDTO findByName(String name) throws ApplicationException {
		log.info("DoctorModelHibImpl findByName started");

		Session session = null;
		DoctorDTO dto = null;

		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(DoctorDTO.class);
			criteria.add(Restrictions.eq("name", name));

			List list = criteria.list();
			if (list.size() == 1) {
				dto = (DoctorDTO) list.get(0);
			}

		} catch (HibernateException e) {
			log.severe("Exception in findByName Doctor");
			throw new ApplicationException("Exception in findByName Doctor " + e.getMessage());
		} finally {
			session.close();
		}

		return dto;
	}

	@Override
	public List list() throws ApplicationException {
		return list(0, 0);
	}

	public List list(int pageNo, int pageSize) throws ApplicationException {
		log.info("DoctorModelHibImpl list started");

		Session session = null;
		List list = null;

		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(DoctorDTO.class);

			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}

			list = criteria.list();

		} catch (HibernateException e) {
			log.severe("Exception in Doctor list");
			throw new ApplicationException("Exception in Doctor list");
		} finally {
			session.close();
		}

		return list;
	}

	@Override
	public List search(DoctorDTO dto, int pageNo, int pageSize) throws ApplicationException {
		log.info("DoctorModelHibImpl search started");

		Session session = null;
		List list = null;

		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(DoctorDTO.class);

			if (dto != null) {

				if (dto.getId() != null && dto.getId() > 0) {
					criteria.add(Restrictions.eq("id", dto.getId()));
				}

				if (dto.getName() != null && dto.getName().length() > 0) {
					criteria.add(Restrictions.like("name", dto.getName() + "%"));
				}

				if (dto.getMobile() != null && dto.getMobile().length() > 0) {
					criteria.add(Restrictions.like("mobile", dto.getMobile() + "%"));
				}

				if (dto.getExperties() != null && dto.getExperties().length() > 0) {
					criteria.add(Restrictions.like("experties", dto.getExperties() + "%"));
				}
			}

			if (pageSize > 0) {
				criteria.setFirstResult((pageNo - 1) * pageSize);
				criteria.setMaxResults(pageSize);
			}

			list = criteria.list();

		} catch (HibernateException e) {
			log.severe("Exception in Doctor search");
			throw new ApplicationException("Exception in Doctor search");
		} finally {
			session.close();
		}

		return list;
	}
}
