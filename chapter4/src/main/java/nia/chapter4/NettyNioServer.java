package nia.chapter4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * Listing 4.4 Asynchronous networking with Netty
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class NettyNioServer {
  public void server(int port) throws Exception {
    final ByteBuf buf =
        Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8")));
    NioEventLoopGroup group = new NioEventLoopGroup();
    try {
      ServerBootstrap b = new ServerBootstrap();
      b.group(group)
          .channel(NioServerSocketChannel.class)
          .localAddress(new InetSocketAddress(port))
          .childHandler(
              // 指定ChannelInitializer 对于每个已接受的连接都调用它
              new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                  ch.pipeline()
                      .addLast(
                          new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                              // 将消息，写到客户端，并添加 ChannelFutureListener ，以便消息一被写完就关闭连接
                              ctx.writeAndFlush(buf.duplicate())
                                  .addListener(ChannelFutureListener.CLOSE);
                            }
                          });
                }
              });
      ChannelFuture f = b.bind().sync();
      f.channel().closeFuture().sync();
    } finally {
      group.shutdownGracefully().sync();
    }
  }
}
