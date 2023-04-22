package com.swm47.swminder.Mentoring.repository;

import com.swm47.swminder.Mentoring.entity.Mentoring;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MentoringRepository extends CrudRepository<Mentoring, Long> {
    Optional<Mentoring> findByQustnrSn(int qustnrSn);
}
