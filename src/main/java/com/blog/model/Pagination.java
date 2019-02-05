package com.blog.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Pagination {
    private Long currentPage;
    private Long totalPages;
    private Long size;

    public Long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Long currentPage) {
        this.currentPage = currentPage;
    }

    public Long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Long totalPages) {
        this.totalPages = totalPages;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public List<Long> createPaginationArray() {
        if (totalPages > 7) {
            List<Long> head = (currentPage > 4) ?
                    Collections.unmodifiableList(Arrays.asList(1L, -1L)) :
                    Collections.unmodifiableList(Arrays.asList(1L, 2L, 3L));
            List<Long> tail = (currentPage < totalPages - 3) ?
                    Collections.unmodifiableList(Arrays.asList(-1L, totalPages)) :
                    Collections.unmodifiableList(Arrays.asList(totalPages - 2, totalPages - 1, totalPages));
            List<Long> bodyBefore = (currentPage > 4 && currentPage < totalPages - 1) ?
                    Collections.unmodifiableList(Arrays.asList(currentPage - 2, currentPage - 1)) :
                    Collections.emptyList();
            List<Long> bodyAfter = (currentPage > 2 && currentPage < totalPages - 3) ?
                    Collections.unmodifiableList(Arrays.asList(currentPage + 1, currentPage + 2)) :
                    Collections.emptyList();
            List<Long> body = new ArrayList<>();
            body.addAll(head);
            body.addAll(bodyBefore);
            if (currentPage > 3 && currentPage < totalPages - 2) {
                body.add(currentPage);
            }
            body.addAll(bodyAfter);
            body.addAll(tail);
            return body;

        } else {
            List<Long> body = new ArrayList<>();
            for (Long i = 1L; i <= totalPages; i++) {
                body.add(i);
            }
            return body;
        }
    }
}
