package com.val.mydocs.unit.validation;

import com.val.mydocs.domain.entities.DocumentType;
import com.val.mydocs.validation.DocumentTypeValidationService;
import com.val.mydocs.validation.DocumentTypeValidationServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class DocumentTypeValidationServiceImplTests {
    private DocumentTypeValidationService userValidationService;

    @Before
    public void init(){
        this.userValidationService = new DocumentTypeValidationServiceImpl();
    }

    @Test
    public void isValidReturnsTrueForValidDocumentType(){
        DocumentType userServiceModel = new DocumentType();
        userServiceModel.setTitle("DocumentTypeName");

        boolean result = this.userValidationService.isValid(userServiceModel);

        assertEquals(true, result);
    }

    @Test
    public void isValidReturnsFalseForNullDocumentType(){

        boolean result = this.userValidationService.isValid(null);

        assertEquals(false, result);
    }

    @Test
    public void isValidReturnsFalseForDocumentTypeNullTitle(){

        DocumentType userServiceModel = new DocumentType();

        boolean result = this.userValidationService.isValid(userServiceModel);

        assertEquals(false, result);
    }

    @Test
    public void isValidReturnsFalseForDocumentTypeEmptyTitle(){

        DocumentType userServiceModel = new DocumentType();
        userServiceModel.setTitle("");

        boolean result = this.userValidationService.isValid(userServiceModel);

        assertEquals(false, result);
    }

    @Test
    public void isValidReturnsFalseForDocumentTypeTitleLengthIsGreater(){

        DocumentType userServiceModel = new DocumentType();
        userServiceModel.setTitle("1234567890 1234567890 1234567890");

        boolean result = this.userValidationService.isValid(userServiceModel);

        assertEquals(false, result);
    }
}
