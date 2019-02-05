package com.blog.model;

import java.util.Random;

public enum Color {
    DEFAULT(1, "badge-default"),
    PRIMARY(2, "badge-primary"),
    SECONDARY(3, "badge-secondary"),
    SUCCESS(4, "badge-success"),
    DANGER(5, "badge-danger"),
    WARNING(6, "badge-warning"),
    DARK(7, "badge-dark");

    private int number;
    private String cssClass;

    Color(int number, String cssClass) {
        this.number = number;
        this.cssClass = cssClass;
    }

    public static String getRandomCssClassColor() {
        Random random = new Random();
        int randomInt = random.nextInt(7);
        for (Color color : Color.values()) {
            if (color.number == randomInt)
                return color.cssClass;
        }
        return DEFAULT.cssClass;
    }
}
