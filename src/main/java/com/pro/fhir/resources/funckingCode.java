package com.pro.fhir.resources;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.JsonParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.FamilyMemberHistory;
import org.hl7.fhir.r4.model.HealthcareService;
import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.instance.model.api.IIdType;

public class funckingCode {

    /*
     * This class contains hints for the tasks outlined in TestApplication
     */

    public static void main(String[] args) {

        System.out.println("Cre");
        step3_create_patient();
        step2_search_for_patients_named_test();
        //step1_read_a_resource();
         createResource();

        familyMemberhistory();
    }

    private static void createResource() {
        // TODO Auto-generated method stub

    }

    private static void familyMemberhistory() {
        FamilyMemberHistory fmh=new FamilyMemberHistory();
        //fmh.
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