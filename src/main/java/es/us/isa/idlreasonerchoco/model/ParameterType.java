package es.us.isa.idlreasonerchoco.model;

public enum ParameterType {
  NUMBER("number"),
  ARRAY("array"),
  INTEGER("integer"),
  STRING("string"),
  BOOLEAN("boolean"),
  OBJECT("object");

  private String type;

  ParameterType(String type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return this.type;
  }
}
