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
        return documents.stream()
                .filter(doc -> doc.getNumber() == documentNumber)
                .findAny();
    }

    @GetMapping("/{documentNumber}/title")
    public Optional<String> getDocumentTitle(@PathVariable long documentNumber) {
        return documents.stream()
                .filter( document -> document.getNumber() == documentNumber)
                .findAny()
                .map(Document::getTitle);
    }

    @PostMapping
    public void addDocument(@RequestBody Document document) {
        this.documents.add(document);
    }

    @PostMapping(value = "/{documentNumber}/tags", consumes = MediaType.TEXT_PLAIN_VALUE)
    public void addTag(@PathVariable long documentNumber, @RequestBody String tag) {
        documents.stream().filter( document -> document.getNumber() == documentNumber)
                .findAny().ifPresent( doc -> doc.getTags().add(tag));
    }

}
