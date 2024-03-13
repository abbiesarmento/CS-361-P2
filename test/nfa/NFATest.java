package test.nfa;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import fa.nfa.NFA;

public class NFATest {
	
	private NFA nfa1() {
		NFA nfa = new NFA();
		
		nfa.addSigma('0');
		nfa.addSigma('1');
		
		assertTrue(nfa.addState("a"));
		assertTrue(nfa.setStart("a"));
		
		assertTrue(nfa.addState("b"));
		assertTrue(nfa.setFinal("b"));
		
		assertFalse(nfa.addState("a"));
		assertFalse(nfa.setStart("c"));
		assertFalse(nfa.setFinal("d"));
		
	
		assertTrue(nfa.addTransition("a", Set.of("a"), '0'));
		assertTrue(nfa.addTransition("a", Set.of("b"), '1'));
		assertTrue(nfa.addTransition("b", Set.of("a"), 'e'));
		
		assertFalse(nfa.addTransition("c", Set.of("a"), '0'));
		assertFalse(nfa.addTransition("a", Set.of("b"), '3'));
		assertFalse(nfa.addTransition("b", Set.of("d","c"), 'e'));
		
		
		
		return nfa;
		
	}

	@Test
	public void test1_1() {
		NFA nfa = nfa1();
		System.out.println("nfa1 instantiation done");
	}
	
	@Test
	public void test1_2() {
		NFA nfa = nfa1();
		assertNotNull(nfa.getState("a"));
		assertEquals(nfa.getState("a").getName(), "a");
		//ensures the same object
		assertEquals(nfa.getState("a"), nfa.getState("a"));
		assertTrue(nfa.isStart("a"));
		assertTrue(nfa.isFinal("b"));
		
		
		System.out.println("nfa1 correctness done");
	}
	
	@Test
	public void test1_3() {
		NFA nfa = nfa1();
		assertFalse(nfa.isDFA());
		System.out.println("nfa1 isDFA done");
	}
	
	@Test
	public void test1_4() {
		NFA nfa = nfa1();
		assertEquals(nfa.eClosure(nfa.getState("a")), Set.of(nfa.getState("a")));
		assertEquals(nfa.eClosure(nfa.getState("b")), Set.of(nfa.getState("a"), nfa.getState("b")));
		System.out.println("nfa1 eClosure done");
	}
	
	@Test
	public void test1_5() {
		NFA nfa = nfa1();
		assertFalse(nfa.accepts("0"));
		assertTrue(nfa.accepts("1"));
		assertFalse(nfa.accepts("00"));
		assertTrue(nfa.accepts("101"));
		assertFalse(nfa.accepts("e"));
		System.out.println("nfa1 accepts done");
	}
	
	@Test
	public void test1_6() {
		NFA nfa = nfa1();
		assertEquals(nfa.maxCopies("0"), 1);
		assertEquals(nfa.maxCopies("1"), 2);
		assertEquals(nfa.maxCopies("00"), 1);
		assertEquals(nfa.maxCopies("101"), 2);
		assertEquals(nfa.maxCopies("e"), 1);
		assertEquals(nfa.maxCopies("2"), 1);
		System.out.println("nfa1 maxCopies done");
	}
	
	private NFA nfa2() {
		NFA nfa = new NFA();
		
		nfa.addSigma('0');
		nfa.addSigma('1');
		
		assertTrue(nfa.addState("q0"));
		assertTrue(nfa.setStart("q0"));
		assertTrue(nfa.addState("q1"));
		assertTrue(nfa.addState("q2"));
		assertTrue(nfa.addState("q3"));
		assertTrue(nfa.addState("q4"));
		assertTrue(nfa.setFinal("q3"));
		
		assertFalse(nfa.addState("q3"));
		assertFalse(nfa.setStart("q5"));
		assertFalse(nfa.setFinal("q6"));
		

		assertTrue(nfa.addTransition("q0", Set.of("q0"), '0'));
		assertTrue(nfa.addTransition("q0", Set.of("q0"), '1'));
		assertTrue(nfa.addTransition("q0", Set.of("q1"), '1'));
		assertTrue(nfa.addTransition("q1", Set.of("q2"), 'e'));
		assertTrue(nfa.addTransition("q2", Set.of("q4"), '0'));
		assertTrue(nfa.addTransition("q2", Set.of("q2","q3"), '1'));
		assertTrue(nfa.addTransition("q4", Set.of("q1"), '0'));
		
		assertFalse(nfa.addTransition("q0", Set.of("q5"), '0'));
		assertFalse(nfa.addTransition("q0", Set.of("q3"), '3'));
		assertFalse(nfa.addTransition("q5", Set.of("q0","q2"), 'e'));
		
		
		
		return nfa;
		
	}
	
