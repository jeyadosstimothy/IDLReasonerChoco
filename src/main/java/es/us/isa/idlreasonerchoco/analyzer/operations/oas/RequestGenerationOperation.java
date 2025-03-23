package es.us.isa.idlreasonerchoco.analyzer.operations.oas;

import es.us.isa.idlreasonerchoco.configuration.IDLException;
import io.swagger.v3.oas.models.parameters.Parameter;
import java.util.Map;

public interface RequestGenerationOperation extends GenericOperation {

  Map<String, String> generate() throws IDLException;

  Map<Parameter, String> generateWithParameter() throws IDLException;
}
