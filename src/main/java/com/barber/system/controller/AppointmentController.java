package com.barber.system.controller;

import com.barber.system.model.Appointment;
import com.barber.system.model.User;
import com.barber.system.model.enums.AppointmentStatus;
import com.barber.system.service.AppointmentService;
import com.barber.system.service.ServiceService;
import com.barber.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final UserService userService;
    private final ServiceService serviceService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService, UserService userService, ServiceService serviceService) {
        this.appointmentService = appointmentService;
        this.userService = userService;
        this.serviceService = serviceService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("appointments", appointmentService.getAppointmentsForToday());
        model.addAttribute("totalEarnings", appointmentService.calculateTotalEarningsForToday());
        
        // Prepare empty appointment for the Quick Book form
        Appointment newAppointment = new Appointment();
        newAppointment.setAppointmentDateTime(LocalDateTime.now().withSecond(0).withNano(0));
        model.addAttribute("newAppointment", newAppointment);
        
        // For the form, we need a list of barbers (users)
        model.addAttribute("barbers", userService.getAllUsers());
        
        // For the form, we need the list of services
        model.addAttribute("services", serviceService.getAllServices());
        
        return "dashboard";
    }

    @PostMapping("/book")
    public String bookAppointment(@ModelAttribute Appointment appointment, 
                                @RequestParam String clientPhone, 
                                @RequestParam String clientName,
                                @RequestParam(required = false) String clientEmail,
                                @RequestParam Long serviceId,
                                org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            // Ensuring we have a dummy user if none exists yet for the MVP
            if (appointment.getUser() == null) {
                List<User> barbers = userService.getAllUsers();
                if (!barbers.isEmpty()) {
                    appointment.setUser(barbers.get(0));
                }
            }
            
            appointmentService.bookAppointment(appointment, clientPhone, clientName, clientEmail, serviceId);
            redirectAttributes.addFlashAttribute("successMessage", "Rendez-vous confirmé avec succès pour " + clientName);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/complete/{id}")
    public String completeAppointment(@PathVariable Long id) {
        appointmentService.updateStatus(id, AppointmentStatus.COMPLETED);
        return "redirect:/dashboard";
    }

    @PostMapping("/cancel/{id}")
    public String cancelAppointment(@PathVariable Long id) {
        appointmentService.updateStatus(id, AppointmentStatus.CANCELLED);
        return "redirect:/redirect:/dashboard";
    }
}
