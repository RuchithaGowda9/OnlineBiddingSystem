package com.crimsonlogic.onlinebiddingsystem.dto;

import lombok.Data;

/**
 * @author Ruchitha
 *
 */
@Data
public class UserDto {
	private Long id;
    private String email;
    private String password;
    private String roleName; 
}