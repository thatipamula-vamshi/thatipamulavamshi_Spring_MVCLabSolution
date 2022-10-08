package fest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fest.entity.Student;
import fest.service.StudentService;

@Controller
@RequestMapping("/students")
public class StudentController {

	@Autowired
	private StudentService studentService;

	@RequestMapping("/list")
	public String listStudents(Model theModel) {
		List<Student> students = studentService.findAll();
		theModel.addAttribute("Students", students);
		return "list-students";
	}

	@RequestMapping("/showFormForAdd")
	public String showFormForUpdate(Model theModel) {
		// create model attribute to bind form data
		Student theStudent = new Student();
		theModel.addAttribute("Student", theStudent);
		return "student-form";
	}

	@RequestMapping("/showFormForUpdate")
	public String showFormForUpdate(@RequestParam("studentId") int theId, Model theModel) {
		// get the Student from the service
		Student theStudent = studentService.findById(theId);
		// set Student as a model attribute to pre-populate the form
		theModel.addAttribute("Student", theStudent);
		return "student-form";
	}

	@PostMapping("/save")
	public String saveStudent(@RequestParam("id") int id, @RequestParam("first_name") String fName,
			@RequestParam("last_name") String lName, @RequestParam("department") String department,
			@RequestParam("country") String country) {

		System.out.println(id);
		Student theStudent;
		if (id != 0) {
			theStudent = studentService.findById(id);
			theStudent.setFirstName(fName);
			theStudent.setLastName(lName);
			theStudent.setDepartment(department);
			theStudent.setCountry(country);
		} else {
			theStudent = new Student(fName, lName, department, country);
		}
		// save the Student
		studentService.save(theStudent);

		// use a redirect to prevent duplicate submissions
		return "redirect:/students/list";
	}

	@RequestMapping("/delete")
	public String delete(@RequestParam("studentId") int theId) {

		// delete the Student
		studentService.deleteById(theId);

		// redirect to /Students/list
		return "redirect:/students/list";
	}

	@RequestMapping("/search")
	public String search(@RequestParam("first_name") String firstName, 
			@RequestParam("last_name") String lastName, 
			@RequestParam("department") String department, 
			@RequestParam("country") String country, Model theModel) {

		// check names, if all fields are empty then just give list of all Students

		if (firstName.trim().isEmpty() && lastName.trim().isEmpty() &&  
				department.trim().isEmpty() &&  country.trim().isEmpty() ) {
			return "redirect:/students/list";
		}
		else {
			// else, search by first name and last name
			List<Student> theStudents = studentService.searchBy(firstName, lastName, department, country);

			// add to the spring model
			theModel.addAttribute("Students", theStudents);

			// send to list-Books
			return "list-students";
		}
	}

}