	@Test
	public void test2_1() {
		NFA nfa = nfa2();
		System.out.println("nfa1 instantiation done");
	}
	
	@Test
	public void test2_2() {
		NFA nfa = nfa2();
		assertNotNull(nfa.getState("q0"));
		assertEquals(nfa.getState("q3").getName(), "q3");
		assertNull(nfa.getState("q5"));
		//ensures the same object
		assertEquals(nfa.getState("q2"), nfa.getState("q2"));
		assertTrue(nfa.isStart("q0"));
		assertFalse(nfa.isStart("q3"));
		assertTrue(nfa.isFinal("q3"));
		assertFalse(nfa.isFinal("q6"));
		
		System.out.println("nfa1 correctness done");
	}
	
	@Test
	public void test2_3() {
		NFA nfa = nfa2();
		assertFalse(nfa.isDFA());
		System.out.println("nfa1 isDFA done");
	}
	
	@Test
	public void test2_4() {
		NFA nfa = nfa2();
		assertEquals(nfa.eClosure(nfa.getState("q0")), Set.of(nfa.getState("q0")));
		assertEquals(nfa.eClosure(nfa.getState("q1")), Set.of(nfa.getState("q1"),nfa.getState("q2")));
		assertEquals(nfa.eClosure(nfa.getState("q3")), Set.of(nfa.getState("q3")));
		assertEquals(nfa.eClosure(nfa.getState("q4")), Set.of(nfa.getState("q4")));
		
		System.out.println("nfa1 eClosure done");
	}
	
	@Test
	public void test2_5() {
		NFA nfa = nfa2();
		assertTrue(nfa.accepts("1111"));
		assertFalse(nfa.accepts("e"));
		assertFalse(nfa.accepts("0001100"));
		assertTrue(nfa.accepts("010011"));
		assertFalse(nfa.accepts("0101"));
		System.out.println("nfa1 accepts done");
	}
	
	@Test
	public void test2_6() {
		NFA nfa = nfa2();
		assertEquals(nfa.maxCopies("1111"), 4);
		assertEquals(nfa.maxCopies("e"), 1);
		assertEquals(nfa.maxCopies("0001100"), 4);
		assertEquals(nfa.maxCopies("010011"), 4);
		assertEquals(nfa.maxCopies("0101"), 3);
		
		System.out.println("nfa1 maxCopies done");
	}
	
	private NFA nfa3() {
		NFA nfa = new NFA();
		
		nfa.addSigma('#');
		nfa.addSigma('0');
		nfa.addSigma('1');
		
		assertTrue(nfa.addState("W"));
		assertTrue(nfa.setStart("W"));
		assertTrue(nfa.addState("L"));
		assertTrue(nfa.addState("I"));
		assertTrue(nfa.addState("N"));
		assertTrue(nfa.setFinal("N"));
		
		assertFalse(nfa.addState("N"));
		assertFalse(nfa.setStart("Z"));
		assertFalse(nfa.setFinal("Y"));

		assertTrue(nfa.addTransition("W", Set.of("N"), '#'));
		assertTrue(nfa.addTransition("W", Set.of("L"), 'e'));
		
		assertTrue(nfa.addTransition("L", Set.of("L","N"), '0'));
		assertTrue(nfa.addTransition("L", Set.of("I"), 'e'));
		
		assertTrue(nfa.addTransition("I", Set.of("I"), '1'));
		assertTrue(nfa.addTransition("I", Set.of("N"), '1'));
		
		assertTrue(nfa.addTransition("N", Set.of("W"), '#'));
		
		assertFalse(nfa.addTransition("W", Set.of("K"), '0'));
		assertFalse(nfa.addTransition("W", Set.of("W"), '3'));
		assertFalse(nfa.addTransition("ZZ", Set.of("W","Z"), 'e'));
		
		
		return nfa;
		
	}

