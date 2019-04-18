package com.val.mydocs.serivce;

import com.val.mydocs.domain.entities.Subject;
import com.val.mydocs.domain.entities.SubjectType;
import com.val.mydocs.domain.entities.User;
import com.val.mydocs.domain.models.service.SubjectServiceModel;
import com.val.mydocs.domain.models.service.UserServiceModel;
import com.val.mydocs.repository.SubjectRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public SubjectServiceImpl(SubjectRepository subjectRepository, UserService userService, ModelMapper modelMapper) {
        this.subjectRepository = subjectRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public SubjectServiceModel addSubject(SubjectServiceModel subjectServiceModel, String username) {
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
            throw new IllegalArgumentException("Subject is not found or not belongs to the user");
        }
        //throws EmptyResultDataAccessException
        this.subjectRepository.deleteById(id);
    }

    @Override
    public SubjectServiceModel editSubject(SubjectServiceModel subjectServiceModel, String username) {
        UserServiceModel userServiceModel = this.userService.findUserByUserName(username);
        User user = this.modelMapper.map(userServiceModel, User.class);
        Subject subject = this.subjectRepository.findSubjectByIdAndAndUser(subjectServiceModel.getId(), user);
        if (subject == null){
            throw new IllegalArgumentException("Subject is not found or not belongs to the user");
        }
        subject.setSubjectType(this.modelMapper.map(subjectServiceModel.getSubjectType(), SubjectType.class));
        subject.setName(subjectServiceModel.getName());
        subject.setDescription(subjectServiceModel.getDescription());
        Subject subjectSaved = saveSubject(subject);
        return this.modelMapper.map(subjectSaved, SubjectServiceModel.class);
    }

    private Subject saveSubject(Subject subject) {
        return this.subjectRepository.save(subject);
    }
}
