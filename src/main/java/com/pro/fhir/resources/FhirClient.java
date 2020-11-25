package com.pro.fhir.resources;

import java.util.Date;

import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.AdverseEvent;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.Appointment;
import org.hl7.fhir.r4.model.Appointment.AppointmentParticipantComponent;
import org.hl7.fhir.r4.model.Appointment.AppointmentStatus;
import org.hl7.fhir.r4.model.Appointment.ParticipationStatus;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.CarePlan.CarePlanIntent;
import org.hl7.fhir.r4.model.CarePlan.CarePlanStatus;
import org.hl7.fhir.r4.model.CareTeam;
import org.hl7.fhir.r4.model.CareTeam.CareTeamStatus;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.r4.model.ContactPoint.ContactPointUse;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.DiagnosticReport.DiagnosticReportStatus;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Encounter.EncounterStatus;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Narrative.NarrativeStatus;
import org.hl7.fhir.r4.model.NutritionOrder;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Observation.ObservationReferenceRangeComponent;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.hl7.fhir.r4.model.ServiceRequest.ServiceRequestIntent;
import org.hl7.fhir.r4.model.ServiceRequest.ServiceRequestStatus;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;

public class FhirClient {

	public static void patientResource() {

		Patient patient = new Patient();
		patient.setId("Patient-01");
		patient.getMeta().setVersionId("1").setLastUpdatedElement(new InstantType("2020-07-09T14:58:58.181+05:30"))
				.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Patient");
		patient.getText().setStatus(NarrativeStatus.GENERATED)
				.setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\">ABC, 41 year, Male</div>");
		patient.addIdentifier()
				.setType(new CodeableConcept(
						new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "MR", "Medical record number")))
				.setSystem("https://ndhm.in/SwasthID").setValue("1234");
		patient.addName().setText("ABC");
		patient.addTelecom().setSystem(ContactPointSystem.PHONE).setValue("+919818512600").setUse(ContactPointUse.HOME);
		patient.setGender(AdministrativeGender.MALE).setBirthDateElement(new DateType("1981-01-12"));

		FhirContext ctx = FhirContext.forR4();
		IGenericClient client = ctx.newRestfulGenericClient("http://localhost:8080/fhir");
		// Create the resource on the server
		MethodOutcome outcome = client.create().resource(patient).execute();
		// Log the ID that the server assigned
		IIdType id = outcome.getId();
		System.out.println("Created resource, got ID: " + id);

	}

	public static void carePlanResource() {

		CarePlan carePlan = new CarePlan();
		carePlan.setId("CarePlan-01");
		carePlan.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/CarePlan");
		carePlan.setStatus(CarePlanStatus.ACTIVE);
		carePlan.setIntent(CarePlanIntent.PLAN);
		carePlan.setSubject(new Reference().setReference("Patient/Patient-0").setDisplay("ABC"));
		FhirContext ctx = FhirContext.forR4();
		IGenericClient client = ctx.newRestfulGenericClient("http://localhost:8080/fhir");
		// Create the resource on the server
		MethodOutcome outcome = client.create().resource(carePlan).execute();
		// Log the ID that the server assigned
		IIdType id = outcome.getId();
		System.out.println("Created resource, got ID: " + id);
	}

	public static void observationResource() {

		Observation observation = new Observation();
		observation.setId("Observation-01");
		observation.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Observation");
		observation.setStatus(ObservationStatus.FINAL);
		observation.setCode(new CodeableConcept(
				new Coding("http://loinc.org", "35200-5", "Cholesterol [Moles/​volume] in Serum or Plasma"))
						.setText("Cholesterol"));
		observation.setValue(new Quantity().setValueElement(new DecimalType("6.3")).setCode("258813002")
				.setUnit("mmol/L").setSystem("http://snomed.info/sct"));
		observation.getReferenceRange().add(
				new ObservationReferenceRangeComponent().setHigh(new Quantity().setValueElement(new DecimalType("6.3"))
						.setCode("258813002").setUnit("mmol/L").setSystem("http://snomed.info/sct")));
		observation.setSubject(new Reference().setReference("Patient/Patient-01"));

		FhirContext ctx = FhirContext.forR4();
		IGenericClient client = ctx.newRestfulGenericClient("http://localhost:8080/fhir");
		// Create the resource on the server
		MethodOutcome outcome = client.create().resource(observation).execute();
		// Log the ID that the server assigned
		IIdType id = outcome.getId();
		System.out.println("Created resource, got ID: " + id);

	}

	public static void diagnosticReportLabRrsource() {

		DiagnosticReport diagnosticReportLab = new DiagnosticReport();
		diagnosticReportLab.setId("DiagnosticReport-01");
		diagnosticReportLab.getMeta().setVersionId("1")
				.setLastUpdatedElement(new InstantType("2020-07-09T15:32:26.605+05:30"))
				.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DiagnosticReportLab");
		diagnosticReportLab.setStatus(DiagnosticReportStatus.FINAL);
		diagnosticReportLab.setCode(
				new CodeableConcept(new Coding("http://loinc.org", "24331-1", "Lipid 1996 panel - Serum or Plasma")));
		diagnosticReportLab.getResultsInterpreter()
				.add(new Reference().setReference("Practitioner/Practitioner-01").setDisplay("Dr. DEF"));
		diagnosticReportLab.setConclusion("Elevated cholesterol/high density lipoprotein ratio");
		// diagnosticReportLab.addResult(new
		// Reference().setReference("Observation/Observation-cholesterol")).addResult(new
		// Reference().setReference("Observation/Observation-triglyceride"));

		diagnosticReportLab.addResult(new Reference().setReference("Observation/Observation-01"))
				.addResult(new Reference().setReference("Observation/Observation-triglyceride"));

		FhirContext ctx = FhirContext.forR4();
		IGenericClient client = ctx.newRestfulGenericClient("http://localhost:8080/fhir");
		// Create the resource on the server
		MethodOutcome outcome = client.create().resource(diagnosticReportLab).execute();
		// Log the ID that the server assigned
		IIdType id = outcome.getId();
		System.out.println("Created resource, got ID: " + id);
	}

	public static void practitionerResource() {

		Practitioner practitioner = new Practitioner();
		practitioner.setId("Practitioner-01");
		practitioner.getMeta().setVersionId("1").setLastUpdatedElement(new InstantType("2019-05-29T14:58:58.181+05:30"))
				.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Practitioner");
		practitioner.getText().setStatus(NarrativeStatus.GENERATED)
				.setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\">Dr. DEF, MD (Medicine)</div>");
		practitioner.addIdentifier()
				.setType(new CodeableConcept(
						new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "MD", "Medical License number")))
				.setSystem("https://ndhm.in/DigiDoc").setValue("7601003178999");
		practitioner.addName().setText("Dr. DEF");

		FhirContext ctx = FhirContext.forR4();
		IGenericClient client = ctx.newRestfulGenericClient("http://localhost:8080/fhir");
		// Create the resource on the server
		MethodOutcome outcome = client.create().resource(practitioner).execute();
		// Log the ID that the server assigned
		IIdType id = outcome.getId();
		System.out.println("Created resource, got ID: " + id);
	}

	public static void resourceAllergyIntolerance() {
		AllergyIntolerance allergyIntolerance = new AllergyIntolerance();
		allergyIntolerance.setId("AllergyIntolerance-01");
		allergyIntolerance.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/AllergyIntolerance");
		allergyIntolerance.getCode().addCoding(new Coding("http://snomed.info/sct", "227493005", "Cashew nuts"));
		allergyIntolerance.setPatient(new Reference().setReference("Patient/Patient-01"));
		allergyIntolerance.setClinicalStatus(new CodeableConcept(
				new Coding("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical", "active", "Active")));
		FhirContext ctx = FhirContext.forR4();
		IGenericClient client = ctx.newRestfulGenericClient("http://localhost:8080/fhir");
		// Create the resource on the server
		MethodOutcome outcome = client.create().resource(allergyIntolerance).execute();
		// Log the ID that the server assigned
		IIdType id = outcome.getId();
		System.out.println("Created diagnosticReportLab, got ID: " + id);
	}

	static void resourceAdverseEvent() {

		AdverseEvent adverseEvent = new AdverseEvent();
		adverseEvent.setId("AdverseEvent-01");
		adverseEvent.setSubject(new Reference().setReference("Patient/Patient-01"));
		// need to add more properties

		FhirContext ctx = FhirContext.forR4();
		IGenericClient client = ctx.newRestfulGenericClient("http://localhost:8080/fhir");
		// Create the resource on the server
		MethodOutcome outcome = client.create().resource(adverseEvent).execute();
		// Log the ID that the server assigned
		IIdType id = outcome.getId();
		System.out.println("Created diagnosticReportLab, got ID: " + id);

	}

	public static void resourceCareTeam() {

		CareTeam ct = new CareTeam();

		ct.addIdentifier().setType(new CodeableConcept(
				new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "MR", "Medical record number")));
		ct.setStatus(CareTeamStatus.ACTIVE);
		ct.addCategory(new CodeableConcept().setText("test"));
		ct.setName("Ranjeet kumar");
		// ct.setSubject(new Reference().setReference("CareTeam-01"));
		ct.setSubject(new Reference().setReference("Patient/Patient-01"));
		ct.setPeriod(new Period().setStart(new Date()));
		ct.addParticipant().addRole().setText("Text");

		FhirContext ctx = FhirContext.forR4();
		IGenericClient client = ctx.newRestfulGenericClient("http://localhost:8080/fhir");
		// Create the resource on the server
		MethodOutcome outcome = client.create().resource(ct).execute();
		// Log the ID that the server assigned
		IIdType id = outcome.getId();
		System.out.println("Created diagnosticReportLab, got ID: " + id);
	}
	
	
	
	
	public static void resourceNutritionOrder() {
		
		NutritionOrder nutritionOrder = new NutritionOrder();
		nutritionOrder.setId("NutritionOrder-01");
		
		//nutritionOrder.setSupplement(new N
	}
	
	
	