	@Test
	public void test3_1() {
		NFA nfa = nfa3();
		System.out.println("nfa1 instantiation done");
	}
	
	@Test
	public void test3_2() {
		NFA nfa = nfa3();
		assertNotNull(nfa.getState("W"));
		assertEquals(nfa.getState("N").getName(), "N");
		assertNull(nfa.getState("Z0"));
		//assertEquals(nfa.getState("I").toStates('1'), Set.of(nfa.getState("I"), nfa.getState("N")));
		assertTrue(nfa.isStart("W"));
		assertFalse(nfa.isStart("L"));
		assertTrue(nfa.isFinal("N"));
		assertFalse(nfa.isFinal("I"));
		System.out.println("nfa1 correctness done");
	}
	
	@Test
	public void test3_3() {
		NFA nfa = nfa3();
		assertFalse(nfa.isDFA());
		System.out.println("nfa1 isDFA done");
	}
	
	@Test
	public void test3_4() {
		NFA nfa = nfa3();
		assertEquals(nfa.eClosure(nfa.getState("W")), Set.of(nfa.getState("W"),nfa.getState("L"),nfa.getState("I")));
		assertEquals(nfa.eClosure(nfa.getState("N")), Set.of(nfa.getState("N")));
		assertEquals(nfa.eClosure(nfa.getState("L")), Set.of(nfa.getState("L"),nfa.getState("I")));
		assertEquals(nfa.eClosure(nfa.getState("I")), Set.of(nfa.getState("I")));
		
		System.out.println("nfa1 eClosure done");
	}
	
	@Test
	public void test3_5() {
		NFA nfa = nfa3();
		assertTrue(nfa.accepts("###"));
		assertTrue(nfa.accepts("111#00"));
		assertTrue(nfa.accepts("01#11##"));
		assertFalse(nfa.accepts("#01000###"));
		assertFalse(nfa.accepts("011#00010#"));
		System.out.println("nfa1 accepts done");
	}
	
	@Test
	public void test3_6() {
		NFA nfa = nfa3();
		assertEquals(nfa.maxCopies("###"), 3);
		assertEquals(nfa.maxCopies("e"), 3);
		assertEquals(nfa.maxCopies("011#00010#"), 3);
		assertEquals(nfa.maxCopies("23"), 3);
		assertEquals(nfa.maxCopies("011#00010#"), 3);
		System.out.println("nfa1 maxCopies done");
	}

	private NFA nfa4() {
		NFA nfa = new NFA();

		nfa.addSigma('a');
		nfa.addSigma('b');
		nfa.addSigma('c');

		assertTrue(nfa.addState("q0"));
		assertTrue(nfa.setStart("q0"));

		assertTrue(nfa.addState("q1"));
		assertTrue(nfa.addState("q2"));
		assertTrue(nfa.addState("q3"));
		assertTrue(nfa.setFinal("q3"));

		assertFalse(nfa.addState("q0"));
		assertFalse(nfa.setStart("c"));
		assertFalse(nfa.setFinal("d"));


		assertTrue(nfa.addTransition("q0", Set.of("q0", "q2"), 'a'));
		assertTrue(nfa.addTransition("q0", Set.of("q1"), 'b'));
		assertTrue(nfa.addTransition("q1", Set.of("q1"), 'c'));
		assertTrue(nfa.addTransition("q1", Set.of("q3"), 'b'));
		assertTrue(nfa.addTransition("q2", Set.of("q2"), 'a'));
		assertTrue(nfa.addTransition("q2", Set.of("q1"), 'b'));
		assertTrue(nfa.addTransition("q3", Set.of("q2"), 'a'));

		assertFalse(nfa.addTransition("c", Set.of("a"), '0'));
		assertFalse(nfa.addTransition("a", Set.of("b"), '3'));
		assertFalse(nfa.addTransition("b", Set.of("d","c"), 'e'));



		return nfa;

	}

	@Test
	public void test4_1() {
		NFA nfa = nfa4();
		System.out.println("nfa4 instantiation done");
	}

