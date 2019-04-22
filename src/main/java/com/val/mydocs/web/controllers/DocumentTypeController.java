package com.val.mydocs.web.controllers;

import com.val.mydocs.domain.models.binding.DocumentTypeBindingModel;
import com.val.mydocs.domain.models.service.DocumentTypeServiceModel;
import com.val.mydocs.domain.models.view.DocumentTypeAllViewModel;
import com.val.mydocs.domain.models.view.DocumentTypeDetailsViewModel;
import com.val.mydocs.exceptions.ModelValidationException;
import com.val.mydocs.exceptions.UniqueFieldException;
import com.val.mydocs.serivce.DocumentTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DocumentTypeController extends BaseController {
    private final DocumentTypeService documentTypeService;
    private final ModelMapper modelMapper;

    @Autowired
    public DocumentTypeController(DocumentTypeService documentTypeService, ModelMapper modelMapper) {
        this.documentTypeService = documentTypeService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/document-types/add")
    public ModelAndView addDocumentType(@ModelAttribute(name = "model") DocumentTypeBindingModel model,
                                        ModelAndView modelAndView) {
        modelAndView.addObject("model", model);
        return this.view("documenttype/add-document-type");
    }

    @PostMapping("/document-types/add")
    public ModelAndView addDocumentTypeConfirm(@Valid @ModelAttribute(name = "model") DocumentTypeBindingModel model,
                                               BindingResult bindingResult,
                                               ModelAndView modelAndView,
                                               HttpServletRequest request) throws ModelValidationException {
        String view = "documenttype/add-document-type";
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("model", model);
            return this.view(view, modelAndView);
        }

        DocumentTypeServiceModel documentTypeServiceModel = this.modelMapper.map(model, DocumentTypeServiceModel.class);
        try {
            this.documentTypeService.addDocumentType(documentTypeServiceModel);
        } catch (UniqueFieldException e) {
            e.printStackTrace();
            this.addErrorUniqueErrorToBindingResult(bindingResult, request, e);
            return this.view(view, modelAndView);
        }

        return this.redirect("/document-types/all");
    }

    @GetMapping("/document-types/all")
    public ModelAndView allDocumentTypes(ModelAndView modelAndView) {
        List<DocumentTypeServiceModel> serviceModels = this.documentTypeService.findAllDocumentTypes();
        List<DocumentTypeAllViewModel> serviceViewModels = serviceModels
                .stream()
                .map(o -> this.modelMapper.map(o, DocumentTypeAllViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("documentTypes", serviceViewModels);

        return super.view("documenttype/all-document-types", modelAndView);
    }

    @GetMapping("/document-types/details/{id}")
    public ModelAndView detailsDocumentTypes(@PathVariable String id, ModelAndView modelAndView) {
        DocumentTypeServiceModel documentTypeServiceModel = this.documentTypeService.findDocumentTypesById(id);
        modelAndView.addObject("model",
                this.modelMapper.map(documentTypeServiceModel, DocumentTypeDetailsViewModel.class));

        return super.view("documenttype/document-type-details", modelAndView);
    }

    @GetMapping("/document-types/edit/{id}")
    public ModelAndView editDocumentTypes(@PathVariable String id,
                                          @ModelAttribute(name = "model") DocumentTypeBindingModel model,
                                          ModelAndView modelAndView) {
        DocumentTypeServiceModel documentTypeServiceModel = this.documentTypeService.findDocumentTypesById(id);
        model = this.modelMapper.map(documentTypeServiceModel, DocumentTypeBindingModel.class);

        modelAndView.addObject("model", model);

        return super.view("documenttype/edit-document-type", modelAndView);
    }

    @PostMapping("/document-types/edit/{id}")
    public ModelAndView editDocumentTypesConfirm(@PathVariable(name = "id") String id,
                                                 @Valid @ModelAttribute(name = "model") DocumentTypeBindingModel model,
                                                 BindingResult bindingResult,
                                                 ModelAndView modelAndView,
                                                 HttpServletRequest request) throws ModelValidationException {
        String view = "documenttype/edit-document-type";
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("model", model);
            return this.view(view, modelAndView);
        }

        DocumentTypeServiceModel documentTypeServiceModel = this.modelMapper.map(model, DocumentTypeServiceModel.class);
        try {
            this.documentTypeService.editDocumentType(documentTypeServiceModel);
        } catch (UniqueFieldException e) {
            e.printStackTrace();
            this.addErrorUniqueErrorToBindingResult(bindingResult, request, e);
            return this.view(view, modelAndView);
        }

        return super.redirect("/document-types/details/" + id);
    }

    @GetMapping("document-types/delete/{id}")
    public ModelAndView deleteDocumentType(@PathVariable String id,
                                           @ModelAttribute(name = "documentType") DocumentTypeBindingModel documentType,
                                           ModelAndView modelAndView) {
        DocumentTypeServiceModel documentTypeServiceModel = this.documentTypeService.findDocumentTypesById(id);
        documentType = this.modelMapper.map(documentTypeServiceModel, DocumentTypeBindingModel.class);

        modelAndView.addObject("model", documentType);

        return super.view("documenttype/delete-document-type", modelAndView);
    }

    @PostMapping("document-types/delete/{id}")
    public ModelAndView deleteDocumentTypeConfirm(@PathVariable String id) {
        this.documentTypeService.deleteDocumentType(id);

        return super.redirect("/document-types/all");
    }
}
