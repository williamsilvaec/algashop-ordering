package com.williamsilva.algashop.ordering.presentation;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class PageModel<T> {

    private int number;
    private int size;
    private int totalPages;
    private long totalElements;

    private List<T> content = new ArrayList<>();

    public static <T> PageModel<T> of(Page<T> page) {
        PageModel<T> pageModel = new PageModel<>();
        pageModel.number = page.getNumber();
        pageModel.size = page.getSize();
        pageModel.totalPages = page.getTotalPages();
        pageModel.totalElements = page.getTotalElements();
        pageModel.content = page.getContent();
        return pageModel;
    }

    public int getNumber() {
        return number;
    }

    public int getSize() {
        return size;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public List<T> getContent() {
        return content;
    }
}
