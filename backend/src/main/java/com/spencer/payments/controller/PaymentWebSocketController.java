package com.spencer.payments.controller;

import com.spencer.payments.dto.response.PaymentResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class PaymentWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendPaymentUpdate(PaymentResponseDTO payment) {
        messagingTemplate.convertAndSend("/topic/payments", payment);
    }
}
