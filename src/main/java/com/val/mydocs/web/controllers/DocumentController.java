package com.val.mydocs.web.controllers;

import com.val.mydocs.domain.models.binding.DocumentBindingModel;
import com.val.mydocs.domain.models.binding.SubjectBindingModel;
import com.val.mydocs.domain.models.service.DocumentServiceModel;
import com.val.mydocs.domain.models.service.DocumentTypeServiceModel;
import com.val.mydocs.domain.models.service.SubjectServiceModel;
import com.val.mydocs.domain.models.view.DocumentAllViewModel;
import com.val.mydocs.domain.models.view.DocumentDetailsViewModel;
import com.val.mydocs.serivce.DocumentService;
import com.val.mydocs.serivce.DocumentTypeService;
import com.val.mydocs.serivce.SubjectService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.naming.AuthenticationException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DocumentController extends BaseController {
    private final DocumentService documentService;
    private final DocumentTypeService documentTypeService;
    private final SubjectService subjectService;
    private final ModelMapper modelMapper;

    @Autowired
    public DocumentController(DocumentService documentService, DocumentTypeService documentTypeService, SubjectService subjectService, ModelMapper modelMapper) {
        this.documentService = documentService;
        this.documentTypeService = documentTypeService;
        this.subjectService = subjectService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/document/add/{subjectId}")
    public ModelAndView addDocument(@PathVariable String subjectId, @ModelAttribute(name = "model") DocumentBindingModel model,
                                   ModelAndView modelAndView,
                                   Principal principal) throws AuthenticationException {

        String username = this.getPrincipalName(principal);
        SubjectBindingModel subjectBindingModel = getSubject(subjectId, username);
        model.setSubject(subjectBindingModel);
        modelAndView.addObject("model", model);
        this.AddDocumentTypesModel(modelAndView);
        return this.view("document/add-document", modelAndView);
    }

    private SubjectBindingModel getSubject(@PathVariable String subjectId, String username) {
        SubjectServiceModel subjectServiceModel = this.subjectService.findSubjectsById(subjectId, username);
        return this.modelMapper.map(subjectServiceModel, SubjectBindingModel.class);
    }

    @PostMapping("/document/add/{subjectId}")
    public ModelAndView addDocumentConfirm(@PathVariable String subjectId, @Valid @ModelAttribute(name = "model") DocumentBindingModel model,
                                          BindingResult bindingResult,
                                          ModelAndView modelAndView,
                                          Principal principal) throws AuthenticationException {

        String username = getPrincipalName(principal);
        SubjectBindingModel subjectBindingModel = getSubject(subjectId, username);
        model.setSubject(subjectBindingModel);
        String view = "document/add-document";
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("model", model);
            this.AddDocumentTypesModel(modelAndView);
            return this.view(view, modelAndView);
        }
        DocumentServiceModel documentServiceModel = this.modelMapper.map(model, DocumentServiceModel.class);
        this.documentService.addDocument(documentServiceModel, username);

        return this.redirect("/home");
    }

    @GetMapping("/document/all")
    public ModelAndView allDocuments(ModelAndView modelAndView,
                                    Principal principal) throws AuthenticationException {
        String username = getPrincipalName(principal);
        List<DocumentServiceModel> serviceModels = this.documentService.findAllDocuments(username);
        List<DocumentAllViewModel> serviceViewModels = serviceModels
                .stream()
                .map(o -> this.modelMapper.map(o, DocumentAllViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("documents", serviceViewModels);

        return super.view("document/all-document", modelAndView);
    }

    @GetMapping("/document/details/{id}")
    public ModelAndView detailsDocuments(@PathVariable String id,
                                        ModelAndView modelAndView,
                                        Principal principal) throws AuthenticationException {
        String username = getPrincipalName(principal);

        DocumentServiceModel documentServiceModel = this.documentService.findDocumentsById(id, username);
        modelAndView.addObject("model",
                this.modelMapper.map(documentServiceModel, DocumentDetailsViewModel.class));

        return super.view("document/document-details", modelAndView);
    }

    @GetMapping("/document/edit/{id}")
    public ModelAndView editDocuments(@PathVariable String id,
                                     @ModelAttribute(name = "model") DocumentBindingModel model,
                                     ModelAndView modelAndView,
                                     Principal principal) throws AuthenticationException {
        String userName = getPrincipalName(principal);

        DocumentServiceModel documentServiceModel = this.documentService.findDocumentsById(id, userName);
        model = this.modelMapper.map(documentServiceModel, DocumentBindingModel.class);

        modelAndView.addObject("model", model);
        this.AddDocumentTypesModel(modelAndView);

        return super.view("document/edit-document", modelAndView);
    }

    @PostMapping("/document/edit/{id}")
    public ModelAndView editDocumentsConfirm(@PathVariable(name = "id") String id,
                                            @Valid @ModelAttribute(name = "model") DocumentBindingModel model,
                                            BindingResult bindingResult,
                                            ModelAndView modelAndView,
                                            Principal principal) throws AuthenticationException {
        String userName = getPrincipalName(principal);

        String view = "document/edit-document";
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("model", model);
            this.AddDocumentTypesModel(modelAndView);
            return this.view(view, modelAndView);
        }

        DocumentServiceModel documentServiceModel = this.modelMapper.map(model, DocumentServiceModel.class);
        this.documentService.editDocument(documentServiceModel, userName);

        return super.redirect("/document/details/" + id);
    }

    @GetMapping("document/delete/{id}")
    public ModelAndView deleteDocument(@PathVariable String id,
                                      @ModelAttribute(name = "document") DocumentBindingModel document,
                                      ModelAndView modelAndView,
                                      Principal principal) throws AuthenticationException {
        String userName = getPrincipalName(principal);

        DocumentServiceModel documentServiceModel = this.documentService.findDocumentsById(id, userName);
        document = this.modelMapper.map(documentServiceModel, DocumentBindingModel.class);

        modelAndView.addObject("model", document);

        return super.view("document/delete-document", modelAndView);
    }

    @PostMapping("document/delete/{id}")
    public ModelAndView deleteDocumentConfirm(@PathVariable String id,
                                             Principal principal) throws AuthenticationException {
        String userName = this.getPrincipalName(principal);

        this.documentService.deleteDocument(id, userName);

        return super.redirect("/document/all");
    }

    @GetMapping("document/fetch/{subjectId}")
    @ResponseBody
    public List<DocumentAllViewModel> fetchBySubject(@PathVariable String subjectId,
                                                     Principal principal) throws AuthenticationException {
        String username = this.getPrincipalName(principal);
        if(subjectId.equals("all")) {
            return this.documentService.findAllDocuments(username)
                    .stream()
                    .map(document -> this.modelMapper.map(document, DocumentAllViewModel.class))
                    .collect(Collectors.toList());
        }

        return this.documentService.findAllBySubject(subjectId, username)
                .stream()
                .map(document -> this.modelMapper.map(document, DocumentAllViewModel.class))
                .collect(Collectors.toList());
    }

    private String getPrincipalName(Principal principal) throws AuthenticationException {
        if (principal == null) {
            throw new AuthenticationException();
            // to do: error
        }
        return principal.getName();
    }

    private List<DocumentTypeServiceModel> getAllDocumentTypes(){
        List<DocumentTypeServiceModel> documentTypeServiceModels = this.documentTypeService.findAllDocumentTypes();
        return documentTypeServiceModels;
    }

    private void AddDocumentTypesModel(ModelAndView modelAndView) {
        List<DocumentTypeServiceModel> documentTypes = this.getAllDocumentTypes();
        modelAndView.addObject("documentTypes", documentTypes);
    }
}
