package com.blog.model;

import java.util.HashMap;
import java.util.Map;

public class Page {

    public static Map<String, String> getPageDafaultParams() {
        Map<String, String> page = new HashMap<>();
        page.put("search", "true");
        page.put("menuItem", "blog");
        page.put("activeTab", "main");
        return page;
    }
}
