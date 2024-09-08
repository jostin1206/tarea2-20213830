package com.example.tarea2.controllers;

import com.example.tarea2.entity.Employee;
import com.example.tarea2.repository.EmployeeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class EmployeesController {
    final EmployeeRepository employeeRepository;

    public EmployeesController(EmployeeRepository employeeRepository) {

        this.employeeRepository = employeeRepository;
    }

    @GetMapping({"/employee/list", "/employee"})
    public String listarEmpleados(Model model) {

        model.addAttribute("lista", employeeRepository.findAll());
        return "employee/lista";
    }

    @GetMapping("/employee/info")
    public String informEmpleado(Model model, @RequestParam("id") int id) {

        Optional<Employee> optEmployee = employeeRepository.findById(id);

        if (optEmployee.isPresent()) {

            Employee employee = optEmployee.get();
            model.addAttribute("employee", employee);
            return "employee/info";
        } else {
            return "redirect:/employee/list";
        }
    }

    @GetMapping("/employee/delete")
    public String borrarEmpleado(RedirectAttributes redirectAttributes, @RequestParam("id") int id) {
        //model.addAttribute("tipo_alert", "success");
        //model.addAttribute("msg", "Se borró el empleado");

        try {
            System.out.println("Intentando eliminar empleado con ID: " + id);
            Optional<Employee> optionalEmployee = employeeRepository.findById(id);

            if (optionalEmployee.isPresent()) {
                employeeRepository.deleteById(id);
                redirectAttributes.addFlashAttribute("tipo_alert", "success");
                redirectAttributes.addFlashAttribute("msg", "Se borró el empleado");
            } else {
                redirectAttributes.addFlashAttribute("tipo_alert", "warning");
                redirectAttributes.addFlashAttribute("msg", "No se pudo borrar el empleado");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("tipo_alert", "danger");
            redirectAttributes.addFlashAttribute("msg", "No se puede borrar el empleado");
            e.printStackTrace(); // Imprime el stack trace para depuración
        }
        return "redirect:/employee/list";
    }
}
