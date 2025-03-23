package es.us.isa.idlreasonerchoco.analyzer.operations.oas;

import es.us.isa.idlreasonerchoco.configuration.ErrorType;
import es.us.isa.idlreasonerchoco.configuration.IDLException;
import es.us.isa.idlreasonerchoco.mapper.OASMapper;
import es.us.isa.idlreasonerchoco.model.ParameterType;
import es.us.isa.idlreasonerchoco.utils.ExceptionManager;
import es.us.isa.idlreasonerchoco.utils.Utils;
import io.swagger.v3.oas.models.parameters.Parameter;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

public class OASExplanation implements Explanation {

  private static final Logger LOG = LogManager.getLogger(OASExplanation.class);

  private final OASMapper mapper;

  private final Map<String, String> request;
  private final boolean partial;
  private List<Constraint> requestConstraints;

  private Map<String, Map<String, List<String>>> explanation;

  public OASExplanation(OASMapper mapper, Map<String, String> request, boolean partial) {

    this.mapper = mapper;
    this.request = request;
    this.partial = partial;
    this.requestConstraints = new ArrayList<>();
    this.explanation = new HashMap<>();
  }

  public Map<String, Map<String, List<String>>> getExplanation() throws IDLException {

    try {

      if (this.request != null) return getRequestExplanation();
      else return getOperationExplanation();

    } catch (IDLException e) {
      ExceptionManager.log(LOG, ErrorType.ERROR_VALIDATING_REQUEST.toString(), e);
      Map<String, List<String>> err = new HashMap<>();
      err.put("ErrorMessage", Collections.singletonList(e.getMessage()));
      this.explanation.put("Error", err);

      return this.explanation;
    }
  }

  public String getExplanationMessage() throws IDLException {

    Map<String, Map<String, List<String>>> explanation = this.getExplanation();

    return getMessage(explanation);
  }

  private Map<String, Map<String, List<String>>> getRequestExplanation() throws IDLException {

    // If the request is valid return null for explanation
    OASValidRequest oasValidRequest = new OASValidRequest(mapper, request, partial);
    boolean result = oasValidRequest.analyze();

    if (result) {
      this.explanation.put("Explanation", null);
      return this.explanation;
    }

    this.explanation.put("InvalidRequestParams", getInvalidRequestExplanation());
    this.explanation.put("IDLConflicts", getIDLConflictsExplanation());

    unpostRequestConstraints();

    return this.explanation;
  }

  private Map<String, Map<String, List<String>>> getOperationExplanation() throws IDLException {

    // If the operation is valid return null for explanation
    OASValidIDL oasValidIDL = new OASValidIDL(mapper);
    boolean result = oasValidIDL.analyze();

    if (result) {
      this.explanation.put("Explanation", null);
      return this.explanation;
    }

    // If the operation has inconsistent IDL, return the explanation
    if (!mapper.getChocoModel().getSolver().solve()) {

      this.explanation.put("IDLConflicts", getIDLConflictsExplanation());

    } else {

      // If the operation has dead parameters, return the explanation
      this.explanation.put("DeadParameters", getDeadParameters());

      // If the operation has false optional parameters, return the explanation
      this.explanation.put("FalseOptionalParameters", getFalseOptionalParameters());
    }

    return this.explanation;
  }

  private Map<String, List<String>> getIDLConflictsExplanation() {

    Map<String, List<String>> conflictsIDLExplanation = new HashMap<>();

    // get All constraints
    List<Constraint> allCstrs = getChocoModelConstraints();

    // remove request constraints from all constraints
    List<Constraint> cstrs = new ArrayList<>(allCstrs);
    if (request != null) cstrs.removeAll(requestConstraints);

    // Find minimum conflicts
    List<Constraint> minConflicts = getMinimumConflictingConstraints(cstrs);

    // get arithmConstraints conflicts
    List<String> idlArithmConstraints = getArithmConstraints(cstrs);

    // Find IDL Conflicts
    List<String> idlConflicts = getIDLConflicts(minConflicts, idlArithmConstraints);

    conflictsIDLExplanation.put("IDLConflicts", idlConflicts);

    return conflictsIDLExplanation;
  }

