package br.com.rpizao.exceptions;

public class BusinessException extends RuntimeException {
	private static final long serialVersionUID = -529922365264181341L;

	public BusinessException(String message)
    {
       super(message);
    }
}
