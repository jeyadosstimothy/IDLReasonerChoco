package es.us.isa.idlreasonerchoco.analyzer.operations.oas;

import es.us.isa.idlreasonerchoco.configuration.IDLException;

import java.util.List;
import java.util.Map;


public interface Explanation extends GenericOperation{

	Map<String, Map<String, List<String>>> getExplanation() throws IDLException;
}
