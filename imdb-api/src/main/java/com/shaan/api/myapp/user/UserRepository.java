package com.shaan.api.myapp.user;

import com.shaan.api.myapp.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    /**
     * This method will find an Users instance in the database by its email.
     * Note that this method is not implemented and its working code will be
     * automagically generated from its signature by Spring Data JPA.
     */
    @Query(value = "SELECT a FROM User a WHERE a.email = :email")
    User findByEmail(@Param("email") String email);

    @Override
    User save(User user);

    @Override
    void delete(User user);

    User findFirstByEmail(String email);

    @Query(value = "SELECT a FROM User a where a.accessToken = :accessToken")
    User findByAccessToken(@Param("accessToken") String accessToken);

    @Query(value = "SELECT a FROM User a where a.email = :email and a.password = :password")
    User findByEmailAndPassword(@Param("email") String email, @Param("password")String password);

}
