package es.us.isa.idlreasonerchoco.analyzer.operations.oas;

import es.us.isa.idlreasonerchoco.configuration.IDLException;

public interface AnalysisOperation extends GenericOperation {

  boolean analyze() throws IDLException;
}
