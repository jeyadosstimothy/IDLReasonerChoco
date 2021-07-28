package es.us.isa.idlreasonerchoco.analyzer.operations.oas;

import es.us.isa.idlreasonerchoco.mapper.OASMapper;
import es.us.isa.idlreasonerchoco.configuration.IDLException;

public class OASConsistent implements AnalysisOperation {

    private final OASMapper mapper;

    public OASConsistent(OASMapper mapper) {
        this.mapper = mapper;
    }
    public boolean analyze() throws IDLException {
        restartSolverIfNeeded(mapper);
        return mapper.getChocoModel().getSolver().solve();
    }
}