	@Test
	public void test4_2() {
		NFA nfa = nfa4();
		assertNotNull(nfa.getState("q0"));
		assertEquals(nfa.getState("q0").getName(), "q0");
		//ensures the same object
		assertEquals(nfa.getState("q0"), nfa.getState("q0"));
		assertTrue(nfa.isStart("q0"));
		assertTrue(nfa.isFinal("q3"));


		System.out.println("nfa4 correctness done");
	}

	@Test
	public void test4_3() {
		NFA nfa = nfa4();
		assertFalse(nfa.isDFA());
		System.out.println("nfa4 isDFA done");
	}

	@Test
	public void test4_4() {
		NFA nfa = nfa4();
		assertEquals(nfa.eClosure(nfa.getState("q0")), Set.of(nfa.getState("q0")));
		assertEquals(nfa.eClosure(nfa.getState("q1")), Set.of(nfa.getState("q1")));
		assertEquals(nfa.eClosure(nfa.getState("q2")), Set.of(nfa.getState("q2")));
		assertEquals(nfa.eClosure(nfa.getState("q3")), Set.of(nfa.getState("q3")));
		System.out.println("nfa4 eClosure done");
	}

	@Test
	public void test4_5() {
		NFA nfa = nfa4();
		assertFalse(nfa.accepts("a"));
		assertTrue(nfa.accepts("bb"));
		assertFalse(nfa.accepts("abcccc"));
		assertTrue(nfa.accepts("abcccbabb"));
		assertFalse(nfa.accepts("e"));
		System.out.println("nfa4 accepts done");
	}

	@Test
	public void test4_6() {
		NFA nfa = nfa4();
		assertEquals(nfa.maxCopies("a"), 2);
		assertEquals(nfa.maxCopies("bb"), 1);
		assertEquals(nfa.maxCopies("abcccc"), 2);
		assertEquals(nfa.maxCopies("abcccbabb"), 2);
		assertEquals(nfa.maxCopies("e"), 1);
		assertEquals(nfa.maxCopies("c"), 1);
		System.out.println("nfa4 maxCopies done");
	}

	private NFA nfa5() {
		NFA nfa = new NFA();

		nfa.addSigma('a');
		nfa.addSigma('b');


		assertTrue(nfa.addState("q0"));
		assertTrue(nfa.setStart("q0"));

		assertTrue(nfa.addState("q1"));
		assertTrue(nfa.addState("q2"));
		assertTrue(nfa.setFinal("q2"));

		assertFalse(nfa.addState("q0"));
		assertFalse(nfa.setStart("c"));
		assertFalse(nfa.setFinal("d"));


		assertTrue(nfa.addTransition("q0", Set.of("q1"), 'b'));
		assertTrue(nfa.addTransition("q0", Set.of("q0" , "q2"), 'a'));
		assertTrue(nfa.addTransition("q1", Set.of("q0", "q2"), 'b'));
		assertTrue(nfa.addTransition("q2", Set.of("q0"), 'a'));

		assertFalse(nfa.addTransition("c", Set.of("a"), '0'));
		assertFalse(nfa.addTransition("a", Set.of("b"), '3'));
		assertFalse(nfa.addTransition("b", Set.of("d","c"), 'e'));



		return nfa;

	}

	@Test
	public void test5_1() {
		NFA nfa = nfa5();
		System.out.println("nfa5 instantiation done");
	}

	@Test
	public void test5_2() {
		NFA nfa = nfa5();
		assertNotNull(nfa.getState("q0"));
		assertEquals(nfa.getState("q0").getName(), "q0");
		//ensures the same object
		assertEquals(nfa.getState("q0"), nfa.getState("q0"));
		assertTrue(nfa.isStart("q0"));
		assertTrue(nfa.isFinal("q2"));


		System.out.println("nfa5 correctness done");
	}

	@Test
	public void test5_3() {
		NFA nfa = nfa5();
		assertFalse(nfa.isDFA());
		System.out.println("nfa5 isDFA done");
	}

	@Test
	public void test5_4() {
		NFA nfa = nfa5();
		assertEquals(nfa.eClosure(nfa.getState("q0")), Set.of(nfa.getState("q0")));
		assertEquals(nfa.eClosure(nfa.getState("q1")), Set.of(nfa.getState("q1")));
		assertEquals(nfa.eClosure(nfa.getState("q2")), Set.of(nfa.getState("q2")));
		System.out.println("nfa5 eClosure done");
	}

