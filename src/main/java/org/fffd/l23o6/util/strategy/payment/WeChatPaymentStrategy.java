package org.fffd.l23o6.util.strategy.payment;

import org.fffd.l23o6.pojo.enum_.OrderStatus;

public class WeChatPaymentStrategy extends PaymentStrategy{
    @Override
    public String PayOrder(int money, String id) throws Exception {
        return "    <form name=\"punchout_form\" method=\"post\" action=\"https://www.baidu.com\">\n" +
                "</form>\n" +
                "<script>document.forms[0].submit();</script>";
    }

    @Override
    public OrderStatus checkOrderStatus(String out_trade_no) throws Exception {
        return OrderStatus.PAID;
    }


    @Override
    public boolean refundOrder(int money, String id) throws Exception {
        return true;
    }

}
