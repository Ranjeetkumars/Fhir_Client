package com.pro.fhir.resources;




import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.JsonParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.ClinicalImpression;
import org.hl7.fhir.r4.model.ClinicalImpression.ClinicalImpressionStatus;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.CoverageEligibilityRequest;
import org.hl7.fhir.r4.model.CoverageEligibilityRequest.EligibilityRequestStatus;
import org.hl7.fhir.r4.model.CoverageEligibilityResponse;
import org.hl7.fhir.r4.model.CoverageEligibilityResponse.EligibilityResponseStatus;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.FamilyMemberHistory;
import org.hl7.fhir.r4.model.Goal;
import org.hl7.fhir.r4.model.HealthcareService;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Immunization;
import org.hl7.fhir.r4.model.Immunization.ImmunizationStatus;
import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Narrative;
import org.hl7.fhir.r4.model.Narrative.NarrativeStatus;
import org.hl7.fhir.r4.model.NutritionOrder;
import org.hl7.fhir.r4.model.NutritionOrder.NutritionOrderStatus;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Procedure;
import org.hl7.fhir.r4.model.Procedure.ProcedureStatus;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Type;
import org.hl7.fhir.r4.model.codesystems.MedicationrequestStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

public class VinodResource {

    /*
     * This class contains hints for the tasks outlined in TestApplication
     */

    public static void main(String[] args) {

        System.out.println("Cre");
//        step3_create_patient();
//        step2_search_for_patients_named_test();
        //step1_read_a_resource();


        familyMemberhistory();
        condition();
        clinicleImpression();
        medicationRequest();
        immunization();
        procedure();
        coverageEligibilityRequest();
        coverageEligibilityResponse();
        goal();
        nutritionOrder();

    }

    private static void nutritionOrder() {
        NutritionOrder no=new NutritionOrder();
        no.setId("infantenteral");
        List<Identifier> theIdentifier=new ArrayList<Identifier>();
        Identifier id=new Identifier();
        id.setId("123");
        theIdentifier.add(id);
        no.setIdentifier(theIdentifier);
        no.setStatus(NutritionOrderStatus.valueOf("active"));
        no.setPatient(new Reference().setReference("Patient/example"));

        createResource(no);
    }

    private static void goal() {
        Goal goal=new Goal();
        goal.setId("");
        List<Identifier> theIdentifier=new ArrayList<Identifier>();
        Identifier id=new Identifier();
        id.setId("123");
        theIdentifier.add(id);
        goal.setIdentifier(theIdentifier);
        CodeableConcept value=new CodeableConcept();
        value.addCoding().setCode("dietary");
        value.addCoding().setSystem("http://terminology.hl7.org/CodeSystem/goal-category");
        value.addCoding().setDisplay("High Priority");
        goal.addCategory(value);
        Narrative nar=new Narrative();
        nar.setStatus(NarrativeStatus.valueOf("additional"));
        goal.setText(nar);
        goal.setSubject(new Reference().setReference("Patient/example"));
        createResource(goal);
    }

    private static void coverageEligibilityResponse() {
        CoverageEligibilityResponse res=new CoverageEligibilityResponse();
        res.setId("CoverageEligibilityResponse-1");
        res.setStatus(EligibilityResponseStatus.valueOf("active"));
        res.setStatus(EligibilityResponseStatus.valueOf("generated"));
        res.setPatient(new Reference().setReference("Patient/example"));

        res.addInsurance().setCoverage(new Reference().setReference("Coverage/9876B1"));

        res.setInsurer(new Reference().setReference("Organization/2"));
        createResource(res);

    }

    private static void coverageEligibilityRequest() {
        CoverageEligibilityRequest res=new CoverageEligibilityRequest();
        res.setId("CoverageEligibilityRequest-1");
        res.setStatus(EligibilityRequestStatus.valueOf("active"));
        res.setStatus(EligibilityRequestStatus.valueOf("generated"));
        res.setPatient(new Reference().setReference("Patient/example"));
        res.addInsurance().setFocal(true);
        res.addInsurance().setCoverage(new Reference().setReference("Coverage/9876B1"));
        res.addInsurance().setFocal(true);
        res.setInsurer(new Reference().setReference("Organization/2"));
        createResource(res);
    }

