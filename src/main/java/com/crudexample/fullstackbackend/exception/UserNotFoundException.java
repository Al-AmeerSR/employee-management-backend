package com.crudexample.fullstackbackend.exception;

public class UserNotFoundException extends RuntimeException{

		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		public UserNotFoundException(Long id) {
			super("could not found the user with id:"+id);
		}
}
