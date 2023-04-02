package com.dbouris.license.controller;

import com.dbouris.license.model.License;
import com.dbouris.license.service.LicenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "v1/organization/{organizationId}/license")
public class LicenseController {

    @Autowired
    LicenseService licenseService;

    @GetMapping(value = "/{licenseId}")
    ResponseEntity<License> getLicense(
            @PathVariable("organizationId") String organizationId,
            @PathVariable("licenseId") String licenseId) {
        License license = licenseService.getLicense(licenseId, organizationId);
        license.add(linkTo(methodOn(LicenseController.class)
                .getLicense(organizationId, license.getLicenseId()))
                .withSelfRel(),
                linkTo(methodOn(LicenseController.class)
                        .createLicense(organizationId,license,null))
                        .withRel("createLicense"),
                linkTo(methodOn(LicenseController.class)
                        .updateLicense(organizationId,license))
                        .withRel("updateLicense"),
                linkTo(methodOn(LicenseController.class)
                        .deleteLicense(organizationId, license.getLicenseId()))
                        .withRel("deleteLicense"));
        return ResponseEntity.ok(license);
    }

    @PostMapping()
    ResponseEntity<String> createLicense(@PathVariable("organizationId") String organizationId,
                                         @RequestBody @Valid License request,
                                         @RequestHeader(value = "Accept-Language", required = false)
                                         Locale locale) {
        return ResponseEntity.ok(licenseService.createLicense(request, organizationId, locale));
    }

    @PutMapping
    ResponseEntity<String> updateLicense(@PathVariable("organizationId") String organizationId,
                                         @RequestBody License request) {
        return ResponseEntity.ok(licenseService.updateLicense(request, organizationId));
    }

    @DeleteMapping(value = "/{licenseId}")
    ResponseEntity<String> deleteLicense(@PathVariable("organizationId") String organizationId,
                                         @PathVariable("licenseId") String licenseId) {
        return ResponseEntity.ok(licenseService.deleteLicense(organizationId, licenseId));
    }
}
