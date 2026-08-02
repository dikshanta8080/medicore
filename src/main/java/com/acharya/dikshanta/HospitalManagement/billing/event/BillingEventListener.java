package com.acharya.dikshanta.HospitalManagement.billing.event;

import com.acharya.dikshanta.HospitalManagement.appointment.event.AppointmentCompletedEvent;
import com.acharya.dikshanta.HospitalManagement.billing.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class BillingEventListener {
    private final BillingService billingService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleAppointmentCompleted(AppointmentCompletedEvent event){
        billingService.createInvoiceFromAppointment(event.appointment());
    }


}
