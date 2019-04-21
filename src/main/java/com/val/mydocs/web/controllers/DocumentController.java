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
            this.prepareErrorsModelAndView(model, bindingResult, modelAndView);
            return this.view(view, modelAndView);
        }
        DocumentServiceModel documentServiceModel = this.modelMapper.map(model, DocumentServiceModel.class);
        this.documentService.addDocument(documentServiceModel, username);

        return this.redirect("/home");
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
        model = getDocumentModel(id, principal);

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
            this.prepareErrorsModelAndView(model, bindingResult, modelAndView);
            return this.view(view, modelAndView);
        }

        DocumentServiceModel documentServiceModel = this.modelMapper.map(model, DocumentServiceModel.class);
        this.documentService.editDocument(documentServiceModel, userName);

        return super.redirect("/home");
    }

    @GetMapping("document/delete/{id}")
    public ModelAndView deleteDocument(@PathVariable String id,
                                      @ModelAttribute(name = "document") DocumentBindingModel document,
                                      ModelAndView modelAndView,
                                      Principal principal) throws AuthenticationException {
        document = getDocumentModel(id, principal);

        modelAndView.addObject("model", document);

        return super.view("document/delete-document", modelAndView);
    }

    @PostMapping("document/delete/{id}")
    public ModelAndView deleteDocumentConfirm(@PathVariable String id,
                                             Principal principal) throws AuthenticationException {
        String userName = this.getPrincipalName(principal);

        this.documentService.deleteDocument(id, userName);

        return super.redirect("/home");
    }

    @GetMapping("/document/renew/{id}")
    public ModelAndView renewDocuments(@PathVariable String id,
                                      @ModelAttribute(name = "model") DocumentBindingModel model,
                                      ModelAndView modelAndView,
                                      Principal principal) throws AuthenticationException {
        model = getDocumentModel(id, principal);
        modelAndView.addObject("model", model);
        this.AddDocumentTypesModel(modelAndView);

        return super.view("document/renew-document", modelAndView);
    }

    @PostMapping("/document/renew/{id}")
    public ModelAndView renewDocumentsConfirm(@PathVariable(name = "id") String id,
                                             @ModelAttribute(name = "model") DocumentBindingModel model,
                                             BindingResult bindingResult,
                                             ModelAndView modelAndView,
                                             Principal principal) throws AuthenticationException {
        String userName = getPrincipalName(principal);

        DocumentServiceModel documentServiceModel = this.modelMapper.map(model, DocumentServiceModel.class);
        DocumentServiceModel newDocument = this.documentService.renewDocument(documentServiceModel, userName);

        return super.redirect("/document/edit/" + newDocument.getId());
    }

    @GetMapping("document/fetch/{subjectId}")
    @ResponseBody
    public List<DocumentAllViewModel> fetchBySubject(@PathVariable String subjectId,
                                                     Principal principal) throws AuthenticationException {
        String username = this.getPrincipalName(principal);
        if(subjectId.equals("all")) {
            return this.documentService.findAllDocumentsOrderByExpiredDate(username)
                    .stream()
                    .map(document -> this.modelMapper.map(document, DocumentAllViewModel.class))
                    .collect(Collectors.toList());
        }

        return this.documentService.findAllBySubjectOrderByExpiredDate(subjectId, username)
                .stream()
                .map(document -> this.modelMapper.map(document, DocumentAllViewModel.class))
                .collect(Collectors.toList());
    }

    private SubjectBindingModel getSubject(@PathVariable String subjectId, String username) {
        SubjectServiceModel subjectServiceModel = this.subjectService.findSubjectsById(subjectId, username);
        return this.modelMapper.map(subjectServiceModel, SubjectBindingModel.class);
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

    private DocumentBindingModel getDocumentModel(@PathVariable String id, Principal principal) throws AuthenticationException {
        DocumentBindingModel model;
        String userName = getPrincipalName(principal);

        DocumentServiceModel documentServiceModel = this.documentService.findDocumentsById(id, userName);
        model = this.modelMapper.map(documentServiceModel, DocumentBindingModel.class);
        return model;
    }

    private void prepareErrorsModelAndView(@Valid @ModelAttribute(name = "model") DocumentBindingModel model, BindingResult bindingResult, ModelAndView modelAndView) {
        modelAndView.addObject("model", model);
        this.AddDocumentTypesModel(modelAndView);
        this.addGlobalErrorsToModelAndView("globalErrors", modelAndView, bindingResult);
    }
}
