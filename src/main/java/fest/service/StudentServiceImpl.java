package fest.service;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fest.entity.Student;

@Repository
public class StudentServiceImpl implements StudentService{
	
	private SessionFactory sessionFactory;
	
	//Create session
	private Session session;
	
	@Autowired
	public StudentServiceImpl(SessionFactory sessionFactory) {
		super();
		this.sessionFactory = sessionFactory;
		try {
			session = sessionFactory.getCurrentSession();
		}
		catch(HibernateException e){
			session=sessionFactory.openSession();			
		}
	}

	@Override
	@Transactional
	public List<Student> findAll() {
		Transaction tx = session.beginTransaction();
		List<Student> students = session.createQuery("from Student").list();
		tx.commit();
		return students;
	}

	@Override
	@Transactional
	public Student findById(int theId) {
		Student student = new Student();
		Transaction tx = session.beginTransaction();
		student = session.get(Student.class, theId);
		tx.commit();
		return student;
	}

	@Override
	@Transactional
	public void save(Student theStudent) {
		Transaction tx = session.beginTransaction();
		session.saveOrUpdate(theStudent);
		tx.commit();
	}

	@Override
	@Transactional
	public void deleteById(int theId) {
		Transaction tx = session.beginTransaction();
		Student student = session.get(Student.class, theId);
		session.delete(student);
		tx.commit();		
	}

	@Override
	@Transactional
	public List<Student> searchBy(String firstName, String lastName, String department, String country) {
		Transaction tx = session.beginTransaction();
		String query = "";
		
		if(firstName.length()!=0 && lastName.length()!=0 && 
				department.length()!=0 && country.length()!=0) {
			query = "from Student where first_name like '%"+firstName+"%' or last_name like '%"+
				lastName+"%' or department like '%"+department+"%' or country like '%"+country+
				"%'";
		}
		
		else if(firstName.length()!=0 && lastName.length()!=0 && 
				department.length()!=0) {
			query = "from Student where first_name like '%"+firstName+"%' or last_name like '%"+
					lastName+"%' or department like '%"+department+"%'";
		}
		
		else if(firstName.length()!=0 && department.length()!=0 && 
				country.length()!=0) {
			query = "from Student where first_name like '%"+firstName+"%'or department like '%"+
				department+"%' or country like '%"+country+"%'";
		}
		
		else if(lastName.length()!=0 && department.length()!=0 && 
				country.length()!=0) {
			query = "from Student where last_name like '%"+lastName+"%' or department like '%"+
				department+"%' or country like '%"+country+"%'";
		}
		
		else if(firstName.length()!=0 && lastName.length()!=0) {
			query = "from Student where first_name like '%"+firstName+"%' or last_name like '%"+
					lastName+"%'";
		}
		
		else if(firstName.length()!=0 && department.length()!=0) {
			query = "from Student where first_name like '%"+firstName+"%' or department like '%"+
					department+"%'";
		}
		
		else if(firstName.length()!=0 && country.length()!=0) {
			query = "from Student where first_name like '%"+firstName+"%'or country like '%"+
					country+"%'";
		}
		
		else if(lastName.length()!=0 && department.length()!=0) {
			query = "from Student where last_name like '%"+lastName+"%' or department like '%"+
					department+"%'";
		}
		
		else if(lastName.length()!=0 && country.length()!=0) {
			query = "from Student where last_name like '%"+lastName+"%' orcountry like '%"+
					country+"%'";
		}
		
		else if(department.length()!=0 && country.length()!=0) {
			query = "from Student where department like '%"+department+"%' or country like '%"+
					country+"%'";
		}
		
		else if(firstName.length()!=0) {
			query = "from Student where first_name like '%"+firstName+"%'";
		}
		
		else if(lastName.length()!=0) {
			query = "from Student where last_name like '%"+lastName+"%'";
		}
		
		else if(department.length()!=0) {
			query = "from Student where department like '%"+department+"%'";
		}
		
		else if(country.length()!=0) {
			query = "from Student where country like '%"+country+"%'";
		}
		List<Student> students = session.createQuery(query).list();
		
		tx.commit();
		
		return students;
	}

}
