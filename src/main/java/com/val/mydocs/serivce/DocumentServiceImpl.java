package com.val.mydocs.serivce;

import com.val.mydocs.domain.entities.Document;
import com.val.mydocs.domain.entities.DocumentType;
import com.val.mydocs.domain.entities.Subject;
import com.val.mydocs.domain.entities.User;
import com.val.mydocs.domain.models.service.DocumentServiceModel;
import com.val.mydocs.domain.models.service.SubjectServiceModel;
import com.val.mydocs.domain.models.service.UserServiceModel;
import com.val.mydocs.exceptions.ModelValidationException;
import com.val.mydocs.repository.DocumentRepository;
import com.val.mydocs.validation.DocumentValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final UserService userService;
    private final SubjectService subjectService;
    private final ModelMapper modelMapper;
    private final DateTimeService dateTimeService;
    private final DocumentValidationService documentValidationService;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository, UserService userService, SubjectService subjectService, ModelMapper modelMapper, DateTimeService dateTimeService, DocumentValidationService documentValidationService) {
        this.documentRepository = documentRepository;
        this.userService = userService;
        this.subjectService = subjectService;
        this.modelMapper = modelMapper;
        this.dateTimeService = dateTimeService;
        this.documentValidationService = documentValidationService;
    }

    @Override
    public DocumentServiceModel addDocument(DocumentServiceModel documentServiceModel, String username) throws ModelValidationException {
        UserServiceModel userServiceModel = this.userService.findUserByUserName(username);
        documentServiceModel.setUser(userServiceModel);
        Document document = this.modelMapper.map(documentServiceModel, Document.class);
        Document documentSaved = saveDocument(document);
        return this.modelMapper.map(documentSaved, DocumentServiceModel.class);
    }

    @Override
    public List<DocumentServiceModel> findAllDocumentsOrderByExpiredDate(String username) {
        User user = this.findUser(username);
        List<Document> userDocuments = this.documentRepository.findDocumentsByUserOrderByExpiredDate(user);
        return userDocuments
                .stream()
                .map(o -> this.modelMapper.map(o, DocumentServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public DocumentServiceModel findDocumentsById(String id, String username) {
        User user = this.findUser(username);
        Document document = this.documentRepository.findDocumentByIdAndAndUser(id, user);
        if (document == null) {
            return null;
        }
        return this.modelMapper.map(document, DocumentServiceModel.class);
    }

    @Override
    public void deleteDocument(String id, String username) {
        User user = this.findUser(username);
        Document document = this.documentRepository.findDocumentByIdAndAndUser(id, user);
        if (document == null) {
            throw new IllegalArgumentException("Document is not found or not belongs to the user");
        }
        //throws EmptyResultDataAccessException
        this.documentRepository.deleteById(id);
    }

    @Override
    public DocumentServiceModel editDocument(DocumentServiceModel documentServiceModel, String username) throws ModelValidationException {
        User user = this.findUser(username);
        Document document = this.documentRepository.findDocumentByIdAndAndUser(documentServiceModel.getId(), user);
        if (document == null) {
            throw new IllegalArgumentException("Document is not found or not belongs to the user");
        }
        document.setDocumentType(this.modelMapper.map(documentServiceModel.getDocumentType(), DocumentType.class));
        document.setTitle(documentServiceModel.getTitle());
        document.setDescription(documentServiceModel.getDescription());
        Document documentSaved = saveDocument(document);
        return this.modelMapper.map(documentSaved, DocumentServiceModel.class);
    }

    @Override
    public List<Object> findAllBySubjectOrderByExpiredDate(String subjectId, String username) {
        User user = this.findUser(username);
        SubjectServiceModel subjectServiceModel = this.subjectService.findSubjectsById(subjectId, username);
        if (subjectServiceModel == null) {
            throw new IllegalArgumentException("Subject is not found or not belongs to the user");
        }
        Subject subject = this.modelMapper.map(subjectServiceModel, Subject.class);
        List<Document> userDocuments = this.documentRepository
                .findDocumentsByUserAndAndSubjectOrderByExpiredDate(user, subject);
        return userDocuments
                .stream()
                .map(o -> this.modelMapper.map(o, DocumentServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DocumentServiceModel renewDocument(DocumentServiceModel documentServiceModel, String username) throws ModelValidationException {
        User user = this.findUser(username);
        Document document = this.documentRepository.findDocumentByIdAndAndUser(documentServiceModel.getId(), user);
        if (document == null) {
            throw new IllegalArgumentException("Document is not found or not belongs to the user");
        }
        document.setRenewDate(this.dateTimeService.getCurrentDate());
        Document documentSaved = saveDocument(document);

        Document documenToRenew = new Document();
        documenToRenew.setDescription(document.getDescription());
        documenToRenew.setDocumentType(document.getDocumentType());
        documenToRenew.setTitle(document.getTitle());
        documenToRenew.setDate(document.getExpiredDate());
        documenToRenew.setExpiredDate(document.getExpiredDate().plusYears(1));
        documenToRenew.setSubject(document.getSubject());
        documenToRenew.setUser(user);

        Document documentNew = saveDocument(documenToRenew);

        return this.modelMapper.map(documentNew, DocumentServiceModel.class);
    }

    @Override
    // at 12:00 AM every day
    @Scheduled(cron = "0 0 0 * * *")
    public void AutoRenewDocuments() throws ModelValidationException {
        System.out.println("schedule");
        List<Document> documentsToRenew = this.documentRepository
                .findDocumentByRenewDateAndAutoRenewAndExpiredDateIsBefore(null,
                        true
                        , this.dateTimeService.getCurrentDate());
        for (Document document : documentsToRenew) {
            this.renewDocument(this.modelMapper.map(document, DocumentServiceModel.class),
                    document.getUser().getUsername());
        }

    }

    private Document saveDocument(Document document) throws ModelValidationException {
        if (!this.documentValidationService.isValid(document)){
            throw new ModelValidationException(document.getClass().getName(), document);
        }
        return this.documentRepository.save(document);
    }

    private User findUser(String username) {
        UserServiceModel userServiceModel = this.userService.findUserByUserName(username);
        return this.modelMapper.map(userServiceModel, User.class);
    }
}
