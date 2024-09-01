package com.fds.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fds.model.Orders;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {

	@Query(
			value="SELECT * FROM orders WHERE order_id=?1",
			nativeQuery=true
	)
	List<Object[]> getSpecificOrderById(int order_id);
}