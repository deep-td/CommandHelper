/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.laytonsmith.core.functions;

import com.laytonsmith.abstraction.MCPlayer;
import com.laytonsmith.abstraction.MCServer;
import com.laytonsmith.core.Env;
import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.CBoolean;
import com.laytonsmith.core.constructs.CInt;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.exceptions.CancelCommandException;
import com.laytonsmith.core.exceptions.ConfigCompileException;
import com.laytonsmith.testing.C;
import static com.laytonsmith.testing.StaticTest.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author Layton
 */
public class BasicLogicTest {

    MCPlayer fakePlayer;
    MCServer fakeServer;
    CArray commonArray;
    CInt arg1_1;
    CInt arg1_2;
    CInt arg2_1;
    CInt argn1_1;
    CInt argn2_1;
    CBoolean _true;
    CBoolean _false;
    Env env = new Env();

    public BasicLogicTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        commonArray = C.Array(C.Null(), C.Int(1), C.String("2"), C.Double(3.0));
        arg1_1 = C.Int(1);
        arg1_2 = C.Int(1);
        arg2_1 = C.Int(2);
        argn1_1 = C.Int(-1);
        argn2_1 = C.Int(-2);
        _true = C.Boolean(true);
        _false = C.Boolean(false);
        fakeServer = GetFakeServer();
        fakePlayer = GetOnlinePlayer(fakeServer);
        env.SetPlayer(fakePlayer);
    }

    @After
    public void tearDown() {
    }

    @Test(timeout = 10000)
    public void testDocs() {
        TestClassDocs(BasicLogic.docs(), BasicLogic.class);
    }

    @Test(timeout = 10000)
    public void testEquals() throws CancelCommandException {
        BasicLogic.equals e = new BasicLogic.equals();

//             T   F   1   0  -1  '1' '0' '-1' N  {} 'CH'  '' 1.0
//        ---------------------------------------------------
//        T    T   F   T   F   T   T   T   T   F   F   T   F   T
//        F    -   T   F   T   F   F   F   F   T   T   F   T   F
//        1    -   -   T   F   F   T   F   F   F   F   F   F   T
//        0    -   -   -   T   F   F   T   F   F   F   F   F   F
//        -1   -   -   -   -   T   F   F   T   F   F   F   F   F
//        '1'  -   -   -   -   -   T   F   F   F   F   F   F   T
//        '0'  -   -   -   -   -   -   T   F   F   F   F   F   F
//        '-1' -   -   -   -   -   -   -   T   F   F   F   F   F
//        N    -   -   -   -   -   -   -   -   T   F   F   F   F
//        {}   -   -   -   -   -   -   -   -   -   T   F   F   F
//        'CH' -   -   -   -   -   -   -   -   -   -   T   F   F
//        ''   -   -   -   -   -   -   -   -   -   -   -   T   F
//        1.0  -   -   -   -   -   -   -   -   -   -   -   -   T

        _t("false", "false");
        _f("false", "1");
        _t("false", "0");
        //TODO: Finish

        _t("true", "true");
        _f("true", "false");
        _t("true", "1");
        _f("true", "0");
        _t("true", "-1");
        _t("true", "'1'");
        _t("true", "'0'");
        _t("true", "'-1'");
        _f("true", "null");
        _f("true", "array()");
        _t("true", "'CH'");
        _f("true", "''");
        _t("true", "1.0");


    }

    @Test(timeout = 10000)
    public void testEqualsMulti() throws ConfigCompileException {
        assertEquals("true", SRun("equals(1, '1', 1.0)", fakePlayer));
        assertEquals("false", SRun("equals('blah', 'blah', 'blarg')", fakePlayer));
    }

    @Test(timeout = 10000)
    public void testEqualsICMulti() throws ConfigCompileException {
        assertEquals("true", SRun("equals_ic(1, '1', 1.0)", fakePlayer));
        assertEquals("false", SRun("equals_ic('blah', 'blah', 'blarg')", fakePlayer));
        assertEquals("true", SRun("equals_ic('blah', 'Blah', 'BLAH')", fakePlayer));
    }

    public void _t(String val1, String val2) {
        try {
            assertEquals("true", SRun("equals(" + val1 + ", " + val2 + ")", null));
        } catch (ConfigCompileException ex) {
            fail(ex.getMessage());
        }
    }

    public void _f(String val1, String val2) {
        try {
            assertEquals("false", SRun("equals(" + val1 + ", " + val2 + ")", null));
        } catch (ConfigCompileException ex) {
            fail(ex.getMessage());
        }
    }

    public void testEqualsIC() throws ConfigCompileException {
        SRun("if(equals_ic('hi', 'HI'), msg('pass'))", fakePlayer);
        SRun("if(equals_ic('hi', 'hi'), msg('pass'))", fakePlayer);
        SRun("if(not(equals_ic('what', 'hi')), msg('pass'))", fakePlayer);
        SRun("if(equals_ic(2, 2), msg('pass'))", fakePlayer);
        SRun("if(not(equals_ic(2, 'hi')), msg('pass'))", fakePlayer);
        verify(fakePlayer, times(5)).sendMessage("pass");
    }

    @Test(timeout = 10000)
    public void testAnd1() throws CancelCommandException, ConfigCompileException {
        SRun("if(and(true, true, true), msg(pass))", fakePlayer);
        SRun("if(and(true, true, false), '', msg(pass))", fakePlayer);
        SRun("if(and(true, true), msg(pass))", fakePlayer);
        SRun("if(and(true, false), '', msg(pass))", fakePlayer);
        SRun("if(and(false, false), '', msg(pass))", fakePlayer);
        SRun("if(and(true), msg(pass))", fakePlayer);
        SRun("if(and(false), '', msg(pass))", fakePlayer);
        verify(fakePlayer, times(7)).sendMessage("pass");
    }

    /**
     * Tests lazy evaluation
     *
     * @return
     * @throws ConfigCompileException
     */
    @Test(timeout = 10000)
    public void testAnd2() throws ConfigCompileException {
        SRun("and(false, msg(lol))", fakePlayer);
        verify(fakePlayer, times(0)).sendMessage("lol");
    }

    @Test(timeout = 10000)
    public void testOr1() throws CancelCommandException, ConfigCompileException {
        SRun("if(or(true, true, true), msg(pass))", fakePlayer);
        SRun("if(or(true, true, false), msg(pass))", fakePlayer);
        SRun("if(or(true, true), msg(pass))", fakePlayer);
        SRun("if(or(true, false), msg(pass))", fakePlayer);
        SRun("if(or(false, false), '', msg(pass))", fakePlayer);
        SRun("if(or(true), msg(pass))", fakePlayer);
        SRun("if(or(false), '', msg(pass))", fakePlayer);
        verify(fakePlayer, times(7)).sendMessage("pass");
    }

    @Test(timeout = 10000)
    public void testOr2() throws ConfigCompileException {
        SRun("or(true, msg(lol))", fakePlayer);
        verify(fakePlayer, times(0)).sendMessage("lol");
    }

    @Test(timeout = 10000)
    public void testNot() throws CancelCommandException {
        BasicLogic.not a = new BasicLogic.not();
        assertCFalse(a.exec(Target.UNKNOWN, env, _true));
        assertCTrue(a.exec(Target.UNKNOWN, env, _false));
    }

    @Test(timeout = 10000)
    public void testGt() throws CancelCommandException {
        BasicLogic.gt a = new BasicLogic.gt();
        assertCFalse(a.exec(Target.UNKNOWN, env, arg1_1, arg1_2));
        assertCTrue(a.exec(Target.UNKNOWN, env, arg2_1, arg1_1));
        assertCFalse(a.exec(Target.UNKNOWN, env, arg1_1, arg2_1));
        assertCFalse(a.exec(Target.UNKNOWN, env, argn1_1, arg1_1));
        assertCTrue(a.exec(Target.UNKNOWN, env, arg1_1, argn1_1));
    }

    @Test(timeout = 10000)
    public void testGte() throws CancelCommandException {
        BasicLogic.gte a = new BasicLogic.gte();
        assertCTrue(a.exec(Target.UNKNOWN, env, arg1_1, arg1_2));
        assertCTrue(a.exec(Target.UNKNOWN, env, arg2_1, arg1_1));
        assertCFalse(a.exec(Target.UNKNOWN, env, arg1_1, arg2_1));
        assertCFalse(a.exec(Target.UNKNOWN, env, argn1_1, arg1_1));
        assertCTrue(a.exec(Target.UNKNOWN, env, arg1_1, argn1_1));
    }

    @Test(timeout = 10000)
    public void testLt() throws CancelCommandException {
        BasicLogic.lt a = new BasicLogic.lt();
        assertCFalse(a.exec(Target.UNKNOWN, env, arg1_1, arg1_2));
        assertCFalse(a.exec(Target.UNKNOWN, env, arg2_1, arg1_1));
        assertCTrue(a.exec(Target.UNKNOWN, env, arg1_1, arg2_1));
        assertCTrue(a.exec(Target.UNKNOWN, env, argn1_1, arg1_1));
        assertCFalse(a.exec(Target.UNKNOWN, env, arg1_1, argn1_1));
    }

    @Test(timeout = 10000)
    public void testLte() throws CancelCommandException {
        BasicLogic.lte a = new BasicLogic.lte();
        assertCTrue(a.exec(Target.UNKNOWN, env, arg1_1, arg1_2));
        assertCFalse(a.exec(Target.UNKNOWN, env, arg2_1, arg1_1));
        assertCTrue(a.exec(Target.UNKNOWN, env, arg1_1, arg2_1));
        assertCTrue(a.exec(Target.UNKNOWN, env, argn1_1, arg1_1));
        assertCFalse(a.exec(Target.UNKNOWN, env, arg1_1, argn1_1));
    }

    @Test(timeout = 10000)
    public void testIf() throws ConfigCompileException {
        BasicLogic._if a = new BasicLogic._if();
        SRun("if(true, msg('correct'), msg('incorrect'))", fakePlayer);
        SRun("if(false, msg('incorrect'), msg('correct'))", fakePlayer);
        verify(fakePlayer, times(2)).sendMessage("correct");
    }

    @Test(timeout = 10000)
    public void testXor() throws ConfigCompileException {
        assertEquals("false", SRun("xor(false, false)", null));
        assertEquals("true", SRun("xor(false, true)", null));
        assertEquals("true", SRun("xor(true, false)", null));
        assertEquals("false", SRun("xor(true, true)", null));
    }

    @Test(timeout = 10000)
    public void testNand() throws ConfigCompileException {
        assertEquals("true", SRun("nand(false, false)", null));
        assertEquals("true", SRun("nand(false, true)", null));
        assertEquals("true", SRun("nand(true, false)", null));
        assertEquals("false", SRun("nand(true, true)", null));
    }

    @Test(timeout = 10000)
    public void testNor() throws ConfigCompileException {
        assertEquals("true", SRun("nor(false, false)", null));
        assertEquals("false", SRun("nor(false, true)", null));
        assertEquals("false", SRun("nor(true, false)", null));
        assertEquals("false", SRun("nor(true, true)", null));
    }

    @Test(timeout = 10000)
    public void testXnor() throws ConfigCompileException {
        assertEquals("true", SRun("xnor(false, false)", null));
        assertEquals("false", SRun("xnor(false, true)", null));
        assertEquals("false", SRun("xnor(true, false)", null));
        assertEquals("true", SRun("xnor(true, true)", null));
    }

    @Test(timeout = 10000)
    public void testBitAnd() throws ConfigCompileException {
        assertEquals("4", SRun("bit_and(4, 7)", null));
        assertEquals("5", SRun("bit_and(7, 5)", null));
        assertEquals("0", SRun("bit_and(1, 4)", null));
    }

    @Test(timeout = 10000)
    public void testBitOr() throws ConfigCompileException {
        assertEquals("3", SRun("bit_or(1, 3)", null));
        assertEquals("6", SRun("bit_or(2, 4)", null));
    }

    @Test(timeout = 10000)
    public void testBitNot() throws ConfigCompileException {
        assertEquals("-5", SRun("bit_not(4)", null));
    }

    @Test(timeout = 10000)
    public void testLshift() throws ConfigCompileException {
        assertEquals("16", SRun("lshift(4, 2)", null));
    }

    @Test(timeout = 10000)
    public void testRshift() throws ConfigCompileException {
        assertEquals("-3", SRun("rshift(-10, 2)", null));
        assertEquals("1", SRun("rshift(3, 1)", null));
    }

    @Test(timeout = 10000)
    public void testUrshift() throws ConfigCompileException {
        assertEquals("2", SRun("urshift(10, 2)", null));
        assertEquals("4611686018427387901", SRun("urshift(-10, 2)", null));
    }

    @Test(timeout = 10000)
    public void testIfelse() throws ConfigCompileException {
        assertEquals("3", SRun("ifelse("
                + "false, 1,"
                + "false, 2,"
                + "true, 3,"
                + "true, 4,"
                + "false, 5)", null));
        assertEquals("4", SRun("ifelse("
                + "false, 1,"
                + "false, 2,"
                + "false, 3,"
                + "add(2, 2))", null));
    }

    @Test(timeout = 10000)
    public void testSwitch() throws ConfigCompileException {
        assertEquals("correct", SRun("switch(3,"
                + "1, wrong,"
                + "2, wrong,"
                + "3, correct,"
                + "4, wrong)", null));
        assertEquals("correct", SRun("switch(4,"
                + "1, wrong,"
                + "2, wrong,"
                + "3, wrong,"
                + "correct)", null));
    }
    
    @Test public void testSwitch2() throws ConfigCompileException{
        SRun("switch(2, 1, msg('nope'), 2, msg('yep'))", fakePlayer);
        verify(fakePlayer).sendMessage("yep");
    }
    
    @Test public void testSwitch3() throws ConfigCompileException{
        SRun("assign(@args, 'test')" +
                "switch(@args," +
                    "'test'," + 
                        "msg('test')," +
                    "msg('default')" +
                ")",
                fakePlayer);
        verify(fakePlayer).sendMessage("test");
    }

    @Test(timeout = 10000)
    public void testSwitchWithArray() throws ConfigCompileException {
        assertEquals("correct", SRun("switch(3,"
                + "array(1, 2), wrong,"
                + "array(3, 4), correct,"
                + "5, wrong)", null));
    }

    @Test(timeout = 10000)
    public void testSequals() throws ConfigCompileException {
        assertEquals("true", SRun("sequals(1, 1)", null));
        assertEquals("false", SRun("sequals(1, '1')", null));
        assertEquals("false", SRun("sequals(1, '2')", null));
    }

    @Test(timeout = 10000)
    public void testIf2() throws ConfigCompileException {
        SRun("assign(@true, true)\n"
                + "if(@true, msg('Hello World!'))", fakePlayer);
        verify(fakePlayer).sendMessage("Hello World!");
    }
}
