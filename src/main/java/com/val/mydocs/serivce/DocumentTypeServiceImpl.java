package com.val.mydocs.serivce;

import com.val.mydocs.domain.entities.DocumentType;
import com.val.mydocs.domain.models.service.DocumentTypeServiceModel;
import com.val.mydocs.exceptions.UniqueFieldException;
import com.val.mydocs.repository.DocumentTypeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentTypeServiceImpl implements DocumentTypeService {
    private final DocumentTypeRepository subjectTypeRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public DocumentTypeServiceImpl(DocumentTypeRepository subjectTypeRepository, ModelMapper modelMapper) {
        this.subjectTypeRepository = subjectTypeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public DocumentTypeServiceModel addDocumentType(DocumentTypeServiceModel subjectTypeServiceModel) throws UniqueFieldException {
        DocumentType subjectType = this.modelMapper.map(subjectTypeServiceModel, DocumentType.class);
        DocumentType subjectSaved = saveDocumentType(subjectType);
        return this.modelMapper.map(subjectSaved, DocumentTypeServiceModel.class);
    }

    @Override
    public DocumentTypeServiceModel editDocumentType(DocumentTypeServiceModel subjectTypeServiceModel) throws UniqueFieldException {
        DocumentType subjectType = this.modelMapper.map(subjectTypeServiceModel, DocumentType.class);
        DocumentType subjectSaved = saveDocumentType(subjectType);
        return this.modelMapper.map(subjectSaved, DocumentTypeServiceModel.class);
    }

    @Override
    public void deleteDocumentType(String id) {
        //throws EmptyResultDataAccessException
       this.subjectTypeRepository.deleteById(id);
    }

    @Override
    public List<DocumentTypeServiceModel> findAllDocumentTypes() {
        return this.subjectTypeRepository.findAll()
                .stream()
                .map(o -> this.modelMapper.map(o, DocumentTypeServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public DocumentTypeServiceModel findDocumentTypesById(String id) {
        DocumentType subjectType = this.subjectTypeRepository.findById(id).orElse(null);
        if (subjectType == null){
            return null;
        }
        return this.modelMapper.map(subjectType, DocumentTypeServiceModel.class);
    }

    private DocumentType saveDocumentType(DocumentType documentType) throws UniqueFieldException {
        this.checkUniqueness(documentType);
        return this.subjectTypeRepository.save(documentType);
    }

    private void checkUniqueness(DocumentType documentType) throws UniqueFieldException {
        DocumentType same = this.subjectTypeRepository
                .findDocumentTypeByTitle(documentType.getTitle()).orElse(null);
        if (same != null){
            throw new UniqueFieldException(this.getClass().getName(), "title");
        }
    }
}
