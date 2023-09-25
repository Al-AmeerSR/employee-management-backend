package com.crudexample.fullstackbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crudexample.fullstackbackend.model.Cuser;
@Repository
public interface UserRepository extends JpaRepository<Cuser, Long> {

}
