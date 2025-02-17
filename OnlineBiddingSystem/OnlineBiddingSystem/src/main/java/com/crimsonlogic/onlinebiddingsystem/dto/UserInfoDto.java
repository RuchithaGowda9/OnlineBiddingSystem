package com.crimsonlogic.onlinebiddingsystem.dto;

import com.crimsonlogic.onlinebiddingsystem.entity.RoleName;

import lombok.Data;

/**
 * @author Ruchitha
 *
 */
@Data
public class UserInfoDto {
	private String firstName;
    private String lastName;
    private String phoneNumber;
    private RoleName roleName;
}