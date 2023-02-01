package com.instalite.api.dtos.responses;

import com.instalite.api.commons.utils.enums.EVisibility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponse {

    private String publicId;
    private String title;
    private EVisibility visibility;
    private String url;
    private Date creationDate;
    private String userPublicId;
    private String userFirstName;
    private String userLastName;

}
