package in.ravikalla.microservices.customerservice.service.mapper;

import in.ravikalla.microservices.customerservice.domain.*;
import in.ravikalla.microservices.customerservice.service.dto.AppointmentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Appointment and its DTO AppointmentDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AppointmentMapper extends EntityMapper<AppointmentDTO, Appointment> {



    default Appointment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Appointment appointment = new Appointment();
        appointment.setId(id);
        return appointment;
    }
}
