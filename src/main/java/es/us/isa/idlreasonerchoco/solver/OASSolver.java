package es.us.isa.idlreasonerchoco.solver;

import com.google.common.collect.HashBiMap;
import com.google.inject.Injector;
import es.us.isa.idl.IDLStandaloneSetupGenerated;
import es.us.isa.idl.generator.IDLGenerator;
import es.us.isa.idl.generator.Response;
import es.us.isa.idlreasonerchoco.configuration.ErrorType;
import es.us.isa.idlreasonerchoco.configuration.IDLException;
import es.us.isa.idlreasonerchoco.model.ParameterType;
import es.us.isa.idlreasonerchoco.utils.ExceptionManager;
import es.us.isa.idlreasonerchoco.utils.Utils;
import io.swagger.v3.oas.models.parameters.Parameter;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.Variable;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResourceSet;

public class OASSolver extends Solver {
  private static final Logger LOG = LogManager.getLogger(OASSolver.class);

  private static final String DUMMY_URI = "dummy:/dummy.idl";
  private static final int MIN_INTEGER = -1000;
  private static final int MAX_INTEGER = 1000;
  private static final String EQUALS = "=";

  private HashBiMap<String, Integer> stringToIntMap;
  private Map<String, Variable> variablesMap;
  private boolean valid = true;

  public OASSolver(
      Map<String, List<String>> data,
      List<Parameter> parameters,
      String operationPath,
      String idl,
      boolean valid)
      throws IDLException {
    super(operationPath);
    this.valid = valid;
    this.variablesMap = new HashMap<>();
    this.stringToIntMap = HashBiMap.create();

    List<Constraint> requiredParameters = this.mapVariables(data, parameters);
    this.generateConstraintsFromIDL(idl, requiredParameters);
  }

  public OASSolver(
      Map<String, List<String>> data, List<Parameter> parameters, String operationPath, String idl)
      throws IDLException {
    this(data, parameters, operationPath, idl, true);
  }

  public Model getChocoModel() {
    return chocoModel;
  }

  public HashBiMap<String, Integer> getStringToIntMap() {
    return stringToIntMap;
  }

  public Map<String, Variable> getVariablesMap() {
    return variablesMap;
  }

  private List<Constraint> mapVariables(Map<String, List<String>> data, List<Parameter> parameters)
      throws IDLException {
    List<Constraint> requiredParameters = new ArrayList<>();
    for (Parameter parameter : parameters) {
      String paramType = parameter.getSchema().getType();
      List<?> paramEnum = parameter.getSchema().getEnum();
      IntVar varParamSet;
      boolean disabledParameter = false;

      boolean paramIsString =
          paramType.equals(ParameterType.STRING.toString())
              || paramType.equals(ParameterType.ARRAY.toString());
      boolean paramIsNumber =
          paramType.equals(ParameterType.INTEGER.toString())
              || paramType.equals(ParameterType.NUMBER.toString());

      if (data == null) { // When initializing the solver (without data)
        if (paramType.equals(ParameterType.BOOLEAN.toString())) {
          this.getVariable(Utils.parseIDLParamName(parameter.getName()), IntVar.class, true, 0, 1);
        } else if (paramEnum != null) {
          if (paramType.equals(ParameterType.STRING.toString())) {
            int[] domain =
                paramEnum.stream().mapToInt(x -> this.stringToInt(x.toString())).toArray();
            this.getVariable(
                Utils.parseIDLParamName(parameter.getName()), IntVar.class, true, domain);

          } else if (paramType.equals(ParameterType.INTEGER.toString())) {
            int[] domain =
                paramEnum.stream().mapToInt(x -> Integer.parseInt(x.toString())).toArray();
            this.getVariable(
                Utils.parseIDLParamName(parameter.getName()), IntVar.class, true, domain);

          } else {
            ExceptionManager.rethrow(
                LOG, ErrorType.ERROR_IN_PARAMETER_TYPE.toString() + " :" + paramType);
          }

        } else if (paramIsString) {
          this.getVariable(
              Utils.parseIDLParamName(parameter.getName()), IntVar.class, false, 0, MAX_INTEGER);
        } else if (paramIsNumber) {
          this.getVariable(
              Utils.parseIDLParamName(parameter.getName()),
              IntVar.class,
              false,
              getMinimumValue(parameter),
              getMaximumValue(parameter));
        } else {
          ExceptionManager.rethrow(
              LOG, ErrorType.ERROR_IN_PARAMETER_TYPE.toString() + " :" + paramType);
        }

      } else { // When providing data
        List<String> paramData = data.get(parameter.getName());

        if (paramData != null && !paramData.isEmpty()) {
          if (paramType.equals(ParameterType.BOOLEAN.toString())) {
            int[] domain = getBooleanDomain(paramData);
            if (domain.length > 0)
              this.getVariable(
                  Utils.parseIDLParamName(parameter.getName()),
                  IntVar.class,
                  true,
                  getBooleanDomain(paramData));
            else disabledParameter = true;
          } else if (paramIsString) {
            int[] domain =
                paramData.stream().mapToInt(x -> this.stringToInt(x.toString())).toArray();
            this.getVariable(
                Utils.parseIDLParamName(parameter.getName()), IntVar.class, true, domain);
          } else if (paramIsNumber) {
            int[] domain = getNumberDomain(paramData);
            this.getVariable(
                Utils.parseIDLParamName(parameter.getName()), IntVar.class, true, domain);

          } else {
            ExceptionManager.rethrow(
                LOG, ErrorType.ERROR_IN_PARAMETER_TYPE.toString() + " :" + paramType);
          }
        } else {
          disabledParameter = true;
        }
      }

      if (disabledParameter) {
        this.getVariable(Utils.parseIDLParamName(parameter.getName()), IntVar.class, true, 0);
        varParamSet =
            this.getVariable(
                    Utils.parseIDLParamName(parameter.getName()) + "Set", IntVar.class, true, 0)
                .asIntVar();
      } else {
        varParamSet =
            this.getVariable(
                    Utils.parseIDLParamName(parameter.getName()) + "Set", IntVar.class, true, 0, 1)
                .asIntVar();
      }

      if (Boolean.TRUE.equals(parameter.getRequired()))
        this.chocoModel.arithm(varParamSet, EQUALS, 1).post();
    }

    return requiredParameters;
  }

