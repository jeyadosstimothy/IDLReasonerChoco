package es.us.isa.idlreasonerchoco.analyzer.operations.oas;

import es.us.isa.idlreasonerchoco.configuration.IDLException;
import es.us.isa.idlreasonerchoco.mapper.OASMapper;
import io.swagger.v3.oas.models.parameters.Parameter;

public class OASValidIDL implements AnalysisOperation {

  private final OASMapper mapper;

  public OASValidIDL(OASMapper mapper) {
    this.mapper = mapper;
  }

  public boolean analyze() throws IDLException {
    restartSolverIfNeeded(mapper);
    OASConsistent consistent = new OASConsistent(mapper);
    boolean result = consistent.analyze();
    for (Parameter p : mapper.getParameters()) {
      OASDeadParameter deadParameter = new OASDeadParameter(mapper, p.getName());
      OASFalseOptional falseOptional = new OASFalseOptional(mapper, p.getName());
      result &= !deadParameter.analyze() && !falseOptional.analyze();
      if (!result) {
        break;
      }
    }
    return result;
  }
}
