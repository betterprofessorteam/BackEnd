package com.israel.betterprofessor.repository

import com.israel.betterprofessor.model.Role
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional

interface RoleRepository : CrudRepository<Role, Long> {

    fun findByName(name: String): Role

    @Transactional
    @Modifying
    @Query(value = "DELETE from user_roles where user_id = :userId", nativeQuery = true)
    fun deleteAllUserRoleByUserId(userId: Long)

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO user_roles(user_id, role_id) values (:userId, :roleId)", nativeQuery = true)
    fun insertUserRole(userId: Long, roleId: Long)
}