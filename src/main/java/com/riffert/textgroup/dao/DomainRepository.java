package com.riffert.textgroup.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.riffert.textgroup.entity.Domain;


@Repository
public interface DomainRepository extends JpaRepository<Domain, Long>
{
	

}
