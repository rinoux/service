package cc.rinoux.server.mina.coder;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/** 
* @author 杨锐
*         rinoux@foxmail.com
* @version 创建时间！2016！3！28！ 下午1:18:52 
* TODO
*/

public class MyMessageEncoder implements MessageEncoder<String> {
	
	
	private static final Charset CHARSET = Charset.forName("UTF-8");

	@Override
	public void encode(IoSession session,String message, ProtocolEncoderOutput out) throws Exception {
		System.out.println("ENCODER编码工具调用");
		IoBuffer buffer = IoBuffer.allocate(1024).setAutoExpand(true);
	    CharsetEncoder charsetEncoder = CHARSET.newEncoder();
		buffer.putString((String)message, charsetEncoder);
		out.write(message);
		buffer.flip();		
	}

}