  private Map<String, List<String>> getFalseOptionalParameters() throws IDLException {

    Map<String, List<String>> falseOptionalParameters = new HashMap<>();

    for (Parameter p : mapper.getParameters()) {

      if (!mapper.getParameters().stream()
          .anyMatch(x -> x.getName().equals(p.getName()) && Boolean.TRUE.equals(x.getRequired()))) {

        if (mapper.getVariablesMap().get(Utils.parseIDLParamName(p.getName()) + "Set") != null) {

          mapper.getChocoModel().getSolver().reset();
          BoolVar varSet =
              mapper
                  .getVariablesMap()
                  .get(Utils.parseIDLParamName(p.getName()) + "Set")
                  .asBoolVar();
          Constraint cons = mapper.getChocoModel().arithm(varSet, "=", 0);

          cons.post();

          mapper.getChocoModel().getSolver().reset();
          boolean result = !mapper.getChocoModel().getSolver().solve();

          if (result) {

            List<Constraint> cstrs = getChocoModelConstraints();
            List<String> arithmeticConstraints = getArithmConstraints(cstrs);
            List<Constraint> minConflicts = getMinimumConflictingConstraints(cstrs);
            List<String> idlConflicts = getIDLConflicts(minConflicts, arithmeticConstraints);

            falseOptionalParameters.put(p.getName(), idlConflicts);
          }
          mapper.getChocoModel().unpost(cons);
          mapper.getChocoModel().getSolver().reset();
        }
      }
    }

    return falseOptionalParameters;
  }

  private Map<String, List<String>> getDeadParameters() throws IDLException {

    Map<String, List<String>> deadParameters = new HashMap<>();

    for (Parameter p : mapper.getParameters()) {

      if (mapper.getVariablesMap().get(Utils.parseIDLParamName(p.getName()) + "Set") != null) {
        BoolVar varSet =
            mapper.getVariablesMap().get(Utils.parseIDLParamName(p.getName()) + "Set").asBoolVar();
        Constraint cons = mapper.getChocoModel().arithm(varSet, "=", 1);
        cons.post();
        mapper.getChocoModel().getSolver().reset();
        boolean result = !mapper.getChocoModel().getSolver().solve();

        if (result) {
          List<Constraint> cstrs = getChocoModelConstraints();
          List<String> arithmeticConstraints = getArithmConstraints(cstrs);
          List<Constraint> minConflicts = getMinimumConflictingConstraints(cstrs);
          List<String> idlConflicts = getIDLConflicts(minConflicts, arithmeticConstraints);

          deadParameters.put(p.getName(), idlConflicts);
        }

        mapper.getChocoModel().unpost(cons);
        mapper.getChocoModel().getSolver().reset();
      }
    }
    return deadParameters;
  }

  private Map<String, List<String>> getInvalidRequestParams(List<Constraint> minConflicts)
      throws IDLException {

    // get list of maps <paramName, paramValue>
    List<Map<String, String>> requestParams = getRequestParams();

    Map<String, List<String>> invalidRequestParams = new HashMap<>();

    for (int i = 0; i < requestConstraints.size(); i++) {
      Constraint c = requestConstraints.get(i);
      if (minConflicts.contains(c)) {
        Map<String, String> param = requestParams.get(i);
        String paramName = param.keySet().iterator().next();
        String paramValue = param.get(paramName);
        invalidRequestParams.put(paramName, Collections.singletonList(paramValue));
      }
    }
    return invalidRequestParams;
  }

  private List<Map<String, String>> getRequestParams() throws IDLException {
    List<Map<String, String>> requestParams = new ArrayList<>();

    try {
      if (request != null
          && request.keySet().stream()
              .allMatch(
                  param -> mapper.getVariablesMap().containsKey(Utils.parseIDLParamName(param)))) {

        for (Parameter parameter : mapper.getParameters()) {
          if (request.containsKey(parameter.getName())) {

            Map<String, String> paramVal = new HashMap<>();
            paramVal.put(parameter.getName(), request.get(parameter.getName()));
            requestParams.add(paramVal);

          } else if (!partial) {
            Map<String, String> paramVal = new HashMap<>();
            paramVal.put(parameter.getName(), request.get(parameter.getName()));
            requestParams.add(paramVal);
          }
        }
        return requestParams;

      } else {
        throw new IDLException(ErrorType.ERROR_UNKNOWN_PARAM_IN_REQUEST.toString());
      }
    } catch (Exception e) {
      throw new IDLException(ErrorType.ERROR_UNKNOWN_PARAM_IN_REQUEST.toString());
    }
  }

