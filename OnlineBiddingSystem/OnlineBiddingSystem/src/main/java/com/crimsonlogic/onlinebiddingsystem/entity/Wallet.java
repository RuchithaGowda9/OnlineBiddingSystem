package com.crimsonlogic.onlinebiddingsystem.entity;

import com.crimsonlogic.onlinebiddingsystem.util.IdGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "wallet")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wallet {

	@Id
	private Long walletId;
	
	@OneToOne
    @JoinColumn(name = "user_id")
    private User user;

	@Column(name = "balance", length = 10)
	private Double balance;
	
	@PrePersist
	public void generateId() {
		this.walletId = (Long) new IdGenerator().generate(null, this);
		this.balance = 0.0;
	}
	
}
