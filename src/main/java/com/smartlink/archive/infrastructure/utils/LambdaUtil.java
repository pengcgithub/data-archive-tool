package com.smartlink.archive.infrastructure.utils;


import org.apache.commons.collections.CollectionUtils;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * Created by pengcheng on 2024/5/6.
 */
public class LambdaUtil {

    // list to mapper
    public static <E, T, R> List<T> listMapper(Collection<E> from, Function<? super E, ? extends Stream<? extends R>> flatMapper, Function<? super R, T> mapper) {
        if (CollectionUtils.isEmpty(from)) {
            return new ArrayList<>();
        }
        return from.stream().flatMap(flatMapper).map(mapper).collect(Collectors.toList());
    }

    // list to mapper
    public static <E, T> List<T> listMapper(Collection<E> from, Function<? super E, T> mapper) {
        if (CollectionUtils.isEmpty(from)) {
            return new ArrayList<>();
        }
        return from.stream().map(mapper).collect(Collectors.toList());
    }


    //filter list to list
    public static <T> List<T> filterToList(Collection<T> from, Predicate<T> predicate) {
        if (CollectionUtils.isEmpty(from)) {
            return new ArrayList<>();
        }
        return from.stream().filter(predicate).collect(Collectors.toList());
    }

    //list to string
    public static <T> String list2str(Collection<T> list, String splitChar, Function<T, Object> function) {
        StringBuilder sb = new StringBuilder();
        T lastT = null;
        for (T t : list) {
            if (sb.length() > 0 && lastT != null) {
                sb.append(splitChar);
            }
            if (t != null) {
                Object obj = function.apply(t);
                if (obj != null) {
                    sb.append(obj);
                    lastT = t;
                }
            }
        }
        return sb.toString();
    }

    public static <T> String list2strStrengthen(Collection<T> list, String splitChar, Function<T, Object> function) {
        return CollectionUtils.isEmpty(list) ? "null" : list2str(list, splitChar, function);
    }

    ;

    //array to string
    public static <T> String array2str(T[] ts, String splitChar, Function<T, Object> function) {
        return list2str(Arrays.asList(ts), splitChar, function);
    }

    //for map to set
    public static <K extends Comparable, V, R> Collection<R> map2KeySet(Map<K, V> map, Function<K, R> func) {
        return map.entrySet().stream().map(kvEntry -> func.apply(kvEntry.getKey())).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    //for map to list
    public static <K extends Comparable, V, R> List<R> map2List(Map<K, V> map, BiFunction<K, V, R> func) {
        return map.entrySet().stream().map(kvEntry -> func.apply(kvEntry.getKey(), kvEntry.getValue())).collect(Collectors.toList());
    }

    //for map to map, same key
    public static <K, V, R> Map<K, R> map2map(Map<K, V> map, Function<V, R> funcValue) {
        Map<K, R> resultMap = new HashMap<>();
        map.forEach((k, v) -> {
            R r = funcValue.apply(v);
            resultMap.put(k, r);
        });
        return resultMap;
        //return map.entrySet().stream()
        //.collect(Collectors.toMap(Map.Entry::getKey, e -> funcValue.apply(e.getValue())));
    }

    //for map to list
    public static <K extends Comparable, V, R> List<R> map2ValueList(Map<K, V> map, Function<V, R> func) {
        return map2List(map, (k, v) -> func.apply(v));
    }

    //for list to map
    public static <K, R> Map<K, R> list2map(Collection<R> list, Function<R, K> func) {
        return list.stream().collect(Collectors.toMap(func, Function.identity(), (v1, v2) -> v1, LinkedHashMap::new));
    }

    //for list to map
    public static <K, V, R> Map<K, V> list2map(Collection<R> list, Function<R, K> funcKey, Function<R, V> funcValue) {
        return list.stream().collect(Collectors.toMap(funcKey, funcValue, (v1, v2) -> v1, LinkedHashMap::new));
    }

    //for list to list
    public static <T, U> List<U> list2list(Collection<T> from, Function<T, U> func) {
        return from.stream().map(func).collect(Collectors.toList());
    }

    public static <T, U> List<U> list2listStrengthen(Collection<T> from, Function<T, U> func) {
        if (CollectionUtils.isEmpty(from)) {
            return new ArrayList<>();
        }
        return from.stream().map(func).collect(Collectors.toList());
    }

    //for list to list
    public static <T, U> Set<U> list2set(Collection<T> from, Function<T, U> func) {
        return from.stream().map(func).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    //for array to list
    public static <T, U> List<U> array2list(T[] from, Function<T, U> func) {
        return Arrays.stream(from).map(func).collect(Collectors.toList());
    }

    //find max value
    public static <T> T maxValue(Collection<T> list, Comparator<T> comparator) {
        return maxValue(list.stream(), comparator);
    }


    //find max value
    public static <T> T maxValue(Stream<T> stream, Comparator<T> comparator) {
        Optional<T> optional = stream.max(comparator);
        return optional.orElse(null);
    }

    //find min value
    public static <T> T minValue(Collection<T> list, Comparator<T> comparator) {
        return minValue(list.stream(), comparator);
    }

    //find min value
    public static <T> T minValue(Stream<T> stream, Comparator<T> comparator) {
        Optional<T> optional = stream.min(comparator);
        return optional.orElse(null);
    }

    //calc sum value
    public static <T extends Number> long sumValue(List<T> list) {
        return list.stream().mapToLong(Number::longValue).sum();
    }
}
