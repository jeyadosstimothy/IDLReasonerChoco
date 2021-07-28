package es.us.isa.idlreasonerchoco.analyzer;

import java.util.List;
import java.util.Map;

import es.us.isa.idlreasonerchoco.mapper.OASMapper;
import es.us.isa.idlreasonerchoco.analyzer.operations.oas.AnalysisOperation;
import es.us.isa.idlreasonerchoco.analyzer.operations.oas.OASConsistent;
import es.us.isa.idlreasonerchoco.analyzer.operations.oas.OASDeadParameter;
import es.us.isa.idlreasonerchoco.analyzer.operations.oas.OASFalseOptional;
import es.us.isa.idlreasonerchoco.analyzer.operations.oas.OASRandomRequest;
import es.us.isa.idlreasonerchoco.analyzer.operations.oas.OASValidIDL;
import es.us.isa.idlreasonerchoco.analyzer.operations.oas.OASValidRequest;
import es.us.isa.idlreasonerchoco.analyzer.operations.oas.RequestGenerationOperation;
import es.us.isa.idlreasonerchoco.configuration.IDLException;

public class OASAnalyzer extends Analyzer {

    private OASMapper mapper;

    public OASAnalyzer(String apiSpecification, String operationPath, String operationType) throws IDLException {
        this(null, apiSpecification, operationPath, operationType, false);
    }

    public OASAnalyzer(String apiSpecification, String operationPath, String operationType, boolean specAsString) throws IDLException {
        this(null, apiSpecification, operationPath, operationType, false);
    }

    public OASAnalyzer(String idlPath, String apiSpecification, String operationPath, String operationType) throws IDLException {
        this(idlPath, apiSpecification, operationPath, operationType, false);
    }

    public OASAnalyzer(String idlPath, String apiSpecification, String operationPath, String operationType, boolean specAsString) throws IDLException {
        super("oas", idlPath, apiSpecification, operationPath, operationType, specAsString);
        this.mapper = new OASMapper(configuration);
    }

    @Override
    public boolean isConsistent() throws IDLException {
    	AnalysisOperation consistent = new OASConsistent(mapper);
        return consistent.analyze();
    }

    @Override
    public boolean isDeadParameter(String paramName) throws IDLException {
        AnalysisOperation deadParameter = new OASDeadParameter(mapper, paramName);
        return deadParameter.analyze();
    }

    @Override
    public boolean isFalseOptional(String paramName) throws IDLException {
    	AnalysisOperation falseOptional = new OASFalseOptional(mapper, paramName);
        return falseOptional.analyze();
    }

    @Override
    public Boolean isValidIDL() throws IDLException {
    	AnalysisOperation validIDL = new OASValidIDL(mapper);
        return validIDL.analyze();
    }

    @Override
    public Map<String, String> getRandomValidRequest() throws IDLException {
    	RequestGenerationOperation randomValidRequest = new OASRandomRequest(mapper, true);
        return randomValidRequest.generate();
    }

    @Override
    public Map<String, String> getRandomInvalidRequest() throws IDLException {
    	RequestGenerationOperation randomInvalidRequest = new OASRandomRequest(mapper, false);
        return randomInvalidRequest.generate();
    }

    @Override
    public boolean isValidRequest(Map<String, String> request) throws IDLException {
    	AnalysisOperation validRequest = new OASValidRequest(mapper, request, false);
        return validRequest.analyze();
    }

    @Override
    public boolean isValidPartialRequest(Map<String, String> request) throws IDLException {
    	AnalysisOperation validPartialRequest = new OASValidRequest(mapper, request, true);
        return validPartialRequest.analyze();
    }

    public void updateData(Map<String, List<String>> data) throws IDLException {
    	this.mapper.updateData(data);
    }

}