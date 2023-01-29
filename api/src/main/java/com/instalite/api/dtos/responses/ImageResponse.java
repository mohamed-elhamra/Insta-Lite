package com.instalite.api.dtos.responses;

import com.instalite.api.commons.utils.enums.EVisibility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponse {

    private String publicId;
    private String title;
    private EVisibility visibility;
    private String url;

}
