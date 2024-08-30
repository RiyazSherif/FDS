package com.fds.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fds.model.MenuItems;
import com.fds.model.Customers;
import com.fds.model.DeliveryAddresses;
import com.fds.model.DeliveryDrivers;
import com.fds.model.Orders;
import com.fds.model.Restaurants;
import com.fds.repository.RestaurantsRepository;

@Service
public class RestaurantsService {

	@Autowired
	private RestaurantsRepository restaurants_repository;
	
	public List<Restaurants> getAllRestaurants() {
		return restaurants_repository.getAllRestaurants();
	}
	
	public void deleteRestaurantById(int restaurant_id) {
		restaurants_repository.deleteRestaurantById(restaurant_id);

	public Restaurants getRestaurantById(int restaurantId){
		return restaurants_repository.findById(restaurantId).orElse(null);
		
	}
	
	public List<MenuItems> getAllMenuItemsByRestaurant(Restaurants restaurant){
		return restaurants_repository.getAllMenuItemsByRestaurant(restaurant);
  }
  
	public List<DeliveryAddresses> getDeliveryAddresses(int restaurantId) {
		Optional<Restaurants> rs = restaurants_repository.findById(restaurantId);
		List<DeliveryAddresses> list = new ArrayList<>();
		if(rs.isPresent()) {
			Restaurants restaurant = rs.get();
			
			for(Orders order:restaurant.getOrders()) {
				
				for(DeliveryAddresses address: order.getCustomers().getDeliveryaddresses()) {
					list.add(address);
				}
			}
			
		}
		return list;
	}
	
	
	public Restaurants updateRestaurantById(Restaurants newRestaurant, int restaurantId) {
		
		Optional<Restaurants> restaurant = restaurants_repository.findById(restaurantId);
		if(restaurant.isPresent()) {
			Restaurants oldRestaurant = restaurant.get();
			oldRestaurant.setRestaurant_name(newRestaurant.getRestaurant_name());
			oldRestaurant.setRestaurant_phone(newRestaurant.getRestaurant_phone());
			oldRestaurant.setRestaurant_address(newRestaurant.getRestaurant_address());
			
			return restaurants_repository.save(oldRestaurant);
		}
		else return null;
	}
}