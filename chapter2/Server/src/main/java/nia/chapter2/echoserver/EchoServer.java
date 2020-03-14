package nia.chapter2.echoserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Listing 2.2 EchoServer class
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class EchoServer {
  private final int port;

  public EchoServer(int port) {
    this.port = port;
  }

  public static void main(String[] args) throws Exception {
    if (args.length != 1) {
      System.err.println("Usage: " + EchoServer.class.getSimpleName() + " <port>");
      return;
    }
    // 设置端口值
    int port = Integer.parseInt(args[0]);
    new EchoServer(port).start();
  }

  public void start() throws Exception {
    final EchoServerHandler serverHandler = new EchoServerHandler();
    //     创建EventLoopGroup
    EventLoopGroup group = new NioEventLoopGroup();
    try {
      ServerBootstrap b = new ServerBootstrap();
      b.group(group)
          // 指定 NIO Channel
          .channel(NioServerSocketChannel.class)
          // 绑定端口
          .localAddress(new InetSocketAddress(port))

          // 添加一个 EchoServerHandler 到子 Channel 的 Channel Pipeline
          .childHandler(
              new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                  // EchoServerHandler 被标记为 shareable，所以总是可以使用同样地实例
                  ch.pipeline().addLast(serverHandler);
                }
              });
      // 异步地绑定服务器，调用 sync 方法阻塞，知道绑定完成
      ChannelFuture f = b.bind().sync();
      System.out.println(
          EchoServer.class.getName()
              + " started and listening for connections on "
              + f.channel().localAddress());
      //      获取 channel 的closeFuture，并且阻塞当前线程，直到它完成
      f.channel().closeFuture().sync();
    } finally {
      //      EventLoopGroup 关闭，并释放所有资源
      group.shutdownGracefully().sync();
    }
  }
}
