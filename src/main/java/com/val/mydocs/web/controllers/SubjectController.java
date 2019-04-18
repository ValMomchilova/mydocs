package com.val.mydocs.web.controllers;

import com.val.mydocs.domain.models.binding.SubjectBindingModel;
import com.val.mydocs.domain.models.service.SubjectServiceModel;
import com.val.mydocs.domain.models.service.SubjectTypeServiceModel;
import com.val.mydocs.domain.models.view.SubjectAllViewModel;
import com.val.mydocs.domain.models.view.SubjectDetailsViewModel;
import com.val.mydocs.serivce.SubjectService;
import com.val.mydocs.serivce.SubjectTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.naming.AuthenticationException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

@Controller
public class SubjectController extends BaseController {
    private final SubjectService subjectService;
    private final SubjectTypeService subjectTypeService;
    private final ModelMapper modelMapper;

    @Autowired
    public SubjectController(SubjectService subjectService, SubjectTypeService subjectTypeService, ModelMapper modelMapper) {
        this.subjectService = subjectService;
        this.subjectTypeService = subjectTypeService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/subject/add")
    public ModelAndView addSubject(@ModelAttribute(name = "model") SubjectBindingModel model,
                                   ModelAndView modelAndView,
                                   Principal principal) throws AuthenticationException {
        if (principal == null) {
            throw new AuthenticationException();
            // to do: error
        }
        modelAndView.addObject("model", model);
        this.AddSubjectTypesModel(modelAndView);
        return this.view("subject/add-subject", modelAndView);
    }

    @PostMapping("/subject/add")
    public ModelAndView addSubjectConfirm(@Valid @ModelAttribute(name = "model") SubjectBindingModel model,
                                          BindingResult bindingResult,
                                          ModelAndView modelAndView,
                                          Principal principal) throws AuthenticationException {

        String userName = getPrincipalName(principal);
        String view = "subject/add-subject";
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("model", model);
            this.AddSubjectTypesModel(modelAndView);
            return this.view(view, modelAndView);
        }
        SubjectServiceModel subjectServiceModel = this.modelMapper.map(model, SubjectServiceModel.class);
        this.subjectService.addSubject(subjectServiceModel, userName);

        return this.redirect("/subject/all");
    }

    @GetMapping("/subject/all")
    public ModelAndView allSubjects(ModelAndView modelAndView,
                                    Principal principal) throws AuthenticationException {
        String username = getPrincipalName(principal);
        List<SubjectServiceModel> serviceModels = this.subjectService.findAllSubjects(username);
        List<SubjectAllViewModel> serviceViewModels = serviceModels
                .stream()
                .map(o -> this.modelMapper.map(o, SubjectAllViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("subjects", serviceViewModels);

        return super.view("subject/all-subject", modelAndView);
    }

    @GetMapping("/subject/details/{id}")
    public ModelAndView detailsSubjects(@PathVariable String id,
                                        ModelAndView modelAndView,
                                        Principal principal) throws AuthenticationException {
        String username = getPrincipalName(principal);

        SubjectServiceModel subjectServiceModel = this.subjectService.findSubjectsById(id, username);
        //subjectServiceModel.setDescription(escapeHtml(subjectServiceModel.getDescription()));
        //subjectServiceModel.setDescription(subjectServiceModel.getDescription().replaceAll("\n", "</br>"));
        modelAndView.addObject("model",
                this.modelMapper.map(subjectServiceModel, SubjectDetailsViewModel.class));

        return super.view("subject/subject-details", modelAndView);
    }

    @GetMapping("/subject/edit/{id}")
    public ModelAndView editSubjects(@PathVariable String id,
                                     @ModelAttribute(name = "model") SubjectBindingModel model,
                                     ModelAndView modelAndView,
                                     Principal principal) throws AuthenticationException {
        String userName = getPrincipalName(principal);

        SubjectServiceModel subjectServiceModel = this.subjectService.findSubjectsById(id, userName);
        model = this.modelMapper.map(subjectServiceModel, SubjectBindingModel.class);

        modelAndView.addObject("model", model);
        this.AddSubjectTypesModel(modelAndView);

        return super.view("subject/edit-subject", modelAndView);
    }

    @PostMapping("/subject/edit/{id}")
    public ModelAndView editSubjectsConfirm(@PathVariable(name = "id") String id,
                                            @Valid @ModelAttribute(name = "model") SubjectBindingModel model,
                                            BindingResult bindingResult,
                                            ModelAndView modelAndView,
                                            Principal principal) throws AuthenticationException {
        String userName = getPrincipalName(principal);

        String view = "subject/edit-subject";
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("model", model);
            this.AddSubjectTypesModel(modelAndView);
            return this.view(view, modelAndView);
        }

        SubjectServiceModel subjectServiceModel = this.modelMapper.map(model, SubjectServiceModel.class);
        this.subjectService.editSubject(subjectServiceModel, userName);

        return super.redirect("/subject/details/" + id);
    }

    @GetMapping("subject/delete/{id}")
    public ModelAndView deleteSubject(@PathVariable String id,
                                      @ModelAttribute(name = "subject") SubjectBindingModel subject,
                                      ModelAndView modelAndView,
                                      Principal principal) throws AuthenticationException {
        String userName = getPrincipalName(principal);

        SubjectServiceModel subjectServiceModel = this.subjectService.findSubjectsById(id, userName);
        subject = this.modelMapper.map(subjectServiceModel, SubjectBindingModel.class);

        modelAndView.addObject("model", subject);

        return super.view("subject/delete-subject", modelAndView);
    }

    @PostMapping("subject/delete/{id}")
    public ModelAndView deleteSubjectConfirm(@PathVariable String id,
                                             Principal principal) throws AuthenticationException {
        String userName = getPrincipalName(principal);

        this.subjectService.deleteSubject(id, userName);

        return super.redirect("/subject/all");
    }

    private String getPrincipalName(Principal principal) throws AuthenticationException {
        if (principal == null) {
            throw new AuthenticationException();
            // to do: error
        }
        return principal.getName();
    }

    private List<SubjectTypeServiceModel> getAllSubjectTypes(){
        List<SubjectTypeServiceModel> subjectTypeServiceModels = this.subjectTypeService.findAllSubjectTypes();
        return subjectTypeServiceModels;
    }

    private void AddSubjectTypesModel(ModelAndView modelAndView) {
        List<SubjectTypeServiceModel> subjectTypes = this.getAllSubjectTypes();
        modelAndView.addObject("subjectTypes", subjectTypes);
    }
}
