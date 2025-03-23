package es.us.isa.idlreasonerchoco.mapper;

import com.google.common.collect.HashBiMap;
import es.us.isa.idlreasonerchoco.configuration.ErrorType;
import es.us.isa.idlreasonerchoco.configuration.IDLConfiguration;
import es.us.isa.idlreasonerchoco.configuration.IDLException;
import es.us.isa.idlreasonerchoco.model.OperationType;
import es.us.isa.idlreasonerchoco.model.ParameterType;
import es.us.isa.idlreasonerchoco.solver.OASSolver;
import es.us.isa.idlreasonerchoco.utils.ExceptionManager;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.Variable;

public class OASMapper extends Mapper {
  private static final String BODY_EXTENSION = "_body";

  private static final Logger LOG = LogManager.getLogger(OASMapper.class);

  private static final String FORM_DATA = "formData";
  private static final String BODY = "body";
  private static final String OAS_SPECIFICATION_TYPE = "oas";
  private static final String X_DEPENDENCIES = "x-dependencies";
  private static final String NEW_LINE = "\n";
  private static final String APPLICATION_TYPE_FORM_URL_ENCODED =
      "application/x-www-form-urlencoded";
  private static final String APPLICATION_TYPE_JSON = "application/json";

  private String idlFromOas = null;
  private OpenAPI openApiSpecification;
  private Operation operation;
  private List<Parameter> parameters;
  private OASSolver solver;
  private Map<String, List<String>> data;

  public OASMapper(IDLConfiguration configuration, Map<String, List<String>> data)
      throws IDLException {
    super(configuration);

    if (!this.configuration.getSpecificationType().toLowerCase().equals(OAS_SPECIFICATION_TYPE)) {
      ExceptionManager.rethrow(LOG, ErrorType.BAD_SPECIFICATION.toString());
    }

    this.readOpenApiSpecification();
    this.generateIDLFromOAS();
    this.updateData(data);
  }

  public OASMapper(IDLConfiguration configuration) throws IDLException {
    this(configuration, null);
  }

  private void readOpenApiSpecification() throws IDLException {
    ParseOptions parseOptions = new ParseOptions();
    parseOptions.setResolve(true);
    parseOptions.setResolveFully(true);
    parseOptions.setResolveCombinators(true);
    if (this.configuration.isSpecAsString()) {
      this.openApiSpecification =
          new OpenAPIV3Parser()
              .readContents(this.configuration.getApiSpecification(), null, parseOptions)
              .getOpenAPI();
    } else {
      this.openApiSpecification =
          new OpenAPIV3Parser().read(this.configuration.getApiSpecification(), null, parseOptions);
    }
    this.operation =
        getOasOperation(
            this.configuration.getOperationPath(), this.configuration.getOperationType());
    this.parameters =
        this.operation.getParameters() != null ? this.operation.getParameters() : new ArrayList<>();

    if (this.operation.getRequestBody() == null) {
      return;
    }
    if (this.operation
        .getRequestBody()
        .getContent()
        .containsKey(APPLICATION_TYPE_FORM_URL_ENCODED)) {
      this.parameters.addAll(getFormDataParameters(this.operation));
    }
    if (this.operation.getRequestBody().getContent().containsKey(APPLICATION_TYPE_JSON)) {
      this.parameters.addAll(getJsonParameters(this.operation));
    }
  }

