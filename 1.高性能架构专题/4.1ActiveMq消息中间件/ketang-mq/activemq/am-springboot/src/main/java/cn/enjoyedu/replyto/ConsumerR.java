package cn.enjoyedu.replyto;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

/**
 * @author Mark老师   享学课堂 https://enjoy.ke.qq.com
 * 往期课程咨询芊芊老师  QQ：2130753077 VIP课程咨询 依娜老师  QQ：2133576719
 * 类说明：
 */
@Component
public class ConsumerR {
    // 使用JmsListener配置消费者监听的队列，其中text是接收到的消息
    @JmsListener(destination = "springboot.replyto.queue")
    @SendTo("out.replyTo.queue")
    public String receiveQueue(String text) {
        System.out.println(this.getClass().getName()+" 收到的报文为:"+text);
        return "Hello,I watch you";
    }
}
