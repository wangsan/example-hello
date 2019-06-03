package com.wangsan.study.match;

import org.junit.Assert;
import org.junit.Test;

/**
 * created by wangsan on 2019-04-11 in project of example .
 *
 * @author wangsan
 * @date 2019-04-11
 */
public class StringMatch {
    public static final char WILD_CHAR = '*';

    public static void main(String[] args) {
        System.out.println(bf("abcdefg", "abcde"));
        System.out.println(bf("abcdefg", "abcdefh"));
        System.out.println(bf("abcdefgh", "cdefg"));

    }

    @Test
    public void testCharEqual() {
        char a = '*';
        char b = new Character('*');
        Assert.assertEquals(a, WILD_CHAR);
        Assert.assertEquals(b, WILD_CHAR);
        Assert.assertEquals(a, b);
        Assert.assertTrue(a == b);
        Assert.assertTrue(a == WILD_CHAR);
        Assert.assertTrue(b == WILD_CHAR);
    }

    @Test
    public void testMatch() {
        Assert.assertEquals(0, bf("abcdefg", "abcdefg"));
        Assert.assertEquals(0, bf("abcdefg", "abcde*g"));
        Assert.assertEquals(0, bf("abc*efg", "abcdefg"));
        Assert.assertEquals(0, bf("abc*efg", "abceefg"));

        Assert.assertEquals(0, forceSearch("abcdefg", "abcdefg"));
        Assert.assertEquals(0, forceSearch("abcdefg", "abcde*g"));
        Assert.assertEquals(0, forceSearch("abc*efg", "abcdefg"));
        Assert.assertEquals(0, forceSearch("abc*efg", "abceefg"));
    }

    @Test
    public void testMatchPerformance() {

    }

    public static boolean equal(char a, char b) {
        return WILD_CHAR == a || WILD_CHAR == b || a == b;
    }

    public static int bf(String text, String pattern) {
        int i = 0;
        int j = 0;

        char[] charsA = text.toCharArray();
        char[] charsB = pattern.toCharArray();
        int lengthA = charsA.length;
        int lengthB = charsB.length;

        while (i < lengthA && j < lengthB) {
            if (equal(charsA[i], charsB[j])) {
                i++;
                j++;
            } else {
                i = i - (j - 1);
                j = 0;
            }
        }

        if (j == lengthB) {
            return i - j;
        }

        return -1;
    }

    public static int forceSearch(String text, String pattern) {
        int lengthA = text.length();
        int lengthB = pattern.length();
        for (int i = 0; i <= lengthA - lengthB; i++) {
            int j;
            for (j = 0; j < lengthB; j++) {
                if (!equal(text.charAt(i + j), pattern.charAt(j))) {
                    break;
                }
            }
            if (j == lengthB) {
                return i;
            }
        }
        return -1;
    }

    public static int kmp(String text, String pattern) {

        return -1;
    }

    public static int bm(String text, String pattern) {
        return -1;
    }

    public static int sunday(String text, String pattern) {
        int i = 0;
        int j = 0;

        char[] charsA = text.toCharArray();
        char[] charsB = pattern.toCharArray();
        int lengthA = charsA.length;
        int lengthB = charsB.length;
        //
        //        List<Character> patternList=pattern.codePointAt()
        //        Map<Character,Integer> patternMap= IntStream.range(0,charsB.length).boxed()
        //                                                   .collect(Collectors.toMap(pattern::charAt, Functions
        //                                                   .identity()))

        while (i < lengthA && j < lengthB) {
            if (equal(charsA[i], charsB[j])) {
                i++;
                j++;
            } else {
                // 如果不匹配的话，看i+1 是否在p中，若不在则i从i+1开始，若在且最右位置为k，则i从i+1-k

                i = i - (j - 1);
                j = 0;
            }
        }

        if (j == lengthB) {
            return i - j;
        }

        return -1;
    }
}
