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
package net.fhirfactory.pegacorn.ladon.mdr.conduit.controller.aggregationservices.defaultstrategies.wholeresourcebased;

import net.fhirfactory.pegacorn.ladon.mdr.conduit.controller.aggregationservices.defaultstrategies.wholeresourcebased.common.WholeResourceBasedAggregationServiceBase;
import net.fhirfactory.pegacorn.ladon.model.virtualdb.mdr.ResourceSoTConduitActionResponse;
import net.fhirfactory.pegacorn.ladon.model.virtualdb.mdr.ResourceSoTConduitSearchResponseElement;
import net.fhirfactory.pegacorn.ladon.model.virtualdb.operations.VirtualDBActionTypeEnum;
import net.fhirfactory.pegacorn.ladon.model.virtualdb.operations.VirtualDBMethodOutcome;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Procedure;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ProcedureDefaultResourceBasedContentAggregationService extends WholeResourceBasedAggregationServiceBase {
    private static final Logger LOG = LoggerFactory.getLogger(ProcedureDefaultResourceBasedContentAggregationService.class);

    @Override
    protected Logger getLogger(){return(LOG);}

    @Override
    protected String getAggregationServiceName() {
        return ("ProcedureDefaultResourceBasedContentAggregationService");
    }

    @Override
    protected ResourceType getResourceType() {
        return (ResourceType.Procedure);
    }

    @Override
    protected Identifier getBestIdentifier(Resource resource) {
        if(resource != null){
            Procedure resourceSubclass = (Procedure)resource;
            Identifier bestIdentifier = getIdentifierPicker().getBestIdentifier(resourceSubclass.getIdentifier());
            return(bestIdentifier);
        }
        return(null);
    }

    @Override
    protected void addIdentifier(Resource resource, Identifier ridIdentifier) {
        if(resource == null){
            return;
        }
        Procedure resourceSubClass = (Procedure) resource;
        resourceSubClass.addIdentifier(ridIdentifier);
    }

    @Override
    protected List<Identifier> getIdentifiers(ResourceSoTConduitActionResponse actionResponse) {
        if(actionResponse == null){
            return(new ArrayList<>());
        }
        Procedure actionResponseResource = (Procedure) actionResponse.getResource();
        if(actionResponseResource.hasIdentifier()){
            return(actionResponseResource.getIdentifier());
        }
        return(new ArrayList<>());
    }

    @Override
    public VirtualDBMethodOutcome aggregateCreateResponseSet(List<ResourceSoTConduitActionResponse> responseSet) {
        VirtualDBMethodOutcome virtualDBMethodOutcome = defaultActionOutcomeAggregationService(VirtualDBActionTypeEnum.CREATE, responseSet);
        return(virtualDBMethodOutcome);
    }

    @Override
    public VirtualDBMethodOutcome aggregateGetResponseSet(List<ResourceSoTConduitActionResponse> responseSet) {
        VirtualDBMethodOutcome virtualDBMethodOutcome = defaultActionOutcomeAggregationService(VirtualDBActionTypeEnum.REVIEW, responseSet);
        return(virtualDBMethodOutcome);
    }

    @Override
    public VirtualDBMethodOutcome aggregateUpdateResponseSet(List<ResourceSoTConduitActionResponse> responseSet) {
        VirtualDBMethodOutcome virtualDBMethodOutcome = defaultActionOutcomeAggregationService(VirtualDBActionTypeEnum.UPDATE, responseSet);
        return(virtualDBMethodOutcome);
    }

    @Override
    public VirtualDBMethodOutcome aggregateDeleteResponseSet(List<ResourceSoTConduitActionResponse> responseSet) {
        VirtualDBMethodOutcome virtualDBMethodOutcome = defaultActionOutcomeAggregationService(VirtualDBActionTypeEnum.DELETE, responseSet);
        return(virtualDBMethodOutcome);
    }

    @Override
    public VirtualDBMethodOutcome aggregateSearchResultSet(List<ResourceSoTConduitSearchResponseElement> responseSet) {
        VirtualDBMethodOutcome virtualDBMethodOutcome = defaultSearchOutcomeAggregationService(responseSet);
        return(virtualDBMethodOutcome);
    }
}
