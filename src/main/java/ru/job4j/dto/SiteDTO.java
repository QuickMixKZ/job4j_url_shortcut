package ru.job4j.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SiteDTO {

    @NotBlank
    private String site;

}
