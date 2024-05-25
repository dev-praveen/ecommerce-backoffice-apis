package com.praveen.jpa.service;

import com.praveen.jpa.event.OrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MqListener {

  private final EmailService emailService;

  @RabbitListener(queues = {"orders_queue"})
  public void receiveOrder(OrderEvent orderEvent) {

    log.info("Order received: {} ", orderEvent);
    String content =
        """
                            ===================================================
                            Order Created Notification
                            ----------------------------------------------------
                            Dear %s,
                            Your order with orderNumber: %s has been created successfully.

                            Thanks,
                            Ecommerce team
                            ===================================================
                            """
            .formatted(orderEvent.customerName(), orderEvent.orderId());
    emailService.sendEmail("email.test@gmail.com", "Order confirmation", content);
  }
}
