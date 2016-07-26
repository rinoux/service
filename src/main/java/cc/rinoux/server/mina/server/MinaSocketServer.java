package cc.rinoux.server.mina.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import cc.rinoux.server.mina.coder.MyMessageDecoder;
import cc.rinoux.server.mina.coder.MyMessageEncoder;
import cc.rinoux.server.mina.handler.ServerSessionHandler;

/**
 * 
 * @Author Rinoux
 * @Orgnization www.5d.com
 * @Description 初始化mina服务的配置
 * @Datetime 2016年5月3日下午2:18:10
 */
public class MinaSocketServer {
	
	 public int port;
	 public int bufferSize;
	 public int idleTime;
	 /***
	  * 
	  * @param port  监听端口
	  * @param bufferSize 缓冲区大小
	  * @param idleTime  空闲提醒间隔（s）
	  */
	public MinaSocketServer(int port, int bufferSize, int idleTime) {
		this.port = port;
		this.bufferSize = bufferSize;
		this.idleTime = idleTime;
	}
	/**
	 * 初始化服务监听方法
	 * @throws IOException 
	 */
	public void startListen() throws IOException {
		IoAcceptor acceptor = new NioSocketAcceptor();
		/*** 获取IoAcceptor的配置*/
		IoSessionConfig sessionConfig = acceptor.getSessionConfig();
		/*** 获取IoAcceptor的过滤链*/
		DefaultIoFilterChainBuilder filterChain = acceptor.getFilterChain();
		//设置缓冲区大！
		sessionConfig.setReadBufferSize(bufferSize);
		//设置空闲类型和时间间！
		sessionConfig.setIdleTime(IdleStatus.BOTH_IDLE, idleTime);
		//设置日志过滤
		filterChain.addLast("logger", new LoggingFilter());
		
		//filterChain.addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory()));
		
        //多路协议编解码工！
        DemuxingProtocolCodecFactory demuxingFactory = new DemuxingProtocolCodecFactory();
        //为多路协议编解码工厂设置解码器BaseMessageDecoder()（实现MessageDecoder！
        demuxingFactory.addMessageDecoder(new MyMessageDecoder());
        demuxingFactory.addMessageEncoder(String.class, new MyMessageEncoder());
        
        filterChain.addLast("context", new ProtocolCodecFilter(demuxingFactory));
        
        
        acceptor.setHandler(new ServerSessionHandler());
        acceptor.bind(new InetSocketAddress(port));
        acceptor.bind();
	}
	

}
