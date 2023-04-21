package com.swm47.swminder.Mentoring.service;

import com.swm47.swminder.Mentoring.entity.Mentoring;
import com.swm47.swminder.Mentoring.repository.MentoringRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MentoringService {
    private final MentoringRepository mentoringRepository;

    @Transactional
    public Long upsert(Mentoring mentoring) {
//        log.info("MentoringService.upsert");
        Mentoring foundMentoring = mentoringRepository.findByQustnrSn(mentoring.getQustnrSn()).orElseGet(Mentoring::new);
        foundMentoring.overwrite(mentoring);
        if (foundMentoring.getMentoringId() == null) {
//            log.info("\tinsert mentoring");
            mentoringRepository.save(foundMentoring);
        } else {
//            log.info("\toverwrite mentoring");
        }
        return foundMentoring.getMentoringId();
    }
}