  private Map<String, List<String>> getInvalidRequestExplanation() throws IDLException {

    Map<String, List<String>> invalidRequestParams = new HashMap<>();

    if (request != null
        && request.keySet().stream()
            .allMatch(
                param -> mapper.getVariablesMap().containsKey(Utils.parseIDLParamName(param)))) {

      mapper.getChocoModel().getSolver().reset();

      for (Parameter parameter : mapper.getParameters()) {
        BoolVar varSet =
            mapper
                .getVariablesMap()
                .get(Utils.parseIDLParamName(parameter.getName()) + "Set")
                .asBoolVar();
        if (request.containsKey(parameter.getName())) {
          IntVar paramVar =
              mapper.getVariablesMap().get(Utils.parseIDLParamName(parameter.getName())).asIntVar();
          Constraint con =
              mapper
                  .getChocoModel()
                  .and(
                      mapper.getChocoModel().arithm(varSet, "=", 1),
                      mapper
                          .getChocoModel()
                          .arithm(
                              paramVar,
                              "=",
                              mapValueToConstraint(
                                  request.get(parameter.getName()),
                                  parameter.getSchema().getType())));

          requestConstraints.add(con);
          con.post();

        } else if (!partial) {

          Constraint setCon = mapper.getChocoModel().arithm(varSet, "=", 0);
          requestConstraints.add(setCon);
          setCon.post();
        }
      }

      mapper.getChocoModel().getSolver().reset();
      boolean result = !mapper.getChocoModel().getSolver().solve();

      if (result) {

        List<Constraint> cstrs = getChocoModelConstraints();
        List<Constraint> minConflicts = getMinimumConflictingConstraints(cstrs);
        invalidRequestParams = getInvalidRequestParams(minConflicts);
      }
      return invalidRequestParams;
    } else {
      throw new IDLException(ErrorType.ERROR_UNKNOWN_PARAM_IN_REQUEST.toString());
    }
  }

  private List<Constraint> getChocoModelConstraints() {
    return Arrays.asList(mapper.getChocoModel().getCstrs());
  }

  private List<Constraint> getMinimumConflictingConstraints(List<Constraint> cstrs) {

    mapper.getChocoModel().getSolver().reset();
    return mapper.getChocoModel().getSolver().findMinimumConflictingSet(cstrs);
  }

  private List<String> getIDLConflicts(
      List<Constraint> minConflicts, List<String> arithmeticConstraints) {

    List<String> idlConflicts = new ArrayList<>();

    String idl = mapper.getIdlFromOas();

    List<String> idlList = new ArrayList<>(Arrays.asList(idl.split("\n")));

    for (Constraint conflictingConstraint : minConflicts) {
      if (arithmeticConstraints.contains(conflictingConstraint.toString())) {
        int index = arithmeticConstraints.indexOf(conflictingConstraint.toString());
        idlConflicts.add(idlList.get(index));
      }
    }

    return idlConflicts;
  }

  private List<String> getArithmConstraints(List<Constraint> cstrs) {
    List<String> arithmConstraints = new ArrayList<>();
    for (Constraint cstr : cstrs) {
      String s = cstr.toString();
      if (s.substring(0, 6).equals("ARITHM") && (!s.contains("Set = 1") && !s.contains("Set = 0")))
        arithmConstraints.add(s);
    }
    return arithmConstraints;
  }

  private void unpostRequestConstraints() {
    for (Constraint cons : requestConstraints) {
      mapper.getChocoModel().unpost(cons);
    }

    mapper.getChocoModel().getSolver().reset();
  }

