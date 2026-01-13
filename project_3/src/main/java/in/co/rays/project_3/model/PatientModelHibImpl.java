package in.co.rays.project_3.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.PatientDTO;
import in.co.rays.project_3.util.HibDataSource;

/**
 * Hibernate implementation of PatientModelInt
 *
 * This class performs database operations for Patient using Hibernate ORM.
 *
 * Operations: - Add Patient - Update Patient - Delete Patient - Find by Primary
 * Key - Search with pagination
 *
 * @author Yusuf Khan
 */
public class PatientModelHibImpl implements PatientModelInt {

	private static Logger log = Logger.getLogger(PatientModelHibImpl.class);

	/**
	 * Adds a new Patient record into database.
	 *
	 * @param dto PatientDTO
	 * @return generated primary key
	 */
	@Override
	public long add(PatientDTO dto) {

		log.info("PatientModel add started");

		Session session = HibDataSource.getSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.save(dto);
			tx.commit();
			log.info("Patient added successfully, ID : " + dto.getId());

		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			log.error("Error while adding Patient", e);
		} finally {
			session.close();
		}
		return dto.getId();
	}

	/**
	 * Deletes Patient record from database.
	 *
	 * @param dto PatientDTO
	 */
	public void delete(PatientDTO dto) {

		log.info("PatientModel delete started");

		Session session = null;
		Transaction tx = null;

		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.delete(dto);
			tx.commit();
			log.info("Patient deleted successfully, ID : " + dto.getId());

		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			log.error("Error while deleting Patient", e);
		} finally {
			session.close();
		}
	}

	/**
	 * Updates existing Patient record.
	 *
	 * @param dto PatientDTO
	 */
	public void update(PatientDTO dto) {

		log.info("PatientModel update started");

		Session session = null;
		Transaction tx = null;

		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(dto);
			tx.commit();
			log.info("Patient updated successfully, ID : " + dto.getId());

		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			log.error("Error while updating Patient", e);
		} finally {
			session.close();
		}
	}

	/**
	 * Finds Patient by primary key.
	 *
	 * @param pk primary key
	 * @return PatientDTO
	 */
	public PatientDTO findByPK(long pk) {

		log.debug("PatientModel findByPK started, ID : " + pk);

		Session session = null;
		PatientDTO dto = null;

		try {
			session = HibDataSource.getSession();
			dto = (PatientDTO) session.get(PatientDTO.class, pk);

		} catch (HibernateException e) {
			log.error("Error while finding Patient by PK", e);
		} finally {
			session.close();
		}
		return dto;
	}

	/**
	 * Searches Patient without pagination.
	 *
	 * @param dto PatientDTO
	 * @return list of PatientDTO
	 */
	@Override
	public List search(PatientDTO dto) {
		return search(null, 0, 0);
	}

	/**
	 * Searches Patient with pagination.
	 *
	 * @param dto      PatientDTO
	 * @param pageNo   page number
	 * @param pageSize page size
	 * @return list of PatientDTO
	 */
	@Override
	public List search(PatientDTO dto, int pageNo, int pageSize) {

		log.debug("PatientModel search started");

		Session session = null;
		ArrayList<PatientDTO> list = null;

		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(PatientDTO.class);

			if (dto != null) {

				if (dto.getId() != null) {
					criteria.add(Restrictions.like("id", dto.getId()));
				}

				if (dto.getName() != null && dto.getName().length() > 0) {
					criteria.add(Restrictions.like("name", dto.getName() + "%"));
				}

				if (dto.getDisease() != null && dto.getDisease().length() > 0) {
					criteria.add(Restrictions.like("disease", dto.getDisease() + "%"));
				}

				if (dto.getGender() != null && dto.getGender().length() > 0) {
					criteria.add(Restrictions.like("gender", dto.getGender() + "%"));
				}
			}

			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}

			list = (ArrayList<PatientDTO>) criteria.list();
			log.debug("Patient search completed, records found : " + list.size());

		} catch (HibernateException e) {
			log.error("Error while searching Patient", e);
		} finally {
			session.close();
		}

		return list;
	}
}
