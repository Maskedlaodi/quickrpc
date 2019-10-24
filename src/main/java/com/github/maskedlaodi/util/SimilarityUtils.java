package com.github.maskedlaodi.util;

import com.github.maskedlaodi.common.impl.FrameworkAop;

import java.util.*;

public class SimilarityUtils {
    public static void main(String[] args) {
        FrameworkAop frameworkAop = new FrameworkAop();

        final Class<?>[] interfaces = frameworkAop.getClass().getInterfaces();
        for (Class<?> anInterface : interfaces) {
            final String[] split = anInterface.toString().split(" ");
            final String str = split[split.length - 1];
            System.out.println("str = " + str);
        }
    }

    public static String getSimilarestByClass(String pointStr, List<Class> strs) {
        Map<Float, String> map = new HashMap<>();

        strs.stream().map(e -> {
            map.put(levenshtein(pointStr, e.toString()), e.toString());
            return e;
        }).count();

        final Optional<Float> max = map.keySet().stream().max((e, f) -> (int) (e - f));

        return map.get(max.get());
    }

    public static String getSimilarest(String pointStr, List<String> strs) {
        Map<Float, String> map = new HashMap<>();

        strs.stream().map(e -> {
            map.put(levenshtein(pointStr, e), e);
            return e;
        }).count();

        final Optional<Float> max = map.keySet().stream().max((e, f) -> (int) (e - f));

        return map.get(max.get());
    }

    /**
     * 相识度, 对比
     */
    public static float levenshtein(String str1,String str2) {
        //计算两个字符串的长度。
        int len1 = str1.length();
        int len2 = str2.length();
        //建立上面说的数组，比字符长度大一个空间
        int[][] dif = new int[len1 + 1][len2 + 1];
        //赋初值，步骤B。
        for (int a = 0; a <= len1; a++) {
            dif[a][0] = a;
        }
        for (int a = 0; a <= len2; a++) {
            dif[0][a] = a;
        }
        //计算两个字符是否一样，计算左上的值
        int temp;
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                //取三个值中最小的
                dif[i][j] = min(dif[i - 1][j - 1] + temp, dif[i][j - 1] + 1,
                        dif[i - 1][j] + 1);
            }
        }
//        System.out.println("字符串\""+str1+"\"与\""+str2+"\"的比较");
        //取数组右下角的值，同样不同位置代表不同字符串的比较
//        System.out.println("差异步骤："+dif[len1][len2]);
        //计算相似度
        return  1 - (float) dif[len1][len2] / Math.max(str1.length(), str2.length());
    }

    //得到最小值
    private static int min(int... is) {
        int min = Integer.MAX_VALUE;
        for (int i : is) {
            if (min > i) {
                min = i;
            }
        }
        return min;
    }
}