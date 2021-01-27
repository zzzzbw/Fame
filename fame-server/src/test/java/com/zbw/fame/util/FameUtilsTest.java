package com.zbw.fame.util;

import com.zbw.fame.model.domain.Comment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ResolvableType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by zzzzbw
 * @since 2021/01/25 13:46
 */
@Slf4j
public class FameUtilsTest {


    public static void main(String[] args) {
        List<Comment> list = new ArrayList<>();

        ResolvableType resolvableType = ResolvableType.forInstance(list);
        System.out.println(resolvableType);
        Class<?> resolve = resolvableType.getGeneric(0).resolve();
        System.out.println(resolve);
    }

}
