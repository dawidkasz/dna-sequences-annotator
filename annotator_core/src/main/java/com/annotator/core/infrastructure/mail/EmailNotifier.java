package com.annotator.core.infrastructure.mail;

import com.annotator.core.domain.order.Order;
import com.annotator.core.domain.order.OrderNotifier;
import com.annotator.core.domain.order.OrderRepository;
import com.annotator.core.infrastructure.csv.AnnotationResultsCsv;
import com.annotator.core.infrastructure.csv.CsvBean;
import com.annotator.core.infrastructure.csv.CsvHandler;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@ConditionalOnProperty(prefix = "spring.notify.mail", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
@Slf4j
public class EmailNotifier implements OrderNotifier {
    private final JavaMailSender mailSender;
    private final CsvHandler csvHandler;
    private final OrderRepository orderRepository;
    @Value("${spring.mail.username}")
    private String senderAddress;
    @Value("${spring.notify.mail.receiver-mail}")
    private String receiverAddress;

    @Override
    public void notifyAbutOrderFinished(final Order order) {
        final List<CsvBean> csvResults = orderRepository.findOrderAnnotations(order.getOrderId()).stream()
                .map(AnnotationResultsCsv::from)
                .map(value -> (CsvBean) value)
                .toList();

        createMessage(order, csvResults).ifPresent(mailSender::send);
    }

    public Optional<MimeMessage> createMessage(final Order order, final List<CsvBean> resultsCsv) {
        final var message = mailSender.createMimeMessage();
        try {
            final var helper = new MimeMessageHelper(message, true);
            helper.setTo(receiverAddress);
            helper.setFrom(senderAddress);
            helper.setSubject("Annotacje dla zamowienia: %s".formatted(order.getOrderId().getId()));
            helper.setText("Zamowienie: %s \nIlosc variantow: %s \nAlgorytmy: %s".
                    formatted(order.getOrderId().getId(), resultsCsv.size(), order.getAlgorithms().toString()));

            csvHandler.convertToCsvStream(resultsCsv)
                    .ifPresent(data -> {
                        try {
                            helper.addAttachment("annotations.csv", data, "text/csv");
                        } catch (final MessagingException e) {
                            log.warn("Error when adding csv attachment", e);
                        }
                    });

        } catch (final MessagingException e) {
            log.warn("Error when creating mail message", e);
            return Optional.empty();
        }
        return Optional.of(message);
    }
}
