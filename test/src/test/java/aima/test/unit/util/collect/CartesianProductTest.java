package aima.test.unit.util.collect;

import aima.core.util.collect.CartesianProduct;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author Ciaran O'Reilly
 */
public class CartesianProductTest {

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalCartesianProduct() {
		// No Elements defined
		new CartesianProduct<>(String.class, new ArrayList<>());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalCartesianProduct2() {
		List<List<String>> listOfListsOfElements = new ArrayList<List<String>>() {
			private static final long serialVersionUID = 1L;
			{
				add(new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add("1e1");
						add("1e2");
					}
				});
				// Forgot to put any elements in second list
				add(new ArrayList<>());
			}
		};
		new CartesianProduct<>(String.class, listOfListsOfElements);
	}

	@Test
	public void testSize() {
		List<List<String>> listOfListsOfElements = new ArrayList<List<String>>() {
			private static final long serialVersionUID = 1L;
			{
				add(new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add("1e1");
						add("1e2");
						add("1e3");
					}
				});
				add(new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add("2e1");
						add("2e2");
					}
				});
			}
		};

		CartesianProduct<String> cp = new CartesianProduct<>(String.class, listOfListsOfElements);
		Assert.assertEquals(BigInteger.valueOf(6), cp.size());
	}

	@Test
	public void testIterator() {
		List<List<String>> listOfListsOfElements = new ArrayList<List<String>>() {
			private static final long serialVersionUID = 1L;
			{
				add(new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add("1e1");
						add("1e2");
					}
				});
				add(new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add("2e1");
						add("2e2");
					}
				});
			}
		};

		Iterator<String[]> itor = new CartesianProduct<>(String.class, listOfListsOfElements).iterator();
		Assert.assertTrue(itor.hasNext());
		Assert.assertArrayEquals(new String[] { "1e1", "2e1" }, itor.next());
		Assert.assertTrue(itor.hasNext());
		Assert.assertArrayEquals(new String[] { "1e1", "2e2" }, itor.next());
		Assert.assertTrue(itor.hasNext());
		Assert.assertArrayEquals(new String[] { "1e2", "2e1" }, itor.next());
		Assert.assertTrue(itor.hasNext());
		Assert.assertArrayEquals(new String[] { "1e2", "2e2" }, itor.next());
		Assert.assertFalse(itor.hasNext());
	}

	@Test
	public void testIteratorWithFixedValue() {
		List<List<String>> listOfListsOfElements = new ArrayList<List<String>>() {
			private static final long serialVersionUID = 1L;
			{
				add(new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add("1e1");
						add("1e2");
					}
				});
				add(new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add("2e1");
					}
				});
				add(new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add("3e1");
						add("3e2");
					}
				});
			}
		};

		Iterator<String[]> itor = new CartesianProduct<>(String.class, listOfListsOfElements).iterator();
		Assert.assertTrue(itor.hasNext());
		Assert.assertArrayEquals(new String[] { "1e1", "2e1", "3e1" }, itor.next());
		Assert.assertTrue(itor.hasNext());
		Assert.assertArrayEquals(new String[] { "1e1", "2e1", "3e2" }, itor.next());
		Assert.assertTrue(itor.hasNext());
		Assert.assertArrayEquals(new String[] { "1e2", "2e1", "3e1" }, itor.next());
		Assert.assertTrue(itor.hasNext());
		Assert.assertArrayEquals(new String[] { "1e2", "2e1", "3e2" }, itor.next());
		Assert.assertFalse(itor.hasNext());
	}

	@Test
	public void testSpliteratorTryAdvance() {
		List<List<String>> listOfListsOfElements = new ArrayList<List<String>>() {
			private static final long serialVersionUID = 1L;
			{
				add(new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add("1e1");
						add("1e2");
						add("1e3");
					}
				});
				add(new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add("2e1");
						add("2e2");
					}
				});
			}
		};

		Spliterator<String[]> splor = new CartesianProduct<>(String.class, listOfListsOfElements).spliterator();
		Assert.assertTrue(splor.tryAdvance(tuple -> Assert.assertArrayEquals(new String[] { "1e1", "2e1" }, tuple)));
		Assert.assertTrue(splor.tryAdvance(tuple -> Assert.assertArrayEquals(new String[] { "1e1", "2e2" }, tuple)));
		Assert.assertTrue(splor.tryAdvance(tuple -> Assert.assertArrayEquals(new String[] { "1e2", "2e1" }, tuple)));
		Assert.assertTrue(splor.tryAdvance(tuple -> Assert.assertArrayEquals(new String[] { "1e2", "2e2" }, tuple)));
		Assert.assertTrue(splor.tryAdvance(tuple -> Assert.assertArrayEquals(new String[] { "1e3", "2e1" }, tuple)));
		Assert.assertTrue(splor.tryAdvance(tuple -> Assert.assertArrayEquals(new String[] { "1e3", "2e2" }, tuple)));
		Assert.assertFalse(splor.tryAdvance(tuple -> Assert.fail("Should not be called")));
	}

	@Test
	public void testSpliteratorTrySimpleSplits() {
		Spliterator<String[]> splor1, splor2, splor3, splor4;

		splor1 = new CartesianProduct<>(String.class, new ArrayList<List<String>>() {
			private static final long serialVersionUID = 1L;
			{
				add(new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add("1e1");
					}
				});
			}
		}).spliterator();
		splor2 = splor1.trySplit();
		Assert.assertNull(splor2);

		splor1 = new CartesianProduct<>(String.class, new ArrayList<List<String>>() {
			private static final long serialVersionUID = 1L;
			{
				add(new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add("1e1");
						add("1e2");
					}
				});
			}
		}).spliterator();
		splor2 = splor1.trySplit();
		Assert.assertNotNull(splor2);
		Assert.assertTrue(splor2.tryAdvance(tuple -> Assert.assertArrayEquals(new String[] { "1e1" }, tuple)));
		Assert.assertFalse(splor2.tryAdvance(tuple -> Assert.fail("Should not be called")));
		Assert.assertTrue(splor1.tryAdvance(tuple -> Assert.assertArrayEquals(new String[] { "1e2" }, tuple)));
		Assert.assertFalse(splor1.tryAdvance(tuple -> Assert.fail("Should not be called")));

		splor1 = new CartesianProduct<>(String.class, new ArrayList<List<String>>() {
			private static final long serialVersionUID = 1L;
			{
				add(new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add("1e1");
						add("1e2");
						add("1e3");
					}
				});
				add(new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add("2e1");
						add("2e2");
					}
				});
			}
		}).spliterator();
		splor2 = splor1.trySplit();
		Assert.assertNotNull(splor2);

		Assert.assertTrue(splor2.tryAdvance(tuple -> Assert.assertArrayEquals(new String[] { "1e1", "2e1" }, tuple)));
		Assert.assertTrue(splor2.tryAdvance(tuple -> Assert.assertArrayEquals(new String[] { "1e1", "2e2" }, tuple)));
		Assert.assertTrue(splor2.tryAdvance(tuple -> Assert.assertArrayEquals(new String[] { "1e2", "2e1" }, tuple)));
		Assert.assertFalse(splor2.tryAdvance(tuple -> Assert.fail("Should not be called")));
		Assert.assertTrue(splor1.tryAdvance(tuple -> Assert.assertArrayEquals(new String[] { "1e2", "2e2" }, tuple)));
		Assert.assertTrue(splor1.tryAdvance(tuple -> Assert.assertArrayEquals(new String[] { "1e3", "2e1" }, tuple)));
		Assert.assertTrue(splor1.tryAdvance(tuple -> Assert.assertArrayEquals(new String[] { "1e3", "2e2" }, tuple)));
		Assert.assertFalse(splor1.tryAdvance(tuple -> Assert.fail("Should not be called")));

		splor1 = new CartesianProduct<>(String.class, new ArrayList<List<String>>() {
			private static final long serialVersionUID = 1L;
			{
				add(new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add("1e1");
						add("1e2");
						add("1e3");
					}
				});
				add(new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add("2e1");
						add("2e2");
						add("2e3");
					}
				});
			}
		}).spliterator();
		splor2 = splor1.trySplit();
		Assert.assertNotNull(splor2);
		Assert.assertTrue(splor2.tryAdvance(tuple -> Assert.assertArrayEquals(new String[] { "1e1", "2e1" }, tuple)));
		Assert.assertTrue(splor2.tryAdvance(tuple -> Assert.assertArrayEquals(new String[] { "1e1", "2e2" }, tuple)));
		Assert.assertTrue(splor2.tryAdvance(tuple -> Assert.assertArrayEquals(new String[] { "1e1", "2e3" }, tuple)));
		Assert.assertTrue(splor2.tryAdvance(tuple -> Assert.assertArrayEquals(new String[] { "1e2", "2e1" }, tuple)));
		Assert.assertFalse(splor2.tryAdvance(tuple -> Assert.fail("Should not be called")));
		Assert.assertTrue(splor1.tryAdvance(tuple -> Assert.assertArrayEquals(new String[] { "1e2", "2e2" }, tuple)));
		Assert.assertTrue(splor1.tryAdvance(tuple -> Assert.assertArrayEquals(new String[] { "1e2", "2e3" }, tuple)));
		Assert.assertTrue(splor1.tryAdvance(tuple -> Assert.assertArrayEquals(new String[] { "1e3", "2e1" }, tuple)));
		Assert.assertTrue(splor1.tryAdvance(tuple -> Assert.assertArrayEquals(new String[] { "1e3", "2e2" }, tuple)));
		Assert.assertTrue(splor1.tryAdvance(tuple -> Assert.assertArrayEquals(new String[] { "1e3", "2e3" }, tuple)));
		Assert.assertFalse(splor1.tryAdvance(tuple -> Assert.fail("Should not be called")));

		splor1 = new CartesianProduct<>(String.class, new ArrayList<List<String>>() {
			private static final long serialVersionUID = 1L;
			{
				add(new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add("1e1");
						add("1e2");
						add("1e3");
					}
				});
				add(new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add("2e1");
						add("2e2");
						add("2e3");
					}
				});
			}
		}).spliterator();
		splor2 = splor1.trySplit();
		splor3 = splor1.trySplit();
		splor4 = splor2.trySplit();
		Assert.assertTrue(splor4.tryAdvance(tuple -> Assert.assertArrayEquals(new String[] { "1e1", "2e1" }, tuple)));
		Assert.assertTrue(splor4.tryAdvance(tuple -> Assert.assertArrayEquals(new String[] { "1e1", "2e2" }, tuple)));
		Assert.assertFalse(splor4.tryAdvance(tuple -> Assert.fail("Should not be called")));
		Assert.assertTrue(splor2.tryAdvance(tuple -> Assert.assertArrayEquals(new String[] { "1e1", "2e3" }, tuple)));
		Assert.assertTrue(splor2.tryAdvance(tuple -> Assert.assertArrayEquals(new String[] { "1e2", "2e1" }, tuple)));
		Assert.assertFalse(splor2.tryAdvance(tuple -> Assert.fail("Should not be called")));
		Assert.assertTrue(splor3.tryAdvance(tuple -> Assert.assertArrayEquals(new String[] { "1e2", "2e2" }, tuple)));
		Assert.assertTrue(splor3.tryAdvance(tuple -> Assert.assertArrayEquals(new String[] { "1e2", "2e3" }, tuple)));
		Assert.assertFalse(splor3.tryAdvance(tuple -> Assert.fail("Should not be called")));
		Assert.assertTrue(splor1.tryAdvance(tuple -> Assert.assertArrayEquals(new String[] { "1e3", "2e1" }, tuple)));
		Assert.assertTrue(splor1.tryAdvance(tuple -> Assert.assertArrayEquals(new String[] { "1e3", "2e2" }, tuple)));
		Assert.assertTrue(splor1.tryAdvance(tuple -> Assert.assertArrayEquals(new String[] { "1e3", "2e3" }, tuple)));
		Assert.assertFalse(splor1.tryAdvance(tuple -> Assert.fail("Should not be called")));
	}

	@Test
	public void testSpliteratorEstimateSize() {
		Spliterator<String[]> splor1, splor2;
		Assert.assertEquals(9, new CartesianProduct<>(String.class, new ArrayList<List<String>>() {
			private static final long serialVersionUID = 1L;
			{
				add(new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add("1e1");
						add("1e2");
						add("1e3");
					}
				});
				add(new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add("2e1");
						add("2e2");
						add("2e3");
					}
				});
			}
		}).spliterator().estimateSize());

		splor1 = new CartesianProduct<>(String.class, new ArrayList<List<String>>() {
			private static final long serialVersionUID = 1L;
			{
				add(new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add("1e1");
						add("1e2");
					}
				});
				add(new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add("2e1");
						add("2e2");
					}
				});
				add(new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add("3e1");
						add("3e2");
					}
				});
			}
		}).spliterator();
		splor2 = splor1.trySplit();
		Assert.assertEquals(4, splor1.estimateSize());
		Assert.assertEquals(4, splor2.estimateSize());

		splor1 = new CartesianProduct<>(String.class, new ArrayList<List<String>>() {
			private static final long serialVersionUID = 1L;
			{
				add(new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add("1e1");
						add("1e2");
						add("1e3");
					}
				});
				add(new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add("2e1");
						add("2e2");
						add("2e3");
					}
				});
				add(new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add("3e1");
						add("3e2");
						add("3e3");
					}
				});
			}
		}).spliterator();
		splor2 = splor1.trySplit();
		Assert.assertEquals(14, splor1.estimateSize());
		Assert.assertEquals(13, splor2.estimateSize());

		List<List<Integer>> listOfListOfElements = new ArrayList<>();
		for (int i = 0; i < 7; i++) {
			List<Integer> dimension = new ArrayList<>();
			for (int j = 0; j < 512; j++) {
				dimension.add(j);
			}
			listOfListOfElements.add(dimension);
		}
		Assert.assertEquals(Long.MAX_VALUE,
				new CartesianProduct<>(Integer.class, listOfListOfElements).spliterator().estimateSize());
	}

	@Test
	public void testSpliteratorCharacteristics() {
		Spliterator<String[]> splor1, splor2;

		splor1 = new CartesianProduct<>(String.class, new ArrayList<List<String>>() {
			private static final long serialVersionUID = 1L;
			{
				add(new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add("1e1");
						add("1e2");
					}
				});
				add(new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add("2e1");
						add("2e2");
					}
				});
				add(new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add("3e1");
						add("3e2");
					}
				});
			}
		}).spliterator();
		Assert.assertEquals(splor1.characteristics(), (Spliterator.DISTINCT | Spliterator.NONNULL
				| Spliterator.IMMUTABLE | Spliterator.SIZED | Spliterator.SUBSIZED));
		List<List<String>> listOfListOfElements = new ArrayList<>();
		for (int i = 0; i < 7; i++) {
			List<String> dimension = new ArrayList<>();
			for (int j = 0; j < 512; j++) {
				dimension.add("" + j);
			}
			listOfListOfElements.add(dimension);
		}
		splor1 = new CartesianProduct<>(String.class, listOfListOfElements).spliterator();
		Assert.assertEquals(splor1.characteristics(),
				(Spliterator.DISTINCT | Spliterator.NONNULL | Spliterator.IMMUTABLE));

		splor2 = splor1.trySplit();
		Assert.assertEquals(splor1.characteristics(), (Spliterator.DISTINCT | Spliterator.NONNULL
				| Spliterator.IMMUTABLE | Spliterator.SIZED | Spliterator.SUBSIZED));
		Assert.assertEquals(splor2.characteristics(), (Spliterator.DISTINCT | Spliterator.NONNULL
				| Spliterator.IMMUTABLE | Spliterator.SIZED | Spliterator.SUBSIZED));
	}

	@Test
	public void testStream() {
		List<List<String>> listOfListOfElements = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			List<String> dimension = new ArrayList<>();
			for (int j = 0; j < 512; j++) {
				dimension.add("" + j);
			}
			listOfListOfElements.add(dimension);
		}
		CartesianProduct<String> cp = new CartesianProduct<>(String.class, listOfListOfElements);
		Stream<String[]> stream = cp.stream();

		Assert.assertEquals(512 * 512 * 512, cp.size().longValue());
		Assert.assertFalse(stream.isParallel());
		Assert.assertEquals(cp.size().longValue(), stream.count());
	}

	@Test
	public void testParallelStream() {
		List<List<String>> listOfListOfElements = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			List<String> dimension = new ArrayList<>();
			for (int j = 0; j < 512; j++) {
				dimension.add("" + j);
			}
			listOfListOfElements.add(dimension);
		}
		CartesianProduct<String> cp = new CartesianProduct<>(String.class, listOfListOfElements);
		Stream<String[]> stream = cp.parallelStream();

		Assert.assertEquals(512 * 512 * 512, cp.size().longValue());
		Assert.assertTrue(stream.isParallel());
		Assert.assertEquals(cp.size().longValue(), stream.count());
	}

	@Test
	public void testToString() {
		List<List<String>> listOfListsOfElements = new ArrayList<List<String>>() {
			private static final long serialVersionUID = 1L;
			{
				add(new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add("1e1");
						add("1e2");
					}
				});
				add(new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add("2e1");
					}
				});
				add(new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add("3e1");
						add("3e2");
					}
				});
			}
		};

		CartesianProduct<String> cp = new CartesianProduct<>(String.class, listOfListsOfElements);
		Assert.assertEquals("(2x1x2)", cp.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalCartesianPower1() {
		new CartesianProduct<>(String.class, new ArrayList<>(), 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalCartesianPower2() {
		new CartesianProduct<>(String.class, new ArrayList<>(), -1);
	}

	@Test
	public void testCartesianPower() {
		CartesianProduct<String> cp = new CartesianProduct<>(String.class, Arrays.asList("1e1", "1e2"), 3);
		Assert.assertEquals(8, cp.size().intValue());

		Iterator<String[]> itor = cp.iterator();
		Assert.assertTrue(itor.hasNext());
		Assert.assertArrayEquals(new String[] { "1e1", "1e1", "1e1" }, itor.next());
		Assert.assertTrue(itor.hasNext());
		Assert.assertArrayEquals(new String[] { "1e1", "1e1", "1e2" }, itor.next());
		Assert.assertTrue(itor.hasNext());
		Assert.assertArrayEquals(new String[] { "1e1", "1e2", "1e1" }, itor.next());
		Assert.assertTrue(itor.hasNext());
		Assert.assertArrayEquals(new String[] { "1e1", "1e2", "1e2" }, itor.next());
		Assert.assertTrue(itor.hasNext());
		Assert.assertArrayEquals(new String[] { "1e2", "1e1", "1e1" }, itor.next());
		Assert.assertTrue(itor.hasNext());
		Assert.assertArrayEquals(new String[] { "1e2", "1e1", "1e2" }, itor.next());
		Assert.assertTrue(itor.hasNext());
		Assert.assertArrayEquals(new String[] { "1e2", "1e2", "1e1" }, itor.next());
		Assert.assertTrue(itor.hasNext());
		Assert.assertArrayEquals(new String[] { "1e2", "1e2", "1e2" }, itor.next());
		Assert.assertFalse(itor.hasNext());
	}
}
