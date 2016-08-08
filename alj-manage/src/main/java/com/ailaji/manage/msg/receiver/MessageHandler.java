package com.ailaji.manage.msg.receiver;

public interface MessageHandler {
	void start();
	void stop();
	void shutdown();
}
