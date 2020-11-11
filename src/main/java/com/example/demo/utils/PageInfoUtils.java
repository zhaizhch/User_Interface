package com.example.demo.utils;

import com.example.demo.entity.PageResult;
import com.github.pagehelper.PageInfo;

import java.util.List;

public class PageInfoUtils {
    public static PageResult getPageResult(PageInfo<?> pageInfo, List<?> content) {
        PageResult pageResult = new PageResult();
        pageResult.setPageNum(pageInfo.getPageNum());
        pageResult.setPageSize(pageInfo.getPageSize());
        pageResult.setTotalSize(pageInfo.getTotal());
        pageResult.setTotalPages(pageInfo.getPages());
        pageResult.setContent(content);
        return pageResult;
    }
}
