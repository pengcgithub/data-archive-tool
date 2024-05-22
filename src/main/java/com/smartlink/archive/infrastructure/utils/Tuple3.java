package com.smartlink.archive.infrastructure.utils;

/**
 * 元组三<br/>
 *
 * @author pengcheng@smartlink.com.cn
 * @date 2024/4/30 9:16 AM
 */
public class Tuple3<A, B, C> {

    public final A first;
    public final B second;
    public final C third;

    public Tuple3(A a, B b, C c) {
        this.first = a;
        this.second = b;
        this.third = c;
    }

}
