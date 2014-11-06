/**
 * 
 */
package exceptions;

/**
 * Custom exception to be thrown in cases of errors related login/registration
 * 
 * @author mohan
 */
public class DuplicateEmailException extends Exception {

	private static final long serialVersionUID = 9160679014555957793L;

	/**
	 * Default Constructor
	 */
	public DuplicateEmailException() {
	}

	/**
	 * @param pMessage
	 */
	public DuplicateEmailException(String pMessage) {
		super(pMessage);
	}

	/**
	 * @param pCause
	 */
	public DuplicateEmailException(Throwable pCause) {
		super(pCause);
	}

	/**
	 * @param pMessage
	 * @param pCause
	 */
	public DuplicateEmailException(String pMessage, Throwable pCause) {
		super(pMessage, pCause);
	}

}
