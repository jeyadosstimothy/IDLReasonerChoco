package es.us.isa.idlreasonerchoco;

import java.time.Duration;
import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import es.us.isa.idlreasonerchoco.analyzer.Analyzer;
import es.us.isa.idlreasonerchoco.analyzer.OASAnalyzer;
import es.us.isa.idlreasonerchoco.configuration.IDLException;

import static org.junit.jupiter.api.Assertions.*;

public class AdditionalTests {

    @Test
    public void idl4oasTest() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("./src/test/resources/OAS_example_orig.yaml", "/optionalParams", "get");
        assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
        System.out.println("Test passed: idl4oasTest.");
    }

    @Test
    public void formDataParametersTest() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("./src/test/resources/stripe_v3.yaml", "/v1/products", "post");
        assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
        System.out.println("Test passed: formDataParametersTest.");
    }

    @Test
    public void formDataParametersV3Test() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("./src/test/resources/stripe_v3.yaml", "/v1/products", "post");
        assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");
    }

    @Test
    public void validAfterInvalidRequest() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial5", "get");
        analyzer.getRandomInvalidRequest();
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
    }

    @Test
    public void invalidAfterValidRequest() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial5", "get");
        analyzer.getRandomValidRequest();
        Map<String, String> invalidRequest = analyzer.getRandomInvalidRequest();
        assertFalse(analyzer.isValidRequest(invalidRequest), "The request should be NOT valid");
    }

    @Test
    public void multipleOperationsTest() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial8", "get");

        assertFalse(analyzer.isDeadParameter("p1"), "The parameter p1 should NOT be dead");
        assertFalse(analyzer.isDeadParameter("p2"), "The parameter p2 should NOT be dead");
        assertFalse(analyzer.isDeadParameter("p3"), "The parameter p3 should NOT be dead");
        assertFalse(analyzer.isDeadParameter("p4"), "The parameter p4 should NOT be dead");
        assertFalse(analyzer.isDeadParameter("p5"), "The parameter p5 should NOT be dead");
        assertFalse(analyzer.isDeadParameter("p6"), "The parameter p6 should NOT be dead");
        assertFalse(analyzer.isDeadParameter("p7"), "The parameter p7 should NOT be dead");
        assertFalse(analyzer.isDeadParameter("p8"), "The parameter p8 should NOT be dead");
        assertFalse(analyzer.isDeadParameter("p9"), "The parameter p9 should NOT be dead");
        assertFalse(analyzer.isDeadParameter("p10"), "The parameter p10 should NOT be dead");

        Map<String, String> validRequest = new HashMap<>();
        validRequest.put("p1", "false");
        validRequest.put("p2", "false");
        validRequest.put("p4", "true");
        validRequest.put("p5", "true");
        validRequest.put("p6", "one string");
        validRequest.put("p8", "something");
        validRequest.put("p9", "fixed string");
        validRequest.put("p10", "something");
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");

        assertFalse(analyzer.isFalseOptional("p1"), "The parameter p1 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p2"), "The parameter p2 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p3"), "The parameter p3 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p4"), "The parameter p4 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p5"), "The parameter p5 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p6"), "The parameter p6 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p7"), "The parameter p7 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p8"), "The parameter p8 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p9"), "The parameter p9 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p10"), "The parameter p10 should NOT be false optional");

        Map<String, String> validRandomRequest = analyzer.getRandomValidRequest();
        assertTrue(analyzer.isValidRequest(validRandomRequest), "The request should be VALID");

        assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");

        Map<String, String> invalidRandomRequest = analyzer.getRandomInvalidRequest();
        assertFalse(analyzer.isValidRequest(invalidRandomRequest), "The request should be NOT valid");

        Map<String, String> validPseudoRequest = analyzer.getRandomValidRequest();
        assertTrue(analyzer.isValidRequest(validPseudoRequest), "The request should be VALID");

        Map<String, String> invalidRequest = new HashMap<>();
        invalidRequest.put("p1", "false");
        invalidRequest.put("p2", "false");
        invalidRequest.put("p4", "true");
        invalidRequest.put("p5", "true");
        invalidRequest.put("p6", "one string");
        invalidRequest.put("p8", "something"); // (See next comment)
        invalidRequest.put("p9", "fixed string");
        invalidRequest.put("p10", "something different from p8"); // Violates this dependency: AllOrNone(p6!=p8, p8==p10);
        assertFalse(analyzer.isValidRequest(invalidRequest), "The request should be NOT valid");

        Map<String, String> validRandomRequest2 = analyzer.getRandomValidRequest();
        assertTrue(analyzer.isValidRequest(validRandomRequest2), "The request should be VALID");

        Map<String, String> partiallyValidRequest = new HashMap<>();
        partiallyValidRequest.put("p1", "false");
        partiallyValidRequest.put("p2", "false");
        partiallyValidRequest.put("p4", "true");
        partiallyValidRequest.put("p6", "one string");
        partiallyValidRequest.put("p8", "something");
        partiallyValidRequest.put("p9", "fixed string");
        partiallyValidRequest.put("p10", "something");
        assertTrue(analyzer.isValidPartialRequest(partiallyValidRequest), "The partial request should be VALID");

        Map<String, String> partiallyInvalidRequest = new HashMap<>();
        partiallyInvalidRequest.put("p1", "false");
        partiallyInvalidRequest.put("p7", "a string"); // Violates this dependency: AllOrNone(p6!=p8, p8==p10);
        assertFalse(analyzer.isValidPartialRequest(partiallyInvalidRequest), "The partial request should be NOT valid");

        System.out.println("Test passed: multipleOperationsTest.");
    }

    @Test
    public void randomValidRequestCustomDataTest() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial8", "get");

        List<String> p1Data = Arrays.asList("true", "false");
        List<String> p2Data = Arrays.asList("true", "false");
        List<String> p3Data = Arrays.asList("true", "false");
        List<String> p4Data = Arrays.asList("true", "false");
        List<String> p5Data = Arrays.asList("true", "false");
        List<String> p6Data = Arrays.asList("a", "b", "c", "d", "e");
        List<String> p7Data = Arrays.asList("a", "b", "c", "d", "e", "f", "g");
        List<String> p8Data = Arrays.asList("z", "f", "g", "h");
        List<String> p9Data = Arrays.asList("a", "b", "c", "d", "e", "f", "g");
        List<String> p10Data = Arrays.asList("z", "f", "g", "h");
        Map <String, List<String>> inputData = new HashMap<>();
        inputData.put("p1", p1Data);
        inputData.put("p2", p2Data);
        inputData.put("p3", p3Data);
        inputData.put("p4", p4Data);
        inputData.put("p5", p5Data);
        inputData.put("p6", p6Data);
        inputData.put("p7", p7Data);
        inputData.put("p8", p8Data);
        inputData.put("p9", p9Data);
        inputData.put("p10", p10Data);
        analyzer.updateData(inputData);
        Map<String, String> validRandomRequest = analyzer.getRandomValidRequest();
        assertTrue(analyzer.isValidRequest(validRandomRequest), "The request should be VALID");
        if (validRandomRequest.get("p6") != null)
            assertTrue(p6Data.contains(validRandomRequest.get("p6")));
        if (validRandomRequest.get("p7") != null)
            assertTrue(p7Data.contains(validRandomRequest.get("p7")));
        if (validRandomRequest.get("p8") != null)
            assertTrue(p8Data.contains(validRandomRequest.get("p8")));
        if (validRandomRequest.get("p9") != null)
            assertTrue(p9Data.contains(validRandomRequest.get("p9")));
        if (validRandomRequest.get("p10") != null)
            assertTrue(p10Data.contains(validRandomRequest.get("p10")));
    }

    @Test
    public void defaultAndNonDefaultDataIsValidRequestTest() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial8", "get");

        Map<String, String> validRandomRequestBeforeUpdateData = analyzer.getRandomValidRequest();
        assertTrue(analyzer.isValidRequest(validRandomRequestBeforeUpdateData), "The request should be VALID");

        // Update data
        List<String> p1Data = Arrays.asList("true", "false");
        List<String> p2Data = Arrays.asList("true", "false");
        List<String> p3Data = Arrays.asList("true", "false");
        List<String> p4Data = Arrays.asList("true", "false");
        List<String> p5Data = Arrays.asList("true", "false");
        List<String> p6Data = Arrays.asList("a", "b", "c", "d", "e");
        List<String> p7Data = Arrays.asList("a", "b", "c", "d", "e", "f", "g");
        List<String> p8Data = Arrays.asList("z", "f", "g", "h");
        List<String> p9Data = Arrays.asList("a", "b", "c", "d", "e", "f", "g");
        List<String> p10Data = Arrays.asList("z", "f", "g", "h");
        Map <String, List<String>> inputData = new HashMap<>();
        inputData.put("p1", p1Data);
        inputData.put("p2", p2Data);
        inputData.put("p3", p3Data);
        inputData.put("p4", p4Data);
        inputData.put("p5", p5Data);
        inputData.put("p6", p6Data);
        inputData.put("p7", p7Data);
        inputData.put("p8", p8Data);
        inputData.put("p9", p9Data);
        inputData.put("p10", p10Data);
        analyzer.updateData(inputData);

        assertFalse(analyzer.isValidRequest(validRandomRequestBeforeUpdateData), "The data was updated and, according to it, the request should be invalid");
        analyzer.updateData(null);
        assertTrue(analyzer.isValidRequest(validRandomRequestBeforeUpdateData), "With the default data, the request should be valid");
        
        analyzer.updateData(inputData);
        Map<String, String> validRandomRequestAfterUpdateData = analyzer.getRandomValidRequest();
        assertTrue(analyzer.isValidRequest(validRandomRequestAfterUpdateData), "The request should be VALID");
        assertTrue(analyzer.isValidRequest(validRandomRequestAfterUpdateData), "The request should be VALID");
        if (validRandomRequestAfterUpdateData.get("p6") != null)
            assertTrue(p6Data.contains(validRandomRequestAfterUpdateData.get("p6")));
        if (validRandomRequestAfterUpdateData.get("p7") != null)
            assertTrue(p7Data.contains(validRandomRequestAfterUpdateData.get("p7")));
        if (validRandomRequestAfterUpdateData.get("p8") != null)
            assertTrue(p8Data.contains(validRandomRequestAfterUpdateData.get("p8")));
        if (validRandomRequestAfterUpdateData.get("p9") != null)
            assertTrue(p9Data.contains(validRandomRequestAfterUpdateData.get("p9")));
        if (validRandomRequestAfterUpdateData.get("p10") != null)
            assertTrue(p10Data.contains(validRandomRequestAfterUpdateData.get("p10")));
    }

    @Test
    public void randomInvalidRequestCustomDataTest() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial8", "get");

        List<String> p1Data = Arrays.asList("true", "false");
        List<String> p2Data = Arrays.asList("true", "false");
        List<String> p3Data = Arrays.asList("true", "false");
        List<String> p4Data = Arrays.asList("true", "false");
        List<String> p5Data = Arrays.asList("true", "false");
        List<String> p6Data = Arrays.asList("a", "b", "c", "d", "e");
        List<String> p7Data = Arrays.asList("a", "b", "c", "d", "e", "f", "g");
        List<String> p8Data = Arrays.asList("z", "f", "g", "h");
        List<String> p9Data = Arrays.asList("a", "b", "c", "d", "e", "f", "g");
        List<String> p10Data = Arrays.asList("z", "f", "g", "h");
        Map <String, List<String>> inputData = new HashMap<>();
        inputData.put("p1", p1Data);
        inputData.put("p2", p2Data);
        inputData.put("p3", p3Data);
        inputData.put("p4", p4Data);
        inputData.put("p5", p5Data);
        inputData.put("p6", p6Data);
        inputData.put("p7", p7Data);
        inputData.put("p8", p8Data);
        inputData.put("p9", p9Data);
        inputData.put("p10", p10Data);
        analyzer.updateData(inputData);
        Map<String, String> invalidRandomRequest = analyzer.getRandomInvalidRequest();
        assertFalse(analyzer.isValidRequest(invalidRandomRequest), "The request should be NOT valid");
        if (invalidRandomRequest.get("p6") != null)
            assertTrue(p6Data.contains(invalidRandomRequest.get("p6")));
        if (invalidRandomRequest.get("p7") != null)
            assertTrue(p7Data.contains(invalidRandomRequest.get("p7")));
        if (invalidRandomRequest.get("p8") != null)
            assertTrue(p8Data.contains(invalidRandomRequest.get("p8")));
        if (invalidRandomRequest.get("p9") != null)
            assertTrue(p9Data.contains(invalidRandomRequest.get("p9")));
        if (invalidRandomRequest.get("p10") != null)
            assertTrue(p10Data.contains(invalidRandomRequest.get("p10")));
    }

    @Test
    public void customDataTwiceTestValidRequest() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial8", "get");

        List<String> p1Data = Arrays.asList("true", "false");
        List<String> p2Data = Arrays.asList("true", "false");
        List<String> p3Data = Arrays.asList("true", "false");
        List<String> p4Data = Arrays.asList("true", "false");
        List<String> p5Data = Arrays.asList("true", "false");
        List<String> p6Data = Arrays.asList("a", "f", "example");
        List<String> p7Data = Arrays.asList("b", "f");
        List<String> p8Data = Arrays.asList("c", "f", "something");
        List<String> p9Data = Arrays.asList("d", "f", "fixed string");
        List<String> p10Data = Arrays.asList("e", "f", "example");
        Map <String, List<String>> inputData = new HashMap<>();
        inputData.put("p1", p1Data);
        inputData.put("p2", p2Data);
        inputData.put("p3", p3Data);
        inputData.put("p4", p4Data);
        inputData.put("p5", p5Data);
        inputData.put("p6", p6Data);
        inputData.put("p7", p7Data);
        inputData.put("p8", p8Data);
        inputData.put("p9", p9Data);
        inputData.put("p10", p10Data);
        analyzer.updateData(inputData);
        Map<String, String> validRandomRequest = analyzer.getRandomValidRequest();

        List<String> p1Data2 = Arrays.asList("true", "false");
        List<String> p2Data2 = Arrays.asList("true", "false");
        List<String> p3Data2 = Arrays.asList("true", "false");
        List<String> p4Data2 = Arrays.asList("true", "false");
        List<String> p5Data2 = Arrays.asList("true", "false");
        List<String> p6Data2 = Arrays.asList("z", "u");
        List<String> p7Data2 = Arrays.asList("y", "u");
        List<String> p8Data2 = Arrays.asList("x", "u");
        List<String> p9Data2 = Arrays.asList("w", "u");
        List<String> p10Data2 = Arrays.asList("v", "u");
        Map <String, List<String>> inputData2 = new HashMap<>();
        inputData2.put("p1", p1Data2);
        inputData2.put("p2", p2Data2);
        inputData2.put("p3", p3Data2);
        inputData2.put("p4", p4Data2);
        inputData2.put("p5", p5Data2);
        inputData2.put("p6", p6Data2);
        inputData2.put("p7", p7Data2);
        inputData2.put("p8", p8Data2);
        inputData2.put("p9", p9Data2);
        inputData2.put("p10", p10Data2);
        analyzer.updateData(inputData2);
        Map<String, String> validRandomRequest2 = analyzer.getRandomValidRequest();

        if (validRandomRequest.get("p6") != null || validRandomRequest2.get("p6") != null)
            assertNotEquals(validRandomRequest.get("p6"), validRandomRequest2.get("p6"));
        if (validRandomRequest.get("p7") != null || validRandomRequest2.get("p7") != null)
            assertNotEquals(validRandomRequest.get("p7"), validRandomRequest2.get("p7"));
        if (validRandomRequest.get("p8") != null || validRandomRequest2.get("p8") != null)
            assertNotEquals(validRandomRequest.get("p8"), validRandomRequest2.get("p8"));
        if (validRandomRequest.get("p9") != null || validRandomRequest2.get("p9") != null)
            assertNotEquals(validRandomRequest.get("p9"), validRandomRequest2.get("p9"));
        if (validRandomRequest.get("p10") != null || validRandomRequest2.get("p10") != null)
            assertNotEquals(validRandomRequest.get("p10"), validRandomRequest2.get("p10"));
    }

    @Test
    public void customDataTwiceTestInvalidRequest() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial8", "get");

        List<String> p1Data = Arrays.asList("true", "false");
        List<String> p2Data = Arrays.asList("true", "false");
        List<String> p3Data = Arrays.asList("true", "false");
        List<String> p4Data = Arrays.asList("true", "false");
        List<String> p5Data = Arrays.asList("true", "false");
        List<String> p6Data = Arrays.asList("a", "f", "example");
        List<String> p7Data = Arrays.asList("b", "f");
        List<String> p8Data = Arrays.asList("c", "f", "something");
        List<String> p9Data = Arrays.asList("d", "f", "fixed string");
        List<String> p10Data = Arrays.asList("e", "f", "example");
        Map <String, List<String>> inputData = new HashMap<>();
        inputData.put("p1", p1Data);
        inputData.put("p2", p2Data);
        inputData.put("p3", p3Data);
        inputData.put("p4", p4Data);
        inputData.put("p5", p5Data);
        inputData.put("p6", p6Data);
        inputData.put("p7", p7Data);
        inputData.put("p8", p8Data);
        inputData.put("p9", p9Data);
        inputData.put("p10", p10Data);
        analyzer.updateData(inputData);
        Map<String, String> invalidRandomRequest = analyzer.getRandomInvalidRequest();

        List<String> p1Data2 = Arrays.asList("true", "false");
        List<String> p2Data2 = Arrays.asList("true", "false");
        List<String> p3Data2 = Arrays.asList("true", "false");
        List<String> p4Data2 = Arrays.asList("true", "false");
        List<String> p5Data2 = Arrays.asList("true", "false");
        List<String> p6Data2 = Arrays.asList("z", "u");
        List<String> p7Data2 = Arrays.asList("y", "u");
        List<String> p8Data2 = Arrays.asList("x", "u");
        List<String> p9Data2 = Arrays.asList("w", "u");
        List<String> p10Data2 = Arrays.asList("v", "u");
        Map <String, List<String>> inputData2 = new HashMap<>();
        inputData2.put("p1", p1Data2);
        inputData2.put("p2", p2Data2);
        inputData2.put("p3", p3Data2);
        inputData2.put("p4", p4Data2);
        inputData2.put("p5", p5Data2);
        inputData2.put("p6", p6Data2);
        inputData2.put("p7", p7Data2);
        inputData2.put("p8", p8Data2);
        inputData2.put("p9", p9Data2);
        inputData2.put("p10", p10Data2);
        analyzer.updateData(inputData2);
        Map<String, String> invalidRandomRequest2 = analyzer.getRandomInvalidRequest();

        if (invalidRandomRequest.get("p6") != null || invalidRandomRequest2.get("p6") != null)
            assertNotEquals(invalidRandomRequest.get("p6"), invalidRandomRequest2.get("p6"));
        if (invalidRandomRequest.get("p7") != null || invalidRandomRequest2.get("p7") != null)
            assertNotEquals(invalidRandomRequest.get("p7"), invalidRandomRequest2.get("p7"));
        if (invalidRandomRequest.get("p8") != null || invalidRandomRequest2.get("p8") != null)
            assertNotEquals(invalidRandomRequest.get("p8"), invalidRandomRequest2.get("p8"));
        if (invalidRandomRequest.get("p9") != null || invalidRandomRequest2.get("p9") != null)
            assertNotEquals(invalidRandomRequest.get("p9"), invalidRandomRequest2.get("p9"));
        if (invalidRandomRequest.get("p10") != null || invalidRandomRequest2.get("p10") != null)
            assertNotEquals(invalidRandomRequest.get("p10"), invalidRandomRequest2.get("p10"));
    }

    @Test
    public void conflictiveParameterNamesTest1() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/conflictiveParameterNames", "get");

        assertFalse(analyzer.isDeadParameter("type"), "The parameter type should NOT be dead");
        assertFalse(analyzer.isDeadParameter("constraint"), "The parameter constraint should NOT be dead");
        assertFalse(analyzer.isDeadParameter("with_underscore"), "The parameter with_underscore should NOT be dead");
        assertFalse(analyzer.isDeadParameter("Accept-Language"), "The parameter Accept-Language should NOT be dead");
        assertFalse(analyzer.isDeadParameter("index:set"), "The parameter index:set should NOT be dead");
        assertFalse(analyzer.isDeadParameter("something[one]"), "The parameter something[one] should NOT be dead");
        assertFalse(analyzer.isDeadParameter("something[two]"), "The parameter something[two] should NOT be dead");
        assertFalse(analyzer.isDeadParameter("b.b"), "The parameter b.b should NOT be dead");
        assertFalse(analyzer.isDeadParameter("p9"), "The parameter p9 should NOT be dead");
        assertFalse(analyzer.isDeadParameter("p10"), "The parameter p10 should NOT be dead");

        Map<String, String> validRequest = new HashMap<>();
        validRequest.put("type", "false");
        validRequest.put("constraint", "false");
        validRequest.put("Accept-Language", "true");
        validRequest.put("index:set", "true");
        validRequest.put("something[one]", "one string");
        validRequest.put("b.b", "something");
        validRequest.put("p9", "fixed string");
        validRequest.put("p10", "something");
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");

        assertFalse(analyzer.isFalseOptional("type"), "The parameter type should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("constraint"), "The parameter constraint should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("with_underscore"), "The parameter with_underscore should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("Accept-Language"), "The parameter Accept-Language should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("index:set"), "The parameter index:set should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("something[one]"), "The parameter something[one] should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("something[two]"), "The parameter something[two] should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("b.b"), "The parameter b.b should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p9"), "The parameter p9 should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p10"), "The parameter p10 should NOT be false optional");

        Map<String, String> validRandomRequest = analyzer.getRandomValidRequest();
        assertTrue(analyzer.isValidRequest(validRandomRequest), "The request should be VALID");

        assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");

        Map<String, String> invalidRandomRequest = analyzer.getRandomInvalidRequest();
        assertFalse(analyzer.isValidRequest(invalidRandomRequest), "The request should be NOT valid");

        Map<String, String> validPseudoRequest = analyzer.getRandomValidRequest();
        assertTrue(analyzer.isValidRequest(validPseudoRequest), "The request should be VALID");

        Map<String, String> invalidRequest = new HashMap<>();
        invalidRequest.put("type", "false");
        invalidRequest.put("constraint", "false");
        invalidRequest.put("Accept-Language", "true");
        invalidRequest.put("index:set", "true");
        invalidRequest.put("something[one]", "one string");
        invalidRequest.put("b.b", "something"); // (See next comment)
        invalidRequest.put("p9", "fixed string");
        invalidRequest.put("p10", "something different from b.b"); // Violates this dependency: AllOrNone(something[one]!=b.b, b.b==p10);
        assertFalse(analyzer.isValidRequest(invalidRequest), "The request should be NOT valid");

        Map<String, String> validRandomRequest2 = analyzer.getRandomValidRequest();
        assertTrue(analyzer.isValidRequest(validRandomRequest2), "The request should be VALID");

        Map<String, String> partiallyValidRequest = new HashMap<>();
        partiallyValidRequest.put("type", "false");
        partiallyValidRequest.put("constraint", "false");
        partiallyValidRequest.put("Accept-Language", "true");
        partiallyValidRequest.put("something[one]", "one string");
        partiallyValidRequest.put("b.b", "something");
        partiallyValidRequest.put("p9", "fixed string");
        partiallyValidRequest.put("p10", "something");
        assertTrue(analyzer.isValidPartialRequest(partiallyValidRequest), "The partial request should be VALID");

        Map<String, String> partiallyInvalidRequest = new HashMap<>();
        partiallyInvalidRequest.put("type", "false");
        partiallyInvalidRequest.put("something[two]", "a string"); // Violates this dependency: IF type THEN (with_underscore==true OR (NOT with_underscore)) AND NOT [something[two]] AND p9=='fixed string';
        assertFalse(analyzer.isValidPartialRequest(partiallyInvalidRequest), "The partial request should be NOT valid");

        System.out.println("Test passed: multipleOperationsTest.");
    }

    @Test
    public void conflictiveParameterNamesTest2() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/conflictiveParameterNames2", "get");

        assertFalse(analyzer.isDeadParameter("type"), "The parameter type should NOT be dead");
        assertFalse(analyzer.isDeadParameter("constraint"), "The parameter constraint should NOT be dead");
        assertFalse(analyzer.isDeadParameter("with_underscore"), "The parameter with_underscore should NOT be dead");
        assertFalse(analyzer.isDeadParameter("Accept-Language"), "The parameter Accept-Language should NOT be dead");
        assertFalse(analyzer.isDeadParameter("index:set"), "The parameter index:set should NOT be dead");
        assertFalse(analyzer.isDeadParameter("something[one]"), "The parameter something[one] should NOT be dead");
        assertFalse(analyzer.isDeadParameter("something[two]"), "The parameter something[two] should NOT be dead");
        assertFalse(analyzer.isDeadParameter("b.b"), "The parameter b.b should NOT be dead");
        assertFalse(analyzer.isDeadParameter("c.c"), "The parameter c.c should NOT be dead");
        assertFalse(analyzer.isDeadParameter("p10"), "The parameter p10 should NOT be dead");

        Map<String, String> validRequest = new HashMap<>();
        validRequest.put("type", "false");
        validRequest.put("constraint", "false");
        validRequest.put("Accept-Language", "true");
        validRequest.put("index:set", "true");
        validRequest.put("something[one]", "one string");
        validRequest.put("b.b", "something");
        validRequest.put("c.c", "fixed string");
        validRequest.put("p10", "something");
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");

        assertFalse(analyzer.isFalseOptional("type"), "The parameter type should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("constraint"), "The parameter constraint should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("with_underscore"), "The parameter with_underscore should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("Accept-Language"), "The parameter Accept-Language should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("index:set"), "The parameter index:set should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("something[one]"), "The parameter something[one] should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("something[two]"), "The parameter something[two] should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("b.b"), "The parameter b.b should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("c.c"), "The parameter c.c should NOT be false optional");
        assertFalse(analyzer.isFalseOptional("p10"), "The parameter p10 should NOT be false optional");

        Map<String, String> validRandomRequest = analyzer.getRandomValidRequest();
        assertTrue(analyzer.isValidRequest(validRandomRequest), "The request should be VALID");

        assertTrue(analyzer.isValidIDL(), "The IDL should be VALID");

        Map<String, String> invalidRandomRequest = analyzer.getRandomInvalidRequest();
        assertFalse(analyzer.isValidRequest(invalidRandomRequest), "The request should be NOT valid");

        Map<String, String> validPseudoRequest = analyzer.getRandomValidRequest();
        assertTrue(analyzer.isValidRequest(validPseudoRequest), "The request should be VALID");

        Map<String, String> invalidRequest = new HashMap<>();
        invalidRequest.put("type", "false");
        invalidRequest.put("constraint", "false");
        invalidRequest.put("Accept-Language", "true");
        invalidRequest.put("index:set", "true");
        invalidRequest.put("something[one]", "one string");
        invalidRequest.put("b.b", "something"); // (See next comment)
        invalidRequest.put("c.c", "fixed string");
        invalidRequest.put("p10", "something different from b.b"); // Violates this dependency: AllOrNone(something[one]!=b.b, b.b==p10);
        assertFalse(analyzer.isValidRequest(invalidRequest), "The request should be NOT valid");

        Map<String, String> validRandomRequest2 = analyzer.getRandomValidRequest();
        assertTrue(analyzer.isValidRequest(validRandomRequest2), "The request should be VALID");

        Map<String, String> partiallyValidRequest = new HashMap<>();
        partiallyValidRequest.put("type", "false");
        partiallyValidRequest.put("constraint", "false");
        partiallyValidRequest.put("Accept-Language", "true");
        partiallyValidRequest.put("something[one]", "one string");
        partiallyValidRequest.put("b.b", "something");
        partiallyValidRequest.put("c.c", "fixed string");
        partiallyValidRequest.put("p10", "something");
        assertTrue(analyzer.isValidPartialRequest(partiallyValidRequest), "The partial request should be VALID");

        Map<String, String> partiallyInvalidRequest = new HashMap<>();
        partiallyInvalidRequest.put("type", "false");
        partiallyInvalidRequest.put("something[two]", "a string"); // Violates this dependency: IF type THEN (with_underscore==true OR (NOT with_underscore)) AND NOT [something[two]] AND p9=='fixed string';
        assertFalse(analyzer.isValidPartialRequest(partiallyInvalidRequest), "The partial request should be NOT valid");

        System.out.println("Test passed: multipleOperationsTest.");
    }
    

    @ParameterizedTest
    @ValueSource(strings = {"noParams", "oneParamBoolean", "oneDependencyRequires", "oneDependencyOr", "oneDependencyOnlyOne",
            "oneDependencyAllOrNone", "oneDependencyZeroOrOne", "oneDependencyArithRel", "oneDependencyComplex"})
    void randomValidRequestTest(String operation) throws IDLException {
        Analyzer analyzer = new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/" + operation, "get");
        Map<String, String> validRequest = analyzer.getRandomValidRequest();
        assertTrue(analyzer.isValidRequest(validRequest), "The request should be VALID");
        System.out.println("Test passed: " + operation);
    }

    @Test
    public void updateDataTest() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/oneDependencyEnumAndIntParams", "get");

        List<String> p1Data = Collections.singletonList("true");
        List<String> p2Data = new ArrayList<>();
        List<String> p3Data = Collections.singletonList("1");
        Map <String, List<String>> inputData = new HashMap<>();
        inputData.put("p1", p1Data);
        inputData.put("p2", p2Data);
        inputData.put("p3", p3Data);
        analyzer.updateData(inputData);

        Map<String, String> validRequest = analyzer.getRandomValidRequest();

        assertNull(validRequest.get("p2"), "Parameter p2 must be null");
        assertEquals("true", validRequest.get("p1"), "Parameter p1 can only be 'true'");
        assertEquals("1", validRequest.get("p3"), "Parameter p3 can only be '1'");

        inputData.remove("p2");
        analyzer.updateData(inputData);

        validRequest = analyzer.getRandomValidRequest();

        assertNull(validRequest.get("p2"), "Parameter p2 must be null");
        assertEquals("true", validRequest.get("p1"), "Parameter p1 can only be 'true'");
        assertEquals("1", validRequest.get("p3"), "Parameter p3 can only be '1'");
    }

    @Test
    public void problematicYelpDependencies() throws IDLException {
        assertTimeoutPreemptively(Duration.ofSeconds(30), () ->{
            Analyzer analyzer = new OASAnalyzer("./src/test/resources/yelp.yaml", "/businesses/search", "get");

            Map<String, List<String>> inputData = new HashMap<>();
            inputData.put("offset", Arrays.asList("930", "391", "87", "598", "199", "798", "945", "515", "601", "916"));
            inputData.put("latitude", Arrays.asList("41.4248", "47.090492", "36.76126", "41.559193", "34.588825", "40.88466", "35.54669", "42.216522", "40.81036", "43.965572"));
            inputData.put("sort_by", Arrays.asList("best_match", "rating", "review_count", "distance"));
            inputData.put("locale", Arrays.asList("cs_CZ", "da_DK", "de_AT", "de_CH", "de_DE", "en_AU", "en_BE", "en_CA", "en_CH", "en_GB", "en_HK", "en_IE", "en_MY", "en_NZ", "en_PH", "en_SG", "en_US", "es_AR", "es_CL", "es_ES", "es_MX", "fi_FI", "fil_PH", "fr_BE", "fr_CA", "fr_CH", "fr_FR", "it_CH", "it_IT", "ja_JP", "ms_MY", "nb_NO", "nl_BE", "nl_NL", "pl_PL", "pt_BR", "pt_PT", "sv_FI", "sv_SE", "tr_TR", "zh_HK", "zh_TW"));
            inputData.put("open_now", Arrays.asList("false", "true"));
            inputData.put("price", Arrays.asList("3,4", "4,1,2", "1,2,3,4", "4", "3", "2,3", "1,3,2,4"));
            inputData.put("limit", Arrays.asList("9", "4", "19", "14", "43", "11", "26", "46", "21", "39"));
            inputData.put("term", Arrays.asList("food", "cinema", "open", "cheap", "public", "exciting", "cafe", "ok", "the", "watch"));
            inputData.put("location", Arrays.asList("NYC", "Seville, Spain", "Milan, Italy", "Melbourne", "Tokyo", "Egypt", "Juarez, Mexico", "Paris", "San Francisco", "Krakow", "Daca, Bangladesh", "Santa Fe"));
            inputData.put("attributes", Arrays.asList("waitlist_reservation,deals", "deals", "request_a_quote", "gender_neutral_restrooms,hot_and_new,open_to_all", "wheelchair_accessible", "request_a_quote,open_to_all", "request_a_quote,waitlist_reservation"));
            inputData.put("categories", Arrays.asList("driveintheater,qigong", "trafficticketinglaw,occupationaltherapy,ateliers,driving_schools,drugstores", "suppliesrestaurant,homeappliancerepair,surfshop", "driveintheater", "commissionedartists", "taekwondo", "afrobrazilian", "apartmentagents", "african,lasertag", "kids_activities,truckrepair"));
            inputData.put("radius", Arrays.asList("29782", "15173", "17616", "10720", "23468", "6164", "35502", "25428", "14380", "28849"));
            inputData.put("open_at", Arrays.asList("2103623019", "1647379796", "1758687139", "1968886206", "1730905071", "1966451543", "2027034146", "1677754192", "2081502156", "1767117931"));
            inputData.put("longitude", Arrays.asList("-90.311646", "-114.955315", "-106.71193", "-91.312546", "-96.45189", "-95.25761", "-111.34424", "-99.32146", "-114.827", "-117.82459"));

            analyzer.updateData(inputData);

            for (int i = 0; i < 100; i++) {
                // Dead parameter
                assertFalse(analyzer.isDeadParameter("offset"));
                assertFalse(analyzer.isDeadParameter("latitude"));
                assertFalse(analyzer.isDeadParameter("sort_by"));
                assertFalse(analyzer.isDeadParameter("locale"));
                assertFalse(analyzer.isDeadParameter("open_now"));
                assertFalse(analyzer.isDeadParameter("price"));
                assertFalse(analyzer.isDeadParameter("limit"));
                assertFalse(analyzer.isDeadParameter("term"));
                assertFalse(analyzer.isDeadParameter("location"));
                assertFalse(analyzer.isDeadParameter("attributes"));
                assertFalse(analyzer.isDeadParameter("categories"));
                assertFalse(analyzer.isDeadParameter("radius"));
                assertFalse(analyzer.isDeadParameter("open_at"));
                assertFalse(analyzer.isDeadParameter("longitude"));

                // False optional parameter
                assertFalse(analyzer.isFalseOptional("offset"));
                assertFalse(analyzer.isFalseOptional("latitude"));
                assertFalse(analyzer.isFalseOptional("sort_by"));
                assertFalse(analyzer.isFalseOptional("locale"));
                assertFalse(analyzer.isFalseOptional("open_now"));
                assertFalse(analyzer.isFalseOptional("price"));
                assertFalse(analyzer.isFalseOptional("limit"));
                assertFalse(analyzer.isFalseOptional("term"));
                assertFalse(analyzer.isFalseOptional("location"));
                assertFalse(analyzer.isFalseOptional("attributes"));
                assertFalse(analyzer.isFalseOptional("categories"));
                assertFalse(analyzer.isFalseOptional("radius"));
                assertFalse(analyzer.isFalseOptional("open_at"));
                assertFalse(analyzer.isFalseOptional("longitude"));

                // Consistent
                assertTrue(analyzer.isConsistent());

                // Valid IDL
                assertTrue(analyzer.isValidIDL());

                // Random valid request
                Map<String, String> validRequest = analyzer.getRandomValidRequest();

                // Random invalid request
                Map<String, String> invalidRequest = analyzer.getRandomInvalidRequest();

                // Valid partial request
                assertTrue(analyzer.isValidPartialRequest(validRequest));

                // Valid request
                assertTrue(analyzer.isValidRequest(validRequest));
                assertFalse(analyzer.isValidRequest(invalidRequest));

//                System.out.println(i);
            }
        });
    }

    @Test
    public void updateDataOutOfRangeEnumsForNumbers() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/numberAndIntegerParams", "get");

        List<String> p1Data = Arrays.asList("true", "false");
        List<String> p2Data = Arrays.asList("1", "2", "3");
        List<String> p3Data = Arrays.asList("6");
        List<String> p4Data = Arrays.asList("1.3", "-5.34452");
        List<String> p5Data = Arrays.asList("hi", "hey");
        Map <String, List<String>> inputData = new HashMap<>();
        inputData.put("p1", p1Data);
        inputData.put("p2", p2Data);
        inputData.put("p3", p3Data);
        inputData.put("p4", p4Data);
        inputData.put("p5", p5Data);

        analyzer.updateData(inputData);

        Map<String, String> validRequest = analyzer.getRandomValidRequest();

        assertEquals("6", validRequest.get("p3"));

        System.out.println("Test passed: updateDataOutOfRangeEnumsForNumbers");
    }

    @Test
    public void updateDataInvalidValuesForNumbersAndBooleans() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/numberAndIntegerParams", "get");

        List<String> p1Data = Arrays.asList("true", "error");
        List<String> p2Data = Arrays.asList("2.4");
        List<String> p3Data = Arrays.asList("6");
        List<String> p4Data = Arrays.asList("3.3", "not a double");
        List<String> p5Data = Arrays.asList("hi", "hey");
        Map <String, List<String>> inputData = new HashMap<>();
        inputData.put("p1", p1Data);
        inputData.put("p2", p2Data);
        inputData.put("p3", p3Data);
        inputData.put("p4", p4Data);
        inputData.put("p5", p5Data);

        analyzer.updateData(inputData);

        Map<String, String> validRequest = analyzer.getRandomValidRequest();

        assertEquals("true", validRequest.get("p1"));
        assertEquals("2", validRequest.get("p2"));
        assertEquals("6", validRequest.get("p3"));
        assertEquals("3", validRequest.get("p4"));

        System.out.println("Test passed: updateDataInvalidValuesForNumbersAndBooleans");
    }

    @Test
    public void updateDataVeryHighNumber() throws IDLException {
        Analyzer analyzer = new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/numberAndIntegerParams", "get");

        List<String> p1Data = Arrays.asList("true", "error");
        List<String> p2Data = Arrays.asList("2.4", "2147483647");
        List<String> p3Data = Arrays.asList("6");
        List<String> p4Data = Arrays.asList("3.3", "not a double", "3458372835748375632523853");
        List<String> p5Data = Arrays.asList("hi", "hey");
        Map <String, List<String>> inputData = new HashMap<>();
        inputData.put("p1", p1Data);
        inputData.put("p2", p2Data);
        inputData.put("p3", p3Data);
        inputData.put("p4", p4Data);
        inputData.put("p5", p5Data);

        analyzer.updateData(inputData);

        Map<String, String> validRequest = analyzer.getRandomValidRequest();

        assertEquals("true", validRequest.get("p1"));
        assertEquals("2", validRequest.get("p2"));
        assertEquals("6", validRequest.get("p3"));
        assertEquals("3", validRequest.get("p4"));

        System.out.println("Test passed: updateDataVeryHighNumber");
    }

