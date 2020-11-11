package com.example.demo.utils;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class CommonUtils {
    /**
     * 修改起止时间参数
     *
     * @param queryAlgoResultDetailDto
     * @param stage
     * @return com.ehualu.algorithm_result_display.entity.dto.QueryAlgoResultDetailDto
     * @date 2020/7/23
     */


    /**
     * 使用Map按value拼音首字母进行排序
     *

     * @return java.util.Map<java.lang.String, java.lang.String>
     * @date 2020/8/10
     */

    public static String encodeByMd5(String str) {
        return DigestUtils.md5DigestAsHex(str.getBytes(StandardCharsets.UTF_8));
    }

    public static String getRandomStr() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
