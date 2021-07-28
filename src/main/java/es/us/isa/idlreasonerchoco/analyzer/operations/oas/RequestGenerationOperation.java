package es.us.isa.idlreasonerchoco.analyzer.operations.oas;

import java.util.Map;

import es.us.isa.idlreasonerchoco.configuration.IDLException;

public interface RequestGenerationOperation extends GenericOperation {

    Map<String, String> generate() throws IDLException;
}