    private static void procedure() {
        Procedure pro=new Procedure();
        pro.setId("PROCEDURE-1");
        pro.setStatus(ProcedureStatus.valueOf("completed"));
        CodeableConcept value=new CodeableConcept();
        value.addCoding().setCode("80146002");
        value.addCoding().setSystem("http://snomed.info/sct");
        value.addCoding().setDisplay("Appendectomy (Procedure)");
        pro.setCode(value);
        pro.setSubject(new Reference().setReference("Patient/example"));
        pro.addNote().setText("Routine Appendectomy. Appendix was inflamed and in retro-caecal position");
        pro.addReasonCode().setText("Generalized abdominal pain 24 hours. Localized in RIF with rebound and guarding");

    createResource(pro);
    }


    private static void immunization() {
        Immunization imm=new Immunization();
        imm.setPatient(new Reference().setReference("Patient/example"));
        imm.setText((Narrative) new Narrative().setId("Fluvax (Influenza)"));
        imm.setStatus(ImmunizationStatus.valueOf("Completed"));
        CodeableConcept value=new CodeableConcept();
        value.addCoding().setCode("24484000");
        value.addCoding().setSystem("http://snomed.info/sct");
        value.addCoding().setDisplay("Severe");
        imm.setVaccineCode(value);
        imm.setSite(value);

        createResource(imm);
    }

    private static void medicationRequest() {
        MedicationRequest mr=new MedicationRequest();
mr.setId("MR-1");
mr.setLanguage("English");
mr.addBasedOn(new Reference().setType("Patient/example"));
mr.addDetectedIssue().setType("Some Issue");
mr.addDosageInstruction().setText("3 times a day afeter eating");

    }

    private static void clinicleImpression() {
        ClinicalImpression ci= new ClinicalImpression();
        ci.setId("");
        ci.setDate(new Date());
        ci.addProblem().setDisplay("Some Problem");
        ci.setSummary("provisional diagnoses of laceration of head and traumatic brain injury (TBI)");
        ci.setSubject(new Reference().setReference("Patient/example"));
        ci.setStatus(ClinicalImpressionStatus.valueOf("completed"));
        createResource(ci);
    }

    private static void condition() {
        Condition condition= new Condition();
        condition.setId("");
        condition.setSubject(new Reference().setReference("Patient/example"));
        condition.setText((Narrative) new Narrative().setId("Burnt Ear"));
        CodeableConcept value=new CodeableConcept();
        value.addCoding().setCode("24484000");
        value.addCoding().setSystem("http://snomed.info/sct");
        value.addCoding().setDisplay("Severe");

        condition.setSeverity(value);
        List<CodeableConcept> list=new ArrayList<CodeableConcept>();
        list.add(value);
        condition.setCategory(list);
        createResource(condition);
    }

    private static void createResource(IBaseResource theResource) {
        // Create a client
                FhirContext ctx = FhirContext.forR4();
                IGenericClient client = ctx.newRestfulGenericClient("http://localhost:8080/fhir");

        // Create the resource on the server
                MethodOutcome outcome = client
                    .create()
                    .resource(theResource)
                    .execute();

                // Log the ID that the server assigned
                IIdType id = outcome.getId();
        System.out.println("Created resource-->"+id);
    }

    private static void familyMemberhistory() {
        FamilyMemberHistory fmh=new FamilyMemberHistory();
        fmh.setId("");
        fmh.setName("");
        CodeableConcept relation=new CodeableConcept();
        relation.setText("Father");
        fmh.setRelationship(relation);
        fmh.setLanguage("");
        Reference ref= new Reference();
        ref.setReference("Patient/example");
        fmh.setPatient(ref);

         createResource(fmh);
    }

