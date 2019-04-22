package com.val.mydocs.web.controllers;

import com.val.mydocs.domain.models.binding.SubjectTypeBindingModel;
import com.val.mydocs.domain.models.service.SubjectTypeServiceModel;
import com.val.mydocs.domain.models.view.SubjectTypeAllViewModel;
import com.val.mydocs.domain.models.view.SubjectTypeDetailsViewModel;
import com.val.mydocs.exceptions.ModelValidationException;
import com.val.mydocs.exceptions.UniqueFieldException;
import com.val.mydocs.serivce.CloudinaryService;
import com.val.mydocs.serivce.SubjectTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
    public ModelAndView addSubjectType(@ModelAttribute(name = "model") SubjectTypeBindingModel model,
                                       ModelAndView modelAndView) {
        modelAndView.addObject("model", model);
        return this.view("subjecttype/add-subject-type");
    }

    @PostMapping("/subject-types/add")
    public ModelAndView addSubjectTypeConfirm(@Valid @ModelAttribute(name = "model") SubjectTypeBindingModel model,
                                              BindingResult bindingResult,
                                              ModelAndView modelAndView,
                                              HttpServletRequest request) throws ModelValidationException {
        String view = "subjecttype/add-subject-type";
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("model", model);
            return this.view(view, modelAndView);
        }

        String imageUrl = uploadImage(model, bindingResult, request);
        if (bindingResult.hasErrors()) {
            return this.view(view, modelAndView);
        }

        SubjectTypeServiceModel subjectTypeServiceModel = this.modelMapper.map(model, SubjectTypeServiceModel.class);
        subjectTypeServiceModel.setImageUrl(imageUrl);

        try {
            this.subjectTypeService.addSubjectType(subjectTypeServiceModel);
        } catch (UniqueFieldException e) {
            e.printStackTrace();
            this.addErrorUniqueErrorToBindingResult(bindingResult, request, e);
            return this.view(view, modelAndView);
        }

        return this.redirect("/subject-types/all");
    }

    @GetMapping("/subject-types/all")
    public ModelAndView allSubjectTypes(ModelAndView modelAndView) {
        List<SubjectTypeServiceModel> serviceModels = this.subjectTypeService.findAllSubjectTypes();
        List<SubjectTypeAllViewModel> serviceViewModels = serviceModels
                .stream()
                .map(o -> this.modelMapper.map(o, SubjectTypeAllViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("subjectTypes", serviceViewModels);

        return super.view("subjecttype/all-subject-types", modelAndView);
    }

    @GetMapping("/subject-types/details/{id}")
    public ModelAndView detailsSubjectTypes(@PathVariable String id, ModelAndView modelAndView) {
        SubjectTypeServiceModel subjectTypeServiceModel = this.subjectTypeService.findSubjectTypesById(id);
        modelAndView.addObject("model",
                this.modelMapper.map(subjectTypeServiceModel, SubjectTypeDetailsViewModel.class));

        return super.view("subjecttype/subject-type-details", modelAndView);
    }

    @GetMapping("/subject-types/edit/{id}")
    public ModelAndView editSubjectTypes(@PathVariable String id,
                                         @ModelAttribute(name = "model") SubjectTypeBindingModel model,
                                         ModelAndView modelAndView) {
        SubjectTypeServiceModel subjectTypeServiceModel = this.subjectTypeService.findSubjectTypesById(id);
        model = this.modelMapper.map(subjectTypeServiceModel, SubjectTypeBindingModel.class);

        modelAndView.addObject("model", model);

        return super.view("subjecttype/edit-subject-type", modelAndView);
    }

    @PostMapping("/subject-types/edit/{id}")
    public ModelAndView editSubjectTypesConfirm(@PathVariable(name = "id") String id,
                                                @Valid @ModelAttribute(name = "model") SubjectTypeBindingModel model,
                                                BindingResult bindingResult,
                                                ModelAndView modelAndView,
                                                HttpServletRequest request) throws ModelValidationException {
        String view = "subjecttype/edit-subject-type";
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("model", model);
            return this.view(view, modelAndView);
        }

        SubjectTypeServiceModel subjectTypeServiceModel = this.modelMapper.map(model, SubjectTypeServiceModel.class);

        if (model.getImage() != null && !model.getImage().getOriginalFilename().isEmpty()) {
            String imageUrl = this.uploadImage(model, bindingResult, request);
            if (bindingResult.hasErrors()) {
                modelAndView.addObject("model", model);
                return this.view(view, modelAndView);
            }
            subjectTypeServiceModel.setImageUrl(imageUrl);
        }

        try {
            this.subjectTypeService.editSubjectType(subjectTypeServiceModel);
        } catch (UniqueFieldException e) {
            e.printStackTrace();
            this.addErrorUniqueErrorToBindingResult(bindingResult, request, e);
            return this.view(view, modelAndView);
        }

        return super.redirect("/subject-types/details/" + id);
    }

    @GetMapping("subject-types/delete/{id}")
    public ModelAndView deleteSubjectType(@PathVariable String id,
                                          @ModelAttribute(name = "subjectType") SubjectTypeBindingModel subjectType,
                                          ModelAndView modelAndView) {
        SubjectTypeServiceModel subjectTypeServiceModel = this.subjectTypeService.findSubjectTypesById(id);
        subjectType = this.modelMapper.map(subjectTypeServiceModel, SubjectTypeBindingModel.class);

        modelAndView.addObject("model", subjectType);

        return super.view("subjecttype/delete-subject-type", modelAndView);
    }

    @PostMapping("subject-types/delete/{id}")
    public ModelAndView deleteSubjectTypeConfirm(@PathVariable String id) {
        this.subjectTypeService.deleteSubjectType(id);

        return super.redirect("/subject-types/all");
    }

    private String uploadImage(SubjectTypeBindingModel model, BindingResult bindingResult, HttpServletRequest request) {
        String imageUrl = null;
        try {
            imageUrl = this.cloudinaryService.uploadImage(model.getImage());
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.addError(new FieldError("model", "image",
                    this.messageSource.getMessage("error.image.upload", null, RequestContextUtils.getLocale(request))));
        }
        return imageUrl;
    }

}
