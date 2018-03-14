package chatting.controller;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


public class EchoHandler extends TextWebSocketHandler{

	private static Logger logger = LoggerFactory.getLogger(EchoHandler.class);

	public static Object getAttrFromSession(String key) {
		ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
		return attr.getAttribute(key, ServletRequestAttributes.SCOPE_SESSION);
	}
	private List<WebSocketSession> sessionList = new ArrayList<WebSocketSession>();

	/**
	 * Ŭ���̾�Ʈ ���� ���Ŀ� ����Ǵ� �޼ҵ�
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session)
			throws Exception {
		sessionList.add(session);
		logger.info("{} �����", session.getLocalAddress());
	}

	/**
	 * Ŭ���̾�Ʈ�� �����ϼ����� �޽����� �������� �� ����Ǵ� �޼ҵ�
	 */
	@Override
	protected void handleTextMessage(WebSocketSession session,
			TextMessage message) throws Exception {
		logger.info("{}�� ���� {} ����", session.getLocalAddress(), message.getPayload());
		for(WebSocketSession sess : sessionList){
			sess.sendMessage(new TextMessage(message.getPayload()));
		}
	}

	/**
	 * Ŭ���̾�Ʈ�� ������ ������ �� ����Ǵ� �޼ҵ�
	 */
	@Override
	public void afterConnectionClosed(WebSocketSession session,
			CloseStatus status) throws Exception {
		sessionList.remove(session);
		logger.info("{} ���� ����", session.getLocalAddress());
	}
}