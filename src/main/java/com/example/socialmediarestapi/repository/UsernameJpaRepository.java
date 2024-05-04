package com.example.socialmediarestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of methods all or almost all repositories use
 *
 * @param <D>
 * @param <I>
 */
@NoRepositoryBean
public interface UsernameJpaRepository<D, I> extends JpaRepository<D, I> {
    public Optional<D> findByUsername(String username);

    public Optional<List<D>> findAllByUsername(String username);


}
