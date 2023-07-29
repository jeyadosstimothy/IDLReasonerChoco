package es.us.isa.idlreasonerchoco.analyzer;

import java.util.List;
import java.util.Map;

import es.us.isa.idlreasonerchoco.configuration.IDLConfiguration;
import es.us.isa.idlreasonerchoco.configuration.IDLException;

public abstract class Analyzer {

    protected final IDLConfiguration configuration;

    protected Analyzer(String specificationType, String apiSpecification, String operationPath, String operationType) throws IDLException {
        this(specificationType, null, apiSpecification, operationPath, operationType, false);
    }

    protected Analyzer(String specificationType, String apiSpecification, String operationPath, String operationType, boolean specAsString) throws IDLException {
        this(specificationType, null, apiSpecification, operationPath, operationType, specAsString);
    }

    protected Analyzer(String specificationType, String idlPath, String apiSpecification, String operationPath, String operationType) throws IDLException {
        this(specificationType, idlPath, apiSpecification, operationPath, operationType, false);
    }

    protected Analyzer(String specificationType, String idlPath, String apiSpecification, String operationPath, String operationType, boolean specAsString) throws IDLException {
        this.configuration = new IDLConfiguration(specificationType, idlPath, apiSpecification, operationPath, operationType, specAsString);
    }

    public abstract boolean isConsistent() throws IDLException;

    public abstract boolean isDeadParameter(String paramName) throws IDLException;

    public abstract boolean isFalseOptional(String paramName) throws IDLException;

    public abstract Boolean isValidIDL() throws IDLException;
    
    public abstract Map<String, String> getRandomValidRequest() throws IDLException;

    public abstract Map<String, String> getRandomInvalidRequest() throws IDLException;

    public abstract boolean isValidRequest(Map<String, String> request) throws IDLException;

    public abstract boolean isValidPartialRequest(Map<String, String> request) throws IDLException;
    
    public abstract void updateData(Map<String, List<String>> data) throws IDLException;

    public abstract Map<String, Map<String, List<String>>> getRequestExplanation(Map<String, String> request) throws IDLException;

    public abstract Map<String, Map<String, List<String>>> getOASOperationExplanation() throws IDLException;

}