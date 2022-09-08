package vn.trinhtung.service;

import java.util.Map;

import vn.trinhtung.dto.EmailForgotPasswordDto;

public interface EmailService {
	void sendMessageHtml(String to, String subject, String template, Map<String, Object> attributes);
	
	void sendEmailForgotPassword(EmailForgotPasswordDto dto);
}
