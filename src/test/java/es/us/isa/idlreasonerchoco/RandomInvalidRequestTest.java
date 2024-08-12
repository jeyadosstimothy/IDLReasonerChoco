package es.us.isa.idlreasonerchoco;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import es.us.isa.idlreasonerchoco.analyzer.Analyzer;
import es.us.isa.idlreasonerchoco.analyzer.OASAnalyzer;
import es.us.isa.idlreasonerchoco.configuration.IDLException;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class RandomInvalidRequestTest {

  @Test
  public void no_params() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/noParams", "get");
    Map<String, String> invalidRequest = analyzer.getRandomInvalidRequest();
    // System.out.println(invalidRequest);
    assertNull(invalidRequest, "There should be no invalid request for this operation");
    System.out.println("Test passed: no_params.");
  }

  @Test
  public void one_param_boolean_no_deps() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/oneParamBoolean", "get");
    Map<String, String> invalidRequest = analyzer.getRandomInvalidRequest();
    // System.out.println(invalidRequest);
    assertNull(invalidRequest, "There should be no invalid request for this operation");
    System.out.println("Test passed: one_param_boolean_no_deps.");
  }

  @Test
  public void one_param_string_no_deps() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/oneParamString", "get");
    Map<String, String> invalidRequest = analyzer.getRandomInvalidRequest();
    // System.out.println(invalidRequest);
    assertNull(invalidRequest, "There should be no invalid request for this operation");
    System.out.println("Test passed: one_param_string_no_deps.");
  }

  @Test
  public void one_param_int_no_deps() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/oneParamInt", "get");
    Map<String, String> invalidRequest = analyzer.getRandomInvalidRequest();
    // System.out.println(invalidRequest);
    assertNull(invalidRequest, "There should be no invalid request for this operation");
    System.out.println("Test passed: one_param_int_no_deps.");
  }

  @Test
  public void one_param_enum_string_no_deps() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer(
            "./src/test/resources/OAS_test_suite_orig.yaml", "/oneParamEnumString", "get");
    Map<String, String> invalidRequest = analyzer.getRandomInvalidRequest();
    // System.out.println(invalidRequest);
    assertNull(invalidRequest, "There should be no invalid request for this operation");
    System.out.println("Test passed: one_param_enum_string_no_deps.");
  }

  @Test
  public void one_param_enum_int_no_deps() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/oneParamEnumInt", "get");
    Map<String, String> invalidRequest = analyzer.getRandomInvalidRequest();
    // System.out.println(invalidRequest);
    assertNull(invalidRequest, "There should be no invalid request for this operation");
    System.out.println("Test passed: one_param_enum_int_no_deps.");
  }

  @Test
  public void one_dep_requires() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer(
            "./src/test/resources/OAS_test_suite_orig.yaml", "/oneDependencyRequires", "get");
    Map<String, String> invalidRequest = analyzer.getRandomInvalidRequest();
    // System.out.println(invalidRequest);
    assertFalse(analyzer.isValidRequest(invalidRequest), "The request should be NOT valid");
    System.out.println("Test passed: one_dep_requires.");
  }

  @Test
  public void one_dep_or() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/oneDependencyOr", "get");
    Map<String, String> invalidRequest = analyzer.getRandomInvalidRequest();
    // System.out.println(invalidRequest);
    assertFalse(analyzer.isValidRequest(invalidRequest), "The request should be NOT valid");
    System.out.println("Test passed: one_dep_or.");
  }

  @Test
  public void one_dep_onlyone() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer(
            "./src/test/resources/OAS_test_suite_orig.yaml", "/oneDependencyOnlyOne", "get");
    Map<String, String> invalidRequest = analyzer.getRandomInvalidRequest();
    // System.out.println(invalidRequest);
    assertFalse(analyzer.isValidRequest(invalidRequest), "The request should be NOT valid");
    System.out.println("Test passed: one_dep_onlyone.");
  }

  @Test
  public void one_dep_allornone() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer(
            "./src/test/resources/OAS_test_suite_orig.yaml", "/oneDependencyAllOrNone", "get");
    Map<String, String> invalidRequest = analyzer.getRandomInvalidRequest();
    // System.out.println(invalidRequest);
    assertFalse(analyzer.isValidRequest(invalidRequest), "The request should be NOT valid");
    System.out.println("Test passed: one_dep_allornone.");
  }

  @Test
  public void one_dep_zeroorone() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer(
            "./src/test/resources/OAS_test_suite_orig.yaml", "/oneDependencyZeroOrOne", "get");
    Map<String, String> invalidRequest = analyzer.getRandomInvalidRequest();
    // System.out.println(invalidRequest);
    assertFalse(analyzer.isValidRequest(invalidRequest), "The request should be NOT valid");
    System.out.println("Test passed: one_dep_zeroorone.");
  }

  @Test
  public void one_dep_arithrel() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer(
            "./src/test/resources/OAS_test_suite_orig.yaml", "/oneDependencyArithRel", "get");
    Map<String, String> invalidRequest = analyzer.getRandomInvalidRequest();
    // System.out.println(invalidRequest);
    assertFalse(analyzer.isValidRequest(invalidRequest), "The request should be NOT valid");
    System.out.println("Test passed: one_dep_arithrel.");
  }

  @Test
  public void one_dep_complex() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer(
            "./src/test/resources/OAS_test_suite_orig.yaml", "/oneDependencyComplex", "get");
    Map<String, String> invalidRequest = analyzer.getRandomInvalidRequest();
    // System.out.println(invalidRequest);
    assertFalse(analyzer.isValidRequest(invalidRequest), "The request should be NOT valid");
    System.out.println("Test passed: one_dep_complex.");
  }

  @Test
  public void combinatorial1() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial1", "get");
    Map<String, String> invalidRequest = analyzer.getRandomInvalidRequest();
    // System.out.println(invalidRequest);
    assertFalse(analyzer.isValidRequest(invalidRequest), "The request should be NOT valid");
    System.out.println("Test passed: combinatorial1.");
  }

  //    // The operations whose IDL specification is invalid cannot be tested
  //    @Test
  //    public void combinatorial2() throws IDLException {
  //        Analyzer analyzer = new OASAnalyzer("oas",
  // "./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial2", "get");
  //        System.out.println("Test passed: combinatorial2.");
  //    }

  @Test
  public void combinatorial3() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial3", "get");
    Map<String, String> invalidRequest = analyzer.getRandomInvalidRequest();
    // System.out.println(invalidRequest);
    assertFalse(analyzer.isValidRequest(invalidRequest), "The request should be NOT valid");
    System.out.println("Test passed: combinatorial3.");
  }

  @Test
  public void combinatorial4() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial4", "get");
    Map<String, String> invalidRequest = analyzer.getRandomInvalidRequest();
    // System.out.println(invalidRequest);
    assertFalse(analyzer.isValidRequest(invalidRequest), "The request should be NOT valid");
    System.out.println("Test passed: combinatorial4.");
  }

  @Test
  public void combinatorial5() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial5", "get");
    Map<String, String> invalidRequest = analyzer.getRandomInvalidRequest();
    // System.out.println(invalidRequest);
    assertFalse(analyzer.isValidRequest(invalidRequest), "The request should be NOT valid");
    System.out.println("Test passed: combinatorial5.");
  }

  //    // The operations whose IDL specification is invalid cannot be tested
  //    @Test
  //    public void combinatorial6() throws IDLException {
  //        Analyzer analyzer = new OASAnalyzer("oas",
  // "./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial6", "get");
  //        System.out.println("Test passed: combinatorial6.");
  //    }

  //    // The operations whose IDL specification is invalid cannot be tested
  //    @Test
  //    public void combinatorial7() throws IDLException {
  //        Analyzer analyzer = new OASAnalyzer("oas",
  // "./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial7", "get");
  //        System.out.println("Test passed: combinatorial7.");
  //    }

  @Test
  public void combinatorial8() throws IDLException {
    Analyzer analyzer =
        new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial8", "get");
    Map<String, String> invalidRequest = analyzer.getRandomInvalidRequest();
    // System.out.println(invalidRequest);
    assertFalse(analyzer.isValidRequest(invalidRequest), "The request should be NOT valid");
    System.out.println("Test passed: combinatorial8.");
  }

  //    // The operations whose IDL specification is invalid cannot be tested
  //    @Test
  //    public void combinatorial9() throws IDLException {
  //        Analyzer analyzer = new OASAnalyzer("oas",
  // "./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial9", "get");
  //        System.out.println("Test passed: combinatorial9.");
  //    }

  //    // The operations whose IDL specification is invalid cannot be tested
  //    @Test
  //    public void combinatorial10() throws IDLException {
  //        Analyzer analyzer = new OASAnalyzer("oas",
  // "./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial10", "get");
  //        System.out.println("Test passed: combinatorial10.");
  //    }
}
