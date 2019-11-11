package cn.enjoyedu.queue;

import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @author Mark老师   享学课堂 https://enjoy.ke.qq.com
 * 往期课程咨询芊芊老师  QQ：2130753077 VIP课程咨询 依娜老师  QQ：2133576719
 * 类说明：
 */
@Component
public class GetResponse implements MessageListener {
    public void onMessage(Message message) {
        String textMsg = null;
        try {
            textMsg = ((TextMessage) message).getText();
            System.out.println("GetResponse accept msg : " + textMsg);
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
