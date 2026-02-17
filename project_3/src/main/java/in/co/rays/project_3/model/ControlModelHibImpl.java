package in.co.rays.project_3.model;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.ControlDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

public class ControlModelHibImpl implements ControlModelInt {

	// ========================= ADD =========================
	public long add(ControlDTO dto) throws ApplicationException, DuplicateRecordException {

		Session session = null;
		Transaction tx = null;

		ControlDTO duplicate = findByVersionName(dto.getVersionName());

		if (duplicate != null) {
			throw new DuplicateRecordException("Version Name already exists");
		}

		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.save(dto);
			tx.commit();

		} catch (HibernateException e) {

			if (tx != null) {
				tx.rollback();
			}
			throw new ApplicationException("Exception in Control Add " + e.getMessage());

		} finally {
			session.close();
		}

		return dto.getId();
	}

	// ========================= DELETE =========================
	public void delete(ControlDTO dto) throws ApplicationException {

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
			throw new ApplicationException("Exception in Control Delete");

		} finally {
			session.close();
		}
	}

	// ========================= UPDATE =========================
	public void update(ControlDTO dto) throws ApplicationException, DuplicateRecordException {

		Session session = null;
		Transaction tx = null;

		ControlDTO exist = findByVersionName(dto.getVersionName());

		if (exist != null && exist.getId() != dto.getId()) {
			throw new DuplicateRecordException("Version Name already exists");
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
			throw new ApplicationException("Exception in Control Update");

		} finally {
			session.close();
		}
	}

	// ========================= FIND BY PK =========================
	public ControlDTO findByPK(long pk) throws ApplicationException {

		Session session = null;
		ControlDTO dto = null;

		try {
			session = HibDataSource.getSession();
			dto = (ControlDTO) session.get(ControlDTO.class, pk);

		} catch (HibernateException e) {

			throw new ApplicationException("Exception in Control FindByPK");

		} finally {
			session.close();
		}

		return dto;
	}

	// ========================= FIND BY VERSION NAME =========================
	public ControlDTO findByVersionName(String versionName) throws ApplicationException {

		Session session = null;
		ControlDTO dto = null;

		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(ControlDTO.class);
			criteria.add(Restrictions.eq("versionName", versionName));

			List list = criteria.list();

			if (list.size() == 1) {
				dto = (ControlDTO) list.get(0);
			}

		} catch (HibernateException e) {

			throw new ApplicationException("Exception in Control FindByVersionName");

		} finally {
			session.close();
		}

		return dto;
	}

	// ========================= LIST =========================
	public List list() throws ApplicationException {
		return list(0, 0);
	}

	public List list(int pageNo, int pageSize) throws ApplicationException {

		Session session = null;
		List list = null;

		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(ControlDTO.class);

			if (pageSize > 0) {
				criteria.setFirstResult((pageNo - 1) * pageSize);
				criteria.setMaxResults(pageSize);
			}

			list = criteria.list();

		} catch (HibernateException e) {

			throw new ApplicationException("Exception in Control List");

		} finally {
			session.close();
		}

		return list;
	}

	// ========================= SEARCH =========================
	public List search(ControlDTO dto) throws ApplicationException {
		return search(dto, 0, 0);
	}

	public List search(ControlDTO dto, int pageNo, int pageSize) throws ApplicationException {

		Session session = null;
		List list = null;

		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(ControlDTO.class);

			if (dto.getVersionName() != null && dto.getVersionName().length() > 0) {
				criteria.add(Restrictions.like("versionName", dto.getVersionName() + "%"));
			}

			if (dto.getVersionStatus() != null && dto.getVersionStatus().length() > 0) {
				criteria.add(Restrictions.like("versionStatus", dto.getVersionStatus() + "%"));
			}

			if (dto.getReleaseDate() != null) {
				criteria.add(Restrictions.eq("releaseDate", dto.getReleaseDate()));
			}

			if (pageSize > 0) {
				criteria.setFirstResult((pageNo - 1) * pageSize);
				criteria.setMaxResults(pageSize);
			}

			list = criteria.list();

		} catch (HibernateException e) {

			throw new ApplicationException("Exception in Control Search");

		} finally {
			session.close();
		}

		return list;
	}
}
