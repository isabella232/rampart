package org.apache.axis.phaserule;

import java.util.ArrayList;

import javax.xml.namespace.QName;

import junit.framework.TestCase;

import org.apache.axis.context.ConfigurationContext;
import org.apache.axis.context.MessageContext;
import org.apache.axis.description.HandlerDescription;
import org.apache.axis.description.PhaseRule;
import org.apache.axis.engine.AxisConfigurationImpl;
import org.apache.axis.engine.AxisFault;
import org.apache.axis.engine.Handler;
import org.apache.axis.engine.Phase;
import org.apache.axis.phaseresolver.PhaseHolder;

/*
 * Copyright 2004,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * 
 */

/**
 * Author : Deepal Jayasinghe
 * Date: May 20, 2005
 * Time: 2:57:21 PM
 */
public class SingleHandlerPhaseTest extends TestCase {

    public void testSingleHandler() {
        try {
            ArrayList phases = new ArrayList();
            Phase p1 = new Phase("PhaseA");
            phases.add(p1);
            Phase p2 = new Phase("PhaseB");
            phases.add(p2);

            MessageContext msg = new MessageContext(new ConfigurationContext(new AxisConfigurationImpl()));

            PhaseHolder ph = new PhaseHolder(phases);
            HandlerDescription hm = new HandlerDescription();
            hm.setClassName("org.apache.axis.phaserule.PhaseRuleHandlers");
            Handler h1 = new PhaseRuleHandlers();
            h1.init(hm);
            ((PhaseRuleHandlers) h1).setName(new QName("PhaseFirstHnadler"));
            hm.setHandler(h1);
            hm.setName(new QName("H1"));
            PhaseRule rule = new PhaseRule();
            rule.setPhaseName("PhaseA");
            rule.setPhaseFirst(true);
            rule.setPhaseLast(true);
            hm.setRules(rule);
            ph.addHandler(hm);

            HandlerDescription hm1 = new HandlerDescription();
            hm1.setClassName("org.apache.axis.phaserule.PhaseRuleHandlers");
            Handler h2 = new PhaseRuleHandlers();
            ((PhaseRuleHandlers) h2).setName(new QName("Second Handler"));
            h2.init(hm1);
            hm1.setHandler(h2);
            hm1.setName(new QName("H2"));
            PhaseRule rule1 = new PhaseRule();
            rule1.setPhaseName("PhaseA");
            rule1.setAfter("H1");
            hm1.setRules(rule1);
            ph.addHandler(hm1);
            p1.invoke(msg);
            fail("This should fail with : can only have one handler, since there is a " +
                       "handler with both phaseFirst and PhaseLast true ");
        } catch (AxisFault axisFault) {
            return;
        }
    }
}
