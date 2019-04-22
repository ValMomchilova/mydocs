package com.val.mydocs.serivce;

import com.val.mydocs.domain.entities.Subject;
import com.val.mydocs.domain.entities.SubjectType;
import com.val.mydocs.domain.entities.User;
import com.val.mydocs.domain.models.service.SubjectServiceModel;
import com.val.mydocs.domain.models.service.UserServiceModel;
import com.val.mydocs.exceptions.ModelValidationException;
import com.val.mydocs.exceptions.UniqueFieldException;
import com.val.mydocs.repository.SubjectRepository;
import com.val.mydocs.validation.SubjectValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectServiceImpl implements SubjectService {
    public static final String SUBJECT_IS_NOT_FOUND_OR_NOT_BELONGS_TO_THE_USER_MESSAGE = "Subject is not found or not belongs to the user";
    private final SubjectRepository subjectRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final SubjectValidationService subjectValidationService;

    @Autowired
    public SubjectServiceImpl(SubjectRepository subjectRepository,
                              UserService userService,
                              ModelMapper modelMapper,
                              SubjectValidationService subjectValidationService) {
        this.subjectRepository = subjectRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.subjectValidationService = subjectValidationService;
    }

    @Override
    public SubjectServiceModel addSubject(SubjectServiceModel subjectServiceModel, String username) throws UniqueFieldException, ModelValidationException {
        UserServiceModel userServiceModel = this.userService.findUserByUserName(username);
        subjectServiceModel.setUser(userServiceModel);
        Subject subject = this.modelMapper.map(subjectServiceModel, Subject.class);
        Subject subjectSaved = saveSubject(subject);
        return this.modelMapper.map(subjectSaved, SubjectServiceModel.class);
    }

    @Override
    public List<SubjectServiceModel> findAllSubjects(String username) {
        UserServiceModel userServiceModel = this.userService.findUserByUserName(username);
        User user = this.modelMapper.map(userServiceModel, User.class);
        List<Subject> userSubjects = this.subjectRepository.findSubjectsByUser(user);
        return userSubjects
                .stream()
                .map(o -> this.modelMapper.map(o, SubjectServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<SubjectServiceModel> findAllSubjectsBySubjectTypeOrder(String username) {
        List<SubjectServiceModel> subjectServiceModels = this.findAllSubjects(username);
        List<SubjectServiceModel> orderSubjects = subjectServiceModels.stream()
                .sorted(Comparator.comparingInt(s -> s.getSubjectType().getTypeOrder()))
                .collect(Collectors.toList());
        return orderSubjects;
    }

    @Override
    public SubjectServiceModel findSubjectsById(String id, String username) {
        UserServiceModel userServiceModel = this.userService.findUserByUserName(username);
        User user = this.modelMapper.map(userServiceModel, User.class);
        Subject subject = this.subjectRepository.findSubjectByIdAndAndUser(id, user);
        if (subject == null){
            return null;
        }
        return this.modelMapper.map(subject, SubjectServiceModel.class);
    }

    @Override
    public void deleteSubject(String id, String username) {
        UserServiceModel userServiceModel = this.userService.findUserByUserName(username);
        User user = this.modelMapper.map(userServiceModel, User.class);
        Subject subject = this.subjectRepository.findSubjectByIdAndAndUser(id, user);
        if (subject == null){
            throw new IllegalArgumentException(SUBJECT_IS_NOT_FOUND_OR_NOT_BELONGS_TO_THE_USER_MESSAGE);
        }
        this.subjectRepository.deleteById(id);
    }

    @Override
    public SubjectServiceModel editSubject(SubjectServiceModel subjectServiceModel, String username) throws UniqueFieldException, ModelValidationException {
        UserServiceModel userServiceModel = this.userService.findUserByUserName(username);
        User user = this.modelMapper.map(userServiceModel, User.class);
        Subject subject = this.subjectRepository.findSubjectByIdAndAndUser(subjectServiceModel.getId(), user);
        if (subject == null){
            throw new IllegalArgumentException(SUBJECT_IS_NOT_FOUND_OR_NOT_BELONGS_TO_THE_USER_MESSAGE);
        }
        subject.setSubjectType(this.modelMapper.map(subjectServiceModel.getSubjectType(), SubjectType.class));
        subject.setName(subjectServiceModel.getName());
        subject.setDescription(subjectServiceModel.getDescription());
        Subject subjectSaved = saveSubject(subject);
        return this.modelMapper.map(subjectSaved, SubjectServiceModel.class);
    }

    private Subject saveSubject(Subject subject) throws UniqueFieldException, ModelValidationException {
        if (!this.subjectValidationService.isValid(subject)){
            throw new ModelValidationException(subject.getClass().getName(), subject);
        }
        this.checkUniqueness(subject);
        return this.subjectRepository.save(subject);
    }

    private void checkUniqueness(Subject subject) throws UniqueFieldException {
        Subject same = this.subjectRepository.findSubjectByName(subject.getName());
        if (same != null && !same.getId().equals(subject.getId())){
            throw new UniqueFieldException(this.getClass().getName(), "name");
        }
    }
}
