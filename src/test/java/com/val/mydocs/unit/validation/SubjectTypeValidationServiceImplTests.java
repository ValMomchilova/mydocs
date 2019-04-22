package com.val.mydocs.unit.validation;

import com.val.mydocs.domain.entities.SubjectType;
import com.val.mydocs.validation.SubjectTypeValidationService;
import com.val.mydocs.validation.SubjectTypeValidationServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class SubjectTypeValidationServiceImplTests {
    private SubjectTypeValidationService userValidationService;

    @Before
    public void init(){
        this.userValidationService = new SubjectTypeValidationServiceImpl();
    }

    @Test
    public void isValidReturnsTrueForValidSubjectType(){
        SubjectType userServiceModel = new SubjectType();
        userServiceModel.setTitle("SubjectTypeName");

        boolean result = this.userValidationService.isValid(userServiceModel);

        assertEquals(true, result);
    }

    @Test
    public void isValidReturnsFalseForNullSubjectType(){

        boolean result = this.userValidationService.isValid(null);

        assertEquals(false, result);
    }

    @Test
    public void isValidReturnsFalseForSubjectTypeNullTitle(){

        SubjectType userServiceModel = new SubjectType();

        boolean result = this.userValidationService.isValid(userServiceModel);

        assertEquals(false, result);
    }

    @Test
    public void isValidReturnsFalseForSubjectTypeEmptyTitle(){

        SubjectType userServiceModel = new SubjectType();
        userServiceModel.setTitle("");

        boolean result = this.userValidationService.isValid(userServiceModel);

        assertEquals(false, result);
    }

    @Test
    public void isValidReturnsFalseForSubjectTypeTitleLengthIsGreater(){

        SubjectType userServiceModel = new SubjectType();
        userServiceModel.setTitle("1234567890 1234567890 1234567890");

        boolean result = this.userValidationService.isValid(userServiceModel);

        assertEquals(false, result);
    }
}