	@Test
	public void test5_5() {
		NFA nfa = nfa5();
		assertFalse(nfa.accepts("b"));
		assertTrue(nfa.accepts("a"));
		assertFalse(nfa.accepts("abcccc"));
		assertTrue(nfa.accepts("aabb"));
		assertFalse(nfa.accepts("e"));
		System.out.println("nfa5 accepts done");
	}

	@Test
	public void test5_6() {
		NFA nfa = nfa5();
		assertEquals(nfa.maxCopies("a"), 2);
		assertEquals(nfa.maxCopies("bb"), 2);
		assertEquals(nfa.maxCopies("abcccc"), 2);
		assertEquals(nfa.maxCopies("aabb"), 2);
		assertEquals(nfa.maxCopies("e"), 1);
		assertEquals(nfa.maxCopies("c"), 1);
		System.out.println("nfa5 maxCopies done");
	}

	private NFA nfa6() {
		NFA nfa = new NFA();

		nfa.addSigma('a');
		nfa.addSigma('b');
		nfa.addSigma('d');


		assertTrue(nfa.addState("q0"));
		assertTrue(nfa.setStart("q0"));

		assertTrue(nfa.addState("q1"));
		assertTrue(nfa.addState("q2"));
		assertTrue(nfa.addState("q3"));
		assertTrue(nfa.addState("q4"));
		assertTrue(nfa.setFinal("q3"));

		assertFalse(nfa.addState("q0"));
		assertFalse(nfa.setStart("c"));
		assertFalse(nfa.setFinal("d"));


		assertTrue(nfa.addTransition("q0", Set.of("q1"), 'a'));
		assertTrue(nfa.addTransition("q0", Set.of("q2"), 'b'));
		assertTrue(nfa.addTransition("q1", Set.of("q3"), 'd'));
		assertTrue(nfa.addTransition("q3", Set.of("q4"), 'a'));
		assertTrue(nfa.addTransition("q4", Set.of("q3"), 'a'));

		assertTrue(nfa.addTransition("q3", Set.of("q2"), 'e'));
		assertTrue(nfa.addTransition("q2", Set.of("q0", "q1"), 'b'));

		assertFalse(nfa.addTransition("c", Set.of("a"), '0'));
		assertFalse(nfa.addTransition("a", Set.of("b"), '3'));
		assertFalse(nfa.addTransition("b", Set.of("d","c"), 'e'));


		return nfa;

	}

	@Test
	public void test6_1() {
		NFA nfa = nfa6();
		System.out.println("nfa6 instantiation done");
	}

	@Test
	public void test6_2() {
		NFA nfa = nfa6();
		assertNotNull(nfa.getState("q0"));
		assertEquals(nfa.getState("q0").getName(), "q0");
		//ensures the same object
		assertEquals(nfa.getState("q0"), nfa.getState("q0"));
		assertTrue(nfa.isStart("q0"));
		assertTrue(nfa.isFinal("q3"));


		System.out.println("nfa6 correctness done");
	}

	@Test
	public void test6_3() {
		NFA nfa = nfa6();
		assertFalse(nfa.isDFA());
		System.out.println("nfa6 isDFA done");
	}

	@Test
	public void test6_4() {
		NFA nfa = nfa6();
		assertEquals(nfa.eClosure(nfa.getState("q0")), Set.of(nfa.getState("q0")));
		assertEquals(nfa.eClosure(nfa.getState("q1")), Set.of(nfa.getState("q1")));
		assertEquals(nfa.eClosure(nfa.getState("q2")), Set.of(nfa.getState("q2")));
		assertEquals(nfa.eClosure(nfa.getState("q3")), Set.of(nfa.getState("q3"), nfa.getState("q2")));
		assertEquals(nfa.eClosure(nfa.getState("q4")), Set.of(nfa.getState("q4")));
		System.out.println("nfa6 eClosure done");
	}

	@Test
	public void test6_5() {
		NFA nfa = nfa6();
		assertFalse(nfa.accepts("b"));
		assertTrue(nfa.accepts("ad"));
		assertFalse(nfa.accepts("abcccc"));
		assertTrue(nfa.accepts("adaaaa"));
		assertFalse(nfa.accepts("e"));
		System.out.println("nfa6 accepts done");
	}

