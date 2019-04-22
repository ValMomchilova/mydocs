package com.val.mydocs.validation;

import com.val.mydocs.domain.entities.Subject;
import org.springframework.stereotype.Component;

@Component
public class SubjectValidationServiceImpl implements SubjectValidationService {
    public static final int SUBJECT_MAX_VALUE = 30;

    @Override
    public boolean isValid(Subject subject) {
        if (subject == null){
            return false;
        }
        if(subject.getName() == null
                || subject.getName().isEmpty()
                || subject.getName().length() > SUBJECT_MAX_VALUE){
            return false;
        }
        return true;
    }
}
