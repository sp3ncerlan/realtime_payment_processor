package com.spencer.payments.payment.controller;

import com.spencer.payments.payment.dto.response.PaymentResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class PaymentWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendPaymentUpdate(UUID customerId, PaymentResponseDTO payment) {
        String destination = "/topic/payments/" + customerId;
        messagingTemplate.convertAndSend(destination, payment);
    }
}
