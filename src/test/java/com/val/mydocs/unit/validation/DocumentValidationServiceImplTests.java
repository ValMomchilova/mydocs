package com.val.mydocs.unit.validation;

import com.val.mydocs.domain.entities.Document;
import com.val.mydocs.validation.DocumentValidationService;
import com.val.mydocs.validation.DocumentValidationServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class DocumentValidationServiceImplTests {
    private DocumentValidationService userValidationService;

    @Before
    public void init(){
        this.userValidationService = new DocumentValidationServiceImpl();
    }

    @Test
    public void isValidReturnsTrueForValidDocument(){
        Document userServiceModel = new Document();
        userServiceModel.setTitle("DocumentName");

        boolean result = this.userValidationService.isValid(userServiceModel);

        assertEquals(true, result);
    }

    @Test
    public void isValidReturnsFalseForNullDocument(){

        boolean result = this.userValidationService.isValid(null);

        assertEquals(false, result);
    }

    @Test
    public void isValidReturnsFalseForDocumentNullTitle(){

        Document userServiceModel = new Document();

        boolean result = this.userValidationService.isValid(userServiceModel);

        assertEquals(false, result);
    }

    @Test
    public void isValidReturnsFalseForDocumentEmptyTitle(){

        Document userServiceModel = new Document();
        userServiceModel.setTitle("");

        boolean result = this.userValidationService.isValid(userServiceModel);

        assertEquals(false, result);
    }

    @Test
    public void isValidReturnsFalseForDocumentTitleLengthIsGreater(){

        Document userServiceModel = new Document();
        userServiceModel.setTitle("1234567890 1234567890 1234567890");

        boolean result = this.userValidationService.isValid(userServiceModel);

        assertEquals(false, result);
    }
}