  private Integer mapValueToConstraint(String paramValue, String type) throws IDLException {
    try {
      switch (ParameterType.valueOf(type.toUpperCase())) {
        case STRING:
        case ARRAY:
          return mapper.getSolver().stringToInt(paramValue);
        case NUMBER:
        case INTEGER:
          return Integer.valueOf(paramValue);
        case BOOLEAN:
          if (Boolean.toString(true).equals(paramValue)) {
            return 1;
          } else if (Boolean.toString(false).equals(paramValue)) {
            return 0;
          }
        default:
          throw new IDLException(
              ErrorType.ERROR_IN_PARAMETER_TYPE.toString()
                  + " -> type: "
                  + type
                  + ", value: "
                  + paramValue);
      }

    } catch (IDLException e) {
      ExceptionManager.rethrow(LOG, ErrorType.ERROR_MAPPING_TO_CONSTRAINT.toString(), e);
      return null;
    }
  }

  private String getMessage(Map<String, Map<String, List<String>>> explanation)
      throws IDLException {

    // create a string with the explanation message

    StringBuilder explanationMessage = new StringBuilder();
    String title = "";
    String description = "";

    if (explanation.containsKey("Explanation")) {
      if (explanation.get("Explanation") == null) {
        title = "Valid Request/Operation";
        description = "There is no error in the request/OAS operation";
        explanationMessage.append(buildErrorMessage(title, description));
        return explanationMessage.toString();
      }
    }

    if (explanation.containsKey("InvalidRequestParams")) {
      Map<String, List<String>> invalidRequestParams = explanation.get("InvalidRequestParams");

      if (invalidRequestParams != null && (!invalidRequestParams.isEmpty())) {
        title = "Invalid Request Parameters";
        description = invalidRequestParams.toString();
        explanationMessage.append(buildErrorMessage(title, description));
      }
    }

    if (explanation.containsKey("DeadParameters")) {
      Map<String, List<String>> deadParameters = explanation.get("DeadParameters");

      StringBuilder deadParamsDescription = new StringBuilder();

      if (deadParameters != null && (!deadParameters.isEmpty())) {
        title = "Dead Parameters";

        for (String key : deadParameters.keySet()) {
          deadParamsDescription.append(
              "- "
                  + key
                  + " is dead, check constraints "
                  + deadParameters.get(key).toString()
                  + ".\n");
        }
        description = deadParamsDescription.toString();
        explanationMessage.append(buildErrorMessage(title, description));
      }
    }

    if (explanation.containsKey("FalseOptionalParameters")) {
      Map<String, List<String>> falseOptionalParameters =
          explanation.get("FalseOptionalParameters");
      StringBuilder falseOptionalParamsDescription = new StringBuilder();
      if (falseOptionalParameters != null && (!falseOptionalParameters.isEmpty())) {
        title = "False Optional Parameters";

        for (String key : falseOptionalParameters.keySet()) {
          falseOptionalParamsDescription.append(
              "- "
                  + key
                  + " is false optional, check constraints "
                  + falseOptionalParameters.get(key).toString()
                  + ".\n");
        }
        description = falseOptionalParamsDescription.toString();
        explanationMessage.append(buildErrorMessage(title, description));
      }
    }

    if (explanation.containsKey("IDLConflicts")) {
      Map<String, List<String>> idlConflicts = explanation.get("IDLConflicts");
      StringBuilder idlConflictsDescription = new StringBuilder();

      if (idlConflicts != null && (!idlConflicts.get("IDLConflicts").isEmpty())) {
        title = "Inconsistent IDL";

        for (String key : idlConflicts.keySet()) {
          idlConflictsDescription.append(
              "- Check constraints " + idlConflicts.get(key).toString() + ".\n");
        }
        description = idlConflictsDescription.toString();
        explanationMessage.append(buildErrorMessage(title, description));
      }
    }

    if (explanation.containsKey("Error")) {
      Map<String, List<String>> error = explanation.get("Error");

      if (error != null && (!error.get("ErrorMessage").isEmpty())) {
        title = "Error";
        description = error.get("ErrorMessage").toString();
        explanationMessage.append(buildErrorMessage(title, description));
      }
    }

    return explanationMessage.toString();
  }

  private String buildErrorMessage(String title, String description) {
    StringBuilder errorMessage = new StringBuilder();

    errorMessage.append("Title: ").append(title).append("\n");
    errorMessage.append("Details:\n");
    errorMessage.append(description);
    errorMessage.append("\n");
    return errorMessage.toString();
  }
}
