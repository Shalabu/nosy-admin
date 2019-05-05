package com.nosy.admin.nosyadmin.repository;

import com.nosy.admin.nosyadmin.model.InputSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.transaction.Transactional;

@Transactional
@Repository
@CrossOrigin
public interface InputSystemRepository extends JpaRepository<InputSystem, String> {

    @Query("from InputSystem where email=:email and " +
            "input_system_name=:inputSystemName")
    InputSystem findByInputSystemNameAndEmail(
            @Param("email") String email,
            @Param("inputSystemName") String inputSystemName);

    @Query("from InputSystem where email=:email and " +
            "input_system_id=:inputSystemId")
    InputSystem findByIdAndEmail(@Param("email") String email,
                                 @Param("inputSystemId") String inputSystemId);


}
