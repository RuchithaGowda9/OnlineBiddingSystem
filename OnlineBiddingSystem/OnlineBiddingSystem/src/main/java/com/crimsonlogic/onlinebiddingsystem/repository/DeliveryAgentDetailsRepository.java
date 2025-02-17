package com.crimsonlogic.onlinebiddingsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.crimsonlogic.onlinebiddingsystem.dto.DeliveryAgentDTO;
import com.crimsonlogic.onlinebiddingsystem.entity.DeliveryAgentDetails;

@Repository
public interface DeliveryAgentDetailsRepository extends JpaRepository<DeliveryAgentDetails, Long> {
	@Query("SELECT d FROM DeliveryAgentDetails d WHERE d.deliveryAgencyName = :agencyName")
    List<DeliveryAgentDetails> findByDeliveryAgencyName(@Param("agencyName") String agencyName);
	

}
