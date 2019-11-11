package cn.enjoyedu.producerconfirm;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *@author Mark老师   享学课堂 https://enjoy.ke.qq.com
 *往期视频咨询芊芊老师  QQ：2130753077  VIP课程咨询 依娜老师  QQ：2470523467
 *类说明：存放到延迟队列的元素，对业务数据进行了包装
 */
public class ProducerConfirmAsync {

    private final static String EXCHANGE_NAME = "producer_confirm";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        /**
         * 创建连接连接到MabbitMQ
         */
        ConnectionFactory factory = new ConnectionFactory();

        // 设置MabbitMQ所在主机ip或者主机名
        factory.setHost("127.0.0.1");
        // 创建一个连接
        Connection connection = factory.newConnection();
        // 创建一个信道
        Channel channel = connection.createChannel();
        // 指定转发
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        connection.addShutdownListener(new ShutdownListener() {
            public void shutdownCompleted(ShutdownSignalException cause) {
                System.out.println("connect is shutdown: "+cause.getMessage()
                        +System.currentTimeMillis());
            }
        });

        channel.addShutdownListener(new ShutdownListener() {
            public void shutdownCompleted(ShutdownSignalException cause) {
                System.out.println("channel is shutdown: "+cause.getMessage()
                        +System.currentTimeMillis());
            }
        });

        // 启用发送者确认模式
        channel.confirmSelect();

        /*服务端确认信息：
        1、投递的标识：Delivery Tags
        当一个消费者向RabbitMQ注册后，RabbitMQ会用 basic.deliver 方法向消费者推送消息，
        这个方法携带了一个 delivery tag， 它在一个channel中唯一代表了一次投递。
        delivery tag的唯一标识范围限于channel.
        delivery tag是单调递增的正整数，客户端获取投递的方法用用dellivery tag作为一个参数。
        2、multiple：是否批量.true:将一次性ack所有小于deliveryTag的消息，
        SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());
        * */
        channel.addConfirmListener(new ConfirmListener() {
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("Ack: deliveryTag = "+deliveryTag
                        +" multiple: "+multiple);
                /*if (multiple) {
                    confirmSet.headSet(deliveryTag + 1).clear();
                } else {
                    confirmSet.remove(deliveryTag);
                }*/
            }

            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("Nack: deliveryTag = "+deliveryTag
                        +" multiple: "+multiple);
            }
        });


        /*
        服务端返回信息:
        1、发送消息时当mandatory标志位设置为true时，
        如果exchange根据自身类型和消息routingKey无法找到一个合适的queue存储消息，
        那么broker会调用basic.return方法将消息返还给生产者;
        2、当mandatory设置为false时，
        出现上述情况broker会直接将消息丢弃;
        总结，
        mandatory标志告诉broker代理服务器至少将消息route到一个队列中，
        否则就将消息return给发送者;
        mandatory缺省为FALSE
        * */
        channel.addReturnListener(new ReturnListener() {
            public void handleReturn(int replyCode, String replyText,
                                     String exchange, String routingKey,
                                     AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body);
                System.out.println("RabbitMq返回的replyText:  "+replyText);
                System.out.println("RabbitMq返回的exchange:  "+exchange);
                System.out.println("RabbitMq返回的routingKey:  "+routingKey);
                System.out.println("RabbitMq返回的message:  "+message);
            }
        });

        String[] severities={"error","info","warning"};
        for(int i=0;i<3;i++){
            String severity = severities[i%3];
            // 发送的消息
            String message = "Hello World_"+(i+1)+("_"+System.currentTimeMillis());
            channel.basicPublish(EXCHANGE_NAME, severity, true,
                    null, message.getBytes());
            System.out.println("----------------------------------------------------");
            System.out.println(" Sent Message: [" + severity +"]:'"+ message + "'");
            Thread.sleep(200);
        }

        // 关闭频道和连接
        channel.close();
        connection.close();
    }


}
