package org.zerock.b01.dto.upload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UploadResultDTO {

    private String uuid;

    private String fileName;

    private boolean img;

    //getLink()는 나중에 JSON으로 처리될 때는 link라는 속성으로 자동 처리됩니다.
    public String getLink() {
        if (img) {
            return "s_" + uuid + "_" + fileName;//이미지인 경우 썸네일
        } else {
            return uuid+"_"+fileName;
        }
    }
}
