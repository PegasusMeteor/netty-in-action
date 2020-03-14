package nia.chapter2.echoclient;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * Listing 2.3 ChannelHandler for the client
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
@Sharable // 标记这个 注解地实例，可以被多个 Channel 共享
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
  /**
   * 连接建立时被调用，发送一条消息
   *
   * @param ctx
   */

  @Override
  public void channelActive(ChannelHandlerContext ctx) {
    ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
  }

  /**
   * 每次接收数据时都会调用这个方法
   * 服务器端发送的消息，有可能被分块接收
   * @param ctx
   * @param in
   */
  @Override
  public void channelRead0(ChannelHandlerContext ctx, ByteBuf in) {
    System.out.println("Client received: " + in.toString(CharsetUtil.UTF_8));
  }

  // 对异常的统一处理
  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    cause.printStackTrace();
    ctx.close();
  }
}
