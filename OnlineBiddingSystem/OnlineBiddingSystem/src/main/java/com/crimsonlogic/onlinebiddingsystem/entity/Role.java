package com.crimsonlogic.onlinebiddingsystem.entity;

import com.crimsonlogic.onlinebiddingsystem.util.IdGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ruchitha
 *
 */
@Entity
@Table(name = "role")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
	@Id
	private Long roleId;
	
	@Enumerated(EnumType.STRING)
    @Column(unique = true)
    private RoleName name;
	
	@PrePersist
    public void generateId() {
        this.roleId = (Long) new IdGenerator().generate(null, this);
    }

	public Role(Long roleId2) {
		
	}
}