  @SuppressWarnings("unchecked")
  public void generateIDLFromOAS() {
    try {
      List<String> IDLdeps;
      if (configuration.getIdlPath() != null)
        IDLdeps = Files.lines(Paths.get(configuration.getIdlPath())).collect(Collectors.toList());
      else IDLdeps = (List<String>) operation.getExtensions().get(X_DEPENDENCIES);

      if (IDLdeps != null
          && !IDLdeps.isEmpty()
          && !IDLdeps.get(0).trim().equals("")) // At least one dep
      this.idlFromOas = String.join(NEW_LINE, IDLdeps);
      else this.idlFromOas = NEW_LINE;
    } catch (Exception e) {
      if (!(e
          instanceof
          NullPointerException)) // Happens when there are no extensions in the OAS operation
      ExceptionManager.log(LOG, ErrorType.ERROR_READING_DEPENDECIES.toString(), e);
    } finally {
      if (this.idlFromOas == null) this.idlFromOas = NEW_LINE;
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private Collection<Parameter> getFormDataParameters(Operation operation) {
    List<Parameter> formDataParameters = new ArrayList<>();
    Schema formDataBody;
    Map<String, Schema> formDataBodyProperties;

    try {
      formDataBody =
          operation
              .getRequestBody()
              .getContent()
              .get(APPLICATION_TYPE_FORM_URL_ENCODED)
              .getSchema();
      formDataBodyProperties = formDataBody.getProperties();
    } catch (NullPointerException e) {
      return formDataParameters;
    }

    if (formDataBodyProperties == null) {
      String body =
          this.configuration.getOperationPath().replace("/", "_").substring(1) + BODY_EXTENSION;
      formDataBody = this.openApiSpecification.getComponents().getSchemas().get(body);
      formDataBodyProperties = formDataBody.getProperties();
    }

    for (Map.Entry<String, Schema> property : formDataBodyProperties.entrySet()) {
      Parameter parameter =
          new Parameter()
              .name(property.getKey())
              .in(FORM_DATA)
              .required(
                  formDataBody.getRequired() != null
                      && formDataBody.getRequired().contains(property.getKey()));
      parameter.setSchema(new Schema().type(property.getValue().getType()));
      parameter.getSchema().setEnum(property.getValue().getEnum());
      formDataParameters.add(parameter);
    }

    return formDataParameters;
  }

  private Collection<Parameter> getJsonParametersForObjectProperty(
      String propertyName, Schema<?> propertySchema) {
    List<Parameter> parameters = new ArrayList<>();
    for (Map.Entry<String, Schema> property : propertySchema.getProperties().entrySet()) {
      String childPropertyName = propertyName + "." + property.getKey();
      if (property.getValue().getType().equals(ParameterType.OBJECT.toString())) {
        parameters.addAll(
            getJsonParametersForObjectProperty(childPropertyName, property.getValue()));
        continue;
      }
      Parameter parameter =
          new Parameter()
              .name(childPropertyName)
              .in(BODY)
              .required(
                  propertySchema.getRequired() != null
                      && propertySchema.getRequired().contains(property.getKey()));
      parameter.setSchema(new Schema().type(property.getValue().getType()));
      parameter.getSchema().setEnum(property.getValue().getEnum());
      parameters.add(parameter);
    }
    return parameters;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private Collection<Parameter> getJsonParameters(Operation operation) {
    List<Parameter> jsonParameters = new ArrayList<>();
    Schema jsonBody;
    Map<String, Schema> jsonBodyProperties;

    try {
      jsonBody = operation.getRequestBody().getContent().get(APPLICATION_TYPE_JSON).getSchema();
      jsonBodyProperties = jsonBody.getProperties();
    } catch (NullPointerException e) {
      return jsonParameters;
    }

    if (jsonBodyProperties == null) {
      String body =
          this.configuration.getOperationPath().replace("/", "_").substring(1) + BODY_EXTENSION;
      jsonBody = this.openApiSpecification.getComponents().getSchemas().get(body);
      jsonBodyProperties = jsonBody.getProperties();
    }

    for (Map.Entry<String, Schema> property : jsonBodyProperties.entrySet()) {
      if (property.getValue().getType().equals(ParameterType.OBJECT.toString())) {
        jsonParameters.addAll(
            getJsonParametersForObjectProperty(property.getKey(), property.getValue()));
        continue;
      }
      Parameter parameter =
          new Parameter()
              .name(property.getKey())
              .in(BODY)
              .required(
                  jsonBody.getRequired() != null
                      && jsonBody.getRequired().contains(property.getKey()));
      parameter.setSchema(new Schema().type(property.getValue().getType()));
      parameter.getSchema().setEnum(property.getValue().getEnum());
      jsonParameters.add(parameter);
    }

    return jsonParameters;
  }

  private Operation getOasOperation(String operationPath, String operationType)
      throws IDLException {
    PathItem item = this.openApiSpecification.getPaths().get(operationPath);
    if (item != null) {
      try {
        switch (OperationType.valueOf(operationType.toUpperCase())) {
          case GET:
            return item.getGet();
          case DELETE:
            return item.getDelete();
          case HEAD:
            return item.getHead();
          case OPTIONS:
            return item.getOptions();
          case PATCH:
            return item.getPatch();
          case POST:
            return item.getPost();
          case PUT:
            return item.getPut();
          default:
            ExceptionManager.rethrow(LOG, ErrorType.BAD_OAS_OPERATION.toString());
            return null;
        }
      } catch (IllegalArgumentException e) {
        ExceptionManager.rethrow(LOG, ErrorType.BAD_OAS_OPERATION.toString());
        return null;
      }
    } else {
      ExceptionManager.rethrow(LOG, ErrorType.ERROR_OPERATION_PATH.toString());
      return null;
    }
  }

  public String getIdlFromOas() {
    return idlFromOas;
  }

  public OpenAPI getOpenApiSpecification() {
    return openApiSpecification;
  }

  public Operation getOperation() {
    return operation;
  }

  public List<Parameter> getParameters() {
    return parameters;
  }

  public OASSolver getSolver() {
    return solver;
  }

  public void updateData(Map<String, List<String>> data) throws IDLException {
    this.data = data;
    restartSolver(true);
  }

  public void restartSolver(boolean valid) throws IDLException {
    this.solver =
        new OASSolver(data, parameters, configuration.getOperationPath(), idlFromOas, valid);
  }

  public Model getChocoModel() {
    return this.solver.getChocoModel();
  }

  public Map<String, Variable> getVariablesMap() {
    return this.solver.getVariablesMap();
  }

  public HashBiMap<String, Integer> getStringToIntMap() {
    return this.solver.getStringToIntMap();
  }

  public boolean hasDeps() {
    return !NEW_LINE.equals(idlFromOas);
  }
}
