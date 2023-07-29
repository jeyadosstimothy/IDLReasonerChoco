package es.us.isa.idlreasonerchoco.analyzer.operations.oas;

import es.us.isa.idlreasonerchoco.configuration.ErrorType;
import es.us.isa.idlreasonerchoco.configuration.IDLException;
import es.us.isa.idlreasonerchoco.mapper.OASMapper;
import es.us.isa.idlreasonerchoco.utils.ExceptionManager;
import es.us.isa.idlreasonerchoco.utils.Utils;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.BoolVar;

import java.util.*;

public class OASOperationExplanation implements Explanation {

    private static final Logger LOG = LogManager.getLogger(OASOperationExplanation.class);
    private final OASMapper mapper;

    public OASOperationExplanation(OASMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Map<String, Map<String, List<String>>> getExplanation() throws IDLException {

        try {
            restartSolverIfNeeded(mapper);

            Map<String, Map<String, List<String>>> explanation = new HashMap<>();

            // If the operation is valid return null for explanation
            OASValidIDL oasValidIDL = new OASValidIDL(mapper);
            boolean result = oasValidIDL.analyze();

            if (result) {
                explanation.put("Explanation", null);
                return explanation;
            }

            restartSolverIfNeeded(mapper);

            // If the operation has inconsistent IDL, return the explanation
            if (!mapper.getChocoModel().getSolver().solve()) {
                Map<String, List<String>> inconsistentIDLExplanation = getInconsistentIDLExplanation();

                if (inconsistentIDLExplanation.isEmpty())
                    explanation.put("InconsistentIDLExplanation", null);
                else
                    explanation.put("InconsistentIDLExplanation", inconsistentIDLExplanation);

                return explanation;

            } else {
                // If the operation has dead or false optional parameters, return the explanation

                // If the operation has dead parameters, return the explanation
                Map<String, List<String>> deadParametersExplanation = getDeadParametersExplanation();

                if (deadParametersExplanation.isEmpty())
                    explanation.put("DeadParametersExplanation", null);
                else
                    explanation.put("DeadParametersExplanation", deadParametersExplanation);

                // If the operation has false optional parameters, return the explanation
                Map<String, List<String>> falseOptionalParametersExplanation = getFalseOptionalParametersExplanation();

                if (falseOptionalParametersExplanation.isEmpty())
                    explanation.put("FalseOptionalParametersExplanation", null);
                else
                    explanation.put("FalseOptionalParametersExplanation", falseOptionalParametersExplanation);

                return explanation;

            }
        } catch (IDLException e) {
            ExceptionManager.log(LOG, ErrorType.ERROR_VALIDATING_REQUEST.toString(), e);
            Map<String, Map<String, List<String>>> explanation = new HashMap<>();
            Map<String, List<String>> err = new HashMap<String, List<String>>();
            err.put("ErrorMessage", Collections.singletonList(e.getMessage()));
            explanation.put("Error", err);

            return explanation;
        }

    }

    private List<Constraint> getChocoModelConstraints() {
        return Arrays.asList(mapper.getChocoModel().getCstrs());
        }

    private Map<String, List<String>> getInconsistentIDLExplanation() {

        Map<String, List<String>> inconsistentIDL = new HashMap<>();

        List<Constraint> cstrs = getChocoModelConstraints();

        List<String> arithmeticConstraints = getArithmConstraints(cstrs);
        List<Constraint> minConflicts = getMinimumConflictingConstraints(cstrs);
        List<String> idlConflicts = getIDLConflicts(minConflicts, arithmeticConstraints);

        inconsistentIDL.put("IDLConflicts", idlConflicts);

        return inconsistentIDL;

    }

    private Map<String, List<String>> getFalseOptionalParametersExplanation() throws IDLException {

        Map<String, List<String>> falseOptionalParameters = new HashMap<>();

        for (Parameter p : mapper.getParameters()) {

            if (!mapper.getParameters().stream().anyMatch(x -> x.getName().equals(p.getName()) && Boolean.TRUE.equals(x.getRequired()))) {

                if (mapper.getVariablesMap().get(Utils.parseIDLParamName(p.getName()) + "Set") != null) {

                    mapper.getChocoModel().getSolver().reset();
                    BoolVar varSet = mapper.getVariablesMap().get(Utils.parseIDLParamName(p.getName()) + "Set").asBoolVar();
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

    private Map<String, List<String>> getDeadParametersExplanation() throws IDLException {

        Map<String, List<String>> deadParameters = new HashMap<>();

        for (Parameter p : mapper.getParameters()) {

            if (mapper.getVariablesMap().get(Utils.parseIDLParamName(p.getName()) + "Set") != null) {
                BoolVar varSet = mapper.getVariablesMap().get(Utils.parseIDLParamName(p.getName()) + "Set").asBoolVar();
                Constraint cons = mapper.getChocoModel().arithm(varSet, "=", 1);
                cons.post();
                mapper.getChocoModel().getSolver().reset();
                boolean result = !mapper.getChocoModel().getSolver().solve();

                if (result) {
                    List<Constraint> cstrs =  getChocoModelConstraints();
                    List<String> arithmeticConstraints = getArithmConstraints(cstrs);
                    List<Constraint> minConflicts = getMinimumConflictingConstraints(cstrs);
                    List<String> idlConflicts = getIDLConflicts(minConflicts, arithmeticConstraints);

                    deadParameters.put(p.getName(), idlConflicts);
                }

                mapper.getChocoModel().unpost(cons);
                mapper.getChocoModel().getSolver().reset();
            }

        }
        if (deadParameters.isEmpty())
            return null;
        else
            return deadParameters;
    }

    private List<String> getArithmConstraints(List<Constraint> cstrs) {

        List<String> arithmConstraints = new ArrayList<String>();

        for (Constraint cstr : cstrs) {
            String s = cstr.toString();
            if (s.substring(0, 6).equals("ARITHM")) {
                if (!s.contains("Set = 1") && !s.contains("Set = 0"))
                    arithmConstraints.add(s);
            }
        }
        return arithmConstraints;
    }

    private List<Constraint> getMinimumConflictingConstraints(List<Constraint> cstrs) {

        mapper.getChocoModel().getSolver().reset();
        return mapper.getChocoModel().getSolver()
                .findMinimumConflictingSet(cstrs);
    }

    private List<String> getIDLConflicts(List<Constraint> minConflicts, List<String> arithmeticConstraints) {

        List<String> idlConflicts = new ArrayList<>();

        String idl = mapper.getIdlFromOas();
        List<String> idlList = new ArrayList<String>(Arrays.asList(idl.split("\n")));

        for (Constraint conflictingConstraint : minConflicts) {
            if (arithmeticConstraints.contains(conflictingConstraint.toString())) {
                int index = arithmeticConstraints.indexOf(conflictingConstraint.toString());
                idlConflicts.add(idlList.get(index));
            }
        }
        return idlConflicts;
    }
}
