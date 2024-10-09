package com.crimsonlogic.onlinebiddingsystem.entity;

import com.crimsonlogic.onlinebiddingsystem.util.IdGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
	
	@Id
	private Long userId;
	
	@Column(name = "email", length = 100, nullable = false, unique = true)
	private String email;
	
	@Column(name = "password", length = 100, nullable = false)
	private String password;
	
	@PrePersist
    public void generateId() {
        this.userId = (Long) new IdGenerator().generate(null, this);
    }
	
	public User(Long id) {
        this.userId = id;
    }
	
	
}
