package es.us.isa.idlreasonerchoco.explanation;

import static org.junit.jupiter.api.Assertions.*;

import es.us.isa.idlreasonerchoco.analyzer.Analyzer;
import es.us.isa.idlreasonerchoco.analyzer.OASAnalyzer;
import es.us.isa.idlreasonerchoco.configuration.IDLException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class OperationExplanationTest {

  @Test
  public void no_params() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/noParams", "get");
    Map<String, Map<String, List<String>>> result = analyzer.getExplanation(null);

    assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
    assertNull(result.get("IDLConflicts"), "The IDL should has no conflicts!");
    assertNull(result.get("DeadParameters"), "The IDL should has no DeadParameters!");
    assertNull(
        result.get("FalseOptionalParameters"), "The IDL should has no FalseOptionalParameters!");

    System.out.println("result: " + result);
    System.out.println("Test passed: no_params.");
  }

  @Test
  public void one_param_boolean_no_deps() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/oneParamBoolean", "get");
    Map<String, Map<String, List<String>>> result = analyzer.getExplanation(null);

    assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");

    assertNull(result.get("IDLConflicts"), "The IDL should has no conflicts!");
    assertNull(result.get("DeadParameters"), "The IDL should has no DeadParameters!");
    assertNull(
        result.get("FalseOptionalParameters"), "The IDL should has no FalseOptionalParameters!");

    System.out.println("result: " + result);
    System.out.println("Test passed: one_param_boolean_no_deps.");
  }

  @Test
  public void one_param_string_no_deps() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/oneParamString", "get");
    Map<String, Map<String, List<String>>> result = analyzer.getExplanation(null);

    assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
    assertNull(result.get("IDLConflicts"), "The IDL should has no conflicts!");
    assertNull(result.get("DeadParameters"), "The IDL should has no DeadParameters!");
    assertNull(
        result.get("FalseOptionalParameters"), "The IDL should has no FalseOptionalParameters!");

    System.out.println("result: " + result);
    System.out.println("Test passed: one_param_string_no_deps.");
  }

  @Test
  public void one_param_int_no_deps() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/oneParamInt", "get");
    Map<String, Map<String, List<String>>> result = analyzer.getExplanation(null);

    assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
    assertNull(result.get("IDLConflicts"), "The IDL should has no conflicts!");
    assertNull(result.get("DeadParameters"), "The IDL should has no DeadParameters!");
    assertNull(
        result.get("FalseOptionalParameters"), "The IDL should has no FalseOptionalParameters!");

    System.out.println("result: " + result);
    System.out.println("Test passed: one_param_int_no_deps.");
  }

  @Test
  public void one_param_enum_string_no_deps() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer(
            "./src/test/resources/OAS_test_suite_orig.yaml", "/oneParamEnumString", "get");
    Map<String, Map<String, List<String>>> result = analyzer.getExplanation(null);

    assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
    assertNull(result.get("IDLConflicts"), "The IDL should has no conflicts!");
    assertNull(result.get("DeadParameters"), "The IDL should has no DeadParameters!");
    assertNull(
        result.get("FalseOptionalParameters"), "The IDL should has no FalseOptionalParameters!");

    System.out.println("result: " + result);
    System.out.println("Test passed: one_param_enum_string_no_deps.");
  }

  @Test
  public void one_param_enum_int_no_deps() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/oneParamEnumInt", "get");
    Map<String, Map<String, List<String>>> result = analyzer.getExplanation(null);

    assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
    assertNull(result.get("IDLConflicts"), "The IDL should has no conflicts!");
    assertNull(result.get("DeadParameters"), "The IDL should has no DeadParameters!");
    assertNull(
        result.get("FalseOptionalParameters"), "The IDL should has no FalseOptionalParameters!");

    System.out.println("result: " + result);
    System.out.println("Test passed: one_param_enum_int_no_deps.");
  }

  @Test
  public void one_dep_requires() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer(
            "./src/test/resources/OAS_test_suite_orig.yaml", "/oneDependencyRequires", "get");
    Map<String, Map<String, List<String>>> result = analyzer.getExplanation(null);

    assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
    assertNull(result.get("IDLConflicts"), "The IDL should has no conflicts!");
    assertNull(result.get("DeadParameters"), "The IDL should has no DeadParameters!");
    assertNull(
        result.get("FalseOptionalParameters"), "The IDL should has no FalseOptionalParameters!");

    System.out.println("result: " + result);
    System.out.println("Test passed: one_dep_requires.");
  }

  @Test
  public void one_dep_or() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/oneDependencyOr", "get");
    Map<String, Map<String, List<String>>> result = analyzer.getExplanation(null);

    assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
    assertNull(result.get("IDLConflicts"), "The IDL should has no conflicts!");
    assertNull(result.get("DeadParameters"), "The IDL should has no DeadParameters!");
    assertNull(
        result.get("FalseOptionalParameters"), "The IDL should has no FalseOptionalParameters!");

    System.out.println("result: " + result);
    System.out.println("Test passed: one_dep_or.");
  }

  @Test
  public void one_dep_onlyone() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer(
            "./src/test/resources/OAS_test_suite_orig.yaml", "/oneDependencyOnlyOne", "get");
    Map<String, Map<String, List<String>>> result = analyzer.getExplanation(null);

    assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
    assertNull(result.get("IDLConflicts"), "The IDL should has no conflicts!");
    assertNull(result.get("DeadParameters"), "The IDL should has no DeadParameters!");
    assertNull(
        result.get("FalseOptionalParameters"), "The IDL should has no FalseOptionalParameters!");

    System.out.println("result: " + result);
    System.out.println("Test passed: one_dep_onlyone.");
  }

  @Test
  public void one_dep_allornone() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer(
            "./src/test/resources/OAS_test_suite_orig.yaml", "/oneDependencyAllOrNone", "get");
    Map<String, Map<String, List<String>>> result = analyzer.getExplanation(null);

    assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
    assertNull(result.get("IDLConflicts"), "The IDL should has no conflicts!");
    assertNull(result.get("DeadParameters"), "The IDL should has no DeadParameters!");
    assertNull(
        result.get("FalseOptionalParameters"), "The IDL should has no FalseOptionalParameters!");

    System.out.println("result: " + result);
    System.out.println("Test passed: one_dep_allornone.");
  }

  @Test
  public void one_dep_zeroorone() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer(
            "./src/test/resources/OAS_test_suite_orig.yaml", "/oneDependencyZeroOrOne", "get");
    Map<String, Map<String, List<String>>> result = analyzer.getExplanation(null);

    assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
    assertNull(result.get("IDLConflicts"), "The IDL should has no conflicts!");
    assertNull(result.get("DeadParameters"), "The IDL should has no DeadParameters!");
    assertNull(
        result.get("FalseOptionalParameters"), "The IDL should has no FalseOptionalParameters!");

    System.out.println("result: " + result);
    System.out.println("Test passed: one_dep_zeroorone.");
  }

  @Test
  public void one_dep_arithrel() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer(
            "./src/test/resources/OAS_test_suite_orig.yaml", "/oneDependencyArithRel", "get");
    Map<String, Map<String, List<String>>> result = analyzer.getExplanation(null);

    assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
    assertNull(result.get("IDLConflicts"), "The IDL should has no conflicts!");
    assertNull(result.get("DeadParameters"), "The IDL should has no DeadParameters!");
    assertNull(
        result.get("FalseOptionalParameters"), "The IDL should has no FalseOptionalParameters!");

    System.out.println("result: " + result);
    System.out.println("Test passed: one_dep_arithrel.");
  }

  @Test
  public void one_dep_complex() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer(
            "./src/test/resources/OAS_test_suite_orig.yaml", "/oneDependencyComplex", "get");
    Map<String, Map<String, List<String>>> result = analyzer.getExplanation(null);

    assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
    assertNull(result.get("IDLConflicts"), "The IDL should has no conflicts!");
    assertNull(result.get("DeadParameters"), "The IDL should has no DeadParameters!");
    assertNull(
        result.get("FalseOptionalParameters"), "The IDL should has no FalseOptionalParameters!");

    System.out.println("result: " + result);
    System.out.println("Test passed: one_dep_complex.");
  }

  @Test
  public void combinatorial1() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial1", "get");
    Map<String, Map<String, List<String>>> result = analyzer.getExplanation(null);

    assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
    assertNull(result.get("IDLConflicts"), "The IDL should has no conflicts!");
    assertNull(result.get("DeadParameters"), "The IDL should has no DeadParameters!");
    assertNull(
        result.get("FalseOptionalParameters"), "The IDL should has no FalseOptionalParameters!");

    System.out.println("result: " + result);
    System.out.println("Test passed: combinatorial1.");
  }

  @Test
  public void combinatorial2() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial2", "get");
    Map<String, Map<String, List<String>>> result = analyzer.getExplanation(null);

    assertFalse(analyzer.isValidIDL(), "The IDL should be VALID");
    assertNull(result.get("IDLConflicts"), "The IDL should has no conflicts!");
    assertNotNull(result.get("DeadParameters"), "The IDL should has no DeadParameters!");
    assertTrue(
        result.get("FalseOptionalParameters").isEmpty(),
        "The IDL should has no FalseOptionalParameters!");

    System.out.println("result: " + result);
    System.out.println("Test passed: combinatorial2.");
  }

  @Test
  public void combinatorial3() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial3", "get");
    Map<String, Map<String, List<String>>> result = analyzer.getExplanation(null);

    assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");

    assertNull(result.get("IDLConflicts"), "The IDL should has no conflicts!");
    assertNull(result.get("DeadParameters"), "The IDL should has no DeadParameters!");
    assertNull(
        result.get("FalseOptionalParameters"), "The IDL should has no FalseOptionalParameters!");

    System.out.println("result: " + result);

    System.out.println("Test passed: combinatorial3.");
  }

  @Test
  public void combinatorial4() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial4", "get");
    Map<String, Map<String, List<String>>> result = analyzer.getExplanation(null);

    assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
    assertNull(result.get("IDLConflicts"), "The IDL should has no conflicts!");
    assertNull(result.get("DeadParameters"), "The IDL should has no DeadParameters!");
    assertNull(
        result.get("FalseOptionalParameters"), "The IDL should has no FalseOptionalParameters!");

    System.out.println("result: " + result);
    System.out.println("Test passed: combinatorial4.");
  }

  @Test
  public void combinatorial5() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial5", "get");
    Map<String, Map<String, List<String>>> result = analyzer.getExplanation(null);

    assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
    assertNull(result.get("IDLConflicts"), "The IDL should has no conflicts!");
    assertNull(result.get("DeadParameters"), "The IDL should has no DeadParameters!");
    assertNull(
        result.get("FalseOptionalParameters"), "The IDL should has no FalseOptionalParameters!");

    System.out.println("result: " + result);
    System.out.println("Test passed: combinatorial5.");
  }

  @Test
  public void combinatorial6() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial6", "get");
    Map<String, Map<String, List<String>>> result = analyzer.getExplanation(null);

    assertFalse(analyzer.isValidIDL(), "The IDL should be VALID");
    assertNotNull(result.get("IDLConflicts"), "The IDL should has no conflicts!");
    assertNull(result.get("DeadParameters"), "The IDL should has no DeadParameters!");
    assertNull(
        result.get("FalseOptionalParameters"), "The IDL should has no FalseOptionalParameters!");

    System.out.println("result: " + result);
    System.out.println("Test passed: combinatorial6.");
  }

  @Test
  public void combinatorial7() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial7", "get");
    Map<String, Map<String, List<String>>> result = analyzer.getExplanation(null);

    assertFalse(analyzer.isValidIDL(), "The IDL should be not VALID");
    assertNull(result.get("IDLConflicts"), "The IDL should has no conflicts!");
    assertNotNull(result.get("DeadParameters"), "The IDL should has no DeadParameters!");
    assertTrue(
        result.get("FalseOptionalParameters").isEmpty(),
        "The IDL should has no FalseOptionalParameters!");

    System.out.println("result: " + result);
    System.out.println("Test passed: combinatorial7.");
  }

  @Test
  public void combinatorial8() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial8", "get");
    Map<String, Map<String, List<String>>> result = analyzer.getExplanation(null);

    assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
    assertNull(result.get("IDLConflicts"), "The IDL should has no conflicts!");
    assertNull(result.get("DeadParameters"), "The IDL should has no DeadParameters!");
    assertNull(
        result.get("FalseOptionalParameters"), "The IDL should has no FalseOptionalParameters!");

    System.out.println("result: " + result);
    System.out.println("Test passed: combinatorial8.");
  }

  @Test
  public void combinatorial9() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial9", "get");
    Map<String, Map<String, List<String>>> result = analyzer.getExplanation(null);

    assertFalse(analyzer.isValidIDL(), "The IDL should be VALID");
    assertNull(result.get("IDLConflicts"), "The IDL should has no conflicts!");
    assertNotNull(result.get("DeadParameters"), "The IDL should has no DeadParameters!");
    assertNotNull(
        result.get("FalseOptionalParameters"), "The IDL should has no FalseOptionalParameters!");

    System.out.println("result: " + result);
    System.out.println("Test passed: combinatorial9.");
  }

  @Test
  public void combinatorial10() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial10", "get");
    Map<String, Map<String, List<String>>> result = analyzer.getExplanation(null);

    assertFalse(analyzer.isValidIDL(), "The IDL should be VALID");
    assertNotNull(result.get("IDLConflicts"), "The IDL should has no conflicts!");
    assertNull(result.get("DeadParameters"), "The IDL should has no DeadParameters!");
    assertNull(
        result.get("FalseOptionalParameters"), "The IDL should has no FalseOptionalParameters!");

    System.out.println("result: " + result);
    System.out.println("Test passed: combinatorial10.");
  }
}
