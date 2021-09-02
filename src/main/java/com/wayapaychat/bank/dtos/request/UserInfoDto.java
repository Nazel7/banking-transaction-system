package com.wayapaychat.bank.dtos.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoDto {

    private String phone;
    private String bvn;

}
