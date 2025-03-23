package es.us.isa.idlreasonerchoco;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import es.us.isa.idlreasonerchoco.analyzer.Analyzer;
import es.us.isa.idlreasonerchoco.analyzer.OASAnalyzer;
import es.us.isa.idlreasonerchoco.configuration.IDLException;
import org.junit.jupiter.api.Test;

public class ValidIDLTest {

  @Test
  public void no_params() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/noParams", "get");
    assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
    System.out.println("Test passed: no_params.");
  }

  @Test
  public void one_param_boolean_no_deps() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/oneParamBoolean", "get");
    assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
    System.out.println("Test passed: one_param_boolean_no_deps.");
  }

  @Test
  public void one_param_string_no_deps() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/oneParamString", "get");
    assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
    System.out.println("Test passed: one_param_string_no_deps.");
  }

  @Test
  public void one_param_int_no_deps() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/oneParamInt", "get");
    assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
    System.out.println("Test passed: one_param_int_no_deps.");
  }

  @Test
  public void one_param_enum_string_no_deps() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer(
            "./src/test/resources/OAS_test_suite_orig.yaml", "/oneParamEnumString", "get");
    assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
    System.out.println("Test passed: one_param_enum_string_no_deps.");
  }

  @Test
  public void one_param_enum_int_no_deps() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/oneParamEnumInt", "get");
    assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
    System.out.println("Test passed: one_param_enum_int_no_deps.");
  }

  @Test
  public void one_dep_requires() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer(
            "./src/test/resources/OAS_test_suite_orig.yaml", "/oneDependencyRequires", "get");
    assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
    System.out.println("Test passed: one_dep_requires.");
  }

  @Test
  public void one_dep_or() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/oneDependencyOr", "get");
    assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
    System.out.println("Test passed: one_dep_or.");
  }

  @Test
  public void one_dep_onlyone() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer(
            "./src/test/resources/OAS_test_suite_orig.yaml", "/oneDependencyOnlyOne", "get");
    assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
    System.out.println("Test passed: one_dep_onlyone.");
  }

  @Test
  public void one_dep_allornone() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer(
            "./src/test/resources/OAS_test_suite_orig.yaml", "/oneDependencyAllOrNone", "get");
    assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
    System.out.println("Test passed: one_dep_allornone.");
  }

  @Test
  public void one_dep_zeroorone() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer(
            "./src/test/resources/OAS_test_suite_orig.yaml", "/oneDependencyZeroOrOne", "get");
    assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
    System.out.println("Test passed: one_dep_zeroorone.");
  }

  @Test
  public void one_dep_arithrel() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer(
            "./src/test/resources/OAS_test_suite_orig.yaml", "/oneDependencyArithRel", "get");
    assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
    System.out.println("Test passed: one_dep_arithrel.");
  }

  @Test
  public void one_dep_complex() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer(
            "./src/test/resources/OAS_test_suite_orig.yaml", "/oneDependencyComplex", "get");
    assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
    System.out.println("Test passed: one_dep_complex.");
  }

  @Test
  public void combinatorial1() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial1", "get");
    assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
    System.out.println("Test passed: combinatorial1.");
  }

  @Test
  public void combinatorial2() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial2", "get");
    assertFalse(analyzer.isValidIDL(), "The IDL should be NOT valid");
    System.out.println("Test passed: combinatorial2.");
  }

  @Test
  public void combinatorial3() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial3", "get");
    assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
    System.out.println("Test passed: combinatorial3.");
  }

  @Test
  public void combinatorial4() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial4", "get");
    assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
    System.out.println("Test passed: combinatorial4.");
  }

  @Test
  public void combinatorial5() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial5", "get");
    assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
    System.out.println("Test passed: combinatorial5.");
  }

  @Test
  public void combinatorial6() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial6", "get");
    assertFalse(analyzer.isValidIDL(), "The IDL should be NOT valid");
    System.out.println("Test passed: combinatorial6.");
  }

  @Test
  public void combinatorial7() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial7", "get");
    assertFalse(analyzer.isValidIDL(), "The IDL should be NOT valid");
    System.out.println("Test passed: combinatorial7.");
  }

  @Test
  public void combinatorial8() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial8", "get");
    assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
    System.out.println("Test passed: combinatorial8.");
  }

  @Test
  public void combinatorial9() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial9", "get");
    assertFalse(analyzer.isValidIDL(), "The IDL should be NOT valid");
    System.out.println("Test passed: combinatorial9.");
  }

  @Test
  public void combinatorial10() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial10", "get");
    assertFalse(analyzer.isValidIDL(), "The IDL should be NOT valid");
    System.out.println("Test passed: combinatorial10.");
  }
}
