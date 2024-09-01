package com.icebuckwheat.oauthserver.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddinfoRequsetDto {
    private String userId;
    private String habit;
    private List<Object> favorite;
    private int height;
    private int weight;
}
