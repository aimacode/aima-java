package aima.test.unit.util.math;

import aima.core.util.math.MixedRadixInterval;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.stream.Stream;

/**
 *
 * @author Ciaran O'Reilly
 */
public class MixedRadixIntervalTest {

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRadices1() {
        new MixedRadixInterval(new int[] {2, -2});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRadices2() {
        new MixedRadixInterval(new int[] {2, 0});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidStartNumeralValues1() {
        new MixedRadixInterval(new int[] {3, 2},
                new int[] {2, -1},
                new int[] {2, 1});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidStartNumeralValues2() {
        new MixedRadixInterval(new int[] {3, 2},
                new int[] {2, 2},
                new int[] {2, 1});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidStartNumeralValues3() {
        new MixedRadixInterval(new int[] {3, 2},
                new int[] {2, 1}, // is > end
                new int[] {1, 1});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidStartNumeralValues4() {
        new MixedRadixInterval(new int[] {3, 2},
                new int[] {2},
                new int[] {2, 1});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidStartNumeralValues5() {
        new MixedRadixInterval(new int[] {3, 2},
                new int[] {2, 1, 1},
                new int[] {2, 1});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEndNumeralValues1() {
        new MixedRadixInterval(new int[] {3, 2},
                new int[] {0, 0},
                new int[] {2, -1});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEndNumeralValues2() {
        new MixedRadixInterval(new int[] {3, 2},
                new int[] {0, 0},
                new int[] {2, 2});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEndNumeralValues3() {
        new MixedRadixInterval(new int[] {3, 2},
                new int[] {0, 0},
                new int[] {2});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEndNumeralValues4() {
        new MixedRadixInterval(new int[] {3, 2},
                new int[] {0, 0},
                new int[] {2, 1, 1});
    }

    @Test
    public void testFirst() {
        Assert.assertArrayEquals(new int[]{0, 0, 0, 0},
                MixedRadixInterval.first(new int[]{6, 5, 4, 3}));
    }

    @Test
    public void testLast() {
        Assert.assertArrayEquals(new int[] { 5, 4, 3, 2 },
                MixedRadixInterval.last(new int[] { 6, 5, 4, 3 }));
    }

    @Test
    public void testGetLeftEndPointValue() {
        Assert.assertEquals(
                BigInteger.ZERO,
                new MixedRadixInterval(new int[] {3, 2})
                        .getLeftEndPointValue()
        );
        Assert.assertEquals(
                BigInteger.ZERO,
                new MixedRadixInterval(new int[] {3, 2},
                        new int[] {0, 0},
                        new int[] {2, 1})
                        .getLeftEndPointValue()
        );
        Assert.assertEquals(
                BigInteger.ONE,
                new MixedRadixInterval(new int[] {3, 2},
                        new int[] {0, 1},
                        new int[] {2, 1})
                        .getLeftEndPointValue()
        );
        Assert.assertEquals(
                BigInteger.valueOf(5),
                new MixedRadixInterval(new int[] {3, 2},
                        new int[] {2, 1},
                        new int[] {2, 1})
                        .getLeftEndPointValue()
        );
    }

    @Test
    public void testGetRightEndPointValue() {
        Assert.assertEquals(
                BigInteger.valueOf(5),
                new MixedRadixInterval(new int[] {3, 2})
                        .getRightEndPointValue()
        );
        Assert.assertEquals(
                BigInteger.valueOf(5),
                new MixedRadixInterval(new int[] {3, 2},
                        new int[] {0, 0},
                        new int[] {2, 1})
                        .getRightEndPointValue()
        );
        Assert.assertEquals(
                BigInteger.valueOf(4),
                new MixedRadixInterval(new int[] {3, 2},
                        new int[] {0, 1},
                        new int[] {2, 0})
                        .getRightEndPointValue()
        );
        Assert.assertEquals(
                BigInteger.valueOf(5),
                new MixedRadixInterval(new int[] {3, 2},
                        new int[] {2, 1},
                        new int[] {2, 1})
                        .getRightEndPointValue()
        );
    }

    @Test
    public void testSize() {
        Assert.assertEquals(
                BigInteger.valueOf(6),
                new MixedRadixInterval(new int[] {3, 2})
                        .size()
        );
        Assert.assertEquals(
                BigInteger.valueOf(6),
                new MixedRadixInterval(new int[] {3, 2},
                        new int[] {0, 0},
                        new int[] {2, 1})
                        .size()
        );
        Assert.assertEquals(
                BigInteger.valueOf(4),
                new MixedRadixInterval(new int[] {3, 2},
                        new int[] {0, 1},
                        new int[] {2, 0})
                        .size()
        );
        Assert.assertEquals(
                BigInteger.ONE,
                new MixedRadixInterval(new int[] {3, 2},
                        new int[] {0, 0},
                        new int[] {0, 0})
                        .size()
        );
        Assert.assertEquals(
                BigInteger.ONE,
                new MixedRadixInterval(new int[] {3, 2},
                        new int[] {2, 1},
                        new int[] {2, 1})
                        .size()
        );
        Assert.assertEquals(
                BigInteger.valueOf(256),
                new MixedRadixInterval(new int[] {2, 2, 2, 2, 2, 2, 2, 2})
                        .size()
        );
    }

    @Test
    public void testMinPossibleValue() {
        Assert.assertEquals(BigInteger.ZERO, new MixedRadixInterval(new int[] {3, 2})
                        .getMinPossibleValue()
        );
        Assert.assertEquals(BigInteger.ZERO,
                new MixedRadixInterval(new int[] {3, 2},
                        new int[] {1, 0},
                        new int[] {2, 1})
                        .getMinPossibleValue()
        );
    }

    @Test
    public void testMaxPossibleValue() {
        Assert.assertEquals(BigInteger.valueOf(5),
                new MixedRadixInterval(new int[] {3, 2})
                        .getMaxPossibleValue()
        );
        Assert.assertEquals(BigInteger.valueOf(5),
                new MixedRadixInterval(new int[] {3, 2},
                        new int[] {1, 0},
                        new int[] {2, 0})
                        .getMaxPossibleValue()
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGetValueFor1() {
        new MixedRadixInterval(new int[] {3, 2}).getValueFor(new int[] {});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGetValueFor2() {
        new MixedRadixInterval(new int[] {3, 2}).getValueFor(new int[] {2, 1, 1});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGetValueFor3() {
        new MixedRadixInterval(new int[] {3, 2}).getValueFor(new int[] {-1, 1});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGetValueFor4() {
        new MixedRadixInterval(new int[] {3, 2}).getValueFor(new int[] {2, 2});
    }

    @Test
    public void testGetValueFor() {
        Assert.assertEquals(
                BigInteger.valueOf(2),
                new MixedRadixInterval(new int[] {3, 2},
                        new int[] {1, 1},
                        new int[] {2, 1})
                        .getValueFor(new int[] {1, 0})
        );
        Assert.assertEquals(
                BigInteger.ZERO,
                new MixedRadixInterval(new int[] { 3, 3, 2, 2 })
                        .getValueFor(new int[] {0, 0, 0, 0})
        );
        Assert.assertEquals(
                BigInteger.valueOf(35),
                new MixedRadixInterval(new int[] { 3, 3, 2, 2 })
                        .getValueFor(new int[] {2, 2, 1, 1})
        );
        Assert.assertEquals(
                BigInteger.valueOf(25),
                new MixedRadixInterval(new int[] { 3, 3, 2, 2 })
                        .getValueFor(new int[] {2, 0, 0, 1})
        );
        Assert.assertEquals(
                BigInteger.valueOf(17),
                new MixedRadixInterval(new int[] { 3, 3, 2, 2 })
                        .getValueFor(new int[] {1, 1, 0, 1})
        );
        Assert.assertEquals(
                BigInteger.valueOf(8),
                new MixedRadixInterval(new int[] { 3, 3, 2, 2 })
                        .getValueFor(new int[] {0, 2, 0, 0})
        );
        Assert.assertEquals(
                BigInteger.valueOf(359),
                new MixedRadixInterval(new int[] { 3, 4, 5, 6 })
                        .getValueFor(new int[] {2, 3, 4, 5})
        );
        Assert.assertEquals(
                BigInteger.valueOf(359),
                new MixedRadixInterval(new int[] { 6, 5, 4, 3 })
                        .getValueFor(new int[] {5, 4, 3, 2})
        );
        Assert.assertEquals(
                BigInteger.valueOf(2),
                new MixedRadixInterval(new int[] { 2, 1, 2 })
                        .getValueFor(new int[] {1, 0, 0})
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGetNumeralsFor1() {
        new MixedRadixInterval(new int[] {3, 2}).getNumeralsFor(BigInteger.valueOf(-1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGetNumeralsFor2() {
        new MixedRadixInterval(new int[] {3, 2}).getNumeralsFor(BigInteger.valueOf(6));
    }

    @Test
    public void testGetNumeralsFor() {
        Assert.assertArrayEquals(
                new int[] {0, 0, 0, 0},
                new MixedRadixInterval(new int[] { 3, 3, 2, 2 }).getNumeralsFor(BigInteger.ZERO));
        Assert.assertArrayEquals(
                new int[] {2, 2, 1, 1},
                new MixedRadixInterval(new int[] { 3, 3, 2, 2 }).getNumeralsFor(BigInteger.valueOf(35)));
        Assert.assertArrayEquals(
                new int[] {2, 0, 0, 1},
                new MixedRadixInterval(new int[] { 3, 3, 2, 2 }).getNumeralsFor(BigInteger.valueOf(25)));
        Assert.assertArrayEquals(
                new int[] {1, 1, 0, 1},
                new MixedRadixInterval(new int[] { 3, 3, 2, 2 }).getNumeralsFor(BigInteger.valueOf(17)));
        Assert.assertArrayEquals(
                new int[] {0, 2, 0, 0},
                new MixedRadixInterval(new int[] { 3, 3, 2, 2 }).getNumeralsFor(BigInteger.valueOf(8)));
        Assert.assertArrayEquals(
                new int[] {2, 3, 4, 5},
                new MixedRadixInterval(new int[] { 3, 4, 5, 6 }).getNumeralsFor(BigInteger.valueOf(359)));
        Assert.assertArrayEquals(
                new int[] {5, 4, 3, 2},
                new MixedRadixInterval(new int[] { 6, 5, 4, 3 }).getNumeralsFor(BigInteger.valueOf(359)));
        Assert.assertArrayEquals(
                new int[] {1, 0, 0},
                new MixedRadixInterval(new int[] { 2, 1, 2 }).getNumeralsFor(BigInteger.valueOf(2)));
    }

    @Test
    public void testIterator() {
        Iterator<int[]> itor = new MixedRadixInterval(new int[] {3, 2}).iterator();

        Assert.assertTrue(itor.hasNext());
        Assert.assertArrayEquals(new int[] {0, 0}, itor.next());
        Assert.assertTrue(itor.hasNext());
        Assert.assertArrayEquals(new int[] {0, 1}, itor.next());
        Assert.assertTrue(itor.hasNext());
        Assert.assertArrayEquals(new int[] {1, 0}, itor.next());
        Assert.assertTrue(itor.hasNext());
        Assert.assertArrayEquals(new int[] {1, 1}, itor.next());
        Assert.assertTrue(itor.hasNext());
        Assert.assertArrayEquals(new int[] {2, 0}, itor.next());
        Assert.assertTrue(itor.hasNext());
        Assert.assertArrayEquals(new int[] {2, 1}, itor.next());
        Assert.assertFalse(itor.hasNext());
    }

    @Test
    public void testSpliteratorTryAdvance() {
        Spliterator<int[]> splor = new MixedRadixInterval(new int[] {3, 2}).spliterator();

        Assert.assertTrue(splor.tryAdvance(numerals -> Assert.assertArrayEquals(new int[] {0, 0}, numerals)));
        Assert.assertTrue(splor.tryAdvance(numerals -> Assert.assertArrayEquals(new int[] {0, 1}, numerals)));
        Assert.assertTrue(splor.tryAdvance(numerals -> Assert.assertArrayEquals(new int[] {1, 0}, numerals)));
        Assert.assertTrue(splor.tryAdvance(numerals -> Assert.assertArrayEquals(new int[] {1, 1}, numerals)));
        Assert.assertTrue(splor.tryAdvance(numerals -> Assert.assertArrayEquals(new int[] {2, 0}, numerals)));
        Assert.assertTrue(splor.tryAdvance(numerals -> Assert.assertArrayEquals(new int[] {2, 1}, numerals)));
        Assert.assertFalse(splor.tryAdvance(numerals -> Assert.fail("Should not be called")));

        splor = new MixedRadixInterval(new int[] {3, 2}, new int[] {0, 1}, new int[] {2, 0}).spliterator();
        Assert.assertTrue(splor.tryAdvance(numerals -> Assert.assertArrayEquals(new int[] {0, 1}, numerals)));
        Assert.assertTrue(splor.tryAdvance(numerals -> Assert.assertArrayEquals(new int[] {1, 0}, numerals)));
        Assert.assertTrue(splor.tryAdvance(numerals -> Assert.assertArrayEquals(new int[] {1, 1}, numerals)));
        Assert.assertTrue(splor.tryAdvance(numerals -> Assert.assertArrayEquals(new int[] {2, 0}, numerals)));
        Assert.assertFalse(splor.tryAdvance(numerals -> Assert.fail("Should not be called")));
    }

    @Test
    public void testSpliteratorTrySimpleSplits() {
        Spliterator<int[]> splor1, splor2, splor3, splor4;

        splor1 = new MixedRadixInterval(new int[] {1}).spliterator();
        splor2 = splor1.trySplit();
        Assert.assertNull(splor2);

        splor1 = new MixedRadixInterval(new int[] {2}).spliterator();
        splor2 = splor1.trySplit();
        Assert.assertNotNull(splor2);
        Assert.assertTrue(splor2.tryAdvance(numerals -> Assert.assertArrayEquals(new int[] {0}, numerals)));
        Assert.assertFalse(splor2.tryAdvance(numerals -> Assert.fail("Should not be called")));
        Assert.assertTrue(splor1.tryAdvance(numerals -> Assert.assertArrayEquals(new int[] {1}, numerals)));
        Assert.assertFalse(splor1.tryAdvance(numerals -> Assert.fail("Should not be called")));

        splor1 = new MixedRadixInterval(new int[] {3, 2}).spliterator();
        splor2 = splor1.trySplit();
        Assert.assertNotNull(splor2);
        Assert.assertTrue(splor2.tryAdvance(numerals -> Assert.assertArrayEquals(new int[] {0, 0}, numerals)));
        Assert.assertTrue(splor2.tryAdvance(numerals -> Assert.assertArrayEquals(new int[] {0, 1}, numerals)));
        Assert.assertTrue(splor2.tryAdvance(numerals -> Assert.assertArrayEquals(new int[] {1, 0}, numerals)));
        Assert.assertFalse(splor2.tryAdvance(numerals -> Assert.fail("Should not be called")));
        Assert.assertTrue(splor1.tryAdvance(numerals -> Assert.assertArrayEquals(new int[] {1, 1}, numerals)));
        Assert.assertTrue(splor1.tryAdvance(numerals -> Assert.assertArrayEquals(new int[] {2, 0}, numerals)));
        Assert.assertTrue(splor1.tryAdvance(numerals -> Assert.assertArrayEquals(new int[] {2, 1}, numerals)));
        Assert.assertFalse(splor1.tryAdvance(numerals -> Assert.fail("Should not be called")));

        splor1 = new MixedRadixInterval(new int[] {3, 3}).spliterator();
        splor2 = splor1.trySplit();
        Assert.assertNotNull(splor2);
        Assert.assertTrue(splor2.tryAdvance(numerals -> Assert.assertArrayEquals(new int[] {0, 0}, numerals)));
        Assert.assertTrue(splor2.tryAdvance(numerals -> Assert.assertArrayEquals(new int[] {0, 1}, numerals)));
        Assert.assertTrue(splor2.tryAdvance(numerals -> Assert.assertArrayEquals(new int[] {0, 2}, numerals)));
        Assert.assertTrue(splor2.tryAdvance(numerals -> Assert.assertArrayEquals(new int[] {1, 0}, numerals)));
        Assert.assertFalse(splor2.tryAdvance(numerals -> Assert.fail("Should not be called")));
        Assert.assertTrue(splor1.tryAdvance(numerals -> Assert.assertArrayEquals(new int[] {1, 1}, numerals)));
        Assert.assertTrue(splor1.tryAdvance(numerals -> Assert.assertArrayEquals(new int[] {1, 2}, numerals)));
        Assert.assertTrue(splor1.tryAdvance(numerals -> Assert.assertArrayEquals(new int[] {2, 0}, numerals)));
        Assert.assertTrue(splor1.tryAdvance(numerals -> Assert.assertArrayEquals(new int[] {2, 1}, numerals)));
        Assert.assertTrue(splor1.tryAdvance(numerals -> Assert.assertArrayEquals(new int[] {2, 2}, numerals)));
        Assert.assertFalse(splor1.tryAdvance(numerals -> Assert.fail("Should not be called")));

        splor1 = new MixedRadixInterval(new int[] {3, 3}).spliterator();
        splor2 = splor1.trySplit();
        splor3 = splor1.trySplit();
        splor4 = splor2.trySplit();

        Assert.assertTrue(splor4.tryAdvance(numerals -> Assert.assertArrayEquals(new int[] {0, 0}, numerals)));
        Assert.assertTrue(splor4.tryAdvance(numerals -> Assert.assertArrayEquals(new int[] {0, 1}, numerals)));
        Assert.assertFalse(splor4.tryAdvance(numerals -> Assert.fail("Should not be called")));
        Assert.assertTrue(splor2.tryAdvance(numerals -> Assert.assertArrayEquals(new int[] {0, 2}, numerals)));
        Assert.assertTrue(splor2.tryAdvance(numerals -> Assert.assertArrayEquals(new int[] {1, 0}, numerals)));
        Assert.assertFalse(splor2.tryAdvance(numerals -> Assert.fail("Should not be called")));
        Assert.assertTrue(splor3.tryAdvance(numerals -> Assert.assertArrayEquals(new int[] {1, 1}, numerals)));
        Assert.assertTrue(splor3.tryAdvance(numerals -> Assert.assertArrayEquals(new int[] {1, 2}, numerals)));
        Assert.assertFalse(splor3.tryAdvance(numerals -> Assert.fail("Should not be called")));
        Assert.assertTrue(splor1.tryAdvance(numerals -> Assert.assertArrayEquals(new int[] {2, 0}, numerals)));
        Assert.assertTrue(splor1.tryAdvance(numerals -> Assert.assertArrayEquals(new int[] {2, 1}, numerals)));
        Assert.assertTrue(splor1.tryAdvance(numerals -> Assert.assertArrayEquals(new int[] {2, 2}, numerals)));
        Assert.assertFalse(splor1.tryAdvance(numerals -> Assert.fail("Should not be called")));
    }

    @Test
    public void testSpliteratorEstimateSize() {
        Spliterator<int[]> splor1, splor2;

        Assert.assertEquals(8,
                new MixedRadixInterval(new int[] {2, 2, 2})
                        .spliterator().estimateSize());

        splor1 = new MixedRadixInterval(new int[] {2, 2, 2}).spliterator();
        splor2 = splor1.trySplit();
        Assert.assertEquals(4, splor1.estimateSize());
        Assert.assertEquals(4, splor2.estimateSize());

        Assert.assertEquals(27,
                new MixedRadixInterval(new int[] {3, 3, 3})
                        .spliterator().estimateSize());

        splor1 = new MixedRadixInterval(new int[] {3, 3, 3}).spliterator();
        splor2 = splor1.trySplit();
        Assert.assertEquals(14, splor1.estimateSize());
        Assert.assertEquals(13, splor2.estimateSize());


        // 2^63 -1 (9223372036854775807) == mixed interval size exactly
        Assert.assertEquals(Long.MAX_VALUE,
                new MixedRadixInterval(new int[] {512, 512, 512, 512, 512, 512, 512},
                        new int[] {0, 0, 0, 0, 0, 0, 0},
                        new int[] {511, 511, 511, 511, 511, 511, 510})
                        .spliterator().estimateSize());
        // < 2^63 -1 by 1 (i.e. 9223372036854775806)
        Assert.assertEquals(Long.MAX_VALUE-1,
                new MixedRadixInterval(new int[] {512, 512, 512, 512, 512, 512, 512},
                        new int[] {0, 0, 0, 0, 0, 0, 0},
                        new int[] {511, 511, 511, 511, 511, 511, 509})
                        .spliterator().estimateSize());


        // Splitting the iterator should divide the size
        splor1 = new MixedRadixInterval(new int[] {512, 512, 512, 512, 512, 512, 512},
                new int[] {0, 0, 0, 0, 0, 0, 0},
                new int[] {511, 511, 511, 511, 511, 511, 510})
                .spliterator();
        splor2 = splor1.trySplit();
        Assert.assertEquals(Long.MAX_VALUE/2 + 1, splor1.estimateSize());
        Assert.assertEquals(Long.MAX_VALUE/2, splor2.estimateSize());
    }

    @Test
    public void testSpliteratorCharacteristics() {
        Spliterator<int[]> splor1, splor2;

        splor1 = new MixedRadixInterval(new int[] {2, 2, 2}).spliterator();
        Assert.assertEquals(splor1.characteristics(), (Spliterator.DISTINCT | Spliterator.NONNULL | Spliterator.IMMUTABLE | Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED));

        splor1 =  new MixedRadixInterval(new int[] {512, 512, 512, 512, 512, 512, 512},
                new int[] {0, 0, 0, 0, 0, 0, 0},
                new int[] {511, 511, 511, 511, 511, 511, 510})
                .spliterator();

        Assert.assertEquals(splor1.characteristics(), (Spliterator.DISTINCT | Spliterator.NONNULL | Spliterator.IMMUTABLE | Spliterator.ORDERED));

        splor2 = splor1.trySplit();
        Assert.assertEquals(splor1.characteristics(), (Spliterator.DISTINCT | Spliterator.NONNULL | Spliterator.IMMUTABLE | Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED));
        Assert.assertEquals(splor2.characteristics(), (Spliterator.DISTINCT | Spliterator.NONNULL | Spliterator.IMMUTABLE | Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED));
    }

    @Test
    public void testStream() {
        MixedRadixInterval mri    = new MixedRadixInterval(new int[] {512, 512, 512});
        Stream<int[]> stream = mri.stream();

        Assert.assertEquals(512*512*512, mri.size().longValue());
        Assert.assertFalse(stream.isParallel());
        Assert.assertEquals(mri.getRightEndPointValue().longValue()+1, stream.count());
    }

    @Test
    public void testParallelStream() {
        MixedRadixInterval mri    = new MixedRadixInterval(new int[] {512, 512, 512});
        Stream<int[]>      stream = mri.parallelStream();

        Assert.assertEquals(512*512*512, mri.size().longValue());
        Assert.assertTrue(stream.isParallel());
        Assert.assertEquals(mri.getRightEndPointValue().longValue()+1, stream.count());
    }

    @Test
    public void testToString() {
        Assert.assertEquals("[0, 3]", new MixedRadixInterval(new int[] {2, 2}).toString());
        Assert.assertEquals("[0, "+Long.MAX_VALUE+"]",
                new MixedRadixInterval(new int[] {512, 512, 512, 512, 512, 512, 512}).toString());
        Assert.assertEquals("[1, "+(Long.MAX_VALUE-1)+"]",
                new MixedRadixInterval(new int[] {512, 512, 512, 512, 512, 512, 512},
                        new int[] {0, 0, 0, 0, 0, 0, 1},
                        new int[] {511, 511, 511, 511, 511, 511, 510}).toString());
    }

}
