package com.val.mydocs.web.controllers;

import com.val.mydocs.domain.models.binding.SubjectTypeBindingModel;
import com.val.mydocs.domain.models.service.SubjectTypeServiceModel;
import com.val.mydocs.domain.models.view.SubjectTypesAllViewModel;
import com.val.mydocs.domain.models.view.SubjectTypesDetailsViewModel;
import com.val.mydocs.serivce.CloudinaryService;
import com.val.mydocs.serivce.SubjectTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SubjectTypeController extends BaseController {
    private final SubjectTypeService subjectTypeService;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;
    private final MessageSource messageSource;


    @Autowired
    public SubjectTypeController(SubjectTypeService subjectTypeService, ModelMapper modelMapper, CloudinaryService cloudinaryService, MessageSource messageSource) {
        this.subjectTypeService = subjectTypeService;
        this.modelMapper = modelMapper;
        this.cloudinaryService = cloudinaryService;
        this.messageSource = messageSource;
    }

    @GetMapping("/subject-types/add")
    //@PreAuthorize("hasAuthority('MODERATOR')")
    public ModelAndView addSubjectType(@ModelAttribute(name = "model") SubjectTypeBindingModel model,
                                       ModelAndView modelAndView) {
        modelAndView.addObject("model", model);
        return this.view("subjecttype/add-subject-type");
    }

    @PostMapping("/subject-types/add")
    //@PreAuthorize("hasAuthority('MODERATOR')")
    public ModelAndView addSubjectTypeConfirm(@Valid @ModelAttribute(name = "model") SubjectTypeBindingModel model,
                                              BindingResult bindingResult,
                                              ModelAndView modelAndView,
                                              HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return this.view("subjecttype/add-subject-type", modelAndView);
        }

        SubjectTypeServiceModel subjectTypeServiceModel = this.modelMapper.map(model, SubjectTypeServiceModel.class);
        String imageUrl = null;
        try {
            imageUrl = this.cloudinaryService.uploadImage(model.getImage());
        } catch (IOException e) {
            bindingResult.addError(new FieldError("model", "image",
                    this.messageSource.getMessage("error.image.upload", null, RequestContextUtils.getLocale(request))));
        }
        subjectTypeServiceModel.setImageUrl(imageUrl);

        this.subjectTypeService.saveSubjectType(subjectTypeServiceModel);

        return this.redirect("/subject-types/all");
    }

    @GetMapping("/subject-types/all")
    public ModelAndView allSubjectTypes(ModelAndView modelAndView) {
        List<SubjectTypeServiceModel> serviceModels = this.subjectTypeService.findAllSubjectTypes();
        List<SubjectTypesAllViewModel> serviceViewModels = serviceModels
                .stream()
                .map(o -> this.modelMapper.map(o, SubjectTypesAllViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("subjectTypes", serviceViewModels);

        return super.view("subjecttype/all-subject-types", modelAndView);
    }

    @GetMapping("/subject-types/details/{id}")
    public ModelAndView detailsSubjectTypes(@PathVariable String id, ModelAndView modelAndView) {
        SubjectTypeServiceModel subjectTypeServiceModel = this.subjectTypeService.findSubjectTypesById(id);
        modelAndView.addObject("subjectType",
                this.modelMapper.map(subjectTypeServiceModel, SubjectTypesDetailsViewModel.class));

        return super.view("subjecttype/details", modelAndView);
    }
}
