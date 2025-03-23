package es.us.isa.idlreasonerchoco.analyzer.operations.oas;

import es.us.isa.idlreasonerchoco.configuration.ErrorType;
import es.us.isa.idlreasonerchoco.configuration.IDLException;
import es.us.isa.idlreasonerchoco.mapper.OASMapper;
import es.us.isa.idlreasonerchoco.model.ParameterType;
import es.us.isa.idlreasonerchoco.utils.ExceptionManager;
import es.us.isa.idlreasonerchoco.utils.Utils;
import io.swagger.v3.oas.models.parameters.Parameter;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.chocosolver.solver.search.limits.TimeCounter;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

public class OASRandomRequest implements RequestGenerationOperation {

  private static final Logger LOG = LogManager.getLogger(OASRandomRequest.class);
  private static final long TIMEOUT = 1000000000L; // Timeout to generate request (1s)

  private final OASMapper mapper;
  private final boolean valid;

  public OASRandomRequest(OASMapper mapper, boolean valid) {
    this.mapper = mapper;
    this.valid = valid;
  }

  public Map<String, String> generate() throws IDLException {
    boolean restarted = restartSolverIfNeeded(mapper);
    Map<String, String> request = null;
    if (valid
        || mapper.hasDeps()) { // If there are no deps, it's impossible to generate invalid request
      if (!restarted) mapper.getChocoModel().getSolver().reset();
      request = mapRequest();
    }
    return request;
  }

  private Map<String, String> mapRequest() throws IDLException {
    mapper
        .getChocoModel()
        .getSolver()
        .addStopCriterion(new TimeCounter(mapper.getChocoModel(), TIMEOUT));
    while (!mapper.getChocoModel().getSolver().solve()) {
      mapper.getChocoModel().getSolver().reset();
      mapper
          .getChocoModel()
          .getSolver()
          .addStopCriterion(new TimeCounter(mapper.getChocoModel(), TIMEOUT));
    }
    Map<String, String> request = new HashMap<>();
    for (Parameter parameter : mapper.getParameters()) {
      BoolVar varSet =
          mapper
              .getVariablesMap()
              .get(Utils.parseIDLParamName(parameter.getName()) + "Set")
              .asBoolVar();
      if (varSet.getValue() == 1) {
        IntVar paramVar =
            mapper.getVariablesMap().get(Utils.parseIDLParamName(parameter.getName())).asIntVar();
        request.put(
            parameter.getName(),
            mapConstraintToValue(paramVar.getValue(), parameter.getSchema().getType()));
      }
    }
    return request;
  }

  private String mapConstraintToValue(Integer intValue, String type) throws IDLException {
    switch (ParameterType.valueOf(type.toUpperCase())) {
      case STRING:
      case ARRAY:
        return mapper.getStringToIntMap().inverse().get(intValue) != null
            ? mapper.getStringToIntMap().inverse().get(intValue)
            : "toString" + intValue;
      case NUMBER:
      case INTEGER:
        return intValue.toString();
      case BOOLEAN:
        return Boolean.toString(intValue == 1);
      default:
        ExceptionManager.rethrow(LOG, ErrorType.ERROR_IN_PARAMETER_TYPE.toString() + " :" + type);
        return null;
    }
  }

  public boolean isValid() {
    return valid;
  }
}
