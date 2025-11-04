package com.globalskills.booking_service.Common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopMentorResponse {
    private Long id;
    private String username;
    private String fullName;
    private String avatarUrl;
    private Long confirmedCount;
}
