package com.nightonke.saver;

import java.util.Comparator;
import java.util.Map;

/**
 * Created by 伟平 on 2015/11/1.
 */
public class SortMapByValue implements Comparator<String> {

    Map<String, Double> map;

    public SortMapByValue(Map<String, Double> base) {
        this.map = base;
    }

    public int compare(String a, String b) {
        return Double.compare(map.get(a), map.get(b));
    }
}