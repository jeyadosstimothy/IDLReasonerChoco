package es.us.isa.idlreasonerchoco.analyzer.operations.oas;

import es.us.isa.idlreasonerchoco.mapper.OASMapper;
import es.us.isa.idlreasonerchoco.configuration.IDLException;

public interface GenericOperation {
    default boolean restartSolverIfNeeded(OASMapper mapper) throws IDLException {
        if (!mapper.getSolver().isValid() && (!(this instanceof OASRandomRequest) || ((OASRandomRequest)this).isValid()))
            mapper.restartSolver(true);
        else if (mapper.getSolver().isValid() && (this instanceof OASRandomRequest && !((OASRandomRequest)this).isValid()))
            mapper.restartSolver(false);
        else
            return false;
        return true;
    }
}
