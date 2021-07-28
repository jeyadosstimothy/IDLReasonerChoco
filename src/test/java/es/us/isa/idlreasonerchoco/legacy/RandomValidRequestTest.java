package es.us.isa.idlreasonerchoco.legacy;

import es.us.isa.idlreasonerchoco.analyzer.Analyzer;
import es.us.isa.idlreasonerchoco.analyzer.OASAnalyzer;
import es.us.isa.idlreasonerchoco.configuration.IDLException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RandomValidRequestTest {

    @Test
    public void no_params() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("./src/test/resources/no_deps.idl", "./src/test/resources/OAS_test_suite_old.yaml", "/noParams", "get");
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        // System.out.println(validRequest);
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: no_params.");
    }

    @Test
    public void one_param_boolean_no_deps() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("./src/test/resources/no_deps.idl", "./src/test/resources/OAS_test_suite_old.yaml", "/oneParamBoolean", "get");
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        // System.out.println(validRequest);
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: one_param_boolean_no_deps.");
    }

    @Test
    public void one_param_string_no_deps() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("./src/test/resources/no_deps.idl", "./src/test/resources/OAS_test_suite_old.yaml", "/oneParamString", "get");
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        // System.out.println(validRequest);
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: one_param_string_no_deps.");
    }

    @Test
    public void one_param_int_no_deps() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("./src/test/resources/no_deps.idl", "./src/test/resources/OAS_test_suite_old.yaml", "/oneParamInt", "get");
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        // System.out.println(validRequest);
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: one_param_int_no_deps.");
    }

    @Test
    public void one_param_enum_string_no_deps() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("./src/test/resources/no_deps.idl", "./src/test/resources/OAS_test_suite_old.yaml", "/oneParamEnumString", "get");
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        // System.out.println(validRequest);
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: one_param_enum_string_no_deps.");
    }

    @Test
    public void one_param_enum_int_no_deps() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("./src/test/resources/no_deps.idl", "./src/test/resources/OAS_test_suite_old.yaml", "/oneParamEnumInt", "get");
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        // System.out.println(validRequest);
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: one_param_enum_int_no_deps.");
    }

    @Test
    public void one_dep_requires() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("./src/test/resources/one_dep_requires.idl", "./src/test/resources/OAS_test_suite_old.yaml", "/oneDependency", "get");
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        // System.out.println(validRequest);
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: one_dep_requires.");
    }

    @Test
    public void one_dep_or() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("./src/test/resources/one_dep_or.idl", "./src/test/resources/OAS_test_suite_old.yaml", "/oneDependency", "get");
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        // System.out.println(validRequest);
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: one_dep_or.");
    }

    @Test
    public void one_dep_onlyone() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("./src/test/resources/one_dep_onlyone.idl", "./src/test/resources/OAS_test_suite_old.yaml", "/oneDependency", "get");
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        // System.out.println(validRequest);
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: one_dep_onlyone.");
    }

    @Test
    public void one_dep_allornone() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("./src/test/resources/one_dep_allornone.idl", "./src/test/resources/OAS_test_suite_old.yaml", "/oneDependency", "get");
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        // System.out.println(validRequest);
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: one_dep_allornone.");
    }

    @Test
    public void one_dep_zeroorone() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("./src/test/resources/one_dep_zeroorone.idl", "./src/test/resources/OAS_test_suite_old.yaml", "/oneDependency", "get");
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        // System.out.println(validRequest);
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: one_dep_zeroorone.");
    }

    @Test
    public void one_dep_arithrel() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("./src/test/resources/one_dep_arithrel.idl", "./src/test/resources/OAS_test_suite_old.yaml", "/oneDependency", "get");
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        // System.out.println(validRequest);
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: one_dep_arithrel.");
    }

    @Test
    public void one_dep_complex() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("./src/test/resources/one_dep_complex.idl", "./src/test/resources/OAS_test_suite_old.yaml", "/oneDependency", "get");
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        // System.out.println(validRequest);
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: one_dep_complex.");
    }

    @Test
    public void combinatorial1() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("./src/test/resources/combinatorial1.idl", "./src/test/resources/OAS_test_suite_old.yaml", "/combinatorial1", "get");
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        // System.out.println(validRequest);
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: combinatorial1.");
    }

//    // The operations whose IDL specification is invalid cannot be tested
//    @Test
//    public void combinatorial2() throws IDLException {
//        Analyzer analyzer = new OASAnalyzer("./src/test/resources/combinatorial2.idl", "./src/test/resources/OAS_test_suite_old.yaml", "/combinatorial2", "get");
//        System.out.println("Test passed: combinatorial2.");
//    }

    @Test
    public void combinatorial3() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("./src/test/resources/combinatorial3.idl", "./src/test/resources/OAS_test_suite_old.yaml", "/combinatorial3", "get");
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        // System.out.println(validRequest);
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: combinatorial3.");
    }

    @Test
    public void combinatorial4() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("./src/test/resources/combinatorial4.idl", "./src/test/resources/OAS_test_suite_old.yaml", "/combinatorial4", "get");
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        // System.out.println(validRequest);
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: combinatorial4.");
    }

    @Test
    public void combinatorial5() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("./src/test/resources/combinatorial5.idl", "./src/test/resources/OAS_test_suite_old.yaml", "/combinatorial5", "get");
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        // System.out.println(validRequest);
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: combinatorial5.");
    }

//    // The operations whose IDL specification is invalid cannot be tested
//    @Test
//    public void combinatorial6() throws IDLException {
//        Analyzer analyzer = new OASAnalyzer("./src/test/resources/combinatorial6.idl", "./src/test/resources/OAS_test_suite_old.yaml", "/combinatorial6", "get");
//        System.out.println("Test passed: combinatorial6.");
//    }

//    // The operations whose IDL specification is invalid cannot be tested
//    @Test
//    public void combinatorial7() throws IDLException {
//        Analyzer analyzer = new OASAnalyzer("./src/test/resources/combinatorial7.idl", "./src/test/resources/OAS_test_suite_old.yaml", "/combinatorial7", "get");
//        System.out.println("Test passed: combinatorial7.");
//    }

    @Test
    public void combinatorial8() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("./src/test/resources/combinatorial8.idl", "./src/test/resources/OAS_test_suite_old.yaml", "/combinatorial8", "get");
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        // System.out.println(validRequest);
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: combinatorial8.");
    }

//    // The operations whose IDL specification is invalid cannot be tested
//    @Test
//    public void combinatorial9() throws IDLException {
//        Analyzer analyzer = new OASAnalyzer("./src/test/resources/combinatorial9.idl", "./src/test/resources/OAS_test_suite_old.yaml", "/combinatorial9", "get");
//        System.out.println("Test passed: combinatorial9.");
//    }

//    // The operations whose IDL specification is invalid cannot be tested
//    @Test
//    public void combinatorial10() throws IDLException {
//        Analyzer analyzer = new OASAnalyzer("./src/test/resources/combinatorial10.idl", "./src/test/resources/OAS_test_suite_old.yaml", "/combinatorial10", "get");
//        System.out.println("Test passed: combinatorial10.");
//    }
}