    public static void step1_read_a_resource() {

        FhirContext ctx = FhirContext.forR4();
        IGenericClient client = ctx.newRestfulGenericClient("http://localhost:8080/fhir");

        Patient patient;
        try {
            // Try changing the ID from 952975 to 999999999999
            patient = client.read().resource(Patient.class).withId("952975").execute();
        } catch (ResourceNotFoundException e) {
            System.out.println("Resource not found!");
            return;
        }

        String string = ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
        System.out.println(string);

    }

    public static void step2_search_for_patients_named_test() {
        FhirContext ctx = FhirContext.forR4();
        IGenericClient client = ctx.newRestfulGenericClient("http://localhost:8080/fhir");

        org.hl7.fhir.r4.model.Bundle results = client
            .search()
            .forResource(Patient.class)
            .where(Patient.NAME.matches().value("John"))

            .returnBundle(org.hl7.fhir.r4.model.Bundle.class)
            .execute();

        System.out.println("First page: ");
        //System.out.println(ctx.newXmlParser().encodeResourceToString(results));
        System.out.println(ctx.newJsonParser().encodeResourceToString(results));

        // Load the next page
//        org.hl7.fhir.r4.model.Bundle nextPage = client
//            .loadPage()
//            .next(results)
//            .execute();

        System.out.println("Next page: ");
    //    System.out.println(ctx.newXmlParser().encodeResourceToString(nextPage));

    }

    public static void step3_create_patient() {

        Organization org = new Organization();
        org.setId("Hospital-1");
        org.setName("MediCareHospital");
        org.addAlias("Medicare");
        org.addAddress().setCity("Kurnool");
        org.addAddress().setCountry("India");
        org.addAddress().setDistrict("Kurnool");
        org.addAddress().setPostalCode("518899");
        org.addContact().addTelecom().setValue("9876543210");
        org.setActive(true);

//        myOrganizationDao.update(org, mySrd);
//
//        myDaoConfig.setAllowExternalReferences(true);

        Location loc = new Location();
        loc.setId("1");
        loc.setName("Location1");
        loc.addAlias("");
        Address address=new Address();
        address.setCountry("Kurnool");
        address.setDistrict("Kurnool");
        address.setCity("Kurnool");
        address.setPostalCode("Kurnool");
        address.setId("address1");

        loc.setAddress(address);
        loc.addTelecom().setValue("9876543210");



        HealthcareService hcs = new HealthcareService();
        hcs.addContained(loc);
        hcs.setId("HealthcareService");
        hcs.addAvailableTime().setAllDay(true);
        //hcs.
        //hcs.setLocation(List<Reference> loc);
        // Create a patient
        Patient newPatient = new Patient();

        // Populate the patient with fake information
        newPatient
            .addName()
                .setFamily("DevDays2015")
                .addGiven("John")
                .addGiven("Q");
        newPatient
            .addIdentifier()
                .setSystem("http://acme.org/mrn")
                .setValue("1234567");
        newPatient.setGender(Enumerations.AdministrativeGender.MALE);
        newPatient.setBirthDateElement(new DateType("2015-11-18"));
        newPatient.setLanguage("English");
        newPatient.setManagingOrganizationTarget(org);
        newPatient.getManagingOrganization().setReference("Organization/FOO");



        System.out.println("Created patient, newPatient:-->" + newPatient.toString());
        // Create a client
        FhirContext ctx = FhirContext.forR4();
        IGenericClient client = ctx.newRestfulGenericClient("http://localhost:8080/fhir");

        // Create the resource on the server
        MethodOutcome outcome = client
            .create()
            .resource(newPatient)
            .execute();

        // Log the ID that the server assigned
        IIdType id = outcome.getId();
        System.out.println("Created patient, got ID: " + id);
        MethodOutcome outcome1 = client
                .create()
                .resource(hcs)
                .execute();
        IIdType id1 = outcome1.getId();
        System.out.println("Created patient, got ID1: " + id1);
    }

}
