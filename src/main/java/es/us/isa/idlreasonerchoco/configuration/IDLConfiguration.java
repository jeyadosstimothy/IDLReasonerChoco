package es.us.isa.idlreasonerchoco.configuration;

public class IDLConfiguration {

  private final String specificationType;
  private final String operationPath;
  private final String operationType;
  private String apiSpecification;
  private String idlPath;
  private boolean specAsString;

  public IDLConfiguration(
      String specificationType,
      String idlPath,
      String apiSpecification,
      String operationPath,
      String operationType,
      boolean specAsString) {
    this.specificationType = specificationType;
    this.operationPath = operationPath;
    this.operationType = operationType;
    this.apiSpecification = apiSpecification;
    this.idlPath = idlPath;
    this.specAsString = specAsString;
  }

  public String getSpecificationType() {
    return specificationType;
  }

  public String getApiSpecification() {
    return apiSpecification;
  }

  public String getOperationPath() {
    return operationPath;
  }

  public String getOperationType() {
    return operationType;
  }

  public String getIdlPath() {
    return idlPath;
  }

  public boolean isSpecAsString() {
    return specAsString;
  }
}
