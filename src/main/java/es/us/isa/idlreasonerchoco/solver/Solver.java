package es.us.isa.idlreasonerchoco.solver;

import org.chocosolver.solver.Model;

import es.us.isa.idlreasonerchoco.configuration.IDLException;

public abstract class Solver {
    protected Model chocoModel;

	protected Solver(String operationPath) throws IDLException {
        this.chocoModel = new Model(operationPath);
    }

	public Model getChocoModel() {
        return chocoModel;
    }
}