package com.blog.service;

import com.blog.model.Pagination;
import org.springframework.ui.Model;

import java.util.Map;

public interface PageService {
    Pagination getPagination(Long size, Long totalPages, Long page);

    void setDefaultModelAttributes(Model model);

    Map<String, String> getPageDefaultParams();

}
