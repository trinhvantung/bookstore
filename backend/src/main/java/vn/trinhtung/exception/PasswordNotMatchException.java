package vn.trinhtung.exception;

public class PasswordNotMatchException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer code;

	public PasswordNotMatchException(Integer code, String message) {
		super(message);
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
}