public static void resourceVisionPrecption() {
		
		NutritionOrder nutritionOrder = new NutritionOrder();
		nutritionOrder.setId("NutritionOrder-01");
		
		//nutritionOrder.setSupplement(new N
	}



public static void resourceGoal() {
	
	NutritionOrder nutritionOrder = new NutritionOrder();
	nutritionOrder.setId("NutritionOrder-01");
	
	//nutritionOrder.setSupplement(new N
}

public static void populateServiceRequestResource() {

	ServiceRequest serviceRequest = new ServiceRequest();
	serviceRequest.setId("ServiceRequest-01");
	serviceRequest.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/ServiceRequest");
	serviceRequest.setStatus(ServiceRequestStatus.ACTIVE);
	serviceRequest.setIntent(ServiceRequestIntent.ORIGINALORDER);
	serviceRequest.setCode(new CodeableConcept(new Coding("http://snomed.info/sct", "16254007", "Lipid Panel")));
	serviceRequest.setSubject(new Reference().setReference("Patient/Patient-01"));
	serviceRequest.setOccurrence(new DateTimeType("2020-07-08T09:33:27+07:00"));
	serviceRequest.setRequester(new Reference().setReference("Practitioner/Practitioner-01").setDisplay("Dr PQR"));
	

	FhirContext ctx = FhirContext.forR4();
	IGenericClient client = ctx.newRestfulGenericClient("http://localhost:8080/fhir");
	// Create the resource on the server
	MethodOutcome outcome = client.create().resource(serviceRequest).execute();
	// Log the ID that the server assigned
	IIdType id = outcome.getId();
	System.out.println("Created diagnosticReportLab, got ID: " + id);
	
	
}