  private int[] getBooleanDomain(List<String> booleanData) {
    int containsFalse = booleanData.contains("false") ? 1 : 0;
    int containsTrue = booleanData.contains("true") ? 1 : 0;

    switch (containsFalse * 10 + containsTrue) {
      case 1:
        return new int[] {1};
      case 10:
        return new int[] {0};
      case 11:
        return new int[] {0, 1};
      default:
        return new int[] {};
    }
  }

  private int[] getNumberDomain(List<String> numberData) {
    return numberData.stream()
        .filter(
            x -> { // We need to filter out values that are not doubles or are too high
              try {
                return Double.parseDouble(x) < Integer.MAX_VALUE;
              } catch (NumberFormatException e) {
                return false;
              }
            })
        .mapToInt(x -> (int) Double.parseDouble(x))
        .toArray();
  }

  private int getMaximumValue(Parameter parameter) {
    int maximum =
        parameter.getSchema().getMaximum() != null
            ? parameter.getSchema().getMaximum().intValue()
            : MAX_INTEGER;
    return parameter.getSchema().getExclusiveMaximum() != null
            && parameter.getSchema().getExclusiveMaximum()
        ? maximum - 1
        : maximum;
  }

  private int getMinimumValue(Parameter parameter) {
    int minimum =
        parameter.getSchema().getMinimum() != null
            ? parameter.getSchema().getMinimum().intValue()
            : MIN_INTEGER;
    return parameter.getSchema().getExclusiveMinimum() != null
            && parameter.getSchema().getExclusiveMinimum()
        ? minimum + 1
        : minimum;
  }

  public Integer stringToInt(String stringValue) {
    Integer intMapping = stringToIntMap.get(stringValue);
    if (intMapping != null) {
      return intMapping;
    } else {
      int size = stringToIntMap.entrySet().size();
      stringToIntMap.put(stringValue, size);
      return size;
    }
  }

  private Variable getVariable(
      String name, Class<? extends Variable> type, boolean absoluteDomain, int... domain) {
    Variable paramVar = variablesMap.get(name);
    if (paramVar != null) {
      return paramVar;
    } else {
      if (type == BoolVar.class) {
        variablesMap.put(name, chocoModel.boolVar(name));
      } else if (type == IntVar.class) {
        if (absoluteDomain) {
          variablesMap.put(name, chocoModel.intVar(name, domain));
        } else if (domain.length <= 2) {
          variablesMap.put(
              name,
              chocoModel.intVar(
                  name,
                  domain.length >= 1 ? domain[0] : MIN_INTEGER,
                  domain.length == 2 ? domain[1] : MAX_INTEGER));
        }
      }
      return variablesMap.get(name);
    }
  }

  private void generateConstraintsFromIDL(String idl, List<Constraint> requiredParameters)
      throws IDLException {
    IDLGenerator idlGenerator = new IDLGenerator(stringToIntMap, variablesMap, chocoModel);
    Injector injector = new IDLStandaloneSetupGenerated().createInjectorAndDoEMFRegistration();
    XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
    Resource resource = resourceSet.createResource(URI.createURI(DUMMY_URI));
    InputStream in = new ByteArrayInputStream(idl.getBytes());

    try {
      resource.load(in, resourceSet.getLoadOptions());
      Response response = idlGenerator.doGenerateChocoModel(resource, valid, requiredParameters);
      this.stringToIntMap = HashBiMap.create(response.getStringToIntMap());
      this.chocoModel = response.getChocoModel();
      this.chocoModel.getSolver().setRestartOnSolutions();
      this.chocoModel
          .getSolver()
          .setSearch(
              Search.randomSearch(
                  variablesMap.values().stream().map(x -> x.asIntVar()).toArray(IntVar[]::new),
                  System.currentTimeMillis()));

    } catch (Exception e) {
      ExceptionManager.rethrow(LOG, ErrorType.ERROR_MAPPING_CONSTRAINTS_FROM_IDL.toString(), e);
    }
  }

  public boolean isValid() {
    return valid;
  }
}
