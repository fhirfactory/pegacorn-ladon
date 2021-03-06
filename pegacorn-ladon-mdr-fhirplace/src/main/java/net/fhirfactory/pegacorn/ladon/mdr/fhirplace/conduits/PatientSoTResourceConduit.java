/*
 * Copyright (c) 2020 Mark A. Hunter (ACT Health)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.fhirfactory.pegacorn.ladon.mdr.fhirplace.conduits;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Property;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.rest.api.MethodOutcome;
import net.fhirfactory.pegacorn.ladon.mdr.conduit.controller.PatientSoTConduitController;
import net.fhirfactory.pegacorn.ladon.mdr.fhirplace.accessor.FHIRPlaceBaseIndividualsMDRAccessor;
import net.fhirfactory.pegacorn.ladon.mdr.fhirplace.conduits.common.FHIRPlaceSoTConduitCommon;
import net.fhirfactory.pegacorn.ladon.model.virtualdb.businesskey.VirtualDBKeyManagement;
import net.fhirfactory.pegacorn.ladon.model.virtualdb.mdr.ResourceGradeEnum;
import net.fhirfactory.pegacorn.ladon.model.virtualdb.mdr.ResourceSoTConduitActionResponse;
import net.fhirfactory.pegacorn.ladon.model.virtualdb.mdr.ResourceSoTConduitSearchResponseElement;
import net.fhirfactory.pegacorn.ladon.model.virtualdb.mdr.SoTConduitGradeEnum;
import net.fhirfactory.pegacorn.ladon.model.virtualdb.searches.SearchNameEnum;
import net.fhirfactory.pegacorn.platform.restfulapi.PegacornInternalFHIRClientServices;

@ApplicationScoped
public class PatientSoTResourceConduit extends FHIRPlaceSoTConduitCommon {
    private static final Logger LOG = LoggerFactory.getLogger(PatientSoTResourceConduit.class);

    @Inject
    private PatientSoTConduitController conduitController;

    @Inject
    VirtualDBKeyManagement virtualDBKeyResolver;

    @Inject
    private FHIRPlaceBaseIndividualsMDRAccessor servicesAccessor;

    @Override
    protected Logger getLogger(){
        return(LOG);
    }

    @Override
    protected String specifySourceOfTruthEndpointSystemName() {
        return (getPegacornFHIRPlaceMDRComponentNames().getBaseIndividualsPegacornMDRSubsystem());
    }

    @Override
    protected Identifier getBestIdentifier(MethodOutcome outcome) {
        if(outcome == null){
            return(null);
        }
        Resource containedResource = (Resource)outcome.getResource();
        if(containedResource == null){
            return(null);
        }
        Patient actualResource = (Patient)containedResource;
        if(actualResource.hasIdentifier()){
            Identifier bestIdentifier = virtualDBKeyResolver.getBestIdentifier(actualResource.getIdentifier());
            return(bestIdentifier);
        }
        return(null);
    }

    @Override
    protected PegacornInternalFHIRClientServices specifySecureAccessor() {
        return (servicesAccessor);
    }

    @Override
    protected void registerWithSoTCConduitController() {
        conduitController.addResourceConduit(this);
    }

    /**
     * This is the CREATE (POST) Function for the Patient Resource --> persisting
     * the content within a FHIRPlace instance.
     *
     * This function, after invoking the default FHIRPlace writer/create function, adds the
     * CREATE_FULL_COVERAGE response - since, within the context of a Patient
     * resource, FHIRPlace is the sole persistence framework (as of Release 1.0.0).
     *
     * @param resourceToCreate The Patient (Resource) to be persisted
     * @return A Response/Outcome of the operation, including a copy of the Resource.
     */
    @Override
    public ResourceSoTConduitActionResponse createResource(Resource resourceToCreate) {
        LOG.debug(".createResource(): Entry, resourceToCreate --> {}", resourceToCreate);
        ResourceSoTConduitActionResponse outcome = standardCreateResource(resourceToCreate);
        outcome.setResponseResourceGrade(ResourceGradeEnum.LIMITED);
        outcome.setSoTGrade(SoTConduitGradeEnum.PARTIALLY_INFORMATIVE);
        LOG.debug(".createResource(): Exit, outcome --> {}", outcome);
        return(outcome);
    }

    /**
     * This is the REVIEW (GET) Function for the Patient Resource --> retrieving
     * the Patient resource from a FHIRPlace instance.
     *
     * This function, after invoking the default FHIRPlace read/get function, adds the
     * REVIEW_FULL_COVERAGE response - since, within the context of a Patient
     * resource, FHIRPlace is the sole persistence framework (as of Release 1.0.0).
     *
     * @param identifier The identifier of the Patient to be retrieved
     * @return A Response/Outcome of the operation, including a copy of the Resource (if found).
     */
    @Override
    public ResourceSoTConduitActionResponse getResourceViaIdentifier(Identifier identifier) {
        LOG.debug(".readResource(): Entry, identifier --> {}", identifier);
        ResourceSoTConduitActionResponse outcome = standardGetResourceViaIdentifier(ResourceType.Patient.toString(), identifier);
        outcome.setResponseResourceGrade(ResourceGradeEnum.LIMITED);
        outcome.setSoTGrade(SoTConduitGradeEnum.PARTIALLY_INFORMATIVE);
        LOG.debug(".readResource(): Exit, outcome --> {}", outcome);
        return(outcome);
    }

    /**
     * This is the REVIEW (GET) Function for the CareTeam Resource --> retrieving
     * the CareTeam resource from a FHIRPlace instance.
     *
     * This function, after invoking the default FHIRPlace read/get function, adds the
     * REVIEW_FULL_COVERAGE response - since, within the context of a CareTeam
     * resource, FHIRPlace is the sole persistence framework (as of Release 1.0.0).
     *
     * @param id The identifier of the CareTeam to be retrieved
     * @return A Response/Outcome of the operation, including a copy of the Resource (if found).
     */
    @Override
    public ResourceSoTConduitActionResponse reviewResource(IdType id) {
        LOG.debug(".readResource(): Entry, identifier --> {}", id);
        ResourceSoTConduitActionResponse outcome = standardReviewResource(Patient.class, id);
        outcome.setResponseResourceGrade(ResourceGradeEnum.THOROUGH);
        outcome.setSoTGrade(SoTConduitGradeEnum.AUTHORITATIVE);
        LOG.debug(".readResource(): Exit, outcome --> {}", outcome);
        return(outcome);
    }

    /**
     * This is the UPDATE (PUT) Function for the Patient Resource --> updating the
     * content within a FHIRPlace instance.
     *
     * This function, after invoking the default FHIRPlace update/put function, adds the
     * UPDATE_FULL_COVERAGE response - since, within the context of a Patient
     * resource, FHIRPlace is the sole persistence framework (as of Release 1.0.0).
     *
     * @param resourceToUpdate The Patient (Resource) to be updated
     * @return A Response/Outcome of the operation, including a copy of the Resource.
     */
    @Override
    public ResourceSoTConduitActionResponse updateResource(Resource resourceToUpdate) {
        LOG.debug(".updateResource(): Entry, resourceToUpdate --> {}", resourceToUpdate);
        ResourceSoTConduitActionResponse outcome = standardUpdateResource(resourceToUpdate);
        outcome.setResponseResourceGrade(ResourceGradeEnum.LIMITED);
        outcome.setSoTGrade(SoTConduitGradeEnum.PARTIALLY_INFORMATIVE);
        LOG.debug(".updateResource(): Exit, outcome --> {}", outcome);
        return(outcome);
    }

    @Override
    public ResourceSoTConduitActionResponse deleteResource(Resource resourceToDelete) {
        return null;
    }

    @Override
    public List<ResourceSoTConduitSearchResponseElement> searchSourceOfTruthUsingCriteria(ResourceType resourceType, SearchNameEnum searchName, Map<Property, Serializable> parameterSet) {
        return null;
    }

    @Override
    public boolean supportiveOfSearch(SearchNameEnum searchName) {
        return false;
    }

    @Override
    public boolean supportsDirectCreateAction(Resource wholeResource) {
        return true;
    }

    @Override
    public boolean supportsDirectUpdateAction(Resource wholeResource) {
        return true;
    }

    @Override
    public boolean supportsDirectDeleteAction(Resource wholeResource) {
        return false;
    }
    
    @Override
    protected ResourceType specifyResourceType() {
        return (ResourceType.Patient);
    }

    //
    // Supported Searches
    //

}
