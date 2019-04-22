package com.val.mydocs.serivce;

import com.val.mydocs.domain.entities.DocumentType;
import com.val.mydocs.domain.models.service.DocumentTypeServiceModel;
import com.val.mydocs.exceptions.ModelValidationException;
import com.val.mydocs.exceptions.UniqueFieldException;
import com.val.mydocs.repository.DocumentTypeRepository;
import com.val.mydocs.validation.DocumentTypeValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentTypeServiceImpl implements DocumentTypeService {
    private final DocumentTypeRepository documentTypeRepository;
    private final ModelMapper modelMapper;
    private final DocumentTypeValidationService documentTypeValidationService;

    @Autowired
    public DocumentTypeServiceImpl(DocumentTypeRepository documentTypeRepository,
                                   ModelMapper modelMapper,
                                   DocumentTypeValidationService documentTypeValidationService) {
        this.documentTypeRepository = documentTypeRepository;
        this.modelMapper = modelMapper;
        this.documentTypeValidationService = documentTypeValidationService;
    }

    @Override
    public DocumentTypeServiceModel addDocumentType(DocumentTypeServiceModel subjectTypeServiceModel) throws UniqueFieldException, ModelValidationException {
        DocumentType subjectType = this.modelMapper.map(subjectTypeServiceModel, DocumentType.class);
        DocumentType subjectSaved = saveDocumentType(subjectType);
        return this.modelMapper.map(subjectSaved, DocumentTypeServiceModel.class);
    }

    @Override
    public DocumentTypeServiceModel editDocumentType(DocumentTypeServiceModel subjectTypeServiceModel) throws UniqueFieldException, ModelValidationException {
        DocumentType subjectType = this.modelMapper.map(subjectTypeServiceModel, DocumentType.class);
        DocumentType subjectSaved = saveDocumentType(subjectType);
        return this.modelMapper.map(subjectSaved, DocumentTypeServiceModel.class);
    }

    @Override
    public void deleteDocumentType(String id) {
       this.documentTypeRepository.deleteById(id);
    }

    @Override
    public List<DocumentTypeServiceModel> findAllDocumentTypes() {
        return this.documentTypeRepository.findAll()
                .stream()
                .map(o -> this.modelMapper.map(o, DocumentTypeServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public DocumentTypeServiceModel findDocumentTypesById(String id) {
        DocumentType subjectType = this.documentTypeRepository.findById(id).orElse(null);
        if (subjectType == null){
            return null;
        }
        return this.modelMapper.map(subjectType, DocumentTypeServiceModel.class);
    }

    private DocumentType saveDocumentType(DocumentType documentType) throws UniqueFieldException, ModelValidationException {
        if (!this.documentTypeValidationService.isValid(documentType)){
            throw new ModelValidationException(documentType.getClass().getName(), documentType);
        }
        this.checkUniqueness(documentType);
        return this.documentTypeRepository.save(documentType);
    }

    private void checkUniqueness(DocumentType documentType) throws UniqueFieldException {
        DocumentType same = this.documentTypeRepository
                .findDocumentTypeByTitle(documentType.getTitle());
        if (same != null && !same.getId().equals(documentType.getId())){
            throw new UniqueFieldException(this.getClass().getName(), "title");
        }
    }
}
