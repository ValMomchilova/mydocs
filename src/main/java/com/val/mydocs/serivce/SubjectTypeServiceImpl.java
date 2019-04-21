package com.val.mydocs.serivce;

import com.val.mydocs.domain.entities.SubjectType;
import com.val.mydocs.domain.models.service.SubjectTypeServiceModel;
import com.val.mydocs.exceptions.UniqueFieldException;
import com.val.mydocs.repository.SubjectTypeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectTypeServiceImpl implements SubjectTypeService {
    private final SubjectTypeRepository subjectTypeRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public SubjectTypeServiceImpl(SubjectTypeRepository subjectTypeRepository, ModelMapper modelMapper) {
        this.subjectTypeRepository = subjectTypeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public SubjectTypeServiceModel addSubjectType(SubjectTypeServiceModel subjectTypeServiceModel) throws UniqueFieldException {
        SubjectType subjectType = this.modelMapper.map(subjectTypeServiceModel, SubjectType.class);
        SubjectType subjectSaved = saveSubjectType(subjectType);
        return this.modelMapper.map(subjectSaved, SubjectTypeServiceModel.class);
    }

    @Override
    public SubjectTypeServiceModel editSubjectType(SubjectTypeServiceModel subjectTypeServiceModel) throws UniqueFieldException {
        SubjectType subjectType = this.modelMapper.map(subjectTypeServiceModel, SubjectType.class);
        SubjectType subjectSaved = saveSubjectType(subjectType);
        return this.modelMapper.map(subjectSaved, SubjectTypeServiceModel.class);
    }

    @Override
    public void deleteSubjectType(String id) {
        //throws EmptyResultDataAccessException
       this.subjectTypeRepository.deleteById(id);
    }

    @Override
    public List<SubjectTypeServiceModel> findAllSubjectTypes() {
        return this.subjectTypeRepository.findAll()
                .stream()
                .map(o -> this.modelMapper.map(o, SubjectTypeServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public SubjectTypeServiceModel findSubjectTypesById(String id) {
        SubjectType subjectType = this.subjectTypeRepository.findById(id).orElse(null);
        if (subjectType == null){
            return null;
        }
        return this.modelMapper.map(subjectType, SubjectTypeServiceModel.class);
    }

    private SubjectType saveSubjectType(SubjectType subjectType) throws UniqueFieldException {
        this.checkUniqueness(subjectType);
        return this.subjectTypeRepository.save(subjectType);
    }

    private void checkUniqueness(SubjectType subjectType) throws UniqueFieldException {
        SubjectType same = this.subjectTypeRepository
                .findSubjectTypeByTitle(subjectType.getTitle()).orElse(null);
        if (same != null){
            throw new UniqueFieldException(this.getClass().getName(), "title");
        }
    }
}
