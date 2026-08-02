package com.acharya.dikshanta.HospitalManagement.appointment.event;

import java.util.UUID;

public record AppointmentBookedEvent(
        UUID appointmentId

) {

}
