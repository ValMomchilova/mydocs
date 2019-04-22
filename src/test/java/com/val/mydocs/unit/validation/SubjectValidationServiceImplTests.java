package com.val.mydocs.unit.validation;

import com.val.mydocs.domain.entities.Subject;
import com.val.mydocs.validation.SubjectValidationService;
import com.val.mydocs.validation.SubjectValidationServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class SubjectValidationServiceImplTests {
    private SubjectValidationService userValidationService;

    @Before
    public void init(){
        this.userValidationService = new SubjectValidationServiceImpl();
    }

    @Test
    public void isValidReturnsTrueForValidSubject(){
        Subject userServiceModel = new Subject();
        userServiceModel.setName("SubjectName");

        boolean result = this.userValidationService.isValid(userServiceModel);

        assertEquals(true, result);
    }

    @Test
    public void isValidReturnsFalseForNullSubject(){

        boolean result = this.userValidationService.isValid(null);

        assertEquals(false, result);
    }

    @Test
    public void isValidReturnsFalseForSubjectNullName(){

        Subject userServiceModel = new Subject();

        boolean result = this.userValidationService.isValid(userServiceModel);

        assertEquals(false, result);
    }

    @Test
    public void isValidReturnsFalseForSubjectEmptyName(){

        Subject userServiceModel = new Subject();
        userServiceModel.setName("");

        boolean result = this.userValidationService.isValid(userServiceModel);

        assertEquals(false, result);
    }

    @Test
    public void isValidReturnsFalseForSubjectNameLengthIsGreater(){

        Subject userServiceModel = new Subject();
        userServiceModel.setName("1234567890 1234567890 1234567890");

        boolean result = this.userValidationService.isValid(userServiceModel);

        assertEquals(false, result);
    }
}
