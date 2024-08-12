package es.us.isa.idlreasonerchoco.solver;

import es.us.isa.idlreasonerchoco.configuration.IDLException;
import org.chocosolver.solver.Model;

public abstract class Solver {
  protected Model chocoModel;

  protected Solver(String operationPath) throws IDLException {
    this.chocoModel = new Model(operationPath);
  }

  public Model getChocoModel() {
    return chocoModel;
  }
}
