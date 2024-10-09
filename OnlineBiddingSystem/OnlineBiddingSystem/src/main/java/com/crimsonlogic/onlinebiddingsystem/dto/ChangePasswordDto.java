package com.crimsonlogic.onlinebiddingsystem.dto;

import lombok.Data;

/**
 * @author Ruchitha
 *
 */
@Data
public class ChangePasswordDto {
    private String currentPassword;
    private String newPassword;
}