//    @Test
//    public void loopInvalid() throws IDLException {
//        Analyzer analyzer = new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial3", "get");
//        for (int i=0; i<=1000; i++) {
//            Map<String, String> invalidRequest = analyzer.getRandomInvalidRequest();
//            // System.out.println(invalidRequest);
//            assertFalse(analyzer.isValidRequest(invalidRequest), "The request should be NOT valid");
//        }
//        System.out.println("Test passed: combinatorial3.");
//    }
//
//    @Test
//    public void loopValid() throws IDLException {
//        Analyzer analyzer = new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial3", "get");
//        for (int i=0; i<=1000; i++) {
//            Map<String, String> validRequest = analyzer.getRandomValidRequest();
//            // System.out.println(invalidRequest);
//            assertFalse(!analyzer.isValidRequest(validRequest), "The request should be valid");
//        }
//        System.out.println("Test passed: combinatorial3.");
//    }
//
//    @Test
//    public void loopValidInvalid() throws IDLException {
//        Analyzer analyzer = new OASAnalyzer("./src/test/resources/OAS_test_suite_orig.yaml", "/combinatorial3", "get");
//        for (int i=0; i<=1000; i++) {
//            if (i%2==0) {
//                Map<String, String> validRequest = analyzer.getRandomValidRequest();
//                // System.out.println(invalidRequest);
//                assertFalse(!analyzer.isValidRequest(validRequest), "The request should be valid");
//            } else {
//                Map<String, String> invalidRequest = analyzer.getRandomInvalidRequest();
//                // System.out.println(invalidRequest);
//                assertFalse(analyzer.isValidRequest(invalidRequest), "The request should be NOT valid");
//            }
//        }
//        System.out.println("Test passed: combinatorial3.");
//    }
}
