package es.us.isa.idlreasonerchoco.utils;

import org.apache.logging.log4j.Logger;

import es.us.isa.idlreasonerchoco.configuration.IDLException;

public class ExceptionManager {

	private ExceptionManager() {
	}

	public static void rethrow(Logger logger, String error, Exception exception) throws IDLException {
		log(logger, error, exception);
		throw new IDLException(error, exception);
	}
	
	public static void rethrow(Logger logger, String error) throws IDLException {
		rethrow(logger, error, null);
	}
	
	public static void log(Logger logger, String error, Exception exception) {
		logger.error(error, exception);
	}
	
	public static void log(Logger logger, String error) {
		log(logger, error, null);
	}
}
