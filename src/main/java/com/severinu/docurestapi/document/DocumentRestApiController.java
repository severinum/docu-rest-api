package com.severinu.docurestapi.document;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/documents")
public class DocumentRestApiController {

    private Collection<Document> documents = new ArrayList<>();

    @GetMapping
    public Iterable<Document> getDocuments() {
        return this.documents;
    }

    @GetMapping("/{documentNumber}")
    public Optional<Document> getDocument(@PathVariable long documentNumber) {
        return findDocumentByNumber(documentNumber);
    }

    @GetMapping("/{documentNumber}/title")
    public Optional<String> getDocumentTitle(@PathVariable long documentNumber) {
        return findDocumentByNumber(documentNumber)
                .map(Document::getTitle);
    }

    @PostMapping
    public void addDocument(@RequestBody Document document) {
        this.documents.add(document);
    }

    @PostMapping(value = "/{documentNumber}/tags", consumes = MediaType.TEXT_PLAIN_VALUE)
    public void addTag(@PathVariable long documentNumber,
                       @RequestBody String tag) {
        findDocumentByNumber(documentNumber)
                .ifPresent( doc -> doc.getTags().add(tag));
    }


    @PutMapping("/{documentNumber}")
    public void replaceDocument(@PathVariable long documentNumber,
                                @RequestBody Document newDocument) {
        findDocumentByNumber(documentNumber)
                .ifPresent(document ->  {
                    document.setTitle(newDocument.getTitle());
                    document.setTags(newDocument.getTags());
                });
    }

    @PatchMapping("/{documentNumber}")
    public void updateDocument(@PathVariable long documentNumber,
                               @RequestBody Document newPartialDocument) {
        findDocumentByNumber(documentNumber)
                .ifPresent(document -> {
                    if(newPartialDocument.getTitle() != null)
                        document.setTitle(newPartialDocument.getTitle());
                    if(newPartialDocument.getTags() != null)
                        document.setTags(newPartialDocument.getTags());
                });
    }

    private Optional<Document> findDocumentByNumber(long documentNumber) {
        return documents.stream()
                .filter(document -> document.getNumber() == documentNumber)
                .findAny();
    }

}
