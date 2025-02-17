package com.crimsonlogic.onlinebiddingsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ruchitha
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDto {
    private UserDto user;
    private UserInfoDto userInfo;
}
