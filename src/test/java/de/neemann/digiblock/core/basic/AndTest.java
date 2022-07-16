/*
 * Copyright (c) 2016 Helmut Neemann
 * Use of this source code is governed by the GPL v3 license
 * that can be found in the LICENSE file.
 */
package de.neemann.digiblock.core.basic;

import de.neemann.digiblock.TestExecuter;
import de.neemann.digiblock.core.Model;
import de.neemann.digiblock.core.ObservableValue;
import de.neemann.digiblock.core.element.ElementAttributes;
import junit.framework.TestCase;

import static de.neemann.digiblock.core.ObservableValues.ovs;

/**
 */
public class AndTest extends TestCase {

    public void testAnd() throws Exception {
        ObservableValue a = new ObservableValue("a", 1);
        ObservableValue b = new ObservableValue("b", 1);

        Model model = new Model();
        FanIn out = model.add(new And(new ElementAttributes().setBits(1)));
        out.setInputs(ovs(a, b));

        TestExecuter sc = new TestExecuter(model).setInputs(a, b).setOutputs(out.getOutputs());
        sc.check(0, 0, 0);
        sc.check(1, 0, 0);
        sc.check(0, 1, 0);
        sc.check(1, 1, 1);
        sc.check(1, 0, 0);
        sc.check(0, 1, 0);
    }

    public void testAnd64() throws Exception {
        ObservableValue a = new ObservableValue("a", 64);
        ObservableValue b = new ObservableValue("b", 64);

        Model model = new Model();
        FanIn out = model.add(new And(new ElementAttributes().setBits(64)));
        out.setInputs(ovs(a, b));

        TestExecuter sc = new TestExecuter(model).setInputs(a, b).setOutputs(out.getOutputs());
        sc.check(0xff00000000000000L, 0x2200000000000000L, 0x2200000000000000L);
    }
}