	@Test
	public void test6_6() {
		NFA nfa = nfa6();
		assertEquals(nfa.maxCopies("a"), 1);
		assertEquals(nfa.maxCopies("bb"), 2);
		assertEquals(nfa.maxCopies("abcccc"), 1);
		assertEquals(nfa.maxCopies("adaaaa"), 2);
		assertEquals(nfa.maxCopies("e"), 1);
		assertEquals(nfa.maxCopies("c"), 1);
		System.out.println("nfa6 maxCopies done");
	}

	private NFA nfa7() {
		NFA nfa = new NFA();

		nfa.addSigma('0');
		nfa.addSigma('1');


		assertTrue(nfa.addState("q0"));
		assertTrue(nfa.setStart("q0"));

		assertTrue(nfa.addState("q1"));
		assertTrue(nfa.addState("q2"));
		assertTrue(nfa.addState("q3"));
		assertTrue(nfa.addState("q4"));
		assertTrue(nfa.setFinal("q3"));

		assertFalse(nfa.addState("q0"));
		assertFalse(nfa.setStart("c"));
		assertFalse(nfa.setFinal("d"));


		assertTrue(nfa.addTransition("q0", Set.of("q1"), '0'));
		assertTrue(nfa.addTransition("q1", Set.of("q2"), '0'));
		assertTrue(nfa.addTransition("q2", Set.of("q3"), '1'));
		assertTrue(nfa.addTransition("q3", Set.of("q1"), '0'));

		assertTrue(nfa.addTransition("q3", Set.of("q4"), '1'));
		assertTrue(nfa.addTransition("q4", Set.of("q3"), '1'));

		assertFalse(nfa.addTransition("c", Set.of("a"), '0'));
		assertFalse(nfa.addTransition("a", Set.of("b"), '3'));
		assertFalse(nfa.addTransition("b", Set.of("d","c"), 'e'));


		return nfa;

	}

	@Test
	public void test7_1() {
		NFA nfa = nfa7();
		System.out.println("nfa7 instantiation done");
	}

	@Test
	public void test7_2() {
		NFA nfa = nfa7();
		assertNotNull(nfa.getState("q0"));
		assertEquals(nfa.getState("q0").getName(), "q0");
		//ensures the same object
		assertEquals(nfa.getState("q0"), nfa.getState("q0"));
		assertTrue(nfa.isStart("q0"));
		assertTrue(nfa.isFinal("q3"));


		System.out.println("nfa7 correctness done");
	}

	@Test
	public void test7_3() {
		NFA nfa = nfa7();
		assertTrue(nfa.isDFA());
		System.out.println("nfa7 isDFA done");
	}

	@Test
	public void test7_4() {
		NFA nfa = nfa7();
		assertEquals(nfa.eClosure(nfa.getState("q0")), Set.of(nfa.getState("q0")));
		assertEquals(nfa.eClosure(nfa.getState("q1")), Set.of(nfa.getState("q1")));
		assertEquals(nfa.eClosure(nfa.getState("q2")), Set.of(nfa.getState("q2")));
		assertEquals(nfa.eClosure(nfa.getState("q3")), Set.of(nfa.getState("q3")));
		assertEquals(nfa.eClosure(nfa.getState("q4")), Set.of(nfa.getState("q4")));
		System.out.println("nfa7 eClosure done");
	}

	@Test
	public void test7_5() {
		NFA nfa = nfa7();
		assertFalse(nfa.accepts("1001"));
		assertTrue(nfa.accepts("001"));
		assertFalse(nfa.accepts("1001000"));
		assertTrue(nfa.accepts("0011100001"));
		assertFalse(nfa.accepts("e"));
		System.out.println("nfa7 accepts done");
	}

	@Test
	public void test7_6() {
		NFA nfa = nfa7();
		assertEquals(nfa.maxCopies("1001"), 1);
		assertEquals(nfa.maxCopies("001"), 1);
		assertEquals(nfa.maxCopies("1001000"), 1);
		assertEquals(nfa.maxCopies("0011100001"), 1);
		assertEquals(nfa.maxCopies("e"), 1);
		assertEquals(nfa.maxCopies("0"), 1);
		System.out.println("nfa7 maxCopies done");
	}
}
