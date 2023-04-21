package com.swm47.swminder.Mentoring;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MentoringRepository extends CrudRepository<Mentoring, Long> {
    Optional<Mentoring> findByQustnrSn(int qustnrSn);
}
