package com.blog.service;

import com.blog.model.Pagination;

import java.util.Map;

public interface PageService {
    Pagination getPagination(Long size, Long totalPages, Long page);

    Map<String, String> getPageDefaultParams();
}
