package me.jiangcai.demo.pay.bean;

import me.jiangcai.demo.pay.entity.DemoPayOrder;
import me.jiangcai.demo.project.MockPaymentEvent;
import me.jiangcai.payment.PayableOrder;
import me.jiangcai.payment.PaymentForm;
import me.jiangcai.payment.entity.PayOrder;
import me.jiangcai.payment.exception.SystemMaintainException;
import me.jiangcai.payment.service.PaymentGatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

/**
 * @author CJ
 */
@Service
public class DemoPaymentForm implements PaymentForm {

    @Autowired
    private PaymentGatewayService paymentGatewayService;

    @Override
    public PayOrder newPayOrder(PayableOrder order, Map<String, Object> additionalParameters) throws SystemMaintainException {
        PayOrder payOrder = new DemoPayOrder();
        payOrder.setPlatformId(UUID.randomUUID().toString());
        return payOrder;
    }

    @EventListener(MockPaymentEvent.class)
    public void event(MockPaymentEvent event) {
        // 我们很直接！
        final DemoPayOrder order = paymentGatewayService.getOrder(DemoPayOrder.class, event.getId());
        if (order == null)
            return;
        if (event.isSuccess())
            paymentGatewayService.paySuccess(order);
        else
            paymentGatewayService.payCancel(order);
    }
}
