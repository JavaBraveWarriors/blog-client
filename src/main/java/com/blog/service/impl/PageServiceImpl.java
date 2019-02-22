package com.blog.service.impl;

import com.blog.model.Pagination;
import com.blog.service.PageService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PageServiceImpl implements PageService {

    @Override
    public Pagination getPagination(Long size, Long totalPages, Long page) {
        Pagination pagination = new Pagination();
        pagination.setTotalPages(totalPages);
        pagination.setSize(size);
        pagination.setCurrentPage(page);
        return pagination;
    }

    @Override
    public Map<String, String> getPageDefaultParams() {
        Map<String, String> page = new HashMap<>();
        page.put("search", "true");
        page.put("menuItem", "blog");
        return page;
    }
}
