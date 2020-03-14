package nia.chapter1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * Created by kerr.
 *
 * <p>Listing 1.3 Asynchronous connect
 *
 * <p>Listing 1.4 Callback in action
 */
public class ConnectExample {
  private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();

  /**
   * Listing 1.3 Asynchronous connect
   *
   * <p>Listing 1.4 Callback in action
   */
  public static void connect() {
    Channel channel = CHANNEL_FROM_SOMEWHERE; // reference form somewhere
    // Does not block
    // 异步连接到远程节点
    ChannelFuture future = channel.connect(new InetSocketAddress("192.168.0.1", 25));

    // 注册一个 ChannelFutureListener 以便在完成操作时获得通知
    future.addListener(
        (ChannelFuture channelFuture) -> {
          //  如果成功了 创建一个 ByteBuf 以持有数据
          if (channelFuture.isSuccess()) {
            ByteBuf buffer = Unpooled.copiedBuffer("Hello", Charset.defaultCharset());
            // 将数据异步地发送到远程节点
            ChannelFuture wf = channelFuture.channel().writeAndFlush(buffer);
            // ...
          } else {
            Throwable cause = channelFuture.cause();
            cause.printStackTrace();
          }
        });
  }
}
