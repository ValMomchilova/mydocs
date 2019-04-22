package com.val.mydocs.validation;

import com.val.mydocs.domain.entities.SubjectType;
import org.springframework.stereotype.Component;

@Component
public class SubjectTypeValidationServiceImpl implements SubjectTypeValidationService {
    public static final int SUBJECT_TYPE_MAX_VALUE = 30;

    @Override
    public boolean isValid(SubjectType subjectType) {
        if (subjectType == null){
            return false;
        }
        if(subjectType.getTitle() == null
                || subjectType.getTitle().isEmpty()
                || subjectType.getTitle().length() > SUBJECT_TYPE_MAX_VALUE){
            return false;
        }
        return true;
    }
}
