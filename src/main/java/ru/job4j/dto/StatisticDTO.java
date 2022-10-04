package ru.job4j.dto;

import lombok.Data;

@Data
public class StatisticDTO {

    private String url;
    private int total;

    public StatisticDTO(String url, int total) {
        this.url = url;
        this.total = total;
    }
}
