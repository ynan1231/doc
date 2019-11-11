package cn.enjoyedu.ch02.serializable.msgpack;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.net.InetSocketAddress;

/**
 * 作者：Mark/Maoke
 * 创建日期：2018/08/25
 * 类说明：
 */
public class ServerMsgPackEcho {

    public static final int PORT = 9995;

    public static void main(String[] args) throws InterruptedException {
        ServerMsgPackEcho serverMsgPackEcho = new ServerMsgPackEcho();
        System.out.println("服务器即将启动");
        serverMsgPackEcho.start();
    }

    public void start() throws InterruptedException {
        final MsgPackServerHandler serverHandler = new MsgPackServerHandler();
        EventLoopGroup group = new NioEventLoopGroup();/*线程组*/
        try {
            ServerBootstrap b = new ServerBootstrap();/*服务端启动必须*/
            b.group(group)/*将线程组传入*/
                .channel(NioServerSocketChannel.class)/*指定使用NIO进行网络传输*/
                .localAddress(new InetSocketAddress(PORT))/*指定服务器监听端口*/
                /*服务端每接收到一个连接请求，就会新启一个socket通信，也就是channel，
                所以下面这段代码的作用就是为这个子channel增加handle*/
                .childHandler(new ChannelInitializerImp());
            ChannelFuture f = b.bind().sync();/*异步绑定到服务器，sync()会阻塞直到完成*/
            System.out.println("服务器启动完成，等待客户端的连接和数据.....");
            f.channel().closeFuture().sync();/*阻塞直到服务器的channel关闭*/
        } finally {
            group.shutdownGracefully().sync();/*优雅关闭线程组*/
        }
    }

    private static class ChannelInitializerImp extends ChannelInitializer<Channel> {

        @Override
        protected void initChannel(Channel ch) throws Exception {
        	/*
        	 *  lengthFieldOffset：指的是长度域的偏移量，表示跳过指定个数字节之后的才是长度域；
				lengthFieldLength：记录该帧数据长度的字段本身的长度；
				lengthAdjustment：长度的一个修正值，可正可负；
				initialBytesToStrip：从数据帧中跳过的字节数，表示得到一个完整的数据包之后，忽略多少字节，开始读取实际我要的数据
        	 */
            //根据消息长度，从中剥离出完整的实际数据
            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535,
                    0,2,0,2));
            //反序列化
            ch.pipeline().addLast(new MsgPackDecoder());
            //将反序列化后的实体类交给业务处理
            ch.pipeline().addLast(new MsgPackServerHandler());
        }
    }

}
