package com.val.mydocs.serivce;

import com.val.mydocs.domain.entities.Document;
import com.val.mydocs.domain.entities.DocumentType;
import com.val.mydocs.domain.entities.Subject;
import com.val.mydocs.domain.entities.User;
import com.val.mydocs.domain.models.service.DocumentServiceModel;
import com.val.mydocs.domain.models.service.SubjectServiceModel;
import com.val.mydocs.domain.models.service.UserServiceModel;
import com.val.mydocs.repository.DocumentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.print.Doc;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final UserService userService;
    private final SubjectService subjectService;
    private final ModelMapper modelMapper;
    private final DateTimeService dateTimeService;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository, UserService userService, SubjectService subjectService, ModelMapper modelMapper, DateTimeService dateTimeService) {
        this.documentRepository = documentRepository;
        this.userService = userService;
        this.subjectService = subjectService;
        this.modelMapper = modelMapper;
        this.dateTimeService = dateTimeService;
    }

    @Override
    public DocumentServiceModel addDocument(DocumentServiceModel documentServiceModel, String username) {
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
        if (document == null){
            return null;
        }
        return this.modelMapper.map(document, DocumentServiceModel.class);
    }

    @Override
    public void deleteDocument(String id, String username) {
        User user = this.findUser(username);
        Document document = this.documentRepository.findDocumentByIdAndAndUser(id, user);
        if (document == null){
            throw new IllegalArgumentException("Document is not found or not belongs to the user");
        }
        //throws EmptyResultDataAccessException
        this.documentRepository.deleteById(id);
    }

    @Override
    public DocumentServiceModel editDocument(DocumentServiceModel documentServiceModel, String username) {
        User user = this.findUser(username);
        Document document = this.documentRepository.findDocumentByIdAndAndUser(documentServiceModel.getId(), user);
        if (document == null){
            throw new IllegalArgumentException("Document is not found or not belongs to the user");
        }
        document.setDocumentType(this.modelMapper.map(documentServiceModel.getDocumentType(), DocumentType.class));
        document.setTitle(documentServiceModel.getTitle());
        document.setDescription(documentServiceModel.getDescription());
        Document documentSaved = saveDocument(document);
        return this.modelMapper.map(documentSaved, DocumentServiceModel.class);
    }

    @Override
    public List<Object> findAllBySubject(String subjectId, String username) {
        User user = this.findUser(username);
        SubjectServiceModel subjectServiceModel = this.subjectService.findSubjectsById(subjectId, username);
        if (subjectServiceModel == null){
            throw new IllegalArgumentException("Subject is not found or not belongs to the user");
        }
        Subject subject = this.modelMapper.map(subjectServiceModel, Subject.class);
        List<Document> userDocuments = this.documentRepository.findDocumentsByUserAndAndSubject(user, subject);
        return userDocuments
                .stream()
                .map(o -> this.modelMapper.map(o, DocumentServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DocumentServiceModel renewDocument(DocumentServiceModel documentServiceModel, String username) {
        User user = this.findUser(username);
        Document document = this.documentRepository.findDocumentByIdAndAndUser(documentServiceModel.getId(), user);
        if (document == null){
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

    private Document saveDocument(Document document) {
        return this.documentRepository.save(document);
    }

    private User findUser(String username) {
        UserServiceModel userServiceModel = this.userService.findUserByUserName(username);
        return this.modelMapper.map(userServiceModel, User.class);
    }
}
