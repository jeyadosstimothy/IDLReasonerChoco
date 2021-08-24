package es.us.isa.idlreasonerchoco.analyzer.operations.oas;

import es.us.isa.idlreasonerchoco.mapper.OASMapper;
import es.us.isa.idlreasonerchoco.utils.ExceptionManager;
import es.us.isa.idlreasonerchoco.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.BoolVar;

import es.us.isa.idlreasonerchoco.configuration.ErrorType;
import es.us.isa.idlreasonerchoco.configuration.IDLException;

public class OASFalseOptional implements AnalysisOperation {

    private static final Logger LOG = LogManager.getLogger(OASFalseOptional.class);

    private final OASMapper mapper;
    private final String paramName;

    public OASFalseOptional(OASMapper mapper, String paramName) {
        this.mapper = mapper;
        this.paramName = paramName;
    }

    public boolean analyze() throws IDLException {
        restartSolverIfNeeded(mapper);
    	if(mapper.getParameters().stream().anyMatch(x -> x.getName().equals(paramName) && Boolean.TRUE.equals(x.getRequired()))) {
    		return false;
    	}
        if (mapper.getVariablesMap().get(Utils.parseIDLParamName(paramName) + "Set") != null) {
            boolean consistent = new OASConsistent(mapper).analyze();
            mapper.getChocoModel().getSolver().reset();
            BoolVar varSet = mapper.getVariablesMap().get(Utils.parseIDLParamName(paramName) + "Set").asBoolVar();
            Constraint cons = mapper.getChocoModel().arithm(varSet, "=", 0);
            cons.post();
            boolean result = consistent && !mapper.getChocoModel().getSolver().solve();
            mapper.getChocoModel().unpost(cons);
            mapper.getChocoModel().getSolver().reset();
            return result;
        } else {
            ExceptionManager.rethrow(LOG, ErrorType.ERROR_OPERATION_PARAM.toString());
            return false;
        }
    }
}