public static void populateAppointmentResource(){

	Appointment appointment = new Appointment();
	appointment.setId("Appointment-01");
	appointment.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Appointment");
	appointment.setStatus(AppointmentStatus.BOOKED);
	appointment.getParticipant().add(new AppointmentParticipantComponent().setActor(new Reference().setReference("Patient/Patient-01")).
			setStatus(ParticipationStatus.ACCEPTED).setActor(new Reference().setReference("Practitioner/Practitioner-01")).setStatus(ParticipationStatus.ACCEPTED));
	appointment.setStartElement(new InstantType("2020-07-12T09:00:00Z"));
	appointment.setEndElement(new InstantType("2020-07-12T09:30:00Z"));
	appointment.addReasonReference(new Reference().setReference("Condition/Condition-01"));
	appointment.addBasedOn(new Reference().setReference("ServiceRequest/ServiceRequest-01"));
	
	
	FhirContext ctx = FhirContext.forR4();
	IGenericClient client = ctx.newRestfulGenericClient("http://localhost:8080/fhir");
	// Create the resource on the server
	MethodOutcome outcome = client.create().resource(appointment).execute();
	// Log the ID that the server assigned
	IIdType id = outcome.getId();
	System.out.println("Created diagnosticReportLab, got ID: " + id);

}






public static void populateEncounterResource()
{
	Encounter encounter = new Encounter();
	encounter.setId("Encounter-01");
	encounter.setStatus(EncounterStatus.FINISHED);
	encounter.getMeta().setLastUpdatedElement(new InstantType("2020-07-09T14:58:58.181+05:30")).addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Encounter");
	encounter.getIdentifier().add(new Identifier().setSystem("https://ndhm.in").setValue("S100"));
	encounter.setClass_(new Coding("http://terminology.hl7.org/CodeSystem/v3-ActCode", "IMP", "inpatient encounter"));
	encounter.setSubject(new Reference().setReference("Patient/Patient-01"));
	encounter.setPeriod(new Period().setStartElement(new DateTimeType("2020-04-20T15:32:26.605+05:30")).setEndElement(new DateTimeType("2020-05-01T15:32:26.605+05:30")));
	//encounter.setHospitalization(new EncounterHospitalizationComponent().
		//	setDischargeDisposition(new CodeableConcept(new Coding("http://terminology.hl7.org/CodeSystem/discharge-disposition","home", "Home")).setText("Discharged to Home Care")));
	

	FhirContext ctx = FhirContext.forR4();
	IGenericClient client = ctx.newRestfulGenericClient("http://localhost:8080/fhir");
	// Create the resource on the server
	MethodOutcome outcome = client.create().resource(encounter).execute();
	// Log the ID that the server assigned
	IIdType id = outcome.getId();
	System.out.println("Created diagnosticReportLab, got ID: " + id);

}
	public static void main(String[] args) {

		// patientResource();
		// carePlanResource();
		// observationResource();
		// practitionerResource();

		// diagnosticReportLabRrsource();

		// resourceAllergyIntolerance();

		// resourceAdverseEvent();

		// resourceCareTeam();
		
		//populateServiceRequestResource();
		
	//	populateAppointmentResource();
		
	  populateEncounterResource();

	}
}
