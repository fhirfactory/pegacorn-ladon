/*
 * Copyright (c) 2020 Mark A. Hunter
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
package net.fhirfactory.pegacorn.ladon.statespace.twinpathway.manifestor.common;

import net.fhirfactory.pegacorn.ladon.model.behaviours.BehaviourIdentifier;
import net.fhirfactory.pegacorn.ladon.statespace.stimuli.model.BehaviourCentricExclusiveFilterRulesInterface;
import net.fhirfactory.pegacorn.petasos.model.topics.TopicToken;
import net.fhirfactory.pegacorn.petasos.wup.archetypes.LadonStandardMOAWUP;

import java.util.List;
import java.util.Set;

abstract public class TwinManifestorBase extends LadonStandardMOAWUP {
    private BehaviourIdentifier behaviourIdentifier;
    private List<BehaviourCentricExclusiveFilterRulesInterface> behaviourFilterSet;

    public TwinManifestorBase() {
        super();
        this.behaviourIdentifier = specifyBehaviour();
    }

    abstract protected BehaviourIdentifier specifyBehaviour();

    @Override
    protected Set<TopicToken> specifySubscriptionTopics() {
        return null;
    }

    @Override
    protected String specifyWUPInstanceName() {
        String manifestorName = "TwinManifestorFor-"+specifyBehaviour().getName();
        return (manifestorName);
    }

    @Override
    protected String specifyWUPVersion() {
        return (specifyBehaviour().getVersion());
    }

    @Override
    protected String specifyWUPWorkshop() {
        return ("StateSpace");
    }

    @Override
    public void configure() throws Exception {

    }
}
