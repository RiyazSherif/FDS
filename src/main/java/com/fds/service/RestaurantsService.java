package com.fds.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fds.model.MenuItems;
import com.fds.exception.MenuNotFoundException;
import com.fds.exception.OrderNotFoundException;
import com.fds.exception.RestaurantNotFoundException;
import com.fds.model.Customers;
import com.fds.model.DeliveryAddresses;
import com.fds.model.DeliveryDrivers;
import com.fds.model.Orders;
import com.fds.model.Ratings;
import com.fds.model.Restaurants;
import com.fds.repository.RestaurantsRepository;

@Service
public class RestaurantsService {

	@Autowired
	private RestaurantsRepository restaurants_repository;
	
	// method to get all the restaurants
	public List<Restaurants> getAllRestaurants() {
		List<Restaurants> restaurants = restaurants_repository.findAll();
		
		// throw exception if the no restaurants are found
		if(restaurants.isEmpty()) throw new RestaurantNotFoundException("Restaurants list is empty", "GETALLFAILS");
		return restaurants;
	}
	
	// method to delete the specific restaurant
	public void deleteRestaurantById(int restaurant_id) {
		Restaurants restaurant = restaurants_repository.findById(restaurant_id).orElse(null);
		
		// throw exception if the restaurant is not found
		if(restaurant == null) throw new RestaurantNotFoundException("Restaurant doesn't exist with id: " +restaurant_id, "DELETEFAILS");
		
		restaurants_repository.deleteById(restaurant_id);
	}

	public Restaurants getRestaurantById(int restaurantId){
		return restaurants_repository.findById(restaurantId).orElse(null);
	}
	
	// method to get all the delivery areas served by the specific restaurant
	public List<DeliveryAddresses> getDeliveryAddressesOfSpecificRestaurant(int restaurant_id) {
		List<DeliveryAddresses> addresses = new ArrayList<>();
		
		Restaurants restaurant = restaurants_repository.findById(restaurant_id).orElse(null);
		// throw exception if the restaurant is not found
		if(restaurant == null) throw new RestaurantNotFoundException("Restaurant not found with id: " +restaurant_id, "GETALLFAILS");
			
		List<Orders> orders = restaurant.getOrders();
		// throw exception if orders list is empty
		if(orders.isEmpty()) throw new OrderNotFoundException("Orders list is empty", "GETALLFAILS");
		
		for(Orders order : orders) {
			for(DeliveryAddresses address : order.getCustomers().getDeliveryaddresses()) addresses.add(address);
		}
			
		return addresses;
	}
	
	// method to update the specific restaurant
	public void updateRestaurantById(Restaurants newRestaurant, int restaurantId) {
		Restaurants restaurant = restaurants_repository.findById(restaurantId).orElse(null);
		
		// throw exception if the restaurant is not found
		if(restaurant == null) throw new RestaurantNotFoundException("Restaurant not found with id: " +restaurantId, "UPDATEFAILS");
			
		restaurant.setRestaurant_name(newRestaurant.getRestaurant_name());
		restaurant.setRestaurant_phone(newRestaurant.getRestaurant_phone());
		restaurant.setRestaurant_address(newRestaurant.getRestaurant_address());
			
		restaurants_repository.save(restaurant);
	}

	public List<String> getAllRatingsOfRestaurant(int restaurantId) {
		List<String> ratingList = new ArrayList<>();
		Restaurants restaurant = restaurants_repository.findById(restaurantId).orElse(null);
		if(restaurant == null) {
			throw new RestaurantNotFoundException("Restaurant not found with id: " + restaurantId, "GETFAILS");
		}
		List<Ratings> ratings= restaurant.getRatings();
		for(Ratings currRating:ratings) {
			ratingList.add(currRating.getReview());
		}
		if(ratingList.isEmpty()) {
			throw new MenuNotFoundException("No review is found with the specific Restaurant Id: " + restaurantId, "GETFAILS");
		}
		return ratingList;
	}

	// method to get all the menu items of the specific restaurant
	public List<MenuItems> getMenuItemsByRestaurant(int restaurantId) {
		Restaurants restaurant = restaurants_repository.findById(restaurantId).orElse(null);
		if(restaurant == null) {
			throw new RestaurantNotFoundException("Restaurant not found with id: " +restaurantId, "GETFAILS");
		}
		return restaurant.getMenuitems();
	}
	
	// method to add menu item for the specific restaurant
	public void addMenuItemInSpecificRestaurant(int restaurant_id, MenuItems menuItem) {
		Restaurants restaurant = restaurants_repository.findById(restaurant_id).orElse(null);
		
		// throw exception if the restaurant is not found
		if(restaurant == null) throw new RestaurantNotFoundException("Restaurant not found with id: " +restaurant_id, "ADDFAILS");
		
		menuItem.setRestaurants(restaurant);
		restaurant.getMenuitems().add(menuItem);
		restaurants_repository.save(restaurant);
	}

	public Restaurants saveRestaurants(Restaurants restaurant) {
		return restaurants_repository.save(restaurant);
	}

	public void updateMenuItemOfRestaurant(Restaurants restaurant, int itemId, MenuItems menuItem) {
		List<MenuItems> menuItems = restaurant.getMenuitems();
		boolean found = false;
		for(MenuItems m: menuItems) {
			if(m.getItem_id() == itemId) {
				if(menuItem.getItem_name() != null) {
					m.setItem_name(menuItem.getItem_name());					
				}
				
				if(menuItem.getItem_description() != null) {
					m.setItem_description(menuItem.getItem_description());					
				}
				
				if(menuItem.getItem_price() != null) {
					m.setItem_price(menuItem.getItem_price());					
				}
				restaurants_repository.save(restaurant);
				found = true;
				break;
			}
		}
		
		if(!found) {
			throw new MenuNotFoundException("MenuItem of Id: " + itemId + " not found", "PUTFAILS");
		}
	}

}